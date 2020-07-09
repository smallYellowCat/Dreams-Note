package com.doudou.leetcode;

import java.awt.*;
import java.awt.font.NumericShaper;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author doudou
 * @date 2019/07/24
 */
public class MainTest {

    public static void main(String[] args) {
        //isValid("(){}[]");
        int[] n = {1,1,3};
        String[] str = {""};
        //nextPermutation(n);
        //combinationSum(n, 7);
        //multiply("123", "456");
        //List<List<Integer>> list = permuteUnique(n);
        //groupAnagrams(str);
        //myPow(2.0d, -2);
        //lengthOfLastWord("a");
        System.out.println();
    }

    static public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int n = nums1.length, m = nums2.length;
        double x,y;
        if (n % 2 != 0){
            x = nums1[n/2];
        }else {
            x =  ((nums1[n/2]) + (nums1[n/2+1]))/1.0/2;
        }

        if (m % 2 != 0){
            y = nums2[m/2];
        }else {
            y =  ((nums2[m/2]) + (nums2[m/2+1]))/1.0/2;
        }

        return  (x + y) /1.0/ 2;
    }


    public String longestPalindrome(String s) {
        int length = 1;
        String rs = s.substring(0,1);
        for(int i = 0; i < s.length() - 1; i++){
            for(int j = i + 1; j < s.length(); j++){
                String temp = s.substring(i,j);
                if(judging(temp)){
                    if(temp.length() > length){
                        rs = temp;
                        length = temp.length();
                    }
                }
            }
        }
        return rs;
    }

    boolean judging(String s){
        for(int i = 0, j = s.length() - 1; i < j; i++, j--){

            if(s.charAt(i) != s.charAt(j)){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断大中小括号匹配
     * @param s
     * @return
     */
    static boolean isValid(String s) {
        if(s.length() % 2 != 0) return false;
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++){
            if (stack.size() > s.length() / 2) return false;
            char c = s.charAt(i);
            if ((c == ')' || c == ']' || c == '}') && stack.isEmpty())
                return false;
            if (c == ')' && stack.pop() != '(') return false;
            if (c == ']' && stack.pop() != '[') return false;
            if (c == '}' && stack.pop() != '{') return false;

            if (c == '(' || c == '[' || c == '{')
            stack.add(c);

        }
        return true;
    }

    Map<String, String> phone = new HashMap() {{
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


    public List<List<Integer>> fourSum(int[] nums, int target) {
        return null;
    }

    public void nextPermutation(int[] nums) {
        int i = nums.length - 2;
        while (i >= 0 && nums[i + 1] <= nums[i]) {
            i--;
        }
        if (i >= 0) {
            int j = nums.length - 1;
            while (j >= 0 && nums[j] <= nums[i]) {
                j--;
            }
            swap(nums, i, j);
        }
        reverse(nums, i + 1);
    }

    private void reverse(int[] nums, int start) {
        int i = start, j = nums.length - 1;
        while (i < j) {
            swap(nums, i, j);
            i++;
            j--;
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> rs = new ArrayList<>();
        if (candidates.length == 0){
            return rs;
        }
        Arrays.sort(candidates);
        backcombinationSum(candidates, 0, target, rs, new Stack<>());
        return rs;

    }

    static void backcombinationSum(int[] candidates, int start, int target, List<List<Integer>> list, Stack<Integer> temp ){

        if (target == 0){
            list.add(new ArrayList<>(temp));
            return;
        }

        for (int i = start; i < candidates.length; i ++){
            if (target-candidates[i] < 0) break;
                temp.add(candidates[i]);
                backcombinationSum(candidates, i,target-candidates[i], list, temp);
                temp.pop();

        }
    }

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> rs = new ArrayList<>();
        if (candidates.length == 0){
            return rs;
        }
        Arrays.sort(candidates);
        backcombinationSum2(candidates,0, target, rs, new Stack<>());
        return rs;
    }

    static void backcombinationSum2(int[] candidates, int start,  int target, List<List<Integer>> list, Stack<Integer> temp ){

        if (target == 0){
            list.add(new ArrayList<>(temp));
            return;
        }

        for (int i = start; i < candidates.length; i ++){
            if (target-candidates[i] < 0) break;
            if (i > start && candidates[i] == candidates[i - 1]) {
                continue;
            }
            temp.add(candidates[i]);
            backcombinationSum2(candidates, i+1, target-candidates[i], list, temp);
            temp.pop();

        }
    }

    public int firstMissingPositive(int[] nums) {
        int min = 0;
        int j = 1;
        int count = 0;
        while (j <= nums.length ){
            for (int i = 0; i < nums.length; i++){
                if (nums[i] == j){
                    if (min != j){
                        min = j;
                        count++;
                    }
                }
            }
            if (count != j) break;
            j++;
        }

        return min+1;
    }


    /**
     * 字符串乘法， 没有使用Integer.valueOF();
     * @param num1
     * @param num2
     * @return
     */
    public static String multiply(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        int length1 = num1.length();
        int length2 = num2.length();
        StringBuilder str = new StringBuilder();

        int[] arrayInt = new int[length1 + length2];

        for (int i = length1 - 1; i >= 0; i--) {
            for (int z = length2 - 1; z >= 0; z--) {
                int number1 = num1.charAt(i) - 48;
                int number2 = num2.charAt(z) - 48;
                arrayInt[i + z] += number1 * number2;
                if (arrayInt[i + z] >= 10 && (i + z) != 0) {
                    arrayInt[i + z - 1] += arrayInt[i + z] / 10;
                    arrayInt[i + z] = arrayInt[i + z] % 10;
                }
            }
        }

        for (int i = 0; i <= length1 + length2 - 2; i++) {
            str.append(arrayInt[i]);
        }

        return str.toString();
    }

    /**
     * 通配符匹配
     * @param s
     * @param p
     * @return
     */
    public boolean isMatch(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] f = new boolean[m + 1][n + 1];

        f[0][0] = true;
        for(int i = 1; i <= n; i++){
            f[0][i] = f[0][i - 1] && p.charAt(i - 1) == '*';
        }

        for(int i = 1; i <= m; i++){
            for(int j = 1; j <= n; j++){
                if(s.charAt(i - 1) == p.charAt(j - 1) || p.charAt(j - 1) == '?'){
                    f[i][j] = f[i - 1][j - 1];
                }
                if(p.charAt(j - 1) == '*'){
                    f[i][j] = f[i][j - 1] || f[i - 1][j];
                }
            }
        }
        return f[m][n];
    }


    /**
     * 全排列
     * @param nums
     * @return
     */
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> rs = new ArrayList<>();
        if (nums.length == 0){
            return rs;
        }
        back(rs, new ArrayList<>(),  nums);
        return rs;
    }

    static void back(List<List<Integer>> rs, List<Integer> stack, int[] nums){
        if (stack.size() == nums.length){
            rs.add(new ArrayList<>(stack));
            return;
        }

        for (int i = 0; i < nums.length; i++){
            if (stack.contains(nums[i])) continue;
                stack.add(nums[i]);
            back(rs, stack, nums);
            stack.remove(stack.size() - 1);
        }
    }

    /**
     * 包含重复元素的全排列，要求排列结果不重复，回溯思想
     * @param nums
     * @return
     */
    public static List<List<Integer>>  permuteUnique(int[] nums) {
        //先排序
        Arrays.sort(nums);
        List<List<Integer>> rs = new ArrayList<>();
        if (nums.length == 0){
            return rs;
        }
        int[] visited = new int[nums.length];
        backUnique(rs, new ArrayList<>(), visited,nums);
        return rs;
    }

    static void backUnique(List<List<Integer>> rs, List<Integer> list, int[] visited, int[] nums){
        if (list.size() == nums.length ){
            rs.add(new ArrayList<>(list));
            return;
        }

        for (int i = 0; i < nums.length ; i++){
            if (visited[i] == 1 || (i > 0 && visited[i - 1] == 0 && nums[i] == nums[i - 1])) continue;
            //if (list.contains(nums[i])) continue;
            list.add(nums[i]);
            visited[i] = 1;
            backUnique(rs, list,visited, nums);
            list.remove(list.size() - 1);
            visited[i] = 0;
        }
    }

    /**
     * 矩阵旋转九十度
     * @param matrix
     */
    public void rotate(int[][] matrix) {
        int n = matrix.length;

        //先转置
        for (int i = 0; i < n; i++){
            for (int j = i; j < n; j++){
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        //行反转
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n / 2; j++){
                int temp = matrix[i][j];
                matrix[i][j] = matrix[i][n - j - 1];
                matrix[i][n-j-1] = temp;
            }
        }

    }

    public List<List<String>> groupAnagrams(String[] strs) {
        if (strs.length == 0) return new ArrayList<>();
        Map<String, List<String>> ans = new HashMap<>();
        for (String s : strs) {
            char[] ca = s.toCharArray();
            Arrays.sort(ca);
            String key = String.valueOf(ca);
            if (!ans.containsKey(key)) ans.put(key, new ArrayList<>());
            ans.get(key).add(s);
        }
        return new ArrayList<>(ans.values());

    }

    private double fastPow(double x, long n) {
        if (n == 0) {
            return 1.0;
        }
        double half = fastPow(x, n / 2);
        if (n % 2 == 0) {
            return half * half;
        } else {
            return half * half * x;
        }
    }
    public double myPow(double x, int n) {
        long N = n;
        if (N < 0) {
            x = 1 / x;
            N = -N;
        }

        return fastPow(x, N);
    }

    public int maxSubArray(int[] nums) {
        int max = nums[0];
        int temp = 0;
        for (int i = 0; i < nums.length; i++){
            if (temp > 0){
                temp += nums[i];
            }else {
                temp = nums[i];
            }
            max = Math.max(temp, max);
        }
        return max;
    }

    public boolean canJump(int[] nums) {
        int lastPos = nums.length - 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (i + nums[i] >= lastPos) {
                lastPos = i;
            }
        }
        return lastPos == 0;
    }

    public static  int lengthOfLastWord(String s) {
        if (s.length() == 0) return 0;
        int star = 0, end = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == ' ') continue;
            star = i;
            break;
        }
        int j = star;
        while (j >= 0) {
            if (s.charAt(j) == ' ') {
                end = j;
                break;
            }
            j--;
            end = j;
        }
        return star - end;
    }

}


