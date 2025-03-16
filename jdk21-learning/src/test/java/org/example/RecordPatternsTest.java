package org.example;

import org.junit.jupiter.api.Test;

public class RecordPatternsTest {

  record Point(int x, int y) {

  }

  void printSum(Object o) {
    // As of Java 21
    if (o instanceof Point(int x, int y)) {
      System.out.println(x + y);
    }
  }

  @Test
  public void testPrintSum() {
    printSum(new Point(1, 1));
  }

}