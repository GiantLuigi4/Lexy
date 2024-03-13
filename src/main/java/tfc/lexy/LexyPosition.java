package tfc.lexy;

import tfc.lexy.util.ObjectProvider;
import tfc.lexy.util.data.LexyCopyable;

import java.util.HashMap;
import java.util.function.Supplier;

public class LexyPosition<T> {
    protected Branch<T> active;
    private final HashMap<Branch<T>, HashMap<Integer, Object>> datas = new HashMap<>();

    public final ObjectProvider<T> provider;
    int layer;

    int datUncopyable = 0;

    public <A> A layer(Branch<T> b, Supplier<A> r) {
        layer++;
        Branch<T> prev = active;
        active = b;
        A o = r.get();
        active = prev;
        layer--;
        return o;
    }

    public void layer(Branch<T> b, Runnable r) {
        layer++;
        Branch<T> prev = active;
        active = b;
        r.run();
        active = prev;
        layer--;
    }

    public <A> A getData(Class<A> cls) {
        HashMap<Integer, Object> datas = this.datas.computeIfAbsent(active, (k) -> new HashMap<>());
        return (A) datas.get(layer);
    }

    public <A> A setData(A obj) {
        HashMap<Integer, Object> datas = this.datas.computeIfAbsent(active, (k) -> new HashMap<>());

        A old = (A) datas.put(layer, obj);
        if (old != null && !(old instanceof LexyCopyable)) datUncopyable--;
        if (obj != null && !(obj instanceof LexyCopyable)) datUncopyable++;

        return old;
    }

    public <A> A removeData() {
        HashMap<Integer, Object> datas = this.datas.computeIfAbsent(active, (k) -> new HashMap<>());

        A val = (A) datas.remove(layer);
        if (val != null && !(val instanceof LexyCopyable)) datUncopyable--;

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
        if (datUncopyable != 0) throw new RuntimeException("Data is not copy safe.");

        LexyPosition<T> copy = new LexyPosition<>(active, provider.copy());
        datas.forEach((k, v) -> {
            HashMap<Integer, Object> mp = new HashMap<>();
            copy.datas.put(k, mp);
            v.forEach((k1, v1) -> {
                mp.put(k1, ((LexyCopyable) v1).copy());
            });
        });

        return copy;
    }

    public boolean canCopy() {
        return layer == 0 && datas.isEmpty();
    }
}
