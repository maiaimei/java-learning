# Java术语及其解释

## Java SE、Java EE、Java ME

**Java SE**（Java Platform, Standard Edition）：标准版本，包含Java语言核心的类，如数据库连接、接口定义、输入/输出、网络编程。

**Java EE**（Java Platform, Enterprise Edition）：企业版本，包含Java SE中的类，并增加了用于开发企业级应用的类，如EJB、Servlet、JSP、XML、事务控制。

**Java ME**（Java Platform, Micro Edition）：微型版本，为嵌入式设备提供的Java平台，包括虚拟机和一系列标准化的Java API。

## JCP、JEP、JSR

The **JCP([Java Community Process](https://www.jcp.org/en/home/index))** is the mechanism for developing standard technical specifications for Java technology. 

**[JEP](https://openjdk.org/jeps/0)(JDK Enhancement Proposals)**：**jdk 改进提案**，每当需要有新的设想时候，JEP 可以在 JCP(java  community Process)之前或者同时提出**非正式的规范(specification)**，被正式认可的 JEP 正式写进JDK 的
发展路线图并分配版本号。

JEPs的目的是引入新的功能、改进现有功能或修复问题，从而提升Java平台的能力和性能。JEPs的提出、审查和接受过程确保了新特性的质量和稳定性‌。

JEPs通过JCP进行定义和管理。JEPs根据其发展阶段进行分类：

- ‌**草案JEPs**‌：正在制定的提案，准备正式提交。
- **提交的JEPs**‌：已经完成创建过程，但尚未被接受。一旦被接受，将进入正在运行的阶段。
- **正在运行的JEPs**‌：目前正在研究的提案，可能包含在未来的Java版本中。
- ‌**交付的功能**‌：已经成功实施的JEP，成为JVM的一部分。
- ‌**撤回的JEPs**‌：未成功而被撤回的提案‌。

**JSR(Java Specification Requests)**：**java 规范提案**，新特性的规范出现在这一阶段，是指向 JCP(Java Community Process)提出新增一个**标准化技术规范的正式请求**。请求可以来自于小组/项目、JEP、JCF
成员或者 java 社区(community)成员的提案，每个java 版本都由相应的 JSR 支持。

## JRE、JDK、JVM

**JRE**（Java Runtime Environment）：Java运行环境，是运行Java程序所必须的环境集合，包含JVM标准实现及Java核心类库。

**JDK**（Java Development Kit）：Java开发工具包，包含JRE和开发工具，是整个Java开发的核心。

**JVM**（Java Virtual Machine）：Java虚拟机，是一种用于计算设备的规范，通过仿真模拟各种计算机功能来实现。Java语言使用JVM屏蔽了与具体平台相关的信息，使得Java程序可以在多种平台上运行。

# OpenJDK & Oracle JDK

**[OpenJDK](https://openjdk.org/)** and **Oracle JDK** are both implementations of the Java Development Kit (JDK), but they have some key differences in terms of licensing, support, performance, and features.

**OpenJDK** is an open-source implementation of the Java SE Platform and is licensed under the GNU General Public License (GNU GPL) version 2 with a linking exception. This means it is free to use, modify, and distribute. On the other hand, **Oracle JDK** is licensed under the Oracle Binary Code License Agreement, which requires a paid license for commercial use.

**Oracle JDK** provides long-term support (LTS) for its releases, with new LTS versions released every three years. In contrast, **OpenJDK** releases new versions every six months and only supports changes to a release until the next version is released. This means that Oracle JDK offers more stability and long-term support, while OpenJDK provides more frequent updates and new features.

There is no significant technical difference between the two, as the build process for Oracle JDK is based on OpenJDK. However, Oracle JDK is known for better performance and stability, especially in enterprise environments. Oracle JDK includes features like Flight Recorder, Java Mission Control, and Application Class-Data Sharing, which are not available in OpenJDK. Additionally, Oracle JDK has more garbage collection options and better renderers.

**OpenJDK** is developed by Oracle, Red Hat, and the Java Community, with contributions from companies like Red Hat, Azul Systems, IBM, Apple Inc., and SAP AG. Major Linux distributions like Fedora, Ubuntu, and Red Hat Enterprise Linux provide OpenJDK as the default Java SE implementation. **Oracle JDK**, on the other hand, is fully developed and maintained by Oracle.

See [https://www.baeldung.com/oracle-jdk-vs-openjdk](https://www.baeldung.com/oracle-jdk-vs-openjdk) for more details.

**OpenJDK** 发布地址：[https://openjdk.org/jeps/0](https://openjdk.org/jeps/0)

**OpenJDK** 下载地址：[https://jdk.java.net/archive/](https://jdk.java.net/archive/)

**Oracle JDK** 发布地址：[https://www.oracle.com/java/technologies/javase/jdk-relnotes-index.html](https://www.oracle.com/java/technologies/javase/jdk-relnotes-index.html)

**Oracle JDK** 下载地址：[https://www.oracle.com/java/technologies/downloads/archive/](https://www.oracle.com/java/technologies/downloads/archive/)

**Oracle Java SE Support Roadmap**: [https://www.oracle.com/java/technologies/java-se-support-roadmap.html](https://www.oracle.com/java/technologies/java-se-support-roadmap.html)

# Using Preview Features

Preview features are disabled by default. To enable them, we must use the enable-preview argument, which enables all preview features at once.

Enable preview features in the Command Line.

```shell
# compile
# Do not enable any preview features
javac Example.java
# Enable all preview features of JDK 21
javac --release 21 --enable-preview Example.java
# DISALLOWED
javac --release 20 --enable-preview Example.java

# Run with preview features of JDK 21
java --enable-preview Example
```

Enable preview features in Maven.

```xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <maven.compiler.release>21</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.13.0</version>
            <configuration>
                <source>${maven.compiler.source}</source>
                <target>${maven.compiler.target}</target>
                <release>${maven.compiler.release}</release>
                <compilerArgs>--enable-preview</compilerArgs>
            </configuration>
        </plugin>
    </plugins>
</build>
```

# Reference

[https://dev.java/learn/](https://dev.java/learn/)

[The Java™ Tutorials](https://docs.oracle.com/javase/tutorial/getStarted/intro/index.html)

[Java Language and Virtual Machine Specifications](https://docs.oracle.com/javase/specs/)

[https://javaalmanac.io/](https://javaalmanac.io/)
