package tfc.lexy.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListObjectProvider<T> extends ObjectProvider<T> {
    List<T> internal;

    public ListObjectProvider(List<T> internal) {
        this.internal = internal;
    }

    @Override
    public T get(int i) {
        return internal.get(index + i);
    }

    @Override
    public int size() {
        return internal.size();
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
        ObjectProvider<T> cpy = new ListObjectProvider<>(internal);
        cpy.index = this.index;
        return cpy;
    }
}
