package com.practice.leetcode.concurrency;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

/**
 * PDF Word Counter - Concurrent Implementation
 * 
 * Features:
 * - Count words across multiple PDF files in parallel
 * - Aggregate word frequencies
 * - Thread-safe with proper synchronization
 * - Support large files with streaming
 * 
 * Note: For real PDF parsing, add Apache PDFBox dependency:
 *       <dependency>
 *           <groupId>org.apache.pdfbox</groupId>
 *           <artifactId>pdfbox</artifactId>
 *           <version>2.0.29</version>
 *       </dependency>
 * 
 * Run: mvn compile exec:java -Dexec.mainClass="com.practice.leetcode.concurrency.PdfWordCounter"
 */
public class PdfWordCounter {
    
    public static void main(String[] args) throws Exception {
        // Create test files
        List<Path> testFiles = TestDataGenerator.createTestFiles("/tmp/pdf_test", 5);
        
        System.out.println("=== PDF Word Counter Demo ===\n");
        
        // Initialize counter with 4 worker threads
        WordCounterService service = new WordCounterService(4);
        
        // Count words
        WordCountResult result = service.countWords(testFiles);
        
        // Print results
        ResultPrinter.print(result);
    }
}

// ============================================================================
// SERVICE LAYER
// ============================================================================

/**
 * Main service that orchestrates the word counting across multiple files
 */
class WordCounterService {
    
    private final int workerThreads;
    private final ExecutorService executor;
    
    public WordCounterService(int workerThreads) {
        this.workerThreads = workerThreads;
        this.executor = new ThreadPoolExecutor(
            workerThreads, workerThreads,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
    
    /**
     * Count words in all files concurrently
     */
    public WordCountResult countWords(List<Path> files) throws Exception {
        long startTime = System.currentTimeMillis();
        
        // Thread-safe structures for aggregation
        ConcurrentMap<String, AtomicLong> globalWordCount = new ConcurrentHashMap<>();
        ConcurrentMap<Path, FileResult> fileResults = new ConcurrentHashMap<>();
        AtomicLong totalWords = new AtomicLong(0);
        AtomicInteger filesProcessed = new AtomicInteger(0);
        
        // Submit all files for processing
        List<CompletableFuture<Void>> futures = files.stream()
            .map(file -> CompletableFuture.runAsync(() -> {
                try {
                    FileResult result = processFile(file);
                    fileResults.put(file, result);
                    
                    // Aggregate word counts (thread-safe)
                    result.wordFrequency.forEach((word, count) -> {
                        globalWordCount
                            .computeIfAbsent(word, k -> new AtomicLong(0))
                            .addAndGet(count);
                    });
                    
                    totalWords.addAndGet(result.totalWords);
                    filesProcessed.incrementAndGet();
                    
                    System.out.printf("[%s] Processed: %s (%d words)%n",
                        Thread.currentThread().getName(),
                        file.getFileName(),
                        result.totalWords);
                        
                } catch (Exception e) {
                    fileResults.put(file, new FileResult(file, 0, Collections.emptyMap(), e.getMessage()));
                }
            }, executor))
            .collect(Collectors.toList());
        
        // Wait for all to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        long endTime = System.currentTimeMillis();
        
        // Convert AtomicLong map to Long map
        Map<String, Long> finalWordCount = globalWordCount.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().get()
            ));
        
        return new WordCountResult(
            files.size(),
            filesProcessed.get(),
            totalWords.get(),
            finalWordCount,
            new ArrayList<>(fileResults.values()),
            endTime - startTime
        );
    }
    
    /**
     * Process a single file and return word counts
     */
    private FileResult processFile(Path file) throws Exception {
        // In real implementation, use PDFBox:
        // PDDocument document = PDDocument.load(file.toFile());
        // PDFTextStripper stripper = new PDFTextStripper();
        // String text = stripper.getText(document);
        
        // For demo, read as text file (Java 8 compatible)
        String content = new String(Files.readAllBytes(file));
        
        Map<String, Long> wordFrequency = Arrays.stream(content.toLowerCase().split("\\W+"))
            .filter(word -> !word.isEmpty() && word.length() > 2)
            .collect(Collectors.groupingBy(
                word -> word,
                Collectors.counting()
            ));
        
        long totalWords = wordFrequency.values().stream().mapToLong(Long::longValue).sum();
        
        return new FileResult(file, totalWords, wordFrequency, null);
    }
    
    public void shutdown() {
        executor.shutdown();
    }
}

