package org.example.api;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

/**
 * 1. Virtual Threads:
 * <p>
 * 使用 Thread.startVirtualThread()创建虚拟线程
 * <p>
 * StructuredTaskScope中的任务自动运行在虚拟线程上
 * <p>
 * 2. Structured Concurrency:
 * <p>
 * 使用 StructuredTaskScope管理并发任务
 * <p>
 * 自动的错误传播和取消机制
 * <p>
 * 清晰的任务层次结构
 * <p>
 * 3. Scoped Values:
 * <p>
 * 使用 ScopedValue传递请求上下文和跟踪ID
 * <p>
 * 线程安全的上下文传递
 * <p>
 * 自动的生命周期管理
 */
public class ModernConcurrencyExample {

  // 定义ScopedValue用于传递上下文
  private static final ScopedValue<RequestContext> REQUEST_CONTEXT = ScopedValue.newInstance();
  private static final ScopedValue<String> TRACE_ID = ScopedValue.newInstance();

  record RequestContext(String userId, Instant timestamp) {

  }

  record UserData(String userId, String name, String email) {

  }

  record OrderData(String orderId, Double amount) {

  }

  record Response(UserData user, OrderData order, String traceId) {

  }

  public static void main(String[] args) throws Exception {
    // 使用 Thread.startVirtualThread()创建虚拟线程, StructuredTaskScope中的任务自动运行在虚拟线程上
    final Thread virtualThread = Thread.startVirtualThread(() -> {
      try {
        processRequest("user123");
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    // 等待虚拟线程完成
    virtualThread.join();
    // 可以指定超时
    // virtualThread.join(5000); // 等待最多5秒
  }

  public static void processRequest(String userId) throws Exception {
    // 创建请求上下文
    var context = new RequestContext(userId, Instant.now());
    var traceId = generateTraceId();

    // 使用ScopedValues包装整个请求处理过程
    ScopedValue.where(REQUEST_CONTEXT, context)
        .where(TRACE_ID, traceId)
        .run(() -> {
          try {
            Response response = fetchDataConcurrently();
            logResponse(response);
          } catch (Exception e) {
            logError(e);
            throw new RuntimeException(e);
          }
        });
  }

  private static Response fetchDataConcurrently() throws Exception {
    // 使用StructuredTaskScope进行并发数据获取
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      // Fork任务到虚拟线程
      Subtask<UserData> userFuture = scope.fork(() -> {
        logOperation("Fetching user data");
        return fetchUserData(REQUEST_CONTEXT.get().userId());
      });

      Subtask<OrderData> orderFuture = scope.fork(() -> {
        logOperation("Fetching order data");
        return fetchOrderData(REQUEST_CONTEXT.get().userId());
      });

      // 等待所有任务完成
      scope.join();
      // 如果有任务失败，抛出异常
      scope.throwIfFailed();

      // 构建响应
      return new Response(
          userFuture.get(),
          orderFuture.get(),
          TRACE_ID.get()
      );
    }
  }

  // 模拟获取用户数据
  private static UserData fetchUserData(String userId) throws InterruptedException {
    Thread.sleep(100); // 模拟网络延迟
    return new UserData(userId, "John Doe", "john@example.com");
  }

  // 模拟获取订单数据
  private static OrderData fetchOrderData(String userId) throws InterruptedException {
    Thread.sleep(150); // 模拟网络延迟
    return new OrderData("order_" + userId, 99.99);
  }

  // 日志记录方法，使用ScopedValues中的上下文
  private static void logOperation(String operation) {
    System.out.printf("[Thread: %s][Trace: %s][User: %s] %s%n",
        Thread.currentThread().getName(),
        TRACE_ID.get(),
        REQUEST_CONTEXT.get().userId(),
        operation
    );
  }

  private static void logResponse(Response response) {
    System.out.printf("[Thread: %s][Trace: %s] Completed processing for user: %s%n",
        Thread.currentThread().getName(),
        TRACE_ID.get(),
        response.user().userId()
    );
  }

  private static void logError(Exception e) {
    System.out.printf("[Thread: %s][Trace: %s][User: %s] Error: %s%n",
        Thread.currentThread().getName(),
        TRACE_ID.get(),
        REQUEST_CONTEXT.get().userId(),
        e.getMessage()
    );
  }

  private static String generateTraceId() {
    return UUID.randomUUID().toString().substring(0, 8);
  }
}
