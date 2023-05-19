package cn.maiaimei.example.oom;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 堆溢出-java.lang.OutOfMemoryError: Java heap space
 * 堆溢出异常。堆内对象不能进行回收了，堆内存持续增大，这样达到了堆内存的最大值。
 * java -Xms5m -Xmx5m -jar jvm-tuning-1.0-SNAPSHOT.jar
 */
@SuppressWarnings("all")
public class HeapOOM {
    
    public static void main(String[] args) throws InterruptedException {
        List<Person> list = new ArrayList<>();

        for (int i = 100; ; i++) {
            TimeUnit.NANOSECONDS.sleep(1);
            final Person person = new Person(System.currentTimeMillis(), "HeapOOM-TEST-".concat(String.valueOf(i)), LocalDateTime.now());
            list.add(person);
        }
    }

    private static class Person {
        private Long id;
        private String name;
        private LocalDateTime birthday;

        public Person(Long id, String name, LocalDateTime birthday) {
            this.id = id;
            this.name = name;
            this.birthday = birthday;
        }
    }
    
}
