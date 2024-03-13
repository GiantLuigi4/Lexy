package branches;

import tfc.lexy.Branch;
import tfc.lexy.LexyPosition;

public class AnyBranch<T> extends Branch<T> {
    Branch<T>[] branches;

    public AnyBranch(Branch<T>[] branches) {
        this.branches = branches;
    }

    @Override
    public boolean shouldStart(LexyPosition<T> position) {
        return (boolean) position.layer(this, () -> {
            for (Branch<T> branch : branches) {
                if (position.provider.isEmpty())
                    break;

                if (branch.shouldStart(position)) {
                    position.setData(branch);
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public boolean shouldEnd(LexyPosition<T> position) {
        return (boolean) position.layer(this, () -> {
            Branch<T> b = position.getData(Branch.class);
            if (b.shouldEnd(position)) {
                position.removeData();
                return true;
            }
            return false;
        });
    }

    @Override
    public Object advance(LexyPosition<T> position) {
        return position.layer(this, () -> {
            Branch<T> b = position.getData(Branch.class);
            return b.advance(position);
        });
    }

    public void end(LexyPosition<T> position) {
        position.layer(this, (Runnable) position::removeData);
    }
}
