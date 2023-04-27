package cn.maiaimei.java.collection;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;

/**
 * HashSet：底层使用HashMap，数组+链表+红黑树
 */
@SuppressWarnings("all")
public class HashSetTest {
    @Test
    void testAdd01(){
        final HashSet hashSet = new HashSet();
        for (int i = 0; i <= 100; i++) {
            hashSet.add(i);
        }
        System.out.println(hashSet);
    }
    
    @Test
    void testAdd02(){
        final HashSet hashSet = new HashSet();
        for (int i = 0; i <= 12; i++) {
            hashSet.add(new A(i));
        }
        System.out.println(hashSet);
    }
    
    class A{
        private Integer n;
        
        public A(Integer n){
            this.n = n;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            A a = (A) o;
            return Objects.equals(n, a.n);
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }
}
