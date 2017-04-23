import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Tobias on 4/23/2017.
 */
public class JavascriptRunner implements Runner {
    @Override
    public CodeChecker getCodeChecker(File classPath, String className) {

        StringBuilder sb = new StringBuilder();
        try {
            Files.lines(Paths.get(classPath.getAbsolutePath() + "/Testing/" + className + ".js")).forEachOrdered(sb::append);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String script = sb.toString();
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();

        ScriptEngine engine = factory.getScriptEngine(new jsSecurityFilter());
        try {
            engine.eval(script);
        } catch (Exception e) {
            System.out.println("Exception caught: " + e.toString());
        }
        Invocable invocableEngine = (Invocable) engine;

        return new JavascriptCodeChecker(invocableEngine, script);
    }

    class jsSecurityFilter implements ClassFilter {
        @Override
        public boolean exposeToScripts(String s) {
            return s.contains("java.lang");
        }
    }
}
