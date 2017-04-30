package main.java.com.github.jmtucker6.mlsdatabase;
import java.util.*;

public class Query {
	private List<String> tableNames, columnNames, selectConditions, joinConditions;
	private int classificationLevel;

	public Query() {
		super();
		tableNames = new ArrayList<String>();
		columnNames = new ArrayList<String>();
		selectConditions = new ArrayList<String>();
		joinConditions = new ArrayList<String>();
		classificationLevel = 4;
	}
	
	public Query(int classificationLevel) {
		super();
		this.classificationLevel = classificationLevel;
		tableNames = new ArrayList<String>();
		columnNames = new ArrayList<String>();
		selectConditions = new ArrayList<String>();
		joinConditions = new ArrayList<String>();
	}
	
	public Query(List<String> tableNames, List<String> columnNames, List<String> selectConditions,List<String> joinConditions, int classificationLevel) {
		super();
		this.tableNames = tableNames;
		this.columnNames = columnNames;
		this.selectConditions = selectConditions;
		this.joinConditions = joinConditions;
		this.classificationLevel = classificationLevel;
	}

	public List<String> getTableNames() {
		return tableNames;
	}

	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}
	
	public void addTableName(String tableName) {
		tableNames.add(tableName);
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public List<String> getSelectConditions() {
		return selectConditions;
	}

	public void setSelectConditions(List<String> conditions) {
		this.selectConditions = conditions;
	}

	public List<String> getJoinConditions() {
		return joinConditions;
	}

	public void setJoinConditions(List<String> conditions) {
		this.joinConditions = conditions;
	}

	public int getClassificationLevel() {
		return classificationLevel;
	}

	public void setClassificationLevel(int classificationLevel) {
		this.classificationLevel = classificationLevel;
	}
	
	/**
	 * Converts a string query into a query objects using the components
	 * @param userQuery string query
	 */
	public void parseUserQuery(String userQuery) {
		userQuery = userQuery.toUpperCase();
		String[] selectStage;
		selectStage = userQuery.split("SELECT\\s*|\\s*FROM\\s*");
		columnNames = new ArrayList<String>(Arrays.asList(selectStage[1].split(",\\s*")));
		String[] fromStage = selectStage[2].split("\\s*WHERE\\s*");
		fromStage[0] = fromStage[0].replaceAll(";", "");
		tableNames = new ArrayList<String>(Arrays.asList(fromStage[0].split(",\\s")));
		if (fromStage.length > 1) {
			fromStage[1] = fromStage[1].replaceAll(";", "");
			List<String> conditions = new ArrayList<String>(Arrays.asList(fromStage[1].split("\\sAND\\s")));
			separateConditions(conditions);
		}
	}
	
	/**
	 * Separates generic conditions into join and select conditions
	 * @param conditions
	 */
	private void separateConditions(List<String> conditions) {
		String[] tokens;
		for (String condition : conditions) {
			tokens = condition.split("=");
			if (tokens[1].matches("[0-9]+")) {
				selectConditions.add(condition);
			} else {
				joinConditions.add(condition);
			}
		}
		
	}
}
