package tokens;

import tfc.lexy.Branch;
import tfc.lexy.builtin.text.WordSetBranch;

public enum FlowControl {
    IF, ELSE,
    FOR, WHILE, DO,
    SWITCH, CASE,
    BREAK, CONTINUE
    ;

    public static final Branch<Character> BRANCH = new WordSetBranch(
            new String[]{
                    "if", "else",
                    "for", "while", "do",
                    "switch", "case",
                    "break", "continue",
            },
            FlowControl::select
    );

    public static Object select(String wd) {
        for (FlowControl value : values()) {
            if (value.name().toLowerCase().equals(wd))
                return value;
        }
        return null;
    }
}
