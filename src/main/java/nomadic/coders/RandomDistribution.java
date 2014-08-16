package nomadic.coders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by jay on 13/08/2014.
 */
public class RandomDistribution {

    public static void main(String[] args){
        randomSelection();
        shuffleSelection();
    }

    private static  void randomSelection(){
        System.out.println("Random selection");
        int[] freq = new int[5];
        Random rng = new Random();
        for(int i = 0; i < 10000; i++){
            int index = rng.nextInt(5);
            freq[index] += 1;
        }

        for(int i = 0; i < 5; i++){
            System.out.printf("index: %s, frequency: %s\n", i, freq[i]);
        }
    }
    private static  void shuffleSelection(){
        System.out.println("\n\nshuffle selection");
        int[] freq = new int[5];
        Random rng = new Random();
        List<Integer> indexes = get();
        for(int i = 0; i < 10250; i++){
            int pos = rng.nextInt(indexes.size());
            int index = indexes.remove(pos);
            freq[index] += 1;
            if(indexes.isEmpty()){
                indexes = get();
            }
        }


        for(int i = 0; i < 5; i++){
            System.out.printf("index: %s, frequency: %s\n", i, freq[i]);
        }
    }

    private static List<Integer> get(){
        return  new ArrayList<Integer>(){
            {   add(0);
                add(1);
                add(2);
                add(3);
                add(4);
            }
        };
    }
}
