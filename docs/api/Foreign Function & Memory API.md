# Foreign Function & Memory API

## JEP 370: Foreign-Memory Access API (Incubator) - JDK 14

Core Purpose: 

- Allows Java programs to safely and efficiently access foreign memory outside of the Java heap
- Provides a supported alternative to existing methods like ByteBuffer and sun.misc.Unsafe

Main Abstractions:

- MemorySegment: Represents a contiguous memory region with spatial and temporal bounds
- MemoryAddress: Represents an offset within a segment
- MemoryLayout: Provides programmatic description of memory segment contents

Key Features:

- Spatial bounds checking for safety
- Explicit memory deallocation (temporal bounds)
- Support for various memory sources (native memory, Java arrays, byte buffers)
- Memory access through var handles
- Rich addressing schemes including multi-dimensional access

Safety Guarantees:

- Prevents access outside memory bounds
- Requires explicit memory segment closure
- Throws exceptions for illegal access attempts
- Eliminates risks of JVM crashes

Example Usage:

```java
try (MemorySegment segment = MemorySegment.allocateNative(100)) {
    MemoryAddress base = segment.baseAddress();
    VarHandle intHandle = MemoryHandles.varHandle(int.class, ByteOrder.nativeOrder());
    // Work with memory safely...
}
```

Advantages over Existing Solutions:

- More reliable than sun.misc.Unsafe
- Better performance than JNI
- Overcomes ByteBuffer limitations (like 2GB size limit)
- Provides explicit memory management
- Designed with JIT optimizations in mind

Implementation Status:

- Provided as an incubating module (jdk.incubator.foreign)
- Focuses on both safety and performance
- Requires JIT optimization work for efficient runtime performance

This API represents a significant improvement in Java's foreign memory access capabilities, providing a safe, efficient, and supported way to work with off-heap memory.

核心目的：

- 允许 Java 程序安全高效地访问 Java 堆外的外部内存
- 为现有的 ByteBuffer 和 sun.misc.Unsafe 提供一个受支持的替代方案

主要抽象概念：

- MemorySegment（内存段）：表示具有空间和时间边界的连续内存区域
- MemoryAddress（内存地址）：表示段内的偏移量
- MemoryLayout（内存布局）：提供内存段内容的程序化描述

核心特性：

- 空间边界检查以确保安全性
- 显式内存释放（时间边界）
- 支持多种内存源（原生内存、Java 数组、字节缓冲区）
- 通过 var handles 进行内存访问
- 支持多维寻址等丰富的寻址方案

安全保证：

- 防止访问内存边界之外的区域
- 要求显式关闭内存段
- 对非法访问抛出异常
- 消除 JVM 崩溃的风险

使用示例：

```java
try (MemorySegment segment = MemorySegment.allocateNative(100)) {
    MemoryAddress base = segment.baseAddress();
    VarHandle intHandle = MemoryHandles.varHandle(int.class, ByteOrder.nativeOrder());
    // 安全地操作内存...
}
```

相比现有解决方案的优势：

- 比 sun.misc.Unsafe 更可靠
- 比 JNI 性能更好
- 克服了 ByteBuffer 的限制（如 2GB 大小限制）
- 提供显式内存管理
- 设计时考虑了 JIT 优化

实现状态：

- 作为孵化模块提供（jdk.incubator.foreign）
- 同时注重安全性和性能
- 需要 JIT 优化工作以确保运行时性能

这个 API 代表了 Java 外部内存访问能力的重大改进，提供了一种安全、高效且受支持的方式来处理堆外内存。它的设计既保证了内存访问的安全性，又不影响性能，是一个非常重要的改进。

## JEP 383: Foreign-Memory Access API (Second Incubator) - JDK 15

Purpose of Second Incubation:

- Refines and enhances the initial Foreign-Memory Access API from JEP 370
- Incorporates feedback from the first incubation period
- Continues to provide safe and efficient access to foreign memory

Key Enhancements in JEP 383:

- Added rich VarHandle combinator API for customizing memory access var handles
- Introduced support for parallel processing of memory segments via Spliterator interface
- Enhanced support for mapped memory segments (e.g., MappedMemorySegment::force)
- Added safe API points for serial confinement (thread ownership transfer)
- Included unsafe API points for address manipulation from native calls

Thread Confinement Features:

- Memory segments are confined to the thread that created them
- Supports explicit handoff operations between threads
- Example of thread ownership transfer:

```java
MemorySegment segmentA = MemorySegment.allocateNative(10); // confined by thread A
var segmentB = segmentA.withOwnerThread(threadB); // confined by thread B
```

Parallel Processing Support:

- Added ability to process segment contents in parallel using Spliterator
- Example of parallel processing:

