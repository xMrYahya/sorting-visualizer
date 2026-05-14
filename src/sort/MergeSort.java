package sort;

import java.util.Arrays;

public class MergeSort extends Sorter {

    @Override
    protected void doSort() throws InterruptedException {
        if (a.length <= 1) return;
        int[] aux = Arrays.copyOf(a, a.length);
        mergeSort(0, a.length - 1, aux);
    }

    private void mergeSort(int lo, int hi, int[] aux) throws InterruptedException {
        checkInterrupted();
        if (lo >= hi) return;
        int mid = (lo + hi) / 2;
        mergeSort(lo, mid, aux);
        mergeSort(mid + 1, hi, aux);
        merge(lo, mid, hi, aux);
    }

    private void merge(int lo, int mid, int hi, int[] aux) throws InterruptedException {
        System.arraycopy(a, lo, aux, lo, hi - lo + 1);

        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            compare(i, j);
            if (aux[i] <= aux[j]) {
                set(k++, aux[i++]);
            } else {
                set(k++, aux[j++]);
            }
        }
        while (i <= mid) set(k++, aux[i++]);
        while (j <= hi)  set(k++, aux[j++]);
    }
}