package cn.maiaimei.java.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;

/**
 * HashSet：底层使用HashMap，HashMap底层使用数组+链表+红黑树
 */
@SuppressWarnings("all")
public class HashSetTest {
    @Test
    @DisplayName("【数组】添加元素，不需调用resize方法")
    void testAdd01(){
        final HashSet hashSet = new HashSet();
        for (Integer i = 0; i <= 11; i++) {
            hashSet.add(i);
        }
        System.out.println(hashSet);
    }

    @Test
    @DisplayName("【数组】添加元素，需要调用resize方法，因为数组元素数目超出阈值")
    void testAdd02(){
        final HashSet hashSet = new HashSet();
        for (Integer i = 0; i <= 12; i++) {
            hashSet.add(i);
        }
        System.out.println(hashSet);
    }

    @Test
    @DisplayName("【链表】添加元素，不需调用resize方法")
    void testAdd03(){
        final HashSet hashSet = new HashSet();
        for (Integer i = 0; i <= 7; i++) {
            hashSet.add(new A(i));
        }
        System.out.println(hashSet);
    }

    @Test
    @DisplayName("【链表】添加元素，需要调用resize方法，因为某条链上元素数目大于8，新索引位置等于旧索引位置")
    void testAdd04(){
        final HashSet hashSet = new HashSet();
        for (Integer i = 0; i <= 8; i++) {
            hashSet.add(new A(i));
        }
        System.out.println(hashSet);
    }

    @Test
    @DisplayName("【链表】添加元素，需要调用resize方法，因为某条链上元素数目大于8，新索引位置等于旧索引位置加旧数组长度")
    void testAdd05(){
        final HashSet hashSet = new HashSet();
        for (Integer i = 0; i <= 8; i++) {
            hashSet.add(new A(i, "xxx".hashCode()));
        }
        System.out.println(hashSet);
    }
    
    @Test
    void testAdd06(){
        int cnt = 0;
        int oldCap = 16;
        float loadFactor = 0.75f;
        final HashSet hashSet = new HashSet();
        for (Integer i = 0; i <= 10; i++) {
            if(i >= 8){
                int oldThr = (int)(loadFactor * oldCap);
                int newCap = oldCap << 1;
                int newThr = oldThr << 1;
                System.out.printf("链表上某条链上元素数目大于%s，第%s次扩容：\n", i, ++cnt);
                System.out.printf("===> 原数组长度：%s，原数组阈值：%s\n", oldCap, oldThr);
                System.out.printf("<=== 新数组长度：%s，新数组阈值：%s\n", newCap, newThr);
                System.out.println();
                oldCap = newCap;
            }
            if(i==10){
                System.out.println("链表转化为红黑树");
            }
            hashSet.add(new A(i));
        }
        System.out.println(hashSet);
    }
    
    class A{
        private Integer n;
        private Integer h;
        
        public A(Integer n){
            this.n = n;
        }

        public A(Integer n, Integer h){
            this.n = n;
            this.h = h;
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
            return this.h == null ? 0 : this.h;
        }
    }
}
