package cn.maiaimei;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
public abstract class BaseTest {

  protected final ObjectMapper objectMapper = new ObjectMapper();

  protected String readFileContent(String pathname) {
    URL url = this.getClass().getClassLoader().getResource(pathname);
    if (url != null) {
      File file = new File(url.getFile());
      try {
        return FileUtils.readFileToString(file, Charset.defaultCharset());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  protected void logInfo(String format, Object value) {
    try {
      log.info(format, objectMapper.writeValueAsString(value));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
