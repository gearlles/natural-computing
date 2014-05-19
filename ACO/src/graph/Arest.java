package graph;
import java.util.Random;

import aco.*;

public class Arest {

	public Node source, destination;
	public double weight;
	private double decreasingPher;
	public double pherValue;
	public double probability;
	public double visibilityInfluence, pherInfluence;
	public double length;

	Random rand = new Random();
	
	public Arest(Node source, Node destination, double decreasingPher, double visibilityInfluence, double pherInfluence, double length)
	{
		this.decreasingPher = decreasingPher;
		this.source = source;
		this.destination = destination;
		
		this.weight = 0.0;
		this.pherValue = 0.01 + 0.3 * Math.random();
		
		this.pherInfluence = pherInfluence;
		this.visibilityInfluence = visibilityInfluence;
		this.probability = 0.0;
		this.length = length;
	}
	
	public void evaporatePheromone()
	{
		this.pherValue -= (1 - this.decreasingPher) * this.pherValue;
	}
	
	public void incrementPheromone(double d)
	{
		this.pherValue += d;
	}

	
	
}
