package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import graph.Map;
import graph.Node;

public class Parser {
	char i=0;
	int j=0;
	public ArrayList<Node> nodes = new ArrayList<Node>();
	
	public Map parser(String fileName) throws IOException
	{
		Map map = null;
		File file = new File("C:\\Users\\Gearlles\\Desktop\\ACOo\\Problems\\" + fileName);
		BufferedReader buffer = new BufferedReader(new FileReader(file));
		int dimension = 0;
		String problemName = "";
		String functionName = "";
		String string = buffer.readLine();
		
		while (!(string.equals("EOF")))
		{
			if (string.startsWith("NAME"))
			{
				problemName = string.substring(string.indexOf(':') + 1).trim();
			}
			else if (string.startsWith("DIMENSION"))
			{
				dimension = Integer.parseInt(string.substring(string.indexOf(':') + 1).trim());
			}
			else if (string.startsWith("EDGE"))
			{
				functionName = string.substring(string.indexOf(':') + 1).trim();
				
				map = new Map(dimension, functionName, problemName);
			}
			else if (string.startsWith("NODE"))
			{
				string = buffer.readLine();
				while (!string.equals("EOF"))
				{
					int id = Integer.parseInt(string.substring(0, string.indexOf(' ')).trim());
					double[] coordinate = new double[2];
					
					coordinate[0] = Double.parseDouble(string.substring(string.indexOf(' '), string.lastIndexOf(' ')).trim());
					coordinate[1] = Double.parseDouble(string.substring(string.lastIndexOf(' ')).trim());
				    
					Node node = new Node(id, coordinate);
					nodes.add(node);
					
					string = buffer.readLine();
					
				}
			}
			
			if (string.equals("EOF"))
			{
				continue;
			}
			else
			{
				string = buffer.readLine();
			}
			
		}
		
		buffer.close();
		return map;
		
	}
	
}