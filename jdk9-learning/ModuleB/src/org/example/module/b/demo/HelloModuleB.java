package org.example.module.b.demo;

import org.example.module.a.demo.HelloModuleA;

public class HelloModuleB {

  public static void main(String[] args) {
    HelloModuleA helloModuleA = new HelloModuleA();
    HelloModuleB helloModuleB = new HelloModuleB();
    helloModuleA.say();
    helloModuleB.say();
  }

  public void say() {
    System.out.println("Hello, Module B");
  }

}
