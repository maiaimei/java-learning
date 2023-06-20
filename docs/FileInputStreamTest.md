```java
@Test
void test_delete_01() {
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
}

@Test
void test_delete_02() {
    String pathname = "C:\\Users\\lenovo\\Desktop\\test.xlsx";
    try (final FileInputStream fis = new FileInputStream(pathname)) {
    } catch (IOException e) {
        log.error(e.getMessage(), e);
    }
    log.info("文件删除{}", new File(pathname).delete() ? "成功" : "失败"); // true
}
```

![](./images/20230617221632.png)

![](./images/20230617222442.PNG)