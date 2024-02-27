package tokens;

import tfc.lexy.Branch;
import tfc.lexy.builtin.text.WordSetBranch;

public enum AccessMods {
    PUBLIC, PRIVATE, PROTECTED,
    NATIVE, FINAL, STATIC,
    SYNCHRONIZED,
    INTERFACE, ABSTRACT;

    public static final Branch<Character> BRANCH = new WordSetBranch(
            new String[]{
                    "public", "private", "protected",
                    "native", "final", "static",
                    "synchronized",
                    "interface", "abstract",
            },
            AccessMods::select
    );

    public static Object select(String wd) {
        for (AccessMods value : values()) {
            if (value.name().toLowerCase().equals(wd))
                return value;
        }
        return null;
    }
}
