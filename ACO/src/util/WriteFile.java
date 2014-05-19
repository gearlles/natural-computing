package util;

import graph.Map;

import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
	
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

}