```java
SequenceLayout seq = MemoryLayout.ofSequence(1_000_000, MemoryLayouts.JAVA_INT);
SequenceLayout seq_bulk = seq.reshape(-1, 100);
VarHandle intHandle = seq.varHandle(int.class, PathElement.sequenceElement());    

int sum = StreamSupport.stream(MemorySegment.spliterator(segment, seq_bulk), true)
    .mapToInt(slice -> {
        // Process segment slice
    }).sum();
```

Safety Features:

- Maintains spatial and temporal bounds checking from JEP 370
- Adds thread confinement guarantees
- Ensures safe concurrent access through structured parallelism

Memory Access Control:

- Introduces restricted operations control through the foreign.restricted property
- Provides different modes: deny (default), permit, warn, and debug
- Plans for future integration with the module system

Core Benefits:

- Improved performance through optimized parallel processing
- Enhanced safety through thread confinement
- More flexible memory access patterns
- Better support for complex memory operations

This second incubation phase represents a significant maturation of the API, with improved features for concurrent processing and thread safety while maintaining the core benefits of safe foreign memory access.

第二次孵化的目的：

- 完善和增强 JEP 370 中的初始外部内存访问 API
- 根据第一次孵化期间收到的反馈进行改进
- 继续提供安全和高效的外部内存访问机制

JEP 383 的主要改进：

- 增加了丰富的 VarHandle 组合器 API，用于自定义内存访问 var handles
- 通过 Spliterator 接口引入了内存段的并行处理支持
- 增强了对映射内存段的支持（例如 MappedMemorySegment::force）
- 添加了用于串行限制的安全 API（线程所有权转移）
- 包含了用于处理原生调用地址操作的不安全 API

线程限制特性：

- 内存段被限制在创建它的线程中
- 支持线程间的显式交接操作
- 线程所有权转移示例：

```java
MemorySegment segmentA = MemorySegment.allocateNative(10); // 由线程A限制
var segmentB = segmentA.withOwnerThread(threadB); // 转移到线程B
```

并行处理支持：

- 新增使用 Spliterator 并行处理段内容的能力
- 并行处理示例：

```java
SequenceLayout seq = MemoryLayout.ofSequence(1_000_000, MemoryLayouts.JAVA_INT);
SequenceLayout seq_bulk = seq.reshape(-1, 100);
VarHandle intHandle = seq.varHandle(int.class, PathElement.sequenceElement());    

int sum = StreamSupport.stream(MemorySegment.spliterator(segment, seq_bulk), true)
    .mapToInt(slice -> {
        // 处理段切片
    }).sum();
```

安全特性：

- 保持了 JEP 370 中的空间和时间边界检查
- 增加了线程限制保证
- 通过结构化并行确保安全的并发访问

内存访问控制：

- 通过 foreign.restricted 属性引入受限操作控制
- 提供不同模式：
  - deny（默认）：拒绝受限操作
  - permit：允许受限操作
  - warn：允许但显示警告
  - debug：允许并显示调用栈
- 计划未来与模块系统集成

核心优势：

- 通过优化的并行处理提高性能
- 通过线程限制增强安全性
- 更灵活的内存访问模式
- 更好地支持复杂内存操作

这第二次孵化阶段代表了该 API 的显著成熟，在保持安全外部内存访问核心优势的同时，增强了并发处理和线程安全性的特性。这些改进使得 API 更加完善，能够更好地满足开发者在处理外部内存时的各种需求。

## JEP 393: Foreign-Memory Access API (Third Incubator) - JDK 16

Core Objectives:

- Further refinement of the Foreign-Memory Access API based on feedback
- Maintains the goal of safe and efficient access to foreign memory
- Re-incubates the API in Java 16

Major Changes and Improvements:

- Clearer separation of roles between MemorySegment and MemoryAddress interfaces
- Introduction of new MemoryAccess interface for common static memory accessors
- Added support for shared segments
- New capability to register segments with a Cleaner

Memory Access Simplification:

```java
// Simplified memory access using MemoryAccess static methods
try (MemorySegment segment = MemorySegment.allocateNative(100)) {
    for (int i = 0; i < 25; i++) {
        MemoryAccess.setIntAtOffset(segment, i * 4, i);
    }
}
```

Memory Layout Features:

- Programmatic description of memory contents
- Support for complex memory layouts
- Example of layout usage:

```java
SequenceLayout intArrayLayout = MemoryLayout.ofSequence(25,
    MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()));

try (MemorySegment segment = MemorySegment.allocateNative(intArrayLayout)) {
    VarHandle indexedElementHandle = intArrayLayout.varHandle(int.class,
        PathElement.sequenceElement());
    // Use the layout-based access
}
```

Segment Sharing and Thread Safety:

- Support for both confined and shared segments
- Ability to transfer segment ownership between threads
- Concurrent access through shared segments

