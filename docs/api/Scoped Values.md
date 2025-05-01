# Scoped Values 作用域值

## JEP 429: Scoped Values (Incubator) - JDK 20

1. Core Purpose: 核心目的：

- Provides a way to safely share immutable data within and across threads 提供一种在线程内部和跨线程间安全共享不可变数据的方式
- Better alternative to `ThreadLocal` variables, especially for virtual threads 作为 `ThreadLocal` 变量的更好替代方案，特别是在虚拟线程场景下

2. Basic Usage: 基本用法：

```java
// Creating a scoped value // 创建作用域值
final static ScopedValue<String> USER_ID = ScopedValue.newInstance();

// Using scoped value // 使用作用域值
void processRequest() {
    ScopedValue.where(USER_ID, "user-123")
        .run(() -> {
            // Value available in this scope // 在这个作用域内可以访问该值
            handleRequest();
            performOperation();
        });
}

void performOperation() {
    // Access the value anywhere in the call chain // 在调用链的任何位置访问该值
    String userId = USER_ID.get();
    // Use userId...
}
```

3. Real-world Example (Web Framework): Web框架实例：

```java
class Server {
    // Define scoped value for Principal // 定义用于Principal的作用域值
    final static ScopedValue<Principal> PRINCIPAL = ScopedValue.newInstance();

    void serve(Request request, Response response) {
        var level = request.isAuthorized() ? ADMIN : GUEST;
        var principal = new Principal(level);
        
        // Bind the principal for this request // 为这个请求绑定principal
        ScopedValue.where(PRINCIPAL, principal)
                  .run(() -> Application.handle(request, response));
    }
}

class DBAccess {
    DBConnection open() {
        // Access principal anywhere in the call chain // 在调用链的任何位置访问principal
        var principal = Server.PRINCIPAL.get();
        if (!principal.canOpen()) {
            throw new InvalidPrincipalException();
        }
        return newConnection();
    }
}
```

4. Rebinding Example: 重新绑定示例：

```java
class Logger {
    void log(Supplier<String> formatter) {
        if (loggingEnabled) {
            // Create guest principal for logging // 创建访客权限的principal
            var guest = Principal.createGuest();
            
            // Rebind principal for logging operation // 为日志操作重新绑定principal
            var message = ScopedValue.where(Server.PRINCIPAL, guest)
                                   .call(() -> formatter.get());
                                   
            write(logFile, "%s %s".format(timeStamp(), message));
        }
    }
}
```

5. Structured Concurrency Integration: 结构化并发集成：

```java
class Application {
    Response handle() throws ExecutionException, InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Principal automatically inherited by child threads // Principal自动被子线程继承
            Future<String> user = scope.fork(() -> findUser());
            Future<Integer> order = scope.fork(() -> fetchOrder());
            
            scope.join().throwIfFailed();
            return new Response(user.resultNow(), order.resultNow());
        }
    }
    
    String findUser() {
        // Can access PRINCIPAL.get() here in child thread // 可以在子线程中访问 PRINCIPAL.get()
        return DBAccess.open().queryUser();
    }
}
```

6. Comparison with `ThreadLocal`: 与`ThreadLocal`比较：

```java
// Old way with ThreadLocal // 旧方式：使用ThreadLocal
class Server {
    final static ThreadLocal<Principal> principal = new ThreadLocal<>();
    
    void serve(Request request) {
        principal.set(new Principal(level));
        try {
            handleRequest();
        } finally {
            principal.remove(); // Must clean up // 必须手动清理
        }
    }
}

// New way with ScopedValue // 新方式：使用ScopedValue
class Server {
    final static ScopedValue<Principal> PRINCIPAL = ScopedValue.newInstance();
    
    void serve(Request request) {
        ScopedValue.where(PRINCIPAL, new Principal(level))
                  .run(() -> handleRequest());
        // Automatic cleanup // 自动清理
    }
}
```

