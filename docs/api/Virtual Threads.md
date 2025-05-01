# Virtual Threads

## JEP 425: Virtual Threads (Preview) - JDK 19

1. Core Purpose: 核心目的：

- Enables high-throughput concurrent applications 实现高吞吐量的并发应用
- Significantly improves throughput when: 在以下情况下显著提升吞吐量：
  - Concurrent tasks > few thousand 并发任务数量很大（数千以上）
  - Workload is NOT CPU-bound 工作负载不是 CPU 密集型
  - Tasks spend much time waiting (I/O, network, etc.) 任务大多在等待（I/O、网络等）

2. Basic Usage Example:

```java
// Creating virtual threads
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    var future1 = executor.submit(() -> fetchURL(url1));
    var future2 = executor.submit(() -> fetchURL(url2));
    response.send(future1.get() + future2.get());
} catch (ExecutionException | InterruptedException e) {
    response.fail(e);
}
```

3. Key Characteristics: 关键特性：

- Virtual threads are lightweight 虚拟线程非常轻量级
- Mounted on carrier platform threads 运行在载体平台线程上
- Support thread-local variables 支持线程本地变量（ThreadLocal）
- Support thread interruption 支持线程中断
- Always daemon threads 始终是守护线程（daemon）
- Fixed NORM_PRIORITY priority 固定优先级（NORM_PRIORITY）

4. Important Warnings: 重要警告：

```java
// Don't pool virtual threads  // 不要池化虚拟线程
// BAD:
ExecutorService executor = Executors.newFixedThreadPool(n); // Don't pool

// GOOD:
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
```

5. Thread Pinning Considerations: Virtual threads can be pinned (blocked) in two scenarios: 线程钳制（Pinning）注意事项： 虚拟线程在两种情况下会被钳制：

```java
// 1. Inside synchronized blocks
synchronized(lock) {
    // Virtual thread is pinned here
    networkOperation(); // Blocking operation while pinned
}

// 2. During native method calls
nativeMethod(); // Virtual thread is pinned
```

Better alternative for synchronized:

```java
// Use ReentrantLock instead of synchronized for better virtual thread performance
private final ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    networkOperation();
} finally {
    lock.unlock();
}
```

6. Debugging Support:

```java
// Enable pinned thread tracking
-Djdk.tracePinnedThreads=full  // Full stack trace
-Djdk.tracePinnedThreads=short // Short trace
```

7. Scheduling: 调度机制：

- Uses ForkJoinPool in FIFO mode 使用 FIFO 模式的 ForkJoinPool
- Default parallelism = available processors 默认并行度等于可用处理器数
- Configurable via: jdk.virtualThreadScheduler.parallelism 可通过 jdk.virtualThreadScheduler.parallelism 配置
- No time-sharing implementation 没有实现时间片轮转

8. Memory and GC Interaction: 内存和 GC 交互：

- Virtual thread stacks stored in Java heap 虚拟线程栈存储在 Java 堆中
- Stacks grow/shrink dynamically 栈可动态增长/收缩
- Not GC roots (can be collected if unreachable) 不是 GC roots（unreachable 时可被回收）
- Stack chunks limited by G1 GC region size 栈块大小受 G1 GC 区域大小限制

9. Preview Feature Usage:

```bash
# Enable preview features
javac --release 19 --enable-preview Main.java
java --enable-preview Main

# Or with source launcher
java --source 19 --enable-preview Main.java

# In jshell
jshell --enable-preview
```

10. Monitoring and Management:

```java
// Thread dump to JSON format
jcmd <pid> Thread.dump_to_file -format=json <file>
```

11. Best Practices:

```java
// Use virtual threads for I/O-bound tasks 用于 I/O 密集型任务
// Don't use for CPU-bound work 不要用于 CPU 密集型工作
// Avoid thread pools 避免线程池化
// Use ReentrantLock instead of synchronized 使用 ReentrantLock 替代 synchronized
// Be careful with thread-locals (memory consumption) 谨慎使用线程本地变量（考虑内存消耗）
```

12. Limitations:

- No support for ThreadGroup operations 不支持 ThreadGroup 操作
- Cannot change daemon status 不能更改守护线程状态
- Fixed priority 固定优先级
- Limited permissions with SecurityManager SecurityManager 下权限受限
- Pinning with synchronized and native calls synchronized 和本地调用会导致钳制
- G1 GC humongous region limitation 受 G1 GC 大对象区域限制

13. Error Handling:

```java
try {
    // Virtual thread operations
} catch (IllegalStateException e) {
    // Handle scope closure
} catch (UnsupportedOperationException e) {
    // Handle unsupported operations
}
```

