package org.example;

import static java.lang.StringTemplate.RAW;
import static java.util.FormatProcessor.FMT;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class StringTemplatesTest {

  /**
   * Prior to Java 21, several methods of string concatenation
   */
  @Test
  public void test01() {
    int x = 1;
    int y = 1;
    String s;

    // String concatenation with the + operator produces hard-to-read code
    s = x + " plus " + y + " equals " + (x + y);
    System.out.println(s);

    // StringBuilder is verbose
    s = new StringBuilder()
        .append(x)
        .append(" plus ")
        .append(y)
        .append(" equals ")
        .append(x + y)
        .toString();
    System.out.println(s);

    // String::format and String::formatted separate the format string from the parameters, inviting arity and type mismatches
    s = String.format("%2$d plus %1$d equals %3$d", x, y, x + y);
    System.out.println(s);

    s = "%2$d plus %1$d equals %3$d".formatted(x, y, x + y);
    System.out.println(s);

    // java.text.MessageFormat requires too much ceremony and uses an unfamiliar syntax in the format string
    s = MessageFormat.format("{0} plus {1} equals {2}", x, y, x + y);
    System.out.println(s);
  }

  /**
   * As of Java 21, several methods of string concatenation
   */
  @Test
  public void test02() {
    int x = 1;
    int y = 1;
    String s;

    // The STR template processor
    // It performs string interpolation by replacing each embedded expression in the template with the (stringified) value of
    // that expression.
    // STR is a public static final field that is automatically imported into every Java source file.
    s = STR."\{x} plus \{y} equals \{x + y}";
    System.out.println(s);

    // The FMT template processor
    // FMT is another template processor defined in the Java Platform. FMT is like STR in that it performs interpolation, but it
    // also interprets format specifiers which appear to the left of embedded expressions. The format specifiers are the same as
    // those defined in java.util.Formatter.
    s = FMT."%d\{x} plus %d\{y} equals %d\{x + y}";
    System.out.println(s);

    // The RAW template processor
    // where RAW is a standard template processor that produces an unprocessed StringTemplate object.
    StringTemplate st = RAW."\{x} plus \{y} equals \{x + y}";
    s = STR.process(st);
    System.out.println(s);
  }

  /**
   * Multi-line template expressions
   */
  @Test
  public void test03() {
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

  /**
   * The template processor API
   * <p>
   * Safely composing and executing database queries
   */
  @Test
  public void test04() throws SQLException {
    final Connection conn = Mockito.mock(Connection.class);
    when(conn.prepareStatement(anyString())).thenReturn(Mockito.mock(PreparedStatement.class));
    QueryBuilder DB = new QueryBuilder(conn);

    BigDecimal id = BigDecimal.ONE;
    String username = "username";
    String password = "password";

    PreparedStatement ps1 = DB."SELECT * FROM USER WHERE ID = \{id}";
    ps1.executeQuery();

    PreparedStatement ps2 = DB."SELECT * FROM USER WHERE USERNAME = \{username} AND PASSWORD = \{password}";
    ps2.executeQuery();

    PreparedStatement ps3 = DB."INSERT INTO USER (USERNAME, PASSWORD) VALUES (\{username}, \{password})";
    ps3.executeUpdate();

    PreparedStatement ps4 = DB."UPDATE USER SET PASSWORD = \{password} WHERE ID = \{id}";
    ps4.executeUpdate();

    PreparedStatement ps5 = DB."DELETE FROM USER WHERE ID = \{id}";
    ps5.executeUpdate();
  }

  record QueryBuilder(Connection conn)
      implements StringTemplate.Processor<PreparedStatement, SQLException> {

    public PreparedStatement process(StringTemplate st) throws SQLException {
      // 1. Replace StringTemplate placeholders with PreparedStatement placeholders
      String query = String.join("?", st.fragments());

      // 2. Create the PreparedStatement on the connection
      PreparedStatement ps = conn.prepareStatement(query);

      // 3. Set parameters of the PreparedStatement and build debug info
      int index = 1;
      List<String> params = new ArrayList<>();

      for (Object value : st.values()) {
        // Set parameter
        switch (value) {
          case Integer i -> {
            ps.setInt(index, i);
            params.add("Int: " + i);
          }
          case Float f -> {
            ps.setFloat(index, f);
            params.add("Float: " + f);
          }
          case Double d -> {
            ps.setDouble(index, d);
            params.add("Double: " + d);
          }
          case BigDecimal bd -> {
            ps.setBigDecimal(index, bd);
            params.add("BigDecimal: " + bd);
          }
          case Boolean b -> {
            ps.setBoolean(index, b);
            params.add("Boolean: " + b);
          }
          default -> {
            ps.setString(index, String.valueOf(value));
            params.add("String: '" + value + "'");
          }
        }
        index++;
      }

      // Print SQL and parameters
      System.out.println("===> Executing SQL: " + query);
      System.out.println("===> Parameters: " + String.join(", ", params));

      // Optional: Print actual SQL with replaced parameters
      String actualSql = query;
      for (String param : params) {
        actualSql = actualSql.replaceFirst("\\?", param.substring(param.indexOf(":") + 1).trim());
      }
      System.out.println("<=== Actual SQL: " + actualSql);

      return ps;
    }
  }

}
