package branches;

import tfc.lexy.Branch;
import tfc.lexy.LexyPosition;
import tfc.lexy.builtin.text.WhitespaceBranch;
import tokens.AccessMods;
import tokens.Primitives;
import tokens.obj.Declaration;

import java.util.ArrayList;

public class DeclarationBranch extends Branch<Character> {
    Branch<Character> ACC = AccessMods.BRANCH;
    Branch<Character> WS = WhitespaceBranch.INSTANCE;
    AnyBranch ACC_COLLECT = new AnyBranch(new Branch[]{ACC, WS});
    Branch<Character> WORD = new LetterBranch();
    AnyBranch TYPE = new AnyBranch(new Branch[]{Primitives.BRANCH, WORD});

    @Override
    public boolean shouldStart(LexyPosition<Character> position) {
//        if (WS.shouldStart(position)) return false;

        return (boolean) position.layer(this, () -> {
            int idx = position.provider.index();

            ArrayList<Object> acc_mods = new ArrayList<>();
            while (ACC_COLLECT.shouldStart(position))
                acc_mods.add(ACC_COLLECT.advance(position));
            ACC_COLLECT.end(position);

            if (position.provider.isEmpty()) {
                position.provider.setIndex(idx);
                return false;
            }

            Object type;
            if (TYPE.shouldStart(position)) {
                type = TYPE.advance(position);
                TYPE.end(position);
            } else {
                position.provider.setIndex(idx);
                return false;
            }
            Object ws0 = null;
            if (!position.provider.isEmpty() && WS.shouldStart(position))
                ws0 = WS.advance(position);

            if (position.provider.isEmpty()) {
                position.provider.setIndex(idx);
                return false;
            }

            String name = (String) WORD.advance(position);

            int pos = position.provider.index();
            position.provider.setIndex(idx);

            position.setData(new Declaration(pos, acc_mods, type, ws0, name));
            return true;
        });
    }

    @Override
    public boolean shouldEnd(LexyPosition<Character> position) {
        return true;
    }

    @Override
    public Object advance(LexyPosition<Character> position) {
        return position.layer(this, () -> {
            Declaration decl = position.getData(Declaration.class);
            position.removeData();
            position.provider.setIndex(decl.endIndex);
            return decl;
        });
    }
}