## JEP 436: Virtual Threads (Second Preview) - JDK 20

Key changes from JEP 425 to JEP 436:

1. Virtual Thread Creation API:

```java
// JEP 425
Thread.startVirtualThread(Runnable)

// JEP 436
// More options through Thread.Builder
Thread thread = Thread.ofVirtual()
    .name("worker-", 1)    // Name with counter
    .unstarted(runnable);  // Create unstarted thread
```

2. ExecutorService Usage:

```java
// JEP 425 and 436 both support this pattern, but 436 emphasizes its importance
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 10_000).forEach(i -> {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));
            return i;
        });
    });
}  // executor.close() called implicitly
```

3. Thread Pool Warning:

```java
// JEP 436 more strongly emphasizes not to pool virtual threads
// BAD - Don't do this with virtual threads:
ExecutorService executor = Executors.newFixedThreadPool(200);

// GOOD - Create new virtual thread per task:
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
```

4. Monitoring and Observability:

```java
// JEP 436 introduces new thread dump format
$ jcmd <pid> Thread.dump_to_file -format=json <file>

// New thread dump groups virtual threads by their state and creation context
```

5. Scheduling Clarifications:

```java
// JEP 436 provides more details about scheduling
// Configure scheduler parallelism
-Djdk.virtualThreadScheduler.parallelism=N
// Configure maximum pool size
-Djdk.virtualThreadScheduler.maxPoolSize=N
```

6. Pinning Behavior:

```java
// JEP 436 clarifies pinning scenarios
synchronized(lock) {
    networkOperation(); // Thread is pinned
}

// Native method calls
nativeMethod(); // Thread is pinned

// New diagnostic options
-Djdk.tracePinnedThreads=full
-Djdk.tracePinnedThreads=short
```

7. Memory Management:

```java
// JEP 436 provides more details about memory management
// Stack chunks stored in heap
// Dynamic growth/shrinking
// Not GC roots
```

8. Key Improvements in JEP 436:

- Better documentation of virtual thread behavior
- Clearer guidance on usage patterns
- Enhanced monitoring capabilities
- More detailed explanation of scheduling
- Better diagnostic tools
- Improved thread dump format

9. Thread Dump Enhancements:

```java
// JEP 436 introduces structured thread dumps
{
    "threads": {
        "virtualThreads": [...],
        "platformThreads": [...],
        "groups": {
            "main": [...],
            "system": [...]
        }
    }
}
```

10. Documentation Clarifications:

- Better explanation of when to use virtual threads
- Clearer guidance on thread pooling
- More detailed performance characteristics
- Better explanation of memory usage
- Enhanced debugging information

11. Main Changes Summary:

- Enhanced API for thread creation
- Better monitoring tools
- Improved documentation
- Clearer best practices
- Enhanced diagnostic capabilities
- Better thread dump format
- More detailed scheduling information

The key themes of changes in JEP 436 are:

1. Better documentation and clarity
2. Enhanced monitoring and diagnostics
3. Improved thread creation API
4. Clearer best practices
5. Better debugging support

## JEP 444: Virtual Threads - JDK 21

Key changes from JEP 436 to JEP 444 (Final Release of Virtual Threads):

1. API Finalization:

```java
// Virtual Thread Creation - Now finalized
Thread vThread = Thread.startVirtualThread(() -> {
    // task code
});

// Builder Pattern - Now stable
Thread thread = Thread.ofVirtual()
    .name("worker-", 1)
    .start(runnable);
```

2. ExecutorService Usage (Finalized Pattern):

```java
// Standard pattern for virtual thread per task
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 10_000).forEach(i -> {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));
            return i;
        });
    });
}  // executor.close() called implicitly
```

3. Scheduling Clarifications:

```java
// Configuration properties finalized
System.setProperty("jdk.virtualThreadScheduler.parallelism", "N");
System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "N");

// Scheduler uses ForkJoinPool in FIFO mode
// Default parallelism = available processors
```

4. Thread Tracking:

```java
// New property for controlling thread tracking
-Djdk.trackAllThreads=false  // Disable tracking for some virtual threads

// Affects thread dump visibility
// Virtual threads created by Thread.Builder might not appear in dumps
```

5. Key Finalized Features:

- Virtual threads are always daemon threads
- Fixed NORM_PRIORITY priority
- No ThreadGroup support
- No SecurityManager permissions

6. Monitoring and Diagnostics:

```java
// Thread dump format finalized
$ jcmd <pid> Thread.dump_to_file -format=json <file>

// Pinning diagnostics
-Djdk.tracePinnedThreads=full
-Djdk.tracePinnedThreads=short
```

