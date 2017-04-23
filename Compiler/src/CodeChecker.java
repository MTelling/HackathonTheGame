import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * Created by Tobias on 4/23/2017.
 */
public abstract class CodeChecker implements Callable<String> {
    Test test;
    int round;

    public CodeChecker newRound(Test test, int round) {
        this.test = test;
        this.round = round;
        return this;
    }

    @Override
    abstract public String call() throws Exception;

    protected String compareResults(Object actual) {
        if (actual == null) {
            Launcher.result.errors.add("Error in test " + (round + 1) + ": Number of returned variables is wrong");
            return "error";
        } else {
            Object expected = test.getExpectedReturn();
            if (expected.toString().indexOf(".0") == expected.toString().length() - 2) {
                expected = Integer.parseInt(expected.toString().replace(".0", ""));
            }
            if (!actual.equals(expected)) {
                Launcher.result.errors.add("Error in test " + (round + 1) + ": Output " + actual + " does not satisfy the challenge with inputs " + Arrays.toString(test.getArguments()) + "!");
                return "error";
            } else {
                Launcher.result.passedTests += 1;
            }
        }
        return "";
    }
}