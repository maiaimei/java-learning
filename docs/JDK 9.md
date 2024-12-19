# JDK 9

## Overview

JDK 9 is released on September 22, 2017.

See [What's New in JDK 9](https://docs.oracle.com/javase/9/whatsnew/toc.htm#JSNEW-GUID-C23AFD78-C777-460B-8ACE-58BE5EA681F6) for features and enhancements in this release.

See the [JDK 9 Release Notes](http://www.oracle.com/technetwork/java/javase/9-relnotes-3622618.html) for detailed information about this release. 

## New Features

### Java Platform Module System

Introduces a new kind of Java programing component, the module, which is a named, self-describing collection of code and data. 

[https://blogs.oracle.com/java/post/diving-into-the-modular-system](https://blogs.oracle.com/java/post/diving-into-the-modular-system)



JDK8 与 JDK9 目录结构比较



如何区分项目是模块化项目还是非模块项目？

如果项目的根目录中有模块描述文件module-info.java，JVM以模块化方式运行。

如果项目的根目录中无模块描述文件module-info.java，JVM以非模块方式运行。



module-info.java 是一个特殊的 Java 文件，位于模块的根目录下。它定义了模块的名称、导出的包、需要的其他模块以及其他模块相关的声明。

基本语法：

```java
module <module-name> {
  // 需要其他模块：一个模块依赖于其他模块
  requires <module-name>;  
  // 需要其他模块（传递依赖）：简化模块之间依赖管理
  requires transitive <module-name>;  
  // 需要其他模块（静态依赖）：编译时必须存在，但运行时不一定需要，JVM解析模块时不会加载静态依赖的模块（即使不存在，也不报错）
  requires static <module-name>;    
  // 导出包：声明一个模块的包对其他模块可见
  exports <package-name>;
  // 导出包（定向导出）：限定将包导出到某些模块，其它模块不可访问。多个模块使用逗号分隔。
  exports <package-name> to <module-name>;
    
  // 提供服务：多个实现类使用逗号分隔。
  provides <service-interface> with <implementation-class>;
  // 使用服务
  uses <service-interface>
      
  // 打开包（允许反射访问）
  opens <package-name>;
}
```



```shell
java --list-modules
java --list-modules <module-name>
```

## Enhancements and Deprecated



## About JVM



## Reference

[JDK 9 Is Released!](https://blogs.oracle.com/java/post/jdk-9-is-released)

[What's New in JDK 9](https://docs.oracle.com/javase/9/whatsnew/toc.htm#JSNEW-GUID-C23AFD78-C777-460B-8ACE-58BE5EA681F6)

[JDK 9 Release Notes](http://www.oracle.com/technetwork/java/javase/9-relnotes-3622618.html)

[JDK 9 Documentation](https://docs.oracle.com/javase/9/)

[JDK and JRE 9 Readme](https://www.oracle.com/java/technologies/jdk9-readme.html)

[Java SE Downloads](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
