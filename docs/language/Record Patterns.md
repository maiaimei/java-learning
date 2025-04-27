# Record Patterns

## JEP 405: Record Patterns (Preview) - JDK 19

Direct record deconstruction

```java
record Point(int x, int y) {}

// Old style
if (o instanceof Point p) {
    int x = p.x();
    int y = p.y();
    System.out.println(x + y);
}

// New style with Record Pattern
if (o instanceof Point(int x, int y)) {
    System.out.println(x + y);
}
```

Support for nested patterns

```java
record Point(int x, int y) {}
enum Color { RED, GREEN, BLUE }
record ColoredPoint(Point p, Color c) {}
record Rectangle(ColoredPoint upperLeft, ColoredPoint lowerRight) {}

// Single-level decomposition
static void printUpperLeftColoredPoint(Rectangle r) {
    if (r instanceof Rectangle(ColoredPoint ul, ColoredPoint lr)) {
        System.out.println(ul.c());
    }
}

// Multi-level nested decomposition
static void printColorOfUpperLeftPoint(Rectangle r) {
    if (r instanceof Rectangle(
            ColoredPoint(Point p, Color c),
            ColoredPoint lr)) {
        System.out.println(c);
    }
}

// Deep nested patterns
static void printXCoordOfUpperLeftPointWithPatterns(Rectangle r) {
    if (r instanceof Rectangle(
            ColoredPoint(Point(var x, var y), var c),
            var lr)) {
        System.out.println("Upper-left corner: " + x);
    }
}
```

## JEP 432: Record Patterns (Second Preview) - JDK 20

Enhanced for Loop Support

```java
record Point(int x, int y) {}

static void dump(Point[] pointArray) {
    for (Point(var x, var y) : pointArray) {        // Record Pattern in header!
        System.out.println("(" + x + ", " + y + ")");
    }
}
```

## JEP 440: Record Patterns - JDK 21

Record patterns were proposed as a preview feature by [JEP 405](https://openjdk.org/jeps/405) and delivered in [JDK 19](https://openjdk.org/projects/jdk/19/), and previewed a second time by [JEP 432](https://openjdk.org/jeps/432) and delivered in [JDK 20](https://openjdk.org/projects/jdk/20/). This feature has co-evolved with *Pattern Matching for `switch`* ([JEP 441](https://openjdk.org/jeps/441)), with which it has considerable interaction. This JEP proposes to finalize the feature with further refinements based upon continued experience and feedback.

Apart from some minor editorial changes, the main change since the second preview is to remove support for record patterns appearing in the header of an enhanced `for` statement. This feature may be re-proposed in a future JEP.

## Reference

[https://javaalmanac.io/features/recordpatterns/](https://javaalmanac.io/features/recordpatterns/)