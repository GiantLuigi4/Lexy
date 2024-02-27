package tfc.lexy.builtin.generic;

import tfc.lexy.Branch;
import tfc.lexy.LexyPosition;

public class ObjSetBranch<T> extends Branch<T> {
    T[] accepted;

    public ObjSetBranch(T[] accepted) {
        this.accepted = accepted;
    }

    @Override
    public boolean shouldStart(LexyPosition<T> lexyPosition) {
        T curr = lexyPosition.provider.get(0);
        for (T t : accepted)
            if (t.equals(curr))
                return true;
        return false;
    }

    @Override
    public boolean shouldEnd(LexyPosition<T> lexyPosition) {
        return true;
    }

    @Override
    public Object advance(LexyPosition<T> lexyPosition) {
        return lexyPosition.provider.poll();
    }
}
