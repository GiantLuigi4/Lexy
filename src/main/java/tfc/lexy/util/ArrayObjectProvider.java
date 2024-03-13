package tfc.lexy.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ArrayObjectProvider<T> extends ObjectProvider<T> {
    T[] internal;

    public ArrayObjectProvider(T[] internal) {
        this.internal = internal;
    }

    @Override
    public T get(int i) {
        return internal[index + i];
    }

    @Override
    public int size() {
        return internal.length;
    }

    public List<T> filter(Function<T, Boolean> filter) {
        ArrayList<T> ts = new ArrayList<>();
        while (!isEmpty()) {
            T t;
            if (filter.apply(t = get(0))) break;
            else {
                ts.add(t);
                index++;
            }
        }
        return ts;
    }

    @Override
    public ObjectProvider<T> copy() {
        ObjectProvider<T> cpy = new ArrayObjectProvider<>(internal);
        cpy.index = this.index;
        return cpy;
    }
}
