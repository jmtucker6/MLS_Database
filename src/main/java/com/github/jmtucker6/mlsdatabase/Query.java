package main.java.com.github.jmtucker6.mlsdatabase;
import java.util.*;

public class Query {
	private List<String> tableNames, columnNames, conditions;
	
	public Query() {
		super();
		tableNames = new ArrayList<String>();
		columnNames = new ArrayList<String>();
		conditions = new ArrayList<String>();
	}
	
	public Query(List<String> tableNames, List<String> columnNames, List<String> conditions) {
		super();
		this.tableNames = tableNames;
		this.columnNames = columnNames;
		this.conditions = conditions;
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

	public List<String> getConditions() {
		return conditions;
	}

	public void setConditions(List<String> conditions) {
		this.conditions = conditions;
	}
}
