package com.doudou.ds.leetcode;


import java.util.*;

public class SecondMainTest {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    /**
     *入口函数，自动生成
     * @author 豆豆
     * @date 2019/7/26 14:52a
    */
    public static void main(String[] args){
        int[] nums = {1,3,5,6};
        //removeElement(nums, 3);
        //strStr("aaa", "aaa");
        //divide(-2147483648, 1);
        String[] p = {"foo","bar"};
        String s = "barfoothefoobarman";
        int[][] n = {{1,2,3},{1,5,1},{4,2,1}};
       // merge(n);
        //findSubstring(s, p);
        //searchRange(nums, 6);
        //searchInsert(nums, 7);
        //generateMatrix(3);
        //getPermutation(9, 3);
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(2);
        listNode.next.next = new ListNode(3);
        listNode.next.next.next = new ListNode(4);
        listNode.next.next.next.next = new ListNode(5);
        //rotateRight(listNode, 2);
        //minPathSum(n);

        ListNode pre = listNode;
        pre.next = null;
        System.out.println("");


    }


    /**
     * k个链表有序合并
     * @param lists
     * @return
     */
    public ListNode mergeKLists(ListNode[] lists) {
        if(lists.length == 0) return null;
        ListNode dummy = new ListNode(0);
        dummy = merge(0, lists.length - 1, lists);
        return dummy;
    }
    //
    public ListNode merge(int start, int end, ListNode[] lists) {
        int mid = start + (end - start)/2;
        if(start == end) return lists[start];
        ListNode l1 = merge(start, mid, lists);
        ListNode l2 = merge(mid+1, end, lists);
        return merge2Lists(l1, l2);
    }
    public ListNode merge2Lists(ListNode l1, ListNode l2) {
        if(l1 == null) return l2;
        if(l2 == null) return l1;
        ListNode head  = new ListNode(0);
        if(l1.val <= l2.val) {
            head = l1;
            head.next = merge2Lists(l1.next, l2);
        } else {
            head = l2;
            head.next = merge2Lists(l2.next, l1);
        }
        return head;
    }


    /**
     * 链表相邻元素两两互换
     * @param head
     * @return
     */
    public ListNode swapPairs(ListNode head) {
        //递归
        /*if(head == null || head.next == null){
            return head;
        }
        ListNode next = head.next;
        head.next = swapPairs(next.next);
        next.next = head;
        return next;*/

        //非递归

        ListNode prevHead = new ListNode(-1);
        prevHead.next = head;
        ListNode prev = prevHead;

        while (prev.next != null && prev.next.next != null){
            ListNode temp1 = prev.next;
            ListNode temp2 = prev.next.next;

            prev.next = temp2;

            temp1.next = temp2.next;
            temp2.next = temp1;

            //移动指针
            prev = temp1;
        }

        return prevHead.next;

    }

