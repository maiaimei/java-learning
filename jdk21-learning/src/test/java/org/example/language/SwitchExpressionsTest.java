package org.example.language;

import org.junit.jupiter.api.Test;

public class SwitchExpressionsTest {

  @Test
  public void test01() {
    // Prior to Java 12
    Week day = Week.FRIDAY;
    switch (day) {
      case MONDAY:
      case FRIDAY:
      case SUNDAY:
        System.out.println(6);
        break;
      case TUESDAY:
        System.out.println(7);
        break;
      case THURSDAY:
      case SATURDAY:
        System.out.println(8);
        break;
      case WEDNESDAY:
        System.out.println(9);
        break;
    }
  }

  @Test
  public void test02() {
    // As of Java 12
    Week day = Week.FRIDAY;
    switch (day) {
      case MONDAY, FRIDAY, SUNDAY -> System.out.println(6);
      case TUESDAY -> System.out.println(7);
      case THURSDAY, SATURDAY -> System.out.println(8);
      case WEDNESDAY -> System.out.println(9);
    }
  }

  @Test
  public void test03() {
    // Prior to Java 12
    Week day = Week.FRIDAY;
    int numLetters;
    switch (day) {
      case MONDAY:
      case FRIDAY:
      case SUNDAY:
        numLetters = 6;
        break;
      case TUESDAY:
        numLetters = 7;
        break;
      case THURSDAY:
      case SATURDAY:
        numLetters = 8;
        break;
      case WEDNESDAY:
        numLetters = 9;
        break;
      default:
        throw new IllegalStateException("Wat: " + day);
    }
    System.out.println(numLetters);
  }

  @Test
  public void test04() {
    // As of Java 12
    Week day = Week.FRIDAY;
    int numLetters = switch (day) {
      case MONDAY, FRIDAY, SUNDAY -> 6;
      case TUESDAY -> 7;
      case THURSDAY, SATURDAY -> 8;
      case WEDNESDAY -> 9;
    };
    System.out.println(numLetters);
  }

  @Test
  public void test05() {
    // As of Java 13
    Week day = Week.FRIDAY;
    int dayOfWeek = switch (day) {
      case MONDAY -> 1;
      case TUESDAY -> 2;
      default -> {
        int k = day.toString().length();
        System.out.println("Neither MONDAY nor TUESDAY, hmmm...");
        yield k;
      }
    };
  }

  @Test
  public void test06() {
    // As of Java 13
    Week day = Week.FRIDAY;
    int dayOfWeek = switch (day) {
      case MONDAY:
        yield 1;
      case TUESDAY:
        yield 2;
      default: {
        int k = day.toString().length();
        System.out.println("Neither MONDAY nor TUESDAY, hmmm...");
        yield k;
      }
    };
  }

  enum Week {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
  }
}
