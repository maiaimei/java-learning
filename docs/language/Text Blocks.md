# Text Blocks

Text blocks were proposed by [JEP 355](https://openjdk.java.net/jeps/355) in early 2019 as a follow-on to explorations begun in [JEP 326](https://openjdk.java.net/jeps/326) (Raw String Literals), which was initially targeted to JDK 12 but eventually [withdrawn and did not appear in that release](https://mail.openjdk.java.net/pipermail/jdk-dev/2018-December/002402.html). JEP 355 was [targeted to JDK 13 in June 2019](https://mail.openjdk.java.net/pipermail/jdk-dev/2019-June/003050.html) as a [preview feature](http://openjdk.java.net/jeps/12). Feedback on JDK 13 suggested that text blocks should be previewed again in JDK 14, with the addition of [two new escape sequences](https://openjdk.org/jeps/378#New-escape-sequences). Consequently, [JEP 368](https://openjdk.java.net/jeps/368) was [targeted to JDK 14 in November 2019](https://mail.openjdk.java.net/pipermail/jdk-dev/2019-November/003653.html) as a preview feature. Feedback on JDK 14 suggested that text blocks were ready to become final and permanent in JDK 15 with no further changes.

[https://openjdk.org/projects/jdk/13/](https://openjdk.org/projects/jdk/13/)

[https://openjdk.org/projects/jdk/14/](https://openjdk.org/projects/jdk/14/)

[https://openjdk.org/projects/jdk/15/](https://openjdk.org/projects/jdk/15/)

## JEP 355: Text Blocks (Preview) - JDK 13

A text block is a multi-line string literal, consists of zero or more content characters, enclosed by opening and closing delimiters.

The *opening delimiter* is a sequence of three double quote characters (`"""`) followed by zero or more white spaces followed by a line terminator. The *content* begins at the first character after the line terminator of the opening delimiter.

The *closing delimiter* is a sequence of three double quote characters. The content ends at the last character before the first double quote of the closing delimiter.

The content may include double quote characters directly, unlike the characters in a string literal. The use of `\"` in a text block is permitted, but not necessary or recommended. Fat delimiters (`"""`) were chosen so that `"` characters could appear unescaped, and also to visually distinguish a text block from a string literal.

The content may include line terminators directly, unlike the characters in a string literal. The use of `\n` in a text block is permitted, but not necessary or recommended. 

```java
// Prior to Java 13

String html = "<html>\n" +
              "    <body>\n" +
              "        <p>Hello, world</p>\n" +
              "    </body>\n" +
              "</html>\n";

// As of Java 13
String html = """
              <html>
                  <body>
                      <p>Hello, world</p>
                  </body>
              </html>
              """;
```

## JEP 368: Text Blocks (Second Preview) - JDK 14

To allow finer control of the processing of newlines and white space, we introduce two new escape sequences.

First, the `\<line-terminator>` escape sequence explicitly suppresses the insertion of a newline character.

```java
String literal = "Lorem ipsum dolor sit amet, consectetur adipiscing " +
                 "elit, sed do eiusmod tempor incididunt ut labore " +
                 "et dolore magna aliqua.";

String text = """
                Lorem ipsum dolor sit amet, consectetur adipiscing \
                elit, sed do eiusmod tempor incididunt ut labore \
                et dolore magna aliqua.\
                """;
                    
assertEquals(literal, text); // true
```

Second, the new `\s` escape sequence simply translates to a single space (`\u0020`).

Escape sequences aren't translated until after incident space stripping, so `\s` can act as fence to prevent the stripping of trailing white space. Using `\s` at the end of each line.

转义序列在后续空格剥离处理之前不会进行转换，因此\s可作为屏障来防止尾部空白被移除。

```java
String colors = """
    red  \s
    green\s
    blue \s
    """;
```

The `\s` escape sequence can be used in both text blocks and traditional string literals.

## JEP 378: Text Blocks - JDK 15