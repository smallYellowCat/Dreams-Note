package com.doudou.tree;

/**
 * @author 豆豆
 * @date 2019/7/23 14:26
 * @flag 以万物智能，化百千万亿身
 */
public class TreeNode<E> {
    private TreeNode<E> parenNode;
    private TreeNode<E> firstChild;
    private TreeNode<E> nextSibling;
    private E element;
    private final int hash;

    public TreeNode(int  hash, TreeNode<E> parenNode, TreeNode<E> firstChild, TreeNode<E> nextSibling, E element){
        this.parenNode = parenNode;
        this.firstChild = firstChild;
        this.nextSibling = nextSibling;
        this.element = element;
        this.hash = hash;
    }

    public boolean isRoot(){
        return this.parenNode == null;
    }


    @Override
    public int hashCode() {
        return (element.hashCode() ^ parenNode.hashCode()) ^(firstChild.hashCode() ^ nextSibling.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        TreeNode<E> n;
        return ((obj instanceof TreeNode)
                && ((n = (((TreeNode<E>) obj))).getParenNode()) == parenNode
                && (n.firstChild == firstChild)
                && (n.nextSibling == nextSibling));
    }

    public int getHash() {
        return hash;
    }

    public TreeNode<E> getParenNode() {
        return parenNode;
    }

    public void setParenNode(TreeNode<E> parenNode) {
        this.parenNode = parenNode;
    }

    public TreeNode<E> getFirstChild() {
        return firstChild;
    }

    public void setFirstChild(TreeNode<E> firstChild) {
        this.firstChild = firstChild;
    }

    public TreeNode<E> getNextSibling() {
        return nextSibling;
    }

    public void setNextSibling(TreeNode<E> nextSibling) {
        this.nextSibling = nextSibling;
    }

    public E getElement() {
        return element;
    }

    public void setElement(E element) {
        this.element = element;
    }
}
