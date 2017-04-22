import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

/**
 * Created by Tobias on 4/22/2017.
 */
public class Launcher {

    public static void main(String... args) {
        Result result = new Result();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().serializeNulls().create();
        if(args.length < 1) {
            result.compilerErrors.add("Error: Please supply name of program to test");
            System.out.println(gson.toJson(result, Result.class));
            System.exit(0);
        }

        File compilerRoot = new File(Paths.get("").toString());
        String absPath = compilerRoot.getAbsolutePath();
        File root = new File(Paths.get(absPath.substring(0, absPath.lastIndexOf("\\"))).toString());

        Challenge challenge = null;
        try {
            challenge = gson.fromJson(new BufferedReader(new FileReader(root.getAbsolutePath() + "/Testing/Tests/" + args[0] + ".json")), Challenge.class);
        } catch (FileNotFoundException e) {
            result.compilerErrors.add(e.getMessage());
        }

        if(challenge != null) {
            try {
                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
                Class<?> aClass = Class.forName("Testing.Program", true, classLoader);
                Object prg = aClass.newInstance();

                java.lang.reflect.Method method = aClass.getMethod("run", String[].class);
                for(int i = 0; i < challenge.getTests().size(); i++) {
                    Test test = challenge.getTests().get(i);
                    Object[] actual = (Object[]) method.invoke(prg, new Object[]{test.getArguments()});
                    if(actual.length != test.getExpectedReturns().length) {
                        result.errors.add("Error in test " + (i+1) + ": Number of returned variables is wrong");
                    } else {
                        for(int k = 0; k < test.getExpectedReturns().length; k++) {
                            Object expected = test.getExpectedReturns()[k];
                            if(expected.toString().indexOf(".0") == expected.toString().length() - 2) {
                                expected = Integer.parseInt(expected.toString().replace(".0", ""));
                            }
                            if(!actual[k].equals(expected)) {
                                result.errors.add("Error in test " + (i+1) + ": " + actual[k] + " should be " + expected);
                            } else {
                                result.passedTests += 1;
                            }
                        }
                    }
                }
                if(result.passedTests == challenge.getTests().size()) {
                    result.success = true;
                }
            } catch (Exception e) {
                result.compilerErrors.add(e.getMessage());
            }
        }
        System.out.println(gson.toJson(result, Result.class));
    }

}
