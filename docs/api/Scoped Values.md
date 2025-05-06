# Scoped Values 作用域值

## JEP 429: Scoped Values (Incubator) - JDK 20

作用域值（Scoped Values），允许在线程内部和跨线程之间安全共享不可变数据。与线程局部变量相比，作用域值更适合在大量虚拟线程的场景中使用。此为孵化阶段API。

`ThreadLocal`存在以下问题：

1. **无约束可变性**：任何线程均可修改`ThreadLocal`值，导致数据流难以追踪。
2. **无界生命周期**：线程结束后`ThreadLocal`值可能仍驻留内存，引发内存泄漏。
3. **继承开销**：子线程默认继承父线程的`ThreadLocal`值，大量线程时内存占用显著增加。

虚拟线程是由JDK实现的轻量级线程，多个虚拟线程可以共享同一个操作系统线程，从而支持海量并发。

虚拟线程是`Thread`的实例，它们可以拥有线程局部变量；事实上，虚拟线程的短生命周期和非池化特性使得长期内存泄漏问题不再那么突出。（当线程快速终止时，无需调用线程局部变量的`remove()`方法，因为线程终止会自动清理其线程局部变量。）然而，如果一百万个虚拟线程各自持有可变的线程局部变量，内存占用可能会显著增加。

具有线程局部变量的 Web 框架示例
```java
/**
 * 1. 使用ThreadLocal确保每个请求线程都有其独立的用户权限上下文
 * 2. 在处理请求时设置用户权限级别
 * 3. 在需要访问数据库时进行权限检查
 * 4. 这种设计模式有助于实现线程安全的权限管理，适用于多线程web服务器环境
 */
class Server {
    // 使用ThreadLocal存储Principal对象，确保线程安全
    // 每个线程都有自己独立的Principal实例，避免线程间互相影响
    final static ThreadLocal<Principal> PRINCIPAL = new ThreadLocal<>();  // (1)

    void serve(Request request, Response response) {
        // 根据请求的授权状态确定用户级别(ADMIN或GUEST)
        var level     = (request.isAuthorized() ? ADMIN : GUEST);
        // 创建新的Principal对象，包含用户权限级别
        var principal = new Principal(level);
        // 将principal对象存储在当前线程的ThreadLocal中
        PRINCIPAL.set(principal);                                         // (2)
        // 调用应用程序处理请求
        Application.handle(request, response);
    }
}

class DBAccess {
    DBConnection open() {
        // 从当前线程的ThreadLocal中获取Principal对象
        var principal = Server.PRINCIPAL.get();                           // (3)
        // 检查用户是否有权限打开数据库连接
        if (!principal.canOpen()) throw new InvalidPrincipalException();
        // 如果权限验证通过，创建并返回新的数据库连接
        return newConnection(...);                                        // (4)
    }
}
```
作用域值（Scoped Values）允许数据在大型程序的组件之间安全高效地共享，而无需依赖方法参数。它是一个`ScopedValue`类型的变量。通常将其声明为`final static`字段，以便多个组件轻松访问。与线程局部变量类似，作用域值具有多个实例（每个线程一个），具体使用的实例取决于调用其方法的线程。但与线程局部变量不同，作用域值一旦写入后便不可变，且仅在当前线程执行期间的有限时间段内可用。

具有作用域值的 Web 框架示例

```java
/**
 * 1. 使用ScopedValue替代传统的ThreadLocal，提供了更严格的作用域控制
 * 2. 在处理请求时创建一个特定的作用域，并在该作用域内执行业务逻辑
 * 3. 确保权限信息只能在正确的上下文中访问
 * 4. 这种实现方式比ThreadLocal更安全，因为它强制了变量的作用域边界
 */
class Server {
    // 声明一个静态的ScopedValue实例，用于存储Principal对象
    // ScopedValue是Java新特性，提供了一种更安全的线程局部变量机制
    final static ScopedValue<Principal> PRINCIPAL = ScopedValue.newInstance(); // (1)

    void serve(Request request, Response response) {
        // 根据请求判断用户权限级别(ADMIN或GUEST)
        var level = (request.isAdmin() ? ADMIN : GUEST);
        // 创建新的Principal对象，包含用户权限信息
        var principal = new Principal(level);
        // 使用ScopedValue.where创建一个新的作用域，并在该作用域内运行处理逻辑
        // 这确保principal对象只在特定作用域内可访问
        ScopedValue.where(PRINCIPAL, principal)                            // (2)
                   .run(() -> Application.handle(request, response));
    }
}

class DBAccess {
    DBConnection open() {
        // 从当前作用域获取Principal对象
        // 如果在作用域外调用会抛出异常，提供了额外的安全保障
        var principal = Server.PRINCIPAL.get();                            // (3)
        // 验证用户是否具有打开数据库连接的权限
        if (!principal.canOpen()) throw new InvalidPrincipalException();
        // 权限验证通过后，创建并返回新的数据库连接
        return newConnection(...);                                         // (4)
    }
}
```
作用域值的重新绑定

