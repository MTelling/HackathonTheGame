import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Tobias on 4/23/2017.
 */
public class JavaRunner implements Runner {

    @Override
    public CodeChecker getCodeChecker(File classPath, String className) {
        Object prg = null;
        java.lang.reflect.Method method = null;

        try {
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{classPath.toURI().toURL()});
            Class<?> aClass = Class.forName("Testing." + className, true, classLoader);
            prg = aClass.newInstance();

            method = aClass.getMethod("run", String[].class);
        } catch (Exception e) {
            e.printStackTrace();
            Launcher.result.runtimeErrors.add(e.getMessage());
            System.out.println(Launcher.gson.toJson(Launcher.result, Result.class));
            System.exit(0);
        }
        return new JavaCodeChecker(prg, method);
    }

}

