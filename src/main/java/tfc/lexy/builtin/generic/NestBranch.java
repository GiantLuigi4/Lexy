package tfc.lexy.builtin.generic;

import tfc.lexy.Branch;
import tfc.lexy.LexyPosition;
import tfc.lexy.util.data.DataBox;

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
                    position.setData(DataBox.of(branch));
                    return true;
                }
            }

            return false;
        });
    }

    @Override
    public boolean shouldEnd(LexyPosition<T> position) {
        DataBox<Branch<T>> b = position.getData(DataBox.class);
        if (b.get().shouldEnd(position)) {
            position.removeData();
            return true;
        }
        return false;
    }

    @Override
    public Object advance(LexyPosition<T> position) {
        return position.layer(this, () -> {
            DataBox<Branch<T>> b = position.getData(DataBox.class);
            return b.get().advance(position);
        });
    }
}