7. Memory Management (Finalized Behavior):

```java
// Stack chunks in heap
// Dynamic growth/shrink
// Not GC roots
// Limited by G1 GC region size
```

8. Best Practices (Now Stable):

```java
// Do NOT pool virtual threads
// BAD:
ExecutorService pooled = Executors.newFixedThreadPool(n);

// GOOD:
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

// Use ReentrantLock instead of synchronized when possible
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}
```

9. Major Differences from JEP 436:

- API is now final and stable
- Clearer documentation on limitations
- Finalized thread tracking behavior
- Stable monitoring and diagnostic tools
- Production-ready implementation

10 Performance Characteristics (Finalized):

```java
// Virtual threads are beneficial when:
- Task count > few thousand
- NOT CPU-bound workload
- Tasks spend time waiting (I/O, network, etc.)
```

11. Key Implementation Details:

```java
// Carrier thread behavior finalized
- Mounting/unmounting mechanics stable
- Scheduling behavior defined
- Memory management characteristics fixed
```

12. Limitations (Now Clearly Defined):

```java
// Virtual threads cannot:
- Change daemon status
- Change priority
- Have security permissions
- Be active ThreadGroup members
```

13. Main Changes Summary:

14. API Stabilization:

- Final API design
- Stable behavior patterns
- Production-ready implementation

15.  Documentation:

- Clear use cases
- Well-defined limitations
- Stable best practices

16. Performance:

- Finalized scheduling behavior
- Stable memory management
- Clear performance characteristics

17. Monitoring:

- Stable diagnostic tools
- Final thread dump format
- Clear tracking options

The key themes of changes in JEP 444 are:

1. API Finalization
2. Stability
3. Production Readiness
4. Clear Documentation
5. Performance Predictability
6. Monitoring Capability

## JEPs comparison

1. JEP 425 (Preview 1): 关键点：

- 引入虚拟线程概念
- 提供基本的虚拟线程创建API
- 定义基本行为和限制
- 引入线程钳制概念

示例代码：

```java
// 1. 基本创建方式
Thread vThread = Thread.startVirtualThread(() -> {
    System.out.println("Running in virtual thread");
});

// 2. 使用ExecutorService
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> {
        System.out.println("Task in virtual thread");
        return "Result";
    });
}

// 3. 避免的模式 - 线程池
// 错误示例
ExecutorService pooled = Executors.newFixedThreadPool(100); // 不要对虚拟线程使用池

// 4. 钳制示例
synchronized (lock) {
    // 虚拟线程在这里被钳制
    networkOperation(); // 不推荐
}
```

2. JEP 436 (Preview 2): 关键点：

- 增强线程创建API
- 改进监控能力
- 提供更好的诊断工具
- 完善调度机制说明

示例代码：

```java
// 1. 增强的线程创建API
Thread vThread = Thread.ofVirtual()
    .name("worker-", 1)
    .start(() -> {
        System.out.println("Enhanced virtual thread creation");
    });

// 2. 结构化并发示例
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    List<Future<String>> futures = new ArrayList<>();
    
    // 提交多个任务
    for (int i = 0; i < 1000; i++) {
        final int id = i;
        futures.add(executor.submit(() -> {
            Thread.sleep(Duration.ofMillis(100));
            return "Task " + id + " completed";
        }));
    }
    
    // 获取结果
    for (Future<String> future : futures) {
        System.out.println(future.get());
    }
}

// 3. 诊断工具使用
// 启用线程钳制跟踪
// -Djdk.tracePinnedThreads=full
// 使用新的线程转储格式
// jcmd <pid> Thread.dump_to_file -format=json thread-dump.json
```

3. JEP 444 (Final): 关键点：

- API 最终确定
- 生产就绪
- 明确的最佳实践
- 稳定的监控工具

示例代码：

