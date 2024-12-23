package org.example.domain;

import java.math.BigDecimal;

public class SampleDomain {

  private BigDecimal id;
  private String name;

  public SampleDomain() {
  }

  public BigDecimal getId() {
    return id;
  }

  public void setId(BigDecimal id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "SampleDomain{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
