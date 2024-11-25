package cn.maiaimei.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class StreamAPITest {

  List<Person> personList;

  @BeforeEach
  public void setup() {
    personList = new ArrayList<>();
    personList.add(Person.builder().id(UUID.randomUUID().toString()).name("Tom").sex("Male").country("UK").build());
    personList.add(Person.builder().id(UUID.randomUUID().toString()).name("Amy").sex("Female").country("CN").build());
    personList.add(Person.builder().id(UUID.randomUUID().toString()).name("Kate").sex("Female").country("US").build());
    personList.add(Person.builder().id(UUID.randomUUID().toString()).name("Jane").sex("Female").country("CN").build());
    personList.add(Person.builder().id(UUID.randomUUID().toString()).name("Charlie").sex("Male").country("US").build());
    personList.add(Person.builder().id(UUID.randomUUID().toString()).name("David").sex("Male").country("UK").build());
    personList.add(Person.builder().id(UUID.randomUUID().toString()).name("Bob").sex("Male").country("CN").build());
    personList.add(Person.builder().id(UUID.randomUUID().toString()).name("Alice").sex("Male").country("US").build());
  }

  @AfterEach
  public void teardown() {
    personList.clear();
    personList = null;
  }

  /**
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
    log.info("map01:\n{}", map01);

    // key重复场景，country作为map的key，name作为map的value，如果n1与n2的key相同，选择n1 + ", " + n2作为那个key所对应的value值
    final Map<String, String> map02 = personList.stream()
        .collect(Collectors.toMap(Person::getCountry, Person::getName, (n1, n2) -> n1 + ", " + n2));
    log.info("map02:\n{}", map02);

    // key重复场景，country作为map的key，name作为map的value，如果n1与n2的key相同，选择n1 + ", " + n2作为那个key所对应的value值
    // mapSupplier：Map构造器，在需要返回特定的Map时使用。第四个参数mapSupplier用于返回一个任意类型的Map实例，比如我们希望返回的Map是根据Key排序的。TreeMap::new
    final Map<String, String> map03 = personList.stream()
        .collect(Collectors.toMap(Person::getCountry, Person::getName, (n1, n2) -> n1 + ", " + n2, TreeMap::new));
    log.info("map03:\n{}", map03);

    // key不重复场景：id作为map的key，Person对象作为map的value
    final Map<String, Person> map04 = personList.stream().collect(Collectors.toMap(Person::getId, Function.identity()));
    final Map<String, Person> map05 = personList.stream().collect(Collectors.toMap(Person::getId, p -> p));
    log.info("map04:\n{}", map04);
    log.info("map05:\n{}", map05);

    // key重复场景，country作为map的key，Person对象作为map的value，如果p1与p2的key相同，选择p1作为那个key所对应的value值，即取前一个Person对象作为那个key所对应的value值
    final Map<String, Person> map06 = personList.stream().collect(Collectors.toMap(Person::getCountry, p -> p, (p1, p2) -> p1));
    // key重复场景，country作为map的key，Person对象作为map的value，如果p1与p2的key相同，选择p2作为那个key所对应的value值，即取后一个Person对象作为那个key所对应的value值
    final Map<String, Person> map07 = personList.stream().collect(Collectors.toMap(Person::getCountry, p -> p, (p1, p2) -> p2));
    log.info("map06:\n{}", map06);
    log.info("map07:\n{}", map07);
  }

  @Data
  @Builder
  @ToString
  public static class Person {

    private String id;
    private String name;
    private String sex;
    private String country;
  }
}
