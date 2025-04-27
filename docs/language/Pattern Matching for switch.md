# Pattern Matching for switch

## JEP 406: Pattern Matching for switch (Preview) - JDK 17

Extend `case` labels to include patterns in addition to constants 将 case 标签扩展为除常量外还支持模式匹配

Enables type testing and conditional extraction of values directly within `case` labels. You can use type patterns (e.g., `case String s ->`) to test the type of an object and bind it to a variable if the test succeeds.

```java
String formatterPatternSwitch(Object o) {
    return switch (o) {
      case Integer i -> String.format("int %d", i);
      case Long l -> String.format("long %d", l);
      case Double d -> String.format("double %f", d);
      case String s -> String.format("String %s", s);
      default -> o.toString();
    };
  }
```

There are four major design issues when `case` labels can have patterns:

1. Enhanced type checking
2. Completeness of `switch` expressions and statements
3. Scope of pattern variable declarations
4. Dealing with `null`

## JEP 420: Pattern Matching for switch (Second Preview) - JDK 18

In the [Java 18 preview](https://openjdk.java.net/jeps/420), guards were written as `case String s &&  s.length() > 5 ->`

## JEP 427: Pattern Matching for switch (Third Preview) - JDK 19

Guarded patterns are replaced with `when` clauses in switch blocks.

Allow optional `when` clauses to follow `case` labels.

Allows adding conditions (guards) to patterns using the `when` keyword (e.g., `case String s when s.length() > 5 ->`).

```java
// 之前的语法
Object obj = ...;
switch (obj) {
    case String s && s.length() > 0: // 使用 && 作为守卫条件
        System.out.println(s);
        break;
}

// 现在的语法：使用 when 子句
switch (obj) {
    case String s when s.length() > 0 ->  // 使用 when 作为守卫条件
        System.out.println(s);
}
```

## JEP 433: Pattern Matching for switch (Fourth Preview) - JDK 20

**MatchException 替换异常处理**

An exhaustive switch (i.e., a `switch` expression or a pattern `switch` statement) over an `enum` class now throws `MatchException` rather than `IncompatibleClassChangeError` if no switch label applies at run time.

```java
// 之前：对于枚举类型的穷尽switch，如果没有匹配的标签会抛出 IncompatibleClassChangeError
// 现在：改为抛出 MatchException
enum Color { RED, GREEN, BLUE }

static void testEnum(Color c) {
    switch (c) {  // 穷尽switch
        case RED -> System.out.println("Red");
        case GREEN -> System.out.println("Green");
        case BLUE -> System.out.println("Blue");
    }
    // 如果运行时没有匹配的标签，现在会抛出 MatchException
}
```

**支持泛型 Record 模式的类型参数推断**

Inference of type arguments for generic record patterns is now supported in `switch` expressions and statements, along with the other constructs that support patterns.

```java
// 定义泛型 Record
record MyPair<S,T>(S fst, T snd){}

// 在 switch 中使用泛型 Record 模式
static void recordInference(MyPair<String, Integer> pair) {
    switch (pair) {
        case MyPair(var f, var s) ->  // 自动推断类型：MyPair<String,Integer>
            System.out.println(STR."first=\{f}, second=\{s}");
    }
}
```

```java
record Point(int x, int y) {}
record ColoredPoint(Point point, Color color) {}
enum Color { RED, GREEN, BLUE }

static String describePoint(Object obj) {
    return switch (obj) {
        // 嵌套的 Record 模式匹配
        case Point(int x, int y) when x == 0 && y == 0 -> 
            "Origin";
        case Point(int x, int y) when x == y -> 
            STR."Point on diagonal: (\{x}, \{y})";
        case Point(int x, int y) -> 
            STR."Point: (\{x}, \{y})";
        // 复杂的嵌套模式
        case ColoredPoint(Point(var x, var y), Color c) -> 
            STR."Colored point: (\{x}, \{y}), color: \{c}";
        default -> 
            "Not a point";
    };
}
```

## JEP 441: Pattern Matching for switch - JDK 21

## Reference

[https://javaalmanac.io/features/typepatterns/](https://javaalmanac.io/features/typepatterns/)