import branches.DeclarationBranch;
import tfc.lexy.Lexy;
import tfc.lexy.builtin.generic.FallbackBranch;
import tfc.lexy.builtin.text.WhitespaceBranch;
import tfc.lexy.util.StringReader;
import tokens.AccessMods;
import tokens.Constants;
import tokens.FlowControl;
import tokens.Primitives;

public class Main {
    public static void main(String[] args) {
        String test = """
                public static void main(String[] args) {
                    boolean value = false;
                }
                """;

        Lexy<Character> lexy = new Lexy<>();

        lexy.addBranch(WhitespaceBranch.INSTANCE);
        lexy.addBranch(new DeclarationBranch());
        lexy.addBranch(FlowControl.BRANCH);
        lexy.addBranch(Constants.BRANCH);
        lexy.addBranch(AccessMods.BRANCH);
        lexy.addBranch(Primitives.BRANCH);
        lexy.addBranch(new FallbackBranch<>((c) -> c));

        StringReader reader = new StringReader(test);
        lexy.setup(reader);
        while (!lexy.shouldEnd()) {
            System.out.println(lexy.advance());
        }
        lexy.getPosition().copy();
    }
}
