package com.doudou.leetcode;

import java.util.*;

public class FourMainTest {

    /**
     * 入口函数，自动生成
     *
     * @author 豆豆
     * @date 2019/8/14 19:29a
     */
    public static void main(String[] args) {
        int[] n = {2, 1, 5, 6, 2, 3};
        ListNode head = new ListNode(2);
        head.next = new ListNode(1);
        //head.next.next = new ListNode(3);
        //head.next.next.next = new ListNode(2);
        //head.next.next.next.next = new ListNode(5);
        //head.next.next.next.next.next = new ListNode(2);
        //largestRectangleArea(n);
        //partition(head, 2);
        //isScramble("great", "rgeat");
        //grayCode(4);
        //numDecodings("27");
        FourMainTest fourMainTest = new FourMainTest();
        fourMainTest.restoreIpAddresses("25525511135");
    }

    /**
     * leetcode 84
     * 柱状图中最大的矩形，给定 n 个非负整数，用来表示柱状图中各个柱子的高度。每个柱子彼此相邻，且宽度为 1 。
     * 求在该柱状图中，能够勾勒出来的矩形的最大面积。
     * eg.
     * 输入: [2,1,5,6,2,3]
     * 输出: 10
     *
     * 思路：动态规划，
     * @param heights
     * @return
     */
    /*public static int largestRectangleArea(int[] heights) {
        //时间复杂度太高
        if (heights.length == 0) return 0;
        if (heights.length == 1) return heights[0];

        int area = 0;
        for (int i = 0; i < heights.length; i++){
            int min = heights[i];
            for (int j = i + 1; j < heights.length; j++){
                min = Math.min(min, heights[j]);
                area = Math.max(area, (j-i+1)*min);
            }
            area = Math.max(area, heights[i]);
        }
        return area;
    }*/


    /**
     * 压栈解决最大柱状图的问题 leetcode 84
     *
     * @param heights
     * @return
     */
    public int largestRectangleArea(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        int maxarea = 0;
        for (int i = 0; i < heights.length; ++i) {
            while (stack.peek() != -1 && heights[stack.peek()] >= heights[i])
                maxarea = Math.max(maxarea, heights[stack.pop()] * (i - stack.peek() - 1));
            stack.push(i);
        }
        //此时应该是把最后几个最小的切升序的抛出来计算面积
        while (stack.peek() != -1)
            maxarea = Math.max(maxarea, heights[stack.pop()] * (heights.length - stack.peek() - 1));
        return maxarea;
    }

    /**
     * 快乐数
     *
     * @param n
     * @return
     */
    public static boolean isHappy(int n) {
        Set<Integer> rs = new HashSet<>();
        while (n != 1) {
            rs.add(n);
            int t = 0;
            while (n != 0) {
                t += (n % 10) * (n % 10);
                n = n / 10;
            }
            if (rs.contains(t)) return false;
            n = t;
        }
        return true;
    }


    /**
     * 删除链表中给定的值
     *
     * @param head
     * @param val
     * @return
     */
    public static ListNode removeElements(ListNode head, int val) {
        ListNode node = new ListNode(-1);
        node.next = head;
        ListNode p = node;
        while (p.next != null) {
            if (p.next.val == val) {
                p.next = p.next.next;
            } else {
                p = p.next;
            }
        }
        return node.next;
    }


    /**
     * 统计质数
     *
     * @param n
     * @return
     */
    public static int countPrimes(int n) {
        int[] rs = new int[n];
        int count = 0;
        for (int i = 2; i <= n / 2; i++)
            if (rs[i] == 0) {
                count++;
                for (int j = 2; i * j < n; j++) {

                    rs[i * j] = 1;
                }
            }
        return count;
    }

