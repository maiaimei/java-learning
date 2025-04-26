# Records

Records were intended to provide a concise way to define immutable data carriers.

## JEP 359: Records (Preview) - JDK 14

Records were proposed by [JEP 359](https://openjdk.java.net/jeps/359) and delivered in [JDK 14](https://openjdk.java.net/projects/jdk/14) as a preview language feature.

Consider your typical `Point` class that represents a point on a plane, with an x and a y coordinate.

Prior to Java 14:

```java
public class Point {

    int x;
    int y;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }
}
```

As Of Java 14:

```java
public record Point(int x, int y) {

}
```

In IDEA, compile Point.java, select Point.class, View -> Show Bytecode With Jclasslib

![](./images/Records-20250426-155801.png)

Record Characteristics:

1. Records are implicitly final, cannot be abstract, and cannot extend any other class (the superclass of a record is always `java.lang.Record`)
2. Can implement interfaces
3. Automatically generate public constructor methods that accept all declared fields as parameters
4. Constructor may be declared without formal parameter list (compact constructor) for validation and normalization
5. Each declared field corresponds to private final member, with getter method of same name and type
6. Automatically generated `equals()`, `hashCode()`, and `toString()`
7. Cannot explicitly declare instance fields or instance initializers
8. Can declare static fields, static initializers, static methods, constructors, instance methods, and nested types

## JEP 384: Records (Second Preview) - JDK 15

Records were refined by [JEP 384](https://openjdk.java.net/jeps/384) and delivered in [JDK 15](https://openjdk.java.net/projects/jdk/15) as a preview feature for a second time.

Records work well with *sealed types* ([JEP 360](https://openjdk.java.net/jeps/360)).

```java
public sealed interface Expr 
    permits ConstantExpr, PlusExpr, TimesExpr, NegExpr {...}

public record ConstantExpr(int i)       implements Expr {...}
public record PlusExpr(Expr a, Expr b)  implements Expr {...}
public record TimesExpr(Expr a, Expr b) implements Expr {...}
public record NegExpr(Expr e)           implements Expr {...}
```

Records can declare inside a method, it is local records.

```java
List<Merchant> findTopMerchants(List<Merchant> merchants, int month) {
    // Local record
    record MerchantSales(Merchant merchant, double sales) {}

    return merchants.stream()
        .map(merchant -> new MerchantSales(merchant, computeSales(merchant, month)))
        .sorted((m1, m2) -> Double.compare(m2.sales(), m1.sales()))
        .map(MerchantSales::merchant)
        .collect(toList());
}
```

## JEP 395: Records - JDK 16

Records were finalized by [JEP 395](https://openjdk.java.net/jeps/395) and delivered in [JDK 16](https://openjdk.java.net/projects/jdk/16) as an official feature.

Relax the longstanding restriction whereby an inner class cannot declare a member that is explicitly or implicitly static. This will become legal and, in particular, will allow an inner class to declare a member that is a record class.

```java
public class OuterClass {
    private int outerField = 1;

    // 在 Java 16 之前：内部类不能有静态成员
    class BeforeJava16Inner {
        // 编译错误：内部类不能有静态字段
        // static int staticField = 1;
        
        // 编译错误：内部类不能有静态方法
        // static void staticMethod() {}
        
        // 编译错误：内部类不能声明静态类
        // static class StaticNestedClass {}
        
        // 编译错误：内部类不能声明 Record
        // record InnerRecord(int x, int y) {}
    }

    // 在 Java 16 之后：内部类可以有静态成员
    class AfterJava16Inner {
        // 1. 可以声明静态字段
        static int staticField = 1;
        
        // 2. 可以声明静态方法
        static void staticMethod() {
            System.out.println("Static method in inner class");
        }
        
        // 3. 可以声明静态类
        static class StaticNestedClass {
            int value;
        }
        
        // 4. 可以声明 Record（Record 隐式为 static）
        record Point(int x, int y) {
            // Record 可以有自己的方法
            public double distance(Point other) {
                int dx = this.x - other.x;
                int dy = this.y - other.y;
                return Math.sqrt(dx * dx + dy * dy);
            }
        }

        // 5. 可以声明静态初始化块
        static {
            System.out.println("Static initializer in inner class");
        }
        
        // 实例方法可以访问外部类的实例成员
        void instanceMethod() {
            System.out.println(outerField);
        }
    }
}
```

## Reference

[https://javaalmanac.io/features/records/](https://javaalmanac.io/features/records/)