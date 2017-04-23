import java.io.File;
import java.util.concurrent.Callable;

/**
 * Created by Tobias on 4/23/2017.
 */
public interface Runner {

    CodeChecker getCodeChecker(File classPath, String className);

}
