## Introduction

The use of class loaders is crucial in Java dependency management.
Class loaders in java are responsible for loading classes into the Java Virtual Machine (JVM) dynamically at runtime.
In this guide, we will explore the intricacies of using class loaders for managing dependencies in Java applications.

## Different Types of Class Loaders in java

Class loaders in Java can be categorized into three main types:
A. Bootstrap Class Loader,
B. Extensions Class Loader, and
C. System/Application Class Loader.
Each type has a specific role in loading classes based on their location and priority within the class loading process.

### Bootstrap Class Loader

The Bootstrap Class Loader is the first class loader that comes into action when the JVM starts.
It is the parent of all other class loaders and is responsible for loading the core Java libraries found in the `rt.jar` file.
It loads standard JDK classes such as those residing in `java.lang` and `java.util` packages.
The Bootstrap Class Loader is written in native languages, and therefore, it is not represented by a specific class in the Java language.

## Extension Class Loader

The Extension Class Loader is responsible for loading classes from the extension directories.
It loads classes from the extensions directory, usually located at ***$JAVA_HOME/lib/ext***.
This class loader is primarily used for extending the functionality of the Java core platform by adding additional libraries and resources.

This type of class loader comes into action after the Bootstrap Class Loader and before the System/Application Class Loader.
It plays a vital role in enhancing the Java platform with additional functionality beyond the core libraries and APIs.

## System/Application Class Loader

The System/Application Class Loader, also known as the Application Class Loader, is responsible for loading application-specific classes from the classpath.
When a Java application is launched, this class loader is used to load classes from the application’s classpath, including JAR files and directories specified by the ***CLASSPATH\*** environment variable.
This class loader is crucial for loading classes that are specific to the application being executed.
It is also worth noting that the System/Application Class Loader is a part of the standard Java class loader hierarchy and is used extensively in Java applications.

## Class Loading Process

The class loading process in Java involves three steps:

### 3.1. Loading

During this phase, the class loader reads the binary data (bytecode) of a class file and creates a `java.lang.Class` object for it.
The class loader searches for the class in its designated classpath.

### 3.2. Linking

The linking phase consists of three steps: verification, preparation, and resolution. Verification ensures that the loaded class is structurally correct. Preparation involves allocating memory for class variables and initializing them with default values. Resolution involves replacing symbolic references in the class with direct references.

### 3.3. Initialization

In this final phase, the class loader initializes the class by executing the static initializers and initializing static fields. This phase completes the class loading process, and the class is now ready for use.

## Delegation Model

Class loaders in Java follow a delegation model, where each class loader delegates the class-loading request to its parent before attempting to load the class itself.
This model ensures that classes are loaded consistently and avoids redundant loading.
If a parent class loader can successfully load the class, the child class loader avoids duplicating the effort.

Suppose some classes need to be loaded into the JVM.
The system class loader will delegate the task to extension class loader(its parent), which in turn will delegate it to the bootstrap class loader(parent of extension class loader).

The system class loader will try to load the class only if these two are unsuccessful.

This delegation model ensures consistency in class loading and helps maintain a separation of concerns between the different class loaders.

## Factors Influencing Class Loading in Java

Keep in mind that class loading in Java is influenced by several key factors.
Understanding these factors is crucial for a thorough grasp of how class loading works in the Java Virtual Machine (JVM).
Let’s take a closer look at some of the main factors influencing class loading in Java.

### Understanding Class Loading Hierarchy

On the surface, the class loading process may seem straightforward, but there is a complex hierarchy involved.
The bootstrap class loader, extension class loader, and application class loader form a hierarchical relationship, with each loader responsible for loading classes based on its specific scope and visibility.

Recognizing and understanding this hierarchy is essential for troubleshooting class loading issues and optimizing the overall performance of a Java application.

### The Role of Classpath in Class Loading

For a Java application, the classpath is a critical factor that influences class loading.
The classpath is a parameter that tells the JVM where to look for user-defined classes and packages.
It can consist of directories, JAR files, and ZIP files, and it plays a significant role in determining which classes are loaded by the JVM.

For developers, understanding how the classpath works is essential for managing dependencies and ensuring that the correct classes are loaded into the application.

Influencing the class loading process are other factors like the visibility of classes, namespace conflicts, and the structure of the classpath.
These factors can have a significant impact on how classes are loaded and utilized within a Java application.

### How Namespace and Visibility Affect Class Loading

