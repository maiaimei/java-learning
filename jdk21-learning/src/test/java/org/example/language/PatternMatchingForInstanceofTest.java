package org.example.language;

import org.junit.jupiter.api.Test;

public class PatternMatchingForInstanceofTest {

  @Test
  public void test01() {
    Object what = Math.random() < 0.5 ? 42 : "42";

    // Prior to Java 14
    if (what instanceof Integer) {
      Integer i = (Integer) what;
      System.out.println("Integer with value " + i.intValue());
    } else if (what instanceof String) {
      String s = (String) what;
      System.out.println("String of length " + s.length());
    }

    // As of Java 14
    if (what instanceof Integer i) {
      System.out.println("Integer with value " + i.intValue());
    } else if (what instanceof String s) {
      System.out.println("String of length " + s.length());
    }
  }

  @Test
  public void test02() {
    Object what = Math.random() < 0.5 ? Math.random() < 0.5 ? 42 : -42 : "1729";
    if (!(what instanceof Integer i) || i.intValue() <= 0) {
      System.out.println("No good: " + what + " is not an integer or not positive");
    } else {
      System.out.println("Hooray: a positive integer with value " + i.intValue());
    }
  }

  @Test
  public void test03() {
    Object what = Math.random() < 0.5 ? 42 : "1729";
    System.out.println(what instanceof Integer i
        ? "Hooray: " + i.intValue()
        : "No good: " + what);
  }

  @Test
  public void test04() {
    Double x = Math.PI;
    if (x instanceof Number n) { // Error
      System.out.println(n.longValue());
    }
    if (x instanceof Number) { // Ok
      System.out.println(x.longValue());
    }
  }

}
