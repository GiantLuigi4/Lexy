package tfc.lexy.builtin.text;

import tfc.lexy.Branch;
import tfc.lexy.LexyPosition;
import tfc.lexy.util.StringReader;

public class WordBranch extends Branch<Character> {
    public static final WordBranch INSTANCE = new WordBranch();

    protected WordBranch() {
    }

    @Override
    public boolean shouldStart(LexyPosition<Character> position) {
        return !Character.isWhitespace(((StringReader) position.provider).charAt(0));
    }

    @Override
    public boolean shouldEnd(LexyPosition<Character> position) {
        return position.provider.isEmpty() || !shouldStart(position);
    }

    @Override
    public Object advance(LexyPosition<Character> position) {
        StringBuilder dat = new StringBuilder();
        dat.append(position.provider.poll());
        while (!shouldEnd(position))
            dat.append(position.provider.poll());
        return dat.toString();
    }
}
