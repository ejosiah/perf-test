package nomadic.coders;

import java.util.concurrent.ThreadFactory;

/**
 * Created by jay on 16/08/2014.
 */
public class MyThreadFactory implements ThreadFactory {

    private static final String NAME_PREFIX = "Queue-Tester-";
    private static final int nThreads = 0;

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, NAME_PREFIX + +nThreads);
    }
}
