package cn.maiaimei.java8;

import cn.maiaimei.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class StreamAPITest extends BaseTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private List<Person> personList;

  @BeforeEach
  public void setup() throws JsonProcessingException {
    final String fileContent = readFileContent("person-list.json");
    personList = objectMapper.readValue(fileContent, new TypeReference<>() {
    });
  }

  @AfterEach
  public void teardown() {
    personList.clear();
    personList = null;
  }

  /**
   * Collectors.groupingBy用于将流中的元素按照某个键进行分组，生成一个Map，其中键是分组依据，值是分组后的元素列表。
   */
  @Test
  public void testCollectorsGroupingBy() {
    // grouping person list by country
    final Map<String, List<Person>> groupingPersonList01 = personList.stream()
        .collect(Collectors.groupingBy(Person::getCountry));
    info("groupingPersonList01:\n{}", groupingPersonList01);

    // grouping person list by country first, then grouping person list by nationality
    final Map<String, Map<String, List<Person>>> groupingPersonList02 = personList.stream()
        .collect(Collectors.groupingBy(Person::getCountry,
            Collectors.groupingBy(Person::getNationality)));
    info("groupingPersonList02:\n{}", groupingPersonList02);

    // grouping person list by country first, then grouping person list by nationality, and finally grouping person list by sex
    final Map<String, Map<String, Map<String, List<Person>>>> groupingPersonList03 = personList.stream()
        .collect(Collectors.groupingBy(Person::getCountry,
            Collectors.groupingBy(Person::getNationality,
                Collectors.groupingBy(Person::getSex))));
    info("groupingPersonList03:\n{}", groupingPersonList03);
  }

  /**
   * Collectors.toMap用于创建一个1:1映射的Map，当遇到键冲突时，可以使用合并函数来处理。
   * <p>
   * Collectors.toMap是Java8引入的流特性，可以把集合转换为Map集合，key重复会报错，可以使用合并函数来取其默认值，避免报错
   * <p>
   * Collectors.toMap有三个重载方法，参数解释：
   * <p>
   * 1. keyMapper：Key的映射函数，Person::getId表示选择Person的getId作为map的key值。
   * <p>
   * 2. valueMapper：Value的映射函数，Function.identity()表示选择将原来的对象作为Map的value值。
   * <p>
   * 3. mergeFunction：当Key冲突时，调用的合并方法。(p1, p2) -> p2 中，如果p1与p2的key值相同，选择p2作为那个key所对应的value值。
   * <p>
   * 4. mapSupplier：Map构造器，在需要返回特定的Map时使用。第四个参数mapSupplier用于返回一个任意类型的Map实例，比如我们希望返回的Map是根据Key排序的。TreeMap::new
   */
  @Test
  public void testCollectorsToMap() {
    // key不重复场景：id作为map的key，name作为map的value
    final Map<String, String> map01 = personList.stream().collect(Collectors.toMap(Person::getId, Person::getName));
    info("map01:\n{}", map01);

    // key重复场景，country作为map的key，name作为map的value，如果n1与n2的key相同，选择n1 + ", " + n2作为那个key所对应的value值
    final Map<String, String> map02 = personList.stream()
        .collect(Collectors.toMap(Person::getCountry, Person::getName, (n1, n2) -> n1 + ", " + n2));
    info("map02:\n{}", map02);

    // key重复场景，country作为map的key，name作为map的value，如果n1与n2的key相同，选择n1 + ", " + n2作为那个key所对应的value值
    // mapSupplier：Map构造器，在需要返回特定的Map时使用。第四个参数mapSupplier用于返回一个任意类型的Map实例，比如我们希望返回的Map是根据Key排序的。TreeMap::new
    final Map<String, String> map03 = personList.stream()
        .collect(Collectors.toMap(Person::getCountry, Person::getName, (n1, n2) -> n1 + ", " + n2, TreeMap::new));
    info("map03:\n{}", map03);

    // key不重复场景：id作为map的key，Person对象作为map的value
    final Map<String, Person> map04 = personList.stream().collect(Collectors.toMap(Person::getId, Function.identity()));
    final Map<String, Person> map05 = personList.stream().collect(Collectors.toMap(Person::getId, p -> p));
    info("map04:\n{}", map04);
    info("map05:\n{}", map05);

    // key重复场景，country作为map的key，Person对象作为map的value，如果p1与p2的key相同，选择p1作为那个key所对应的value值，即取前一个Person对象作为那个key所对应的value值
    final Map<String, Person> map06 = personList.stream().collect(Collectors.toMap(Person::getCountry, p -> p, (p1, p2) -> p1));
    // key重复场景，country作为map的key，Person对象作为map的value，如果p1与p2的key相同，选择p2作为那个key所对应的value值，即取后一个Person对象作为那个key所对应的value值
    final Map<String, Person> map07 = personList.stream().collect(Collectors.toMap(Person::getCountry, p -> p, (p1, p2) -> p2));
    info("map06:\n{}", map06);
    info("map07:\n{}", map07);
  }

  @Test
  public void groupedAndMapped() {
    // 首先按国家分组，然后在每个国家组内按名字创建映射。
    // 外层Map的键是国家，值是另一个Map
    // 内层Map的键是名字，值是对应的Person对象。
    Map<String, Map<String, Person>> groupedAndMapped = personList.stream()
        .collect(Collectors.groupingBy(Person::getCountry, Collectors.toMap(Person::getName, Function.identity())));
    info("groupedAndMapped:\n{}", groupedAndMapped);
  }

  private void info(String format, Object arg) {
    try {
      log.info(format, objectMapper.writeValueAsString(arg));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Data
  @Builder
  @ToString
  @AllArgsConstructor
  @NoArgsConstructor
  private static class Person {

    private String id;
    private String name;
    private String sex;
    private String country;
    private String nationality;
  }
}
