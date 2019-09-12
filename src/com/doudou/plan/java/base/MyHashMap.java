package com.doudou.plan.java.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * <p>The first step which read jdk's hashMap design
 * and understand it's principle</p>
 * @author dream dou
 */
public class MyHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {

    private static final long seriilizableUuid = 1L;

    /**默认的初始容量大小，必须是2的整数次幂*/
    static final int DEFAULT_INITITIAL_CAPACITY = 1 << 4;

    /**最大容量，必须小于等于 1 << 30*/
    static final int MAXIMUM_CAPACITY = 1 << 30;

    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**bin中条目计数链表转树的阈值，此值应该比2大，而且至少是8，以便与树删除收缩回转的假设吻合*/
    static final int TREEIFY_THRESHOLD = 8;

    /**树转链表的阈值*/
    static final int UNTREEIFY_THRESHOLD = 6;

    /**能够被转成树的最小表容量，（否则如果一个bin中有太多的节点将会进行扩容），
     * 这个大小至少为4倍的链表转树的阈值，以避免扩容与树化之间的冲突**/
    static final int MIN_TREEIFY_CAPACITY = 64;

    /**
     *
     * @param <K>
     * @param <V>
     */
    static class Node<K,V> implements Map.Entry<K,V> {

        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next){
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }


        @Override
        public V getValue() {
            return value;
        }


        @Override
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry){
                Map.Entry<?,?> e = (Entry<?, ?>) o;
                if (Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }

    /*-------------------------static utilities-----------------------------------*/

    /**
     * 扰动函数，高位hash向低位扩散
     * @param key
     * @return
     */
    static final int hash(Object key){
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * 如果此类实现了Comparable接口，返回此类的类类型，否则返回null
     * @param x
     * @return
     */
    static Class<?> comparableClassFor(Object x){
        if (x instanceof Comparable){
            Class<?> c;
            Type[] ts, as; Type t;
            ParameterizedType p;
            if ((c = x.getClass()) == String.class) //bypass checks
                return c;
            if ((ts = c.getGenericInterfaces()) != null){
                for (int i = 0; i < ts.length; i++){
                    if (((t = ts[i]) instanceof ParameterizedType) &&
                            ((p = (ParameterizedType) t).getRawType() ==
                            Comparable.class) &&
                            (as = p.getActualTypeArguments()) != null &&
                            as.length == 1 && as[0] == c) // type arg is c
                        return c;
                }
            }
        }
        return null;
    }

    /***
     * 如果k和x可比返回k和x的比较结果，否则返回0
     * @param kc
     * @param k
     * @param x
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    static int compareComparables(Class<?> kc, Object k, Object x){
        return (x == null || x.getClass() != kc ? 0 :
                ((Comparable)k).compareTo(x));
    }

    /**
     * table容量计算
     * @param cap
     * @return
     */
    static final int tableSizeFor(int cap){
       int n = cap - 1;
       n |= n >>> 1;
       n |= n >>> 2;
       n |= n >>> 4;
       n |= n >>> 8;
       n |= n >>> 16;
       return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;

    }


    /*----------------------------fields-----------------------------------------*/

    /**
     * 再第一次使用的时候初始化， 并且在必要的时候重新调整大小， 分配的时候，长度
     * 总是2的幂。在某操作中我们容忍长度为0以允许引导机制（当前不需要）
     */
    transient Node<K,V>[] table;
    /**
     *入口函数，自动生成
     * @author 豆豆
     * @date 2019/9/4 11:22a
    */
    public static void main(String[] args){
        Node<String, Object> node = new Node<>(1, "1", "1", null);
        Type[] types = node.getClass().getGenericInterfaces();
        String s = "dsadasdads";
        System.out.println(Objects.hash(s));
        System.out.println(Objects.hashCode(s));
    }


    private Map<String, Object> map = new HashMap<>();

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}
