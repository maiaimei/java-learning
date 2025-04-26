# Switch Expressions

## JEP 325: Switch Expressions (Preview) - JDK 12

Switch Expressions were proposed by [JEP 325](https://openjdk.java.net/jeps/325) and delivered in [JDK 12](https://openjdk.java.net/projects/jdk/12) as a preview language feature.

Use of `case` with `->` (arrow syntax) for concise code.

No need for `break` statements to prevent fall-through.

Multiple case labels can be combined using commas (e.g., `case 1, 2, 3 ->`).

```java
// Prior to Java 12
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

// As of Java 12
switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> System.out.println(6);
    case TUESDAY                -> System.out.println(7);
    case THURSDAY, SATURDAY     -> System.out.println(8);
    case WEDNESDAY              -> System.out.println(9);
}
```

`switch` can now be used as an expression, returning a value, or as a traditional statement.

```java
// Prior to Java 12
T result = switch (arg) {
    case L1 -> e1;
    case L2 -> e2;
    default -> e3;
};
```

## JEP 354: Switch Expressions (Second Preview) - JDK 13

Switch Expressions were refined by [JEP 354](https://openjdk.java.net/jeps/354) and delivered in [JDK 13](https://openjdk.java.net/projects/jdk/13) as a preview feature for a second time.

In switch expressions, `yield` is used to return a value from a case block.

```java
// As of Java 13
int j = switch (day) {
    case MONDAY  -> 0;
    case TUESDAY -> 1;
    default      -> {
        int k = day.toString().length();
        int result = f(k);
        yield result;
    }
};

// As of Java 13
int result = switch (s) {
    case "Foo": 
        yield 1;
    case "Bar":
        yield 2;
    default:
        System.out.println("Neither Foo nor Bar, hmmm...");
        yield 0;
};
```

The compiler ensures all possible values are handled, especially with `enum` types.

A `default` case is required unless all possible inputs are covered.

## JEP 361: Switch Expressions - JDK 14

Switch Expressions were finalized by [JEP 361](https://openjdk.java.net/jeps/361) and delivered in [JDK 14](https://openjdk.java.net/projects/jdk/14) as an official feature  with no further changes.

## Reference

[https://javaalmanac.io/features/switch/](https://javaalmanac.io/features/switch/)