7. Key Benefits: 主要优势：

```java
// 1. Immutability // 1. 不可变性
final static ScopedValue<User> CURRENT_USER = ScopedValue.newInstance();
// Value can't be modified once bound // 一旦绑定，值就不能被修改

// 2. Bounded Lifetime // 2. 有界生命周期
ScopedValue.where(CURRENT_USER, user)
          .run(() -> {
              // Value only available within this scope // 值只在这个作用域内可用
          });

// 3. Inheritance in Virtual Threads // 3. 虚拟线程中的继承
try (var scope = new StructuredTaskScope<>()) {
    // Values automatically inherited by child threads // 值自动被子线程继承
    scope.fork(() -> task1());
    scope.fork(() -> task2());
}
```

8. Best Practices: 最佳实践：

```java
// 1. Declare as static final // 1. 声明为静态final
public final static ScopedValue<Context> CONTEXT = ScopedValue.newInstance();

// 2. Handle missing values // 2. 处理值不存在的情况
void process() {
    try {
        var context = CONTEXT.get();
    } catch (NoSuchElementException e) {
        // Handle case when value not bound
    }
}

// 3. Use try-with-resources for structured concurrency // 3. 使用try-with-resources进行结构化并发
try (var scope = new StructuredTaskScope<>()) {
    // Work with scoped values
}
```

9. Key Points: 关键特点：

- Values are immutable once bound 值一旦绑定就不可变
- Automatic cleanup when scope ends 作用域结束时自动清理
- Efficient inheritance in virtual threads 在虚拟线程中高效继承
- Type-safe access 类型安全的访问
- Clear visibility of data flow 清晰的数据流向
- Better memory characteristics than `ThreadLocal` 比 `ThreadLocal` 有更好的内存特性

10. 使用场景：

- Web应用中的请求上下文
- 安全认证信息传递
- 分布式追踪
- 事务上下文
- 日志上下文

11. 注意事项：

- 作用域值应该是不可变的
- 避免在作用域值中存储大量数据
- 注意处理值不存在的情况
- 合理规划作用域范围
- 考虑线程继承的影响

## JEP 446: Scoped Values (Preview) - JDK 21

Key changes from JEP 429 (Scoped Values Incubator) to JEP 446 (Scoped Values Preview):

1. API Status Change:

```java
// JEP 429 - Incubator API
// Required incubator module
requires jdk.incubator.concurrent;

// JEP 446 - Preview API
// Now part of java.base module
import java.lang.runtime.ScopedValue;
```

2. Core API Refinements:

```java
// JEP 429
final static ScopedValue<String> VALUE = ScopedValue.newInstance();

// JEP 446 - More explicit API
public class Example {
    // Creation remains the same
    private final static ScopedValue<String> VALUE = ScopedValue.newInstance();
    
    // Enhanced where() method with multiple bindings
    void process() {
        ScopedValue.where(VALUE, "data")
                  .where(OTHER_VALUE, "other")
                  .run(() -> doWork());
    }
    
    // New callWhere() method for convenience
    String compute() {
        return ScopedValue.callWhere(VALUE, "data", () -> {
            return processWithValue();
        });
    }
}
```

3. Improved Error Handling:

```java
// JEP 446 adds clearer exception handling
try {
    String value = VALUE.get();
} catch (NoSuchElementException e) {
    // More predictable exception behavior
    // Better documentation on when exceptions occur
}
```

4. Enhanced Thread Integration:

```java
// Better integration with structured concurrency
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    // Clearer semantics for inheritance
    scope.fork(() -> {
        // Values automatically inherited
        String value = VALUE.get();
        return processData(value);
    });
    
    scope.join();
}
```

5. Performance Improvements:

