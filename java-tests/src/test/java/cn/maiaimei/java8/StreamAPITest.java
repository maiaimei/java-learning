package cn.maiaimei.java8;

import cn.maiaimei.BaseTest;
import cn.maiaimei.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 在Java 8中引入了Stream API，它提供了一种新的方式来处理集合对象。
 * <p>
 * Stream API允许以声明性方式处理数据，通过一系列中间操作构建流水线，最后以一个终止操作来执行。
 * <p>
 * Stream API执行过程：创建流->经历一系列中间操作->以一个终止操作结束流
 */
@Slf4j
public class StreamAPITest extends BaseTest {

  @Test
  public void test_flatMap() {
    List<List<String>> nestedList = Lists.newArrayList();
    nestedList.add(Lists.newArrayList("1", "2", "3"));
    nestedList.add(Lists.newArrayList("4", "5", "6"));
    final List<String> list = nestedList.stream().flatMap(Collection::stream).collect(Collectors.toList());
    logInfo("{}\n", nestedList);
    logInfo("{}\n", list);
  }

  /**
   * 过滤集合数据（filter）
   */
  @Test
  public void test_filter() {
    List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
    // 获取长度大于2的字符串
    List<String> filtered = strings.stream()
        .filter(s -> s.length() > 2)
        .collect(Collectors.toList());
    logInfo("{}\n", filtered);
  }

  /**
   * 转换集合元素（map）
   */
  @Test
  public void test_map() {
    List<String> strings = Arrays.asList("1", "2", "3", "4", "5");
    // 将字符串转换为整数
    List<Integer> converted = strings.stream()
        .map(Integer::parseInt)
        .collect(Collectors.toList());
    logInfo("{}\n", converted);
  }

  /**
   * 排序集合元素
   */
  @Test
  public void test_sorted() {
    List<String> strings = Arrays.asList("abc", "abcd", "def", "jkl");
    // 按字符串长度排序
    List<String> sorted = strings.stream()
        .sorted(Comparator.comparingInt(String::length))
        .collect(Collectors.toList());
    logInfo("{}\n", sorted);
  }

  @Test
  public void test_reduce() {
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    // 计算集合元素的和
    int sum = numbers.stream()
        .reduce(0, Integer::sum);
    logInfo("sum: {}\n", sum);
    // 计算集合元素的差
    int initialValue = 0;
    int difference = numbers.stream()
        .reduce(initialValue, (a, b) -> a - b);
    logInfo("difference: {}\n", difference);
  }

  /**
   * 查找集合元素
   */
  @Test
  public void test_findFirst_findAny() {
    List<String> strings = Arrays.asList("abc", "abcd", "def", "jkl");
    // 查找第一个长度大于3的字符串
    Optional<String> findFirst = strings.stream()
        .filter(s -> s.length() > 3)
        .findFirst();
    logInfo("{}\n", findFirst.orElse(""));
    // 查找任意一个长度大于3的字符串
    Optional<String> findAny = strings.stream()
        .filter(s -> s.length() > 3)
        .findAny();
    logInfo("{}\n", findAny.orElse(""));
  }

  /**
   * Collectors.groupingBy用于将流中的元素按照某个键进行分组，生成一个Map，其中键是分组依据，值是分组后的元素列表。
   */
  @Test
  public void test_collect_groupingBy() {
    final List<Person> personList = getPersonList();

    // grouping person list by country
    final Map<String, List<Person>> groupingPersonList01 = personList.stream()
        .collect(Collectors.groupingBy(Person::getCountry));
    logInfo("groupingPersonList01:\n{}", groupingPersonList01);

    // grouping person list by country first, then grouping person list by nationality
    final Map<String, Map<String, List<Person>>> groupingPersonList02 = personList.stream()
        .collect(Collectors.groupingBy(Person::getCountry,
            Collectors.groupingBy(Person::getNationality)));
    logInfo("groupingPersonList02:\n{}", groupingPersonList02);

    // grouping person list by country first, then grouping person list by nationality, and finally grouping person list by sex
    final Map<String, Map<String, Map<String, List<Person>>>> groupingPersonList03 = personList.stream()
        .collect(Collectors.groupingBy(Person::getCountry,
            Collectors.groupingBy(Person::getNationality,
                Collectors.groupingBy(Person::getSex))));
    logInfo("groupingPersonList03:\n{}", groupingPersonList03);
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
  public void test_collect_toMap() {
    final List<Person> personList = getPersonList();

    // key不重复场景：id作为map的key，name作为map的value
    final Map<String, String> map01 = personList.stream().collect(Collectors.toMap(Person::getId, Person::getName));
    logInfo("map01:\n{}", map01);

    // key重复场景，country作为map的key，name作为map的value，如果n1与n2的key相同，选择n1 + ", " + n2作为那个key所对应的value值
    final Map<String, String> map02 = personList.stream()
        .collect(Collectors.toMap(Person::getCountry, Person::getName, (n1, n2) -> n1 + ", " + n2));
    logInfo("map02:\n{}", map02);

    // key重复场景，country作为map的key，name作为map的value，如果n1与n2的key相同，选择n1 + ", " + n2作为那个key所对应的value值
    // mapSupplier：Map构造器，在需要返回特定的Map时使用。第四个参数mapSupplier用于返回一个任意类型的Map实例，比如我们希望返回的Map是根据Key排序的。TreeMap::new
    final Map<String, String> map03 = personList.stream()
        .collect(Collectors.toMap(Person::getCountry, Person::getName, (n1, n2) -> n1 + ", " + n2, TreeMap::new));
    logInfo("map03:\n{}", map03);

    // key不重复场景：id作为map的key，Person对象作为map的value
    final Map<String, Person> map04 = personList.stream().collect(Collectors.toMap(Person::getId, Function.identity()));
    final Map<String, Person> map05 = personList.stream().collect(Collectors.toMap(Person::getId, p -> p));
    logInfo("map04:\n{}", map04);
    logInfo("map05:\n{}", map05);

    // key重复场景，country作为map的key，Person对象作为map的value，如果p1与p2的key相同，选择p1作为那个key所对应的value值，即取前一个Person对象作为那个key所对应的value值
    final Map<String, Person> map06 = personList.stream().collect(Collectors.toMap(Person::getCountry, p -> p, (p1, p2) -> p1));
    // key重复场景，country作为map的key，Person对象作为map的value，如果p1与p2的key相同，选择p2作为那个key所对应的value值，即取后一个Person对象作为那个key所对应的value值
    final Map<String, Person> map07 = personList.stream().collect(Collectors.toMap(Person::getCountry, p -> p, (p1, p2) -> p2));
    logInfo("map06:\n{}", map06);
    logInfo("map07:\n{}", map07);
  }

  @Test
  public void test_collect_groupingBy_toMap() {
    final List<Person> personList = getPersonList();
    // 首先按国家分组，然后在每个国家组内按名字创建映射。
    // 外层Map的键是国家，值是另一个Map
    // 内层Map的键是名字，值是对应的Person对象。
    Map<String, Map<String, Person>> groupedAndMapped = personList.stream()
        .collect(Collectors.groupingBy(Person::getCountry, Collectors.toMap(Person::getName, Function.identity())));
    logInfo("groupedAndMapped:\n{}", groupedAndMapped);
  }

  private List<Person> getPersonList() {
    final String fileContent = readFileContent("person-list.json");
    try {
      return objectMapper.readValue(fileContent, new TypeReference<List<Person>>() {
      });
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
