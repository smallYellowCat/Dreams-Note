package com.doudou.leetcode;

import java.util.Stack;

public class FourMainTest {
    
    /**
     *入口函数，自动生成
     * @author 豆豆
     * @date 2019/8/14 19:29a
    */
    public static void main(String[] args){
        int[] n = {2,1,5,6,2,3};
        //largestRectangleArea(n);
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
     * @param heights
     * @return
     */
    public int largestRectangleArea(int[] heights) {
        Stack< Integer > stack = new Stack < > ();
        stack.push(-1);
        int maxarea = 0;
        for (int i = 0; i < heights.length; ++i) {
            while (stack.peek() != -1 && heights[stack.peek()] >= heights[i])
                maxarea = Math.max(maxarea, heights[stack.pop()] * (i - stack.peek() - 1));
            stack.push(i);
        }
        while (stack.peek() != -1)
            maxarea = Math.max(maxarea, heights[stack.pop()] * (heights.length - stack.peek() -1));
        return maxarea;
    }


}
