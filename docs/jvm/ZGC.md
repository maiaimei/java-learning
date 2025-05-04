# ZGC

## JEP 333: ZGC: A Scalable Low-Latency Garbage Collector (Experimental) - JDK 11

Z垃圾收集器，也称为ZGC，是一种可扩展的低延迟垃圾收集器。

ZGC是一个并发、单代、基于区域、支持NUMA的压缩收集器。

停止世界阶段仅限于根扫描，因此GC暂停时间不会随着堆或活动集的大小而增加。

工作原理：基于负载屏障与彩色对象指针（即彩色oops）的结合使用。这允许ZGC在Java应用程序线程运行时执行并发操作，如对象重定位。

主要特点：

- GC暂停时间不超过10ms

- 支持从几百MB到数TB的堆内存规模

- 相比G1，应用吞吐量降低不超过15%

初始实验版本：

- 仅支持Linux/x64平台

- 不支持类卸载。默认情况下，`ClassUnloading`和`ClassUnloadingWithConcurrentMark`选项将被禁用。启用它们不会有任何效果。

- 不支持JVMCI（即`Graal`）。如果启用了`EnableJVMCI`选项，将打印错误消息。

ZGC是实验性特性，默认情况下构建系统不会包含它。Oracle生产的Linux/x64 JDK构建中已默认包含ZGC。

- 构建时：使用 `--with-jvm-features=zgc` 编译配置选项，在JDK构建时显式启用ZGC特性。
- 运行时：使用 `-XX:+UnlockExperimentalVMOptions -XX:+UseZGC` 启用/使用ZGC。

## JEP 377: ZGC: A Scalable Low-Latency Garbage Collector (Production) - JDK 15

将Z垃圾回收器从实验功能更改为产品功能。默认GC，仍为G1。

## JEP 376: ZGC: Concurrent Thread-Stack Processing - JDK 16

将ZGC 线程栈处理从**安全点（Safepoints）**移至并发阶段。

为了进行垃圾回收，需要所有的线程都暂停下来，这个暂停的时间我们成为 **Stop The World**。

为了实现 STW 这个操作， JVM 需要为每个线程选择一个点停止运行，这个点就叫做**安全点（Safepoints）**。

## JEP 439: Generational ZGC - JDK 21

启用非分代ZGC：`-XX:+UseZGC`

启用分代ZGC：`-XX:+UseZGC -XX:+ZGenerational`

## Reference

[https://docs.oracle.com/en/java/javase/21/gctuning/z-garbage-collector.html](https://docs.oracle.com/en/java/javase/21/gctuning/z-garbage-collector.html)

[https://www.baeldung.com/jvm-zgc-garbage-collector](https://www.baeldung.com/jvm-zgc-garbage-collector)