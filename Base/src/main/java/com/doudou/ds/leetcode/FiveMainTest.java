package com.doudou.ds.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
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


    /**
     * leetcode 96 计算不同的二叉搜索树
     * @param n
     * @return
     */
    public int numTrees(int n) {
        int[] G = new int[n + 1];
        G[0] = 1;
        G[1] = 1;

        for (int i = 2; i <= n; ++i) {
            for (int j = 1; j <= i; ++j) {
                G[i] += G[j - 1] * G[i - j];
            }
        }
        return G[n];
    }


    /**
     * leetcode 105
     * 根据中序遍历和前序遍历恢复一颗二叉树，假设没有重复元素
     *
     * 思路：左右递归构造根，因为前序遍历的第一个元素是根节点，
     * 同时根节点将中序遍历分为左右两个子树。
     * @param preorder
     * @param inorder
     * @return
     */
    // start from first preorder element
    int pre_idx = 0;
    int[] preorder;
    int[] inorder;
    HashMap<Integer, Integer> idx_map = new HashMap<>();

    public TreeNode helper(int in_left, int in_right) {
        // if there is no elements to construct subtrees
        if (in_left == in_right)
            return null;

        // pick up pre_idx element as a root
        int root_val = preorder[pre_idx];
        TreeNode root = new TreeNode(root_val);

        // root splits inorder list
        // into left and right subtrees
        int index = idx_map.get(root_val);

        // recursion
        pre_idx++;
        // build left subtree
        root.left = helper(in_left, index);
        // build right subtree
        root.right = helper(index + 1, in_right);
        return root;
    }

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        this.preorder = preorder;
        this.inorder = inorder;

        // build a hashmap value -> its index
        int idx = 0;
        for (Integer val : inorder)
            idx_map.put(val, idx++);
        return helper(0, inorder.length);
    }




}
