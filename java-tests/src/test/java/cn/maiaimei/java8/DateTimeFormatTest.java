package cn.maiaimei.java8;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import org.junit.jupiter.api.Test;

public class DateTimeFormatTest {

  @Test
  void test1() throws ParseException {
    String format = "MM/dd/yyyy";
    String date = "5/3/1969";
    // Sat May 03 00:00:00 CST 1969
    System.out.println(new SimpleDateFormat(format).parse(date));
    // java.time.format.DateTimeParseException: Text '5/3/1969' could not be parsed at index 0
    System.out.println(LocalDate.parse(date, DateTimeFormatter.ofPattern(format)));
  }

  /**
   * <p>关于java：DateTimeFormatter对一个月的月份和一年的月份的支持: https://www.codenong.com/27571377/</p>
   * <p>DateTimeFormatter支持单位数字的月份日期和年份月份: https://segmentfault.com/q/1010000042903251</p>
   */
  @Test
  void test2() {
    // single digit is supported for the month and date fields.
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .appendOptional(DateTimeFormatter.ofPattern("M/d/yyyy"))
        .appendOptional(DateTimeFormatter.ofPattern("M/dd/yyyy"))
        .appendOptional(DateTimeFormatter.ofPattern("MM/d/yyyy"))
        .appendOptional(DateTimeFormatter.ofPattern(("MM/dd/yyyy")))
        .toFormatter();

    System.out.println(LocalDate.parse("1/2/2023", formatter));   // 2023-01-02
    System.out.println(LocalDate.parse("1/12/2023", formatter));  // 2023-01-12
    System.out.println(LocalDate.parse("11/2/2023", formatter));  // 2023-11-02
    System.out.println(LocalDate.parse("11/12/2023", formatter)); // 2023-11-12
  }
}
