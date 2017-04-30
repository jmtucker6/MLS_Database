package main.java.com.github.jmtucker6.mlsdatabase;
import java.util.*;

public class Database {
	private Map<String, Relation> tables;

	public Database() {
		super();
		tables = new LinkedHashMap<String, Relation>();
	}
	public Database(Map<String, Relation> tables) {
		super();
		this.tables = tables;
	}

	public Map<String, Relation> getTables() {
		return tables;
	}

	public void addTables(Relation relation) {
		tables.put(relation.getName(), relation);
	}
	
	/**
	 * Processes query object
	 * @param query query object
	 * @return result Relation after query is applied
	 */
	public Relation processQuery(Query query) {
		List<String> tableNames = query.getTableNames();
		Relation relation = executeJoins(tableNames, query.getJoinConditions());
		try {
			relation.applyConditions(query.getSelectConditions(), query.getClassificationLevel());
		} catch (Exception e) {
			System.out.println("Error: Security Level Violation");
			System.exit(1);
		}
		relation.filterClassified(query.getClassificationLevel());
		relation = relation.selectColumns(query.getColumnNames());
		return relation;
	}
	
	/**
	 * handles the join stage of query execution
	 * @param tableNames name of tables asked for by query
	 * @param joinConditions join conditions dictated by query
	 * @return result relation of joins
	 */
	private Relation executeJoins(List<String> tableNames, List<String> joinConditions) {
		String[] tokens;
		Relation leftRelation = null, rightRelation = null, resultRelation;
		for (String condition : joinConditions) {
			tokens = condition.split("=");
			for (String tableName : tableNames) {
				if (tables.get(tableName).getColumnNames().contains(tokens[0])) {
					leftRelation = tables.get(tableName);
					break;
				}
			}
			for (String tableName : tableNames) {
				if (tables.get(tableName).getColumnNames().contains(tokens[1])) {
					rightRelation = tables.get(tableName);
					break;
				}
			}
			tableNames.remove(leftRelation.getName());
			tableNames.remove(rightRelation.getName());
			resultRelation = leftRelation.join(rightRelation, condition);
			tables.put(resultRelation.getName(), resultRelation);
			tableNames.add(resultRelation.getName());
		}
		while (tableNames.size() > 1) {
			leftRelation = tables.get(tableNames.get(0));
			rightRelation = tables.get(tableNames.get(1));
			tableNames.remove(leftRelation.getName());
			tableNames.remove(rightRelation.getName());
			resultRelation = leftRelation.join(rightRelation, null);
			tables.put(resultRelation.getName(), resultRelation);
			tableNames.add(resultRelation.getName());
		}
		return tables.get(tableNames.get(0));
	}
	
	

}
