package tfc.lexy.util;

public class StringReader extends ObjectProvider<Character> {
    String txt;

    public StringReader(String txt) {
        this.txt = txt;
    }

    public Character get(int i) {
        return txt.charAt(i + index);
    }

    @Override
    public int size() {
        return txt.length();
    }

    public char charAt(int i) {
        return txt.charAt(i + index);
    }

    public boolean startsWith(String text) {
        return txt.startsWith(text, index);
    }

    public Character poll() {
        char c = charAt(0);
        advance(1);
        return c;
    }

    @Override
    public ObjectProvider<Character> copy() {
        ObjectProvider<Character> cpy = new StringReader(txt);
        cpy.index = this.index;
        return cpy;
    }
}