```java
// JEP 446 optimizes common patterns
public class OptimizedExample {
    private static final ScopedValue<Context> CONTEXT = ScopedValue.newInstance();
    
    public void optimizedProcess() {
        // More efficient binding and access
        ScopedValue.where(CONTEXT, new Context())
                  .run(() -> {
                      // Optimized access pattern
                      Context ctx = CONTEXT.get();
                      // Use context...
                  });
    }
}
```

6. Documentation Clarifications:

```java
// JEP 446 provides clearer guidance
public class BestPractices {
    // 1. Proper declaration
    private static final ScopedValue<State> STATE = ScopedValue.newInstance();
    
    // 2. Clear scope boundaries
    public void process() {
        ScopedValue.where(STATE, new State())
                  .run(() -> {
                      // Clear documentation on lifetime
                      // Better guidance on inheritance
                      doWork();
                  });
    }
}
```

7. Security Enhancements:

```java
public class SecurityExample {
    private static final ScopedValue<SecurityContext> SECURITY = ScopedValue.newInstance();
    
    public void secureOperation() {
        // JEP 446 provides better security guarantees
        SecurityContext ctx = createSecurityContext();
        ScopedValue.where(SECURITY, ctx)
                  .run(() -> {
                      // Improved immutability guarantees
                      performSecureOperation();
                  });
    }
}
```

8. Key Changes Summary:

```java
// 1. Status Change
// - Moved from incubator to preview
// - Part of java.base module

// 2. API Improvements
// - Enhanced where() method
// - New convenience methods
// - Better error handling

// 3. Performance
// - Optimized common patterns
// - Better thread inheritance

// 4. Documentation
// - Clearer usage guidelines
// - Better examples
// - More detailed specifications

// 5. Security
// - Stronger guarantees
// - Better immutability
```

9. Migration Example:

```java
// JEP 429
import jdk.incubator.concurrent.ScopedValue;

class OldCode {
    static final ScopedValue<String> OLD = ScopedValue.newInstance();
    
    void oldMethod() {
        ScopedValue.where(OLD, "value").run(() -> {
            // Old pattern
        });
    }
}

// JEP 446
import java.lang.runtime.ScopedValue;

class NewCode {
    static final ScopedValue<String> NEW = ScopedValue.newInstance();
    
    void newMethod() {
        // New pattern with improved API
        ScopedValue.where(NEW, "value")
                  .where(OTHER, "other")
                  .run(() -> {
                      // Enhanced functionality
                  });
    }
}
```

10. Best Practices Updates:

```java
public class UpdatedBestPractices {
    // 1. Declare as private when possible
    private static final ScopedValue<Context> CONTEXT = ScopedValue.newInstance();
    
    // 2. Use structured concurrency
    public void structuredOperation() {
        try (var scope = new StructuredTaskScope<>()) {
            ScopedValue.where(CONTEXT, new Context())
                      .run(() -> {
                          // Better structured approach
                          scope.fork(() -> task1());
                          scope.fork(() -> task2());
                          scope.join();
                      });
        }
    }
    
    // 3. Handle errors appropriately
    public void errorHandling() {
        try {
            Context ctx = CONTEXT.get();
        } catch (NoSuchElementException e) {
            // Better error handling guidance
        }
    }
}
```

The main themes of changes in JEP 446 are:

1. API Maturity
2. Better Integration
3. Performance Optimization
4. Enhanced Security
5. Clearer Documentation
6. Improved Error Handling

## JEP 464: Scoped Values (Second Preview) - JDK 22

