package tfc.lexy.util;

public abstract class ObjectProvider<T> {
    protected int index;

    public ObjectProvider() {
    }

    public abstract T get(int i);

    public abstract int size();

    public void advance(int amt) {
        this.index += amt;
    }

    public int remaining() {
        return size() - index;
    }

    public T poll() {
        T c = get(0);
        advance(1);
        return c;
    }

    public int index() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isEmpty() {
        return index == size();
    }

    public abstract ObjectProvider<T> copy();
}
