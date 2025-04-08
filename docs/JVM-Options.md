# JVM Options

JVM options are command-line arguments that you can pass to the Java Virtual Machine (JVM) to configure its behavior. These options can control various aspects of the JVM, such as memory allocation, garbage collection, debugging, and performance tuning.

## Types of JVM Options

There are three main categories of options: standard options, non-standard options, and developer options. 

Standard options are expected to be accepted by all JVM implementations and are stable between releases (though they can be deprecated). 

Options that begin with -X are non-standard (not guaranteed to be supported on all JVM implementations), and are subject to change without notice in subsequent releases of the Java SDK. 

Options that begin with -XX are developer options and often have specific system requirements for correct operation and may require privileged access to system configuration parameters; they are not recommended for casual use. These options are also subject to change without notice.

1. **Standard Options**: These are options that are guaranteed to be supported by all JVM implementations.
2. **Non-Standard Options**: These options begin with `-X` and are not guaranteed to be supported by all JVM implementations.
3. **Advanced Options(Developer Options)**: These options begin with `-XX` and are used for advanced tuning and debugging.

Command-line flags control the values of internal variables in the JVM, all of which have a type and a default value. For boolean values, the mere presence or lack of presence of a flag on the command-line can control the value of the variables. For -XX boolean flags, a ‘+’ or '-' prefix before the name indicates a true or false value, respectively. For variables that require additional data, there are a number of different mechanisms used to pass that data in. Some flags accept the data passed in directly after the name of the flag without any delineator, while for other flags you have to separate the flag name from the data with a ‘:’ or a ‘=’ character. Unfortunately the method depends on the particular flag and its parsing mechanism. Developer flags (the -XX flags) appear in only three different forms: -XX:+*OptionName*, -XX:-*OptionName*, and -XX:*OptionName*=.

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

## Reference

[https://openjdk.org/groups/hotspot/docs/RuntimeOverview.html](https://openjdk.org/groups/hotspot/docs/RuntimeOverview.html)