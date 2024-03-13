package tfc.lexy.util.data;

/**
 * used for immutable data
 *
 * @param <T> the type to point to
 */
public class DataBox<T> implements LexyCopyable {
    public final T ptr;

    public DataBox(T ptr) {
        this.ptr = ptr;
    }

    public static <T> DataBox<T> of(T obj) {
        return new DataBox<>(obj);
    }

    public T get() {
        return ptr;
    }

    @Override
    public LexyCopyable copy() {
        return this;
    }
}
