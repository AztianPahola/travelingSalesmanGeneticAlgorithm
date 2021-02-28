package Default;

import java.util.*;

public class travelingSalesman {

	public static void main(String args[]){
		
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

	private static int[] generatePath(int[][] input, int n) {

		final double MINIMUMIMPROVEMENT = 0.05; // Once the average improvement is lower than this value, the algorithm
												// will end
		final double MINIMUMGENERATIONS = 10;	// Minumum number of generation that will be generated
		double improvement = 0; // Running average improvement between generations
		int generation = 0; // Current number of generations, used to calculate improvement
		double previousTotalStrength;

		chromosome[] currentGeneration = generateInitialPaths(input, n);
		
		int size = currentGeneration.length;

		do {
			double totalStrength = 0; // *Add all chromosomes strengths*
			for (int i = 0; i < size; i++) {
				totalStrength += currentGeneration[i].getStrength();
			}

			double[] cumulativeStrengths = new double[size];
			
			// Initialize index 0 to avoid out of array index error 
			cumulativeStrengths[0] = currentGeneration[0].getStrength/totalStrength;
			// Add the normalized strengths of each chromosome cumulatively to
			// cumulativeStrengths array
			for (int i = 1; i < size; i++) {
				double relativeStrength = currentGeneration[i].getStrength/totalStrength;
				cumulativeStrengths[i] = cumulativeStrengths[i-1] + relativeStrength;
			}
			chromosome[] parents = new chromosome[currentGeneration.length/2]; 
			for (int i = 0; i < size/2; i++) {
				parents[i] = selectRandom(currentGeneration, cumulativeStrengths, 0, size/2);
				
			}

			chromosome[] nextGeneration = new chromosome[size];
			
			for (int i = 0; i < size; i += 2) {
				nextGeneration[i] = crossOver(parents[i], parents[i+1]);
			}

			// Calculate change to improvement
			if(generation > 0) {
				improvement += (totalStrength - previousTotalStrength) / previousTotalStrength / ++generation;
			}
			previousTotalStrength = totalStrength;

		} while (generation >= MINIMUMGENERATIONS && improvement >= MINIMUMIMPROVEMENT);
	}
	
	private static chromosome selectRandom(chromosome[] chromos, double[] ranges, int low, int high){
			
		// Generate a random double, select the correct chromosome based on that random
		double random = Math.random();
		int mid = low+high/2;
		while(low < high) {
			
			if(ranges[mid] < random) {
				if(ranges[mid+1] >= random) 
					return chromos[mid+1];
				else 
					low = mid;
			}
			else {
				if(mid == 0 || ranges[mid-1] < random) 
					return chromos[mid];
				else
					high = mid;
			}
			mid = low+high/2;
		}
		return chromos[mid];
	
	}

}
