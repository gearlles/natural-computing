package util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import parser.Parser;
import tspDistanceFunctions.Functions;
import graph.Arest;
import graph.Map;

public class Util {

	public static double[][] generateAdjacencyMatrix(Map map, Parser parse)
	{
		double[][] adjacencyMatrix = new double[map.countNodes][map.countNodes];
		
		for (int i = 0; i < adjacencyMatrix.length; i++) 
		{
			for (int j = i + 1; j < adjacencyMatrix.length; j++) 
			{
				adjacencyMatrix[i][j] = Functions.chooseFunction(map.functionName, parse.nodes.get(i).coordinate, parse.nodes.get(j).coordinate); 
			}
		}
		
		for (int i = 0; i < adjacencyMatrix.length; i++) 
		{
			for (int j = 0; j < adjacencyMatrix.length; j++) 
			{
				adjacencyMatrix[j][i] = adjacencyMatrix[i][j];
			}
		}
		
		return adjacencyMatrix;
	}
	
	public static void writeFile(double[][] adjacencyMatrix, Map map){
		FileWriter fWriter;
		try {
			fWriter = new FileWriter("C:\\Users\\Gearlles\\Desktop\\ACOo\\Problems\\" + map.problemName + ".den");
			for (int i = 0; i < adjacencyMatrix.length; i++) 
			{
				for (int j = 0; j < adjacencyMatrix.length; j++) 
				{
					fWriter.write(String.valueOf(adjacencyMatrix[i][j]));
					fWriter.write(' ');
					fWriter.flush();
				}
				fWriter.write('\n');
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Arest> createArests(double[][] adjacencyMatrix, Parser parse, double decreasingPher,
			double visibilityInfluence, double pherInfluence)
	{
		ArrayList<Arest> paths = new ArrayList<Arest>();
		Arest path = null;
		
		for (int i = 0; i < adjacencyMatrix.length; i++) 
		{
			for (int j = i + 1; j < adjacencyMatrix.length; j++) 
			{
				path = new Arest(parse.nodes.get(i), parse.nodes.get(j), decreasingPher, visibilityInfluence, 
									pherInfluence, adjacencyMatrix[i][j]);
				paths.add(path);
			}
		}
		
		return paths;
	}

}
