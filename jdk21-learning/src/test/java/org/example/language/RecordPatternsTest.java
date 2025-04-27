package org.example.language;

import org.junit.jupiter.api.Test;

public class RecordPatternsTest {

  @Test
  public void test01() {
    Object what = Math.random() < 0.5 ? new Point(1, 1) : "new Point(1, 1)";

    // Prior to Java 19
    if (what instanceof Point p) {
      int x = p.x();
      int y = p.y();
      System.out.println(x + y);
    }
  }

  @Test
  public void test02() {
    Object what = Math.random() < 0.5 ? new Point(1, 1) : "new Point(1, 1)";

    // As of Java 19, Point(int x, int y) is a record pattern.
    if (what instanceof Point(int x, int y)) {
      System.out.println(x + y);
    }
  }

  record Point(int x, int y) {

  }

}