import java.util.Arrays;

/**
 * Created by Tobias on 4/23/2017.
 */
public class JavaCodeChecker extends CodeChecker {
    Object prg;
    java.lang.reflect.Method method;

    public JavaCodeChecker(Object program, java.lang.reflect.Method method) {
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
        return compareResults(actual);
    }
}
