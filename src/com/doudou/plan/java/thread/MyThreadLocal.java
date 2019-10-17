package com.doudou.plan.java.thread;

import java.lang.ref.WeakReference;
import java.time.temporal.ValueRange;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 此类提供线程局部变量。这些变量不同于它们的普通副本，因为访问它们（通过{@code get}或{@code set}）
 * 的每个线程都有自己独立初始化的变量副本。{@code ThreadLocal}实例在类中通常是私有的静态属性
 * 借此希望将状态与线程关联（e.g. 用户id或事务id）。
 *
 * <p>例如：下面的类型为每个线程生成唯一的本地标识符。
 *
 * 线程的id在第一次调用ThreadId.get()时被赋值，并在随后的调用中保持不变。
 * <pre>
 *   import java.util.concurrent.atomic.AtomicInteger;
 *
 *   public class ThreadId {
 *       // Atomic integer containing the next thread ID to be assigned
 *       private static final AtomicInteger nextId = new AtomicInteger(0);
 *
 *       // Thread local variable containing each thread's ID
 *       private static final ThreadLocal&lt;Integer&gt; threadId =
 *           new ThreadLocal&lt;Integer&gt;() {
 *              &#64;Override protected Integer initialValue() {
 *                   return nextId.getAndIncrement();
 *           }
 *       };
 *
 *       // Returns the current thread's unique ID, assigning it if necessary
 *       public static int get() {
 *           return threadId.get();
 *       }
 *   }
 * </pre>
 * </p>
 * 只要线程是alive并且{@code ThreadLocal}实例可以被访问，
 * 那么每个线程都将持有线程局部变量副本的隐式引用。在线程消失后，
 * 它的所有线程局部变量的副本都被垃圾收集（除非存在对这些副本的其
 * 他引用）
 * @author 豆豆
 * @date 2019/10/8 14:18
 * @flag 以万物智能，化百千万亿身
 */
public class MyThreadLocal<T> {

    /**
     *
     */
    private final int threadLocalHashCode = nextHashCode();

    /**
     * 下一个要给出的哈希码，自动更新。从0开始
     */
    private static AtomicInteger nextHashCode =
            new AtomicInteger();

    /**
     * 连续生成的哈希码之间的差异，将隐式顺序线程本地ID转换为用
     * 2的幂次方表的近似最佳分布的乘法哈希值
     */
    private static final int HASH_INCREMENT = 0x61c88647;

