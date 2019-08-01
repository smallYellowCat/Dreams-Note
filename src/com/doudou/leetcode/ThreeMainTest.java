package com.doudou.leetcode;

public class ThreeMainTest {
    /**
     *入口函数，自动生成
     * @author 豆豆
     * @date 2019/7/31 21:21a
    */
    public static void main(String[] args){
        addBinary("1010", "1011");
    }

    /**
     * 基础加1
     * @param digits
     * @return
     */
    public int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--){
            digits[i]++;
            digits[i] = digits[i] % 10;
            //如果没有进位了直接返回
            if (digits[i] != 0) return digits;
        }
        //如果全部进位了那么这个数一定是高位为1，其余位为0，只需扩展下数组大小，高位置一
        digits = new int[digits.length + 1];
        digits[0] = 1;
        return digits;

    }

    /**
     * 二进制求和
     * @param a
     * @param b
     * @return
     */
    public static String addBinary(String a, String b) {
        if (a.equals("0")) return b;
        if (b.equals("0")) return a;
        int la = a.length(), lb= b.length();
        //从后向前遍历，记录进位
        StringBuilder sb = new StringBuilder();
        int up = la >= lb ? la : lb;
        int low = la < lb ? la : lb;
        int sum = 0, pop = 0;
        for (int i = 0; i < up; i++){
            if (i < low)
                sum = a.charAt(la-i-1) - 48 + b.charAt(lb-i-1) - 48 + pop;
            else if (a.length() > b.length()){
                sum = pop + a.charAt(la-i-1) - 48;
            }else {
                sum = pop + b.charAt(lb-i-1) - 48;
            }
            sb.append(sum % 2);
            pop = sum/2;
        }
        sb.reverse();
        return pop == 1 ? "1" + sb.toString() : sb.toString();
    }



}
