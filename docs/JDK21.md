# Overview of JDK 21

JDK 21 reached General Availability on 19 September 2023. 

JDK 21 will be a long-term support (LTS) release from most vendors.

Examples of some key product dates for Oracle Java SE product offerings include:

![](./images/Oracle Java SE Support Roadmap.png)

[https://www.oracle.com/java/technologies/java-se-support-roadmap.html](https://www.oracle.com/java/technologies/java-se-support-roadmap.html)

Java moved to a 6-month, time-based release cadence in 2017 with a new feature release of Java becoming available like clockwork every March and September.

![](./images/Comparison of JEPs from JDK8 to JDK21.png)

[https://dev.java/evolution/](https://dev.java/evolution/)

![](./images/JEPs of JDK21.png)

[https://openjdk.org/](https://openjdk.org/)

# Language

## String Templates

Simplify string concatenation and enhance string readability.

A preview language feature and API.

use --enable-preview to enable string templates.

Java 目前支持三种模板处理器：

- STR：自动执行字符串插值，即将模板中的每个嵌入式表达式替换为其值（转换为字符串）。
- FMT：和 STR 类似，但是它还可以接受格式说明符，这些格式说明符出现在嵌入式表达式的左边，用来控制输出的样式。
- RAW：不会像 STR 和 FMT 模板处理器那样自动处理字符串模板，而是返回一个 `StringTemplate` 对象，这个对象包含了模板中的文本和表达式的信息。

除了 JDK 自带的三种模板处理器外，你还可以实现 `StringTemplate.Processor` 接口来创建自己的模板处理器，只需要继承 `StringTemplate.Processor`接口，然后实现 `process` 方法即可。

String Templates were first previewed by [JEP 430](https://openjdk.org/jeps/430) in JDK 21. 

String Templates were second previewed by [JEP 459](https://openjdk.org/jeps/459) in JDK 22 in order to gain additional experience and feedback. Except for a technical change in the types of template expressions, there are no changes relative to the first preview.

String Templates were intended to re-preview again in JDK 23 ([JEP 465](https://openjdk.org/jeps/465)). However, after feedback and extensive discussion, we concluded that the feature is unsuitable in its current form. There is no consensus on what a better design will be; therefore, we have withdrawn the feature for now, and JDK 23 will not include it.

See [March 2024 Archives by thread](https://mail.openjdk.org/pipermail/amber-spec-experts/2024-March/thread.html) and [Update on String Templates (JEP 459)](https://mail.openjdk.org/pipermail/amber-spec-experts/2024-April/004106.html) from the Project Amber amber-spec-experts mailing list for further discussion.

https://openjdk.org/jeps/430

[https://javaalmanac.io/features/stringtemplates/](https://javaalmanac.io/features/stringtemplates/)

[https://docs.oracle.com/en/java/javase/23/language/java-language-changes-release.html#GUID-D05217D4-FCF1-40BA-8628-EA5571F16E38](https://docs.oracle.com/en/java/javase/23/language/java-language-changes-release.html#GUID-D05217D4-FCF1-40BA-8628-EA5571F16E38)

## Record Patterns

https://openjdk.org/jeps/440

Record patterns were proposed as a preview feature by [JEP 405](https://openjdk.org/jeps/405) and delivered in [JDK 19](https://openjdk.org/projects/jdk/19/), and previewed a second time by [JEP 432](https://openjdk.org/jeps/432) and delivered in [JDK 20](https://openjdk.org/projects/jdk/20/). This feature has co-evolved with *Pattern Matching for `switch`* ([JEP 441](https://openjdk.org/jeps/441)), with which it has considerable interaction. This JEP proposes to finalize the feature with further refinements based upon continued experience and feedback.

Apart from some minor editorial changes, the main change since the second preview is to remove support for record patterns appearing in the header of an enhanced `for` statement. This feature may be re-proposed in a future JEP.

# Project Amber in Action

Here is a picture that represents [Project Amber](http://openjdk.org/projects/amber/), the release cadence, and the preview system in action over the last few years. As you can see, many of Amber's language features started off as preview features to collect broader feedback, and sometimes made slight changes before becoming standard.

![](./images/Project Amber in Action.png)

[https://dev.java/evolution/](https://dev.java/evolution/)
