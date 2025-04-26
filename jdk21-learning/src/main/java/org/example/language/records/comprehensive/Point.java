package org.example.language.records.comprehensive;

// As Of Java 14
public record Point(double x, double y) {

  // Static fields
  private static final Point ORIGIN = new Point(0, 0);
  private static final double EPSILON = 0.00001;

  // Static initializer
  static {
    System.out.println("Point class initialized");
  }

  // Static methods
  public static Point origin() {
    return ORIGIN;
  }

  public static Point of(double x, double y) {
    return new Point(x, y);
  }

  // Canonical constructor (compact form with validation)
  public Point {
    if (Double.isNaN(x) || Double.isNaN(y)) {
      throw new IllegalArgumentException("Coordinates cannot be NaN");
    }
  }

  // Custom constructor overloads
  public Point() {
    this(0, 0);
  }

  public Point(Point p) {
    this(p.x, p.y);
  }

  // Instance methods
  public double distance(Point other) {
    double dx = this.x - other.x;
    double dy = this.y - other.y;
    return Math.sqrt(dx * dx + dy * dy);
  }

  public Point add(Point other) {
    return new Point(this.x + other.x, this.y + other.y);
  }

  public Point scale(double factor) {
    return new Point(this.x * factor, this.y * factor);
  }

  public boolean isOrigin() {
    return Math.abs(x) < EPSILON && Math.abs(y) < EPSILON;
  }

  // Override toString with custom format
  @Override
  public String toString() {
    return STR."Point(\{x}, \{y})";
  }

  // Nested enum
  public enum Quadrant {
    FIRST(1), SECOND(2), THIRD(3), FOURTH(4), ORIGIN(0);

    private final int value;

    Quadrant(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  // Nested record
  public record PolarCoordinate(double r, double theta) {

    public Point toCartesian() {
      return new Point(
          r * Math.cos(theta),
          r * Math.sin(theta)
      );
    }
  }

  // Instance method using nested enum
  public Quadrant getQuadrant() {
    if (isOrigin()) {
      return Quadrant.ORIGIN;
    }
    if (x > 0 && y > 0) {
      return Quadrant.FIRST;
    }
    if (x < 0 && y > 0) {
      return Quadrant.SECOND;
    }
    if (x < 0 && y < 0) {
      return Quadrant.THIRD;
    }
    return Quadrant.FOURTH;
  }

  // Static method using nested record
  public static Point fromPolar(double r, double theta) {
    return new PolarCoordinate(r, theta).toCartesian();
  }
}
