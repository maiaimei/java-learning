package org.example;

import org.junit.jupiter.api.Test;

/**
 * In programming, ++i and i++ are both used to increment the value of a variable by 1, but they do so in different ways.
 * <p>
 * These operators are known as pre-increment and post-increment operators, respectively.
 * <p>
 * Performance Comparison
 * <p>
 * The pre-increment operator (++i) is generally faster than the post-increment operator (i++).
 * <p>
 * This is because the post-increment operator requires an additional step to create a temporary copy of the variable's previous
 * state before incrementing it.
 * <p>
 * The steps involved are as follows:
 * <p>
 * Pre-Increment (++i)
 * <p>
 * 1. Get the value of the variable in memory.
 * <p>
 * 2. Add 1 to the value.
 * <p>
 * 3. Store the new value back to memory.
 * <p>
 * Post-Increment (i++)
 * <p>
 * 1. Get the value of the variable in memory.
 * <p>
 * 2. Make a temporary copy of the variable to retrieve the previous state.
 * <p>
 * 3. Add 1 to the value.
 * <p>
 * 4. Store the new value back to memory.
 * <p>
 * Due to the extra step in post-increment, pre-increment is faster. This difference in speed may be negligible for integer
 * operations but can be significant for pointer increments
 */
public class IncrementOperatorTest {

  /**
   * Pre-Increment Operator (++i)
   * <p>
   * The pre-increment operator increments the value of the variable by 1 before its current value is used in any operation.
   * <p>
   * For example: In this case, the value of i is incremented first, and then the new value is assigned to a.
   */
  @Test
  public void testPreIncrementOperator() {
    int i = 3;
    int a = ++i; // a = 4, i = 4
    System.out.println("a = " + a);
    System.out.println("i = " + i);
  }

  /**
   * Post-Increment Operator (i++)
   * <p>
   * The post-increment operator increments the value of the variable by 1 after its current value is used in an expression.
   * <p>
   * For example: Here, the current value of i is assigned to a first, and then i is incremented.
   */
  @Test
  public void testPostIncrementOperator() {
    int i = 3;
    int a = i++; // a = 3, i = 4
    System.out.println("a = " + a);
    System.out.println("i = " + i);
  }

  @Test
  public void testIncrement() {
    int i = 0;
    System.out.println("Post-Increment");
    System.out.println(i++); // Output: 0
    int j = 0;
    System.out.println("Pre-Increment");
    System.out.println(++j); // Output: 1
  }

  @Test
  public void test01() {
    int i = 1;
    i++;
    System.out.println(i); // 2
  }

  @Test
  public void test02() {
    int i = 1;
    ++i;
    System.out.println(i); // 2
  }

  @Test
  public void test03() {
    int i = 1;
    i = i++;
    System.out.println(i); // 1
  }

  @Test
  public void test04() {
    int i = 1;
    i = ++i;
    System.out.println(i); // 2
  }

  /**
   * This bytecode sequence performs the following operations:
   * <p>
   * 0 iconst_2 Pushes the integer 2 onto the stack.
   * <p>
   * 1 istore_1 Store the integer value from the top of the operand stack into local variable 1.
   * <p>
   * 2 iload_1 Load the integer value from local variable 1 onto the operand stack.
   * <p>
   * 3 iload_1 Load the integer value from local variable 1 onto the operand stack again.
   * <p>
   * 4 iinc 1 by 1 Increments the value of local variable 1 by 1 (so it becomes 3).
   * <p>
   * 7 imul Multiply the top two integer values on the operand stack and push the result back onto the operand stack.
   * <p>
   * 8 istore_1 Store the integer value from the top of the operand stack into local variable 1.
   * <p>
   * 9 getstatic #2 <java/lang/System.out : Ljava/io/PrintStream;> Retrieves the System.out PrintStream object.
   * <p>
   * 12 iload_1 Load the integer value from local variable 1 (which is 4)  onto the operand stack.
   * <p>
   * 13 invokevirtual #3 <java/io/PrintStream.println : (I)V> Calls System.out.println to print the value 4.
   * <p>
   * 16 return Returns from the method.
   */
  @Test
  public void test05() {
    int i = 2;
    i *= i++; // i = i * i++
    System.out.println(i); // 4
  }
}
