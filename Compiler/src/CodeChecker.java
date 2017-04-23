import java.util.concurrent.Callable;

/**
 * Created by Tobias on 4/23/2017.
 */
public abstract class CodeChecker implements Callable<String> {
    Result result;
    Test test;
    int round;

    public CodeChecker(Result result) {
        this.result = result;
    }

    public CodeChecker newRound(Test test, int round) {
        this.test = test;
        this.round = round;
        return this;
    }

    @Override
    abstract public String call() throws Exception;
}