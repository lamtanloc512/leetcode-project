# 🔄 Batch Processing Pipeline - Multi-Language Comparison

> **Use Case**: Process large CSV files in batches with parallel processing while maintaining order

---

## 📊 Quick Comparison

| Approach | Pros | Cons | Best For |
|----------|------|------|----------|
| **Java Pure** | No deps, full control | Verbose | Learning, simple apps |
| **Spring Batch** | Production-ready, restart/retry | Complex setup | Enterprise |
| **Scala Pure** | Concise, functional | JVM dependency | FP lovers |
| **Akka Streams** | Backpressure, resilient | Learning curve | High-throughput |
| **Go** | Simple, fast, low memory | Limited generics | Microservices |
| **Rust Rayon** | Zero-cost, memory-safe | Steep learning | Performance-critical |

---

## 1. 🟨 Java Pure (ExecutorService + CompletableFuture)

📁 **Full implementation**: [CsvBatchProcessor.java](file:///home/ethan/Projects/leetcode-project/leetcode/src/main/java/com/practice/leetcode/concurrency/CsvBatchProcessor.java)

```java
// Key pattern: ThreadPoolExecutor + BlockingQueue + ConcurrentHashMap
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    cores, cores, 0L, TimeUnit.MILLISECONDS,
    new LinkedBlockingQueue<>(100),
    new ThreadPoolExecutor.CallerRunsPolicy()
);

// Process with CompletableFuture
CompletableFuture.runAsync(() -> processRow(row), executor);
```

---

## 2. 🍃 Spring Batch Framework

```java
@Configuration
@EnableBatchProcessing
public class CsvBatchConfig {
    
    @Bean
    public Job csvProcessingJob(JobRepository jobRepository, Step processStep) {
        return new JobBuilder("csvJob", jobRepository)
            .start(processStep)
            .build();
    }
    
    @Bean
    public Step processStep(JobRepository jobRepository,
                           PlatformTransactionManager txManager,
                           ItemReader<CsvRow> reader,
                           ItemProcessor<CsvRow, Result> processor,
                           ItemWriter<Result> writer) {
        return new StepBuilder("processStep", jobRepository)
            .<CsvRow, Result>chunk(1000, txManager)  // Batch size 1000
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .taskExecutor(taskExecutor())  // Enable parallel
            .throttleLimit(4)               // Max 4 threads
            .build();
    }
    
    @Bean
    public FlatFileItemReader<CsvRow> reader() {
        return new FlatFileItemReaderBuilder<CsvRow>()
            .name("csvReader")
            .resource(new FileSystemResource("data.csv"))
            .delimited()
            .names("name", "email", "age")
            .targetType(CsvRow.class)
            .build();
    }
    
    @Bean
    public ItemProcessor<CsvRow, Result> processor() {
        return row -> {
            // Business logic here
            return new Result(row.getId(), row.getName().toUpperCase());
        };
    }
    
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
```

**Spring Batch Features:**
- ✅ Automatic restart on failure
- ✅ Skip/Retry policies
- ✅ Job monitoring & metrics
- ✅ Partitioning for distributed processing

---

## 3. 🔴 Scala Pure (Parallel Collections + Futures)

```scala
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source
import java.util.concurrent.ConcurrentHashMap

object CsvBatchProcessor extends App {
  
  val BatchSize = 1000
  val results = new ConcurrentHashMap[Long, ProcessResult]()
  
  def process(csvPath: String): Future[Unit] = {
    val lines = Source.fromFile(csvPath).getLines().drop(1).toList
    val batches = lines.zipWithIndex.grouped(BatchSize).toList
    
    // Process batches in parallel
    val futures = batches.map { batch =>
      Future {
        batch.par.foreach { case (line, idx) =>  // Parallel collection
          val result = processRow(idx, line)
          results.put(idx.toLong, result)
        }
      }
    }
    
    Future.sequence(futures).map(_ => outputResults())
  }
  
  def processRow(id: Int, line: String): ProcessResult = {
    val fields = line.split(",")
    ProcessResult(id, s"Processed: ${fields.headOption.getOrElse("")}")
  }
  
  def outputResults(): Unit = {
    (0L until results.size()).foreach { i =>
      println(results.get(i))
    }
  }
  
  case class ProcessResult(id: Long, result: String)
  
  // Run
  Await.result(process("/tmp/data.csv"), Duration.Inf)
}
```

---

## 4. 🔵 Scala Akka Streams (Reactive Streams)

```scala
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import akka.util.ByteString
import java.nio.file.Paths
import scala.concurrent.duration._

object AkkaCsvProcessor extends App {
  
  implicit val system: ActorSystem = ActorSystem("csv-processor")
  implicit val materializer: Materializer = Materializer(system)
  import system.dispatcher
  
  val BatchSize = 1000
  val Parallelism = 4
  
  val source: Source[String, _] = FileIO.fromPath(Paths.get("/tmp/data.csv"))
    .via(Framing.delimiter(ByteString("\n"), 4096))
    .map(_.utf8String)
    .drop(1)  // Skip header
    
  val batchFlow: Flow[String, Seq[String], _] = 
    Flow[String].grouped(BatchSize)
    
  val processFlow: Flow[Seq[String], Seq[ProcessResult], _] =
    Flow[Seq[String]].mapAsync(Parallelism) { batch =>
      Future {
        batch.zipWithIndex.map { case (line, idx) =>
          processRow(idx, line)
        }
      }
    }
    
  val sink: Sink[Seq[ProcessResult], _] = 
    Sink.foreach { results =>
      results.foreach(println)
    }
  
  // Run pipeline with backpressure
  source
    .via(batchFlow)
    .via(processFlow)
    .runWith(sink)
    .onComplete { _ =>
      system.terminate()
    }
    
  def processRow(id: Int, line: String): ProcessResult = {
    val fields = line.split(",")
    ProcessResult(id, s"Processed: ${fields.headOption.getOrElse("")}")
  }
  
  case class ProcessResult(id: Int, result: String)
}
```

**Akka Streams Features:**
- ✅ Built-in backpressure (never OOM)
- ✅ Automatic buffer management
- ✅ Easy to compose flows
- ✅ Async boundaries

---

## 5. 🐹 Go (Goroutines + Channels)

```go
package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
	"sync"
)

const (
	BatchSize     = 1000
	WorkerCount   = 4
)

type CsvRow struct {
	ID   int64
	Data string
}

type ProcessResult struct {
	RowID  int64
	Result string
	Error  error
}

func main() {
	// Channels
	batchChan := make(chan []CsvRow, 10)
	resultChan := make(chan ProcessResult, 1000)
	
	var wg sync.WaitGroup
	results := make(map[int64]ProcessResult)
	var totalRows int64
	
	// 1. Reader goroutine
	go func() {
		defer close(batchChan)
		file, _ := os.Open("/tmp/data.csv")
		defer file.Close()
		
		scanner := bufio.NewScanner(file)
		scanner.Scan() // Skip header
		
		var batch []CsvRow
		var rowID int64 = 0
		
		for scanner.Scan() {
			rowID++
			batch = append(batch, CsvRow{ID: rowID, Data: scanner.Text()})
			
			if len(batch) >= BatchSize {
				batchChan <- batch
				batch = nil
			}
		}
		
		if len(batch) > 0 {
			batchChan <- batch
		}
		totalRows = rowID
	}()
	
	// 2. Worker goroutines (fan-out)
	for i := 0; i < WorkerCount; i++ {
		wg.Add(1)
		go func(workerID int) {
			defer wg.Done()
			for batch := range batchChan {
				for _, row := range batch {
					result := processRow(row)
					resultChan <- result
				}
			}
		}(i)
	}
	
	// Close results when workers done
	go func() {
		wg.Wait()
		close(resultChan)
	}()
	
	// 3. Collect results
	for result := range resultChan {
		results[result.RowID] = result
	}
	
	// 4. Output in order
	fmt.Println("=== Results (in order) ===")
	for i := int64(1); i <= min(totalRows, 5); i++ {
		fmt.Printf("[%d] %s\n", i, results[i].Result)
	}
	fmt.Printf("... and %d more rows\n", totalRows-5)
}

func processRow(row CsvRow) ProcessResult {
	fields := strings.Split(row.Data, ",")
	result := "Processed: " + strings.ToUpper(fields[0])
	return ProcessResult{RowID: row.ID, Result: result}
}

func min(a, b int64) int64 {
	if a < b { return a }
	return b
}
```

**Go Advantages:**
- ✅ Goroutines are cheap (2KB vs Java 1MB)
- ✅ Channels provide safe communication
- ✅ Simple syntax, fast compilation
- ✅ Great for I/O-bound tasks

---

## 6. 🦀 Rust (Rayon + Crossbeam)

### Option A: Rayon (Data Parallelism)

```rust
use rayon::prelude::*;
use std::collections::HashMap;
use std::fs::File;
use std::io::{BufRead, BufReader};
use std::sync::Mutex;

fn main() {
    let file = File::open("/tmp/data.csv").unwrap();
    let reader = BufReader::new(file);
    
    let results: Mutex<HashMap<usize, String>> = Mutex::new(HashMap::new());
    
    // Read all lines (for simplicity)
    let lines: Vec<_> = reader.lines()
        .skip(1)  // Skip header
        .filter_map(|l| l.ok())
        .enumerate()
        .collect();
    
    // Process in parallel with Rayon
    lines.par_iter().for_each(|(idx, line)| {
        let result = process_row(*idx, line);
        results.lock().unwrap().insert(*idx, result);
    });
    
    // Output in order
    let results = results.lock().unwrap();
    for i in 0..5.min(results.len()) {
        println!("[{}] {}", i, results.get(&i).unwrap());
    }
}

fn process_row(id: usize, line: &str) -> String {
    let fields: Vec<&str> = line.split(',').collect();
    format!("Processed: {}", fields.first().unwrap_or(&"").to_uppercase())
}
```

### Option B: Crossbeam Channels (Producer-Consumer)

```rust
use crossbeam_channel::{bounded, unbounded};
use std::thread;
use std::fs::File;
use std::io::{BufRead, BufReader};
use std::collections::HashMap;

const BATCH_SIZE: usize = 1000;
const WORKERS: usize = 4;

#[derive(Clone)]
struct CsvRow {
    id: usize,
    data: String,
}

struct ProcessResult {
    row_id: usize,
    result: String,
}

fn main() {
    let (batch_tx, batch_rx) = bounded::<Vec<CsvRow>>(10);
    let (result_tx, result_rx) = unbounded::<ProcessResult>();
    
    // Reader thread
    let reader_handle = thread::spawn(move || {
        let file = File::open("/tmp/data.csv").unwrap();
        let reader = BufReader::new(file);
        let mut batch = Vec::with_capacity(BATCH_SIZE);
        let mut row_id = 0usize;
        
        for line in reader.lines().skip(1).filter_map(|l| l.ok()) {
            row_id += 1;
            batch.push(CsvRow { id: row_id, data: line });
            
            if batch.len() >= BATCH_SIZE {
                batch_tx.send(batch.clone()).unwrap();
                batch.clear();
            }
        }
        
        if !batch.is_empty() {
            batch_tx.send(batch).unwrap();
        }
        row_id
    });
    
    // Worker threads
    let mut handles = vec![];
    for _ in 0..WORKERS {
        let rx = batch_rx.clone();
        let tx = result_tx.clone();
        
        handles.push(thread::spawn(move || {
            while let Ok(batch) = rx.recv() {
                for row in batch {
                    let result = process_row(&row);
                    tx.send(ProcessResult { row_id: row.id, result }).unwrap();
                }
            }
        }));
    }
    
    drop(batch_rx);  // Close channel when done
    drop(result_tx);
    
    // Collect results
    let mut results = HashMap::new();
    while let Ok(r) = result_rx.recv() {
        results.insert(r.row_id, r.result);
    }
    
    for handle in handles {
        handle.join().unwrap();
    }
    
    let total = reader_handle.join().unwrap();
    
    // Output in order
    println!("=== Results ===");
    for i in 1..=5.min(total) {
        println!("[{}] {}", i, results.get(&i).unwrap());
    }
}

fn process_row(row: &CsvRow) -> String {
    let fields: Vec<&str> = row.data.split(',').collect();
    format!("Processed: {}", fields.first().unwrap_or(&"").to_uppercase())
}
```

**Rust Advantages:**
- ✅ Zero-cost abstractions
- ✅ Memory safety guaranteed at compile time
- ✅ No garbage collection pauses
- ✅ Fearless concurrency

---

## 🎯 When to Use What?

| Scenario | Recommended |
|----------|-------------|
| Quick prototype | Go |
| Enterprise/ETL jobs | Spring Batch |
| High-throughput streaming | Akka Streams |
| Performance-critical | Rust Rayon |
| Existing Java codebase | Java Pure |
| Functional programming | Scala |
