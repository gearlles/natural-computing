package aco;

import graph.*;

import java.util.ArrayList;

public class Ant {
	public double pheromone;
	public ArrayList<Node> visitedNode;
	public ArrayList<Arest> visitedArests;
	public int reached;
	
	public double bored;
	public double decrementBored;
	public double boredLimit;
	
	public Node source, actualNode;
	
	public Ant(double pheromone)
	{
		this.pheromone = pheromone;
		this.actualNode = source;
		this.visitedNode = new ArrayList<Node>();
		this.visitedArests = new ArrayList<Arest>();
		this.reached = 0;
	}
	
}
