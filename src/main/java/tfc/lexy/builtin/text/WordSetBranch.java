package tfc.lexy.builtin.text;

import tfc.lexy.Branch;
import tfc.lexy.LexyPosition;
import tfc.lexy.util.StringReader;

import java.util.function.Function;

public class WordSetBranch extends Branch<Character> {
    String[] accepted;
    Function<String, Object> mapper;

    public WordSetBranch(String[] accepted, Function<String, Object> mapper) {
        this.accepted = accepted;
        this.mapper = mapper;
    }

    @Override
    public boolean shouldStart(LexyPosition<Character> position) {
        for (String s : accepted) {
            if (((StringReader) position.provider).startsWith(s))
                return true;
        }
        return false;
    }

    @Override
    public boolean shouldEnd(LexyPosition<Character> position) {
        return true;
    }

    @Override
    public Object advance(LexyPosition<Character> position) {
        for (String s : accepted) {
            if (((StringReader) position.provider).startsWith(s)) {
                position.provider.advance(s.length());
                return mapper.apply(s);
            }
        }
        throw new RuntimeException("Invalid input.");
    }
}
