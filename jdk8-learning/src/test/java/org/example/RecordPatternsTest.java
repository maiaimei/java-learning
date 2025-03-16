package org.example;

import org.junit.jupiter.api.Test;

public class RecordPatternsTest {

  class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }
  }

  void printSum(Object obj) {
    // As of Java 8
    if (obj instanceof Point) {
      Point p = (Point) obj;
      int x = p.getX();
      int y = p.getY();
      System.out.println(x + y);
    }
  }

  @Test
  public void testPrintSum() {
    printSum(new Point(1, 1));
  }
}
