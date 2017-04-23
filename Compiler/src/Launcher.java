import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.FileSystems;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Tobias on 4/22/2017.
 */
public class Launcher {

    private static Map<String, Runner> availableCompilers = new HashMap<>();

    public static Gson gson = null;
    public static Result result = null;

    public static void main(String... args) {
        result = new Result();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.setPrettyPrinting().serializeNulls().create();
        if(args.length < 2) {
            result.runtimeErrors.add("Error: Please supply name of the challenge as well as the name of the class file as arguments");
            System.out.println(gson.toJson(result, Result.class));
            System.exit(0);
        }

        availableCompilers.put("Java", new JavaRunner());
        availableCompilers.put("Javascript", new JavascriptRunner());

        File root = new File(FileSystems.getDefault().getPath("../").toAbsolutePath().toString());

        Challenge challenge = null;
        try {
            challenge = gson.fromJson(new BufferedReader(new FileReader(root.getAbsolutePath() + "/Testing/Tests/" + args[0] + ".json")), Challenge.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result.runtimeErrors.add(e.getMessage());
            System.out.println(gson.toJson(result, Result.class));
            System.exit(0);
        }

        if(challenge != null) {
            String wantedCompiler = args.length < 3 ? "Java" : args[2];
            Runner runner = availableCompilers.getOrDefault(wantedCompiler, availableCompilers.get("Java"));

            CodeChecker codeChecker = runner.getCodeChecker(root, args[1]);

            for (int i = 0; i < challenge.getTests().size(); i++) {
                Test test = challenge.getTests().get(i);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<String> future = executor.submit(codeChecker.newRound(test, i));
                try {
                    if(future.get(30, TimeUnit.SECONDS).equals("error")) {
                        System.out.println(gson.toJson(result, Result.class));
                        System.exit(0);
                    }
                } catch (TimeoutException ex) {
                    future.cancel(true);
                    result.runtimeErrors.add("Error in test " + (i+1) + ": Program took too long to run!");
                    System.out.println(gson.toJson(result, Result.class));
                    System.exit(0);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } finally {
                    executor.shutdown();
                }
            }
            if (result.passedTests == challenge.getTests().size()) {
                result.success = true;
            }
        }
        System.out.println(gson.toJson(result, Result.class));
        System.exit(0);
    }
}