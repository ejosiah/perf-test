package nomadic.coders;

/**
 * Created by jay on 10/08/2014.
 */
public class SynchronizedNumberProvider extends DefaultNumberProvider {

    @Override
    public synchronized int next(){
        return super.next();
    }
}
