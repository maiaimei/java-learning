# ZGC

## JEP 333: ZGC: A Scalable Low-Latency Garbage Collector (Experimental) - JDK 11

首次引入ZGC，作为实验特性。

Z垃圾收集器，也称为ZGC，是一种可扩展的低延迟垃圾收集器。

ZGC是一个并发、单代、基于区域、支持NUMA的压缩收集器。

停止世界阶段仅限于根扫描，因此GC暂停时间不会随着堆或活动集的大小而增加。

工作原理：着色指针和读屏障技术。基于负载屏障与彩色对象指针（即彩色oops）的结合使用。这允许ZGC在Java应用程序线程运行时执行并发操作，如对象重定位。

主要特性：

- 低延迟（<10ms），暂停时间不超过10ms
- 可扩展（支持TB级堆内存），支持从几百MB到数TB的堆内存规模
- 相比G1，应用吞吐量降低不超过15%

初始实验版本：

- 仅支持Linux/x64平台

- 不支持类卸载。默认情况下，`ClassUnloading`和`ClassUnloadingWithConcurrentMark`选项将被禁用。启用它们不会有任何效果。

- 不支持JVMCI（即`Graal`）。如果启用了`EnableJVMCI`选项，将打印错误消息。

ZGC是实验性特性，默认情况下构建系统不会包含它。Oracle生产的Linux/x64 JDK构建中已默认包含ZGC。

- 构建时：使用 `--with-jvm-features=zgc` 编译配置选项，在JDK构建时显式启用ZGC特性。
- 运行时：使用 `-XX:+UnlockExperimentalVMOptions -XX:+UseZGC` 启用/使用ZGC。

## JEP 351: ZGC: Uncommit Unused Memory (Experimental) - JDK 13

添加了uncommit unused memory功能

## JEP 364: ZGC on macOS (Experimental) - JDK 14

增加了MacOS支持

## JEP 365: ZGC on Windows (Experimental) - JDK 14

增加了Windows支持

## JEP 377: ZGC: A Scalable Low-Latency Garbage Collector (Production) - JDK 15

ZGC正式转为生产特性（Production Ready）：将Z垃圾回收器从实验功能更改为产品功能。默认GC，仍为G1。

提供了JFR (Java Flight Recorder)事件支持

优化了对象分配性能

## JEP 376: ZGC: Concurrent Thread-Stack Processing - JDK 16

增加了并发栈处理能力：将ZGC 线程栈处理从**安全点（Safepoints）**移至并发阶段。

> 为了进行垃圾回收，需要所有的线程都暂停下来，这个暂停的时间我们成为 **Stop The World**。为了实现 STW 这个操作， JVM 需要为每个线程选择一个点停止运行，这个点就叫做**安全点（Safepoints）**。

## JEP 439: Generational ZGC - JDK 21

启用非分代ZGC：`-XX:+UseZGC`

启用分代ZGC：`-XX:+UseZGC -XX:+ZGenerational`

## Change Log

### JDK 24

- Removed the non-generational mode of ZGC ([JEP 490](https://openjdk.org/jeps/490))

  Remove the non-generational mode by obsoleting the `ZGenerational` option and removing the non-generational ZGC code and its tests. The option will expire in a future release, at which point it will not be recognized by the HotSpot JVM, which will refuse to start.

  After these changes, the relevant command-line options will work as follows:

  - `-XX:+UseZGC`
    - Generational ZGC is used.
  - `-XX:+UseZGC -XX:+ZGenerational`
    - Generational ZGC is used.
    - An obsolete-option warning is printed.
  - `-XX:+UseZGC -XX:-ZGenerational`
    - Generational ZGC is used.
    - An obsolete-option warning is printed.

### JDK 23

- Make Generational ZGC the default ZGC version (and deprecate non-generational ZGC) ([JEP 474](https://openjdk.org/jeps/474))
- Latency Issue Due to ICBufferFull Safepoints Resolved ([JDK-8322630](https://bugs.openjdk.org/browse/JDK-8322630)) 

### JDK 21

- Support for generations (-XX:+ZGenerational) ([JEP 439](https://openjdk.org/jeps/439))
  * To enable/use non-generational ZGC using the JVM options: `-XX:+UseZGC` at run-time
  * To enable/use Generational ZGC using the JVM options: `-XX:+UseZGC -XX:+ZGenerational` at run-time

### JDK 18

- Support for String Deduplication (-XX:+UseStringDeduplication)
- Linux/PowerPC support
- Various bug-fixes and optimizations

### JDK 17

- Dynamic Number of GC threads
- Reduced mark stack memory usage
- macOS/aarch64 support
- GarbageCollectorMXBeans for both pauses and cycles
- Fast JVM termination

### JDK 16

- Concurrent Thread Stack Scanning ([JEP 376](http://openjdk.java.net/jeps/376))
- Support for in-place relocation
- Performance improvements (allocation/initialization of forwarding tables, etc)

### JDK 15

- Production ready ([JEP 377](http://openjdk.java.net/jeps/377)), does not change the default GC, which remains G1
- Improved NUMA awareness
- Improved allocation concurrency
- Support for Class Data Sharing (CDS)
- Support for placing the heap on NVRAM
- Support for compressed class pointers
- Support for incremental uncommit
- Fixed support for transparent huge pages
- Additional JFR events

### JDK 14

- macOS support ([JEP 364](http://openjdk.java.net/jeps/364)), this is an experimental feature
- Windows support ([JEP 365](http://openjdk.java.net/jeps/365)), this is an experimental feature
- Support for tiny/small heaps (down to 8M)
- Support for JFR leak profiler
- Support for limited and discontiguous address space
- Parallel pre-touch (when using -XX:+AlwaysPreTouch)
- Performance improvements (clone intrinsic, etc)
- Stability improvements

### JDK 13

- Increased max heap size from 4TB to 16TB
- Support for uncommitting unused memory ([JEP 351](http://openjdk.java.net/jeps/351))
- Support for -XX:SoftMaxHeapSIze
- Support for the Linux/AArch64 platform
- Reduced Time-To-Safepoint

### JDK 12

- Support for concurrent class unloading
- Further pause time reductions

### JDK 11

- Initial version of ZGC ([JEP 333](http://openjdk.java.net/jeps/333))
  * Initially supported platform: Linux/x64
  * Does not support class unloading (using -XX:+ClassUnloading has no effect)
  * Build a JDK using the configure option: `--with-jvm-features=zgc`
  * To enable/use ZGC using the JVM options: `-XX:+UnlockExperimentalVMOptions -XX:+UseZGC` at run-time

## Reference

[https://wiki.openjdk.org/display/zgc/Main](https://wiki.openjdk.org/display/zgc/Main)

[https://docs.oracle.com/en/java/javase/21/gctuning/z-garbage-collector.html](https://docs.oracle.com/en/java/javase/21/gctuning/z-garbage-collector.html)

[https://www.baeldung.com/jvm-zgc-garbage-collector](https://www.baeldung.com/jvm-zgc-garbage-collector)