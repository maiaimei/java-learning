module org.example.daoimplmodule {
  requires transitive org.example.entitymodule;
  requires transitive org.example.daomodule;

  exports org.example.dao.impl;
}