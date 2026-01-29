package com.practice.leetcode.concurrency;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Java Pass-by-Value vs Pass-by-Reference Explanation
 * 
 * ⚠️ QUAN TRỌNG: Java LUÔN là Pass-by-Value!
 * Nhưng với Objects, value đó là REFERENCE (địa chỉ memory).
 * 
 * Run: java PassByValueDemo
 */
public class PassByValueDemo {

    public static void main(String[] args) {
        System.out.println("=== Java Pass-by-Value Demo ===\n");
        
        example1_PrimitiveTypes();
        example2_ObjectReferences();
        example3_AtomicBooleanInThread();
        example4_WhyAtomicWorks();
        example5_CommonMistake();
    }

    // =========================================================================
    // EXAMPLE 1: Primitive Types - Không thể modify
    // =========================================================================
    
    static void example1_PrimitiveTypes() {
        System.out.println("--- Example 1: Primitive Types ---");
        
        int x = 10;
        modifyPrimitive(x);
        System.out.println("After method call: x = " + x);  // Vẫn là 10!
        
        System.out.println();
    }
    
    static void modifyPrimitive(int value) {
        value = 100;  // Chỉ modify bản copy, không ảnh hưởng original
        System.out.println("Inside method: value = " + value);
    }
    
    /*
     * GIẢI THÍCH:
     * 
     *    main()                    modifyPrimitive()
     *    ┌─────────┐              ┌─────────┐
     *    │  x: 10  │  ──copy──▶   │ value:10│ → 100
     *    └─────────┘              └─────────┘
     *          │                        │
     *          │                        └── Chỉ là bản copy!
     *          └── Original không đổi
     */

    // =========================================================================
    // EXAMPLE 2: Object References - Có thể modify STATE, không thể modify REF
    // =========================================================================
    
    static void example2_ObjectReferences() {
        System.out.println("--- Example 2: Object References ---");
        
        StringBuilder sb = new StringBuilder("Hello");
        modifyObject(sb);
        System.out.println("After method call: sb = " + sb);  // "Hello World"
        
        replaceObject(sb);
        System.out.println("After replace: sb = " + sb);  // Vẫn "Hello World"!
        
        System.out.println();
    }
    
    static void modifyObject(StringBuilder builder) {
        builder.append(" World");  // ✅ Modify state của object → Thành công!
    }
    
    static void replaceObject(StringBuilder builder) {
        builder = new StringBuilder("New Object");  // ❌ Thay reference → Không thành công!
    }
    
    /*
     * GIẢI THÍCH:
     * 
     *    main()                         modifyObject()
     *    ┌─────────┐                   ┌─────────┐
     *    │ sb: 0x100│  ──copy ref──▶   │builder:0x100│
     *    └─────────┘                   └─────────┘
     *         │                              │
     *         ▼                              ▼
     *    ┌──────────────────────────────────────┐
     *    │  StringBuilder Object @ 0x100        │
     *    │  content: "Hello" → "Hello World"    │  ← Cùng trỏ tới 1 object
     *    └──────────────────────────────────────┘
     *    
     *    replaceObject():
     *    ┌─────────┐                   ┌─────────┐
     *    │ sb: 0x100│  ──copy ref──▶   │builder:0x100│ → 0x200 (new object)
     *    └─────────┘                   └─────────┘
     *         │                              │
     *         ▼                              ▼
     *    Object @ 0x100                Object @ 0x200
     *    (original, unchanged)         (new, only local)
     */

    // =========================================================================
    // EXAMPLE 3: AtomicBoolean trong Thread - TẠI SAO HOẠT ĐỘNG?
    // =========================================================================
    
    static void example3_AtomicBooleanInThread() {
        System.out.println("--- Example 3: AtomicBoolean in Thread ---");
        
        // Đây là cách code trong CsvBatchProcessor:
        AtomicBoolean readerDone = new AtomicBoolean(false);
        
        Thread thread = createWorkerThread(readerDone);
        thread.start();
        
        // Wait for thread to modify
        try { Thread.sleep(100); } catch (Exception e) {}
        
        System.out.println("Main thread sees: readerDone = " + readerDone.get());
        // Output: true! Thread đã modify thành công
        
        System.out.println();
    }
    
    static Thread createWorkerThread(AtomicBoolean done) {
        // Parameter 'done' là COPY của reference, nhưng trỏ tới CÙNG object
        return new Thread(() -> {
            System.out.println("Worker: Setting done to true");
            done.set(true);  // Modify STATE của object, không phải replace reference
        });
    }
    
