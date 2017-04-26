package main.java.com.github.jmtucker6.mlsdatabase;
import java.util.*;

public class Relation {
	String name;
	List<String> columnNames;
	List<Map<String, Integer>> tuples;

	public Relation(String name, List<String> columnNames, List<Map<String, Integer>> tuples) {
		super();
		this.name = name;
		this.columnNames = columnNames;
		this.tuples = tuples;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public List<Map<String, Integer>> getTuples() {
		return tuples;
	}

	public void setTuples(List<Map<String, Integer>> tuples) {
		this.tuples = tuples;
	}
	
	public Relation cartesianProduct(Relation rightRelation) {
		List<Map<String, Integer>> leftTuples = this.tuples;
		List<Map<String, Integer>> rightTuples = rightRelation.getTuples();
		List<String> productColumnNames = this.columnNames;
		productColumnNames.addAll(rightRelation.getColumnNames());
		List<Map<String, Integer>> productTuples = new ArrayList<Map<String, Integer>>();
		for (Map<String, Integer> leftEntry : leftTuples) {
			for (Map<String, Integer> rightEntry : rightTuples) {
				Map<String, Integer> combinedEntry = new HashMap<String, Integer>();
				combinedEntry.putAll(leftEntry);
				combinedEntry.putAll(rightEntry);
				productTuples.add(combinedEntry);
			}
		}
		return new Relation("product", productColumnNames, productTuples);
	}
}
