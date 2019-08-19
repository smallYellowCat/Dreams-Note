package com.doudou.leetcode;

import java.util.ArrayList;
import java.util.List;

public class FiveMainTest {

    /**
     *入口函数，自动生成
     * @author 豆豆
     * @date 2019/8/19 21:24a
    */
    public static void main(String[] args){

    }


    /**
     * leetcode 103
     *
     * 给定一个二叉树，返回其节点值的锯齿形层次遍历。（即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）。
     *
     * 例如：
     * 给定二叉树 [3,9,20,null,null,15,7],
     *
     *     3
     *    / \
     *   9  20
     *     /  \
     *    15   7
     * 返回锯齿形层次遍历如下：
     *
     * [
     *   [3],
     *   [20,9],
     *   [15,7]
     * ]
     *
     * @param root
     * @return
     */

    List<List<Integer>> levels = new ArrayList<>();
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        if (root == null) return levels;
        helper(root,0);
        return levels;
    }


    void helper(TreeNode node, int level){
        if (levels.size() == level) levels.add(new ArrayList<>());
        if (level%2 == 0){
            levels.get(level).add(node.val);

        }else {
            levels.get(level).add(0, node.val);
        }
        if (node.left != null)helper(node.left, level + 1);
        if (node.right != null)helper(node.right, level + 1);
    }


    /**
     * leetcode 104 二叉树的最大深度
     * DFS深度遍历
     * @param root
     * @return
     */
    public int maxDepth(TreeNode root) {

        if (root == null){

            return 0;
        } else {
            int left = maxDepth(root.left);
            int right = maxDepth(root.right);
            return Math.max(left, right) + 1;
        }

    }


}
