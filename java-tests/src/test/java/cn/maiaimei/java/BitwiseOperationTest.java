package cn.maiaimei.java;

import org.junit.jupiter.api.Test;

/**
 * 程序中的所有数在计算机内存中都是以二进制的形式储存的。位运算就是直接对整数在内存中的二进制位进行操作。
 * 将整数转换为二进制数，除2取余，逆序排列
 * 将二进制数转换为整数，整数部分从个位开始，每个位上的数字分别乘以2的0/1/2/3...次方
 * 按位与  a & b   同为1才为1，否则为0
 * 按位或  a | b   有1就是1，否则为0
 * 按位异或 a ^ b   相同为0，不同为1
 * 按位取反 ~a  0和1全部取反
 * 左移   a << b  相当于a乘以2的b次方
 * 带符号右移    a >> b  将a转换为二进制数后，把低位的最后b个数字移出，在高位补0，高位第一个数字正数补0负数补1，相当于a除以2的b次方（取整）
 * 无符号右移    a>>> b  与带符号右移类似，高位第一个数字始终补0
 */
public class BitwiseOperationTest {
    /**
     * 左移
     */
    @Test
    void testShiftLeft(){
        
    }

    /**
     * 带符号右移
     */
    @Test
    void testShiftRight(){
        int capacity = 10;
        System.out.println(capacity >> 1);
    }
    
    @Test
    void testArrayListIncreasesCapacity(){
        int capacity = 10;
        System.out.printf("第%s次扩容数组容量是%d\n", 1, capacity);
        for (int i = 2; i <= 5; i++) {
            int growth = capacity >> 1;
            capacity += growth;
            System.out.printf("第%s次扩容数组容量是%d\n", i, capacity);
        }
    }
}
