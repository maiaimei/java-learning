module org.example.mainappmodule {
  requires transitive org.junit.jupiter;
  requires static lombok;
  requires org.slf4j;
  requires org.example.daoimplmodule;
  requires org.example.entitymodule;
  requires org.example.daomodule;
  exports org.example to org.junit.platform.commons;
}