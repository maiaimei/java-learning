# Structured Concurrency

## JEP 428: Structured Concurrency (Incubator) - JDK 19

Structured concurrency treats multiple tasks running in different threads as a single unit of work, thereby streamlining error handling and cancellation, improving reliability, and enhancing observability.

结构化并发将在不同线程中运行的多个任务视为单个工作单元，从而简化错误处理和取消，提高可靠性，增强可观察性。

The principal class of the structured concurrency API is [`StructuredTaskScope`](https://docs.oracle.com/en/java/javase/19/docs/api/jdk.incubator.concurrent/jdk/incubator/concurrent/StructuredTaskScope.html). This class allows developers to structure a task as a family of concurrent subtasks, and to coordinate them as a unit. Subtasks are executed in their own threads by *forking* them individually and then *joining* them as a unit and, possibly, cancelling them as a unit. The subtasks' successful results or exceptions are aggregated and handled by the parent task. `StructuredTaskScope` confines the lifetimes of the subtasks, or *forks*, to a clear [lexical scope](https://en.wikipedia.org/wiki/Scope_(computer_science)#Lexical_scope) in which all of a task's interactions with its subtasks — forking, joining, cancelling, handling errors, and composing results — takes place.

结构化并发API的核心类是`StructuredTaskScope`。该类允许开发者将任务构建为一组并发子任务，并以整体方式协调这些子任务。子任务通过单独分叉的方式在其独立线程中执行，随后作为统一整体进行合并操作，必要时也可作为整体取消。子任务的成功结果或异常将由父任务进行聚合处理。`StructuredTaskScope`将子任务（或分叉操作）的生命周期限定在明确的词法作用域内，在此作用域内完成父任务与子任务之间的所有交互操作——包括分叉、合并、取消、错误处理及结果组合。

### Using `StructuredTaskScope`

The general workflow of code using `StructuredTaskScope` is as follows:

1. Create a scope. The thread that creates the scope is its *owner*.
2. Fork concurrent subtasks in the scope.
3. Any of the forks in the scope, or the scope's owner, may call the scope's [`shutdown()`](https://docs.oracle.com/en/java/javase/19/docs/api/jdk.incubator.concurrent/jdk/incubator/concurrent/StructuredTaskScope.html#shutdown()) method to request cancellation of all remaining subtasks.
4. The scope's owner joins the scope, i.e., all of its forks, as a unit. The owner can call the scope's [`join()`](https://docs.oracle.com/en/java/javase/19/docs/api/jdk.incubator.concurrent/jdk/incubator/concurrent/StructuredTaskScope.html#join()) method, which blocks until all forks have either completed (successfully or not) or been cancelled via `shutdown()`. Alternatively, the owner can call the scope's [`joinUntil(java.time.Instant)`](https://docs.oracle.com/en/java/javase/19/docs/api/jdk.incubator.concurrent/jdk/incubator/concurrent/StructuredTaskScope.html#joinUntil(java.time.Instant)) method, which accepts a deadline.
5. After joining, handle any errors in the forks and process their results.
6. Close the scope, usually implicitly via `try`-with-resources. This shuts down the scope and waits for any straggling forks to complete.

使用 `StructuredTaskScope` 的代码一般工作流程如下：

1. **创建作用域**。创建作用域的线程是其所有者。
2. **在作用域内分叉并发子任务**。
3. **作用域内的任何分叉（子任务）或作用域的所有者均可调用作用域的 shutdown() 方法**，请求取消所有剩余子任务。
4. **作用域的所有者需等待作用域完成**，即等待其所有分叉任务作为一个整体完成。所有者可以调用作用域的 join() 方法（该方法会阻塞，直到所有分叉任务完成（无论成功或失败）或通过 shutdown() 被取消）。或者，所有者可以调用 joinUntil(java.time.Instant) 方法并指定一个截止时间。
5. **等待完成后处理错误并处理结果**。
6. **关闭作用域**（通常通过 try-with-resources 隐式完成）。这会关闭作用域，并等待所有未完成的分叉任务结束。

### Shutdown policies

When dealing with concurrent subtasks it is common to use *short-circuiting patterns* to avoid doing unnecessary work. Sometimes it makes sense, for example, to cancel all subtasks if one of them fails (i.e., *invoke all*) or, alternatively, if one of them succeeds (i.e., *invoke any*). Two subclasses of `StructuredTaskScope`, [`ShutdownOnFailure`](https://docs.oracle.com/en/java/javase/19/docs/api/jdk.incubator.concurrent/jdk/incubator/concurrent/StructuredTaskScope.ShutdownOnFailure.html) and [`ShutdownOnSuccess`](https://docs.oracle.com/en/java/javase/19/docs/api/jdk.incubator.concurrent/jdk/incubator/concurrent/StructuredTaskScope.ShutdownOnSuccess.html), support these patterns with policies that shut down the scope upon the first fork failure or success, respectively. They also provide methods for handling exceptions and successful results.

```java
// Here is a StructuredTaskScope with a shutdown-on-failure policy (used also in the handle() example above) that runs a collection of tasks concurrently and fails if any one of them fails:
<T> List<T> runAll(List<Callable<T>> tasks) throws Throwable {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        List<Future<T>> futures = tasks.stream().map(scope::fork).toList();
        scope.join();
        scope.throwIfFailed(e -> e);  // Propagate exception as-is if any fork fails
        // Here, all tasks have succeeded, so compose their results
        return futures.stream().map(Future::resultNow).toList();
    }
}

// Here is a StructuredTaskScope with a shutdown-on-success policy that returns the result of the first successful subtask:
<T> T race(List<Callable<T>> tasks, Instant deadline) throws ExecutionException {
    try (var scope = new StructuredTaskScope.ShutdownOnSuccess<T>()) {
        for (var task : tasks) {
            scope.fork(task);
        }
        scope.joinUntil(deadline);
        return scope.result();  // Throws if none of the forks completed successfully
    }
}
```

## JEP 437: Structured Concurrency (Second Incubator) - JDK 20

The only change in the re-incubated API is that [`StructuredTaskScope`](https://docs.oracle.com/en/java/javase/20/docs/api/jdk.incubator.concurrent/jdk/incubator/concurrent/StructuredTaskScope.html) is updated to support the inheritance of scoped values ([JEP 429](https://openjdk.org/jeps/429)) by threads created in a task scope. This streamlines the sharing of immutable data across threads.

## JEP 453: Structured Concurrency (Preview) - JDK 21

Structured Concurrency was proposed by [JEP 428](https://openjdk.org/jeps/428) and delivered in [JDK 19](https://openjdk.org/projects/jdk/19/) as an incubating API. It was re-incubated by [JEP 437](https://openjdk.org/jeps/437) in [JDK 20](https://openjdk.org/projects/jdk/20/) with a minor update to inherit scoped values ([JEP 429](https://openjdk.org/jeps/429)).

We here propose to make Structured Concurrency a Preview API in the [`java.util.concurrent`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/package-summary.html) package. The only significant change is that the [`StructuredTaskScope`](https://docs.oracle.com/en/java/javase/21/docsapi/java.base/java/util/concurrent/StructuredTaskScope.html)`::`[`fork(...)`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/StructuredTaskScope.html#fork(java.util.concurrent.Callable)) method returns a [`Subtask`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/StructuredTaskScope.Subtask.html) rather than a [`Future`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/Future.html), as discussed [below](https://openjdk.org/jeps/453#Why-doesn't-fork-return-a-Future?).

In the current API, `Subtask::get()` behaves exactly as `Future::resultNow()` did when the API was incubating.

## JEP 462: Structured Concurrency (Second Preview) - JDK 22

Structured Concurrency was proposed by [JEP 428](https://openjdk.org/jeps/428) and delivered in [JDK 19](https://openjdk.org/projects/jdk/19/) as an incubating API. It was re-incubated by [JEP 437](https://openjdk.org/jeps/437) in [JDK 20](https://openjdk.org/projects/jdk/20/) with a minor update to inherit scoped values ([JEP 429](https://openjdk.org/jeps/429)). It first previewed in [JDK 21](https://openjdk.org/projects/jdk/21/) via [JEP 453](https://openjdk.org/jeps/453) with [`StructuredTaskScope`](https://docs.oracle.com/en/java/javase/21/docsapi/java.base/java/util/concurrent/StructuredTaskScope.html)`::`[`fork(...)`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/StructuredTaskScope.html#fork(java.util.concurrent.Callable)) changed to return a [`Subtask`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/StructuredTaskScope.Subtask.html) rather than a [`Future`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/Future.html). We here propose to re-preview the API in JDK 22, without change, in order to gain more feedback.

## JEP 480: Structured Concurrency (Third Preview) - JDK 23

Structured Concurrency was proposed by [JEP 428](https://openjdk.org/jeps/428) and delivered in [JDK 19](https://openjdk.org/projects/jdk/19/) as an incubating API. It was re-incubated by [JEP 437](https://openjdk.org/jeps/437) in [JDK 20](https://openjdk.org/projects/jdk/20/) with a minor update to inherit scoped values ([JEP 429](https://openjdk.org/jeps/429)). It first previewed in [JDK 21](https://openjdk.org/projects/jdk/21/) via [JEP 453](https://openjdk.org/jeps/453) with [`StructuredTaskScope`](https://docs.oracle.com/en/java/javase/21/docsapi/java.base/java/util/concurrent/StructuredTaskScope.html)`::`[`fork(...)`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/StructuredTaskScope.html#fork(java.util.concurrent.Callable)) changed to return a [`Subtask`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/StructuredTaskScope.Subtask.html) rather than a [`Future`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/Future.html). It re-previewed in [JDK 22](https://openjdk.org/projects/jdk/22/) via [JEP 462](https://openjdk.org/jeps/462), without change. We here propose to re-preview the API once more in JDK 23, without change, in order to gain more feedback.

## JEP 499: Structured Concurrency (Fourth Preview) - JDK 24

Structured Concurrency was proposed by [JEP 428](https://openjdk.org/jeps/428) and delivered in [JDK 19](https://openjdk.org/projects/jdk/19/) as an incubating API. It was re-incubated by [JEP 437](https://openjdk.org/jeps/437) in [JDK 20](https://openjdk.org/projects/jdk/20/) with a minor update to inherit scoped values ([JEP 429](https://openjdk.org/jeps/429)). It first previewed in [JDK 21](https://openjdk.org/projects/jdk/21/) via [JEP 453](https://openjdk.org/jeps/453) with [`StructuredTaskScope`](https://docs.oracle.com/en/java/javase/21/docsapi/java.base/java/util/concurrent/StructuredTaskScope.html)`::`[`fork(...)`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/StructuredTaskScope.html#fork(java.util.concurrent.Callable)) changed to return a [`Subtask`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/StructuredTaskScope.Subtask.html) rather than a [`Future`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/Future.html). It re-previewed in [JDK 22](https://openjdk.org/projects/jdk/22/) via [JEP 462](https://openjdk.org/jeps/462), and [JDK 23](https://openjdk.org/projects/jdk/23/) via [JEP 480](https://openjdk.org/jeps/480), without change.

We here propose to re-preview the API once more in JDK 24, without change, to give more time for feedback from real world usage.

## Structured concurrency和Unstructured concurrency (ExecutorService)

1. 错误处理和传播：

```java
// Unstructured concurrency - 错误处理复杂
public class UnstructuredErrorHandling {
    Response handle(ExecutorService executor) {
        Future<String> userFuture = executor.submit(() -> findUser());
        Future<Integer> orderFuture = executor.submit(() -> fetchOrder());
        
        try {
            String user = userFuture.get();
            try {
                int order = orderFuture.get();
                return new Response(user, order);
            } catch (Exception e) {
                // orderFuture失败，但userFuture已完成
                userFuture.cancel(true); // 可能已经太晚
                throw e;
            }
        } catch (Exception e) {
            // userFuture失败，需要手动取消orderFuture
            orderFuture.cancel(true);
            throw new RuntimeException(e);
        }
    }
}

// Structured concurrency - 自动错误传播
public class StructuredErrorHandling {
    Response handle() throws ExecutionException, InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Future<String> user = scope.fork(() -> findUser());
            Future<Integer> order = scope.fork(() -> fetchOrder());
            
            scope.join();            // 等待所有任务
            scope.throwIfFailed();   // 如果有任务失败，自动取消其他任务
            
            return new Response(user.resultNow(), order.resultNow());
        }
    }
}
```

2. 资源管理：

```java
// Unstructured concurrency - 手动资源管理
public class UnstructuredResourceManagement {
    void processWithResources() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<?> task1 = executor.submit(() -> process1());
            Future<?> task2 = executor.submit(() -> process2());
            
            try {
                task1.get();
                task2.get();
            } catch (Exception e) {
                task1.cancel(true);
                task2.cancel(true);
                throw new RuntimeException(e);
            }
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
    }
}

// Structured concurrency - 自动资源管理
public class StructuredResourceManagement {
    void processWithResources() {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            scope.fork(() -> process1());
            scope.fork(() -> process2());
            
            scope.join();
            scope.throwIfFailed();
        } // 自动清理资源
    }
}
```

3. 任务取消：

```java
// Unstructured concurrency - 复杂的取消逻辑
public class UnstructuredCancellation {
    void processWithTimeout(ExecutorService executor) {
        List<Future<?>> futures = new ArrayList<>();
        try {
            futures.add(executor.submit(() -> task1()));
            futures.add(executor.submit(() -> task2()));
            
            // 超时处理
            try {
                for (Future<?> future : futures) {
                    future.get(5, TimeUnit.SECONDS);
                }
            } catch (TimeoutException e) {
                // 需要手动取消所有任务
                futures.forEach(f -> f.cancel(true));
                throw e;
            }
        } catch (Exception e) {
            // 错误发生时需要取消所有任务
            futures.forEach(f -> f.cancel(true));
            throw new RuntimeException(e);
        }
    }
}

// Structured concurrency - 自动取消
public class StructuredCancellation {
    void processWithTimeout() throws TimeoutException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            scope.fork(() -> task1());
            scope.fork(() -> task2());
            
            // 超时自动取消所有任务
            scope.joinUntil(Instant.now().plus(Duration.ofSeconds(5)));
            scope.throwIfFailed();
        }
    }
}
```

4. 可观察性：

```java
// Unstructured concurrency - 难以追踪任务关系
public class UnstructuredObservability {
    void process(ExecutorService executor) {
        Future<?> parent = executor.submit(() -> {
            // 子任务与父任务无明显关系
            Future<?> child = executor.submit(() -> childTask());
            try {
                child.get();
            } catch (Exception e) {
                // 错误处理复杂
            }
        });
    }
}

// Structured concurrency - 清晰的任务层次
public class StructuredObservability {
    void process() {
        try (var scope = new StructuredTaskScope<>()) {
            // 清晰的父子任务关系
            scope.fork(() -> {
                try (var childScope = new StructuredTaskScope<>()) {
                    childScope.fork(() -> childTask());
                    childScope.join();
                }
            });
            scope.join();
        }
    }
}
```

5. 异常处理策略：

```java
// Unstructured concurrency - 自定义异常处理复杂
public class UnstructuredExceptionHandling {
    void handleWithCustomStrategy(ExecutorService executor) {
        List<Future<?>> futures = new ArrayList<>();
        List<Exception> exceptions = new ArrayList<>();
        
        try {
            futures.add(executor.submit(() -> task1()));
            futures.add(executor.submit(() -> task2()));
            
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    exceptions.add(e);
                    // 需要手动决定是否取消其他任务
                    futures.forEach(f -> f.cancel(true));
                }
            }
            
            if (!exceptions.isEmpty()) {
                // 处理收集的异常
                handleExceptions(exceptions);
            }
        } finally {
            futures.forEach(f -> f.cancel(true));
        }
    }
}

// Structured concurrency - 内置异常处理策略
public class StructuredExceptionHandling {
    // 使用ShutdownOnFailure策略
    void handleWithFailureStrategy() {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            scope.fork(() -> task1());
            scope.fork(() -> task2());
            
            scope.join();
            scope.throwIfFailed(); // 自动处理失败
        }
    }
    
    // 使用ShutdownOnSuccess策略
    void handleWithSuccessStrategy() {
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Result>()) {
            scope.fork(() -> task1());
            scope.fork(() -> task2());
            
            scope.join();
            Result result = scope.result(); // 获取第一个成功结果
        }
    }
}
```



