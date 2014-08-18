package nomadic.coders;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.runner.JUnitCore;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    public static void main(String[] args){
        JUnitCore junit = new JUnitCore();
        for(int i = 0; i < 500; i += 100){
            ListTest.n = i;
            junit.run(ListTest.class);
        }
    }
}
