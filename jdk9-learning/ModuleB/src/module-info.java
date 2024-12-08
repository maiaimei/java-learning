module ModuleB {
  // 模块ModuleB 依赖于 模块ModuleA，任何依赖于 模块ModuleB 的模块将隐式地依赖于 模块ModuleA
  requires transitive ModuleA;

  exports org.example.module.b.demo;
}