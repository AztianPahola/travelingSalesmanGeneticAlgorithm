package Default;

import java.util.Collections;
import java.util.LinkedList;

public class chromosome {
	private LinkedList<Integer> path;
	private int distance = 0;
	private double strength;
	
	public chromosome(int[][] input, int n){
		path = new LinkedList<Integer>();
        	for (int i = 1; i <= n; i++) {
            		path.add(i);
        	}
        	Collections.shuffle(path);
		// Calculate total distance
        	for (int i = 0; i < n-1; i++) {
				distance += input[path.get(i)-1][path.get(i+1)-1];
			}
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
