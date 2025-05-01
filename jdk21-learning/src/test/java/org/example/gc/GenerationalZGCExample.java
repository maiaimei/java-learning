package org.example.gc;

/**
 * JEP 439: Generational ZGC
 * <p>
 * 主要特性示例
 */
public class GenerationalZGCExample {

  // 配置参数
  static {
    // 启用Generational ZGC
    // -XX:+UseZGC
    // -XX:+ZGenerational

    // 调优参数
    // -XX:ZYoungSize=<size>    // 年轻代初始大小
    // -XX:ZYoungGenerationSize=<size>  // 年轻代最大大小
  }

  public void demonstrateAllocation() {
    // 年轻代分配
    byte[] youngObject = new byte[1024];  // 小对象，通常在年轻代分配

    // 可能晋升到老年代的对象
    byte[] potentialOldObject = new byte[20 * 1024 * 1024]; // 大对象

    // 保持对象存活，可能导致晋升
    keepReference(potentialOldObject);
  }

  private void keepReference(Object obj) {
    // 模拟对象存活
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
