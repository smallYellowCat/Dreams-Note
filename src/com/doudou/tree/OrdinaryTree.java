package com.doudou.tree;

public class OrdinaryTree<E> {

    private TreeNode<E> treeNode;
    private int depth;
    private int hight;

    private TreeNode<E> root;

    public OrdinaryTree(TreeNode<E> treeNode){
        this.treeNode = treeNode;
        this.root = treeNode;
        this.depth = 0;
        this.hight = 0;

    }

    public TreeNode<E> getRoot(){
        return this.root;
    }

    public void insert(TreeNode<E> parent, TreeNode<E> node){
        node.setParenNode(parent);
        if (parent.getFirstChild() == null){
            parent.setFirstChild(node);
        }else if (parent.getNextSibling() == null){
            parent.setNextSibling(node);
        }else{
            TreeNode<E> nextSibling = parent.getNextSibling();
            insert(nextSibling, node);
        }
    }

    //public
}
