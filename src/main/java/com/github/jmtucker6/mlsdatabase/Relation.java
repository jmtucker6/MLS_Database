package main.java.com.github.jmtucker6.mlsdatabase;
import java.util.*;

public class Relation {
	static final String[] PRIMARY_KEYS = {"A1", "B1", "C1"};
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
		int maxTC;
		for (Map<String, Integer> leftEntry : leftTuples) {
			for (Map<String, Integer> rightEntry : rightTuples) {
				if (leftEntry.get("KC") != rightEntry.get("KC"))
					continue;
				maxTC = Math.max(leftEntry.get("TC"), rightEntry.get("TC"));
				Map<String, Integer> combinedEntry = new LinkedHashMap<String, Integer>();
				combinedEntry.putAll(leftEntry);
				combinedEntry.putAll(rightEntry);
				combinedEntry.remove("TC");
				combinedEntry.put("TC", maxTC);
				productTuples.add(combinedEntry);
			}
		}
		return new Relation("product", productColumnNames, productTuples);
	}
	
	public Relation selectColumns(List<String> columnNames) {
		for (String primaryKey : PRIMARY_KEYS)
			if (columnNames.contains(primaryKey)) {
				columnNames.add("KC");
				break;
			}
		if (!columnNames.contains("TC"))
			columnNames.add("TC");
		Map<String, Integer> filteredRow;
		List<Map<String, Integer>> filteredTable = new ArrayList<Map<String, Integer>>();
		for (Map<String, Integer> tuple : tuples) {
			filteredRow = new LinkedHashMap<String, Integer>();
			for (Map.Entry<String, Integer> entry : tuple.entrySet()) {
				if (columnNames.contains(entry.getKey())) {
					filteredRow.put(entry.getKey(), entry.getValue());
				}
			}
			filteredTable.add(filteredRow);
		}
		return new Relation("filteredRelation", columnNames, filteredTable);
	}

	public void applyConditions(List<String> conditions) {
		if (conditions.isEmpty())
			return;
		List<Map<String, Integer>> filteredTuples = new ArrayList<Map<String, Integer>>();
		String[] tokens;
		String leftSide, rightSide;
		for (String condition : conditions) {
			filteredTuples.clear();
			tokens = condition.split("=");
			leftSide = tokens[0];
			rightSide = tokens[1];
			if (rightSide.matches("[A-Z].")) {
				for (Map<String, Integer> tuple : tuples) {
					if (tuple.get(leftSide).equals(tuple.get(rightSide)))
						filteredTuples.add(tuple);
				}
			} else {
				for (Map<String, Integer> tuple : tuples) {
					if(tuple.get(leftSide) == Integer.parseInt(rightSide))
						filteredTuples.add(tuple);
				}
			}
			this.tuples = new ArrayList<Map<String, Integer>>(filteredTuples);
		}
	}
	
	public void filterClassified(int classificationLevel) {
		List<Map<String, Integer>> filteredTuples = new ArrayList<Map<String, Integer>>();
		for (Map<String, Integer> tuple : tuples) {
			if (tuple.get("TC") <= classificationLevel)
				filteredTuples.add(tuple);
		}
		this.tuples = new ArrayList<Map<String, Integer>>(filteredTuples);
	}
}
