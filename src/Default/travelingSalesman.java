package Default;

import java.util.*;

public class travelingSalesman {

	public static void main(String args()){
		int n = 5;
		int[][] adjecencyMatrix = {{0, 14, 4, 10, 20},
								   {14 , 0 , 7 , 8 ,7},
								   {4, 5, 0, 7 , 16},
								   {11, 7, 9, 0, 2},
								   {18, 7, 17, 4, 0}};
		int[] resultingPath = GeneratePath(adjecencyMatrix, n);
		for(int x : resultingPath){
			System.out.println(x);
		}
	}
}