```java
/**
 * 1. 使用访客权限执行日志记录，确保最小权限原则
 * 2. 利用ScopedValue创建独立的作用域，避免干扰现有的权限上下文
 * 3. 在特定作用域内安全地执行日志消息格式化
 * 4. 整体设计确保了日志记录过程的安全性和隔离性
 */
class Logger {
    void log(Supplier<String> formatter) {
        if (loggingEnabled) {
            // 创建一个访客权限的Principal对象
            var guest = Principal.createGuest();                      // (1)

            // 使用ScopedValue.where创建一个新的作用域
            // 在这个作用域中，将Server.PRINCIPAL绑定到guest对象
            var message = ScopedValue.where(Server.PRINCIPAL, guest)  // (2)
                            // 在新作用域中执行formatter.get()获取日志消息
                            // 使用lambda表达式确保在正确的作用域内执行格式化操作
                            .call(() -> formatter.get());             // (3)

            // 将格式化后的日志消息写入日志文件
            // 包含时间戳和消息内容
            write(logFile, "%s %s".format(timeStamp(), message));
        }
    }
}
```

用户代码创建虚拟线程的首选机制是结构化并发API（JEP 428），具体通过`StructuredTaskScope`类实现。使用`StructuredTaskScope`创建的子线程会自动继承父线程的作用域值。子线程中的代码可以直接使用父线程中绑定的作用域值，且开销极低。与线程本地变量不同，此过程不会将父线程的作用域值绑定复制到子线程。

```java
/**
 * 1. 使用StructuredTaskScope管理并发任务，提供了更好的错误处理和资源管理
 * 2. 通过fork方法并行执行多个任务，提高程序效率
 * 3. 确保所有任务要么全部成功，要么在出现错误时全部终止
 * 4. 在并发环境中安全地处理数据库连接和权限验证
 */
class Application {
    // 处理请求的方法，可能抛出ExecutionException和InterruptedException异常
    Response handle() throws ExecutionException, InterruptedException {
        // 创建一个StructuredTaskScope实例，使用ShutdownOnFailure策略
        // 当任何任务失败时，会关闭所有其他任务
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // 异步执行findUser()方法获取用户信息
            // fork()方法会创建一个新的子任务并立即开始执行
            Future<String>  user  = scope.fork(() -> findUser());          // (1)
            
            // 异步执行fetchOrder()方法获取订单信息
            // 与获取用户信息的任务并行执行
            Future<Integer> order = scope.fork(() -> fetchOrder());        // (2)
            
            // 等待所有子任务完成
            // throwIfFailed()会在任何任务失败时抛出异常
            scope.join().throwIfFailed();  // Wait for both forks
            
            // 创建并返回包含用户信息和订单信息的响应对象
            // resultNow()方法用于获取已完成任务的结果
            return new Response(user.resultNow(), order.resultNow());
        }
    }

    // 查找用户信息的方法
    String findUser() {
        // 打开数据库连接并执行查询操作
        ... DBAccess.open() ...                                            // (3)
    }
}

class DBAccess {
    // 打开数据库连接的方法
    DBConnection open() {
        // 获取当前线程的权限信息
        var principal = Server.PRINCIPAL.get();                            // (4)
        // 验证用户是否有权限打开数据库连接
        if (!principal.canOpen()) throw new  InvalidPrincipalException();
        // 创建并返回新的数据库连接
        return newConnection(...);
    }
}
```

## JEP 446: Scoped Values (Preview) - JDK 21

Scoped Values 在 JDK 20 中通过 JEP 429 提案进入孵化阶段。在 JDK 21 中，该功能不再处于孵化状态，转而成为预览 API。

以下比对基于Oracle Open JDK 20和Oracle Open JDK 21：

包名变化：

```java
// JDK 20 (JEP 429)
jdk.incubator.concurrent.ScopedValue

// JDK 21 (JEP 446)
java.lang.ScopedValue
```

where方法的变化：

