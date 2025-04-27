package org.example.language;

import org.junit.jupiter.api.Test;

public class PatternMatchingForSwitchTest {

  @Test
  public void test01() {
    Object what = Math.random() < 0.5 ? 42 : "42";
    System.out.println(formatter(what));
    System.out.println(formatterPatternSwitch(what));
  }

  // Prior to Java 17
  @Test
  public void test02() {
    String what = Math.random() < 0.5 ? null : Math.random() < 0.2 ? "Foo" : Math.random() < 0.3 ? "Bar" : "Other";
    if (what == null) {
      System.out.println("oops!");
      return;
    }
    switch (what) {
      case "Foo", "Bar" -> System.out.println("Great");
      default -> System.out.println("Ok");
    }
  }

  // As of Java 17
  @Test
  public void test03() {
    String what = Math.random() < 0.5 ? null : Math.random() < 0.2 ? "Foo" : Math.random() < 0.3 ? "Bar" : "Other";
    switch (what) {
      case null -> System.out.println("Oops");
      case "Foo", "Bar" -> System.out.println("Great");
      default -> System.out.println("Ok");
    }
  }

  // Prior to Java 17
  String formatter(Object o) {
    String formatted = "unknown";
    if (o instanceof Integer i) {
      formatted = String.format("int %d", i);
    } else if (o instanceof Long l) {
      formatted = String.format("long %d", l);
    } else if (o instanceof Double d) {
      formatted = String.format("double %f", d);
    } else if (o instanceof String s) {
      formatted = String.format("String %s", s);
    }
    return formatted;
  }

  // As of Java 17
  String formatterPatternSwitch(Object o) {
    return switch (o) {
      case Integer i -> String.format("int %d", i);
      case Long l -> String.format("long %d", l);
      case Double d -> String.format("double %f", d);
      case String s -> String.format("String %s", s);
      default -> o.toString();
    };
  }

  String formatterPatternSwitch_GuardedAndParenthesizedPatterns(Object obj) {
    return switch (obj) {
      // As of Java 19, Guarded patterns are replaced with when clauses in switch blocks.
      case String s when s.length() > 5 -> "Long String: " + s;
      case String s -> "Short String: " + s;
      case Integer i -> "Integer: " + i;
      case null, default -> "Unknown";
    };
  }

}
