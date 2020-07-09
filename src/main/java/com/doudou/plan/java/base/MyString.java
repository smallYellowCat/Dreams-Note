package com.doudou.plan.java.base;

import java.lang.reflect.Field;
import java.util.stream.Stream;

public class MyString {
    /**
     *入口函数，自动生成
     * @author 豆豆
     * @date 2019/12/3 16:14a
    */
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        //整形 1个字节byte， 两个字节short， 四个字节int， 8个字节是long
        char[] ch = {'a', 'b', 'c'};
        String s1 = "abc";
        //String s2 = new String("abc",)
        byte[] bytes = s1.getBytes();
        Byte b = bytes[0];
        byte bb = (byte) 127;

        String ss = "xcvasdhashdas====\r1224545\rhhsadh";
        String[] split = ss.split("\r");

        Stream<String> stringStream = ss.indent(15).lines();
        String[] sa = stringStream.toArray(String[]::new);
        System.out.println(ss);
        System.out.println(ss.trim());
        System.out.println(ss.strip());

        System.out.println(bytes[0] & 0xff);


        Class clazz = s1.getClass();
        Field field = clazz.getDeclaredField("coder");
        field.setAccessible(true);
        Field[] fs = clazz.getFields();
        System.out.println(field.get(s1));
        String s2 = new String(ch);
        System.out.println("s1:" + s1.hashCode() + "    s2:" + s2.hashCode());
    }
}
