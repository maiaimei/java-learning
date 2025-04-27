# Pattern Matching for instanceof

## JEP 305: Pattern Matching for instanceof (Preview) - JDK 14

A *pattern* is a combination of (1) a *predicate* that can be applied to a target, and (2) a set of *binding variables* that are extracted from the target only if the predicate successfully applies to it.

A *type test pattern* consists of a predicate that specifies a type, along with a single binding variable.

```java
if (obj instanceof String s) {
    // can use s here
} else {
    // can't use s here
}
```

The `instanceof` operator "matches" the target `obj` to the type test pattern as follows: if `obj` is an instance of `String`, then it is cast to `String` and assigned to the binding variable `s`. The binding variable is in scope in the true block of the `if` statement, and not in the false block of the `if` statement.

## JEP 375: Pattern Matching for instanceof (Second Preview) - JDK 15

Pattern matching for `instanceof` was proposed by [JEP 305](https://openjdk.java.net/jeps/305) in mid 2017, and targeted to JDK 14 in late 2019 as a [preview language feature](https://openjdk.java.net/jeps/12). This JEP proposes to re-preview the feature in JDK 15, with no changes relative to the preview in JDK 14, in order to gather additional feedback.

## JEP 394: Pattern Matching for instanceof - JDK 16

Pattern matching for `instanceof` was proposed by [JEP 305](https://openjdk.java.net/jeps/305) and delivered in [JDK 14](https://openjdk.java.net/projects/jdk/14) as a [preview feature](https://openjdk.java.net/jeps/12). It was re-proposed by [JEP 375](https://openjdk.java.net/jeps/375) and delivered in [JDK 15](https://openjdk.java.net/projects/jdk/15) for a second round of preview.

This JEP proposes to finalize the feature in JDK 16, with the following refinements:

- Lift the restriction that pattern variables are implicitly final, to reduce asymmetries between local variables and pattern variables. 取消模式变量隐式为final的限制，以减少局部变量与模式变量之间的不对称性。
- Make it a compile-time error for a pattern `instanceof` expression to compare an expression of type *S* against a pattern of type *T*, where *S* is a subtype of *T*. (This `instanceof` expression will always succeed and is then pointless. The opposite case, where a pattern match will always fail, is already a compile-time error.) 将模式匹配中的instanceof表达式对类型为S的表达式与类型为T的模式进行比较时，若S是T的子类型，应视为编译时错误。（这种情况下模式匹配总会成功，因此该比较是无意义的。反之，当模式匹配必定失败的情况则已存在编译时错误。）-- 没理解

Other refinements may be incorporated based on further feedback.

## Reference

[https://javaalmanac.io/features/instanceof-patterns/](https://javaalmanac.io/features/instanceof-patterns/)