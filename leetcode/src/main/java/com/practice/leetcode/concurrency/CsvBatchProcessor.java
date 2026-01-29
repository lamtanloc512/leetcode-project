package com.practice.leetcode.concurrency;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * CSV Batch Processor - Multiple Concurrency Approaches
 * 
 * Demonstrates 3 different ways to process batches concurrently:
 * 1. parallelStream() - Simplest approach
 * 2. invokeAll() - More control with timeout
 * 3. CompletableFuture - Full async control
 * 
 * Run: java CsvBatchProcessor
 */
public class CsvBatchProcessor {
    
    private final int batchSize;
    private final int workerThreads;
    private final ExecutorService executor;
    
    // Thread-safe data structures
    private final ConcurrentMap<Long, ProcessResult> results = new ConcurrentHashMap<>();
    private final AtomicLong rowIdGenerator = new AtomicLong(0);
    private final AtomicInteger processedCount = new AtomicInteger(0);
    
    public CsvBatchProcessor(int batchSize, int workerThreads) {
        this.batchSize = batchSize;
        this.workerThreads = workerThreads;
        this.executor = new ThreadPoolExecutor(
            workerThreads, workerThreads,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
    
    // =========================================================================
    // APPROACH 1: parallelStream() - Simplest
    // =========================================================================
    
    /**
     * ✅ Simplest approach using Java 8 parallel streams
     * 
     * Pros: Clean, concise, no boilerplate
     * Cons: Less control over thread pool, harder to handle exceptions
     */
    public void processBatchWithParallelStream(List<CsvRow> batch) {
        batch.parallelStream()
            .map(this::processRow)
            .forEach(result -> {
                results.put(result.rowId, result);
                processedCount.incrementAndGet();
            });
    }
    
    // =========================================================================
    // APPROACH 2: invokeAll() - Clean with timeout control
    // =========================================================================
    
    /**
     * ✅ Using ExecutorService.invokeAll() for batch processing
     * 
     * Pros: Clean API, timeout support, all tasks complete together
     * Cons: Blocks until all complete
     */
    public void processBatchWithInvokeAll(List<CsvRow> batch) throws Exception {
        // Convert rows to Callable tasks
        List<Callable<ProcessResult>> tasks = batch.stream()
            .map(row -> (Callable<ProcessResult>) () -> processRow(row))
            .collect(Collectors.toList());
        
        // Execute all with timeout
        List<Future<ProcessResult>> futures = executor.invokeAll(tasks, 30, TimeUnit.SECONDS);
        
        // Collect results
        for (Future<ProcessResult> future : futures) {
            if (!future.isCancelled()) {
                ProcessResult result = future.get();
                results.put(result.rowId, result);
                processedCount.incrementAndGet();
            }
        }
    }
    
    // =========================================================================
    // APPROACH 3: CompletableFuture - Full async control (Refactored)
    // =========================================================================
    
    /**
     * ✅ Using CompletableFuture with helper methods for cleaner code
     * 
     * Pros: Non-blocking, composable, exception handling
     * Cons: More verbose, need to manage futures
     */
    public void processBatchWithCompletableFuture(List<CsvRow> batch) {
        // Create futures for all rows
        List<CompletableFuture<ProcessResult>> futures = submitAllRows(batch);
        
        // Wait for all and collect results
        collectResults(futures);
    }
    
    private List<CompletableFuture<ProcessResult>> submitAllRows(List<CsvRow> batch) {
        return batch.stream()
            .map(row -> CompletableFuture
                .supplyAsync(() -> processRow(row), executor)
                .exceptionally(ex -> errorResult(row.id, ex)))
            .collect(Collectors.toList());
    }
    
    private void collectResults(List<CompletableFuture<ProcessResult>> futures) {
        CompletableFuture.allOf(toArray(futures))
            .thenRun(() -> futures.forEach(f -> {
                ProcessResult result = f.join();
                results.put(result.rowId, result);
                processedCount.incrementAndGet();
            }))
            .join();  // Block until complete
    }
    
    private CompletableFuture<?>[] toArray(List<CompletableFuture<ProcessResult>> futures) {
        return futures.toArray(new CompletableFuture[0]);
    }
    
    private ProcessResult errorResult(long rowId, Throwable ex) {
        return new ProcessResult(rowId, null, false, ex.getMessage());
    }
    
    // =========================================================================
    // MAIN PROCESSING PIPELINE
    // =========================================================================
    
    public enum ProcessingMode {
        PARALLEL_STREAM,
        INVOKE_ALL,
        COMPLETABLE_FUTURE
    }
    
    public void process(String csvPath, ProcessingMode mode) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.printf("Using mode: %s%n%n", mode);
        
        BlockingQueue<List<CsvRow>> batchQueue = new LinkedBlockingQueue<>(10);
        AtomicBoolean readerDone = new AtomicBoolean(false);
        
        // Reader thread
        Thread readerThread = createReaderThread(csvPath, batchQueue, readerDone);
        
        // Process batches
        readerThread.start();
        processBatches(batchQueue, readerDone, mode);
        readerThread.join();
        
        // Print results
        printResults(startTime);
    }
    
    private Thread createReaderThread(String csvPath, 
                                       BlockingQueue<List<CsvRow>> batchQueue,
                                       AtomicBoolean readerDone) {
        return new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
                List<CsvRow> batch = new ArrayList<>(batchSize);
                String line;
                reader.readLine(); // Skip header
                
                while ((line = reader.readLine()) != null) {
                    long rowId = rowIdGenerator.incrementAndGet();
                    batch.add(new CsvRow(rowId, line));
                    
                    if (batch.size() >= batchSize) {
                        batchQueue.put(new ArrayList<>(batch));
                        batch.clear();
                    }
                }
                
                if (!batch.isEmpty()) {
                    batchQueue.put(batch);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readerDone.set(true);
            }
        }, "csv-reader");
    }
    
    private void processBatches(BlockingQueue<List<CsvRow>> batchQueue,
                                AtomicBoolean readerDone,
                                ProcessingMode mode) throws Exception {
        while (!readerDone.get() || !batchQueue.isEmpty()) {
            List<CsvRow> batch = batchQueue.poll(100, TimeUnit.MILLISECONDS);
            if (batch == null) continue;
            
            // Use selected processing mode
            switch (mode) {
                case PARALLEL_STREAM:
                    processBatchWithParallelStream(batch);
                    break;
                case INVOKE_ALL:
                    processBatchWithInvokeAll(batch);
                    break;
                case COMPLETABLE_FUTURE:
                    processBatchWithCompletableFuture(batch);
                    break;
            }
        }
    }
    
    // =========================================================================
    // BUSINESS LOGIC
    // =========================================================================
    
    private ProcessResult processRow(CsvRow row) {
        try {
            Thread.sleep(1);  // Simulate work
            String[] fields = row.data.split(",");
            String processed = "Row " + row.id + ": " + fields[0].toUpperCase();
            return new ProcessResult(row.id, processed, true, null);
        } catch (Exception e) {
            return new ProcessResult(row.id, null, false, e.getMessage());
        }
    }
    
    private void printResults(long startTime) {
        long endTime = System.currentTimeMillis();
        
        System.out.println("\n=== RESULTS (first 5) ===");
        for (long i = 1; i <= Math.min(5, rowIdGenerator.get()); i++) {
            System.out.println(results.get(i));
        }
        if (rowIdGenerator.get() > 5) {
            System.out.println("... and " + (rowIdGenerator.get() - 5) + " more rows");
        }
        
        System.out.println("\n=== STATS ===");
        System.out.println("Total rows: " + rowIdGenerator.get());
        System.out.println("Processed: " + processedCount.get());
        System.out.println("Time: " + (endTime - startTime) + "ms");
    }
    
    public void shutdown() {
        executor.shutdown();
    }
    
    // =========================================================================
    // DATA CLASSES
    // =========================================================================
    
    static class CsvRow {
        final long id;
        final String data;
        
        CsvRow(long id, String data) {
            this.id = id;
            this.data = data;
        }
    }
    
    static class ProcessResult {
        final long rowId;
        final String result;
        final boolean success;
        final String error;
        
        ProcessResult(long rowId, String result, boolean success, String error) {
            this.rowId = rowId;
            this.result = result;
            this.success = success;
            this.error = error;
        }
        
        @Override
        public String toString() {
            return success ? "[OK] " + result : "[FAIL] Row " + rowId + ": " + error;
        }
    }
    
    // =========================================================================
    // MAIN
    // =========================================================================
    
    public static void main(String[] args) throws Exception {
        String testFile = "/tmp/test_data.csv";
        generateTestCsv(testFile, 10000);
        
        System.out.println("=== CSV Batch Processor Demo ===\n");
        
        // Test all 3 modes
        for (ProcessingMode mode : ProcessingMode.values()) {
            System.out.println("\n" + repeatChar('=', 50));
            CsvBatchProcessor processor = new CsvBatchProcessor(500, 4);
            processor.process(testFile, mode);
            processor.shutdown();
        }
    }
    
    private static void generateTestCsv(String path, int rows) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("name,email,age\n");
            for (int i = 1; i <= rows; i++) {
                writer.write(String.format("user_%d,user%d@example.com,%d\n", i, i, 20 + (i % 50)));
            }
        }
        System.out.println("Generated test file: " + path + " (" + rows + " rows)\n");
    }
    
    private static String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
