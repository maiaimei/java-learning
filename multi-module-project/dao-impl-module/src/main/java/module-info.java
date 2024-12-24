module org.example.daoimplmodule {
  requires org.example.entitymodule;
  requires org.example.daomodule;

  exports org.example.dao.impl;
}