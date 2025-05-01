package org.example.api;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EnhancedModernConcurrencyExample {

  private static final ScopedValue<RequestContext> REQUEST_CONTEXT = ScopedValue.newInstance();
  private static final ScopedValue<String> TRACE_ID = ScopedValue.newInstance();
  private static final Duration TIMEOUT = Duration.ofSeconds(5);

  record RequestContext(String userId, Instant timestamp) {

  }

  record UserData(String userId, String name, String email) {

  }

  record OrderData(String orderId, Double amount) {

  }

  record Response(UserData user, OrderData order, String traceId) {

  }

  public static void main(String[] args) {
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    try {
      // 提交多个请求
      List<Future<?>> futures = IntStream.range(0, 5)
          .mapToObj(i -> executor.submit(() -> {
            try {
              processRequest("user" + i);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }))
          .collect(Collectors.toList());

      // 等待所有请求完成
      for (Future<?> future : futures) {
        future.get();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      executor.shutdown();
    }
  }

  public static void processRequest(String userId) throws Exception {
    var context = new RequestContext(userId, Instant.now());
    var traceId = generateTraceId();

    ScopedValue.where(REQUEST_CONTEXT, context)
        .where(TRACE_ID, traceId)
        .run(() -> {
          try {
            Response response = fetchDataConcurrently();
            logResponse(response);
          } catch (TimeoutException e) {
            logError(new Exception("Request timed out", e));
            throw new RuntimeException(e);
          } catch (Exception e) {
            logError(e);
            throw new RuntimeException(e);
          }
        });
  }

  private static Response fetchDataConcurrently() throws Exception {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      // Fork tasks
      Subtask<UserData> userFuture = scope.fork(() -> {
        logOperation("Fetching user data");
        return fetchUserDataWithRetry(REQUEST_CONTEXT.get().userId());
      });

      Subtask<OrderData> orderFuture = scope.fork(() -> {
        logOperation("Fetching order data");
        return fetchOrderDataWithRetry(REQUEST_CONTEXT.get().userId());
      });

      // Wait with timeout
      scope.joinUntil(Instant.now().plus(TIMEOUT));
      scope.throwIfFailed();

      return new Response(
          userFuture.get(),
          orderFuture.get(),
          TRACE_ID.get()
      );
    }
  }

  // 带重试机制的数据获取
  private static UserData fetchUserDataWithRetry(String userId) throws Exception {
    int maxRetries = 3;
    int attempt = 0;
    Exception lastException = null;

    while (attempt < maxRetries) {
      try {
        if (attempt > 0) {
          logOperation("Retrying user data fetch, attempt " + (attempt + 1));
        }
        return fetchUserData(userId);
      } catch (Exception e) {
        lastException = e;
        attempt++;
        if (attempt < maxRetries) {
          Thread.sleep(100 * attempt); // 指数退避
        }
      }
    }
    throw new Exception("Failed to fetch user data after " + maxRetries + " attempts", lastException);
  }

  private static OrderData fetchOrderDataWithRetry(String userId) throws Exception {
    int maxRetries = 3;
    int attempt = 0;
    Exception lastException = null;

    while (attempt < maxRetries) {
      try {
        if (attempt > 0) {
          logOperation("Retrying order data fetch, attempt " + (attempt + 1));
        }
        return fetchOrderData(userId);
      } catch (Exception e) {
        lastException = e;
        attempt++;
        if (attempt < maxRetries) {
          Thread.sleep(100 * attempt);
        }
      }
    }
    throw new Exception("Failed to fetch order data after " + maxRetries + " attempts", lastException);
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