    public int maximalRectangle(char[][] matrix) {
        if (matrix.length == 0) return 0;
        int m = matrix.length;
        int n = matrix[0].length;

        int[] left = new int[n]; // initialize left as the leftmost boundary possible
        int[] right = new int[n];
        int[] height = new int[n];

        Arrays.fill(right, n); // initialize right as the rightmost boundary possible

        int maxarea = 0;
        for (int i = 0; i < m; i++) {
            int cur_left = 0, cur_right = n;
            // update height
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == '1') height[j]++;
                else height[j] = 0;
            }
            // update left
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == '1') left[j] = Math.max(left[j], cur_left);
                else {
                    left[j] = 0;
                    cur_left = j + 1;
                }
            }
            // update right
            for (int j = n - 1; j >= 0; j--) {
                if (matrix[i][j] == '1') right[j] = Math.min(right[j], cur_right);
                else {
                    right[j] = n;
                    cur_right = j;
                }
            }
            // update area
            for (int j = 0; j < n; j++) {
                maxarea = Math.max(maxarea, (right[j] - left[j]) * height[j]);
            }
        }
        return maxarea;
    }


    /**
     * 给定一个链表和一个特定值 x，对链表进行分隔，使得所有小于 x 的节点都在大于或等于 x 的节点之前。
     * <p>
     * 你应当保留两个分区中每个节点的初始相对位置。
     * <p>
     * 示例:
     * <p>
     * 输入: head = 1->4->3->2->5->2, x = 3
     * 输出: 1->2->2->4->3->5
     *
     * @param head
     * @param x
     * @return
     */
    public static ListNode partition(ListNode head, int x) {
        if (head == null) return head;
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode pre = head;
        ListNode preh = dummy;
        while (pre.next != null) {
            if (pre.val < x) {
                pre = pre.next;
                preh = preh.next;
            } else if (pre.next.val >= x) {
                pre = pre.next;
            } else {
                ListNode preNext = pre.next;
                pre.next = pre.next.next;
                ListNode headTmp = preh.next;
                preh.next = preNext;
                preNext.next = headTmp;
                preh = preh.next;
            }
        }

        return dummy.next;
    }


    /**
     * 分割链表的官方解法
     *
     * @param head
     * @param x
     * @return
     */
    public ListNode partition2(ListNode head, int x) {

        // before and after are the two pointers used to create the two list
        // before_head and after_head are used to save the heads of the two lists.
        // All of these are initialized with the dummy nodes created.
        ListNode before_head = new ListNode(0);
        ListNode before = before_head;
        ListNode after_head = new ListNode(0);
        ListNode after = after_head;

        while (head != null) {

            // If the original list node is lesser than the given x,
            // assign it to the before list.
            if (head.val < x) {
                before.next = head;
                before = before.next;
            } else {
                // If the original list node is greater or equal to the given x,
                // assign it to the after list.
                after.next = head;
                after = after.next;
            }

            // move ahead in the original list
            head = head.next;
        }

        // Last node of "after" list would also be ending node of the reformed list
        after.next = null;

        // Once all the nodes are correctly assigned to the two lists,
        // combine them to form a single list which would be returned.
        before.next = after_head.next;

        return before_head.next;
    }

    /**
     * leetcode 87，扰乱字符串
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean isScramble(String s1, String s2) {
        if (s1 == null || s2 == null) return false;
        if (s1.length() != s2.length()) return false;
        if (s1.equals(s2)) return true;
        int[] letters = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            letters[s1.charAt(i) - 'a']++;
            letters[s2.charAt(i) - 'a']--;
        }
        for (int i = 0; i < 26; i++) {
            if (letters[i] != 0) return false;
        }
        for (int i = 1; i < s1.length(); i++) {
            if (isScramble(s1.substring(0, i), s2.substring(0, i)) && isScramble(s1.substring(i), s2.substring(i)))
                return true;
            if (isScramble(s1.substring(0, i), s2.substring(s2.length() - i)) && isScramble(s1.substring(i), s2.substring(0, s2.length() - i)))
                return true;
        }
        return false;
    }

    /**
     * leetcode 88
     * 合并两个有序数组
     *
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int p1 = m - 1, p2 = n - 1, L = nums1.length - 1;
        while (L >= 0 && p2 >= 0 && p1 >= 0) {
            if (nums1[p1] > nums2[p2]) {
                nums1[L--] = nums1[p1--];
            } else {
                nums1[L--] = nums2[p2--];
            }
        }

        if (p1 == -1) {
            while (p2 >= 0)
                nums1[L--] = nums2[p2--];
        }
    }


    /**
     * 格雷编码, leetcode 89
     *
     * @param n
     * @return
     */
    public static List<Integer> grayCode(int n) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < Math.pow(2, n); i++) {
            res.add((i >> 1) ^ i);
        }
        return res;
    }

    /**
     * leetcode 90
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> rs = new ArrayList<>();
        Arrays.sort(nums);
        backtrack(rs, 0, nums, new ArrayList<>());
        return rs;
    }

    private static void backtrack(List<List<Integer>> rs, int i, int[] nums, List<Integer> list) {
        //if (!rs.contains(list))
        rs.add(new ArrayList<>(list));
        for (int j = i; j < nums.length; j++) {
            if (j > i && nums[j] == nums[j - 1])
                continue;
            list.add(nums[j]);
            backtrack(rs, j + 1, nums, list);
            list.remove(list.size() - 1);
        }
    }


    /**
     * leetcode 91 解码, 数字解码
     *
     * @param s
     * @return
     */
    public static int numDecodings(String s) {
        if (s.charAt(0) == '0') return 0;
        int dp[] = new int[s.length() + 1];
        dp[0] = dp[1] = 1;
        for (int i = 2; i <= s.length(); i++) {
            if (s.charAt(i - 1) != '0') {
                dp[i] += dp[i - 1];
            }

            if (s.charAt(i - 2) == '1'
                    || (s.charAt(i - 2) == '2' && s.charAt(i - 1) <= '6')) {
                dp[i] += dp[i - 2];
            }
        }
        return dp[s.length()];
    }

    /**
     *反转从位置 m 到 n 的链表。请使用一趟扫描完成反转。
     *
     * 说明:
     * 1 ≤ m ≤ n ≤ 链表长度。
     *
     * 示例:
     *
     * 输入: 1->2->3->4->5->NULL, m = 2, n = 4
     * 输出: 1->4->3->2->5->NULL
     *
     * @param head
     * @param m
     * @param n
     * @return
     */
    public ListNode reverseBetween(ListNode head, int m, int n) {
        if (head == null || m == n || head.next == null) return head;
        ListNode cur = head;
        ListNode pre = null;
        while (m > 1){
            pre = cur;
            cur = cur.next;
            m--;
            n--;
        }


        ListNode con = pre, tail = cur;
        ListNode third = null;
        while (n > 0){
            third = cur.next;
            cur.next = pre;
            pre = cur;
            cur = third;
            n--;
        }

        if (con != null){
            con.next = pre;
        }else {
            //解决m=1的情况
            head = pre;
        }

        tail.next = cur;
        return head;
    }

    /**
     * 复原ip地址
     * @param s
     * @return
     */
    int n;
    String s;
    LinkedList<String> segments = new LinkedList<String>();
    ArrayList<String> output = new ArrayList<String>();

    public boolean valid(String segment) {
    /*
    Check if the current segment is valid :
    1. less or equal to 255
    2. the first character could be '0'
    only if the segment is equal to '0'
    */
        int m = segment.length();
        if (m > 3)
            return false;
        return (segment.charAt(0) != '0') ? (Integer.valueOf(segment) <= 255) : (m == 1);
    }

    public void update_output(int curr_pos) {
    /*
    Append the current list of segments
    to the list of solutions
    */
        String segment = s.substring(curr_pos + 1, n);
        if (valid(segment)) {
            segments.add(segment);
            output.add(String.join(".", segments));
            segments.removeLast();
        }
    }

    public void backtrack(int prev_pos, int dots) {
    /*
    prev_pos : the position of the previously placed dot
    dots : number of dots to place
    */
        // The current dot curr_pos could be placed
        // in a range from prev_pos + 1 to prev_pos + 4.
        // The dot couldn't be placed
        // after the last character in the string.
        int max_pos = Math.min(n - 1, prev_pos + 4);
        for (int curr_pos = prev_pos + 1; curr_pos < max_pos; curr_pos++) {
            String segment = s.substring(prev_pos + 1, curr_pos + 1);
            if (valid(segment)) {
                segments.add(segment);  // place dot
                if (dots - 1 == 0)      // if all 3 dots are placed
                    update_output(curr_pos);  // add the solution to output
                else
                    backtrack(curr_pos, dots - 1);  // continue to place dots
                segments.removeLast();  // remove the last placed dot
            }
        }
    }

    public List<String> restoreIpAddresses(String s) {
        n = s.length();
        this.s = s;
        backtrack(-1, 3);
        return output;
    }


    /**
     * leetcode 95 不同的二叉搜索树
     * @param start
     * @param end
     * @return
     */
    public LinkedList<TreeNode> generate_trees(int start, int end) {
        LinkedList<TreeNode> all_trees = new LinkedList<>();
        if (start > end) {
            all_trees.add(null);
            return all_trees;
        }

        // pick up a root
        for (int i = start; i <= end; i++) {
            // all possible left subtrees if i is choosen to be a root
            LinkedList<TreeNode> left_trees = generate_trees(start, i - 1);

            // all possible right subtrees if i is choosen to be a root
            LinkedList<TreeNode> right_trees = generate_trees(i + 1, end);

            // connect left and right trees to the root i
            for (TreeNode l : left_trees) {
                for (TreeNode r : right_trees) {
                    TreeNode current_tree = new TreeNode(i);
                    current_tree.left = l;
                    current_tree.right = r;
                    all_trees.add(current_tree);
                }
            }
        }
        return all_trees;
    }

    public List<TreeNode> generateTrees(int n) {
        if (n == 0) {
            return new LinkedList<>();
        }
        return generate_trees(1, n);
    }

    /**
     * 判断是否是相同的树, leetcode 100
     * @param p
     * @param q
     * @return
     */
    public boolean isSameTree(TreeNode p, TreeNode q) {
        return check(p, q);
    }

    boolean check(TreeNode p, TreeNode q){
        if (p == null && q == null) return true;
        if (q == null  ||  p == null) return false;
        if (p.val != q.val) return false;
        return check(p.left, q.left) && check(p.right, q.right);
    }

    /**
     * 检查二叉树是否镜像对称
     * @param root
     * @return
     */
    public boolean isSymmetric(TreeNode root) {
        if (root == null) return true;
        return isSymmetric(root.right)&&isSymmetric(root.left);
    }

    public boolean checkSymmetric(TreeNode left, TreeNode right){
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;
        if (left.val != right.val) return false;
        return checkSymmetric(left.left, right.right) && checkSymmetric(left.right, right.left);
    }



}