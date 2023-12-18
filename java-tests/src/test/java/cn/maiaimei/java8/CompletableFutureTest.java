package cn.maiaimei.java8;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * CompletableFuture: JDK8引入的异步任务编排的类。
 */
@Slf4j
public class CompletableFutureTest {

  /**
   * 自定义线程池
   */
  private final ExecutorService executorService = new ThreadPoolExecutor(
      2,
      2,
      1,
      TimeUnit.MILLISECONDS,
      new LinkedBlockingQueue<>(10),
      new NamedThreadFactory("test"),
      new DiscardPolicy());

  /**
   * runAsync: 开启异步任务， 无返回值
   */
  @Test
  public void testRunAsync() {
    CompletableFuture.runAsync(() -> info("runAsync"));
    CompletableFuture.runAsync(() -> info("runAsync with executor"), executorService);

    CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> info("runAsync"));
    CompletableFuture<Void> future2 = CompletableFuture.runAsync(
        () -> info("runAsync with executor"),
        executorService);
    log.info("future1, isDone: {}, result: {}", future1.isDone(), future1.join());
    log.info("future2, isDone: {}, result: {}", future2.isDone(), future2.join());
  }

  /**
   * supplyAsync: 开启异步任务， 有返回值
   */
  @Test
  public void testSupplyAsync() {
    CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
      String message = "supplyAsync";
      info(message);
      return message;
    });
    CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
      String message = "supplyAsync with executor";
      info(message);
      return message;
    }, executorService);
    log.info("future1, isDone: {}, result: {}", future1.isDone(), future1.join());
    log.info("future2, isDone: {}, result: {}", future2.isDone(), future2.join());
  }

  /**
   * completedFuture：应用场景：单元测试
   */
  @Test
  public void testCompletedFuture() {
    final CompletableFuture<String> future = CompletableFuture.completedFuture(
        "completedFuture");
    log.info("future, isDone: {}, result: {}", future.isDone(), future.join());
  }

  /**
   * thenApply：异步任务回调，有参数有返回值
   */
  @Test
  public void testThenApply() {
    final Integer result = CompletableFuture.supplyAsync(() -> 1)
        .thenApply(res -> res + 2)
        .thenApplyAsync(res -> res + 3)
        .thenApplyAsync(res -> res + 4, executorService).join();
    log.info("1+2+3+4={}", result);
  }

  /**
   * thenRun：异步任务回调，无参数无返回值
   */
  @Test
  public void testThenRun() throws InterruptedException {
    CompletableFuture.runAsync(() -> System.out.println("test"))
        .thenRun(() -> System.out.println("thenRun"))
        .thenRunAsync(() -> System.out.println("thenRunAsync"))
        .thenRunAsync(() -> System.out.println("thenRunAsync with executor"), executorService);

    final CompletableFuture<Void> future = CompletableFuture.runAsync(() -> info("test"))
        .thenRun(() -> info("thenRun"))
        .thenRunAsync(() -> info("thenRunAsync"))
        .thenRunAsync(() -> info("thenRunAsync with executor"), executorService);

    future.join();
  }

  /**
   * thenAccept：异步任务回调，有参数无返回值
   */
  @Test
  public void testThenAccept() {
    CompletableFuture.supplyAsync(() -> "test")
        .thenAccept(result -> info("{} thenAccept", result));

    CompletableFuture.supplyAsync(() -> "test")
        .thenAcceptAsync(result -> info("{} thenAcceptAsync", result));

    CompletableFuture.supplyAsync(() -> "test")
        .thenAcceptAsync(result -> info("{} thenAcceptAsync with executor", result),
            executorService);
  }

  @Test
  public void testThenAcceptBoth() {

  }

  /**
   * thenCompose：编排两个有依赖关系的异步任务
   */
  @Test
  public void testThenCompose() {
    // 使用 thenApply 编排两个有依赖关系的异步任务
    final CompletableFuture<CompletableFuture<String>> f1 = CompletableFuture.supplyAsync(
            () -> "顾客点餐")
        .thenApply(res -> CompletableFuture.completedFuture("收到" + res + "，后厨开始炒菜"));
    // f1 是嵌套的 CompletableFuture ，需要调用两次 join 方法，可以理解为 stream 的 map
    log.info("{}", f1.join().join());

    // 使用 thenCompose 编排两个有依赖关系的异步任务
    final CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "顾客点餐")
        .thenCompose(res -> CompletableFuture.completedFuture("收到" + res + "，后厨开始炒菜"));
    // f2 无嵌套 CompletableFuture ，可以直接调用 join 方法，可以理解为 stream  的 flatMap
    log.info("{}", f2.join());
  }

  /**
   * thenCombine：编排两个非依赖关系的异步任务
   */
  @Test
  public void testThenCombine() {
    // 程序员A开发任务1
    final CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
      info("A do task1");
      return "raise pr for task1";
    });
    // 程序员B开发任务2
    final CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
      info("B do task2");
      return "raise pr for task2";
    });
    // 程序员A和程序员B开发完成
    final CompletableFuture<String> combineFuture = future1.thenCombine(future2, (pr1, pr2) -> {
      log.info("{}", pr1);
      log.info("{}", pr2);
      log.info("prs merged");
      return "task completed";
    });
    log.info("{}", combineFuture.join());
  }

  private void info(String format, Object... arguments) {
    log.info(format, arguments);
    final Random random = new Random();
    final long timeout = random.nextLong(1000, 2000);
    try {
      TimeUnit.MILLISECONDS.sleep(timeout);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
