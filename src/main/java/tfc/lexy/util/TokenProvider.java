package tfc.lexy.util;

import tfc.lexy.Lexy;

import java.util.ArrayList;

public class TokenProvider<T, V> extends ObjectProvider<V> {
    Lexy<T> lexy;
    ArrayList<V> objs = new ArrayList<>();
    boolean ended = false;
    int buffer;

    public TokenProvider(Lexy<T> lexy, int buffer) {
        this.lexy = lexy;
        this.buffer = buffer;
        get(0);
    }

    @Override
    public V get(int i) {
        i += index;
        while (objs.size() < i + buffer) {
            if (lexy.shouldEnd()) {
                ended = true;
                break;
            }

            objs.add((V) lexy.advance());
        }
        return objs.get(i);
    }

    @Override
    public int size() {
        return objs.size();
    }

    @Override
    public ObjectProvider<V> copy() {
        Lexy<T> lexy1 = new Lexy<>();
        lexy1.setPosition(lexy1.getPosition().copy());
        TokenProvider<T, V> copy = new TokenProvider<>(lexy1, buffer);
        copy.ended = ended;
        return copy;
    }
}