// ============================================================================
// DATA CLASSES
// ============================================================================

/**
 * Result for a single file
 */
class FileResult {
    final Path file;
    final long totalWords;
    final Map<String, Long> wordFrequency;
    final String error;
    
    FileResult(Path file, long totalWords, Map<String, Long> wordFrequency, String error) {
        this.file = file;
        this.totalWords = totalWords;
        this.wordFrequency = wordFrequency;
        this.error = error;
    }
    
    boolean isSuccess() {
        return error == null;
    }
}

/**
 * Aggregated result for all files
 */
class WordCountResult {
    final int totalFiles;
    final int processedFiles;
    final long totalWords;
    final Map<String, Long> globalWordFrequency;
    final List<FileResult> fileResults;
    final long processingTimeMs;
    
    WordCountResult(int totalFiles, int processedFiles, long totalWords,
                   Map<String, Long> globalWordFrequency, 
                   List<FileResult> fileResults, long processingTimeMs) {
        this.totalFiles = totalFiles;
        this.processedFiles = processedFiles;
        this.totalWords = totalWords;
        this.globalWordFrequency = globalWordFrequency;
        this.fileResults = fileResults;
        this.processingTimeMs = processingTimeMs;
    }
    
    /**
     * Get top N most frequent words
     */
    List<Map.Entry<String, Long>> getTopWords(int n) {
        return globalWordFrequency.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(n)
            .collect(Collectors.toList());
    }
}

// ============================================================================
// UTILITIES
// ============================================================================

/**
 * Pretty print results
 */
class ResultPrinter {
    
    static void print(WordCountResult result) {
        System.out.println("\n" + repeatChar('=', 50));
        System.out.println("WORD COUNT SUMMARY");
        System.out.println(repeatChar('=', 50));
        
        // Stats
        System.out.printf("%nFiles processed: %d / %d%n", 
            result.processedFiles, result.totalFiles);
        System.out.printf("Total words: %,d%n", result.totalWords);
        System.out.printf("Unique words: %,d%n", result.globalWordFrequency.size());
        System.out.printf("Processing time: %d ms%n", result.processingTimeMs);
        
        // Top words
        System.out.println("\n--- Top 10 Words ---");
        List<Map.Entry<String, Long>> topWords = result.getTopWords(10);
        for (int i = 0; i < topWords.size(); i++) {
            Map.Entry<String, Long> entry = topWords.get(i);
            System.out.printf("%2d. %-15s : %,d%n", i + 1, entry.getKey(), entry.getValue());
        }
        
        // Per-file breakdown
        System.out.println("\n--- Per File ---");
        for (FileResult fr : result.fileResults) {
            if (fr.isSuccess()) {
                System.out.printf("  ✓ %-20s : %,6d words%n", 
                    fr.file.getFileName(), fr.totalWords);
            } else {
                System.out.printf("  ✗ %-20s : ERROR - %s%n", 
                    fr.file.getFileName(), fr.error);
            }
        }
        
        System.out.println("\n" + repeatChar('=', 50));
    }
    
    private static String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}

/**
 * Generate test data files
 */
class TestDataGenerator {
    
    private static final String[] SAMPLE_WORDS = {
        "java", "concurrency", "thread", "executor", "future", "async",
        "parallel", "processing", "data", "stream", "collection", "map",
        "reduce", "filter", "transform", "aggregate", "pipeline", "batch",
        "queue", "lock", "atomic", "volatile", "synchronized", "monitor",
        "performance", "scalability", "throughput", "latency", "memory"
    };
    
    static List<Path> createTestFiles(String directory, int fileCount) throws IOException {
        Path dir = Paths.get(directory);
        Files.createDirectories(dir);
        
        List<Path> files = new ArrayList<>();
        Random random = new Random(42);
        
        for (int i = 1; i <= fileCount; i++) {
            Path file = dir.resolve("document_" + i + ".txt");
            
            // Generate random content
            StringBuilder content = new StringBuilder();
            int wordCount = 1000 + random.nextInt(4000);
            
            for (int j = 0; j < wordCount; j++) {
                content.append(SAMPLE_WORDS[random.nextInt(SAMPLE_WORDS.length)]);
                content.append(j % 10 == 9 ? "\n" : " ");
            }
            
            Files.write(file, content.toString().getBytes());
            files.add(file);
        }
        
        System.out.printf("Generated %d test files in %s%n%n", fileCount, directory);
        return files;
    }
}
