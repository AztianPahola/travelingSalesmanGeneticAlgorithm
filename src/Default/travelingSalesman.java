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

		final double MINIMUMIMPROVEMENT = 0.05; // Once the average improvement is lower than this value, the algorithm will end
		final double MINIMUMGENERATIONS = 10;	// Minimum number of generation that will be generated
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
			
			nextGeneration = crossOver(parents, size, n);
			

			// Calculate change to improvement, avoiding a divide by zero error
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
	
	private static chromosome[] generateInitialPaths(int[][] input, int n) {
		chromosome[] paths = new chromosome[2^n];
		for (int i = 0; i < (2^n); i++) {
			paths[i] = new chromosome(input, n);
		}
		return paths;
	}
	
	private static chromosome[] crossOver(chromosome[] padres, int size, int numCities) {
		Integer randomCity;
		
		int indexCity1;
		int indexCity2;
		
		LinkedList<Integer> path1;
		LinkedList<Integer> path2;
		
		double mutationFactor = Math.random();
		
		for (int i = 0; i < size; i++) {
			// Get paths from chromosomes
			path1 = padres[i].getPath();
			path2 = padres[i+1].getPath();
			
			// Select an int from 0 to numCities-1, 
			randomCity = (int)(Math.random() * numCities);
			
			// Get indexes of the city in each path
			indexCity1 = path1.indexOf(randomCity);
			indexCity2 = path2.indexOf(randomCity);
			
			// Remove the city from the paths
			path1.remove(indexCity1);
			path2.remove(indexCity2);
			
			// Insert the city back into the paths, using the index from the
			// partner chromosome
			path1.add(indexCity2,randomCity);
			path2.add(indexCity1,randomCity);
			
			// Potentially mutate based on mutationFactor
			if(mutationFactor <= .01)
				padres[i] = mutate(padres[i]);
			else if(mutationFactor >= .99) 
				padres[i+1] = mutate(padres[i+1]);
				
		}
	}

}
