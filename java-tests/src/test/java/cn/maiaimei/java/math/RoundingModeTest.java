package cn.maiaimei.java.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Slf4j
public class RoundingModeTest {

  @ParameterizedTest
  @ValueSource(doubles = {-2, -1.9, -1.8, -1.7, -1.6, -1.5, -1.4, -1.3, -1.2, -1.1,
      1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2})
  void test_RoundingMode_UP(double i) {
    final BigDecimal a = new BigDecimal(Double.toString(i));
    final BigDecimal b = BigDecimal.ONE;
    // 向远离零点方向舍入，保留N位小数，N=0
    log.info("{}/{}={}", a, b, a.divide(b, 0, RoundingMode.UP));
  }

  @ParameterizedTest
  @ValueSource(doubles = {-2, -1.9, -1.8, -1.7, -1.6, -1.5, -1.4, -1.3, -1.2, -1.1,
      1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2})
  void test_RoundingMode_DOWN(double i) {
    final BigDecimal a = new BigDecimal(Double.toString(i));
    final BigDecimal b = BigDecimal.ONE;
    // 向零点方向舍入，保留N位小数，N=0
    log.info("{}/{}={}", a, b, a.divide(b, 0, RoundingMode.DOWN));
  }

  @ParameterizedTest
  @ValueSource(doubles = {-2, -1.9, -1.8, -1.7, -1.6, -1.5, -1.4, -1.3, -1.2, -1.1,
      1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2})
  void test_RoundingMode_CEILING(double i) {
    final BigDecimal a = new BigDecimal(Double.toString(i));
    final BigDecimal b = BigDecimal.ONE;
    // 正数向远离零点方向舍入，负数向零点方向舍入，保留N位小数，N=0
    log.info("{}/{}={}", a, b, a.divide(b, 0, RoundingMode.CEILING));
  }

  @ParameterizedTest
  @ValueSource(doubles = {-2, -1.9, -1.8, -1.7, -1.6, -1.5, -1.4, -1.3, -1.2, -1.1,
      1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2})
  void test_RoundingMode_FLOOR(double i) {
    final BigDecimal a = new BigDecimal(Double.toString(i));
    final BigDecimal b = BigDecimal.ONE;
    // 正数向零点方向舍入，负数向远离零点方向舍入，保留N位小数，N=0
    log.info("{}/{}={}", a, b, a.divide(b, 0, RoundingMode.FLOOR));
  }

  @ParameterizedTest
  @ValueSource(doubles = {-2, -1.9, -1.8, -1.7, -1.6, -1.5, -1.4, -1.3, -1.2, -1.1,
      1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2})
  void test_RoundingMode_HALF_UP(double i) {
    final BigDecimal a = new BigDecimal(Double.toString(i));
    final BigDecimal b = BigDecimal.ONE;
    // 四舍五入，保留N位小数，N=0
    // Behaves as for RoundingMode.UP if the discarded fraction is ≥ 0.5; otherwise, behaves as for RoundingMode.DOWN. 
    log.info("{}/{}={}", a, b, a.divide(b, 0, RoundingMode.HALF_UP));
  }

  @ParameterizedTest
  @ValueSource(doubles = {-2, -1.9, -1.8, -1.7, -1.6, -1.5, -1.4, -1.3, -1.2, -1.1,
      1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2})
  void test_RoundingMode_HALF_DOWN(double i) {
    final BigDecimal a = new BigDecimal(Double.toString(i));
    final BigDecimal b = BigDecimal.ONE;
    // Behaves as for RoundingMode.UP if the discarded fraction is > 0.5; otherwise, behaves as for RoundingMode.DOWN.
    log.info("{}/{}={}", a, b, a.divide(b, 0, RoundingMode.HALF_DOWN));
  }

  @ParameterizedTest
  @ValueSource(doubles = {-2, -1.9, -1.8, -1.7, -1.6, -1.5, -1.4, -1.3, -1.2, -1.1,
      1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2})
  void test_RoundingMode_HALF_EVEN(double i) {
    final BigDecimal a = new BigDecimal(Double.toString(i));
    final BigDecimal b = BigDecimal.ONE;
    // Behaves as for RoundingMode.HALF_UP if the digit to the left of the discarded fraction is odd; behaves as for RoundingMode.HALF_DOWN if it's even.
    log.info("{}/{}={}", a, b, a.divide(b, 0, RoundingMode.HALF_EVEN));
  }

}
