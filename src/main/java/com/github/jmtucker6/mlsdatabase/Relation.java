package main.java.com.github.jmtucker6.mlsdatabase;
import java.util.*;

public class Relation {
	static final String[] PRIMARY_KEYS = {"A1", "B1", "C1"};
	String name;
	List<String> columnNames;
	Set<Map<String, Integer>> tuples;

	public Relation(String name, List<String> columnNames, Set<Map<String, Integer>> tuples) {
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

	public Set<Map<String, Integer>> getTuples() {
		return tuples;
	}

	public void setTuples(Set<Map<String, Integer>> tuples) {
		this.tuples = tuples;
	}
	
	public Relation join(Relation rightRelation, String joinCondition) {
		List<String> productColumnNames = this.columnNames;
		productColumnNames.addAll(rightRelation.getColumnNames());
		productColumnNames.removeIf(p -> p.equals("TC"));
		productColumnNames.add("TC");
		Set<Map<String, Integer>> resultTuples;
		if (joinCondition == null) {
			resultTuples = cartesianProduct(rightRelation);
		} else {
			resultTuples = hashJoin(rightRelation, joinCondition);
		}
		return new Relation("result", productColumnNames, resultTuples);
	}
	
	public Set<Map<String, Integer>> cartesianProduct(Relation rightRelation) {
		Set<Map<String, Integer>> leftTuples = this.tuples;
		Set<Map<String, Integer>> rightTuples = rightRelation.getTuples();
		Set<Map<String, Integer>> productTuples = new LinkedHashSet<Map<String, Integer>>();
		int maxTC;
		for (Map<String, Integer> leftEntry : leftTuples) {
			for (Map<String, Integer> rightEntry : rightTuples) {
				if (leftEntry.get("KC") != rightEntry.get("KC"))
					continue;
				maxTC = Math.max(leftEntry.get("TC"), rightEntry.get("TC"));
				joinLeftRight(productTuples, leftEntry, rightEntry, maxTC);
			}
		}
		return productTuples;
	}
	
	public Set<Map<String, Integer>> hashJoin(Relation rightRelation, String condition) {
		String leftSide, rightSide;
		Map<Integer, Set<Map<String, Integer>>> map = new HashMap<Integer, Set<Map<String, Integer>>>();
		Set<Map<String, Integer>> productTable = new LinkedHashSet<Map<String, Integer>>();
		String tokens[] = condition.split("=");
		leftSide = tokens[0];
		rightSide = tokens[1];
		createHashJoinMap(rightRelation, rightSide, map);
		for (Map<String, Integer> leftTuple : tuples) {
			if (map.containsKey(leftTuple.get(leftSide))) {
				for (Map<String, Integer> rightTuple : map.get(leftTuple.get(leftSide))) {
					if (!leftTuple.get("KC").equals(rightTuple.get("KC"))) {
						continue;
					}
					int maxTC = Math.max(leftTuple.get("TC"), rightTuple.get("TC"));
					joinLeftRight(productTable, leftTuple, rightTuple, maxTC);
				}
			}
		}
		return productTable;

	}

	private void joinLeftRight(Set<Map<String, Integer>> productTable, Map<String, Integer> leftTuple,
			Map<String, Integer> rightTuple, int maxTC) {
		Map<String, Integer> productTuple;
		productTuple = new LinkedHashMap<String, Integer>();
		productTuple.putAll(leftTuple);
		productTuple.putAll(rightTuple);
		productTuple.remove("TC");
		productTuple.put("TC", maxTC);
		productTable.add(productTuple);
	}

	private void createHashJoinMap(Relation rightRelation, String rightSide,
			Map<Integer, Set<Map<String, Integer>>> map) {
		Set<Map<String, Integer>> tupleSet;
		for (Map<String, Integer> rightTuple : rightRelation.getTuples()) {
			if (map.containsKey(rightTuple.get(rightSide))) {
				 tupleSet = map.get(rightTuple.get(rightSide));
			} else {
				 tupleSet = new LinkedHashSet<Map<String, Integer>>();
			}
			tupleSet.add(rightTuple);
			map.put(rightTuple.get(rightSide), tupleSet);
		}
	}
	
	public Relation selectColumns(List<String> columnNames) {
		if (columnNames.get(0).equals("*"))
			return this;
		for (String primaryKey : PRIMARY_KEYS)
			if (columnNames.contains(primaryKey)) {
				columnNames.add("KC");
				break;
			}
		if (!columnNames.contains("TC"))
			columnNames.add("TC");
		Map<String, Integer> filteredRow;
		Set<Map<String, Integer>> filteredTable = new LinkedHashSet<Map<String, Integer>>();
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

	public void applyConditions(List<String> conditions, int classificationLevel) throws ClassificationLevelException {
		if (conditions.isEmpty())
			return;
		Set<Map<String, Integer>> filteredTuples = new LinkedHashSet<Map<String, Integer>>();
		String[] tokens;
		String leftSide, rightSide;
		for (String condition : conditions) {
			filteredTuples.clear();
			tokens = condition.split("=");
			leftSide = tokens[0];
			rightSide = tokens[1];
			if (leftSide.equals("TC") && classificationLevel < Integer.parseInt(rightSide)) {
				throw new ClassificationLevelException();
			}
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
			this.tuples = new LinkedHashSet<Map<String, Integer>>(filteredTuples);
		}
	}
	
	public void filterClassified(int classificationLevel) {
		Set<Map<String, Integer>> filteredTuples = new LinkedHashSet<Map<String, Integer>>();
		for (Map<String, Integer> tuple : tuples) {
			if (tuple.get("TC") <= classificationLevel)
				filteredTuples.add(tuple);
		}
		this.tuples = new LinkedHashSet<Map<String, Integer>>(filteredTuples);
	}
	
	public void printRelation() {
		List<String> outputColumns = new ArrayList<String>();
		String previousColumn = "";
		for (String columnName : columnNames) {
			if (columnName.equals("KC"))
				continue;
			if (previousColumn.matches(".1")) {
				outputColumns.add("KC");
			}
			outputColumns.add(columnName);
			previousColumn = columnName;
		}
		for (String columnName : outputColumns) {
			System.out.print(columnName + "\t" );
		}
		System.out.println();
		for (int i = 0; i < outputColumns.size(); i++) {
			System.out.print("---" + "\t");
		}
		System.out.println();
		for (Map<String, Integer> tuple : tuples) {
			for (String columnName : outputColumns) {
				System.out.print(tuple.get(columnName).toString() + "\t");
			}
			System.out.println();
		}
	}
}
