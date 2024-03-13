package tfc.lexy;

import tfc.lexy.util.ObjectProvider;

import java.util.ArrayList;

public class Lexy<T> {
    ArrayList<Branch<T>> branches = new ArrayList<>();

    public void addBranch(Branch<T> branch) {
        branches.add(branch);
    }

    LexyPosition<T> position;

    public void setup(ObjectProvider<T> reader) {
        position = new LexyPosition<>(reader);
    }

    public Object advance() {
        if (shouldEnd()) return null;

        if (position.active != null) {
            if (position.active.shouldEnd(position)) {
                position.active = null;
            }
        }

        if (position.active == null) {
            for (Branch branch : branches) {
                if (branch.shouldStart(position)) {
                    position.active = branch;
                    break;
                }
            }
        }

        return position.active.advance(position);
    }

    public boolean shouldEnd() {
        return position.provider.isEmpty();
    }

    public LexyPosition<T> getPosition() {
        return position;
    }

    public void setPosition(LexyPosition<T> position) {
        this.position = position;
    }
}
