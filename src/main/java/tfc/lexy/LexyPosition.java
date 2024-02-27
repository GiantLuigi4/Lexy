package tfc.lexy;

import tfc.lexy.util.ObjectProvider;
import tfc.lexy.util.StringReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class LexyPosition<T> {
    protected Branch<T> active;
    private final HashMap<Branch<T>, HashMap<Integer, Object>> datas = new HashMap<>();

    public final ObjectProvider<T> provider;
    int layer;

    public Object layer(Branch<T> b, Supplier<Object> r) {
        layer++;
        Branch<T> prev = active;
        active = b;
        Object o = r.get();
        active = prev;
        layer--;
        return o;
    }

    public <A> A getData(Class<A> cls) {
        HashMap<Integer, Object> datas = this.datas.computeIfAbsent(active, (k) -> new HashMap<>());
        return (A) datas.get(layer);
    }

    public <A> A setData(A obj) {
        HashMap<Integer, Object> datas = this.datas.computeIfAbsent(active, (k) -> new HashMap<>());
        return (A) datas.put(layer, obj);
    }

    public <A> A removeData() {
        HashMap<Integer, Object> datas = this.datas.computeIfAbsent(active, (k) -> new HashMap<>());
        A val = (A) datas.remove(layer);
        if (datas.isEmpty())
            this.datas.remove(active);
        return val;
    }

    public LexyPosition(ObjectProvider<T> reader) {
        this.provider = reader;
    }

    public LexyPosition(Branch<T> active, ObjectProvider<T> reader) {
        this.active = active;
        this.provider = reader;
    }

    public LexyPosition<T> copy() {
        if (layer != 0) throw new RuntimeException("Cannot copy a position while in a layer.");
        if (!datas.isEmpty()) throw new RuntimeException("Data is not empty");
        return new LexyPosition<>(active, provider);
    }

    public boolean canCopy() {
        return layer == 0 && datas.isEmpty();
    }
}
