package org.example.module.c.demo;

import org.example.module.a.demo.HelloModuleA;
import org.example.module.b.demo.HelloModuleB;

public class HelloModuleC {

  public static void main(String[] args) {
    HelloModuleA helloModuleA = new HelloModuleA();
    HelloModuleB helloModuleB = new HelloModuleB();
    HelloModuleC helloModuleC = new HelloModuleC();
    helloModuleA.say();
    helloModuleB.say();
    helloModuleC.say();
  }

  public void say() {
    System.out.println("Hello, Module C");
  }

}
