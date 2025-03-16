# Apache Maven Compiler Plugin

[https://maven.apache.org/plugins/maven-compiler-plugin/index.html](https://maven.apache.org/plugins/maven-compiler-plugin/index.html)

The compiler plugin is used to compile the source code of a Maven project. This plugin has two goals, which are already bound to specific phases of the default lifecycle:

- *compile* **–** compile main source files
- *testCompile* **–** compile test source files

Here’s the *compiler* plugin in the POM:

```xml
<plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.12.1</version>
    <configuration>
        ...
    </configuration>
</plugin>
```

We can find the latest version of this plugin [here](https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-compiler-plugin).

By default, the compiler plugin compiles source code compatible with Java 5, and the generated classes also work with Java 5 regardless of the JDK in use. We can modify these settings in the configuration element:

```xml
<properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
</properties>

<plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.12.1</version>
    <configuration>
        <source>${maven.compiler.source}</source>
    	<target>${maven.compiler.target}</target>
    </configuration>
</plugin>
```

Sometimes we want to pass arguments to the [*javac*](https://www.baeldung.com/javac) compiler. This is where the *compilerArgs* parameter comes in handy.

For instance, we can specify the following configuration for the compiler to warn about unchecked operations:

```xml
<properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
</properties>

<plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.12.1</version>
    <configuration>
        <source>${maven.compiler.source}</source>
    	<target>${maven.compiler.target}</target>
        <compilerArgs>
        	<arg>-Xlint:unchecked</arg>
    	</compilerArgs>
    </configuration>
</plugin>
```

As both goals of the *compiler* plugin are automatically bound to phases in the Maven default lifecycle, we can execute these goals with the commands *mvn compile* and *mvn test-compile*.

Starting with JDK 9, the `javac` executable can accept the `--release` option to specify against which Java SE release you want to build the project. For example, you have JDK 11 installed and used by Maven, but you want to build the project against Java 8. The `--release` option ensures that the code is compiled following the rules of the programming language of the specified release, and that generated classes target the release as well as the public API of that release. This means that, unlike the old [`-source` and `-target` options](https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-source-and-target.html), the compiler will detect and generate an error when using APIs that don't exist in previous releases of Java SE.

refer to [https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-release.html](https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-release.html)