    /**
     * return the next hash code
     * @return
     */
    private static final int nextHashCode(){
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    /**
     * 返回此线程局部变量的当前线程的“初始值”，此方法将在线程第一次
     * 通过{@link #get}方法访问变量时被调用，除非线程先调用了{@link #set}
     * 方法，在这种情况下{@code initialValue}方法将不会被此线程调用。正常情况下，
     * 此方法最多被每个线程调用一次，但是当在调用{@link #remove}之后调用{@link #get}
     * 时此方法会被再一次调用。
     * <p>此实现简单的返回了{@code null}; 如果编程人员期望线程局部变量有一个初始值而不是
     * {@code null}, {@code ThreadLocal}必须被子类化，并且重写此方法。通常会使用匿名
     * 内部类。
     * </p>
     * @return
     */
    protected T initialValue(){return null;}


    /**
     * 创建一个线程局部变量。变量的初始值由调用Supplier的get方法决定。
     * @param supplier
     * @param <S>
     * @return
     */
    public static <S> MyThreadLocal<S> withInitial(Supplier<? extends S> supplier){
        return new SuppliedThreadLocal<>(supplier);
    }


    /**
     *
     */
    public MyThreadLocal(){
    }

    /**
     * 返回当前线程的线程局部变量的副本。 如果当前线程变量没有值,
     * 则调用initialValue方法初始化该值并返回。
     *
     * @return
     */
    public T get() {
        MyThread t = MyThread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null){
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null){
                @SuppressWarnings("unchecked")
                T result = (T) e.value;
                return result;
            }
        }
        return setInitialValue();
    }

    /**
     *
     * @return
     */
    private T setInitialValue() {
        T value = initialValue();
        MyThread t = MyThread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
        return value;
    }

    /**
     * 设置当前线程的线程局部变量副本的值为指定值。 多数子类无需重写此方法。
     * 只需依赖initialValue方法去设置本地线程变量的值
     * @param value
     */
    public void set(T value){
        MyThread t = MyThread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }

    /**
     * 移除当前线程的线程局部变量的值。 如果此线程局部变量随后被当前线程读取，
     * 它的值将被再一次调用initialValue方法初始化，除非它的值被当前线程通过
     * set方法设置。这将导致在当前线程中多次调用initialValue方法
     */
    public void remove() {
        ThreadLocalMap m = getMap(MyThread.currentThread());
        if (m != null)
            m.remove(this);
    }

    /**
     * 获取与ThreadLocal关联的map。在InheritableThreadLocal中被重写
     * @param t
     * @return
     */
    ThreadLocalMap getMap(MyThread t){
        return t.threadLocals;
    }

    /**
     * 创建与ThreadLocal关联的map。在InheritableThreadLocal中被重写
     * @param t
     * @param firstValue
     */
    void createMap(MyThread t, T firstValue) {
        t.threadLocals = new ThreadLocalMap(this, firstValue);
    }

    /**
     * 工厂方法用于创建集成的thread locals的map。
     * 设计仅用来被Thread的构造器调用。
     * @param parentMap
     * @return
     */
    static ThreadLocalMap createInheritedMap(ThreadLocalMap parentMap){
        return new ThreadLocalMap(parentMap);
    }


    /**
     * 方法childValue显然是在子类InheritableThreadLocal中定义的，
     * 但是这里是在内部定义的，目的是提供createInheritedMap工厂方法，
     * 而不需要在InheritableThreadLocal中子类化map类。这项技术优
     * 于在方法中嵌入instanceof测试。
     * @param parenValue
     * @return
     */
    T childValue(T parenValue){
        throw new UnsupportedOperationException();
    }



    /**
     *
     * ThreadLocal的扩展，从给定的{@code Supplier}获取初始值
     */
    static final class SuppliedThreadLocal<T> extends MyThreadLocal<T> {
        private final Supplier<? extends T> supplier;

        SuppliedThreadLocal(Supplier<? extends T> supplier){
            this.supplier = Objects.requireNonNull(supplier);
        }

        @Override
        protected T initialValue() {
            return supplier.get();
        }
    }

    /**
     * ThreadLocalMap是一个定制的hash map仅适合持有线程局部变量。
     * 没有任何操作被暴露在ThreadLocal类之外。类是包级私有，允许在
     * Thread类中声明为成员变量。为了帮助处理大量且长时间存活的用法，
     * 此hash表条目使用若引用作为key。但是， 但是，由于没有使用引用
     * 队列，所以只有当表开始耗尽空间时，才可以确保删除陈旧的条目。
     */
    static class ThreadLocalMap{


        /**
         *
         */
        static class Entry extends WeakReference<MyThreadLocal<?>> {

            Object value;

            Entry(MyThreadLocal<?> k, Object v){
                super(k);
                value = v;
            }
        }

        private static final int INITIAL_CAPACITY = 16;

        private Entry[] table;

        private int size = 0;

        /**resize的下一个大小阈值*/
        private int threshold;

        private void setThreshold(int len){
            threshold = len * 2 / 3;
        }

        private static int nextIndex(int i, int len){
            return ((i + 1 < len) ? i + 1 : 0);
        }

        private static int prevIndex(int i, int len) {
            return ((i - 1 >= 0) ? i - 1 : len - 1);
        }

        ThreadLocalMap(MyThreadLocal<?> firstKey, Object firstValue) {
            table = new Entry[INITIAL_CAPACITY];
            int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
            table[i] = new Entry(firstKey, firstValue);
            size = 1;
            setThreshold(INITIAL_CAPACITY);
        }


        /**
         *
         * @param parentMap
         */
        ThreadLocalMap(ThreadLocalMap parentMap) {
            Entry[] parentTable = parentMap.table;
            int len = parentTable.length;
            setThreshold(len);
            table = new Entry[len];

            for (int j = 0; j < len; j++){
                Entry e = parentTable[j];
                if (e != null) {
                    MyThreadLocal<Object> key = (MyThreadLocal<Object>) e.get();
                    if (key != null){
                        Object value = key.childValue(e.value);
                        Entry c = new Entry(key, value);
                        int h = key.threadLocalHashCode & (len - 1);
                        while (table[h] != null)
                            h = nextIndex(h, len);
                        table[h] = c;
                        size++;
                    }
                }
            }
        }


        /**
         * 获取与key关联的entry。此方法本身只处理快速路径：直接命中存在的key。
         * 若没有直接命中就会转到getEntryAfterMiss。这是为了最大限度地提高
         * 直接命中的性能而设计的，部分原因是使此方法易于内联。
         *
         * @param key
         * @return
         */
        private Entry getEntry(MyThreadLocal<?> key){
            int i = key.threadLocalHashCode & (table.length - 1);
            Entry e = table[i];
            if (e != null && e.get() == key)
                 return e;
            else
                return getEntryAfterMiss(key, i, e);
        }


        /**
         *
         * @param key
         * @param i
         * @param e
         * @return
         */
        private Entry getEntryAfterMiss(MyThreadLocal<?> key, int i, Entry e){
            Entry[] tab = table;
            int len = tab.length;

            while (e != null) {
                MyThreadLocal<?> k = e.get();
                if (k == key)
                    return e;
                if (k == null)
                    expungeStaleEntry(i);
                else
                    i = nextIndex(i, len);
                e = tab[i];
            }
            return null;
        }

        /**
         * 设置与key关联的值
         * @param key
         * @param value
         */
        private void set(MyThreadLocal<?> key, Object value) {

            Entry[] tab = table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len - 1);

            for (Entry e = tab[i]; e != null;
                 e = tab[i = nextIndex(i, len)]){
                MyThreadLocal<?> k = e.get();

                if (k == key){
                    e.value = value;
                    return;
                }

                if (k == null){
                    replaceStaleEntry(key, value, i);
                    return;
                }
            }

            tab[i] = new Entry(key, value);
            int sz = ++size;
            if (!cleanSomeSlots(i, sz) && sz >= threshold)
                rehash();
        }

        /**
         * 删除对应条目下的key
         * @param key
         */
        private void remove(MyThreadLocal<?> key) {
            Entry[] tab = table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len - 1);
            for (Entry e = tab[i]; e != null;
                 e = tab[i = nextIndex(i, len)]){
                if (e.get() == key) {
                    e.clear();
                    expungeStaleEntry(i);
                    return;
                }

            }
        }


        /**
         * set操作时对遇到的指定key的旧条目使用新值进行替换。
         * 值存储在value参数条目中，无论指定key对应的entry
         * 是否已存在。
         *
         * 此方法有一个副作用，此方法删除了“run”中包含的所有
         * 过期条目（A run is a sequence of entries
         * between two null slots.）
         * @param key
         * @param value
         * @param staleSlot
         */
        private void replaceStaleEntry(MyThreadLocal<?> key, Object value,
                                       int staleSlot){
            Entry[] tab = table;
            int len = tab.length;
            Entry e;

            // Back up to check for prior stale entry in current run.
            // We clean out whole runs at a time to avoid continual
            // incremental rehashing due to garbage collector freeing
            // up refs in bunches (i.e., whenever the collector runs).
            int slotToExpunge = staleSlot;
            for (int i = prevIndex(staleSlot, len);
                 (e = tab[i]) != null;
                 i = prevIndex(i, len))
                if (e.get() == null)
                    slotToExpunge = i;


        }

        private int expungeStaleEntry(int staleSlot){
            Entry[] tab = table;
            int len = tab.length;

            //expunge entry at staleSlot
            tab[staleSlot].value = null;
            tab[staleSlot] = null;
            size--;

            //Rehash until we encounter null

        }

        private boolean cleanSomeSlots(int i, int n){

        }


        /**
         * 重新打包 and/or 调整表的大小。首先扫描整个表，
         * 删除陈旧的条目。如果这还不够缩小表的大小，则将
         * 表的大小加倍。
         */
        private void rehash(){
            expungeStaleEntries();

            //Use lower threshold for doubling to avoid hysteresis
            if (size >= threshold - threshold / 4)
               resize();
        }

        private void resize() {

        }

        private void expungeStaleEntries(){

        }

    }


}
