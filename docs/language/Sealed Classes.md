# Sealed Classes

Sealed classes and interfaces restrict which other classes or interfaces may extend or implement them. 

## JEP 360: Sealed Classes (Preview) - JDK 15

Sealed Classes were proposed by [JEP 360](https://openjdk.java.net/jeps/360) and delivered in [JDK 15](https://openjdk.java.net/projects/jdk/15) as a preview language feature.

A class is sealed by applying the `sealed` modifier to its declaration. Then, after any `extends` and `implements` clauses, the `permits` clause specifies the classes that are permitted to extend the sealed class.

```java
public abstract sealed class Shape
    permits Circle, Rectangle, Square {...}
```

The classes specified by `permits` must be located near the superclass: either in the same module (if the superclass is in a named module) or in the same package (if the superclass is in the unnamed module).

A sealed class imposes three constraints on its permitted subclasses (the classes specified by its `permits` clause):

1. The sealed class and its permitted subclasses must belong to the same module, and, if declared in an unnamed module, the same package.
2. Every permitted subclass must directly extend the sealed class.
3. Every permitted subclass must choose a modifier to describe how it continues the sealing initiated by its superclass:
   * A permitted subclass may be declared `final` to prevent its part of the class hierarchy from being extended further.
   * A permitted subclass may be declared `sealed` to allow its part of the hierarchy to be extended further than envisaged by its sealed superclass, but in a restricted fashion.
   * A permitted subclass may be declared `non-sealed` so that its part of the hierarchy reverts to being open for extension by unknown subclasses. (A sealed class cannot prevent its permitted subclasses from doing this.)

One and only one of the modifiers `final`, `sealed`, and `non-sealed` must be used by each permitted subclass.

Similar to the story for classes, an interface is sealed by applying the `sealed` modifier to the interface. After any `extends` clause to specify superinterfaces, the implementing classes and subinterfaces are specified with a `permits` clause.

```java
public sealed interface Expr
    permits ConstantExpr, PlusExpr, TimesExpr, NegExpr {...}

public final class ConstantExpr implements Expr {...}
public final class PlusExpr     implements Expr {...}
public final class TimesExpr    implements Expr {...}
public final class NegExpr      implements Expr {...}
```

Sealed classes work well with records ([JEP 384](https://openjdk.java.net/jeps/384)), another preview feature of Java 15. Records are implicitly `final`, so a sealed hierarchy with records is slightly more concise than the example above:

```java
public sealed interface Expr
    permits ConstantExpr, PlusExpr, TimesExpr, NegExpr {...}

public record ConstantExpr(int i)       implements Expr {...}
public record PlusExpr(Expr a, Expr b)  implements Expr {...}
public record TimesExpr(Expr a, Expr b) implements Expr {...}
public record NegExpr(Expr e)           implements Expr {...}
```

## JEP 397: Sealed Classes (Second Preview) - JDK 16

Sealed Classes were re-preview by [JEP 397](https://openjdk.java.net/jeps/397) and delivered in [JDK 16](https://openjdk.java.net/projects/jdk/16)

A significant benefit of sealed classes will be realized in a future release in conjunction with [pattern matching](https://cr.openjdk.java.net/~briangoetz/amber/pattern-match.html). Instead of inspecting an instance of a sealed class with `if`-`else` chains, user code will be able to use a `switch` enhanced with *type test patterns*. This will allow the Java compiler to check that the patterns are exhaustive.

## JEP 409: Sealed Classes - JDK 17

Sealed Classes were finalized by [JEP 409](https://openjdk.java.net/jeps/409) and delivered in [JDK 17](https://openjdk.java.net/projects/jdk/17) as an official feature, with no changes from JDK 16.

## Reference

[https://javaalmanac.io/features/sealedtypes/](https://javaalmanac.io/features/sealedtypes/)