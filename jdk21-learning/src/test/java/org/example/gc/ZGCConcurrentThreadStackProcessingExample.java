package org.example.gc;

import java.util.ArrayList;
import java.util.List;

/**
 * JEP 376: ZGC: Concurrent Thread-Stack Processing
 * <p>
 * 线程栈处理示例
 */
public class ZGCConcurrentThreadStackProcessingExample {
  // ZGC并发处理线程栈
  // -XX:+UseZGC
  // -XX:ConcGCThreads=<n>  // 并发GC线程数

  public void demonstrateStackProcessing() {
    // 创建多个线程，测试并发栈处理
    for (int i = 0; i < 10; i++) {
      Thread thread = new Thread(() -> {
        // 分配对象，触发GC
        List<byte[]> list = new ArrayList<>();
        while (true) {
          list.add(new byte[1024 * 1024]); // 1MB
          if (list.size() > 100) {
            list.clear();
          }
        }
      });
      thread.start();
    }
  }
}
