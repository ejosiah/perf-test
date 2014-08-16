package nomadic.coders;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by josiah on 10/08/14.
 */
public class Logger {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Queue<Object> logQueue = new ArrayBlockingQueue<Object>(10);

    public static void log(Object obj){

    }
}
