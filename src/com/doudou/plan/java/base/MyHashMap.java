package com.doudou.plan.java.base;

import sun.misc.SharedSecrets;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <p>The first step which read jdk's hashMap design
 * and understand it's principle</p>
 * @author dream dou
 */
public class MyHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {

    private static final long seriilizableUuid = 1L;

    /**默认的初始容量大小，必须是2的整数次幂*/
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

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
     * 在第一次使用的时候初始化， 并且在必要的时候重新调整大小， 分配的时候，长度
     * 总是2的幂。在某操作中我们容忍长度为0以允许引导机制（当前不需要）
     */
    transient Node<K,V>[] table;

    /**
     * 持有entrySet()的缓存。这个AbstractMap的属性用于keySet()和values()方法
     */
    transient Set<Map.Entry<K,V>> entrySet;

    transient int size;

    /**
     * 此HashMap结构上修改的次数，结构修改是指改变HashMap中映射的数量或者
     * 更改他的内部结构（例如rehash）。这个属性用来让在集合视图上的迭代快速
     * 失败（阅读ConcurrentModificationException）
     */
    transient int modCount;

    /**
     * resize的下一个大小阈值（capacity * factor）
     * 如果table数组没有被分配内存，那么此属性持有的初始数组容量
     * 或者用0表示DEFAULT_INITIAL_CAPACITY
     */
    int threshold;


    final float loadFactor;


    /*------------------public operations---------------------*/

    public MyHashMap(int initialCapacity, float loadFactor){
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }


    public MyHashMap(int initialCapacity){this(initialCapacity, DEFAULT_LOAD_FACTOR);}


    /**
     * 从这里可以看出来new一个hashmap是很容易的，他只是去指定了容量大小和负载因子
     */
    public MyHashMap(){
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }


    public MyHashMap(Map<? extends K, ? extends V> m){
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        putMapEntries(m, false);
    }

