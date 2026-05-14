package sort;

public abstract class Sorter {
    protected int[] a;
    protected UpdateCallback onUpdate;
    protected int delayMs;

    public final void sort(int[] input, UpdateCallback callback, int delayMs) throws InterruptedException {
        this.a = input;
        this.onUpdate = callback;
        this.delayMs = delayMs;

        paint(-1, -1);
        doSort();
        paint(-1, -1);
    }

    public int[] getArray() {
        return a;
    }

    protected final void compare(int i, int j) throws InterruptedException {
        checkInterrupted();
        paint(i, j);
        sleep();
    }

    protected final void swap(int i, int j) throws InterruptedException {
        checkInterrupted();
        int t = a[i]; a[i] = a[j]; a[j] = t;
        paint(i, j);
        sleep();
    }

    protected final void set(int i, int value) throws InterruptedException {
        checkInterrupted();
        a[i] = value;
        paint(i, -1);
        sleep();
    }

    private void paint(int i, int j) {
        if (onUpdate != null) onUpdate.accept(a, i, j);
    }

    private void sleep() throws InterruptedException {
        if (delayMs > 0) Thread.sleep(delayMs);
    }

    protected final void checkInterrupted() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) throw new InterruptedException();
    }

    protected abstract void doSort() throws InterruptedException;
}