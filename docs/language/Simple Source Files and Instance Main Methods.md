# Simple Source Files and Instance Main Methods

## JEP 445: Unnamed Classes and Instance Main Methods (Preview) - JDK 21

```java
// Traditional way
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}

// New simplified way - main need not be `public`, need not be not `static`, and need not have a `String[]` parameter. 
class HelloWorld { 
    void main() { 
        System.out.println("Hello, World!");
    }
}


// New simplified way - unnamed classes
void main() {
    System.out.println("Hello World");
}

```

When launching a class, the launch protocol chooses the first of the following methods to invoke:

1. A `static void main(String[] args)` method of non-private access (i.e., `public`, `protected` or package) declared in the launched class,
2. A `static void main()` method of non-private access declared in the launched class,
3. A `void main(String[] args)` instance method of non-private access declared in the launched class or inherited from a superclass, or, finally,
4. A `void main()` instance method of non-private access declared in the launched class or inherited from a superclass.

## JEP 463: Implicitly Declared Classes and Instance Main Methods (Second Preview) - JDK 22

```java
// Support for instance methods and fields
String greeting = "Hello World";  // instance field

void main() {
    System.out.println(greeting());  // can call instance methods
}

String greeting() {  // instance method
    return greeting;
}
```

**Implicitly Declared Classes**

- Source files can now omit explicit class declarations
- Methods and fields not enclosed in a class declaration form an implicit class
- Implicit classes are always in the unnamed package
- The class name is chosen by the host system

**A flexible launch protocol**

- Allow the `main` method of a launched class to have `public`, `protected`, or default (i.e., package) access.
- If the launched class contains a `main` method with a `String[]` parameter then choose that method.
- Otherwise, if the class contains a `main` method with no parameters then choose that method.
- In either case, if the chosen method is `static` then simply invoke it.
- Otherwise, the chosen method is an instance method and the launched class must have a zero-parameter, non-`private` constructor (i.e., of `public`, `protected`, or package access). Invoke that constructor and then invoke the `main` method of the resulting object. If there is no such constructor then report an error and terminate.
- If there is no suitable `main` method then report an error and terminate.

**main 方法的访问级别变化**

- 允许 main 方法具有 public、protected 或默认(包)访问级别
- 不再强制要求 public static

**main 方法的参数支持**

- 优先选择带有 String 参数的 main 方法
- 如果没有，则选择无参数的 main 方法

**静态和实例方法的处理**

- 如果是静态方法，直接调用
- 如果是实例方法：
  - 类必须有一个零参数、非私有的构造函数（public、protected 或包访问级别）
  - 自动创建实例并调用 main 方法

**错误处理**

- 如果没有合适的构造函数，报错并终止
- 如果没有合适的 main 方法，报错并终止

这些改变使得 Java 程序的入口点更加灵活，特别是：

- 简化了初学者的学习曲线
- 提供了更多的编程模式选择
- 保持了向后兼容性
- 降低了编写简单程序的复杂度

## JEP 477: Implicitly Declared Classes and Instance Main Methods (Third Preview) - JDK 23

Implicitly declared classes automatically import three `static` methods for simple textual I/O with the console. These methods are declared in the new top-level class [`java.io.IO`](https://cr.openjdk.org/~prappo/8305457/java.base/java/io/IO.html). The new class [`java.io.IO`](https://cr.openjdk.org/~prappo/8305457/java.base/java/io/IO.html) is a preview API in JDK 23.

```java
public static void println(Object obj);
public static void print(Object obj);
public static String readln(String prompt);
```

```java
void main() {
    String name = readln("Please enter your name: ");
    print("Pleased to meet you, ");
    println(name);
}
```

Implicitly declared classes automatically import, on demand, all of the public top-level classes and interfaces of the packages exported by the `java.base` module.

```java
void main() {
    var authors = List.of("James", "Bill", "Guy", "Alex", "Dan", "Gavin");
    for (var name : authors) {
        println(name + ": " + name.length());
    }
}
```

To further simplify the writing of small programs, we make all of the public top-level classes and interfaces of the packages exported by the `java.base` module available for use in the body of every implicit class, as if they were imported on demand. Popular APIs in commonly used packages such as `java.io`, `java.math`, and `java.util` are thus immediately usable, without any fuss. In the above example, the `import java.util.List` declaration can be removed since the interface will be imported automatically.

[JEP 476, *Module Import Declarations*](https://openjdk.org/jeps/476), proposes a new import declaration, `import module M`, which imports, on demand, all of the public top-level class and interfaces of the packages exported by module `M`. Thus every implicitly declared class can be considered to implicitly import the `java.base` module, as if the declaration

```java
import module java.base;
```

appears at the start of every source file containing an implicit class.

## JEP 495: Simple Source Files and Instance Main Methods (Fourth Preview) - JDK 24

We here propose to preview it for a fourth time, with new terminology and a revised title but otherwise unchanged, in order to gain additional experience and feedback.

## Reference

[https://javaalmanac.io/features/jep445/](https://javaalmanac.io/features/jep445/)