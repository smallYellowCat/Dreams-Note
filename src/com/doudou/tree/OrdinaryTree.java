package com.doudou.tree;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
*普通树
*作者：豆豆
*时间:2017/10/16
*/
public class OrdinaryTree {
    private Node root;


}

@Getter
@Setter
class Node{
    private Object data; //数据域
    private Node father; //父节点
    private List<Node> son; //子节点
}