package tokens;

import tfc.lexy.Branch;
import tfc.lexy.builtin.text.WordSetBranch;

public enum Primitives {
    BYTE, SHORT, INT, LONG,
    FLOAT, DOUBLE,
    CHAR, BOOLEAN,
    VOID;

    public static final Branch<Character> BRANCH = new WordSetBranch(
            new String[]{
                    "byte", "short", "int", "long",
                    "float", "double",
                    "char", "boolean",
                    "void"
            },
            Primitives::select
    );

    public static Object select(String wd) {
        for (Primitives value : values()) {
            if (value.name().toLowerCase().equals(wd))
                return value;
        }
        return null;
    }
}
