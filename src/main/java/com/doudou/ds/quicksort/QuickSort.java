package com.doudou.ds.quicksort;

import java.util.Random;

/**
*快速排序算法
*@author 豆豆
*时间:
*/
public class QuickSort {
    private static int[] data = new int[1001];
    public static void main(String[] args){
        //Integer.MAX_VALUE;
        //Integer.MIN_VALUE;
        //自动生成main
        for (int i = 0; i < data.length; i++){
            data[i] = new Random().nextInt(1000000000);
        }
        quicksort(0, 1000);
        for (int i = 0; i < data.length; i++){
            System.out.println(data[i]);
        }

    }

    private static void quicksort(int left, int right){
        int i, j, t, tem;
        if (left > right) {
            return;
        }

        tem = data[left];
        i = left;
        j = right;

        while (i != j){
            while (data[j] >= tem && i < j){
                j--;
            }

            while (data[i] <= tem && i < j){
                i++;
            }

            //当上述两个条件都不满足的时候，说明找到了可以交换的两个数
            if (i < j){
                t = data[j];
                data[i] = data[j];
                data[j] = t;
            }

        }

        //当i = j时表示一轮已经结束,交换基数
        data[left] = data[i];
        data[i] = tem;

        //递归左右两边
        quicksort(left, i-1);
        quicksort(i+1, right);
    }


}
