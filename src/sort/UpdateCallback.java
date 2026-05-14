package sort;

@FunctionalInterface
public interface UpdateCallback {
    void accept(int[] array, int i, int j);
}