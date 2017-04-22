import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.concurrent.*;

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
            System.out.println(gson.toJson(result, Result.class));
            System.exit(0);
        }

        if(challenge != null) {
            Object prg = null;
            java.lang.reflect.Method method = null;
            try {
                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
                Class<?> aClass = Class.forName("Testing.Program", true, classLoader);
                prg = aClass.newInstance();

                method = aClass.getMethod("run", String[].class);
            } catch (Exception e) {
                result.compilerErrors.add(e.getMessage());
                System.out.println(gson.toJson(result, Result.class));
                System.exit(0);
            }
            for (int i = 0; i < challenge.getTests().size(); i++) {
                Test test = challenge.getTests().get(i);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<String> future = executor.submit(new CodeChecker(prg, method, result, test, i));
                try {
                    if(future.get(30, TimeUnit.SECONDS).equals("error")) {
                        System.out.println(gson.toJson(result, Result.class));
                        System.exit(0);
                    }
                } catch (TimeoutException ex) {
                    future.cancel(true);
                    result.compilerErrors.add("Error in test " + (i+1) + ": Program took too long to run!");
                    System.out.println(gson.toJson(result, Result.class));
                    System.exit(0);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            if (result.passedTests == challenge.getTests().size()) {
                result.success = true;
            }
        }
        System.out.println(gson.toJson(result, Result.class));
    }
}

class CodeChecker implements Callable<String> {
    Object prg;
    java.lang.reflect.Method method;
    Result result;
    Test test;
    int round;

    public CodeChecker(Object program, java.lang.reflect.Method method, Result result, Test test, int round) {
        this.prg = program;
        this.method = method;
        this.result = result;
        this.test = test;
        this.round = round;
    }

    @Override
    public String call() throws Exception {
        String check = "";
        Object[] actual = new Object[0];
        try {
            actual = (Object[]) method.invoke(prg, new Object[]{test.getArguments()});
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (actual.length != test.getExpectedReturns().length) {
            result.errors.add("Error in test " + (round + 1) + ": Number of returned variables is wrong");
            check = "error";
        } else {
            for (int k = 0; k < test.getExpectedReturns().length; k++) {
                Object expected = test.getExpectedReturns()[k];
                if (expected.toString().indexOf(".0") == expected.toString().length() - 2) {
                    expected = Integer.parseInt(expected.toString().replace(".0", ""));
                }
                if (!actual[k].equals(expected)) {
                    result.errors.add("Error in test " + (round + 1) + ": " + actual[k] + " should be " + expected);
                    check="error";
                } else {
                    result.passedTests += 1;
                }
            }
        }
        return check;
    }
}
