module org.example.mainappmodule {
  // Basic dependency
  requires org.example.daoimplmodule;
  requires org.slf4j;
  // Transitive dependency
  requires transitive org.junit.jupiter;
  // Static dependency
  requires static lombok;
  // Static transitive dependency
  //requires static transitive org.mockito;

  exports org.example to org.junit.platform.commons; // 解决异常 TestEngine with ID 'junit-jupiter' failed to discover tests
}