    /**
     * k组交换，相邻交换的升级版
     * @param head
     * @param k
     * @return
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode pre = dummy;
        ListNode end = dummy;

        while (end.next != null) {
            for (int i = 0; i < k && end != null; i++) end = end.next;
            if (end == null) break;
            ListNode start = pre.next;
            ListNode next = end.next;
            end.next = null;
            pre.next = reverse(start);
            start.next = next;
            pre = start;

            end = pre;
        }
        return dummy.next;

    }

    private ListNode reverse(ListNode head) {
        ListNode pre = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = pre;
            pre = curr;
            curr = next;
        }
        return pre;

    }

    /**
     * 双指针法， 在原数组删除重复
     * @param nums
     * @return
     */
    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;

    }

    static int removeElement(int[] nums, int val) {
        if (nums.length == 0) return 0;
        int i = 0;
        for (int j = 0; j < nums.length; j++){
            if (nums[j] != val){
                nums[i] = nums[j];
                i++;
            }
        }
        return i;
    }

    /**
     * 实现idexof，，下面的代码还没有实现
     * @param haystack
     * @param needle
     * @return
     */
    static int strStr(String haystack, String needle) {
        //haystack.indexOf(needle);
        if (needle == null) return -1;
        if (needle.isEmpty()) return 0;
        int start = -1, i = 0;
        for (char c : needle.toCharArray()){
            for (int j = 0; j < haystack.length(); j++){
                if (haystack.charAt(j) == c){
                    if (start == -1){
                        start = j;
                        break;
                    }
                    i++;
                    break;
                }
            }
        }

        if (start != -1 && i - start >=0 && i - start == needle.length() - 1){
            return start;
        }else {
            return -1;
        }
    }

    /**
     * 两数相除，取整数
     * @param dividend
     * @param divisor
     * @return
     */
    static int divide(int dividend, int divisor) {
        boolean isNav = false;
        long l1 = dividend, l2 = divisor;
        if (dividend == 0 && divisor != 0) return 0;
        if (dividend > 0 && divisor < 0){
            l2 = -l2;
            isNav = true;
        }

        if (dividend < 0 && divisor > 0){
            l1 = -l1;
            isNav = true;
        }

        if(dividend < 0 && divisor < 0){
            l1 = -l1;
            l2 = -l2;
        }

        int i = 0;
        while (l1 >= l2){
            if (l1 == Integer.MAX_VALUE && l2 == 1){
                return isNav? -Integer.MAX_VALUE : Integer.MAX_VALUE;
            }
            if (-l1 == Integer.MIN_VALUE && l2 == 1){
                return isNav? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }
            l1 = l1 -l2;
            if (i == Integer.MAX_VALUE - 1){
                return isNav? -Integer.MAX_VALUE : Integer.MAX_VALUE;
            }
            i++;
        }

        return isNav? - i : i;

    }

    static List<Integer> findSubstring(String s, String[] words) {
        String pattern = "";
        StringBuilder sb = new StringBuilder();
        for (String str : words){
            sb.append(str);
        }

        pattern = sb.toString();
        List<Integer> rs = new ArrayList<>();
        rs.add(0);
        pattern(s, pattern, rs);
        rs.remove(0);
        return rs;
    }

    static void pattern(String s, String p, List<Integer> rs){
        if(s.contains(p)){
            int i = s.indexOf(p);
            rs.add(i + rs.get(rs.size() - 1));
            pattern(s.substring (p.length()), p, rs);
        }
    }

    /**
     * 最长有效括号个数
     * @param s
     * @return
     */
    public int longestValidParentheses(String s) {
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        int count = 0;
        for (int i = 0; i < s.length(); i++){
            if (s.charAt(i) == '('){
                stack.push(i);
            }else {
                stack.pop();
                if (stack.isEmpty()){
                    stack.push(i);
                }

                count = Math.max(count, i - stack.peek());
            }
        }

        return count;
    }




    /**
     * 假设按照升序排序的数组在预先未知的某个点上进行了旋转。
     *
     * ( 例如，数组 [0,1,2,4,5,6,7] 可能变为 [4,5,6,7,0,1,2] )。
     *
     * 搜索一个给定的目标值，如果数组中存在这个目标值，则返回它的索引，否则返回 -1 。
     *
     * 你可以假设数组中不存在重复的元素。
     *
     * 你的算法时间复杂度必须是 O(log n) 级别。
     *
     * 示例 1:
     *
     * 输入: nums = [4,5,6,7,0,1,2], target = 0
     * 输出: 4
     * 示例 2:
     *
     * 输入: nums = [4,5,6,7,0,1,2], target = 3
     * 输出: -1
     *
     * @param nums
     * @param target
     * @return
     */
    int [] nums;
    int target;

    public int find_rotate_index(int left, int right) {
        if (nums[left] < nums[right])
            return 0;

        while (left <= right) {
            int pivot = (left + right) / 2;
            if (nums[pivot] > nums[pivot + 1])
                return pivot + 1;
            else {
                if (nums[pivot] < nums[left])
                    right = pivot - 1;
                else
                    left = pivot + 1;
            }
        }
        return 0;
    }

    public int search(int left, int right) {
    /*
    Binary search
    */
        while (left <= right) {
            int pivot = (left + right) / 2;
            if (nums[pivot] == target)
                return pivot;
            else {
                if (target < nums[pivot])
                    right = pivot - 1;
                else
                    left = pivot + 1;
            }
        }
        return -1;
    }

    public int search(int[] nums, int target) {
        this.nums = nums;
        this.target = target;

        int n = nums.length;

        if (n == 0)
            return -1;
        if (n == 1)
            return this.nums[0] == target ? 0 : -1;

        int rotate_index = find_rotate_index(0, n - 1);

        // if target is the smallest element
        if (nums[rotate_index] == target)
            return rotate_index;
        // if array is not rotated, search in the entire array
        if (rotate_index == 0)
            return search(0, n - 1);
        if (target < nums[0])
            // search in the right side
            return search(rotate_index, n - 1);
        // search in the left side
        return search(0, rotate_index);
    }

    /**
     * 给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。
     *
     * 你的算法时间复杂度必须是 O(log n) 级别。
     *
     * 如果数组中不存在目标值，返回 [-1, -1]。
     *
     * 示例 1:
     *
     * 输入: nums = [5,7,7,8,8,10], target = 8
     * 输出: [3,4]
     * 示例 2:
     *
     * 输入: nums = [5,7,7,8,8,10], target = 6
     * 输出: [-1,-1]
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/find-first-and-last-position-of-element-in-sorted-array
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * @param nums
     * @param target
     * @return
     */
    static int[] searchRange(int[] nums, int target) {
        int left = binarySearch(nums, target, true);
        int[] rs = {-1, -1};
        if (left == nums.length || nums[left] != target){
            return rs;
        }

        rs[0] = left;
        rs[1] = binarySearch(nums, target, false)-1;
        return rs;

    }


    /**
     * 二分查找返回最左边或是最右边的下标，left参数控制返回的是左边还是右边
     * @param nums
     * @param left
     * @return
     */
    public static int binarySearch(int[] nums, int target, boolean left){
        int L = 0, R = nums.length;
        while (L < R){
            int mid = (R + L) / 2;
            if (nums[mid] > target || (left && nums[mid] == target)){
                R = mid;
            }else {
                L = mid + 1;
            }
        }

        return L;
    }

    /**
     * 查找插入，如果有返回下标负责返回即将要插入的下标
     * @param nums
     * @param target
     * @return
     */
    static int searchInsert(int[] nums, int target) {
        int L = 0, R = nums.length;
        while (L < R){
            if (nums[L] >target || nums[L] == target) break;
            int mid = (R + L) / 2;
            if (nums[mid] > target){
                R = mid;
            }else if (nums[mid] == target){
                L = mid;
                break;
            }else {
                L = mid + 1;
            }
        }
        return L;
    }


    /**
     * 数独有效
     * @param board
     * @return
     */
    public boolean isValidSudoku(char[][] board) {
        HashMap<Integer, Integer>[] rows = new HashMap[9];
        HashMap<Integer, Integer>[] columns = new HashMap[9];
        HashMap<Integer, Integer>[] boxes = new HashMap[9];

        //初始化数组
        for (int i = 0; i < 9; i++){
            rows[i] = new HashMap();
            columns[i] = new HashMap();
            boxes[i] = new HashMap();
        }

        //rows
        for (int i = 0; i < 9; i++){
            //columns
            for (int j = 0; j < 9; j++){
                char c = board[i][j];
                if(c != '.'){
                    int n = (int)c;
                    //子数独下标的计算
                    int boxeIndex = (i/3)*3 + j/3;
                    rows[i].put(n, rows[i].getOrDefault(n, 0) + 1);
                    columns[j].put(n, columns[j].getOrDefault(n, 0) + 1);
                    boxes[boxeIndex].put(n, boxes[boxeIndex].getOrDefault(n, 0) + 1);

                    //检查
                    if (rows[i].get(n) > 1 || columns[j].get(n) > 1 || boxes[boxeIndex].get(n) > 1)
                        return false;
                }
            }
        }

        return true;
    }

    /**
     * 数独解法， 回溯法，时刻判断行列和子数独是否
     * 满足要求，不满足就回退
     */
    // box size
    int n = 3;
    // row size
    int N = n * n;

    int [][] rows = new int[N][N + 1];
    int [][] columns = new int[N][N + 1];
    int [][] boxes = new int[N][N + 1];

    char[][] board;

    boolean sudokuSolved = false;

    public boolean couldPlace(int d, int row, int col) {
    /*
    Check if one could place a number d in (row, col) cell
    */
        int idx = (row / n ) * n + col / n;
        return rows[row][d] + columns[col][d] + boxes[idx][d] == 0;
    }

    public void placeNumber(int d, int row, int col) {
    /*
    Place a number d in (row, col) cell
    */
        int idx = (row / n ) * n + col / n;

        rows[row][d]++;
        columns[col][d]++;
        boxes[idx][d]++;
        board[row][col] = (char)(d + '0');
    }

    public void removeNumber(int d, int row, int col) {
    /*
    Remove a number which didn't lead to a solution
    */
        int idx = (row / n ) * n + col / n;
        rows[row][d]--;
        columns[col][d]--;
        boxes[idx][d]--;
        board[row][col] = '.';
    }

    public void placeNextNumbers(int row, int col) {
    /*
    Call backtrack function in recursion
    to continue to place numbers
    till the moment we have a solution
    */
        // if we're in the last cell
        // that means we have the solution
        if ((col == N - 1) && (row == N - 1)) {
            sudokuSolved = true;
        }
        // if not yet
        else {
            // if we're in the end of the row
            // go to the next row
            if (col == N - 1) backtrack(row + 1, 0);
                // go to the next column
            else backtrack(row, col + 1);
        }
    }

    public void backtrack(int row, int col) {
    /*
    Backtracking
    */
        // if the cell is empty
        if (board[row][col] == '.') {
            // iterate over all numbers from 1 to 9
            for (int d = 1; d < 10; d++) {
                if (couldPlace(d, row, col)) {
                    placeNumber(d, row, col);
                    placeNextNumbers(row, col);
                    // if sudoku is solved, there is no need to backtrack
                    // since the single unique solution is promised
                    if (!sudokuSolved) removeNumber(d, row, col);
                }
            }
        }
        else placeNextNumbers(row, col);
    }

    public void solveSudoku(char[][] board) {
        this.board = board;

        // init rows, columns and boxes
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                char num = board[i][j];
                if (num != '.') {
                    int d = Character.getNumericValue(num);
                    placeNumber(d, i, j);
                }
            }
        }
        backtrack(0, 0);
    }


    /**
     * 报数
     * @param n
     * @return
     */
    public String countAndSay(int n) {
        return countAndSay("1", n);
    }

    public String countAndSay(String start, int n){
        if (n == 1) return start;
        StringBuilder sb = new StringBuilder();
        int  i = 0;
        while (i < start.length()){
            int count = 1;
            while (i < start.length() - 1 && start.charAt(i) == start.charAt(i+1)){
                count++;
                i++;
            }
            sb.append(Integer.toString(count)).append(start.charAt(i));
            i++;
        }
        start = sb.toString();

        return countAndSay(start, n-1);
    }

    /**
     * 螺旋矩阵
     * @param matrix
     * @return
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        List ans = new ArrayList();
        if (matrix.length == 0)
            return ans;
        int r1 = 0, r2 = matrix.length - 1;
        int c1 = 0, c2 = matrix[0].length - 1;
        while (r1 <= r2 && c1 <= c2) {
            for (int c = c1; c <= c2; c++) ans.add(matrix[r1][c]);
            for (int r = r1 + 1; r <= r2; r++) ans.add(matrix[r][c2]);
            if (r1 < r2 && c1 < c2) {
                for (int c = c2 - 1; c > c1; c--) ans.add(matrix[r2][c]);
                for (int r = r2; r > r1; r--) ans.add(matrix[r][c1]);
            }
            r1++;
            r2--;
            c1++;
            c2--;
        }
        return ans;

    }


    /**
     * 区间合并
     * @param intervals
     * @return
     */
    public static int[][] merge(int[][] intervals) {
        List<List<Integer>> rs = new ArrayList<>();
        int l = intervals.length;


        //按照intervals[r][0]排序;
        for (int r = 0; r < l-1; r++){
            boolean flag = false;
            for (int j = 0; j < l -1 -r; j++){
                if (intervals[j][0] > intervals[j+1][0]){
                    int temp1 = intervals[j][0];
                    int temp2 = intervals[j][1];
                    intervals[j][0] = intervals[j+1][0];
                    intervals[j][1] = intervals[j+1][1];
                    intervals[j+1][0] = temp1;
                    intervals[j+1][1] = temp2;
                    flag = true;
                }
            }
            if (!flag) break;
        }

        //区间合并
        for (int i = 0; i < l; i++){
            //后一个区间的下限大于前一个的上限那么一定不相连
            List<Integer> list = new ArrayList<>();
            if (i == l-1 ||  intervals[i][1] < intervals[i+1][0]) {
                //只有不能进行区间合并的时候才计入结果
                list.add(intervals[i][0]);
                list.add(intervals[i][1]);
                rs.add(list);
            }else if (i < l-1){
                //可以进行区间合并
                intervals[i+1][0] = intervals[i][0];
                intervals[i+1][1] = intervals[i][1] >= intervals[i+1][1]?intervals[i][1] : intervals[i+1][1];
            }


        }

        int num[][] = new int[rs.size()][2];
        for (int i = 0; i < rs.size(); i++){
            num[i][0] = rs.get(i).get(0);
            num[i][1] = rs.get(i).get(1);
        }
        return num;


    }


    public int[][] insert(int[][] intervals, int[] newInterval) {
        //找到要插入的位置，然后从该位置开始向后合并区间
        int[][] rs = new int[intervals.length + 1][2];
        int position = -1;
        for (int i = 0; i < intervals.length; i++){
            position = i;
            if (i < intervals.length - 1 && intervals[i][0] <= newInterval[0] && intervals[i+1][0] > newInterval[0]){
                break;
            }
        }

        //把前面不需要区间合并的区间先加到结果集
        for (int i = 0; i < position; i++){
            rs[i][0] = intervals[i][0];
            rs[i][1] = intervals[i][1];
        }


        //从要开始插入的位置的前一个区间开始合并
        for (int i = position; i < rs.length; i++){

        }

        return rs;
    }

    /**
     * 生成n*n的螺旋矩阵
     * @param n
     * @return
     */
    public static int[][] generateMatrix(int n) {
        int [][] matrix = new int[n][n];
        int num = 1;
        int c1 = 0, c2 = n-1;
        int r1 = 0, r2 = n-1;

        while(c1 <= c2 && r1<= r2){
            for (int c = c1; c <= c2; c++) {
                matrix[r1][c] = num++;
            }
            for (int r = r1 +1; r <= r2; r++){
                matrix[r][c2] = num++;
            }

            if (r1 < r2 && c1 < c2){
                for (int c = c2 -1; c > c1; c--){
                    matrix[r2][c] = num++;
                }
                for (int r = r2; r > r1; r--){
                    matrix[r][c1]= num++;
                }
            }
            r1++;
            r2--;
            c1++;
            c2--;
        }

        return matrix;
    }

    /**
     * 全排列，按照大小顺序，并输出第k个(k属于[1, n!])
     * @param n
     * @param k
     * @return
     */
    public static String getPermutation(int n, int k) {
        //回溯和剪枝
        List<List<Integer>> rs = new ArrayList<>();
        int[] nums = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = i+1;
        int[] visited = new int[n];
        generatePermutation(nums, k, visited,new Stack<>(), rs);
        StringBuilder sb = new StringBuilder();
        rs.get(k-1).forEach(sb::append);
        return sb.toString();

    }

    static void generatePermutation(int[] nums,  int k, int[] visited, Stack<Integer> stack, List<List<Integer>> list){
        if (list.size() == k) return;
        if (stack.size() == nums.length){
            list.add(new ArrayList<>(stack));
            return;
        }


        for(int i = 0; i < nums.length; i++){
            if (visited[i] == 1) continue;
            stack.push(nums[i]);
            visited[i] = 1;
            generatePermutation(nums, k,visited,  stack, list);
            visited[i] = 0;
            stack.pop();
        }

    }

    /**
     * 链表旋转
     * @param head
     * @param k
     * @return
     */
    public static ListNode rotateRight(ListNode head, int k) {
        //思想：先将链表连城闭环，头指正后移 length - k %length, 然后选出新头
        if (head == null) return null;
        if (head.next == null) return head;

        ListNode old_head = head;
        int n;
        //找到尾节点，并计算出链表长度
        for (n = 1; old_head.next != null; n++)
            old_head = old_head.next;
        //链表闭合
        old_head.next = head;

        ListNode new_tail = head;
        for (int i = 1; i < n - k%n; i++)
            new_tail = new_tail.next;
        ListNode new_head = new_tail.next;
        new_tail.next = null;

        return new_head;
    }

    /**
     *
     * @param m
     * @param n
     * @return
     */
    public int uniquePaths(int m, int n) {
        int[] cur = new int[n];
        Arrays.fill(cur,1);
        for (int i = 1; i < m;i++){
            for (int j = 1; j < n; j++){
                cur[j] += cur[j-1] ;
            }
        }
        return cur[n-1];

    }


    /**
     * 带障碍物的不同路径
     * @param obstacleGrid
     * @return
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {

        int R = obstacleGrid.length;
        int C = obstacleGrid[0].length;

        //r如果一开始就有障碍物，那么寸步难行，直接返回0
        if (obstacleGrid[0][0] == 1) {
            return 0;
        }

        // 开始的格子填充数字1
        obstacleGrid[0][0] = 1;

        // 填充第一行的值，
        for (int i = 1; i < R; i++) {
            obstacleGrid[i][0] = (obstacleGrid[i][0] == 0 && obstacleGrid[i - 1][0] == 1) ? 1 : 0;
        }

        // 填充第一列的值，第一列如果有障碍物及其障碍物之后的填充为0
        for (int i = 1; i < C; i++) {
            obstacleGrid[0][i] = (obstacleGrid[0][i] == 0 && obstacleGrid[0][i - 1] == 1) ? 1 : 0;
        }

        // 从 cell(1,1) 开始填充值
        // No. of ways of reaching cell[i][j] = cell[i - 1][j] + cell[i][j - 1]
        // i.e. From above and left.
        for (int i = 1; i < R; i++) {
            for (int j = 1; j < C; j++) {
                if (obstacleGrid[i][j] == 0) {
                    obstacleGrid[i][j] = obstacleGrid[i - 1][j] + obstacleGrid[i][j - 1];
                } else {
                    obstacleGrid[i][j] = 0;
                }
            }
        }

        // Return value stored in rightmost bottommost cell. That is the destination.
        return obstacleGrid[R - 1][C - 1];
    }


    /**
     * 最小路径和
     * @param grid
     * @return
     */
    public static int minPathSum(int[][] grid) {

        int R = grid.length, C = grid[0].length;

        int[][] dp =  new int[R][C];

        for (int i = R - 1; i >= 0; i--){
            for (int j = C - 1; j >= 0; j--){
                //最后一行，值由当前格和后面的格子确定
                if (i == R - 1 && j != C - 1){
                    dp[i][j] = grid[i][j] + dp[i][j+1];
                }else if (j == C -1 && i != R - 1){
                    //最后一列的数据，值由当前格子和下面的格子确定
                    dp[i][j] = grid[i][j] + dp[i+1][j];
                } else if (j != C - 1 && i != R - 1) {
                    dp[i][j] = grid[i][j] + Math.min(dp[i][j+1], dp[i+1][j]);
                }else {
                    dp[i][j] = grid[i][j];
                }
            }
        }
        return dp[0][0];
    }


}
