package sort;

public class InsertionSort extends Sorter {

    @Override
    protected void doSort() throws InterruptedException {
        for (int i = 1; i < a.length; i++) {
            int key = a[i];
            int j = i - 1;

            while (j >= 0 && a[j] > key) {
                compare(j, j + 1);
                set(j + 1, a[j]);
                j--;
            }

            set(j + 1, key);
        }
    }
}