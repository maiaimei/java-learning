package org.example.gc;

import java.util.HashMap;
import java.util.Map;

/**
 * JEP 377: ZGC: A Scalable Low-Latency Garbage Collector
 * <p>
 * ZGC基础特性示例
 */
public class ZGCBasicExample {
  // 配置ZGC
  // -XX:+UseZGC
  // -XX:MaxGCPauseMillis=<ms>
  // -XX:GCTimeRatio=<ratio>

  public void demonstrateZGCFeatures() {
    // 内存分配测试
    Map<String, byte[]> memoryMap = new HashMap<>();

    // 模拟内存压力
    for (int i = 0; i < 1000; i++) {
      memoryMap.put("key-" + i, new byte[1024 * 1024]); // 1MB

      if (i % 100 == 0) {
        // 清理一部分内存，测试GC
        memoryMap.clear();
      }
    }
  }
}
