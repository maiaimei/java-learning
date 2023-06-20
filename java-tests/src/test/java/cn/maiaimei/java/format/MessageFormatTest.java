package cn.maiaimei.java.format;

import java.text.MessageFormat;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class MessageFormatTest {

  @Test
  public void test() {
    log.info("{}", MessageFormat.format("length must be between {0} and {1}", 6, 16));
  }
}
