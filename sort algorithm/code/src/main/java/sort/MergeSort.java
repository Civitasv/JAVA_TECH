package sort;

import java.util.Arrays;

public class MergeSort {
    public static void main(String[] args) {
        int[] arr = new int[]{3, 3, 3, 1, 2, 3, 4};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void sort(int[] arr) {
        int[] aux = new int[arr.length];
        sort(arr, aux, 0, arr.length - 1);
    }

    private static void sort(int[] arr, int[] aux, int start, int end) {
        if (start >= end) {
            return;
        }

        int mid = start + ((end - start) >> 1);
        // sort left
        sort(arr, aux, start, mid);
        // sort right
        sort(arr, aux, mid + 1, end);
        // merge both
        merge(arr, aux, start, mid, end);
    }

    private static void merge(int[] arr, int[] aux, int start, int mid, int end) {
        // use aux to backup
        for (int i = start; i <= end; i++) {
            aux[i] = arr[i];
        }
        int first = start, second = mid + 1; // i 表示 左侧已排序数组的起始位置， mid+1 表示 右侧已排序数组的起始位置

        for (int i = start; i <= end; i++) {
            if (first > mid) arr[i] = aux[second++];
            else if (second > end) arr[i] = aux[first++];
            else if (aux[first] < aux[second]) arr[i] = aux[first++];
            else arr[i] = aux[second++];
        }
    }
}