```java
// JDK 20 (JEP 429)
static <T> Carrier where(ScopedValue<T> key, T value)
static <T, R> R where(ScopedValue<T> key, T value, Callable<? extends R> op) throws Exception
static <T> void where(ScopedValue<T> key, T value, Runnable op)

// JDK 21 (JEP 446)
static <T> Carrier where(ScopedValue<T> key, T value)
static <T, R> R callWhere(ScopedValue<T> key, T value, Callable<? extends R> op) throws Exception
static <T> void runWhere(ScopedValue<T> key, T value, Runnable op)
static <T, R> R getWhere(ScopedValue<T> key, T value, Supplier<? extends R> op)
```

## JEP 464: Scoped Values (Second Preview) - JDK 22

Scoped Values 在 JDK 20 中通过 JEP 429 进行孵化，并在 JDK 21 中通过 JEP 446 成为预览 API。我们在此建议在 JDK 22 中重新将此 API 作为预览 API（保持不变），以便获取更多实践经验和用户反馈。

## JEP 481: Scoped Values (Third Preview) - JDK 23

The scoped values API incubated in JDK 20 via [JEP 429](https://openjdk.org/jeps/429), became a preview API in JDK 21 via [JEP 446](https://openjdk.org/jeps/446), and was re-previewed in JDK 22 via [JEP 464](https://openjdk.org/jeps/464).

We here propose to re-preview the API in JDK 23 in order to gain additional experience and feedback, with one change:

- The type of the operation parameter of the [`ScopedValue.callWhere`](https://cr.openjdk.org/~alanb/sv-20240517/java.base/java/lang/ScopedValue.html#callWhere(java.lang.ScopedValue,T,java.lang.ScopedValue.CallableOp)) method is now a new functional interface which allows the Java compiler to infer whether a checked exception might be thrown. With this change, the [`ScopedValue.getWhere`](https://docs.oracle.com/en/java/javase/22/docs/api/java.base/java/lang/ScopedValue.html#getWhere(java.lang.ScopedValue,T,java.util.function.Supplier)) method is no longer needed and is removed.

  ScopedValue.callWhere 方法的操作参数类型现在变为一个新的函数式接口，该接口允许 Java 编译器推断可能抛出的受检异常。通过这一变更，ScopedValue.getWhere 方法已不再需要并被移除。
  

```java
// JDK 21 (JEP 446)
static <T> Carrier where(ScopedValue<T> key, T value)
static <T, R> R callWhere(ScopedValue<T> key, T value, Callable<? extends R> op) throws Exception
static <T> void runWhere(ScopedValue<T> key, T value, Runnable op)
static <T, R> R getWhere(ScopedValue<T> key, T value, Supplier<? extends R> op)

// JDK 23 (JEP 481)
static <T> Carrier where(ScopedValue<T> key, T value)
static <T, R, X extends Throwable> R callWhere(ScopedValue<T> key, T value, CallableOp<? extends R, X> op) throws X
static <T> void runWhere(ScopedValue<T> key, T value, Runnable op)
```

## JEP 487: Scoped Values (Fourth Preview) - JDK 24

The scoped values API was proposed for incubation by [JEP 429](https://openjdk.org/jeps/429) (JDK 20), proposed for preview by [JEP 446](https://openjdk.org/jeps/446) (JDK 21), and subsequently improved and refined by [JEP 464](https://openjdk.org/jeps/464) (JDK 22) and [JEP 481](https://openjdk.org/jeps/481) (JDK 23).

We here propose to re-preview the API once more in JDK 24 in order to gain additional experience and feedback, with one further change:

- We removed the `callWhere` and `runWhere` methods from the `ScopedValue` class, leaving the API completely [fluent](https://en.wikipedia.org/wiki/Fluent_interface). The only way to use one or more bound scoped values is via the `ScopedValue.Carrier.call` and `ScopedValue.Carrier.run` methods.

  我们从ScopedValue类中移除了callWhere和runWhere方法，使API完全保持流畅。使用一个或多个绑定作用域值的唯一方式，是通过ScopedValue.Carrier.call和ScopedValue.Carrier.run方法实现。

```java
// JDK 23 (JEP 481)
static <T> Carrier where(ScopedValue<T> key, T value)
static <T, R, X extends Throwable> R callWhere(ScopedValue<T> key, T value, CallableOp<? extends R, X> op) throws X
static <T> void runWhere(ScopedValue<T> key, T value, Runnable op)
    
// JDK 24 (JEP 487)
static <T> Carrier where(ScopedValue<T> key, T value)
```

```java
// JEP 481 - Previous API
public class OldExample {
    static final ScopedValue<String> VALUE = ScopedValue.newInstance();
    
    void oldMethod() {
        // Old way using callWhere
        String result = ScopedValue.callWhere(VALUE, "data", () -> {
            return processData();
        });
        
        // Old way using runWhere
        ScopedValue.runWhere(VALUE, "data", () -> {
            processData();
        });
    }
}

// JEP 487 - New Fluent API
public class NewExample {
    static final ScopedValue<String> VALUE = ScopedValue.newInstance();
    
    void newMethod() {
        // New fluent way using where().call()
        String result = ScopedValue.where(VALUE, "data")
                                 .call(() -> processData());
        
        // New fluent way using where().run()
        ScopedValue.where(VALUE, "data")
                  .run(() -> processData());
    }
}
```

## JEPs comparison

1. JEP 446 (First Preview):

```java
// Key Points:
// - Initial preview of ScopedValue API
// - Introduction of basic methods: where, callWhere, runWhere
// - Part of java.base module
// - Focus on thread safety and immutability

public class JEP446Example {
    private static final ScopedValue<UserContext> USER = ScopedValue.newInstance();
    
    public void example() {
        // Using callWhere
        String result = ScopedValue.callWhere(USER, new UserContext(), () -> {
            return processUser();
        });
        
        // Using runWhere
        ScopedValue.runWhere(USER, new UserContext(), () -> {
            processUser();
        });
        
        // Multiple bindings (more verbose)
        ScopedValue.runWhere(USER, new UserContext(), () -> {
            ScopedValue.runWhere(OTHER, value, () -> {
                // Nested calls
            });
        });
    }
}
```

2. JEP 481 (Third Preview):

```java
// Key Points:
// - Introduction of new Carrier interface for better exception handling
// - Improved type inference for checked exceptions
// - Enhanced multiple bindings support
// - Removal of getWhere method

public class JEP481Example {
    private static final ScopedValue<DbConnection> DB = ScopedValue.newInstance();
    
    // Better exception handling with Carrier interface
    public Data processData() throws SQLException {
        return ScopedValue.callWhere(DB, new DbConnection(), () -> {
            // Compiler can now infer SQLException
            return executeQuery();
        });
    }
    
    // Multiple bindings still using nested structure
    public void multipleBindings() {
        ScopedValue.callWhere(DB, new DbConnection(), () -> {
            return ScopedValue.callWhere(OTHER, value, () -> {
                return process();
            });
        });
    }
}
```

3. JEP 487 (Fourth Preview):

```java
// Key Points:
// - Removal of callWhere and runWhere methods
// - Introduction of fluent API
// - Simplified multiple bindings
// - Cleaner and more elegant syntax

public class JEP487Example {
    private static final ScopedValue<UserContext> USER = ScopedValue.newInstance();
    private static final ScopedValue<TraceContext> TRACE = ScopedValue.newInstance();
    
    // Fluent API for single binding
    public void singleBinding() {
        ScopedValue.where(USER, new UserContext())
                  .run(() -> processUser());
    }
    
    // Elegant multiple bindings
    public void multipleBindings() {
        ScopedValue.where(USER, new UserContext())
                  .where(TRACE, new TraceContext())
                  .run(() -> {
                      processWithContext();
                  });
    }
    
    // Exception handling with fluent API
    public Data processWithException() throws SQLException {
        return ScopedValue.where(USER, new UserContext())
                         .call(() -> {
                             // Compiler correctly infers SQLException
                             return queryDatabase();
                         });
    }
    
    // Structured concurrency integration
    public void structuredConcurrency() throws InterruptedException {
        ScopedValue.where(USER, new UserContext())
                  .run(() -> {
                      try (var scope = new StructuredTaskScope<>()) {
                          scope.fork(() -> task1());
                          scope.fork(() -> task2());
                          scope.join();
                      }
                  });
    }
}
```

## ScopedValues与传统线程局部变量（ThreadLocal）相比有哪些优势？

1. 不可变性和安全性：

```java
// ThreadLocal - 可变性带来的问题
public class ThreadLocalExample {
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();
    
    void process() {
        userContext.set(new UserContext("user1")); // 可以随时修改
        doWork();
        // 忘记清理可能导致内存泄漏
    }
    
    void doWork() {
        userContext.set(new UserContext("user2")); // 任何地方都可以修改值
    }
}

// ScopedValue - 不可变且安全
public class ScopedValueExample {
    private static final ScopedValue<UserContext> USER_CONTEXT = ScopedValue.newInstance();
    
    void process() {
        ScopedValue.where(USER_CONTEXT, new UserContext("user1"))
                  .run(() -> {
                      doWork(); // 值在作用域内不可变
                  });
        // 自动清理，无需手动处理
    }
    
    void doWork() {
        // 只能读取，不能修改
        UserContext context = USER_CONTEXT.get();
    }
}
```

2. 生命周期管理：

```java
// ThreadLocal - 手动管理生命周期
public class ThreadLocalLifecycle {
    private static final ThreadLocal<Transaction> transaction = new ThreadLocal<>();
    
    void processTransaction() {
        transaction.set(new Transaction());
        try {
            doWork();
        } finally {
            transaction.remove(); // 必须手动清理
        }
    }
}

// ScopedValue - 自动生命周期管理
public class ScopedValueLifecycle {
    private static final ScopedValue<Transaction> TRANSACTION = ScopedValue.newInstance();
    
    void processTransaction() {
        ScopedValue.where(TRANSACTION, new Transaction())
                  .run(() -> {
                      doWork();
                  }); // 作用域结束自动清理
    }
}
```

3. 线程池场景：

```java
// ThreadLocal - 线程池中的问题
public class ThreadLocalPoolIssue {
    private static final ThreadLocal<UserSession> session = new ThreadLocal<>();
    
    void handleRequest(ExecutorService pool) {
        pool.submit(() -> {
            session.set(new UserSession()); // 可能污染线程池中的线程
            try {
                processRequest();
            } finally {
                session.remove(); // 如果忘记清理会影响后续任务
            }
        });
    }
}

// ScopedValue - 线程池中安全使用
public class ScopedValuePoolSafe {
    private static final ScopedValue<UserSession> SESSION = ScopedValue.newInstance();
    
    void handleRequest(ExecutorService pool) {
        pool.submit(() -> {
            ScopedValue.where(SESSION, new UserSession())
                      .run(() -> processRequest());
        }); // 自动清理，不会污染线程池
    }
}
```

4. 虚拟线程集成：

```java
// ThreadLocal - 虚拟线程中的开销
public class ThreadLocalVirtualThread {
    private static final ThreadLocal<Context> context = new ThreadLocal<>();
    
    void handleManyRequests() {
        for (int i = 0; i < 1000000; i++) {
            Thread.startVirtualThread(() -> {
                context.set(new Context()); // 每个虚拟线程都需要存储空间
                try {
                    processRequest();
                } finally {
                    context.remove();
                }
            });
        }
    }
}

// ScopedValue - 高效的虚拟线程支持
public class ScopedValueVirtualThread {
    private static final ScopedValue<Context> CONTEXT = ScopedValue.newInstance();
    
    void handleManyRequests() {
        for (int i = 0; i < 1000000; i++) {
            Thread.startVirtualThread(() -> {
                ScopedValue.where(CONTEXT, new Context())
                          .run(() -> processRequest());
            }); // 更高效的内存使用
        }
    }
}
```

5. 结构化并发支持：

```java
public class StructuredConcurrencyExample {
    private static final ScopedValue<TraceContext> TRACE = ScopedValue.newInstance();
    
    void parallelProcess() throws InterruptedException {
        ScopedValue.where(TRACE, new TraceContext())
                  .run(() -> {
                      try (var scope = new StructuredTaskScope<String>()) {
                          // 子任务自动继承TRACE值
                          scope.fork(() -> task1());
                          scope.fork(() -> task2());
                          scope.join();
                      }
                  });
    }
}
```

6. 异常处理：

```java
public class ExceptionHandlingExample {
    private static final ScopedValue<TransactionContext> TX = ScopedValue.newInstance();
    
    void processWithException() throws DatabaseException {
        ScopedValue.where(TX, new TransactionContext())
                  .call(() -> {
                      // 编译器可以正确推断异常类型
                      return executeTransaction();
                  });
    }
}
```

## ThreadLocal（线程本地变量）和ScopedValue（作用域值）的应用场景

ThreadLocal（线程本地变量）的适用场景：

- 使用传统的线程模型
- 需要可变的线程特定状态（例如调用栈深处的代码通过 `ThreadLocal.set(...)` 向远端调用者传递数据）
- 需要长期存储线程状态
- 处理遗留系统集成

ScopedValue（作用域值）的适用场景：

- 使用虚拟线程和结构化并发
- 需要不可变的上下文传递（单向传递不变数据）
- 需要明确的作用域边界
- 希望自动化资源清理

新项目优先考虑使用ScopedValue，除非有特殊需求

选择时要考虑项目特点、并发模型和维护需求