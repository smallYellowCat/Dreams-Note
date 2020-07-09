package com.doudou.ds.leetcode;

import java.util.*;

/**
 * @author 豆豆
 * @date 2019/7/24 15:28
 * @flag 以万物智能，化百千万亿身
 */
public class FirstMainTest {

    /**
     *入口函数，自动生成
     * @author 豆豆
     * @date 2019/7/24 15:28a
    */
    public static void main(String[] args){
        /*int[] nums = {1,2,3,4,5,6,7,8,9};
        System.out.println(Arrays.toString(towSum(nums, 6)));*/
        ListNode t ;
        ListNode listNode1 = new ListNode(3);
        t = listNode1;
        t.next = new ListNode(9);
        t = t.next;

        t.next = new ListNode(9);
        t = t.next;
        t.next = new ListNode(9);
        t = t.next;
        t.next = new ListNode(9);
        t = t.next;
        t.next = new ListNode(9);
        t = t.next;
        t.next = new ListNode(9);
        t = t.next;
        t.next = new ListNode(9);
        t = t.next;
        t.next = new ListNode(9);
        t = t.next;




        ListNode listNode2 = new ListNode(7);


        //addTwoNumbers(listNode1, listNode2);

        //lengthOfLongestSubstring("aab");
        //longestPalindrome("caba");
        //convert("AB", 1);
        //myAtoi("0-1");
        //isPalindrome(121);
        //romanToInt("MCMXCIV");
        //String[] s = {"flower","flow","flight"};
        //longestCommonPrefix(s);
        //int[] num = {1,-2,-5,-4,-3,3,3,5};
        //fourSum(num, -11);
        generateParenthesis(3);
    }


    /**
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     *
     * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
     *
     * 示例:
     *
     * 给定 nums = [2, 7, 11, 15], target = 9
     *
     * 因为 nums[0] + nums[1] = 2 + 7 = 9
     * 所以返回 [0, 1]
     *
     * @param nums
     * @param target
     * @return
     */
    static int[] towSum(int[] nums, int target){
        //hash算法，速度快
        Map<Integer, Integer> map = new HashMap<>(nums.length * 2);
        for (int i = 0; i < nums.length; i++){
            int complement = target - nums[i];
            if (map.containsKey(complement)){
                return new int[]{map.get(complement), i};
            }

            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");

        //暴力求解，速度慢，内存消耗比上面稍微小
        /*int[] rs = new int[2];
        for(int i = 0; i < nums.length; i++){
            for(int j = i +1 ; j < nums.length; j++){
                int sum = nums[i] + nums[j];
                if(sum == target){
                    rs[0] = i;
                    rs[1] = j;
                    return rs;
                }
            }
        }
        throw  new IllegalArgumentException("No two sum solution!");*/

    }

    /**
     * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度
     * @param s
     * @return
     */
    static int lengthOfLongestSubstring(String s) {
        //滑动窗口求解
        int n = s.length();
        Set<Character> set = new HashSet<>();
        int ans = 0, i = 0, j = 0;
        while (i < n && j < n){
            //try to extend the rage[i, j]
            if (!set.contains(s.charAt(j))){
                //先s.charAt(j); 再 j++;
                set.add(s.charAt(j++));
                ans = Math.max(ans, j - i);
            }else {
                //移除set中上一次滑动窗口的第一个字符
                set.remove(s.charAt(i++));
            }
        }
        return ans;
    }


    static class ListNode {
      int val;
      ListNode next;
      ListNode(int x) { val = x; }
    }

    static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        /*int sum; ListNode rs = new ListNode(0), temp;
        int l1num, l2num;
        l1num = getNum(l1);
        l2num = getNum(l2);
        sum = l1num + l2num;
        String[] ss = String.valueOf(sum).split("");
        temp = rs;
        for(int i = ss.length - 1; i >= 0; i--){
            temp.next = new ListNode(Integer.valueOf(ss[i]));
            temp = temp.next;

        }

        return rs.next;*/

        ListNode dummyHead = new ListNode(0);
        ListNode p = l1, q = l2, curr = dummyHead;
        int carry = 0;
        while (p != null || q != null) {
            int x = (p != null) ? p.val : 0;
            int y = (q != null) ? q.val : 0;
            //进位加上两个相应位置的数值
            int sum = carry + x + y;
            //进位
            carry = sum / 10;
            curr.next = new ListNode(sum % 10);
            curr = curr.next;
            if (p != null) p = p.next;
            if (q != null) q = q.next;
        }
        if (carry > 0) {
            //最后一次的进位
            curr.next = new ListNode(carry);
        }
        return dummyHead.next;



    }