    /**
     * 实现了Map.putAll和Map构造器
     * @param m
     * @param evict
     */
    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict){
        int s = m.size();
        if (s > 0){
            if (table == null){//pre-size
                float ft = (float)s / loadFactor + 1.0f;
                int t = ft < MAXIMUM_CAPACITY ? (int) ft : MAXIMUM_CAPACITY;
                if (t > threshold)
                    threshold = tableSizeFor(t);
            }else if (s > threshold){
                resize();
            }

            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()){
                K key = e.getKey();
                V value = e.getValue();
                putVal(hash(key), key, value, false, evict);
            }
        }
    }

    public int size(){return size;}

    public boolean isEmpty(){ return size == 0;}

    /**
     *
     * @param key
     * @return
     */
    public V get(Object key){
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    /**
     *
     * @param hash
     * @param key
     * @return
     */
    final Node<K,V> getNode(int hash, Object key){
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n-1) & hash]) != null){
            if (first.hash == hash && //always check first node
                    ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null){
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                }while ((e = e.next) != null);
            }
        }
        return null;
    }

    /**
     *
     * @param key
     * @return
     */
    public boolean containsKey(Object key){return getNode(hash(key), key) != null;}

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public V put(K key, V value){
        return putVal(hash(key), key, value, false, true);
    }


    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict){
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            Node<K,V> e; K k;
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                for (int binCount = 0; ; ++binCount){
                    if ((e = p.next) == null){
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1)
                            treeifyBin(tab, hash);
                        break;

                    }
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null){ // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
        afterNodeInsert(evict);
        return null;

    }

    /**
     * 初始化或加倍表大小。 如果为空，则根据属性threshold中的初始目标容量分配。
     * 否则，因为我们使用二的幂次进行扩展，因此每个bin中的元素必须保持相同的index，
     * 或者在新表中以2的幂次方偏移量移动
     * @return
     */
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0){
            if (oldCap >= MAXIMUM_CAPACITY){
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1;
        }else if (oldThr > 0)
            newCap = oldThr;
        else {
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
        if (newThr == 0){
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                    (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes", "unchecked"})
                Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null){
            for (int j = 0; j < oldCap; ++j){
                Node<K,V> e;
                if ((e = oldTab[j]) != null){
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { //维持顺序
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0){
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        }while ((e = next) != null);
                        if (loTail != null){
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null){
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    /**
     * 替换bin中给定hash的索引下的所有链接节点，如果table太小，将会使用resize代替。
     * @param tab
     * @param hash
     */
    final void treeifyBin(Node<K,V>[] tab, int hash){
        int n, index; Node<K,V> e;
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
            resize();
        else if ((e = tab[index = (n - 1) & hash]) != null){
            TreeNode<K,V> hd = null, tl = null;
            do {
                TreeNode<K,V> p = replacementTreeNode(e, null);
                if (tl == null)
                    hd = p;
                else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            }while ((e = e.next) != null);
            if ((tab[index] = hd) != null)
                hd.treeify(tab);
        }
    }

    /**
     * 复制给定map中所有映射到此map中。这些mapping会替换此map中在给定map中存在的映射
     * @param m
     */
    public void putAll(Map<? extends K, ? extends V> m) {putMapEntries(m, true);}

    /**
     * 如果存在，则从此map的映射中删除指定的key
     * @param key
     * @return
     */
    public V remove(Object key){
        Node<K,V> e;
        return (e = removeNode(hash(key), key, null, false, true)) == null ?
                null : e.value;
    }

    /**
     *
     * @param hash
     * @param key
     * @param value
     * @param matchValue
     * @param movable
     * @return
     */
    final Node<K,V> removeNode(int hash, Object key, Object value,
                               boolean matchValue, boolean movable){
        Node<K,V>[] tab; Node<K,V> p; int n, index;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (p = tab[index = (n - 1) & hash]) != null){
            Node<K,V> node = null, e; K k; V v;
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;
            else if ((e = p.next) != null){
                if(p instanceof TreeNode)
                    node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
                else {
                    do {
                        if (e.hash == hash &&
                                ((k = e.key) == key ||
                                        (key != null && key.equals(k)))){
                            node = e;
                            break;
                        }
                        p = e;
                    }while ((e = e.next) != null);
                }
            }
            if (node != null && (!matchValue || (v = node.value) == value ||
                                (value != null && value.equals(v)))){
                if (node instanceof TreeNode)
                    ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
                else if (node == p)
                    tab[index] = node.next;
                else
                    p.next = node.next;
                ++modCount;
                --size;
                afterNodeRemoval(node);
                return node;
            }

        }
        return null;
    }


    public void clear(){
        Node<K,V>[] tab;
        modCount++;
        if ((tab = table) != null && size > 0){
            size = 0;
            for (int i = 0; i < tab.length; ++i)
                tab[i] = null;
        }
    }

    @Override
    public boolean containsValue(Object value) {
        Node<K,V>[] tab; V v;
        if ((tab = table) != null && size > 0){
            for (int i = 0; i < tab.length; ++i){
                for (Node<K,V> e = tab[i]; e != null; e = e.next){
                    if ((v = e.value) == value ||
                        (value != null && value.equals(v)))
                        return  true;
                }
            }
        }
        return false;
    }

    /**
     * 这两个成员变量是在AbstraMap中的，因为包级私有所以无法引用到，暂且重新定义下
     */
    transient Set<K>        keySet;
    transient Collection<V> values;


    /**
     * 返回map中的keys的集合视图，此集合是map的后备，因此改变map此集合也会相应的改变，
     * 反之亦然。如果在此集合迭代遍历期间map被修改，则迭代的结果是未定义的。 此set支持
     * 通过迭代器的remove，set的remove，removeAll，retainAll以及clear操作删除元
     * 素。不支持add或是addAll操作。
     * @return
     */
    @Override
    public Set<K> keySet() {
        //return super.keySet();
        Set<K> ks = keySet;
        if (ks == null){
            ks = new KeySet();
            keySet = ks;
        }
        return ks;
    }

    final class KeySet extends AbstractSet<K> {
        @Override
        public int size() { return size; }

        @Override
        public void clear() {
            MyHashMap.this.clear();
        }

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public boolean contains(Object o) {
            //return super.contains(o);
            return containsKey(o);
        }

        @Override
        public boolean remove(Object key) {
            //return super.remove(o);
            return removeNode(hash(key), key, null, false, true) != null;
        }

        @Override
        public Spliterator<K> spliterator() {
            return new KeySpliterator<>(MyHashMap.this, 0, -1, 0, 0);
        }

        @Override
        public void forEach(Consumer<? super K> action) {
            Node<K,V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null){
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<K,V> e = tab[i]; e != null; e = e.next)
                        action.accept(e.key);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }


    }


    /**
     * 跟keySet()类似
     * @return
     */
    @Override
    public Collection<V> values() {
        //return super.values();
        Collection<V> vs = values;
        if (vs == null){
            vs = new Values();
            values = vs;
        }
        return vs;
    }

    final class Values extends AbstractCollection<V> {
        @Override
        public int size() {
            return size;
        }

        @Override
        public void clear() {
            MyHashMap.this.clear();
        }

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public boolean contains(Object o) {
            return containsValue(o);
        }

        @Override
        public Spliterator<V> spliterator() {
            return new ValueSpliterator<>(MyHashMap.this, 0, -1, 0, 0);
        }

        @Override
        public void forEach(Consumer<? super V> action) {
            Node<K,V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null){
                int mc = modCount;
                for (int  i = 0; i < tab.length; ++i){
                    for (Node<K,V> e = tab[i]; e != null; e = e.next)
                        action.accept(e.value);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }


    public Set<Map.Entry<K,V>> entrySet(){
        Set<Map.Entry<K,V>> es;
        return (es = entrySet) == null ? (entrySet = new EntrySet()) : es;
    }

    final class EntrySet extends AbstractSet<Map.Entry<K,V>> {
        @Override
        public int size() {
            return size;
        }

        @Override
        public void clear() {
            MyHashMap.this.clear();
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public boolean contains(Object o) {
            //return super.contains(o);
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?,?> e = (Map.Entry<?, ?>) o;
            Object key = e.getKey();
            Node<K,V> candidate = getNode(hash(key), key);
            return candidate != null && candidate.equals(e);
        }

        @Override
        public boolean remove(Object o) {
            //return super.remove(o);
            if (o instanceof Map.Entry){
                Map.Entry<?,?> e = (Map.Entry<?, ?>) o;
                Object key = e.getKey();
                Object value = e.getValue();
                return removeNode(hash(key), key, value, true, true) != null;
            }
            return false;
        }

        @Override
        public Spliterator<Map.Entry<K, V>> spliterator() {
            return new EntrySpliterator<>(MyHashMap.this, 0, -1, 0, 0);
        }

        @Override
        public void forEach(Consumer<? super Map.Entry<K, V>> action) {
            Node<K,V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null){
                int mc = modCount;
                for (int i = 0; i < tab.length; i++){
                    for (Node<K,V> e = tab[i]; e != null; e = e.next)
                        action.accept(e);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }


    @Override
    public V getOrDefault(Object key, V defaultValue) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? defaultValue : e.value;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return putVal(hash(key), key, value, true, true);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return removeNode(hash(key), key, value, true, true) != null;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        Node<K,V> e; V v;
        if ((e = getNode(hash(key), key)) != null &&
                ((v = e.value) == oldValue || (v != null && v.equals(oldValue)))){
            e.value = newValue;
            afterNodeAccess(e);
            return true;
        }
        return false;
    }

    @Override
    public V replace(K key, V value) {
        Node<K,V> e;
        if ((e = getNode(hash(key), key)) != null){
            V oldValue = e.value;
            e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
        return null;
    }


    /**
     * jdk8之后增加的方法，通过给定的函数表达式计算出指定key将要放到map中的value
     * @param key
     * @param mappingFunction
     * @return
     */
    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        if (mappingFunction == null)
            throw new NullPointerException();
        int hash = hash(key);
        Node<K,V>[] tab; Node<K,V> first; int n, i;
        int binCount = 0;
        TreeNode<K,V> t = null;
        Node<K,V> old = null;
        if (size > threshold || (tab = table) == null ||
            (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((first = tab[i = (n - 1) & hash]) != null){
            if (first instanceof TreeNode)
                old = (t = (TreeNode<K,V>)first).getTreeNode(hash, key);
            else {
                Node<K,V> e = first; K k;
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k)))){
                        old = e;
                        break;
                    }

                    ++binCount;
                }while ((e = e.next) != null);
            }
            V oldValue;
            if (old != null && (oldValue = old.value) != null){
                afterNodeAccess(old);
                return oldValue;
            }
        }
        V v = mappingFunction.apply(key);
        if (v == null){
            return null;
        }else if (old != null){
            old.value = v;
            afterNodeAccess(old);
            return v;
        }
        else if (t != null)
            t.putTreeVal(this, tab, hash, key, v);
        else {
            tab[i] = newNode(hash, key, v, first);
            if (binCount >= TREEIFY_THRESHOLD - 1)
                treeifyBin(tab, hash);
        }
        ++modCount;
        ++size;
        afterNodeInsert(true);
        return v;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (remappingFunction == null)
            throw new NullPointerException();
        Node<K,V> e; V oldValue;
        int hash = hash(key);
        if ((e = getNode(hash, key)) != null &&
            (oldValue = e.value) != null){
            V v = remappingFunction.apply(key, oldValue);
            if (v != null){
                e.value = v;
                afterNodeAccess(e);
                return v;
            }
            else
                removeNode(hash, key, null, false, true);
        }
        return null;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (remappingFunction == null)
            throw new NullPointerException();
        int hash = hash(key);
        Node<K,V>[] tab; Node<K,V> first; int n, i;
        int binCount = 0;
        TreeNode<K,V> t = null;
        Node<K,V> old = null;
        if ((size > threshold || (tab = table) == null) ||
            (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((first = tab[i = (n - 1) & hash]) != null){
            if (first instanceof TreeNode)
                old = (t = (TreeNode<K,V>)first).getTreeNode(hash, key);
            else {
                Node<K,V> e = first; K k;
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k)))){
                        old = e;
                        break;
                    }
                    ++binCount;

                }while ((e = e.next) != null);
            }
        }
        V oldValue = (old == null) ? null : old.value;
        V v = remappingFunction.apply(key, oldValue);
        if (old != null){
            if (v != null){
                old.value = v;
                afterNodeAccess(old);
            }
            else
                removeNode(hash, key, null, false, true);
        }
        else if (v != null){
            if (t != null)
                t.putTreeVal(this, tab, hash, key, v);
            else {
                tab[i] = newNode(hash, key, v, first);
                if (binCount >= TREEIFY_THRESHOLD - 1)
                    treeifyBin(tab, hash);
            }
            ++modCount;
            ++size;
            afterNodeInsert(true);
        }
        return v;
    }


    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        if (value == null)
            throw new NullPointerException();
        if (remappingFunction == null)
            throw new NullPointerException();
        int hash = hash(key);
        Node<K,V>[] tab; Node<K,V> first; int n, i;
        int binCount = 0;
        TreeNode<K,V> t = null;
        Node<K,V> old = null;
        if (size > threshold || (tab = table) == null ||
                (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((first = tab[i = (n - 1) & hash]) != null) {
            if (first instanceof TreeNode)
                old = (t = (TreeNode<K,V>)first).getTreeNode(hash, key);
            else {
                Node<K,V> e = first; K k;
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k)))) {
                        old = e;
                        break;
                    }
                    ++binCount;
                } while ((e = e.next) != null);
            }
        }
        if (old != null) {
            V v;
            if (old.value != null)
                v = remappingFunction.apply(old.value, value);
            else
                v = value;
            if (v != null) {
                old.value = v;
                afterNodeAccess(old);
            }
            else
                removeNode(hash, key, null, false, true);
            return v;
        }
        if (value != null) {
            if (t != null)
                t.putTreeVal(this, tab, hash, key, value);
            else {
                tab[i] = newNode(hash, key, value, first);
                if (binCount >= TREEIFY_THRESHOLD - 1)
                    treeifyBin(tab, hash);
            }
            ++modCount;
            ++size;
            afterNodeInsert(true);
        }
        return value;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        Node<K,V>[] tab;
        if (action == null)
            throw new NullPointerException();
        if (size > 0 && (tab = table) != null){
            int mc= modCount;
            for (int i = 0; i < tab.length; ++i) {
                for (Node<K,V> e = tab[i]; e != null; e = e.next)
                    action.accept(e.key, e.value);
            }
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }

    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Node<K,V>[] tab;
        if (function == null)
            throw new NullPointerException();
        if (size > 0 && (tab = table) != null){
            int mc = modCount;
            for (int  i = 0; i < tab.length; i++){
                for (Node<K,V> e = tab[i]; e != null; e = e.next){
                    e.value = function.apply(e.key, e.value);
                }
            }
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }

    }

    /*----------------------------------------------------------------------------------*/
    //Cloning and serialization


    /**
     * 返回此HashMap实例的浅拷贝：键和值本身没有被克隆
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    @SuppressWarnings("unchecked")
    protected Object clone() throws CloneNotSupportedException {
        //return super.clone();
        MyHashMap<K,V> result;
        try {
            result = (MyHashMap<K, V>) super.clone();
        }catch (Exception e){
            //实现Cloneable之后就不会发生
            throw new InternalError(e);
        }
        result.reinitialize();
        result.putMapEntries(this, false);
        return result;
    }

    //在序列化HashSets的时候也使用这些方法
    final float loadFactor(){return loadFactor;}
    final int capacity(){
        return (table != null) ?  table.length :
            (threshold > 0) ? threshold :
            DEFAULT_INITIAL_CAPACITY;
    }


    /**
     *
     * @param s
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream s) throws IOException {
        int buckets = capacity();
        //写出阈值，加载因子，以及任何隐藏的东西
        s.defaultWriteObject();
        s.writeInt(buckets);
        s.writeInt(size);
        internalWriteEntries(s);
    }


    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        // Read in the threshold (ignored), loadfactor, and any hidden stuff
        s.defaultReadObject();
        reinitialize();
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new InvalidObjectException("Illegal load factor: " +
                                            loadFactor);
        s.readInt();
        int mappings = s.readInt();
        if (mappings < 0)
            throw new InvalidObjectException("Illegal mappings count: " +
                                            mappings);
        else if (mappings > 0) {
            float lf = Math.min(Math.max(0.25f, loadFactor), 4.0f);
            float fc = (float)mappings / lf + 1.0f;
            int cap = ((fc < DEFAULT_INITIAL_CAPACITY) ?
                        DEFAULT_INITIAL_CAPACITY :
                        (fc >= MAXIMUM_CAPACITY) ?
                        MAXIMUM_CAPACITY :
                        tableSizeFor((int) fc));
            float ft = (float)cap * lf;
            threshold = (cap < MAXIMUM_CAPACITY && ft < MAXIMUM_CAPACITY) ?
                    (int) ft : Integer.MAX_VALUE;

            SharedSecrets.getJavaOISAccess().checkArray(s, Map.Entry[].class, cap);
            @SuppressWarnings({"rawtypes", "unchecked"})
            Node<K,V>[] tab = (Node<K, V>[]) new Node[cap];
            table = tab;

            for (int i = 0; i < mappings; i++){
                @SuppressWarnings("unchecked")
                K key = (K) s.readObject();
                @SuppressWarnings("unchecked")
                V value = (V) s.readObject();
                putVal(hash(key), key, value, false, false);
            }
        }
    }

    /*-----------------------------------------------------------------------------------*/
    //iterators
    abstract class HashIterator{
        Node<K,V> next;
        Node<K,V> current;
        int expectedModCount;
        int index;

        HashIterator(){
            expectedModCount = modCount;
            Node<K,V>[] t = table;
            current = next = null;
            index = 0;
            if (t != null && size > 0){//先入先出
                do {

                }while (index < t.length && (next = t[index++]) == null);
            }
        }

        public final boolean hasNext(){return next != null;}

        final Node<K,V> nextNode(){
            Node<K,V>[] t;
            Node<K,V> e = next;
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (e == null)
                throw new NoSuchElementException();
            if ((next = (current = e).next) == null && (t = table) != null){
                do {

                }while (index < t.length && (next = t[index++]) == null);
            }
            return e;
        }

        public final void remove(){
            Node<K,V> p = current;
            if (p == null)
                throw  new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            current = null;
            K key = p.key;
            removeNode(hash(key), key, null, false, false);
            expectedModCount = modCount;
        }

    }

    final class KeyIterator extends HashIterator implements Iterator<K>{

        @Override
        public K next() {
            return nextNode().key;
        }
    }

    final class ValueIterator extends HashIterator implements Iterator<V>{
        @Override
        public V next() {
            return nextNode().value;
        }
    }

    final class EntryIterator extends HashIterator implements Iterator<Map.Entry<K,V>>{
        @Override
        public Map.Entry<K, V> next() {
            return nextNode();
        }
    }

    /*-----------------------------------------------------------------------------------*/
    // spliterators

    static class MyHashMapSpliterator<K,V>{
        final MyHashMap<K,V> map;
        Node<K,V> current;      //current node
        int index;              //current index, modified on advance/split
        int fence;              //one past last index
        int est;                //size estimate
        int expectedModCount;

        MyHashMapSpliterator(MyHashMap<K,V> m, int origin,
                             int fence, int est,
                             int expectedModCount) {
            this.map = m;
            this.index = origin;
            this.fence = fence;
            this.est = est;
            this.expectedModCount = expectedModCount;
        }

        final int getFence(){
            int hi;
            if ((hi = fence) < 0){
                MyHashMap<K,V> m = map;
                est = m.size;
                expectedModCount = m.modCount;
                Node<K,V>[] tab = m.table;
                hi = fence = (tab == null) ? 0 : tab.length;
            }

            return hi;
        }

        public final long estimateSize(){
            getFence();
            return (long) est;
        }
    }

    static final class KeySpliterator<K,V>
            extends MyHashMapSpliterator<K,V>
            implements Spliterator<K>{

        KeySpliterator(MyHashMap<K, V> m, int origin, int fence, int est, int expectedModCount) {
            super(m, origin, fence, est, expectedModCount);
        }

        @Override
        public boolean tryAdvance(Consumer<? super K> action) {
            int hi;
            if (action == null)
                throw new NullPointerException();
            Node<K,V>[] tab = map.table;
            if (tab != null && tab.length >= (hi = getFence()) && index >= 0){
                while (current != null || index < hi){
                    if (current == null)
                        current = tab[index++];
                    else {
                        K k = current.key;
                        current = current.next;
                        action.accept(k);
                        if (map.modCount != expectedModCount)
                            throw new ConcurrentModificationException();
                        return true;
                    }
                }
            }
            return false;
        }


        @Override
        public Spliterator<K> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid || current != null) ? null :
                    new KeySpliterator<>(map, lo, index = mid, est >>>= 1,
                                            expectedModCount);
        }

        @Override
        public void forEachRemaining(Consumer<? super K> action) {
            int i, hi, mc;
            if (action == null)
                throw new NullPointerException();
            MyHashMap<K,V> m = map;
            Node<K,V>[] tab = m.table;
            if ((hi = fence) < 0){
                mc = expectedModCount = m.modCount;
                hi = fence = (tab == null) ? 0 : tab.length;
            }

            else
                mc = expectedModCount;
            if (tab != null && tab.length >= hi &&
                    (i = index) >= 0 && (i < (index = hi) || current != null)){
                Node<K,V> p = current;
                current = null;
                do {
                    if (p == null)
                        p = tab[i++];
                    else {
                        action.accept(p.key);
                        p = p.next;
                    }
                }while (p != null || i < hi);
                if (m.modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }

        @Override
        public int characteristics() {
            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0) |
                    Spliterator.DISTINCT;
        }
    }


    static final class ValueSpliterator<K,V>
            extends MyHashMapSpliterator<K,V>
            implements Spliterator<V>{

        ValueSpliterator(MyHashMap<K, V> m, int origin, int fence, int est, int expectedModCount) {
            super(m, origin, fence, est, expectedModCount);
        }


        @Override
        public boolean tryAdvance(Consumer<? super V> action) {
            int hi;
            if (action == null)
                throw new NullPointerException();
            Node<K,V>[] tab = map.table;
            if (tab != null && tab.length >= (hi = getFence()) && index >= 0){
                while (current != null || index < hi){
                    if (current == null)
                        current = tab[index++];
                    else {
                        V v = current.value;
                        current = current.next;
                        action.accept(v);
                        if (map
                                .modCount != expectedModCount)
                            throw new ConcurrentModificationException();
                        return true;
                    }
                }
            }
            return false;
        }


        @Override
        public void forEachRemaining(Consumer<? super V> action) {
            int i, hi, mc;
            if (action == null)
                throw new NullPointerException();
            MyHashMap<K,V> m = map;
            Node<K,V>[] tab = m.table;
            if ((hi =fence) < 0) {
                mc = expectedModCount = m.modCount;
                hi = fence = (tab == null) ? 0 : tab.length;
            }
            else
                mc = expectedModCount;
            if (tab != null && tab.length >= hi &&
                    (i = index) >= 0 && (i < (index = hi) || current != null)) {
                Node<K,V> p = current;
                current = null;
                do {
                    if (p == null)
                        p = tab[i++];
                    else {
                        action.accept(p.value);
                        p = p.next;
                    }
                }while (p != null || i < hi);
                if (m.modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }


        @Override
        public Spliterator<V> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid || current != null) ? null :
                    new ValueSpliterator<>(map, lo, index = mid,  est >>>= 1,
                                            expectedModCount);
        }


        @Override
        public int characteristics() {
            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0);
        }
    }


    static final class EntrySpliterator<K,V>
            extends MyHashMapSpliterator<K,V>
            implements Spliterator<Map.Entry<K,V>>{

        EntrySpliterator(MyHashMap<K, V> m, int origin, int fence, int est, int expectedModCount) {
            super(m, origin, fence, est, expectedModCount);
        }


        @Override
        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> action) {
            int hi;
            if (action == null)
                throw new NullPointerException();
            Node<K,V>[] tab = map.table;
            if (tab != null && tab.length >= (hi = getFence()) && index >= 0){
                while (current != null || index < hi){
                    if (current == null)
                        current = tab[index++];
                    else {
                        Node<K,V> e = current;
                        current = current.next;
                        action.accept(e);
                        if (map.modCount != expectedModCount)
                            throw new ConcurrentModificationException();
                        return true;
                    }
                }
            }
            return false;
        }


        @Override
        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> action) {
            int i, hi, mc;
            if (action == null)
                throw new NullPointerException();
            MyHashMap<K,V> m = map;
            Node<K,V>[] tab = m.table;
            if ((hi = fence) < 0) {
                mc = expectedModCount = m.modCount;
                hi = fence = (tab == null) ? 0 : tab.length;
            }
            else
                mc = expectedModCount;
            if (tab != null && tab.length >= hi &&
                (i = index) >= 0 && (i < (index = hi) || current != null)){
                Node<K,V> p = current;
                current = null;
                do {
                    if (p == null)
                        p = tab[i++];
                    else {
                        action.accept(p);
                        p = p.next;
                    }
                } while (p != null || i < hi);
                if (m.modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }


        @Override
        public Spliterator<Map.Entry<K, V>> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid || current != null) ? null :
                    new EntrySpliterator<>(map, lo, index = mid, est >>>= 1,
                                            expectedModCount);
        }


        @Override
        public int characteristics() {
            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0) |
                Spliterator.DISTINCT;
        }
    }

    /*----------------------------------------------------------------------------------*/

    //LinkedHashMap support

    /*
     * The following package-protected methods are designed to be
     * overridden by LinkedHashMap, but not by any other subclass.
     * Nearly all other internal methods are also package-protected
     * but are declared final, so can be used by LinkedHashMap, view
     * classes, and HashSet.
     *
     */

    //create a regular (non-tree) node
    Node<K,V> newNode(int has, K key, V value, Node<K,V> next){
        return new Node<>(has, key, value, next);
    }

    //用于将树节点转换为普通节点
    Node<K,V> replacementNode(Node<K,V> p, Node<K,V> next) {
        return new Node<>(p.hash, p.key, p.value, next);
    }

    //创建一个树节点
    TreeNode<K,V> newTreeNode(int hash, K key, V value, Node<K,V> next) {
        return new TreeNode<>(hash, key, value, next);
    }

    //用于treeifyBin
    TreeNode<K,V> replacementTreeNode(Node<K,V> p, Node<K,V> next){
        return new TreeNode<>(p.hash, p.key, p.value, next);
    }

    /**
     * 重设初始化的默认值，会被clone或者readObject调用
     */
    void reinitialize() {
        table = null;
        entrySet = null;
        //这两个是在AbstractMap中的包级私有的属性
        //keySet = null;
        //values = null;
        modCount = 0;
        threshold = 0;
        size = 0;
    }

    //Callbacks to allow LinkedHashMap post-actions
    void afterNodeAccess(Node<K,V> p){}
    void afterNodeInsert(boolean evict){}
    void afterNodeRemoval(Node<K,V> p){}

    //仅用于writeObject调用， 用于确保顺序一致
    void internalWriteEntries(ObjectOutputStream s) throws IOException {
        Node<K,V>[] tab;
        if (size > 0 && (tab = table) != null){
            for (int i = 0; i < tab.length; ++i){
                for (Node<K,V>  e = tab[i]; e != null; e = e.next){
                    s.writeObject(e.key);
                    s.writeObject(e.value);
                }
            }
        }
    }

    /*-----------------------------------------------------------------------------------*/
    // Tree bins

    /**
     * 从linkedHashMap复制而来，因为LinkedHashMap.Entry包级私有无法引用
     * @param <K>
     * @param <V>
     */
    static class Entry<K,V> extends MyHashMap.Node<K,V> {
        Entry<K,V> before, after;
        Entry(int hash, K key, V value, Node<K,V> next) {
            super(hash, key, value, next);
        }
    }

    static final class TreeNode<K,V> extends Entry<K,V>{

        TreeNode<K,V> parent; //red-black tree links
        TreeNode<K,V> left;
        TreeNode<K,V> right;
        TreeNode<K,V> prev; // needed to unlink next upon deletion
        boolean red;

        TreeNode(int hash, K key, V value, MyHashMap.Node<K, V> next) {
            super(hash, key, value, next);
        }

        /**
         * 返回树的根
         * @return
         */
        final TreeNode<K,V> root(){
            for (TreeNode<K,V> r = this, p;;){
                if ((p = r.parent) == null)
                    return r;
                r = p;
            }
        }

        /**
         * 确保给定的根是指定bins的第一个节点
         * @param tab
         * @param root
         * @param <K>
         * @param <V>
         */
        static <K,V> void moveRootToFront(Node<K,V>[] tab, TreeNode<K,V> root){
            int n;
            if (root != null && tab != null && (n = tab.length) > 0){
                int index = (n - 1) & root.hash;
                TreeNode<K,V> first = (TreeNode<K,V>)tab[index];
                if (root != first){
                    Node<K,V> rn;
                    tab[index] = root;
                    TreeNode<K,V> rp = root.prev;
                    if ((rn = root.next) != null)
                        ((TreeNode<K,V>)rn).prev = rp;
                    if (rp != null)
                        rp.next = rn;
                    if (first != null)
                        first.prev = root;
                    root.next = first;
                    root.prev = null;

                }
                assert checkInvariants(root);
            }
        }

        /**
         * 使用给定的hash和key从跟p查找节点。kc参数在第一次使用比较键时缓存了comparableClassFor(key)
         * @param h
         * @param k
         * @param kc
         * @return
         */
        final TreeNode<K,V> find(int h, Object k, Class<?> kc){
            TreeNode<K,V> p = this;
            do {
                int ph, dir; K pk;
                TreeNode<K,V> pl = p.left, pr = p.right, q;
                if ((ph = p.hash) > h)
                    p = pl;
                else if (ph < h)
                    p = pr;
                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                    return p;
                else if (pl == null)
                    p = pr;
                else if (pr == null)
                    p = pl;
                else if ((kc != null ||
                        (kc = comparableClassFor(k)) != null) &&
                        (dir = compareComparables(kc, k, pk)) != 0)
                    p = (dir < 0) ? pl : pr;
                else if ((q = pr.find(h, k, kc)) != null)
                    return q;
                else
                    p = pl;
            }while (p != null);
            return null;
        }

        /**
         * calls find for root node
         * @param h
         * @param k
         * @return
         */
        final TreeNode<K,V> getTreeNode(int h, Object k){
            return ((parent != null) ? root() : this).find(h, k, null);
        }

        /**
         * Tie-breaking实用程序用于在顺序插入时遇到相同的hashCode且不可比较时。
         * 我们不要求整体的顺序，仅仅是一个一致性的插入规则来维持再平衡的等价。
         * Tie-breaking比简单的测试更重要一点。
         * @param a
         * @param b
         * @return
         */
        static int tieBreakOrder(Object a, Object b) {
            int d;
            if (a == null || b ==null ||
                    (d = a.getClass().getName()
                    .compareTo(b.getClass().getName())) == 0)
                d = (System.identityHashCode(a) <= System.identityHashCode(b) ?
                        -1 : 1);
            return d;
        }

        /**
         * 从该节点链接的节点树
         * @param tab
         */
        final void treeify(Node<K,V>[] tab){
            TreeNode<K,V> root = null;
            for (TreeNode<K,V> x = this, next; x != null; x = next){
                next = (TreeNode<K,V>)x.next;
                x.left = x.right = null;
                if (root == null){
                    x.parent = null;
                    x.red = false;
                    root = x;
                } else {
                   K k = x.key;
                   int h = x.hash;
                   Class<?> kc = null;
                   for (TreeNode<K,V> p = root;;){
                       int dir, ph;
                       K pk = p.key;
                       if ((ph = p.hash) > h)
                           dir = -1;
                       else if (ph < h)
                           dir = 1;
                       else if ((kc == null &&
                               (kc = comparableClassFor(k)) == null) ||
                               (dir = compareComparables(kc, k, pk)) == 0)
                           dir = tieBreakOrder(k, pk);
                       TreeNode<K,V> xp = p;
                       if ((p = (dir <= 0) ? p.left : p.right) == null){
                           x.parent = xp;
                           if (dir <= 0)
                               xp.left = x;
                           else
                               xp.right = x;
                           root = balanceInsertion(root, x);
                           break;
                       }
                   }
                }
            }

            moveRootToFront(tab, root);
        }


        /**
         * 返回一个非树节点的list来替换这个节点链接的树节点
         * @param map
         * @return
         */
        final Node<K,V> untreeify(MyHashMap<K,V> map){
            Node<K,V> hd = null, tl = null;
            for (Node<K,V> q = this; q != null; q = q.next) {
                Node<K,V> p = map.replacementNode(q, null);
                if (tl == null)
                    hd = p;
                else
                    tl.next = p;
                tl = p;
            }
            return hd;
        }

        final TreeNode<K,V> putTreeVal(MyHashMap<K,V> map, Node<K,V>[] tab,
                                       int h, K k, V v){
            Class<?> kc = null;
            boolean searched = false;
            TreeNode<K,V> root = (parent != null) ? root() : this;
            for (TreeNode<K,V> p = root;;){
                int dir, ph; K pk;
                if ((ph = p.hash) > h)
                    dir = -1;
                else if (ph < h)
                    dir = 1;
                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                    return p;

            }
        }

        final void removeTreeNode(MyHashMap<K,V> map, Node<K,V>[] tab,
                                  boolean movable){

        }

        final void split(MyHashMap<K,V> map, Node<K,V>[] tab, int index, int bit) {

        }

        /*---------------------------------------------------------------------------*/
        //Red-black tree methods, all adapted from CLR

        /**
         * 左旋
         * @param root
         * @param p
         * @param <K>
         * @param <V>
         * @return
         */
        static <K,V> TreeNode<K,V> rotateLeft(TreeNode<K,V> root,
                                              TreeNode<K,V> p){
            TreeNode<K,V> r, pp, rl;
            if (p != null && (r = p.right) != null){
                if ((rl = p.right = r.left) != null)
                    rl.parent = p;
                if ((pp = r.parent = p.parent) == null)
                    (root = r).red = false;
                else if (pp.left == p)
                    pp.left = r;
                else
                    pp.right = r;
                r.left = p;
                p.parent = r;
            }
            return root;
        }

        /**
         * 右旋
         * @param root
         * @param p
         * @param <K>
         * @param <V>
         * @return
         */
        static <K,V> TreeNode<K,V> rotateRight(TreeNode<K,V> root,
                                               TreeNode<K,V> p){
            TreeNode<K,V> l, pp, lr;
            if (p != null && (l = p.left) != null){
                if ((lr = p.left = l.right) != null)
                    lr.parent = p;
                if ((pp = l.parent = p.parent) == null)
                    (root = l).red = false;
                else if (pp.right == p)
                    pp.right = l;
                else
                    pp.left = l;
                l.right = p;
                p.parent = l;
            }
            return root;
        }


        /**
         * 平衡插入
         * @param root
         * @param x
         * @param <K>
         * @param <V>
         * @return
         */
        static <K,V> TreeNode<K,V> balanceInsertion(TreeNode<K,V> root,
                                                    TreeNode<K,V> x){
            x.red = true;
            for (TreeNode<K,V> xp, xpp, xppl, xppr;;){
                if ((xp = x.parent) == null){
                    x.red = false;
                    return x;
                }else if (!xp.red || (xpp = xp.parent) == null)
                    return root;
                if (xp == (xppl = xpp.left)){
                    if ((xppr = xpp.right) != null && xppr.red){
                        xppr.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    }else {
                        if (x == xp.right) {
                            root = rotateLeft(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }
                        if (xp != null){
                            xp.red = false;
                            if (xpp != null){
                                xpp.red = true;
                                root = rotateRight(root, xpp);
                            }
                        }
                    }
                }else {
                    if (xppl != null && xppl.red){
                        xppl.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    }else {
                        if (x == xp.left) {
                            root = rotateRight(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }

                        if (xp != null){
                            xp.red = false;
                            if (xpp != null) {
                                xpp.red = true;
                                root = rotateLeft(root, xpp);
                            }
                        }
                    }
                }
            }
        }


        /**
         * 平衡删除
         * @param root
         * @param x
         * @param <K>
         * @param <V>
         * @return
         */
        static <K,V> TreeNode<K,V> balanceDeletion(TreeNode<K,V> root,
                                                   TreeNode<K,V> x){
            for (TreeNode<K,V> xp, xpl, xpr;;){
                if (x == null || x == root)
                    return root;
                else if ((xp = x.parent) == null){
                    x.red = false;
                    return x;
                }else if (x.red){
                    x.red = false;
                    return root;
                }else if ((xpl = xp.left) == x) {
                    if ((xpr = xp.right) != null && xpr.red){
                        xpr.red = false;
                        xp.red = true;
                        root = rotateLeft(root, xp);
                        xpr = (xp = x.parent) == null ? null : xp.right;
                    }


                }

            }
        }

        /**
         * Recursive invariant check
         */
        static <K,V> boolean checkInvariants(MyHashMap.TreeNode<K,V> t) {
            MyHashMap.TreeNode<K,V> tp = t.parent, tl = t.left, tr = t.right,
                    tb = t.prev, tn = (MyHashMap.TreeNode<K,V>)t.next;
            if (tb != null && tb.next != t)
                return false;
            if (tn != null && tn.prev != t)
                return false;
            if (tp != null && t != tp.left && t != tp.right)
                return false;
            if (tl != null && (tl.parent != t || tl.hash > t.hash))
                return false;
            if (tr != null && (tr.parent != t || tr.hash < t.hash))
                return false;
            if (t.red && tl != null && tl.red && tr != null && tr.red)
                return false;
            if (tl != null && !checkInvariants(tl))
                return false;
            if (tr != null && !checkInvariants(tr))
                return false;
            return true;
        }

    }


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

}
