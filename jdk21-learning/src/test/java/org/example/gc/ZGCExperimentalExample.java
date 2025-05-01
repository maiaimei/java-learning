package org.example.gc;

import java.util.ArrayList;
import java.util.List;

/**
 * JEP 333: ZGC: A Scalable Low-Latency Garbage Collector (Experimental)
 * <p>
 * ZGC实验特性示例
 */
public class ZGCExperimentalExample {
  // 实验性配置
  // -XX:+UnlockExperimentalVMOptions
  // -XX:+UseZGC

  public void demonstrateExperimentalFeatures() {
    // 大对象分配
    List<byte[]> largeObjects = new ArrayList<>();

    // 测试内存分配和回收
    for (int i = 0; i < 100; i++) {
      largeObjects.add(new byte[10 * 1024 * 1024]); // 10MB

      if (i % 10 == 0) {
        largeObjects.subList(0, 5).clear();
        System.gc(); // 建议进行GC
      }
    }
  }
}
