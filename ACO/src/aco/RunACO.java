package aco;

import graph.*;

import java.io.IOException;
import java.util.ArrayList;

import parser.Parser;
import util.Util;

public class RunACO {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Parser parse = new Parser();
		Map map = parse.parser("att48.tsp");
		
		double decreasingPher = 0.5; // rho
		double pherInfluence = 1;  // alpha
		double visibilityInfluence = 2.2; // beta
		
		double pheromone = 1;
		int numberOfAnts = map.countNodes;
		
		int numberOfIterations = 1000;
		
		double[][] adjacencyMatrix = new double[map.countNodes][map.countNodes];
		
		adjacencyMatrix = Util.generateAdjacencyMatrix(map, parse);
		map.paths = Util.createArests(adjacencyMatrix, parse, decreasingPher, visibilityInfluence, pherInfluence);
		
		Colony ants = new Colony(numberOfAnts, pheromone);
		ArrayList<Node> lowerPath = ants.runACO(map, numberOfIterations);
		for(Node node : lowerPath){
			System.out.print(node.id + ", ");
		}
		
		System.out.println("\n Tamanho do menor caminho: " + ants.lowerPathLength);
	}

}
