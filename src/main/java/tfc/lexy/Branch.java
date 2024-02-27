package tfc.lexy;

import tfc.lexy.util.StringReader;

public abstract class Branch<T> {
    public abstract boolean shouldStart(LexyPosition<T> position);
    public abstract boolean shouldEnd(LexyPosition<T> position);
    public abstract Object advance(LexyPosition<T> position);
}
