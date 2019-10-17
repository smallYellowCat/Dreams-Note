package com.doudou.plan.database;

import com.sun.org.apache.regexp.internal.RE;

/**
 * AVL平衡二叉树的变种，在最坏的情况下操作花费O(log N)时间。
 *
 * 红黑树是具有以下着色性质的二叉查找树：
 * 1. 每一个节点要么着色为红色，要么着色为黑色。
 * 2. 根是黑色的
 * 3. 如果一个节点是红色的，那么它的子节点必须是黑色的
 * 4. 从一个节点到一个null引用的每一条路径必须包含相同数目的黑色节点
 *
 * 着色法则的一个结论是，红黑树的高度最多是2log(N+1)。因此查找操作
 * 保证是一种对数的操作。
 * @author 豆豆
 * @date 2019/10/10 18:53
 * @flag 以万物智能，化百千万亿身
 */
public class RedBlackTree<E extends Comparable<? super E>> {
    /**Black must be 1*/
    private static final int BLACK = 1;

    private static final int RED = 0;

    private RedBlackNode<E> header;

    private RedBlackNode<E> nullNode;


    //Used in insert routine and its helpers
    private RedBlackNode<E> current;
    private RedBlackNode<E> parent;
    private RedBlackNode<E> grand;
    private RedBlackNode<E> great;

    public RedBlackTree(){
        nullNode = new RedBlackNode<>(null);
        nullNode.left = nullNode.right = nullNode;
        header = new RedBlackNode<>(null);
        header.left = header.right = nullNode;
    }

    public void printTree(){

    }

    /**
     * Internal method to print a subtree in sorted order
     * @param t
     */
    private void printTree(RedBlackNode<E> t){
        if (t != nullNode){
            printTree(t.left);
            System.out.println(t.element);
            printTree(t.right);
        }

    }

    private RedBlackNode<E> rotate(E item, RedBlackNode<E> parent){
        if (compare(item, parent) < 0)
            return parent.left = compare(item, parent.left) < 0 ?

    }

    /**
     * @param item
     * @param t
     * @return
     */
    private final int compare(E item, RedBlackNode<E> t){
        if (t == header)
            return 1;
        else
            return item.compareTo(t.element);
    }


    /**
     * Internal routine that is called during an insertion
     * if a node has two red children. Performs flip and
     * rotations.
     * @param item
     */
    private void handleReorient(E item) {
        //Do the color flip
        current.color = RED;
        current.left.color = BLACK;
        current.right.color = BLACK;

        if (parent.color == RED) { //Have to rotate
            grand.color = RED;
            if ((compare(item, grand) < 0) !=
                (compare(item, parent) < 0))
                parent = rotate(item, great);
            current.color = BLACK;
        }
        //Make root black
        header.right.color = BLACK;
    }


    /**
     * Insert into the tree
     * @param item
     */
    public void insert(E item){
        current = parent = grand = header;
        nullNode.element = item;

        while (compare(item, current) != 0){
            great = grand; grand = parent; parent = current;
            current = compare(item, current) < 0 ? current.left : current.right;

            //Check if two red children; fix if so
            if (current.left.color == RED && current.right.color == RED)
                handleReorient(item);
        }

        //Insertion fail if already present
        if (current != nullNode)
            return;
        current = new RedBlackNode<>(item, nullNode, nullNode);

        //Attach to parent
        if (compare(item, parent) < 0)
            parent.left = current;
        else
            parent.right = current;
        handleReorient(item);

    }

    private static class RedBlackNode<E> {

        E element;
        RedBlackNode<E> left;
        RedBlackNode<E> right;
        int             color;

        RedBlackNode(E theElement){
            this(theElement, null, null);
        }

        RedBlackNode(E theElement, RedBlackNode<E> lt,
                     RedBlackNode<E> rt){
            element = theElement;
            left = lt;
            right = rt;
            color = BLACK;
        }
    }
}