```java
MemorySegment segmentA = MemorySegment.allocateNative(10); // confined
var sharedSegment = segmentA.withOwnerThread(null); // shared
```

Memory Resource Management:

- Deterministic deallocation through try-with-resources
- Optional registration with Cleaner for automatic resource cleanup

```java
MemorySegment segment = MemorySegment.allocateNative(100);
Cleaner cleaner = Cleaner.create();
segment.registerCleaner(cleaner);
```

Safety Features:

- Maintains spatial and temporal bounds checking
- Thread confinement for safe concurrent access
- Exception handling for illegal memory access
- Protection against JVM crashes

Performance Considerations:

- Design optimized for JIT compilation
- Support for efficient bulk operations
- Balanced approach between safety and performance

This third incubation phase represents further maturation of the API, with improved usability through simplified access patterns while maintaining the core safety and performance characteristics of the previous versions.

The API continues to provide a robust alternative to unsafe memory access methods while offering better performance and safety guarantees compared to existing solutions like ByteBuffer and sun.misc.Unsafe.

核心目标：

- 基于反馈进一步完善外部内存访问 API
- 保持安全和高效访问外部内存的目标
- 在 Java 16 中重新孵化该 API

主要变更和改进：

- 明确区分 MemorySegment 和 MemoryAddress 接口的角色
- 引入新的 MemoryAccess 接口用于常用静态内存访问器
- 增加对共享段的支持
- 新增使用 Cleaner 注册段的功能

内存访问简化：

```java
// 使用 MemoryAccess 静态方法简化内存访问
try (MemorySegment segment = MemorySegment.allocateNative(100)) {
    for (int i = 0; i < 25; i++) {
        MemoryAccess.setIntAtOffset(segment, i * 4, i);
    }
}
```

内存布局特性：

- 提供内存内容的程序化描述
- 支持复杂内存布局
- 布局使用示例：

```java
SequenceLayout intArrayLayout = MemoryLayout.ofSequence(25,
    MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()));

try (MemorySegment segment = MemorySegment.allocateNative(intArrayLayout)) {
    VarHandle indexedElementHandle = intArrayLayout.varHandle(int.class,
        PathElement.sequenceElement());
    // 使用基于布局的访问
}
```

段共享和线程安全：

- 支持受限段和共享段
- 能够在线程间转移段的所有权
- 通过共享段实现并发访问

```java
MemorySegment segmentA = MemorySegment.allocateNative(10); // 受限段
var sharedSegment = segmentA.withOwnerThread(null); // 共享段
```

内存资源管理：

- 通过 try-with-resources 实现确定性释放
- 可选的 Cleaner 注册以实现自动资源清理

```java
MemorySegment segment = MemorySegment.allocateNative(100);
Cleaner cleaner = Cleaner.create();
segment.registerCleaner(cleaner);
```

安全特性：

- 维持空间和时间边界检查
- 线程限制确保安全的并发访问
- 对非法内存访问进行异常处理
- 防止 JVM 崩溃

性能考虑：

- 针对 JIT 编译优化的设计
- 支持高效的批量操作
- 在安全性和性能之间取得平衡

这第三次孵化阶段代表了 API 的进一步成熟，通过简化的访问模式提高了可用性，同时保持了前几个版本的核心安全性和性能特性。

该 API 继续为不安全的内存访问方法提供了一个强大的替代方案，与现有的 ByteBuffer 和 sun.misc.Unsafe 相比，提供了更好的性能和安全保证。这些改进使得 API 更加完善和易用，同时保持了高性能和安全性的平衡。

## JEP 389: Foreign Linker API (Incubator) - JDK 16

Core Purpose:

