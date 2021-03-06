package main.java.com.github.jmtucker6.mlsdatabase;
import java.util.*;
import java.io.*;

public class Main {

	public static void main(String[] args) {
		Map<String, Relation> tables = new LinkedHashMap<String, Relation>();
		tables.put("T1", readTableFromFile("T1.txt", "T1"));
		tables.put("T2", readTableFromFile("T2.txt", "T2"));
		tables.put("T3", readTableFromFile("T3.txt", "T3"));
		Database database = new Database(tables);

		System.out.println("Enter classification level");
		Scanner scanner = new Scanner(System.in);
		int classificationLevel = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Enter a query:");
		String userQuery = readQuery(scanner);
		Query query = new Query(classificationLevel);
		query.parseUserQuery(userQuery);
		database.processQuery(query).printRelation();
		scanner.close();
	}
	
	/**
	 * Reads a file and parses data to create a relation
	 * @param fileName name of the file to be read
	 * @param relationName name of the relation to be created
	 * @return returns relation object containing data from the file
	 */
	public static Relation readTableFromFile(String fileName, String relationName) {
		FileReader fr;
		BufferedReader reader;
		String currLine;
		String[] tokens;
		List<String> columnNames = new ArrayList<String>();
		Map<String, Integer> tuple;
		Set<Map<String, Integer>> relationTuples = new LinkedHashSet<Map<String, Integer>>();
		try {
			fr = new FileReader(fileName);
			reader = new BufferedReader(fr);
			currLine = reader.readLine();
			tokens = currLine.split("\\s");
			for (String word : tokens) {
				columnNames.add(word);
			}
			
			while ((currLine = reader.readLine()) != null) {
				tuple = new LinkedHashMap<String, Integer>();
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
	
	/**
	 * Reads a multi-line (or single) query from the console
	 * @param scanner
	 * @return single string query
	 */
	private static String readQuery(Scanner scanner) {
		String query = "";
		do {
			query += scanner.nextLine() + " ";
		} while (!query.contains(";"));
		query = query.trim();
		return query;
	}

}
