package com.doudou.plan.java.base;

public class MyString {
    /**
     *入口函数，自动生成
     * @author 豆豆
     * @date 2019/12/3 16:14a
    */
    public static void main(String[] args){
        char[] ch = {'a', 'b', 'c'};
        String s1 = "abc";
        String s2 = new String(ch);
        System.out.println("s1:" + s1.hashCode() + "    s2:" + s2.hashCode());
    }
}