    /*
     * GIẢI THÍCH TẠI SAO HOẠT ĐỘNG:
     * 
     *    main()                    createWorkerThread()         Thread lambda
     *    ┌──────────────┐         ┌──────────────┐            ┌──────────────┐
     *    │readerDone:0x100│──copy▶│ done: 0x100  │───capture▶ │(uses 0x100)  │
     *    └──────────────┘         └──────────────┘            └──────────────┘
     *           │                        │                          │
     *           └────────────────────────┴──────────────────────────┘
     *                                    │
     *                                    ▼
     *                    ┌───────────────────────────────┐
     *                    │  AtomicBoolean Object @ 0x100 │
     *                    │  value: false → true          │
     *                    └───────────────────────────────┘
     *                              ↑
     *                    Tất cả cùng trỏ tới object này!
     *                    Thread modify state → Main thấy thay đổi
     */

    // =========================================================================
    // EXAMPLE 4: Tại sao dùng AtomicBoolean thay vì boolean?
    // =========================================================================
    
    static void example4_WhyAtomicWorks() {
        System.out.println("--- Example 4: Why Atomic? ---");
        
        /*
         * ❌ CÁI NÀY KHÔNG THỂ LÀM ĐƯỢC:
         * 
         *    boolean readerDone = false;
         *    Thread t = new Thread(() -> {
         *        readerDone = true;  // ERROR: Variable must be final or effectively final
         *    });
         *    
         * Lý do: Lambda capture variables phải là final hoặc effectively final.
         *        Bạn không thể reassign primitive trong lambda.
         * 
         * ✅ GIẢI PHÁP: Dùng wrapper object
         * 
         *    Cách 1: AtomicBoolean (recommended - thread-safe)
         *    AtomicBoolean done = new AtomicBoolean(false);
         *    Thread t = new Thread(() -> done.set(true));
         *    
         *    Cách 2: boolean[] array (hack, not recommended)
         *    boolean[] done = {false};
         *    Thread t = new Thread(() -> done[0] = true);
         *    
         *    Cách 3: AtomicReference (for any object)
         *    AtomicReference<String> status = new AtomicReference<>("running");
         *    Thread t = new Thread(() -> status.set("done"));
         */
        
        // Demo với AtomicBoolean
        AtomicBoolean flag = new AtomicBoolean(false);
        
        new Thread(() -> {
            try { Thread.sleep(50); } catch (Exception e) {}
            flag.set(true);  // ✅ Modify state, không reassign reference
        }).start();
        
        // Wait and check
        while (!flag.get()) {
            // Spin wait (trong thực tế nên dùng wait/notify)
        }
        System.out.println("Flag changed to: " + flag.get());
        
        System.out.println();
    }

    // =========================================================================
    // EXAMPLE 5: Common Mistake - Reassign reference không work
    // =========================================================================
    
    static void example5_CommonMistake() {
        System.out.println("--- Example 5: Common Mistake ---");
        
        String status = "initial";
        // changeString(status);  // Không thể thay đổi status
        // System.out.println(status);  // Vẫn là "initial"
        
        // Với StringBuilder thì được
        StringBuilder sbStatus = new StringBuilder("initial");
        changeStringBuilder(sbStatus);
        System.out.println("StringBuilder: " + sbStatus);  // "modified"
        
        System.out.println();
    }
    
    static void changeStringBuilder(StringBuilder sb) {
        sb.setLength(0);
        sb.append("modified");  // Modify content, không replace reference
    }
    
    /*
     * =========================================================================
     * TÓM TẮT
     * =========================================================================
     * 
     * 1. Java LUÔN là pass-by-value
     *    - Primitives: copy giá trị → không thể modify original
     *    - Objects: copy reference → có thể modify STATE của object
     * 
     * 2. Khi truyền object vào method/thread:
     *    ✅ Có thể: Gọi methods để thay đổi state (obj.set(), list.add(), etc.)
     *    ❌ Không thể: Reassign reference (obj = new Object())
     * 
     * 3. Trong multi-threading:
     *    - Dùng AtomicBoolean/AtomicInteger để share state giữa threads
     *    - Reference được copy, nhưng tất cả threads thao tác cùng 1 object
     *    - Atomic classes còn đảm bảo thread-safety (visibility + atomicity)
     * 
     * 4. Lambda restrictions:
     *    - Variables captured by lambda phải là final/effectively final
     *    - Không thể reassign, nhưng có thể modify state của object
     *    - → Dùng wrapper objects (Atomic*, List, etc.) để workaround
     */
}
