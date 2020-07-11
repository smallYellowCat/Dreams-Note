package com.doudou.java.base;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MyArrayList {

    public static void main(String[] args) throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 51; i++) {
            list.add(i + "");
        }
        Field field = ArrayList.class.getDeclaredField("elementData");
        field.setAccessible(true);
        Object[] object = (Object[]) field.get(list);
        System.out.println(object.length);
        list.trimToSize();
        Object[] object2 = (Object[]) field.get(list);
        System.out.println(object2.length);

        System.out.println(list.get(1));
        list.remove(1);
        System.out.println(list.get(1));

        //构造OOM
        //list.ensureCapacity(Integer.MAX_VALUE / 2);

        //test();


        //看到list iterator

    }


    static void test() {
        System.out.println();
    }
}
