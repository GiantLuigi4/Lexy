package tfc.lexy.builtin.generic;

import tfc.lexy.Branch;
import tfc.lexy.LexyPosition;
import tfc.lexy.util.StringReader;

import java.util.function.Function;

public class FallbackBranch<T> extends Branch<T> {
    Function<T, Object> provider;

    public FallbackBranch(Function<T, Object> provider) {
        this.provider = provider;
    }

    @Override
    public boolean shouldStart(LexyPosition<T> position) {
        return true;
    }

    @Override
    public boolean shouldEnd(LexyPosition<T> position) {
        return true;
    }

    @Override
    public Object advance(LexyPosition<T> position) {
        return provider.apply(position.provider.poll());
    }
}
