import javax.script.Invocable;
import java.util.Arrays;

/**
 * Created by Tobias on 4/23/2017.
 */
public class JavascriptCodeChecker extends CodeChecker {

    Invocable engine;
    String script;

    public JavascriptCodeChecker(Invocable invocableEngine, String script) {
        this.engine = invocableEngine;
        this.script = script;
    }

    @Override
    public String call() throws Exception {
        String check = "";
        Object actual = new Object();
        try {
            actual = engine.invokeFunction("program", new Object[]{test.getArguments()});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compareResults(actual);
    }
}
