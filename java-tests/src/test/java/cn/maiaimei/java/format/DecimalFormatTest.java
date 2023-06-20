package cn.maiaimei.java.format;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * <a
 * href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/text/DecimalFormat.html">Class
 * DecimalFormat</a>
 */
@Slf4j
public class DecimalFormatTest {

  @ParameterizedTest
  @ValueSource(doubles = {26574089.726822})
  void testNewNumberFormat(double i) {
    log.info("{} -> {}", i, new DecimalFormat().format(i));
    log.info("{} -> {}", i, NumberFormat.getInstance().format(i));
    log.info("{} -> {}", i, NumberFormat.getInstance(Locale.CHINA).format(i));
    log.info("{} -> {}", i, NumberFormat.getNumberInstance().format(i));
    log.info("{} -> {}", i, NumberFormat.getNumberInstance(Locale.CHINA).format(i));
    log.info("{} -> {}", i, NumberFormat.getCurrencyInstance().format(i));
    log.info("{} -> {}", i, NumberFormat.getCurrencyInstance(Locale.CHINA).format(i));
  }

  @ParameterizedTest
  @ValueSource(doubles = {26574089.726822, 12})
  void test_format(double i) {
    String pattern;
    // pattern = "000,000.00"; // 数字，被格式化数值不够的位数补零，若够则不变
    pattern = "###,###.##"; // 数字，被格式化数值不够的位数忽略，若够则不变
    final DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getInstance();
    // 将给定的模式应用于Format对象
    numberFormat.applyPattern(pattern);
    // 设置舍入模式
    numberFormat.setRoundingMode(RoundingMode.HALF_UP);
    // 设置精确到小数点后2位
    //numberFormat.setMaximumFractionDigits(2);
    log.info("{}", numberFormat.format(i));
  }

  @Test
  void test_pattern() {
    double d = 156.22359646;
    log.info("{}", new DecimalFormat("0").format(d)); // 被格式化的数值位数够，则取所有整数，156
    log.info("{}", new DecimalFormat("#").format(d)); // 被格式化的数值位数够，则取所有整数，156
    log.info("{}", new DecimalFormat("00000.###").format(d)); // 被格式化的数值位数不够，则整数位不够的补零，00156.224
    log.info("{}", new DecimalFormat("#.######\u2030").format(d)); // 以千分比方式计数并且保留6位小数，156223.59646‰
    log.info("{}", new DecimalFormat("#.##%").format(d)); // 以百分比方式计数并且保留2位小数，15622.36%
    long c = 4673568;
    log.info("{}", new DecimalFormat("#.#####E00").format(c)); // 显示为科学计算法，并保留5为小数且被格式化的数值位数不够，不够的补零
    log.info("{}", new DecimalFormat("00.####E0").format(c)); // 显示为科学计数法，并保留2位整数，4位小数
    log.info("{}", new DecimalFormat("####,###").format(c)); // 毎三位用逗号分隔
    log.info("{}", new DecimalFormat("数据分隔后为,##大小").format(c));
  }
}
