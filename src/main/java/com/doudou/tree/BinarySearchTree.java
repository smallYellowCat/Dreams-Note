package com.doudou.tree;

import java.nio.BufferUnderflowException;
import java.util.Comparator;

/**
 * ADT/BST  binary search tree
 * @param <T>
 */
public class BinarySearchTree<T extends Comparable<? super T>> {


    /**
     *
     * @param <T>
     */
    private static class BinaryNode<T>{
        /**the data in the node*/
        T e;

        /**Left child*/
        BinaryNode<T> left;

        /**Right child*/
        BinaryNode<T> right;

        BinaryNode(T e){
            this(e, null, null);
        }

        BinaryNode(T e, BinaryNode<T> lt, BinaryNode<T> rt){
            this.e = e;
            this.left = lt;
            this.right = rt;
        }
    }

    private BinaryNode<T> root;
    private Comparator<? super T> cmp;

    public BinarySearchTree(){
        root = null;
    }

    public BinarySearchTree(Comparator<? super T> c){
        root = null;
        cmp = c;
    }

    public void makeEmpty(){
        root = null;
    }

    public boolean isEmpty(){
        return root == null;
    }

    public boolean contains(T e){
        return contains(e, root);
    }

    public T findMin(){
        if (isEmpty())
            throw new BufferUnderflowException();
        return findMin(root).e;
    }

    public T findMax(){
        if (isEmpty())
            throw new BufferUnderflowException();
        return findMax(root).e;
    }

    public void insert(T e){
        root = insert(e, root);
    }

    public void remove(T e){

    }

    public void printTree(){
        printTree(root);
    }


    /**
     *  Internal method to find an item in a subtree
     * @param e is item to search for
     * @param node the node that roots the subtree
     * @return true is the item is found, false otherwise
     */
    private boolean contains(T e, BinaryNode<T> node){
        if (e == null)
            return false;

        //recursive algorithm
        int compareResult = e.compareTo(node.e);
        if (compareResult < 0){
            return contains(e, node.left);
        }else if (compareResult > 0){
            return contains(e, node.right);
        }else {
            //Match
            return true;
        }

        /*
        非递归算法
        while (compareResult != 0){

            if (compareResult > 0){
                if (node.right == null)
                    return false;
                compareResult = e.compareTo(node.right.e);
                node = node.right;
            }else {
                if (node.left == null)
                    return false;
                compareResult = e.compareTo(node.left.e);
                node=node.left;
            }

        }
        return true;*/

        //使用一个函数对象，传入比较规则，而不是要求书的节点都是Comparable的

        /*if (e == null)
            return false;

        //recursive algorithm
        int compareResult = myCompare(e,node.e);
        if (compareResult < 0){
            return contains(e, node.left);
        }else if (compareResult > 0){
            return contains(e, node.right);
        }else {
            //Match
            return true;
        }*/


    }

    private int myCompare(T lhs, T rhs){
        if (cmp != null)
            return cmp.compare(lhs, rhs);
        else
            return lhs.compareTo(rhs);
    }

    /**
     * Internal method to find the smallest item in a
     * subtree.
     * @param node The node that roots the subtree.
     * @return node containing the smallest item
     */
    private BinaryNode<T> findMin(BinaryNode<T> node){
        //recursive algorithm
        if (node == null)
            return null;
        else if (node.left == null)
            return node;
        return findMin(node);

    }

    /**
     * Internal method to find the largest item in a subtree.
     * @param node the node that roots the subtree.
     * @return node containing the largest item
     */
    private BinaryNode<T> findMax(BinaryNode<T> node) {
        //non-recursive algorithm
        if (node != null)
            while (node.right != null)
                node = node.right;
        return node;
    }

    /**
     * Internal method to insert into a subtree
     * @param e the item to insert
     * @param node the node that roots the subtree
     * @return the new root of the subtree
     */
    private BinaryNode<T> insert(T e, BinaryNode<T> node){
        if (node == null)
            return new BinaryNode<>(e, null, null);
        int compareResult = e.compareTo(node.e);
        if (compareResult < 0)
            node.left = insert(e, node.left);
        else if (compareResult > 0)
            node.right = insert(e, node.right);
        else
            ;//Duplicate; do nothing
        return node;
    }

    /**
     * Internal Method to remove from a subTree
     * @param e the item to remove
     * @param node the node that roots the subtree
     * @return the new root of the subtree
     */
    private BinaryNode<T> remove(T e, BinaryNode<T> node){
        if (root == null) return null;
        int compareResult = e.compareTo(root.e);

        if (compareResult < 0)
            root.left = remove(e, root.left);
        else if (compareResult > 0)
            root.right = remove(e, root.right);
        else if (root.left != null && root.right != null){
            //tow child
            root.e = findMin(root.right).e;
            root.right = remove(root.e, root.right);
        }
        else
            root = (root.left != null) ? root.left : root.right;

        return root;
    }

    private void printTree(BinaryNode<T> node){

    }




}
