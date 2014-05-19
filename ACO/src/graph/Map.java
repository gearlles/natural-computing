package graph;

import java.util.ArrayList;
import tspDistanceFunctions.Functions;

public class Map {
	public ArrayList<Arest> paths;
	public int countNodes = 0;
	public String functionName = "";
	public String problemName = "";
	
	public Map(int countNodes, String functionName, String problemName)
	{
		this.paths = new ArrayList<Arest>();
		this.countNodes = countNodes;
		this.functionName = functionName;
		this.problemName = problemName;
	}
	
	public void createArest(double decreasingPher, double visibilityInfluence, 
			double pherInfluence, Node source, Node destination, String functionName)
	{
		Arest path = new Arest(source, destination, decreasingPher, visibilityInfluence, 
								pherInfluence, 
								Functions.chooseFunction(functionName, source.coordinate, destination.coordinate));
		paths.add(path);
	}
	
}
