package sort;

import java.util.Arrays;

public class QuickSort {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 5, 4, 3, 3, 3};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void sort(int[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    private static void sort(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }

        // partition
        int index = partition(arr, start, end);

        // sort left
        sort(arr, start, index - 1);
        // sort right
        sort(arr, index + 1, end);
    }

    private static int partition(int[] arr, int start, int end) {
        int partition = arr[end]; // 使用该数据作为分割
        int counter = start; // counter 左侧为所有小于 partition 的数据

        for (int i = start; i < end; i++) {
            if (arr[i] < partition) { // 移动到左侧
                exch(arr, i, counter++);
            }
        }
        exch(arr, end, counter);
        return counter;
    }

    private static void exch(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
}