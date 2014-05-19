package tspDistanceFunctions;

public class Functions {
	
	private static double euclidianDistance(double[] coord1, double[] coord2)
	{
		double xd = coord1[0] - coord2[0];
		double yd = coord1[1] - coord2[1];
		
		double distance = Math.round((Math.sqrt((xd * xd) + (yd * yd))));
		
		return distance;
	}
	
	private static double pseudoEuclidianDistance(double[] coord1, double[] coord2)
	{
		double xd = coord1[0] - coord2[0];
		double yd = coord1[1] - coord2[1];
		
		double rij = Math.sqrt(((xd * xd) + (yd * yd)) / 10.0);
		double tij = Math.round(rij);
		
		if (tij < rij)
		{
			return (tij + 1);	
		}
		else
		{
			return tij;
		}
	}
	
	public static double chooseFunction(String functionName, double[] coord1, double[] coord2)
	{
		double distance = 0.0;
		
		if (functionName.equals("ATT"))
		{
			distance = pseudoEuclidianDistance(coord1, coord2);
		}
		else if (functionName.equals("EUC_2D"))
		{
			distance = euclidianDistance(coord1, coord2);
		}
		
		return distance;
	}
}
