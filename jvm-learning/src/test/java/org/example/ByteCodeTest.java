package org.example;

import org.junit.jupiter.api.Test;

public class ByteCodeTest {

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
}
