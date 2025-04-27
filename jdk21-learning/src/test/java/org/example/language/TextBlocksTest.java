package org.example.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TextBlocksTest {

  @Test
  public void htmlExample() {
    // Prior to Java 13
    String htmlPriorToJava13 = "<html>\n" +
        "    <body>\n" +
        "        <p>Hello, world</p>\n" +
        "    </body>\n" +
        "</html>\n";

    // As of Java 13
    String htmlAsOfJava13 = """
        <html>
            <body>
                <p>Hello, world</p>
            </body>
        </html>
        """;
    assertEquals(htmlPriorToJava13, htmlAsOfJava13);
  }

  @Test
  public void sqlExample() {
    // Prior to Java 13
    String queryPriorToJava13 = "SELECT `EMP_ID`, `LAST_NAME` FROM `EMPLOYEE_TB`\n" +
        "WHERE `CITY` = 'INDIANAPOLIS'\n" +
        "ORDER BY `EMP_ID`, `LAST_NAME`;\n";

    // As of Java 13
    String queryAsOfJava13 = """
        SELECT `EMP_ID`, `LAST_NAME` FROM `EMPLOYEE_TB`
        WHERE `CITY` = 'INDIANAPOLIS'
        ORDER BY `EMP_ID`, `LAST_NAME`;
        """;
    assertEquals(queryPriorToJava13, queryAsOfJava13);
  }

  /**
   * As of Java 13
   * <p>
   * The opening delimiter is a sequence of three double quote characters (""") followed by zero or more white spaces followed by a
   * line terminator. The content begins at the first character after the line terminator of the opening delimiter.
   * <p>
   * The closing delimiter is a sequence of three double quote characters. The content ends at the last character before the first
   * double quote of the closing delimiter.
   * <p>
   * The content may include double quote characters directly, unlike the characters in a string literal. The use of \" in a text
   * block is permitted, but not necessary or recommended. Fat delimiters (""") were chosen so that " characters could appear
   * unescaped, and also to visually distinguish a text block from a string literal.
   * <p>
   * The content may include line terminators directly, unlike the characters in a string literal. The use of \n in a text block is
   * permitted, but not necessary or recommended.
   */
  @Test
  public void basicSyntax() {
    // double quote characters(""") followed by zero white space followed by a line terminator
    String html01 = """
        <html>
            <body>
                <p>Hello, world</p>
            </body>
        </html>
        """;
    System.out.println("The length of html01: " + html01.length()); // 66

    // double quote characters(""") followed by three white spaces followed by a line terminator
    String html02 = """   
        <html>
            <body>
                <p>Hello, world</p>
            </body>
        </html>
        """;
    System.out.println("The length of html02: " + html02.length()); // 66

    // The closing delimiter is a sequence of three double quote characters. The content ends at the last character before the
    // first double quote of the closing delimiter.
    String html03 = """   
        <html>
            <body>
                <p>Hello, world</p>
            </body>
        </html>""";
    System.out.println("The length of html03: " + html03.length()); // 65

    // The content may include double quote characters directly, unlike the characters in a string literal. The use of \" in a
    // text block is permitted, but not necessary or recommended. Fat delimiters (""") were chosen so that " characters could
    // appear unescaped, and also to visually distinguish a text block from a string literal.
    String html04 = """   
        <html>
            <body>
                <p id=\"hw\">Hello, world</p>
            </body>
        </html>
        """;
    String html05 = """   
        <html>
            <body>
                <p id="hw">Hello, world</p>
            </body>
        </html>
        """;
    assertEquals(html04, html05);

    // The content may include line terminators directly, unlike the characters in a string literal. The use of \n in a text
    // block is permitted, but not necessary or recommended.
    String html06 = """   
        <html>\n    <body>\n        <p>Hello, world</p>\n    </body>\n</html>
        """;
    String html07 = """   
        <html>
            <body>
                <p>Hello, world</p>
            </body>
        </html>
        """;
    assertEquals(html06, html07);
  }

  /**
   * As of Java 14
   */
  @Test
  public void newEscapeSequences_newlines() {
    String literal = "Lorem ipsum dolor sit amet, consectetur adipiscing " +
        "elit, sed do eiusmod tempor incididunt ut labore " +
        "et dolore magna aliqua.";
    String text = """
        Lorem ipsum dolor sit amet, consectetur adipiscing \
        elit, sed do eiusmod tempor incididunt ut labore \
        et dolore magna aliqua.\
        """;
    System.out.println(literal);
    System.out.println(text);
    assertEquals(literal, text);
  }

  /**
   * As of Java 14
   * <p>
   * Escape sequences aren't translated until after incident space stripping, so `\s` can act as fence to prevent the stripping of
   * trailing white space. Using `\s` at the end of each line.
   * <p>
   * 转义序列在后续空格剥离处理之前不会进行转换，因此\s可作为屏障来防止尾部空白被移除。
   */
  @Test
  public void newEscapeSequences_whitespace() {
    String colorsWithoutWhitespaceExcapeSequences = """
        red  
        green
        blue 
        """;
    String colorsWithWhitespaceExcapeSequences = """
        red  \s
        green\s
        blue \s
        """;

    String colorsWithWhitespaceExcapeSequencesInTraditionalString = "red  \s\ngreen\s\nblue \s\n";

    System.out.println(
        "The length of colorsWithoutWhitespaceExcapeSequences: " + colorsWithoutWhitespaceExcapeSequences.length()); // 15
    System.out.println(colorsWithoutWhitespaceExcapeSequences);

    System.out.println("The length of colorsWithWhitespaceExcapeSequences: " + colorsWithWhitespaceExcapeSequences.length()); // 21
    System.out.println(colorsWithWhitespaceExcapeSequences);

    System.out.println("The length of colorsWithWhitespaceExcapeSequencesInTraditionalString: "
        + colorsWithWhitespaceExcapeSequencesInTraditionalString.length()); // 21
    System.out.println(colorsWithWhitespaceExcapeSequencesInTraditionalString);
  }


}
