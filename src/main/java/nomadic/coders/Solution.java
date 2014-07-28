package nomadic.coders;

/**
 * Created by jay on 21/07/2014.
 */

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jay on 21/07/2014.
 */
public class Solution {

    public int solution(int[][] A) {
        int[][] matrix = A;
        int size = matrix.length;
        int bottom = size - 1;
        int top = 0;
        ArrayList<Integer> upperSpiral = new ArrayList<>();
        ArrayList<Integer> lowerSpiral = new ArrayList<>();

        while(size > 0){
            for(int i = 0; i < matrix[top].length; i++){
                upperSpiral.add(matrix[top][i]);
            }
            ++top; --size;
            if(size > 0){
                for(int i = matrix[bottom].length - 1;i >= 0; i--){
                    lowerSpiral.add(matrix[bottom][i]);
                }
                --bottom; --size;
            }
            if(size > 0){
                matrix = rotate(matrix, top, matrix[top].length, size);
                size = matrix.length;
                top = 0;
                bottom = size - 1;
            }
        }
        return sum(upperSpiral);
    }

    private int sum(ArrayList<Integer> list){
        Long sum = 0L;
        for(int i = 0; i < list.size(); i++){
            sum += list.get(i);
        }
        return sum >= -100000000L && sum <= 100000000L ? sum.intValue() : -1 ;
    }

    private  int[][] rotate(int[][] matrix, int top, int n, int m){
        int[][] cache = new int[n][m];
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                cache[(n -1) - j][i] = matrix[i + top][j];
            }
        }
        return cache;
    }

    public static void print(int[][] array){
        for(int i = 0; i < array.length; i++){
            System.out.println(Arrays.toString(array[i]));
        }
    }

    public static void main(String...args){
        int[][] A = {
                {-200_000_000,  3,  8,  9,  4,  1,  3, -2},
                {4,  6, 0,  3,  6,  4,  2,  1},
                {4, -5,  3,  1,  9,  5,  6,  6},
                {3,  7,  5,  3,  2,  8,  9,  4},
                {5,  3, -3,  6,  3,  2,  8,  0},
                {5,  7,  5,  3,  3, -9,  2,  2},
                {0,  4,  3,  2,  5,  7,  5,  4}};

        Solution solution = new Solution();
        int sum  = solution.solution(A);
        System.out.println(sum);
    }
}

