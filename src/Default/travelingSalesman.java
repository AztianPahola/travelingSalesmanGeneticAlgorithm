package Default;

import java.util.LinkedList;

public class travelingSalesman {

	public static void main(String args[]){
		
		int numCities = 5;
		int[][] adjecencyMatrix = {{0, 14, 4, 10, 20},
								   {14 , 0 , 7 , 8 ,7},
								   {4, 5, 0, 7 , 16},
								   {11, 7, 9, 0, 2},
								   {18, 7, 17, 4, 0}};
		LinkedList<Integer> resultingPath = generatePath(adjecencyMatrix, numCities);
		System.out.println("The genetic algorithm determined the following path to be the best:");
		for(int x : resultingPath){
			System.out.print(x + " ");
		}
	}

	// Generate an acceptable path to take between the cities through a genetic algorithm
	private static LinkedList<Integer> generatePath(int[][] input, int n) {

		final double MINIMUMIMPROVEMENT = 0.05; // Once the average improvement is lower than this value, the algorithm will end
		final double MINIMUMGENERATIONS = 10;	// Minimum number of generation that will be generated
		double improvement = 0; // Running average improvement between generations
		int generation = 0; // Current number of generations, used to calculate improvement
		double previousTotalStrength = 0; // Used to calculate improvement
		int size; // Size of the population
		chromosome[] parents; // Chromosomes selected to be crossed over to create the next generation
		chromosome[] nextGeneration; // Chromosomes of the next generation
		
		// Generate generation 0 randomly
		chromosome[] currentGeneration = generateInitialPaths(input, n);

		size = currentGeneration.length;

		do {
			double totalStrength = 0; // Total strength combined from all chromosomes in the generation
			// Add all chromosomes strengths
			for (int i = 0; i < size; i++) {
				totalStrength += currentGeneration[i].getStrength();
			}

			double[] cumulativeStrengths = new double[size]; /* Array of doubles which will be used as ranges to select
															    parents for the next generation */
			
			// Initialize index 0 to avoid out of array index error in the following for loop
			cumulativeStrengths[0] = currentGeneration[0].getStrength()/totalStrength;
			
			// Add the normalized strengths of each chromosome cumulatively to the cumulativeStrengths array 
			for (int i = 1; i < size; i++){
				double relativeStrength = currentGeneration[i].getStrength()/totalStrength;
				cumulativeStrengths[i] = cumulativeStrengths[i-1] + relativeStrength;
			}
			
			parents = new chromosome[size/2]; /* Initialize parents as an empty array, 
												 half the size of the current generation */
			// "Randomly" select parents0
			for (int i = 0; i < size/2; i++) {
				parents[i] = selectRandom(currentGeneration, cumulativeStrengths, 0, size/2);
				
			}
			
			// Assign the nextGeneration to be the crossOver product of the parent pairs
			nextGeneration = crossOver(parents, size, n);
			

			// Calculate change to improvement, avoiding a divide by zero error with following if
			if(generation > 0) {
				improvement += (totalStrength - previousTotalStrength) / previousTotalStrength / ++generation;
			}
			previousTotalStrength = totalStrength;
			currentGeneration = nextGeneration;

		} while (generation >= MINIMUMGENERATIONS && improvement >= MINIMUMIMPROVEMENT);
		
		int indexOfStrongest = 0; // Index of the chromosome with the best path
		// Determine which chromosome has the best path
		for (int i = 0; i < size; i++){
			if(currentGeneration[i].getStrength() > currentGeneration[indexOfStrongest].getStrength())
				indexOfStrongest = i;
		}
		return currentGeneration[indexOfStrongest].getPath();
		
	}
	
	// Select a parent pseudo-randomly through a binary search for the correct range for a randomly generated double 
	private static chromosome selectRandom(chromosome[] chromos, double[] ranges, int low, int high){
			
		// Generate a random double, select the correct chromosome based on that random double
		double random = Math.random();
		
		int mid = low+high/2;
		
		/* Below is a modified binary search algorithm which will use the value at mid, in addition to the values to it's left and right 
		 * to determine if the random double is greater than mid-1 and less than mid or 
		 * greater than or equal to mid and less than mid-1 */
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
	
	// Generate generation 0 randomly
	private static chromosome[] generateInitialPaths(int[][] input, int n) {
		chromosome[] paths = new chromosome[(int)Math.pow(2,n)];
		for (int i = 0; i < (int)Math.pow(2,n); i++) {
			paths[i] = new chromosome(input, n);
		}
		return paths;
	}
	
	// Interchange the indices of a given city in two parent chromosomes to produce two children
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
			
			// Select an int from 1 to numCities, 
			randomCity = (Integer)(int)((Math.random() * numCities)+1);

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
			
			// Update paths in the chromosomes
			padres[i].setPath(path1);
			padres[i+1].setPath(path2);
			
			// Potentially mutate based on mutationFactor
			if(mutationFactor <= .01)
				padres[i] = mutate(padres[i], numCities);
			else if(mutationFactor >= .99) 
				padres[i+1] = mutate(padres[i+1], numCities);		
		}
		return padres;
	}
	
	// Flip the order of a random 2 city path in a given chromosome
	private static chromosome mutate(chromosome chromo, int numCities){
		
		// Select an int from 0 to numCities-1, 
		int randomIndex = (int)(Math.random() * numCities);
		
		LinkedList<Integer> path = chromo.getPath();
		Integer cityAtIndex = path.remove(randomIndex);
		
		// Swap the order between the city at index randomCity and the city at randomCity+1, or index 0 if randomCity is at the end
		
		if(randomIndex < numCities-1) { // the random city is NOT the last in the list
			path.add(randomIndex+1,cityAtIndex);
		}else { // The city is the last in the list
			// Get the integer at the beginning
			Integer cityAtBeginning = path.removeFirst();
			
			// Put the last city first, and the first city last
			path.addFirst(cityAtIndex);
			path.addLast(cityAtBeginning);
		}
		
		// Update path in the chromosome
		chromo.setPath(path);
		return chromo;

	}

}