Scoped values incubated in JDK 20 via [JEP 429](https://openjdk.org/jeps/429) and became a preview API in JDK 21 via [JEP 446](https://openjdk.org/jeps/446). We here propose to re-preview the API in JDK 22, without change, in order to gain additional experience and feedback.

## JEP 481: Scoped Values (Third Preview) - JDK 23

The scoped values API incubated in JDK 20 via [JEP 429](https://openjdk.org/jeps/429), became a preview API in JDK 21 via [JEP 446](https://openjdk.org/jeps/446), and was re-previewed in JDK 22 via [JEP 464](https://openjdk.org/jeps/464).

We here propose to re-preview the API in JDK 23 in order to gain additional experience and feedback, with one change:

- The type of the operation parameter of the [`ScopedValue.callWhere`](https://cr.openjdk.org/~alanb/sv-20240517/java.base/java/lang/ScopedValue.html#callWhere(java.lang.ScopedValue,T,java.lang.ScopedValue.CallableOp)) method is now a new functional interface which allows the Java compiler to infer whether a checked exception might be thrown. With this change, the [`ScopedValue.getWhere`](https://docs.oracle.com/en/java/javase/22/docs/api/java.base/java/lang/ScopedValue.html#getWhere(java.lang.ScopedValue,T,java.util.function.Supplier)) method is no longer needed and is removed.

1. Key Change - callWhere Method Enhancement:

```java
// JEP 446
public class OldExample {
    static final ScopedValue<String> VALUE = ScopedValue.newInstance();
    
    void oldMethod() throws Exception {
        String result = ScopedValue.callWhere(VALUE, "data", () -> {
            // Compiler couldn't properly infer checked exceptions
            return processData();
        });
    }
}

// JEP 481 - New Carrier interface
public class NewExample {
    static final ScopedValue<String> VALUE = ScopedValue.newInstance();
    
    void newMethod() throws IOException {  // More precise exception handling
        String result = ScopedValue.callWhere(VALUE, "data", () -> {
            // Compiler can now infer IOException
            return processData();
        });
    }
}
```

2. Removal of getWhere Method:

```java
// JEP 446 - Had both methods
class OldAPI {
    // Now removed
    public static <V> V getWhere(ScopedValue<V> value, V newValue);
    public static <R> R callWhere(ScopedValue<?> value, Object newValue, Callable<R> op);
}

// JEP 481 - Simplified API
class NewAPI {
    // Only callWhere with new Carrier interface
    public static <R, X extends Throwable> R callWhere(
        ScopedValue<?> value, 
        Object newValue, 
        Carrier<R, X> op) throws X;
}
```

## JEP 487: Scoped Values (Fourth Preview) - JDK 24

The scoped values API was proposed for incubation by [JEP 429](https://openjdk.org/jeps/429) (JDK 20), proposed for preview by [JEP 446](https://openjdk.org/jeps/446) (JDK 21), and subsequently improved and refined by [JEP 464](https://openjdk.org/jeps/464) (JDK 22) and [JEP 481](https://openjdk.org/jeps/481) (JDK 23).

We here propose to re-preview the API once more in JDK 24 in order to gain additional experience and feedback, with one further change:

- We removed the `callWhere` and `runWhere` methods from the `ScopedValue` class, leaving the API completely [fluent](https://en.wikipedia.org/wiki/Fluent_interface). The only way to use one or more bound scoped values is via the `ScopedValue.Carrier.call` and `ScopedValue.Carrier.run` methods.

1. API Simplification - Removal of callWhere and runWhere:

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

2. Multiple Bindings - More Elegant Syntax:

```java
// JEP 487 - Fluent API for multiple bindings
public class MultiBindingExample {
    static final ScopedValue<String> NAME = ScopedValue.newInstance();
    static final ScopedValue<Integer> AGE = ScopedValue.newInstance();
    
    void process() {
        ScopedValue.where(NAME, "John")
                  .where(AGE, 30)
                  .run(() -> {
                      String name = NAME.get();
                      int age = AGE.get();
                      // Use values...
                  });
    }
}
```

3. Carrier Interface Usage:

```java
public class CarrierExample {
    static final ScopedValue<Context> CTX = ScopedValue.newInstance();
    
    // Using Carrier for checked exceptions
    String processWithException() throws IOException {
        return ScopedValue.where(CTX, new Context())
                         .call(() -> {
                             // Compiler correctly infers IOException
                             return readFile();
                         });
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