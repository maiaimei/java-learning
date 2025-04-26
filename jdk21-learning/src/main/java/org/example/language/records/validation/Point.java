package org.example.language.records.validation;

// As Of Java 14
public record Point(int x, int y) {

  public Point {
    if (x < 0) {
      throw new IllegalArgumentException("x must be grater or equals 0");
    }
    if (y < 0) {
      throw new IllegalArgumentException("y must be grater or equals 0");
    }
  }
}