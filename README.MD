# Problem Statement
Create a genetic algorithm for the traveling salesman problem, assuming there are five cities, and the given matrix contains their distances to each other.

|    | V1 | V2 | V3 | V4 | V5 |
|----|----|----|----|----|----|
| V1 | 0  | 14 | 4  | 10 | 20 |
| V2 | 14 | 0  | 7  | 8  | 7  |
| V3 | 4  | 5  | 0  | 7  | 16 |
| V4 | 11 | 7  | 9  | 0  | 2  |
| V5 | 18 | 7  | 17 | 4  | 0  |

# Thought Process

One challenge with genetic algorithms is knowing when to stop the generation of new chromosomes. Although we have the fice cities, and calculating the best route by hand
would be possible, I'd like to create a solution which is extendable to more than such a simple set.
So, the approach I will take is to stop the algorithm when the improvements between generations becomes negligible.


# Chromosome Initialization

To keep this algorithm expandable, I will determine the number of initial chromosomes based on the number of cities (n) according to "initial Chromosome Amount = 2^n"
So, the amount of initial chromosomes for this example of five cities will be 2^5 = 32

These chromosomes will be randomly generated, with no inentional biases.
Each one will be generated with a LinkedList containing ints 1 through 5.

# Crossover Mechanism

It must be considered that, when crossing over two chromosomes, it's necessary to not end up repeating the same city; to avoid this, 
I'll simply switch the position of a given city between the two.
e.g. (Assuming city 3 is randomly selected):

Chromosome 1|Chromosome 2
------------|------------
{1,2,3,4,5} |{3,2,5,1,4}

will combine to form:

Chromosome 3|Chromosome 4
------------|------------
{3,1,2,4,5} |{2,5,3,1,4}


# Mutation Mechanism

Mutation is something that is necessary, but not something you want to do often.
To determine when the chromosomes will mutate I will randomly generate a value between [0 and 1) after the crossover,
if this value is <= .01, the first parent will mutate. If it is >= .99 the second parent will mutate.
This will result in a mutation rate of ~ 2%

# Psuedo Code

public class travelingSalesman{
	public static void main(String args[]){
		
		int[][] adjecencyMatrix = *Above matrix, in Problem Statement*
		List resultingPath = GeneratePath(adjecencyMatrix, int n) //n = number of cities

		for(int x: resultingPath){
			System.out.println(x);
		}
	} 

	private static List generatePath(int[][] input, int n){
		final double MINIMUMIMPROVEMENT = 0.05;	// Once the average improvement is lower than this value, the algorithm will end
		final double MINIMUMGENERATIONS = 10;	// Minumum number of generation that will be generated
		double improvement = 0;	// Running average improvement between generations
		int generation = 0;	// Current number of generations, used to calculate improvement
		double previousTotalStrength;

		chromosome[2^n] currentGeneration = generateInitialPaths(input, n);

		do{
		double totalStrength = *Add all chromosomes strengths*
		double[] cumulativeStrengths;
		
		*Add the normalized strengths of each chromosome cumulatively to cumulativeStrengths array*

		*Randomly generate a float between 0 and 1 to select a parent, repeat until 2^n/2 parents are selected*
		
		chromosome[] parents = *"Randomly" selected chromosomes, based on strength*
		
		chromosome[] nextGeneration = crossOver(parents, n);
		
		// Calculate change to improvement
		improvement += (totalStrength - previousTotalStrength)/ previousTotalStrength / ++generation;
		previousTotalStrength = totalStrength;		

		}while(generation >= MINIMUMGENERATIONS && improvement >= MINIMUMIMPROVEMENT);
	}

	private chromosome selectRandom(chromosome[] chromos, double[] ranges, int low, int high){
		
		// Binary search for the correct chromosome, return that chromosome


	}

	private static chromosome[] generateInitialPaths(int[][] input, int n){
	
		chromosome[] paths = new chromosome[2^n]; 
		for(int i = 0; i < 2^n; i++){
			paths[i] = new chromosome(input, n);	
		}
		return paths;
	}

	private static chromosome[] crossOver(chromosome[] padres, int n){
		for (int i = 0; i < padres.length; i += 2){
			int city = *Randomly select from 1 through n*
			*Swap the positions of the given city based on the chromosome pairs*
			float mutationFactor = *Random float between 0 and 1*
			
			*Mutate if appropriate*
		}
	}

	private static chromosome mutate(chromosome chromo, int numCities){
	
		int randomCity = random value between 0 and numCities-1
		
		// Swap the order between the city at index randomCity and the city at randomCity+1, or index 0 if randomCity is at the end

	}
}

public class chromosome{

	private LinkedList path;
	private int distance;
	private double strength;
	
	public chromosome(int[][] input, int n){
		LinkedList<Integer> randomPath = new LinkedList<Integer>();
        	for (int i = 1; i <= n; i++) {
            		randomPath.add(i);
        	}
        	path = Collections.shuffle(randomPath);
		
		*Calculate total distance*
		strength = 1 / distance;  // Strength will be higher the lower the distance
	}

	public LinkedList<Integer> getPath(){
		return path;
	}

	public int getDistance(){
		return distance;
	}

	public double getStrength(){
		return strength;
	}

	public void setPath(LinkedList<Integer> newPath){
	
		path = newPath;

	}
}
