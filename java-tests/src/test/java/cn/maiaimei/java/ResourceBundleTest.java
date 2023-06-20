package cn.maiaimei.java;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 资源文件名称规范：自定义名_语言代码_国别代码.properties
 */
@Slf4j
public class ResourceBundleTest {

  @Test
  public void test_MissingResourceException() {
    // java.util.MissingResourceException: Can't find bundle for base name cn.maiaimei.resources.ValidationMessage, locale zh_CN
    // ValidationMessage.properties 必须放在被标记为Resource Root的文件夹之下
    final ResourceBundle resourceBundle = ResourceBundle.getBundle(
        "cn.maiaimei.resources.ValidationMessage");
  }

  @Test
  public void test_ResourceBundle_getString() {
    final ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationMessage");
    log.info("{}", resourceBundle.getString("validation.Length.message"));
  }

  @Test
  public void test_PropertyResourceBundle_getString() {
    final ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("ValidationMessage");
    log.info("{}", resourceBundle.getString("validation.Length.message"));
  }

  @Test
  public void test_case_01() {
    final ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationMessage");
    log.info("{}",
        MessageFormat.format(resourceBundle.getString("validation.Length.message"), 6, 16));
  }

  @Test
  public void test_case_02() {
    Locale locale = Locale.UK;
    final ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationMessage", locale);
    log.info("{}",
        MessageFormat.format(resourceBundle.getString("validation.Length.message"), 6, 16));
    log.info("{}", resourceBundle.getString("validation.Test.message"));
  }

  @Test
  public void test_Locale() {
    log.info("{}", Locale.getDefault()); // zh_CN
    log.info("{}", Locale.CHINA); // zh_CN
    log.info("{}", Locale.CHINESE); // zh
    log.info("{}", Locale.ENGLISH); // en
    log.info("{}", Locale.US); // en_US
    log.info("{}", Locale.UK); // en_GB
  }
}
