package org.example.gc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 综合应用示例
 */
public class GenerationalZGCCompleteExample {
  // 配置示例
    /*
    JVM参数：
    -XX:+UseZGC 
    -XX:+ZGenerational
    -Xms4G 
    -Xmx4G 
    -XX:ZYoungSize=1G
    -XX:MaxGCPauseMillis=5
    -XX:+UseNUMA
    -XX:ConcGCThreads=4
    */

  static class MemoryPressureTest {

    private static final int YOUNG_OBJECT_SIZE = 1024;  // 1KB
    private static final int OLD_OBJECT_SIZE = 10 * 1024 * 1024;  // 10MB

    public void runTest() {
      // 测试年轻代分配
      testYoungGeneration();

      // 测试对象晋升
      testObjectPromotion();

      // 测试并发回收
      testConcurrentCollection();
    }

    private void testYoungGeneration() {
      List<byte[]> youngObjects = new ArrayList<>();

      // 快速分配和释放，测试年轻代GC
      for (int i = 0; i < 1000; i++) {
        youngObjects.add(new byte[YOUNG_OBJECT_SIZE]);

        if (i % 100 == 0) {
          youngObjects.clear();
        }
      }
    }

    private void testObjectPromotion() {
      List<byte[]> survivingObjects = new ArrayList<>();

      // 创建可能晋升的对象
      for (int i = 0; i < 10; i++) {
        survivingObjects.add(new byte[OLD_OBJECT_SIZE]);

        // 让对象存活一段时间
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    private void testConcurrentCollection() {
      // 创建多个线程产生GC压力
      int threadCount = Runtime.getRuntime().availableProcessors();
      CountDownLatch latch = new CountDownLatch(threadCount);

      for (int i = 0; i < threadCount; i++) {
        new Thread(() -> {
          try {
            List<byte[]> threadLocal = new ArrayList<>();

            // 持续分配和释放内存
            for (int j = 0; j < 100; j++) {
              threadLocal.add(new byte[YOUNG_OBJECT_SIZE]);

              if (j % 10 == 0) {
                threadLocal.subList(0, 5).clear();
                Thread.sleep(10);
              }
            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          } finally {
            latch.countDown();
          }
        }).start();
      }

      try {
        latch.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    MemoryPressureTest test = new MemoryPressureTest();
    test.runTest();
  }
}
