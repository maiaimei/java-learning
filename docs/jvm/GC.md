# Garbage Collection

‌GC（Garbage Collection）‌，即垃圾回收，是Java虚拟机（JVM）的一种自动内存管理机制。其主要作用是回收程序中不再使用的对象所占用的内存空间，防止内存泄漏，保证程序的稳定运行。

## Garbage Collection Algorithms（垃圾回收算法）

Garbage Collection Algorithms（垃圾回收算法）是具体的垃圾回收策略和方法论。

主要的算法包括：

- 标记-清除（Mark-Sweep）
  - 标记阶段：标识出所有存活的对象
  - 清除阶段：回收未被标记对象的空间
  - 缺点：会产生内存碎片
- 标记-整理（Mark-Compact）
  - 标记阶段：与Mark-Sweep相同
  - 整理阶段：将存活对象移到内存的一端，清理边界外的内存
  - 优点：解决了内存碎片问题
- 标记-复制（Mark-Copy）
  - 将内存分为两块
  - 只使用其中一块，当这块内存用完时，将存活对象复制到另一块
  - 适用于新生代垃圾回收
- 分代收集（Generational Collection）
  - 基于对象生命周期的长短进行分代管理
  - 新生代和老年代使用不同的收集算法
  - 提高回收效率

需要理解每种算法的优缺点和适用场景。

## Garbage Collectors（垃圾收集器）

Garbage Collectors（垃圾收集器）是算法的具体实现，是真正执行垃圾回收的组件，可能组合使用多种算法。

- Serial GC
  * 单线程收集器
  * 使用mark-copy（新生代）和mark-compact（老年代）
  * 适用于单核小内存场景

- Parallel GC
  * 多线程并行收集
  * 新生代使用mark-copy
  * 老年代使用mark-sweep-compact
  * 注重吞吐量

- CMS (Concurrent Mark Sweep)
  * 并发收集器
  * 以最短停顿时间为目标
  * 使用mark-sweep算法
  * 适用于对响应时间要求高的应用

- G1 (Garbage First)
  * 将堆内存分割成多个区域
  * 可预测的停顿时间模型
  * 整体上是标记-整理算法，局部是复制算法
  * 适用于大内存多核场景

- ZGC
  * 可扩展的低延迟垃圾收集器
  * 停顿时间不超过10ms
  * 适用于大内存低延迟场景

理解它们的工作原理和适用场景。

## GC学习建议

学习垃圾回收（GC）可以从以下几个方面入手：

1. **GC的基本概念**：
   - 了解GC的作用：自动管理内存，回收不再使用的对象。
   - 学习GC的基本原理：标记-清除、复制、标记-整理等算法。
2. **GC算法**：
   - 学习常见的GC算法：
     - 标记-清除（Mark-Sweep）
     - 标记-整理（Mark-Compact）
     - 复制算法（Copying）
     - 分代收集（Generational GC）
   - 理解每种算法的优缺点和适用场景。
3. **分代垃圾回收**：
   - 学习堆内存的分代模型（年轻代、老年代、永久代/元空间）。
   - 了解不同代的GC策略（Minor GC、Major GC、Full GC）。
4. **常见GC实现**：
   - 学习主流JVM中的GC实现：
     - Serial GC
     - Parallel GC
     - CMS GC
     - G1 GC
     - ZGC
     - Shenandoah GC
   - 理解它们的工作原理和适用场景。
5. **GC调优**：
   - 学习如何通过JVM参数调整GC行为（如`-XX:+UseG1GC`）。
   - 使用工具分析GC日志（如`jstat`、`GCViewer`、`VisualVM`）。
   - 优化GC性能，减少停顿时间（GC Pause）。
6. **GC日志分析**：
   - 学习如何启用GC日志（如`-Xlog:gc`）。
   - 分析GC日志中的信息（如GC频率、停顿时间、回收的内存量）。
7. **实践与实验**：
   - 编写代码模拟内存分配和回收。
   - 使用不同GC策略运行程序，观察性能差异。
8. **深入研究**：
   - 阅读JEP文档（如JEP 333、JEP 312）了解最新GC特性。
   - 学习GC的源码实现（如OpenJDK中的GC模块）。