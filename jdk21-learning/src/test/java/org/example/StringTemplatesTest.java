package org.example;

import static java.lang.StringTemplate.RAW;
import static java.util.FormatProcessor.FMT;

import java.text.MessageFormat;
import org.junit.jupiter.api.Test;

public class StringTemplatesTest {

  @Test
  public void testStringConcatenation() {
    int x = 1;
    int y = 1;

    // String concatenation with the + operator produces hard-to-read code
    String s1 = x + " plus " + y + " equals " + (x + y);
    System.out.println(s1);

    // StringBuilder is verbose
    String s2 = new StringBuilder()
        .append(x)
        .append(" plus ")
        .append(y)
        .append(" equals ")
        .append(x + y)
        .toString();
    System.out.println(s2);

    // String::format and String::formatted separate the format string from the parameters, inviting arity and type mismatches
    String s3 = String.format("%2$d plus %1$d equals %3$d", x, y, x + y);
    String s4 = "%2$d plus %1$d equals %3$d".formatted(x, y, x + y);
    System.out.println(s3);
    System.out.println(s4);

    // java.text.MessageFormat requires too much ceremony and uses an unfamiliar syntax in the format string
    String s5 = MessageFormat.format("{0} plus {1} equals {2}", x, y, x + y);
    System.out.println(s5);

    // The STR template processor
    String s6 = STR."\{x} plus \{y} equals \{x + y}";
    System.out.println(s6);

    // The FMT template processor
    String s7 = FMT."%d\{x} plus %d\{y} equals %d\{x + y}";
    System.out.println(s7);

    // The RAW template processor
    StringTemplate st = RAW."\{x} plus \{y} equals \{x + y}";
    String s8 = STR.process(st);
    System.out.println(s8);
  }

  @Test
  public void testMultiLineStringConcatenation() {
    String title = "My Web Page";
    String text = "Hello, world";
    String html = STR."""
        <html>
          <head>
            <title>\{title}</title>
          </head>
          <body>
            <p>\{text}</p>
          </body>
        </html>
        """;
    System.out.println(html);
  }

}
