package tfc.lexy.builtin.generic;

import tfc.lexy.Branch;
import tfc.lexy.LexyPosition;
import tfc.lexy.util.data.DataBox;

public class FirstBranch<T> extends Branch<T> {
    Branch<T>[] branches;

    public FirstBranch(Branch<T>[] branches) {
        this.branches = branches;
    }

    @Override
    public boolean shouldStart(LexyPosition<T> position) {
        return position.layer(this, () -> {
            for (Branch<T> branch : branches) {
                if (position.provider.isEmpty())
                    break;

                if (branch.shouldStart(position)) {
                    position.setData(DataBox.of(branch));
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public boolean shouldEnd(LexyPosition<T> position) {
        return position.layer(this, () -> {
            DataBox<Branch<T>> b = position.getData(DataBox.class);
            if (b.get().shouldEnd(position)) {
                position.removeData();
                return true;
            }
            return false;
        });
    }

    @Override
    public Object advance(LexyPosition<T> position) {
        return position.layer(this, () -> {
            DataBox<Branch<T>> b = position.getData(DataBox.class);
            return b.get().advance(position);
        });
    }

    public void end(LexyPosition<T> position) {
        position.layer(this, (Runnable) position::removeData);
    }
}
