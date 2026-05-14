package sort;

public class QuickSort extends Sorter {

    @Override
    protected void doSort() throws InterruptedException {
        quickSort(0, a.length - 1);
    }

    private void quickSort(int left, int right) throws InterruptedException {
        checkInterrupted();
        if (left >= right) return;

        int pivotIndex = partition(left, right);
        quickSort(left, pivotIndex);
        quickSort(pivotIndex + 1, right);
    }

    private int partition(int left, int right) throws InterruptedException {
        int pivot = a[(left + right) / 2];
        int i = left;
        int j = right;

        while (i <= j) {
            while (a[i] < pivot) {
                compare(i, (left + right) / 2);
                i++;
            }

            while (a[j] > pivot) {
                compare(j, (left + right) / 2);
                j--;
            }

            if (i <= j) {
                swap(i, j);
                i++;
                j--;
            }
        }
        return j;
    }
}