package cn.maiaimei.java.math;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * 高精度数字需要用BigDecimal，不可用float、double，如金额等
 */
@Slf4j
public class BigDecimalTest {

  @Test
  void testFloat() {
    float a = 0.1f;
    float b = 0.2f;
    float c = a + b;
    log.info("{}+{}={}", a, b, c); // 0.1+0.2=0.3
  }

  @Test
  void testDouble() {
    double a, b;

    a = 0.2;
    b = 0.1;
    log.info("{}+{}={}", a, b, a + b); // 0.1+0.2=0.30000000000000004

    a = 1;
    b = 0.9;
    log.info("{}-{}={}", a, b, a - b); // 1.0-0.9=0.09999999999999998
  }

  /**
   * 不同构造方法有不同的精度
   */
  @Test
  void testNewBigDecimal() {
    log.info("{}", new BigDecimal(1));
    log.info("{}", new BigDecimal(1.0));
    log.info("{}", new BigDecimal(1L));
    log.info("{}", new BigDecimal("1"));
    log.info("{}", new BigDecimal("1.0"));
    log.info("{}", new BigDecimal(BigInteger.ONE));
    log.info("{}", BigDecimal.valueOf(1L));
    log.info("{}", BigDecimal.valueOf(1.0));
  }

  /**
   * 加减乘除
   *
   * @param i i
   * @param j j
   */
  @ParameterizedTest
  @MethodSource("numbersProvider")
  void testCalculation(String i, String j) {
    final BigDecimal a = new BigDecimal(i);
    final BigDecimal b = new BigDecimal(j);
    log.info("{}+{}={}", a, b, a.add(b));
    log.info("{}-{}={}", a, b, a.subtract(b));
    log.info("{}*{}={}", a, b, a.multiply(b));
    log.info("{}/{}={}", a, b, a.divide(b, 2, RoundingMode.CEILING));
    assertTrue(Boolean.TRUE);
  }

  static Stream<Arguments> numbersProvider() {
    return Stream.of(
        Arguments.of("0.1", "0.2"),
        Arguments.of("1.0", "0.9"),
        Arguments.of("1", "0.9"),
        Arguments.of("10", "3")
    );
  }
}