- Introduces an API for binding to native code
- Works together with Foreign-Memory API [[1\]](https://www.infoq.com/news/2022/09/java19-released/?itm_campaign=concurrency&itm_medium=link&itm_source=presentations_about_concurrency)
- Simplifies native library integration without JNI
- Provides pure Java development model for native interop

Main Features:

- Symbol lookups in native libraries
- Support for downcalls (Java to native)
- Support for upcalls (native to Java)
- Platform-specific type mapping

Key Components:

```java
// Library Lookup
LibraryLookup libclang = LibraryLookup.ofLibrary("clang");
LibraryLookup.Symbol symbol = libclang.lookup("function_name");

// CLinker Interface
interface CLinker {
    MethodHandle downcallHandle(LibraryLookup.Symbol func,
                              MethodType type,
                              FunctionDescriptor function);
    MemorySegment upcallStub(MethodHandle target,
                            FunctionDescriptor function);
}
```

Downcall Example (Java calling native code):

```java
// Example calling strlen
MethodHandle strlen = CLinker.getInstance().downcallHandle(
    LibraryLookup.ofDefault().lookup("strlen"),
    MethodType.methodType(long.class, MemoryAddress.class),
    FunctionDescriptor.of(C_LONG, C_POINTER)
);

try (MemorySegment str = CLinker.toCString("Hello")) {
    long len = strlen.invokeExact(str.address()); // 5
}
```

Upcall Example (Native calling Java):

```java
// Creating function pointer from Java method
MemorySegment comparFunc = CLinker.getInstance().upcallStub(
    comparHandle,
    FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER)
);
```

Safety Features:

- Restricted operation requiring explicit permission
- Type safety through FunctionDescriptor
- Memory safety through MemorySegment
- Controlled native library loading

Key Benefits:

- No JNI code required
- Better performance than JNI
- Pure Java development model
- Type-safe native interop
- Simplified native library integration

Implementation Details:

- Provided as incubator module (jdk.incubator.foreign)
- Requires -Dforeign.restricted=permit flag
- Platform-specific optimizations
- JIT compiler support

Limitations and Scope:

- Initial focus on C language support
- x64 and AArch64 platforms support
- No automatic header file processing
- No changes to deployment model

Example Complex Usage (qsort):

```java
// Native qsort with Java comparator
MethodHandle qsort = CLinker.getInstance().downcallHandle(
    LibraryLookup.ofDefault().lookup("qsort"),
    MethodType.methodType(void.class, MemoryAddress.class, long.class,
                         long.class, MemoryAddress.class),
    FunctionDescriptor.ofVoid(C_POINTER, C_LONG, C_LONG, C_POINTER)
);

// Using the native function with Java callback
try (MemorySegment array = MemorySegment.allocateNative(4 * 10)) {
    array.copyFrom(MemorySegment.ofArray(new int[] { 0, 9, 3, 4, 6, 5, 1, 8, 2, 7 }));
    qsort.invokeExact(array.address(), 10L, 4L, comparFunc.address());
}
```

This API represents a significant improvement in Java's native interoperability capabilities, providing a safer, more efficient, and more maintainable alternative to JNI while maintaining high performance and safety guarantees.

核心目的：

- 引入用于绑定原生代码的 API
- 与外部内存访问 API 协同工作
- 简化无需 JNI 的原生库集成
- 为原生互操作提供纯 Java 开发模型

主要特性：

- 原生库中的符号查找
- 支持下调用（Java 调用原生代码）
- 支持上调用（原生代码调用 Java）
- 平台特定的类型映射

关键组件：

```java
// 库查找
LibraryLookup libclang = LibraryLookup.ofLibrary("clang");
LibraryLookup.Symbol symbol = libclang.lookup("function_name");

// CLinker 接口
interface CLinker {
    MethodHandle downcallHandle(LibraryLookup.Symbol func,
                              MethodType type,
                              FunctionDescriptor function);
    MemorySegment upcallStub(MethodHandle target,
                            FunctionDescriptor function);
}
```

下调用示例（Java 调用原生代码）：

```java
// 调用 strlen 示例
MethodHandle strlen = CLinker.getInstance().downcallHandle(
    LibraryLookup.ofDefault().lookup("strlen"),
    MethodType.methodType(long.class, MemoryAddress.class),
    FunctionDescriptor.of(C_LONG, C_POINTER)
);

try (MemorySegment str = CLinker.toCString("Hello")) {
    long len = strlen.invokeExact(str.address()); // 5
}
```

上调用示例（原生代码调用 Java）：

```java
// 从 Java 方法创建函数指针
MemorySegment comparFunc = CLinker.getInstance().upcallStub(
    comparHandle,
    FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER)
);
```

安全特性：

- 需要显式许可的受限操作
- 通过 FunctionDescriptor 实现类型安全
- 通过 MemorySegment 实现内存安全
- 受控的原生库加载

主要优势：

- 无需 JNI 代码
- 比 JNI 性能更好
- 纯 Java 开发模型
- 类型安全的原生互操作
- 简化的原生库集成

实现细节：

- 作为孵化器模块提供（jdk.incubator.foreign）
- 需要 -Dforeign.restricted=permit 标志
- 平台特定的优化
- JIT 编译器支持

限制和范围：

- 初始重点支持 C 语言
- 支持 x64 和 AArch64 平台
- 不自动处理头文件
- 不改变部署模型

复杂使用示例（qsort）：

```java
// 使用 Java 比较器的原生 qsort
MethodHandle qsort = CLinker.getInstance().downcallHandle(
    LibraryLookup.ofDefault().lookup("qsort"),
    MethodType.methodType(void.class, MemoryAddress.class, long.class,
                         long.class, MemoryAddress.class),
    FunctionDescriptor.ofVoid(C_POINTER, C_LONG, C_LONG, C_POINTER)
);

// 使用带有 Java 回调的原生函数
try (MemorySegment array = MemorySegment.allocateNative(4 * 10)) {
    array.copyFrom(MemorySegment.ofArray(new int[] { 0, 9, 3, 4, 6, 5, 1, 8, 2, 7 }));
    qsort.invokeExact(array.address(), 10L, 4L, comparFunc.address());
}
```

这个 API 代表了 Java 原生互操作能力的重大改进，提供了一个比 JNI 更安全、更高效、更易维护的替代方案，同时保持了高性能和安全性保证。它使得 Java 开发者能够更容易地与原生代码交互，而不需要编写复杂的 JNI 代码。

## JEP 412: Foreign Function & Memory API (Incubator) - JDK 17

Purpose:

- Introduces an API for Java programs to interoperate with code and data outside the JVM
- Aims to replace JNI (Java Native Interface) with a safer, more efficient solution
- Enables calling native libraries and processing native data without JNI's limitations

Main Goals:

- Ease of use: Provides a pure-Java development model
- Performance: Comparable or better than existing APIs like JNI
- Generality: Works with different types of foreign memory
- Safety: Disables unsafe operations by default

Key Components:

- Memory Segments: For managing foreign memory (off-heap)
- Resource Scopes: For controlling memory lifecycle and thread access
- Memory Layouts: For describing memory structure
- CLinker: For interoperating between Java and native code
- SymbolLookup: For finding symbols in native libraries

Safety Features:

- Strong spatial and temporal bounds enforcement
- Thread confinement guarantees
- Controlled memory deallocation
- Safe memory dereferencing

Improvements over JNI:

- No need for separate header files or native implementations
- Better type system integration
- More straightforward memory management
- Improved performance for native calls

Key Classes and Interfaces:

- MemorySegment
- MemoryAddress
- SegmentAllocator
- ResourceScope
- CLinker
- SymbolLookup

The API provides a modern, safer alternative to JNI while maintaining high performance and adding features like deterministic memory management and improved native code integration.

主要目的：

- 引入一个新的API，让Java程序能够与JVM之外的代码和数据进行交互
- 目标是替代传统的JNI（Java原生接口），提供更安全、更高效的解决方案
- 能够调用原生库并处理原生数据，避免JNI的局限性

核心目标：

- 易用性：提供纯Java的开发模型
- 性能：与现有API（如JNI）相比，性能相当或更好
- 通用性：可以处理不同类型的外部内存
- 安全性：默认禁用不安全的操作

主要组件：

- 内存段（Memory Segments）：用于管理外部内存（堆外内存）
- 资源作用域（Resource Scopes）：控制内存生命周期和线程访问
- 内存布局（Memory Layouts）：描述内存结构
- CLinker：用于Java和原生代码之间的互操作
- 符号查找（SymbolLookup）：用于在原生库中查找符号

安全特性：

- 强大的空间和时间边界检查
- 线程约束保证
- 可控的内存释放
- 安全的内存解引用

相比JNI的改进：

- 不需要单独的头文件或原生实现
- 更好的类型系统集成
- 更直接的内存管理
- 提升了原生调用的性能

关键类和接口：

- MemorySegment（内存段）
- MemoryAddress（内存地址）
- SegmentAllocator（段分配器）
- ResourceScope（资源作用域）
- CLinker（C语言链接器）
- SymbolLookup（符号查找）

这个API提供了一个现代化、更安全的JNI替代方案，同时保持了高性能，并增加了确定性内存管理和改进的原生代码集成等特性。这对于需要与原生代码交互的Java开发者来说是一个重要的改进。

## JEP 419: Foreign Function & Memory API (Second Incubator) - JDK 18

Core Components:

```java
// Key classes you'll work with
CLinker linker = CLinker.systemCLinker();
MemorySegment segment;
SegmentAllocator allocator;
```

Basic Memory Allocation:

```java
// Allocate native memory
try (ResourceScope scope = ResourceScope.newConfinedScope()) {
    MemorySegment segment = MemorySegment.allocateNative(100, scope);
    // Use the segment here
} // Memory automatically freed
```

Working with Native Functions (Example with strlen):

```java
// Call native function
CLinker linker = CLinker.systemCLinker();
MethodHandle strlen = linker.downcallHandle(
    linker.lookup("strlen").get(),
    FunctionDescriptor.of(JAVA_LONG, ADDRESS)
);

// Use the function
MemorySegment str = implicitAllocator().allocateUtf8String("Hello");
long len = (long)strlen.invoke(str);  // Returns 5
```

Memory Layout Example:

```java
// Define struct layout
SequenceLayout arrayLayout = MemoryLayout.sequenceLayout(10,
    MemoryLayout.structLayout(
        ValueLayout.JAVA_INT.withName("x"),
        ValueLayout.JAVA_INT.withName("y")
    )
);
```

Key Safety Features:

- Use ResourceScope for memory lifecycle management
- Enable unsafe operations with --enable-native-access=MODULE
- Use confined scopes for thread safety

Quick Tips:

- Always use try-with-resources for ResourceScope
- Use SegmentAllocator for efficient memory allocation
- Check platform differences for C type mappings

Basic Array Handling:

```java
// Working with arrays
try (ResourceScope scope = ResourceScope.newConfinedScope()) {
    MemorySegment array = SegmentAllocator.nativeAllocator(scope)
        .allocateArray(ValueLayout.JAVA_INT, 
            new int[] { 1, 2, 3, 4, 5 });
    // Use array here
}
```

Error Handling:

```java
try {
    // Native operations
} catch (IllegalStateException e) {
    // Handle scope closure
} catch (UnsupportedOperationException e) {
    // Handle unsupported platform operations
}
```

Key things to remember:

- Always manage memory with ResourceScope
- Use proper layouts for data structures
- Consider platform differences
- Enable native access when needed
- Use confined scopes for single-thread access
- Use shared scopes for multi-thread access

## JEP 424: Foreign Function & Memory API (Preview) - JDK 19

Core Purpose:

- Provides a better alternative to JNI (Java Native Interface)
- Enables straightforward consumption of native libraries
- Simplifies native interoperation without tedious glue code

Key Components:

```java
// Main API components in java.lang.foreign package:
- MemorySegment, MemoryAddress - For foreign memory allocation
- SegmentAllocator - For memory manipulation
- MemorySession - For memory lifecycle control
- Linker, FunctionDescriptor, SymbolLookup - For calling foreign functions
```

Example Usage (String sorting):

```java
// Example calling C's radixsort
Linker linker = Linker.nativeLinker();
SymbolLookup stdlib = linker.defaultLookup();
MethodHandle radixSort = linker.downcallHandle(
    stdlib.lookup("radixsort").get(),
    ...
);

// Using with strings
String[] javaStrings = { "mouse", "cat", "dog", "car" };
SegmentAllocator allocator = SegmentAllocator.implicitAllocator();
MemorySegment offHeap = allocator.allocateArray(ValueLayout.ADDRESS, 
                                               javaStrings.length);
```

Memory Management:

```java
// Using memory sessions for deterministic deallocation
try (MemorySession session = MemorySession.openConfined()) {
    MemorySegment segment = MemorySegment.allocateNative(100, session);
    // Use segment here
} // Memory automatically freed
```

Structured Memory Access:

```java
// Define struct layout
SequenceLayout ptsLayout = MemoryLayout.sequenceLayout(10,
    MemoryLayout.structLayout(
        ValueLayout.JAVA_INT.withName("x"),
        ValueLayout.JAVA_INT.withName("y")
    ));
```

Safety Features:

- Spatial bounds checking
- Temporal bounds checking
- Thread confinement guarantees
- Controlled memory deallocation

Memory Sessions Types:

- Confined sessions (single-thread access)
- Shared sessions (multi-thread access)
- Implicit sessions (GC-managed lifecycle)

Working with Foreign Functions:

```java
// Looking up and calling foreign functions
SymbolLookup lookup = SymbolLookup.libraryLookup("libname.so", session);
MemorySegment symbol = lookup.lookup("functionName").get();
MethodHandle handle = linker.downcallHandle(symbol, descriptor);
```

Key Benefits:

- Pure Java development model
- No JNI glue code needed
- Better type safety
- Improved performance
- Deterministic resource management

Safety Considerations:

```java
// Unsafe operations require explicit enabling
// Use --enable-native-access=MODULE command line option
try (MemorySession session = MemorySession.openConfined()) {
    // Unsafe operations here
} catch (IllegalStateException e) {
    // Handle safety violations
}
```

Best Practices:

- Always use try-with-resources for sessions
- Prefer confined sessions when possible
- Use proper error handling
- Consider platform differences
- Use appropriate memory layouts

Platform Considerations:

```java
// Handle platform-specific type differences
FunctionDescriptor descriptor = switch(Platform.getCurrent()) {
    case WINDOWS -> FunctionDescriptor.of(JAVA_INT, JAVA_INT);
    case LINUX, MACOS -> FunctionDescriptor.of(JAVA_LONG, JAVA_INT);
    default -> throw new UnsupportedOperationException("Unsupported platform");
};
```

## JEP 434: Foreign Function & Memory API (Second Preview) - JDK 20

Key changes from JEP 424 to JEP 434

1. Terminology and Class Changes:

- JEP 424: Uses `MemorySession` for memory management [1]
- JEP 434: Introduces `Arena` and `SegmentScope`, splitting the functionality of `MemorySession`

```java
// JEP 424
try (MemorySession session = MemorySession.openConfined()) {
    // Memory operations
}

// JEP 434
try (Arena arena = Arena.openConfined()) {
    // Memory operations
}
```

2. Resource Management:

- JEP 424: Focused on `MemorySession` for resource management

- JEP 434: Introduces more granular control with Arena and `SegmentScope` separation

```java
// JEP 434 new approach
try (Arena arena = Arena.openConfined()) {
    MemorySegment segment = arena.allocateArray(ValueLayout.ADDRESS, size);
    // Use arena's scope
    MemorySegment other = MemorySegment.allocateNative(100, arena.scope());
}
```

3. Memory Allocation:

- JEP 424: Uses `MemorySegment.allocateNative`

- JEP 434: Introduces more direct allocation through Arena

```java
// JEP 434 allocation methods
try (Arena arena = Arena.openConfined()) {
    MemorySegment array = arena.allocateArray(ValueLayout.JAVA_INT, 1, 2, 3);
    MemorySegment string = arena.allocateUtf8String("Hello");
}
```

4. Scope Management:

- JEP 424: Combined session management

- JEP 434: Separates concerns between Arena (allocation) and SegmentScope (lifetime management)


5. Memory Sharing:

- JEP 434: Enhanced support for sharing segments across maintenance boundaries

- Improved handling of segment sharing between different operations


6. API Simplification:

- JEP 434: More streamlined API with clearer separation of concerns

- Better integration with pattern matching in switch expressions


7. Safety Improvements:

- JEP 434: Enhanced temporal and spatial safety guarantees

- Better thread confinement controls

Example showing the evolution:

```java
// JEP 424 style
try (MemorySession session = MemorySession.openConfined()) {
    MemorySegment segment = MemorySegment.allocateNative(100, session);
    // Use segment
}

// JEP 434 style
try (Arena arena = Arena.openConfined()) {
    MemorySegment segment = arena.allocate(100);
    // Use segment with arena's scope
    MemorySegment other = MemorySegment.allocateNative(100, arena.scope());
}
```

8. Key Benefits of JEP 434:

- Clearer separation of concerns

- More intuitive API design

- Better support for maintenance boundaries

- Enhanced memory sharing capabilities

- Improved integration with modern Java features


9. Migration Considerations:

```java
// When migrating from JEP 424 to 434:
// Old way
MemorySession session = MemorySession.openConfined();

// New way
Arena arena = Arena.openConfined();
SegmentScope scope = arena.scope();
```

10. Enhanced Features in JEP 434:

- Better support for structured memory access
- Improved memory allocation patterns
- More flexible scope management
- Enhanced thread safety guarantees

These changes in JEP 434 represent a significant evolution in the API's design, focusing on:

- Better separation of concerns
- More intuitive usage patterns
- Enhanced safety guarantees
- Improved maintainability
- Better integration with modern Java features

## JEP 442: Foreign Function & Memory API (Third Preview) - JDK 21

Key changes from JEP 434 to JEP 442:

1. Arena Creation Methods:

```java
// JEP 434
Arena arena = Arena.openConfined();

// JEP 442
Arena arena = Arena.ofConfined();  // New naming convention
```

2. Memory Management Types:

- JEP 434: Used `Arena.openConfined()` and `Arena.openShared()`

- JEP 442: Introduces clearer factory methods:

```java
// JEP 442 arena types
Arena global = Arena.global();         // Unbounded lifetime
Arena auto = Arena.ofAuto();           // GC-managed
Arena confined = Arena.ofConfined();   // Single-thread, deterministic
```

3. Null Handling:

```java
// JEP 442 introduces MemorySegment.NULL
radixsort.invoke(pointers, length, MemorySegment.NULL, '\0');
```

4. Memory Segment Lifecycle:

- JEP 434: Focused on Arena-based lifecycle
- JEP 442: Introduces more granular control with different arena types:
  - Global arena (always alive)
  - Automatic arena (GC-managed)
  - Confined arena (deterministic deallocation)

5. Memory Access:

```java
// JEP 442 introduces more structured access patterns
VarHandle xHandle = ptsLayout.varHandle(
    PathElement.sequenceElement(),
    PathElement.groupElement("x")
);
```

6. Safety Improvements:

- Enhanced spatial and temporal bounds checking
- Better thread confinement guarantees
- More explicit memory lifecycle management

7. API Refinements:

```java
// JEP 442 style
try (Arena offHeap = Arena.ofConfined()) {
    MemorySegment segment = offHeap.allocateArray(ValueLayout.ADDRESS, size);
    // Use segment
} // Deterministic deallocation
```

8. Memory Allocation Patterns:

```java
// JEP 442 introduces more flexible allocation patterns
SegmentAllocator allocator = SegmentAllocator.slicingAllocator(segment);
for (int i = 0; i < 10; i++) {
    MemorySegment s = allocator.allocateArray(JAVA_INT, 1, 2, 3, 4, 5);
}
```

9. Key Improvements:

- More intuitive API design
- Better memory management patterns
- Enhanced safety guarantees
- Improved allocation strategies
- Clearer lifecycle management

10. Migration Considerations:

```java
// When migrating from JEP 434 to 442:
// Old way
Arena.openConfined()
Arena.openShared()

// New way
Arena.ofConfined()
Arena.ofAuto()
Arena.global()
```

11. Enhanced Features in JEP 442:

- Better support for different memory management strategies
- More flexible allocation patterns
- Improved safety guarantees
- Clearer API semantics
- Better integration with native code

The main themes of changes in JEP 442 are:

- API refinement and clarity
- Enhanced memory management options
- Improved safety features
- Better performance characteristics
- More flexible allocation strategies

## JEP 454: Foreign Function & Memory API - JDK 22

Key changes from JEP 442 to JEP 454:

1. Memory Segment Types and Bounds:

```java
// JEP 454 introduces clearer spatial and temporal bounds concepts
// Example of spatial bounds
MemorySegment data = Arena.global().allocate(100);
// Range of addresses from base address b to b + 99 inclusive
```

2. Arena Types Evolution:

```java
// JEP 442
Arena global = Arena.global();
Arena auto = Arena.ofAuto();
Arena confined = Arena.ofConfined();

// JEP 454 emphasizes different arena purposes
// Global arena - unbounded lifetime
MemorySegment data = Arena.global().allocate(100);

// Automatic arena - GC managed
void processData() {
    MemorySegment data = Arena.ofAuto().allocate(100);
    // Memory freed by GC later
}

// Confined arena - deterministic deallocation
try (Arena processing = Arena.ofConfined()) {
    MemorySegment input = processing.allocate(100);
    MemorySegment output = processing.allocate(100);
} // Immediate deallocation
```

3. Memory Layout and Access:

```java
// JEP 454 introduces more detailed layout handling
SequenceLayout ptsLayout = MemoryLayout.sequenceLayout(10,
    MemoryLayout.structLayout(
        ValueLayout.JAVA_INT.withName("x"),
        ValueLayout.JAVA_INT.withName("y")
    ));

// Value layout with explicit properties
ValueLayout intLayout = ValueLayout.JAVA_INT.withName("x")
                                          .withBitAlignment(32);
```

4. Segment Allocators Enhancement:

```java
// JEP 454 introduces more flexible allocation patterns
class SlicingArena implements Arena {
    final Arena arena = Arena.ofConfined();
    final SegmentAllocator slicingAllocator;

    SlicingArena(long size) {
        slicingAllocator = SegmentAllocator.slicingAllocator(
            arena.allocate(size));
    }
}
```

5. Key Changes in Safety Features:

- More explicit spatial bounds checking
- Enhanced temporal bounds guarantees
- Clearer thread confinement rules
- Better memory deallocation control

6. Symbol Lookup Changes:

```java
// JEP 454 clarifies symbol lookup types
SymbolLookup.libraryLookup(String, Arena)  // Library lookup
SymbolLookup.loaderLookup()                // Loader lookup
Linker.defaultLookup()                     // Default lookup
```

7. Foreign Function Integration:

```java
// JEP 454 provides clearer function descriptor usage
MethodHandle strlen = linker.downcallHandle(
    linker.defaultLookup().find("strlen").get(),
    FunctionDescriptor.of(JAVA_LONG, ADDRESS)
);
```

8. Memory Access Patterns:

```java
// JEP 454 emphasizes value layout usage
segment.setAtIndex(ValueLayout.JAVA_INT,
                  /* index */ i,
                  /* value to write */ i);
```

9. Major Improvements:

- Clearer memory bounds concepts
- Enhanced arena lifecycle management
- Better symbol lookup organization
- Improved memory access patterns
- More explicit safety guarantees

10. Key Architectural Changes:

- Better separation of concerns
- More explicit memory management
- Enhanced safety guarantees
- Improved API organization
- Clearer documentation of safety features

11. Safety Considerations:

```java
// JEP 454 emphasizes safety with clear bounds
try (Arena confined = Arena.ofConfined()) {
    MemorySegment segment = confined.allocate(100);
    // Spatial and temporal bounds enforced
} // Deterministic deallocation
```

The main themes of changes in JEP 454 are:

1. Enhanced clarity around memory bounds
2. More explicit memory management
3. Better organized symbol lookup
4. Improved safety guarantees
5. Clearer API documentation
6. Better structured memory access
7. Enhanced arena lifecycle management