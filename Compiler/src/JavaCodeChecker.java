import java.util.Arrays;

/**
 * Created by Tobias on 4/23/2017.
 */
public class JavaCodeChecker extends CodeChecker {
    Object prg;
    java.lang.reflect.Method method;

    public JavaCodeChecker(Object program, java.lang.reflect.Method method, Result result) {
        super(result);
        this.prg = program;
        this.method = method;
    }

    @Override
    public String call() throws Exception {
        String check = "";
        Object actual = new Object();
        try {
            actual = method.invoke(prg, new Object[]{test.getArguments()});
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (actual == null) {
            result.errors.add("Error in test " + (round + 1) + ": Number of returned variables is wrong");
            check = "error";
        } else {
            Object expected = test.getExpectedReturn();
            if (expected.toString().indexOf(".0") == expected.toString().length() - 2) {
                expected = Integer.parseInt(expected.toString().replace(".0", ""));
            }
            if (!actual.equals(expected)) {
                result.errors.add("Error in test " + (round + 1) + ": Output " + actual + " does not satisfy the challenge with inputs " + Arrays.toString(test.getArguments()) + "!");
                check="error";
            } else {
                result.passedTests += 1;
            }
        }
        return check;
    }
}
