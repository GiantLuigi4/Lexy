package tfc.lexy.builtin.generic;

import tfc.lexy.Branch;
import tfc.lexy.LexyPosition;

import java.util.ArrayList;
import java.util.List;

public class NestBranch<T> extends Branch<T> {
    List<Branch<T>> branches = new ArrayList<>();

    public void addBranch(Branch<T> branch) {
        branches.add(branch);
    }

    @Override
    public boolean shouldStart(LexyPosition<T> position) {
        return position.layer(this, () -> {
            for (Branch<T> branch : branches) {
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
        Branch<T> b = position.getData(Branch.class);
        if (b.shouldEnd(position)) {
            position.removeData();
            return true;
        }
        return false;
    }

    @Override
    public Object advance(LexyPosition<T> position) {
        return position.layer(this, () -> {
            Branch<T> b = position.getData(Branch.class);
            return b.advance(position);
        });
    }
}
