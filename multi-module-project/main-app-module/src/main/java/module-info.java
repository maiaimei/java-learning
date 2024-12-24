module org.example.mainappmodule {
  requires static lombok;
  requires org.slf4j;
  requires org.example.entitymodule;
  requires org.example.daomodule;
  requires org.example.daoimplmodule;
}