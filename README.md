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

# OpenJDK

[OpenJDK](https://openjdk.org/) has several implementations, each maintained or supported by diferent organizations.

Each of these implementations adheres to the OpenJDK specifications but may include additional optimizations, support, and certifications provided by the respective organizations.

The support dates for different versions of JDK provided by various organizations can vary.

| Implementations                                              | Maintained by                                                | Description                                                  | Support Policy                                               | Downloads                                                    |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| [Oracle JDK](https://www.oracle.com/java/)                   | 由Oracle公司主导。                                           | The reference implementation of the Java Platform, Standard Edition, provided by Oracle.<br />Oracle提供的Java平台标准版的参考实现。 | Provides upates for each release for 6 months with LTS releases receiving extended support through Oracle's commercial offerings.<br />[Oracle Java SE Support Roadmap](https://www.oracle.com/java/technologies/java-se-support-roadmap.html) | [Java Archive](https://www.oracle.com/java/technologies/downloads/archive/) |
| [AdoptOpenJDK](https://adoptium.net)                         | AdoptOpenJDK community (now part of the Eclipse Foundation as Adoptium)<br />由AdoptOpenJDK社区主导，现在由Eclipse基金会管理，称为Eclipse Adoptium。 | Provides prebuilt OpenJDK binaries for various platforms.<br />为各种平台提供预构建的OpenJDK二进制文件。 | Typically provides updates for each LTS (Long Term Support) release for at least 4 years after the initial release.<br />[Adoptium Support](https://adoptium.net/support.htm) | [https://adoptopenjdk.net/releases.html](https://adoptopenjdk.net/releases.html)<br />[https://adoptopenjdk.net/archive.html](https://adoptopenjdk.net/archive.html) |
| [Amazon Corretto](https://aws.amazon.com/corretto)           | 由Amazon主导。                                               | A no-cost,multiplatform, production-ready distribution of OpenJDK<br />OpenJDK的免费、多平台、生产就绪发行版 | Provides long-term suport for LTS releases, with updates for at least 8 years after the initial release.<br />[Amazon Corretto Support](https://aws.amazon.com/corretto/faqs/#support) | [https://aws.amazon.com/corretto/](https://aws.amazon.com/corretto/) |
| [Azul Zulu](https://www.azul.com/products/core/openjdk-terms-of-use/) | 由Azul Systems主导。                                         | A certified build of OpenJDK that is available for a wide range of platforms and configurations.<br />OpenJDK的认证版本，可用于各种平台和配置。 | Offers commercial support for LTS releases for at least 10 years after the initial releases.<br />[Azul Supportl](https://www.azul.com/products/azul-support-roadmap/) | [Download Azul JDKs](https://www.azul.com/downloads/#zulu)   |
| [IBM Semeru Runtime](https://www.ibm.com/support/pages/semeru-runtimes-getting-started/) | 由IBM主导。                                                  | IBM's distribution of OpenJDK,optimized for performance and reliability.<br />IBM的OpenJDK发行版，针对性能和可靠性进行了优化。 | Offers long-term support for LTS releases, with updates for at least 6 years after the initial release.<br />[IBM Semeru Runtimes support](https://www.ibm.com/support/pages/semeru-runtimes-support) | [IBM Semeru Runtimes Downloads](https://developer.ibm.com/languages/java/semeru-runtimes/downloads/) |
| [Red Hat OpenJDK](https://developers.redhat.com/products/openjdk/getting-started) | 由Red Hat主导。                                              | Red Hat's distribution of OpenJDK,which is used in Red Hat Enterprise Linux and other Red Hat products.<br />Red Hat的OpenJDK发行版，用于Red Hat Enterprise Linux和其他Red Hat产品。 | Provides long-term support for LTS releases, with updates for at least 6 years after the initial release.<br />[Red Hat OpenJDK Support](https://access.redhat.com/articles/1299013) | [Download the Red Hat Build of OpenJDK](https://developers.redhat.com/products/openjdk/download) |

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

# Project Amber

The goal of Project Amber is to explore and incubate smaller, productivity-oriented Java language features that have been accepted as candidate JEPs in the [OpenJDK JEP Process](https://openjdk.org/jeps/1). This Project is sponsored by the [Compiler Group](https://openjdk.org/groups/compiler).

Most Project Amber features go through at least two rounds of [*preview*](https://openjdk.org/jeps/12) before becoming an official part of the Java Platform. For a given feature, there are separate JEPs for each round of preview and for final standardization.

[https://openjdk.org/projects/amber/](https://openjdk.org/projects/amber/)

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

[https://openjdk.org/](https://openjdk.org/)

[https://javaalmanac.io/](https://javaalmanac.io/)

[https://javaalmanac.io/jdk/](https://javaalmanac.io/jdk/)

[https://javaalmanac.io/features/](https://javaalmanac.io/features/)

[https://dev.java/learn/](https://dev.java/learn/)

[https://docs.oracle.com/en/java/javase/](https://docs.oracle.com/en/java/javase/)

[https://docs.oracle.com/javase/tutorial/getStarted/intro/index.html](https://docs.oracle.com/javase/tutorial/getStarted/intro/index.html)

[https://docs.oracle.com/javase/specs/](https://docs.oracle.com/javase/specs/)

[https://advancedweb.hu/a-categorized-list-of-all-java-and-jvm-features-since-jdk-8-to-21/](https://advancedweb.hu/a-categorized-list-of-all-java-and-jvm-features-since-jdk-8-to-21/)

[https://jeps.dev/blog](https://jeps.dev/blog)

[https://javaguide.cn/home.html](https://javaguide.cn/home.html)

