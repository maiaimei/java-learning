[https://openjdk.org/projects/jdk/21/](https://openjdk.org/projects/jdk/21/)

[https://openjdk.org/projects/jdk/21/jeps-since-jdk-17](https://openjdk.org/projects/jdk/21/jeps-since-jdk-17)



JDK 21 reached [General Availability](https://openjdk.org/projects/jdk8/milestones#General_Availability) on 19 September 2023. JDK 21 is a long-term support (LTS) release, introduced several new features and enhancements. Here are some of the notable ones:

1. **Virtual Threads (Preview)**:
   - Virtual threads are lightweight threads that dramatically reduce the effort of writing, maintaining, and observing high-throughput concurrent applications.
   - Virtual threads make it easier to write scalable applications by allowing more threads to run concurrently without the overhead of traditional Java threads.
2. **Pattern Matching for switch (Third Preview)**:
   - This feature enhances the switch statement and expression with pattern matching, allowing more concise and readable code when handling multiple conditions.
   - It provides a powerful way to destructure object hierarchies and perform more sophisticated data processing within switch statements.
3. **Record Patterns (Second Preview)**:
   - Record patterns allow you to match on the components of records in a concise and readable way.
   - This feature is useful for decomposing record values in pattern matching constructs like switch and if statements.
4. **Scoped Values (Incubator)**:
   - Scoped values provide a mechanism to share immutable data within and across threads, offering an alternative to thread-local variables.
   - They enable safer and more efficient data sharing in concurrent applications.
5. **Sequenced Collections**:
   - New interfaces for sequenced collections provide a unified way to handle collections that have a defined encounter order.
   - This includes support for methods such as `getFirst()`, `getLast()`, `reversed()`, and more.
6. **Foreign Function & Memory API (Third Preview)**:
   - This API allows Java programs to interoperate with code and data outside of the Java runtime.
   - It provides a safe and efficient way to access native libraries and manage off-heap memory.
7. **Structured Concurrency (Incubator)**:
   - Structured concurrency simplifies multi-threaded programming by treating multiple tasks running in different threads as a single unit of work.
   - It helps to streamline error handling and cancellation.
8. **Generational Shenandoah**:
   - Shenandoah is a garbage collector with low pause times. The new generational mode improves performance by better handling short-lived and long-lived objects.

These features aim to improve the performance, scalability, and maintainability of Java applications. For more details, you can refer to the [JDK 21 release notes](https://openjdk.java.net/projects/jdk/21/).



In JDK 21, several APIs have been removed. Here are some of the notable removals:

1. **Deprecated Methods and Classes**: Some methods and classes that were deprecated in earlier versions have been removed.
2. **Security Manager and Related APIs**: The Security Manager and related APIs have been removed.
3. **RMI Activation**: The RMI Activation mechanism has been removed.

For a detailed list of all removed APIs, you can refer to the official JDK 21 release notes and the JEPs (JDK Enhancement Proposals) associated with the release.



What are the new features in JDK21?

What are the remove APIs in JDK21?