package com.doudou.ds.link;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
*单链表
*作者：豆豆
*时间:2017/10/13
*/
@Setter
@Getter
public class SingalLink {

    private Node head;

    public boolean insert(int data){
        /*boolean result = false;
        Node node = new Node(data);
        if (head == null){
            head.setRef(node);
            result = true;
        }else {
            //每次都从头插入
            node.setRef(head.getRef());
            head.setRef(node.getRef());
            return true;
        }
        return result;*/
        return true;
    }

    //其他类似

}

@Setter
@Getter
@NoArgsConstructor
class Node {
    private int data; //数据域
    private Node ref; //指针域

    public Node(int data){
        this.data = data;
    }
}
