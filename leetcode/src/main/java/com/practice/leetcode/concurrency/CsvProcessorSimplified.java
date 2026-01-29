package com.practice.leetcode.concurrency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Simplified CSV Processor - Multiple Approaches
 * 
 * So sánh các cách đọc và xử lý file:
 * 1. Files.readAllLines() + parallelStream() - Đơn giản nhất
 * 2. Files.lines() + parallel() - Memory efficient (streaming)
 * 3. Batch with Stream - Kiểm soát batch size
 * 
 * Run: java CsvProcessorSimplified
 */
public class CsvProcessorSimplified {
    
    private final ConcurrentHashMap<Long, String> results = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(0);
    
    public static void main(String[] args) throws Exception {
        String testFile = "/tmp/test_data.csv";
        generateTestFile(testFile, 10000);
        
        CsvProcessorSimplified processor = new CsvProcessorSimplified();
        
        System.out.println("=== Approach 1: readAllLines + parallelStream ===");
        processor.approach1_ReadAllParallel(testFile);
        
        System.out.println("\n=== Approach 2: Files.lines (streaming) + parallel ===");
        processor.approach2_StreamParallel(testFile);
        
        System.out.println("\n=== Approach 3: Batch Processing ===");
        processor.approach3_BatchProcessing(testFile, 500);
    }
    
    // =========================================================================
    // APPROACH 1: Đơn giản nhất - Đọc hết vào memory rồi parallel process
    // =========================================================================
    
    /**
     * ✅ Pros: Code cực kỳ đơn giản
     * ❌ Cons: Đọc toàn bộ file vào memory trước
     * 📌 Use when: File nhỏ (< vài trăm MB)
     */
    public void approach1_ReadAllParallel(String filePath) throws IOException {
        long start = System.currentTimeMillis();
        results.clear();
        counter.set(0);
        
        // Đọc tất cả lines vào memory
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        
        // Process song song (skip header)
        lines.stream()
            .skip(1)  // Skip header
            .parallel()  // ← Magic! Tự chia cho ForkJoinPool
            .forEach(line -> {
                long id = counter.incrementAndGet();
                String result = processLine(id, line);
                results.put(id, result);
            });
        
        printStats(start);
    }
    
    // =========================================================================
    // APPROACH 2: Streaming - Memory efficient
    // =========================================================================
    
    /**
     * ✅ Pros: Không load toàn bộ file vào memory, stream từng line
     * ✅ Pros: Vẫn parallel được
     * 📌 Use when: File lớn, muốn tiết kiệm memory
     */
    public void approach2_StreamParallel(String filePath) throws IOException {
        long start = System.currentTimeMillis();
        results.clear();
        counter.set(0);
        
        // Files.lines() trả về Stream<String> - lazy, không load hết
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.skip(1)  // Skip header
                .parallel()  // Parallel processing
                .forEach(line -> {
                    long id = counter.incrementAndGet();
                    String result = processLine(id, line);
                    results.put(id, result);
                });
        }
        
        printStats(start);
    }
    
    // =========================================================================
    // APPROACH 3: Batch Processing với Stream
    // =========================================================================
    
    /**
     * ✅ Pros: Kiểm soát batch size, memory efficient
     * ✅ Pros: Có thể thêm logic xử lý theo batch
     * 📌 Use when: Cần control batch size, DB operations, etc.
     */
    public void approach3_BatchProcessing(String filePath, int batchSize) throws IOException {
        long start = System.currentTimeMillis();
        results.clear();
        counter.set(0);
        
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        lines = lines.subList(1, lines.size());  // Remove header
        
        // Chia thành batches và process
        int totalLines = lines.size();
        List<String> finalLines = lines;  // Effectively final for lambda
        
        IntStream.range(0, (totalLines + batchSize - 1) / batchSize)
            .parallel()  // Process batches in parallel
            .forEach(batchIndex -> {
                int fromIndex = batchIndex * batchSize;
                int toIndex = Math.min(fromIndex + batchSize, totalLines);
                List<String> batch = finalLines.subList(fromIndex, toIndex);
                
                // Process batch (có thể sequential trong batch)
                for (String line : batch) {
                    long id = counter.incrementAndGet();
                    String result = processLine(id, line);
                    results.put(id, result);
                }
            });
        
        printStats(start);
    }
    
    // =========================================================================
    // HELPER METHODS
    // =========================================================================
    private String processLine(long id, String line) {
    
        // Simulate some work
        String[] fields = line.split(",");
        return "Processed: " + (fields.length > 0 ? fields[0].toUpperCase() : "");
    }
    
    private void printStats(long startTime) {
        System.out.println("Processed: " + results.size() + " rows");
        System.out.println("Time: " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("First 3 results:");
        results.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .limit(3)
            .forEach(e -> System.out.println("  " + e.getKey() + ": " + e.getValue()));
    }
    
    private static void generateTestFile(String path, int rows) throws IOException {
        Path file = Paths.get(path);
        StringBuilder content = new StringBuilder("name,email,age\n");
        for (int i = 1; i <= rows; i++) {
            content.append(String.format("user_%d,user%d@example.com,%d\n", i, i, 20 + (i % 50)));
        }
        Files.write(file, content.toString().getBytes());
        System.out.println("Generated: " + path + " (" + rows + " rows)\n");
    }
}
