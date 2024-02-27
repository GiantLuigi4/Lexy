package tokens.obj;

import java.util.ArrayList;

public class Declaration {
    public final int endIndex;
    public final ArrayList<Object> accMods;
    public final Object type;
    public final Object whiteSpace;
    public final String name;

    public Declaration(int endIndex, ArrayList<Object> accMods, Object type, Object whiteSpace, String name) {
        this.endIndex = endIndex;
        this.accMods = accMods;
        this.type = type;
        this.whiteSpace = whiteSpace;
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder dat = new StringBuilder();
        for (Object accMod : accMods)
            dat.append(accMod.toString());
        dat.append(type.toString());
        if (whiteSpace != null)
            dat.append(whiteSpace.toString());
        dat.append(name);
        return dat.toString();
    }
}
