package aco;
import graph.*;

import java.util.ArrayList;
import java.util.Random;

public class Colony {
	
	private Ant[] ants;
	public double lowerPathLength;
	private ArrayList<Node> lowerPath;
	
	
	public Colony(int numberOfAnts, double pheromone)
	{
		this.ants = new Ant[numberOfAnts];
		this.lowerPathLength = Double.MAX_VALUE;
		
		for (int i = 0; i < numberOfAnts; i++)
		{
			this.ants[i] = new Ant(pheromone);
		}
	}
	
	private Arest chooseArest(Ant ant, Map map)
	{
		double sum = 0.0;
		ArrayList<Arest> possible = possibleArests(ant, map);
		
		double[] heurs = new double[possible.size()];
		for (int i = 0; i < heurs.length; i++) {
			heurs[i] = 1 / possible.get(i).length;
		}
		
		for (int i = 0; i < possible.size(); i++) {
			Arest a = possible.get(i);
			if (possible.contains(a)) {
				if (!ant.visitedArests.contains(a)) {
					sum += Math.pow(a.pherValue, a.pherInfluence)
							* Math.pow(heurs[i], a.visibilityInfluence);
				}
			}
		}
		
		for (int i = 0; i < possible.size(); i++) {
			Arest choosed = possible.get(i);
			choosed.probability = (Math.pow(choosed.pherValue, choosed.pherInfluence) * Math
					.pow(heurs[i], choosed.visibilityInfluence)) / sum;
		}
		
//		for (Arest choosed : possible)
//		{
//			sum += choosed.pherInfluence * choosed.pherValue * (1.00 / choosed.length) * choosed.visibilityInfluence;
//		}
//		
//		for (Arest choosed : possible)
//		{			
//			choosed.probability = Math.random() * ((choosed.pherInfluence * choosed.pherValue * (1.00 / choosed.length) * choosed.visibilityInfluence)/sum);
//		}	
		
		double maxProbability = 0;
		int indexMaxProbability = 0;
		
		for (int i = 0; i < possible.size(); i++)
		{
			if (maxProbability == 0)
			{
				maxProbability = possible.get(i).probability;
				indexMaxProbability = i;
			}
			else
			{
				if (possible.get(i).probability > maxProbability)
				{
					maxProbability = possible.get(i).probability;
					indexMaxProbability = i;
				}
			}
			
		}
		
		if (possible.size() == 0)
		{
			return null;
		}
		else
		{
			return possible.get(indexMaxProbability);
		}
	}
	
	private ArrayList<Arest> possibleArests(Ant ant, Map map)
	{
		ArrayList<Arest> possible = new ArrayList<Arest>();
		
		for(int i = 0; i < map.paths.size(); i++)
		{
			if(map.paths.get(i).source.id == ant.actualNode.id || map.paths.get(i).destination.id == ant.actualNode.id)
			{
				if ((!ant.visitedNode.contains(map.paths.get(i).source) || 
						!ant.visitedNode.contains(map.paths.get(i).destination)))
				{
					possible.add(map.paths.get(i));
				}
				else if (!ant.visitedArests.contains(map.paths.get(i)) &&
						(map.paths.get(i).source == ant.source ||
						 map.paths.get(i).destination == ant.source) &&
						 allNodesVisited(ant, map) &&
						 ant.actualNode != ant.source)
				{
					possible.add(map.paths.get(i));
				}
			}
		}
		
		return possible;
	}
	
	private void initializeAnts(Map map, Ant[] ant)
	{
		Random rand = new Random();
		for (int i = 0; i < ant.length; i++)
		{
			ant[i].source = map.paths.get(rand.nextInt(map.paths.size())).source;
			ant[i].actualNode = ant[i].source;
			ant[i].visitedArests = new ArrayList<Arest>();
			ant[i].visitedNode = new ArrayList<Node>();
		}
	}
	
	private boolean allNodesVisited(Ant ant, Map map)
	{
		if (ant.visitedNode.size() < map.countNodes)
		{
			return false;
		}
		
		return true;
	}
	
	public ArrayList<Node> runACO(Map map, int numberOfIterations)
	{
		Arest arest = null;
		ArrayList<Node> lowerPath;
		
		for (int i = 0; i < numberOfIterations; i++)
		{
			initializeAnts(map, ants);
			
			for (Ant ant : this.ants)
			{
				if (i == 0)
				{
					ant.visitedNode.add(ant.actualNode);
				}

				// Visit all nodes
				while (ant.visitedArests.size() < map.countNodes) {
					arest = chooseArest(ant, map);
					
					if (arest != null) {
						ant.visitedArests.add(arest);

						if (ant.actualNode == arest.source) {
							ant.actualNode = arest.destination;
						} else if (ant.actualNode == arest.destination) {
							ant.actualNode = arest.source;
						}

						ant.visitedNode.add(ant.actualNode);
					}
				}
			}
			
			// Decreasing pheromone
			for (Arest mapArest : map.paths) {
				mapArest.evaporatePheromone();
			}
			
			// Increasing pheromone
			for (Ant ant : this.ants) {
				for (Arest visitedArest : ant.visitedArests) {
					visitedArest.incrementPheromone(1 / visitedArest.length);
				}
			}
			
			lowerPath(ants);
			// menor distância
			System.out.println((int)lowerPathLength);
		}
		
		return this.lowerPath;
	}
	
	public ArrayList<Node> lowerPath (Ant[] ants){
		double sumVisitedArests = 0.0;
		double sumLengthArests = 0.0;
		ArrayList<Node> lowerPath = null;
		
		for (Ant ant : this.ants)
		{
			if (lowerPath == null)
			{
				lowerPath = ant.visitedNode;
				for (Arest visited : ant.visitedArests)
				{
					sumVisitedArests += visited.length;
				}
			}
			else
			{
				for (Arest visited : ant.visitedArests)
				{
					sumLengthArests += visited.length;
				}
				
				if (sumLengthArests < sumVisitedArests)
				{
					lowerPath = ant.visitedNode;
					sumVisitedArests = sumLengthArests;
				}
			}

		}
		
		if (sumVisitedArests < lowerPathLength) {
			this.lowerPathLength = sumVisitedArests;
			this.lowerPath = lowerPath;
		}
		
		return lowerPath;
	}
}
