package nomadic.coders;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import com.carrotsearch.junitbenchmarks.annotation.LabelType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.JUnitCore;

import java.security.SecureRandom;
import java.util.*;

/**
 * Created by jay on 17/08/2014.
 */
@AxisRange(min = 0, max = 1)
@BenchmarkMethodChart(filePrefix = "benchmark-list")
@BenchmarkHistoryChart(labelWith = LabelType.CUSTOM_KEY, maxRuns = 100)
public class ListTest {

    private static final Random RNG = new SecureRandom();
    public static int n = 60000;

    @Rule
    public TestRule BenchmarckRule = new BenchmarkRule();

    public static void insert(int n, List<Integer> list){
        int position = Collections.binarySearch(list, n);
        position = position < 0 ? Math.abs(position) - 1 : position;
        list.add(position, n);
    }

    public static void remove(List<?> list){
        int position = RNG.nextInt(list.size());
        list.remove(position);
    }

    @Test
    public void testArrayList(){
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < n; i++){
            int num = RNG.nextInt();
            insert(num, list);
        }
        for(int i = 0; i < list.size(); i++){
            remove(list);
        }
    }

    @Test
    public void testLinkedList(){
        List<Integer> list = new LinkedList<>();
        for(int i = 0; i < n; i++){
            int num = RNG.nextInt();
            insert(num, list);
        }
        for(int i = 0; i < list.size(); i++){
            remove(list);
        }
    }

    @Test
    public void testVector(){
        List<Integer> list = new Vector<>();
        for(int i = 0; i < n; i++){
            int num = RNG.nextInt();
            insert(num, list);
        }
        for(int i = 0; i < list.size(); i++){
            remove(list);
        }
    }
}
