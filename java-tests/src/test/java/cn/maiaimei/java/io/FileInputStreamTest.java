package cn.maiaimei.java.io;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
public class FileInputStreamTest {

  private List<String> pathnames;

  @AfterEach
  public void tearDown() {
    for (String pathname : pathnames) {
      new File(pathname).deleteOnExit();
    }
  }

  @Test
  public void test_delete_01() {
    String pathname = "C:\\Users\\lenovo\\Desktop\\test.xlsx";
    try {
      final FileInputStream fis = new FileInputStream(pathname);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    log.info("文件删除{}", new File(pathname).delete() ? "成功" : "失败"); // false
    try {
      FileUtils.forceDelete(new File(pathname));
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    pathnames = Lists.newArrayList(pathname);
  }

  @Disabled
  @Test
  public void test_delete_02() {
    String pathname = "C:\\Users\\lenovo\\Desktop\\test.xlsx";
    try (final FileInputStream fis = new FileInputStream(pathname)) {
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    log.info("文件删除{}", new File(pathname).delete() ? "成功" : "失败"); // true
  }

  @Disabled
  @Test
  public void test02() {
    String pathname = "C:\\Users\\lenovo\\Desktop\\TODO.txt";
    // try-with-resources
    try (final FileInputStream fis = new FileInputStream(pathname)) {
      // 不太适合大文件，因为byte数组不能太大
      byte[] bytes = new byte[fis.available()];
      fis.read(bytes);
      System.out.println(new String(bytes));
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  @Disabled
  @Test
  public void test03() {
    String pathname = "C:\\Users\\lenovo\\Desktop\\TODO.txt";
    try (final FileInputStream fis = new FileInputStream(pathname)) {
      byte[] bytes = new byte[fis.available()];
      IOUtils.read(fis, bytes);
      System.out.println(new String(bytes));
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }
}