Class loading in Java is also influenced by namespace and visibility rules.
The namespace of a class determines its unique identifier, and conflicts within the namespace can lead to class loading issues.
Additionally, the visibility of classes, as determined by access modifiers like public, protected, and private, affects how classes are loaded and accessed within a Java application.

With a solid understanding of how namespace and visibility affect class loading, developers can write more robust and maintainable Java code.

## java.lang.ClassLoader

The `java.lang.ClassLoader` class is part of the `java.lang` package and serves as the foundation for all class loaders in Java.
It is an [**abstract class**](https://codippa.com/abstract-class-java/) that provides the basic structure and functionality for loading classes into the JVM.

Typically the classes are loaded by their name using its `loadClass()` method.
Internally, it checks if the class is already loaded with `findLoadedClass()` method and tries to load it, only if it is not.

Below is the source of `loadClass()` method from `java.lang.ClassLoader` class

```java
protected Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException {
  synchronized (getClassLoadingLock(name)) {
    // First, check if the class has already been loaded
    Class<?> c = findLoadedClass(name);
    if (c == null) {
       long t0 = System.nanoTime();
       try {
         if (parent != null) {
           c = parent.loadClass(name, false);
         } else {
           c = findBootstrapClassOrNull(name);
         }
       } catch (ClassNotFoundException e) {
         // ClassNotFoundException thrown if class not found
         // from the non-null parent class loader
       }

       if (c == null) {
         // If still not found, then invoke findClass in order
         // to find the class.
         long t1 = System.nanoTime();
         c = findClass(name);

         // this is the defining class loader; record the stats
         PerfCounter.getParentDelegationTime().addTime(t1 - t0);
         PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
         PerfCounter.getFindClasses().increment();
       }
     }
     if (resolve) {
        resolveClass(c);
     }
     return c;
   }
}
```

`loadClass()` follows delegation model, where it will first ask the parent class loader to load the class, as visible in above method.

If the parent is not able to load the class, then it will load the class itself.
Class Loader will attempt to load the class from a physical file using its name.

If the parent and `ClassLoader` is not able to load the class, then `ClassNotFoundException` is thrown.

## Custom Class Loaders

Custom class loaders allow developers to extend or customize the default class loading behavior in Java.
By implementing custom class loaders, developers can dynamically load classes from sources other than the default classpath, such as databases or remote servers, and define their own class-loading strategy.

To implement custom class loader, we need to define a new class that extends `java.lang.ClassLoader` and implement its `findClass()` method as shown below

```java
public class AppClassLoader extends ClassLoader {

    @Override
    public Class findClass(String name) throws ClassNotFoundException {
        byte[] c = loadClassFromFile(name);
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassFromFile(String fileName)  {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
                fileName.replace('.', File.separatorChar) + ".class");
        byte[] byteBuffer;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        int nextValue = 0;
        try {
            while ( (nextValue = inputStream.read()) != -1 ) {
                bStream.write(nextValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        byteBuffer = bStream.toByteArray();
        return byteBuffer;
    }
}
```

When implementing custom class loaders, it’s important to carefully manage the class loading process and handle potential conflicts with the default class loading mechanism.
Custom class loaders can give you fine-grained control over class loading, but they also come with added complexity and potential pitfalls, so use them judiciously.

## Caching Classes

With proper use of class loaders in java, you can enhance the performance of your Java applications.
One approach is to implement class caching, where frequently used classes are cached in memory to reduce the overhead of class loading.
Here’s a simple example of how class caching can be implemented:

```java
Map<String, Class<?>> classCache = new HashMap<>();

public Class<?> loadClass(String name) throws ClassNotFoundException {
    if (classCache.containsKey(name)) {
        return classCache.get(name);
    } else {
        // Load the class and put it in the cache
        Class<?> clazz = findClass(name);
        classCache.put(name, clazz);
        return clazz;
    }
}
```

By effectively managing class loading and caching, you can significantly improve the overall performance of your Java applications.

## Conclusion

Understanding class loaders in Java is crucial for every beginner.
Class loaders play a key role in the Java runtime environment, and understanding how they work can help developers avoid common pitfalls and optimize their application’s performance.
By delving into the concepts of class loading, delegation, and visibility, beginners can gain a solid foundation in Java development.
It is important to continue exploring and experimenting with class loaders in java to deepen your understanding and become a proficient Java developer.