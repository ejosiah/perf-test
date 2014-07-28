package nomadic.coders;

import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;

/**
 * Created by jay on 29/07/2014.
 */
public class SolutionBenchmark extends Benchmark {

    public int timeMyOperation(int reps){
        Solution solution = new Solution();
        int[][] A = {
                {-200_000_000,  3,  8,  9,  4,  1,  3, -2},
                {4,  6, 0,  3,  6,  4,  2,  1},
                {4, -5,  3,  1,  9,  5,  6,  6},
                {3,  7,  5,  3,  2,  8,  9,  4},
                {5,  3, -3,  6,  3,  2,  8,  0},
                {5,  7,  5,  3,  3, -9,  2,  2},
                {0,  4,  3,  2,  5,  7,  5,  4}};
        int dummy = 0;
        for(int i = 0; i < reps; i++){
            dummy |= solution.solution(A);
        }
        return dummy;
    }

    public static void main(String[] args){
        CaliperMain.main(SolutionBenchmark.class, args);
    }
}