    static void getNum(ListNode l){
        a:for (int i = 0; i < 10; i++){
            if (i == 5){
                break a;
            }
        }
    }


    /**
     * 给定两个大小为 m 和 n 的有序数组 nums1 和 nums2。
     *
     * 请你找出这两个有序数组的中位数，并且要求算法的时间复杂度为 O(log(m + n))。
     *
     * 你可以假设 nums1 和 nums2 不会同时为空。
     *
     * 示例 1:
     *
     * nums1 = [1, 3]
     * nums2 = [2]
     *
     * 则中位数是 2.0
     * 示例 2:
     *
     * nums1 = [1, 2]
     * nums2 = [3, 4]
     *
     * 则中位数是 (2 + 3)/2 = 2.5
     * @param nums1
     * @param nums2
     * @return
     */
    static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        return 0.0d;
    }


    /**
     * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。
     *
     * 示例 1：
     *
     * 输入: "babad"
     * 输出: "bab"
     * 注意: "aba" 也是一个有效答案。
     * 示例 2：
     *
     * 输入: "cbbd"
     * 输出: "bb"
     *
     * @param s
     * @return
     */
    static String longestPalindrome(String s) {
        //中心扩散解法
        if (s == null || s.length() < 1) return "";
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);

    }

    private static int expandAroundCenter(String s, int left, int right) {
        int L = left, R = right;
        while (L >= 0 && R < s.length() && s.charAt(L) == s.charAt(R)) {
            L--;
            R++;
        }
        return R - L - 1;
    }


    /**
     * 将一个给定字符串根据给定的行数，以从上往下、从左到右进行 Z 字形排列。
     *
     * 比如输入字符串为 "LEETCODEISHIRING" 行数为 3 时，排列如下：
     *
     * L   C   I   R
     * E T O E S I I G
     * E   D   H   N
     * 之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："LCIRETOESIIGEDHN"。
     *
     * @param s
     * @param numRows
     * @return
     */
    static String convert(String s, int numRows) {
        if (numRows == 1)
            return s;
        List<StringBuilder> list = new ArrayList<>();
        for (int i = 0; i < Math.min(s.length(), numRows); i++)
            list.add(new StringBuilder());
        //开始逐行遍历
        int curRows = 0;
        boolean isdown = false;
        for (char character : s.toCharArray()){
            list.get(curRows).append(character);
            if (curRows == 0 || curRows == numRows - 1) isdown = !isdown;
            curRows += isdown? 1 : -1;
        }


        //所有行拼接
        StringBuilder rs = new StringBuilder();
        for (StringBuilder sb : list){
            rs.append(sb);
        }
        return rs.toString();
    }


    /**
     * 整数反转，溢出返回0
     * @param x
     * @return
     */
    public int reverse(int x) {
        int rev = 0;
        while(x != 0){
            int pop = x % 10;
            x /= 10;
            //判断是否溢出
            if(rev > Integer.MAX_VALUE/10 || (rev == Integer.MAX_VALUE/10 && pop > 7)) return 0;
            if(rev < Integer.MIN_VALUE/10 || (rev == Integer.MIN_VALUE/10 && pop < -8)) return 0;
            rev = rev * 10 + pop;
        }

        return rev;
    }


    /**
     * 请你来实现一个 atoi 函数，使其能将字符串转换成整数。
     *
     * 首先，该函数会根据需要丢弃无用的开头空格字符，直到寻找到第一个非空格的字符为止。
     *
     * 当我们寻找到的第一个非空字符为正或者负号时，则将该符号与之后面尽可能多的连续数字组合起来，作为该整数的正负号；假如第一个非空字符是数字，则直接将其与之后连续的数字字符组合起来，形成整数。
     *
     * 该字符串除了有效的整数部分之后也可能会存在多余的字符，这些字符可以被忽略，它们对于函数不应该造成影响。
     *
     * 注意：假如该字符串中的第一个非空格字符不是一个有效整数字符、字符串为空或字符串仅包含空白字符时，则你的函数不需要进行转换。
     *
     * 在任何情况下，若函数不能进行有效的转换时，请返回 0。
     *
     * 说明：
     *
     * 假设我们的环境只能存储 32 位大小的有符号整数，那么其数值范围为 [−231,  231 − 1]。如果数值超过这个范围，qing返回  INT_MAX (231 − 1) 或 INT_MIN (−231) 。
     *
     * 示例 1:
     *
     * 输入: "42"
     * 输出: 42
     * 示例 2:
     *
     * 输入: "   -42"
     * 输出: -42
     * 解释: 第一个非空白字符为 '-', 它是一个负号。
     *      我们尽可能将负号与后面所有连续出现的数字组合起来，最后得到 -42 。
     * 示例 3:
     *
     * 输入: "4193 with words"
     * 输出: 4193
     * 解释: 转换截止于数字 '3' ，因为它的下一个字符不为数字。
     * @param str
     * @return
     */
    static int myAtoi(String str) {
        char start = '0', end = '9', sym1 = '-', sym2 = '+'; int res = 0; boolean isNavi = false, hasSym = false, hasNum = false;
        int symcount = 0;
        str  = str.trim();
        for (int i = 0; i < str.length(); i++){
            char c = str.charAt(i);
            //判断非法字符
            if ((c < start || c > end) && (c != sym1 && c != sym2)){
                break;
            }
            //判断溢出
            if (!isNavi && (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && Integer.valueOf(Character.toString(c)) > 7)))
                return Integer.MAX_VALUE;
            if (isNavi && (-res < Integer.MIN_VALUE / 10 || (-res == Integer.MIN_VALUE / 10 && -Integer.valueOf(Character.toString(c)) < -8)))
                return Integer.MIN_VALUE;

            if (hasNum && (c == sym1 || c == sym2)){
                return isNavi? -res : res;
            }
            //首位负数符号
            if (c == sym1 || c == sym2){
                symcount++;
                if (c == sym1){
                    isNavi = true;
                }
            }
            if (symcount > 1){
                return isNavi? -res : res;
            }
            //数字转换
            if (c >= start){
                res = res * 10 + Integer.valueOf(Character.toString(c));
                hasNum = true;
            }


        }

        return isNavi? -res : res;
    }

    /**
     * 判断回文数字
     * @param x
     * @return
     */
    static boolean isPalindrome(int x) {
        /*if (x < 0) return false;
        if (x < 10) return true;
        List<Integer> list = new ArrayList<>();
        while (x != 0){
            list.add(x % 10);
            x /= 10;
        }
        boolean rs = true;
        for (int i = 0, j = list.size() - 1; j > list.size() / 2 - 1; i++, j--){
            if (!list.get(i).equals(list.get(j))){
                rs = false;
                break;
            }
        }

        return rs;*/


        //思路清晰简单，性能没有上面的快
        if (x < 0) return false;
        int cur = 0;
        int num = x;
        while (num != 0){
            cur = cur * 10 + num % 10;
            num /= 10;
        }
        return cur == x;
    }

    static boolean isMatch(String s, String p) {
        //s.matches()
        return false;
    }

    /**
     * 数组求最大面积
     * @param height
     * @return
     */
    public int maxArea(int[] height) {
        int maxarea = 0, l = 0, r = height.length - 1;
        while (l < r) {
            maxarea = Math.max(maxarea, Math.min(height[l], height[r]) * (r - l));
            if (height[l] < height[r])
                l++;
            else
                r--;
        }
        return maxarea;
    }

    /**
     * 整数转罗马数字，数字范围控制在3999以内， 如果范围继续扩大，下面的两个模板继续扩大
     */
    private static int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private String[] s = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    public String intToRoman(int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i--){
            while (num >= values[i]){
                sb.append(s[i]);
                num -= values[i];
            }
        }

        return sb.toString();

    }

    /**
     * 罗马转数字
     */
    private static List<String> list = Arrays.asList("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I");
    static int romanToInt(String s) {
        int num = 0;
        for (int i = 0; i < s.length(); i++){
            String str = Character.toString(s.charAt(i));

            if (i + 1 < s.length() && list.indexOf(str + Character.toString(s.charAt(i + 1))) > -1){
                num += values[list.indexOf(str + Character.toString(s.charAt(i + 1)))];
                i++;
            }else {
                num += values[list.indexOf(Character.toString(s.charAt(i)))];
            }


        }
        return num;
    }

    /**
     * 最长前缀
     * @param strs
     * @return
     */
    static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) return "";
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++)
            while (strs[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) return "";
            }
        return prefix;

    }

    /**
     * 三数之和
     * @param nums
     * @return
     */
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        for (int  i = 0; i < nums.length - 2; i++){
            for (int j = i + 1; j < nums.length - 1; j++){
                for (int k = j + 1; k < nums.length; k++)
                    if (nums[i] + nums[j] + nums[k] == 0)
                        if (!list.contains(Arrays.asList(nums[i], nums[j], nums[k])))
                        list.add(Arrays.asList(nums[i], nums[j], nums[k]));
            }
        }


        return list;
    }


    /**
     * 电话号码对应的字母随机组合的问题
     */
    Map<String, String> phone = new HashMap<String, String>() {{
        put("2", "abc");
        put("3", "def");
        put("4", "ghi");
        put("5", "jkl");
        put("6", "mno");
        put("7", "pqrs");
        put("8", "tuv");
        put("9", "wxyz");
    }};

    List<String> output = new ArrayList<>();


    public void backtrack(String combination, String next_digits) {
        if (next_digits.length() == 0) {
            output.add(combination);
        } else {
            String digit = next_digits.substring(0, 1);
            String letters = phone.get(digit);
            for (int i = 0; i < letters.length(); i++) {
                String letter = phone.get(digit).substring(i, i + 1);
                backtrack(combination + letter, next_digits.substring(1));
            }
        }
    }

    public List<String> letterCombinations(String digits) {
        if (digits.length() != 0)
            backtrack("", digits);
        return output;
    }


    /**
     * 四数之和未指定值
     * @param nums
     * @param target
     * @return
     */
    static List<List<Integer>> fourSum(int[] nums, int target) {
        //排序加双指针
        List<List<Integer>> rs = new ArrayList<>();
        if (nums.length == 0) return rs;
        Arrays.sort(nums);

        for (int i = 0; i < nums.length -3; i++){

            for (int j = i + 1; j < nums.length - 2; j++){
                //为什么这样筛选：是因为如果这一次的i和上次的i对应的值一样，那么上次如果没找合适的组合这次也一定不能找到，
                //上次找到了合适的组合这次如果还能找到那肯定是重复的，而且这次也不一定能找到，因为同一个i下所有的j都会遍历，
                // 同理j也是这样，只不过j就比较简单，j因为i是固定的，所以上一次的j能找到的下一次如果还能找到那肯定是重复的
                //注意：所有的这些分析都是基于已排序的数组进行的
                if ((i > 0 && nums[i] == nums[i-1]) || (j > i +1 && nums[j] == nums[j - 1])){
                    continue;
                }
                int k = j + 1;
                int l = nums.length - 1;
                while (k < l){
                    if (l < nums.length - 1 && nums[l] == nums[l + 1] || nums[i] + nums[j] + nums[k] + nums[l] > target){
                        l--;
                    }else if (k > j + 1 && nums[k] == nums[k-1] || nums[i] + nums[j] + nums[k] + nums[l] < target){
                        k++;
                    }else {
                        List<Integer> list = new ArrayList<>();
                        list.add(nums[i]);
                        list.add(nums[j]);
                        list.add(nums[k++]);
                        list.add(nums[l--]);
                        rs.add(list);
                    }
                }
            }
        }
        return rs;
    }

    /**
     * 删除倒数第n个节点
     * @param head
     * @param n
     * @return
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dumy = new ListNode(0);
        dumy.next = head;
        ListNode first = dumy;
        ListNode second = dumy;

        for (int i = 0; i < n + 1; i++){
            first = first.next;
        }

        while (first != null){
            first= first.next;
            second = second.next;
        }

        second.next = second.next.next;
        return dumy.next;
    }

    /**
     * 合并两个有序的链表，使得新链表也有序
     * @param l1
     * @param l2
     * @return
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode prehead = new ListNode(-1);
        ListNode pre = prehead;
        while (l1 != null && l2 != null){
            if (l1.val < l2.val){
                pre.next = l1;
                l1 = l1.next;
            }else {
                pre.next = l2;
                l2 = l2.next;
            }

            pre = pre.next;
        }

        pre.next = l1 == null? l2 : l1;
        return prehead.next;

        //递归方案
        /*if (l1 == null){
            return l2;
        }

        if (l2 == null){
            return l1;
        }

        if (l1.val < l2.val){
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        }else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }*/
    }

    static List<String> generateParenthesis(int n) {
        List<String> rs = new ArrayList<>();
        backGenerateParenthesis(rs, "",0, 0, n);
        return rs;
    }

    static void backGenerateParenthesis(List<String> ans, String cur,int open, int close, int max){
        if (cur.length() == max * 2){
            ans.add(cur);
            return ;
        }

        if (open < max){
            backGenerateParenthesis(ans, cur+"(", open+1, close, max);
        }

        if (close < open){
            backGenerateParenthesis(ans,cur+")", open, close+1, max);
        }
    }

    /**
     * 利用两个列表有序合并来循环实现k个列表有序合并
     * 但是此算法效率低下
     * @param lists
     * @return
     */
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) return null;
        for (int i = 0; i < lists.length-1; i++){
            lists[i+1] = mergeTwoLists(lists[i], lists[i+1]);
        }
        return lists[lists.length -1];
    }


}
