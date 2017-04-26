package main.java.com.github.jmtucker6.mlsdatabase;
import java.util.*;
import java.io.*;

public class Main {

	public static void main(String[] args) {
		Relation t1 = readTableFromFile("T1.txt", "t1");
		System.out.println(t1.getTuples().toString());
	}
	
	private static Relation readTableFromFile(String fileName, String relationName) {
		FileReader fr;
		BufferedReader reader;
		String currLine;
		String[] tokens;
		List<String> columnNames = new ArrayList<String>();
		Map<String, Integer> tuple;
		List<Map<String, Integer>> relationTuples = new ArrayList<Map<String, Integer>>();
		try {
			fr = new FileReader(fileName);
			reader = new BufferedReader(fr);
			currLine = reader.readLine();
			tokens = currLine.split("\\s");
			for (String word : tokens) {
				columnNames.add(word);
			}
			
			while ((currLine = reader.readLine()) != null) {
				tuple = new HashMap<String, Integer>();
				tokens = currLine.split("\\s");
				for (int i = 0; i < tokens.length; i++) {
					tuple.put(columnNames.get(i), Integer.parseInt(tokens[i]));
				}
				relationTuples.add(tuple);
			}
			reader.close();
			fr.close();
			return new Relation(relationName, columnNames, relationTuples);
			
		} catch (IOException io) {
			io.printStackTrace();
			return null;
		}
		
	}

}
