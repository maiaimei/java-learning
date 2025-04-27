package org.example.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class PatternMatchingForSwitchTest {

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

  @Test
  public void test01() {
    Object what = Math.random() < 0.5 ? 42 : "42";
    System.out.println(formatter(what));
    System.out.println(formatterPatternSwitch(what));
  }

  // Prior to Java 17
  @Test
  public void test02() {
    List<String> randomStrings = new ArrayList<>();
    randomStrings.add(null);
    randomStrings.add("Foo");
    randomStrings.add("Bar");
    randomStrings.add("Other");
    String what = randomStrings.get(new Random().nextInt(randomStrings.size()));
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
    List<String> randomStrings = new ArrayList<>();
    randomStrings.add(null);
    randomStrings.add("Foo");
    randomStrings.add("Bar");
    randomStrings.add("Other");
    String what = randomStrings.get(new Random().nextInt(randomStrings.size()));
    switch (what) {
      case null -> System.out.println("Oops");
      case "Foo", "Bar" -> System.out.println("Great");
      default -> System.out.println("Ok");
    }
  }

  @Test
  public void test04() {
    List<Object> randomObjects = new ArrayList<>();
    randomObjects.add(null);
    randomObjects.add("Foo");
    randomObjects.add("Bar");
    randomObjects.add("Other");
    randomObjects.add(UUID.randomUUID().toString());
    randomObjects.add(Integer.valueOf("1"));
    randomObjects.add(Long.valueOf("1"));
    randomObjects.add(Double.valueOf("1"));
    randomObjects.add(new Point(1, 1));
    Object what = randomObjects.get(new Random().nextInt(randomObjects.size()));
    Object result = switch (what) {
      // As of Java 19, Guarded patterns are replaced with when clauses in switch blocks.
      case String s when s.length() > 5 -> "Long String: " + s;
      case String s -> "Short String: " + s;
      case Integer i -> "Integer: " + i;
      case Long l -> "Long: " + l;
      case Double d -> "Double: " + d;
      case Point(int x, int y) -> String.format("Point: %s,%s", x, y);
      case null, default -> "Unknown";
    };
    System.out.println(result);
  }

  record Point(int x, int y) {

  }

}
