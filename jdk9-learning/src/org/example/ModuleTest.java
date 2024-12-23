package org.example;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.SampleDomain;
import org.junit.jupiter.api.Test;

@Slf4j
public class ModuleTest {

  @Test
  public void test_requires() {
    final SampleDomain sampleDomain = new SampleDomain();
    sampleDomain.setId(BigDecimal.ONE);
    sampleDomain.setName(BigDecimal.ONE.toPlainString());
    log.info("{}", sampleDomain);
  }
}
