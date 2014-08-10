package nomadic.coders;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jay on 10/08/2014.
 */
public class AtomicNumberProvider extends DefaultNumberProvider {

    private AtomicInteger current = new AtomicInteger(0);

    @Override
    public int next(){
        return current.incrementAndGet();
    }
}
