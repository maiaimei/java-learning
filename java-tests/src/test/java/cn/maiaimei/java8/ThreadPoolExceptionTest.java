package cn.maiaimei.java8;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * https://cloud.tencent.com/developer/article/2354361
 */
@Slf4j
public class ThreadPoolExceptionTest {

  @Test
  public void test1_submit() throws InterruptedException {
    // 创建一个线程池
    ExecutorService executorService = Executors.newFixedThreadPool(3);

    // 当线程池抛出异常后 submit无提示，其他线程继续执行
    for (int i = 0; i < 3; i++) {
      executorService.submit(new Task());
    }

    // 关闭线程池
    executorService.shutdown();
    if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
      executorService.shutdownNow();
    }
  }

  @Test
  public void test1_execute() throws InterruptedException {
    // 创建一个线程池
    ExecutorService executorService = Executors.newFixedThreadPool(3);

    // 当线程池抛出异常后 execute抛出异常，其他线程继续执行新任务
    for (int i = 0; i < 3; i++) {
      executorService.execute(new Task());
    }

    // 关闭线程池
    executorService.shutdown();
    if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
      executorService.shutdownNow();
    }
  }

  @Test
  public void test2_submit() throws InterruptedException {
    // 创建一个自己定义的线程池
    ExecutorService executorService = customExecutorService();

    // 当线程池抛出异常后 submit无提示，其他线程继续执行
    for (int i = 0; i < 3; i++) {
      executorService.submit(new Task());
    }

    // 关闭线程池
    executorService.shutdown();
    if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
      executorService.shutdownNow();
    }
  }

  @Test
  public void test2_execute() throws InterruptedException {
    // 创建一个自己定义的线程池
    ExecutorService executorService = customExecutorService();

    // 当线程池抛出异常后 execute抛出异常，其他线程继续执行新任务
    for (int i = 0; i < 3; i++) {
      executorService.execute(new Task());
    }

    // 关闭线程池
    executorService.shutdown();
    if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
      executorService.shutdownNow();
    }
  }

  private ExecutorService customExecutorService() {
    return new ThreadPoolExecutor(
        3,
        3,
        0,
        TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(10)) {
      //重写afterExecute方法
      @Override
      protected void afterExecute(Runnable r, Throwable t) {
        // 这个是excute提交的时候
        if (t != null) {
          log.error("afterExecute里面获取到excute提交的异常信息，处理异常", t);
        }
        // 如果r的实际类型是FutureTask 那么是submit提交的，所以可以在里面get到异常
        if (r instanceof FutureTask) {
          try {
            Future<?> future = (Future<?>) r;
            // get获取异常
            future.get();
          } catch (Exception e) {
            log.error("afterExecute里面获取到submit提交的异常信息，处理异常", e);
          }
        }
      }
    };
  }

  // 任务类
  class Task implements Runnable {

    ThreadLocalRandom random = ThreadLocalRandom.current();

    @Override
    public void run() {
      log.info("Task执行开始");
      final int i = random.nextInt();
      if (i % 2 == 0) {
        try {
          log.info("i % 2 == 0 睡眠开始");
          TimeUnit.SECONDS.sleep(5);
          log.info("i % 2 == 0 睡眠结束");
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      final int j = 1 / (i % 2);
      log.info("i={}, j={}", i, j);
      log.info("Task执行结束");
    }
  }
}
