# OpenJDK & Oracle JDK

**OpenJDK** and **Oracle JDK** are both implementations of the Java Development Kit (JDK), but they have some key differences in terms of licensing, support, performance, and features.

**OpenJDK** is an open-source implementation of the Java SE Platform and is licensed under the GNU General Public License (GNU GPL) version 2 with a linking exception. This means it is free to use, modify, and distribute. On the other hand, **Oracle JDK** is licensed under the Oracle Binary Code License Agreement, which requires a paid license for commercial use.

**Oracle JDK** provides long-term support (LTS) for its releases, with new LTS versions released every three years. In contrast, **OpenJDK** releases new versions every six months and only supports changes to a release until the next version is released. This means that Oracle JDK offers more stability and long-term support, while OpenJDK provides more frequent updates and new features.

There is no significant technical difference between the two, as the build process for Oracle JDK is based on OpenJDK. However, Oracle JDK is known for better performance and stability, especially in enterprise environments. Oracle JDK includes features like Flight Recorder, Java Mission Control, and Application Class-Data Sharing, which are not available in OpenJDK. Additionally, Oracle JDK has more garbage collection options and better renderers.

**OpenJDK** is developed by Oracle, Red Hat, and the Java Community, with contributions from companies like Red Hat, Azul Systems, IBM, Apple Inc., and SAP AG. Major Linux distributions like Fedora, Ubuntu, and Red Hat Enterprise Linux provide OpenJDK as the default Java SE implementation. **Oracle JDK**, on the other hand, is fully developed and maintained by Oracle.

See [https://www.baeldung.com/oracle-jdk-vs-openjdk](https://www.baeldung.com/oracle-jdk-vs-openjdk) for more details.

**OpenJDK** 发布地址：[https://openjdk.org/jeps/0](https://openjdk.org/jeps/0)

**OpenJDK** 下载地址：

**Oracle JDK** 发布地址：[https://www.oracle.com/java/technologies/javase/jdk-relnotes-index.html](https://www.oracle.com/java/technologies/javase/jdk-relnotes-index.html)

**Oracle JDK** 下载地址：[https://www.oracle.com/java/technologies/downloads/archive/](https://www.oracle.com/java/technologies/downloads/archive/)

**Oracle Java SE Support Roadmap**: [https://www.oracle.com/java/technologies/java-se-support-roadmap.html](https://www.oracle.com/java/technologies/java-se-support-roadmap.html)



Get started with Java Development Kit(JDK) by the following items:

1. New Features and Enhancements
2. Removed APIs, Features, and Options
3. Deprecated APIs, Features, and Options
4. JVM
5. GC

# JCP

The JCP is the mechanism for developing standard technical specifications for Java technology. For more details, you can refer to the [Java Community Process](https://www.jcp.org/en/home/index).

# JEP

**JEP(JDK Enhancement Proposals)**：**jdk 改进提案**，每当需要有新的
设想时候，JEP 可以在 JCP(java community Process)之前或者同时提
出**非正式的规范(specification)**，被正式认可的 JEP 正式写进JDK 的
发展路线图并分配版本号。

JEPs的目的是引入新的功能、改进现有功能或修复问题，从而提升Java平台的能力和性能。JEPs的提出、审查和接受过程确保了新特性的质量和稳定性‌。

JEPs通过JCP进行定义和管理。JEPs根据其发展阶段进行分类：

- ‌**草案JEPs**‌：正在制定的提案，准备正式提交。
- **提交的JEPs**‌：已经完成创建过程，但尚未被接受。一旦被接受，将进入正在运行的阶段。
- **正在运行的JEPs**‌：目前正在研究的提案，可能包含在未来的Java版本中。
- ‌**交付的功能**‌：已经成功实施的JEP，成为JVM的一部分。
- ‌**撤回的JEPs**‌：未成功而被撤回的提案‌。

有关更多详细信息，请参阅 [https://openjdk.org/jeps/0](https://openjdk.org/jeps/0).

# JSR

**JSR(Java Specification Requests)**：**java 规范提案**，新特性的规范出
现在这一阶段，是指向 JCP(Java Community Process)提出新增一个
**标准化技术规范的正式请求**。请求可以来自于小组/项目、JEP、JCF
成员或者 java 社区(community)成员的提案，每个java 版本都由相
应的 JSR 支持。

# Reference

[https://www.oracle.com/java/](https://www.oracle.com/java/)

[https://www.oracle.com/java/technologies/java-readme.html](https://www.oracle.com/java/technologies/java-readme.html)

See [Trail: Learning the Java Language - Oracle Help Center](https://docs.oracle.com/javase/tutorial/java/) for the fundamentals of programming in the Java programming language.

See [Java Platform, Standard Edition Documentation](https://docs.oracle.com/en/java/javase/index.html) for information about Java SE Versions.

See [Dev.java](https://dev.java/learn/) for updated tutorials taking advantage of the latest releases.

See [JDK Release Notes](https://www.oracle.com/java/technologies/javase/jdk-relnotes-index.html) for information about new features, enhancements, and removed or deprecated options for all JDK releases.

