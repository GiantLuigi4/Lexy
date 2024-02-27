package tokens;

import tfc.lexy.Branch;
import tfc.lexy.builtin.text.WordSetBranch;

public enum Constants {
    TRUE, FALSE, NULL
    ;

    public static final Branch<Character> BRANCH = new WordSetBranch(
            new String[]{
                    "true", "false", "null"
            },
            Constants::select
    );

    public static Object select(String wd) {
        for (Constants value : values()) {
            if (value.name().toLowerCase().equals(wd))
                return value;
        }
        return null;
    }
}
