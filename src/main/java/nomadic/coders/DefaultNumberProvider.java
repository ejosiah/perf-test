package nomadic.coders;

/**
 * Created by jay on 10/08/2014.
 */
public class DefaultNumberProvider implements NumberProvider {

    private int current;

    @Override
    public int next() {
        return  ++current;
    }
}
