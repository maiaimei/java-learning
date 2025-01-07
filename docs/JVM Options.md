# JVM Options

JVM options are command-line arguments that you can pass to the Java Virtual Machine (JVM) to configure its behavior. These options can control various aspects of the JVM, such as memory allocation, garbage collection, debugging, and performance tuning.

## Types of JVM Options

1. **Standard Options**: These are options that are guaranteed to be supported by all JVM implementations.
2. **Non-Standard Options**: These options begin with `-X` and are not guaranteed to be supported by all JVM implementations.
3. **Advanced Options**: These options begin with `-XX` and are used for advanced tuning and debugging.

## Common JVM Options

### Standard Options

- `-cp` or `-classpath`: Specifies the class path.
- `-Dproperty=value`: Sets a system property.
- `-verbose`: Enables verbose output.
- `-version`: Prints the JVM version and exits.
- `-showversion`: Prints the JVM version and continues execution.

### Non-Standard Options

- `-Xms<size>`: Sets the initial heap size.
- `-Xmx<size>`: Sets the maximum heap size.
- `-Xss<size>`: Sets the stack size for each thread.
- `-Xloggc:<file>`: Logs garbage collection activity to a file.
- `-Xdebug`: Enables debugging support.

### Advanced Options

- `-XX:+UseG1GC`: Enables the G1 garbage collector.
- `-XX:MaxPermSize=<size>`: Sets the maximum permanent generation space size (for Java 7 and earlier).
- `-XX:+PrintGCDetails`: Prints detailed garbage collection information.
- `-XX:+HeapDumpOnOutOfMemoryError`: Generates a heap dump when an `OutOfMemoryError` is thrown.
- `-XX:MetaspaceSize=<size>`: Sets the initial metaspace size (for Java 8 and later).

## Example Usage

To run a Java application with specific JVM options, you can use the following command:

```shell
java -Xms512m -Xmx1024m -XX:+UseG1GC -jar myapp.jar
```

In this example:

- `-Xms512m`: Sets the initial heap size to 512 MB.
- `-Xmx1024m`: Sets the maximum heap size to 1024 MB.
- `-XX:+UseG1GC`: Enables the G1 garbage collector.
- `-jar myapp.jar`: Specifies the JAR file to run.