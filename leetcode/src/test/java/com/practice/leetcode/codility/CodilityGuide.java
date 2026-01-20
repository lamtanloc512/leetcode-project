package com.practice.leetcode.codility;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║                    CODILITY INTERVIEW PROBLEMS GUIDE                         ║
 * ║              Hướng dẫn chi tiết các bài toán Codility phổ biến               ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Tài liệu này tổng hợp các bài toán Codility thường gặp trong phỏng vấn coding,
 * được tổ chức theo các chủ đề (Lessons) của Codility.
 * 
 * ┌─────────────────────────────────────────────────────────────────────────────┐
 * │                           CẤU TRÚC CODILITY LESSONS                         │
 * ├─────────────────────────────────────────────────────────────────────────────┤
 * │ Lesson 1:  Iterations (Vòng lặp)                                            │
 * │ Lesson 2:  Arrays (Mảng)                                                    │
 * │ Lesson 3:  Time Complexity (Độ phức tạp thời gian)                          │
 * │ Lesson 4:  Counting Elements (Đếm phần tử)                                  │
 * │ Lesson 5:  Prefix Sums (Tổng tiền tố)                                       │
 * │ Lesson 6:  Sorting (Sắp xếp)                                                │
 * │ Lesson 7:  Stacks and Queues (Ngăn xếp và Hàng đợi)                         │
 * │ Lesson 8:  Leader (Phần tử chủ đạo)                                         │
 * │ Lesson 9:  Maximum Slice Problem (Bài toán lát cắt lớn nhất)                │
 * │ Lesson 10: Prime and Composite Numbers (Số nguyên tố và hợp số)             │
 * │ Lesson 11: Sieve of Eratosthenes (Sàng Eratosthenes)                        │
 * │ Lesson 12: Euclidean Algorithm (Thuật toán Euclid)                          │
 * │ Lesson 13: Fibonacci Numbers (Số Fibonacci)                                 │
 * │ Lesson 14: Binary Search Algorithm (Tìm kiếm nhị phân)                      │
 * │ Lesson 15: Caterpillar Method (Phương pháp sâu bướm)                        │
 * │ Lesson 16: Greedy Algorithms (Thuật toán tham lam)                          │
 * │ Lesson 17: Dynamic Programming (Quy hoạch động)                             │
 * └─────────────────────────────────────────────────────────────────────────────┘
 * 
 * Mỗi class trong package này sẽ chứa các bài toán tương ứng với từng lesson.
 * 
 * @author Practice Guide
 * @see L1_Iterations - Bài toán về vòng lặp
 * @see L2_Arrays - Bài toán về mảng
 * @see L3_TimeComplexity - Bài toán về độ phức tạp thời gian
 * @see L4_CountingElements - Bài toán về đếm phần tử
 * @see L5_PrefixSums - Bài toán về tổng tiền tố
 * @see L6_Sorting - Bài toán về sắp xếp
 * @see L7_StacksQueues - Bài toán về stack và queue
 * @see L8_Leader - Bài toán về phần tử chủ đạo
 * @see L9_MaxSlice - Bài toán về lát cắt lớn nhất
 * @see L10_PrimeComposite - Bài toán về số nguyên tố
 * @see L15_CaterpillarMethod - Bài toán về phương pháp sâu bướm
 */
public class CodilityGuide {
    
    /**
     * ╔═══════════════════════════════════════════════════════════════════════╗
     * ║             MỨC ĐỘ KHÓ VÀ TẦN SUẤT XUẤT HIỆN TRONG PHỎNG VẤN          ║
     * ╚═══════════════════════════════════════════════════════════════════════╝
     * 
     * ┌──────────────────────────────────────────────────────────────────────┐
     * │ BÀI TOÁN               │ MỨC ĐỘ   │ TẦN SUẤT │ ĐỘ QUAN TRỌNG         │
     * ├──────────────────────────────────────────────────────────────────────┤
     * │ BinaryGap              │ ★☆☆      │ Cao      │ Warmup cơ bản        │
     * │ CyclicRotation         │ ★☆☆      │ Cao      │ Hiểu array manipulation│
     * │ OddOccurrencesInArray  │ ★☆☆      │ Rất cao  │ XOR bit manipulation │
     * │ FrogJmp                │ ★☆☆      │ Cao      │ Toán học cơ bản      │
     * │ PermMissingElem        │ ★☆☆      │ Rất cao  │ Công thức tổng       │
     * │ TapeEquilibrium        │ ★★☆      │ Cao      │ Prefix sum đơn giản  │
     * │ FrogRiverOne           │ ★★☆      │ Cao      │ Set/Hash tracking    │
     * │ PermCheck              │ ★★☆      │ Cao      │ Kiểm tra tính hợp lệ │
     * │ MissingInteger         │ ★★☆      │ Rất cao  │ Phổ biến nhất!       │
     * │ MaxCounters            │ ★★★      │ Cao      │ Lazy propagation     │
     * │ PassingCars            │ ★★☆      │ Trung bình│ Counting pairs      │
     * │ GenomicRangeQuery      │ ★★★      │ Trung bình│ Prefix sum 2D       │
     * │ MinAvgTwoSlice         │ ★★★      │ Trung bình│ Math insight        │
     * │ CountDiv               │ ★★☆      │ Cao      │ Toán học             │
     * │ Distinct               │ ★☆☆      │ Cao      │ Set cơ bản           │
     * │ Triangle               │ ★★☆      │ Cao      │ Sorting + logic      │
     * │ MaxProductOfThree      │ ★★☆      │ Cao      │ Edge cases           │
     * │ NumberOfDiscIntersections│ ★★★    │ Trung bình│ Tricky intervals    │
     * │ Brackets               │ ★★☆      │ Rất cao  │ Stack cơ bản         │
     * │ Fish                   │ ★★★      │ Cao      │ Stack simulation     │
     * │ Nesting                │ ★★☆      │ Cao      │ Stack đơn giản       │
     * │ StoneWall              │ ★★★      │ Trung bình│ Stack + đếm         │
     * │ Dominator              │ ★★☆      │ Cao      │ Boyer-Moore voting   │
     * │ EquiLeader             │ ★★★      │ Trung bình│ Leader + prefix     │
     * │ MaxProfit              │ ★★☆      │ Rất cao  │ DP cơ bản, Kadane    │
     * │ MaxSliceSum            │ ★★☆      │ Rất cao  │ Kadane's algorithm   │
     * │ MaxDoubleSliceSum      │ ★★★      │ Trung bình│ Kadane nâng cao     │
     * │ CountFactors           │ ★★☆      │ Cao      │ Sqrt optimization    │
     * │ MinPerimeterRectangle  │ ★★☆      │ Trung bình│ Factor + optimize   │
     * │ Peaks                  │ ★★★      │ Cao      │ Binary search/Greedy │
     * │ Flags                  │ ★★★      │ Trung bình│ Binary search       │
     * └──────────────────────────────────────────────────────────────────────┘
     */
    
    public static void main(String[] args) {
        System.out.println("Codility Interview Problems Guide");
        System.out.println("Xem các class trong package này để học từng bài toán.");
    }
}
