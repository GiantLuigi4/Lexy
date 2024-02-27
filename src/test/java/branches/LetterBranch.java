package branches;

import tfc.lexy.LexyPosition;
import tfc.lexy.builtin.text.WordBranch;
import tfc.lexy.util.StringReader;

public class LetterBranch extends WordBranch {
    @Override
    public boolean shouldStart(LexyPosition<Character> position) {
        char c = ((StringReader)position.provider).charAt(0);
        return Character.isLetter(c);
    }

    @Override
    public boolean shouldEnd(LexyPosition<Character> position) {
        if (!shouldStart(position)) {
            if (position.provider.isEmpty()) return true;

            char c = ((StringReader) position.provider).charAt(0);

            switch (c) {
                case '_':
                case '$':
                    return false;
            }

            return !Character.isDigit(c);
        }
        return false;
    }
}
