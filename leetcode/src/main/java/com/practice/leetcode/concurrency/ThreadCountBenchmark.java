package com.practice.leetcode.concurrency;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.LongStream;

/**
 * Benchmark: Thread Count vs Performance
 * 
 * Test CPU-bound task với số threads khác nhau để chứng minh:
 * - threads = cores là tối ưu
 * - threads > cores không nhanh hơn (có thể chậm hơn)
 */
public class ThreadCountBenchmark {
    
    private static final int TASK_COUNT = 1000;
    private static final int WORK_INTENSITY = 500000;  // Tính toán nặng
    
    public static void main(String[] args) {
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println("=== Thread Count Benchmark ===");
        System.out.println("Logical Processors: " + processors);
        System.out.println("Tasks: " + TASK_COUNT);
        System.out.println("Work intensity: " + WORK_INTENSITY + " iterations per task\n");
        
        // Test với số threads khác nhau
        int[] threadCounts = {1, 2, processors / 2, processors - 1, processors, 
                              processors + 1, processors * 2, processors * 4};
        
        System.out.println("┌──────────┬──────────┬──────────────────────────────────┐");
        System.out.println("│ Threads  │ Time(ms) │ Speed                            │");
        System.out.println("├──────────┼──────────┼──────────────────────────────────┤");
        
        long baselineTime = 0;
        
        for (int threads : threadCounts) {
            if (threads <= 0) continue;
            
            // Warm up
            runBenchmark(threads);
            
            // Actual benchmark (average of 3 runs)
            long total = 0;
            for (int i = 0; i < 3; i++) {
                total += runBenchmark(threads);
            }
            long avgTime = total / 3;
            
            if (threads == 1) {
                baselineTime = avgTime;
            }
            
            double speedup = (double) baselineTime / avgTime;
            String bar = generateBar(speedup, processors);
            
            System.out.printf("│ %8d │ %8d │ %.2fx %s│%n", 
                threads, avgTime, speedup, bar);
        }
        
        System.out.println("└──────────┴──────────┴──────────────────────────────────┘");
        System.out.println("\n* Optimal = threads ≈ " + processors + " (số logical processors)");
    }
    
    /**
     * CPU-bound task: Heavy computation
     */
    private static long runBenchmark(int threads) {
        ForkJoinPool pool = new ForkJoinPool(threads);
        
        long start = System.currentTimeMillis();
        
        try {
            pool.submit(() -> 
                LongStream.range(0, TASK_COUNT)
                    .parallel()
                    .forEach(i -> doCpuIntensiveWork())
            ).get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
        
        return System.currentTimeMillis() - start;
    }
    
    /**
     * Simulates heavy CPU work (không có I/O)
     */
    private static void doCpuIntensiveWork() {
        // Heavy computation: calculate prime-like operations
        double result = 0;
        for (int i = 0; i < WORK_INTENSITY; i++) {
            result += Math.sin(i) * Math.cos(i) * Math.tan(i % 100 + 1);
            result = Math.sqrt(Math.abs(result) + 1);
        }
        // Prevent JIT from optimizing away
        if (result == Double.MAX_VALUE) {
            System.out.println("Never happens");
        }
    }
    
    private static String generateBar(double speedup, int maxProcessors) {
        int barLength = (int) (speedup * 2);
        barLength = Math.min(barLength, 25);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < barLength; i++) {
            bar.append("█");
        }
        // Pad to fixed width
        while (bar.length() < 25) {
            bar.append(" ");
        }
        return bar.toString();
    }
}