```java
// 1. 标准的虚拟线程使用模式
public class VirtualThreadServer {
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    
    public void handleRequest(Request request) {
        try {
            // 并发获取数据
            CompletableFuture<String> data1 = CompletableFuture.supplyAsync(
                () -> fetchData("service1"), executor);
            CompletableFuture<String> data2 = CompletableFuture.supplyAsync(
                () -> fetchData("service2"), executor);
            
            // 组合结果
            String result = data1.get() + data2.get();
            sendResponse(result);
            
        } catch (Exception e) {
            handleError(e);
        }
    }
    
    private String fetchData(String service) throws IOException {
        // 模拟网络调用
        Thread.sleep(Duration.ofMillis(100));
        return "Data from " + service;
    }
}

// 2. 推荐的锁使用模式
public class ThreadSafeCounter {
    private final ReentrantLock lock = new ReentrantLock();
    private int count = 0;
    
    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }
}

// 3. 完整的服务示例
public class VirtualThreadWebService {
    public static void main(String[] args) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                executor.submit(() -> handleConnection(socket));
            }
        }
    }
    
    private static void handleConnection(Socket socket) {
        try (socket; 
             var in = new BufferedReader(
                 new InputStreamReader(socket.getInputStream()));
             var out = new PrintWriter(socket.getOutputStream(), true)) {
            
            String request = in.readLine();
            // 处理请求
            String response = processRequest(request);
            out.println(response);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

主要演进：

1. JEP 425 → JEP 436:

- API 增强
- 更好的监控
- 更清晰的最佳实践

1. JEP 436 → JEP 444:

- API 最终确定
- 生产就绪
- 性能优化
- 稳定的工具支持

使用建议：

1. 使用 `newVirtualThreadPerTaskExecutor()`而不是传统线程池
2. 优先使用 `ReentrantLock`而不是 `synchronized`
3. 注意避免线程钳制
4. 适用于 I/O 密集型任务
5. 监控虚拟线程的使用情况

## 虚拟线程和传统线程（平台线程）的异同点

相同点：

1. 基本功能：

```java
// 两种线程都支持基本的线程操作
- 都是 Thread 类的实例
- 都可以运行任何 Java 代码
- 支持线程本地变量（ThreadLocal）
- 支持线程中断（interrupt）
```

2. 编程模型：

```java
// 代码编写方式基本相同
Runnable task = () -> {
    // 业务逻辑
};
// 传统线程
Thread platformThread = new Thread(task);
// 虚拟线程
Thread virtualThread = Thread.startVirtualThread(task);
```

不同点：

1. 资源消耗：

```java
// 传统线程（平台线程）
- 与操作系统线程一一对应
- 每个线程占用大量内存（通常是MB级别）
- 线程数受系统资源限制

// 虚拟线程
- 由 JVM 管理，共享操作系统线程
- 非常轻量级（KB级别）
- 可以创建数百万个虚拟线程
```

2. 调度方式：

```java
// 传统线程
- 由操作系统调度
- 使用抢占式调度
- 上下文切换开销大

// 虚拟线程
- 由 JVM 调度器管理
- 使用协作式调度
- 在阻塞操作时自动让出载体线程
```

3. 线程特性：

```java
// 传统线程
Thread t = new Thread();
t.setPriority(Thread.MAX_PRIORITY);  // 可以设置优先级
t.setDaemon(false);                  // 可以设置守护状态

// 虚拟线程
Thread vt = Thread.startVirtualThread(() -> {});
vt.setPriority(Thread.MAX_PRIORITY); // 无效，始终是 NORM_PRIORITY
vt.setDaemon(false);                 // 无效，始终是守护线程
```

4. 线程组和安全管理：

```java
// 传统线程
- 可以是线程组的活动成员
- 支持完整的安全权限

// 虚拟线程
- 不是线程组的活动成员（使用虚拟的 "VirtualThreads" 组）
- 在 SecurityManager 下没有权限
```

5. I/O 操作处理：

```java
// 传统线程
- I/O 阻塞会占用操作系统线程
- 通常需要线程池来管理

// 虚拟线程
- I/O 阻塞时自动释放载体线程
- 不需要也不应该使用线程池
```

6. 性能考虑：

```java
// 适用场景
// 传统线程
- 适合 CPU 密集型任务
- 适合需要真实操作系统线程的场景
- 适合较少数量的并发任务

// 虚拟线程
- 适合 I/O 密集型任务
- 适合大量并发连接
- 适合大量等待操作的场景
```

7. 钳制（Pinning）情况：

```java
// 传统线程
- 不存在钳制问题

// 虚拟线程在以下情况会被钳制：
synchronized (lock) {
    // 在同步块内会被钳制
    networkOperation();
}
// 执行本地方法时也会被钳制
nativeMethod();
```

8. 最佳实践：

```java
// 传统线程
- 使用线程池管理
- 控制线程数量
- 可以自由使用 synchronized

// 虚拟线程
- 避免线程池化
- 优先使用 ReentrantLock 而不是 synchronized
- 注意 ThreadLocal 的内存消耗
- 适合每个任务创建新的虚拟线程
```

使用建议：

1. 对于 I/O 密集型或大量并发的应用，优先考虑虚拟线程
2. 对于 CPU 密集型任务，继续使用传统线程
3. 迁移到虚拟线程时，注意避免 synchronized 的使用
4. 重构代码时，关注线程池的使用模式
5. 注意监控和调试工具的支持情况