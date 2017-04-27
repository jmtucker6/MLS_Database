package main.java.com.github.jmtucker6.mlsdatabase;
import java.util.*;

public class Query {
	private List<String> tableNames, columnNames, conditions;
	private int classificationLevel;

	public Query() {
		super();
		tableNames = new ArrayList<String>();
		columnNames = new ArrayList<String>();
		conditions = new ArrayList<String>();
		classificationLevel = 4;
	}
	
	public Query(int classificationLevel) {
		super();
		this.classificationLevel = classificationLevel;
		tableNames = new ArrayList<String>();
		columnNames = new ArrayList<String>();
		conditions = new ArrayList<String>();
	}
	
	public Query(List<String> tableNames, List<String> columnNames, List<String> conditions, int classificationLevel) {
		super();
		this.tableNames = tableNames;
		this.columnNames = columnNames;
		this.conditions = conditions;
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

	public List<String> getConditions() {
		return conditions;
	}

	public void setConditions(List<String> conditions) {
		this.conditions = conditions;
	}

	public int getClassificationLevel() {
		return classificationLevel;
	}

	public void setClassificationLevel(int classificationLevel) {
		this.classificationLevel = classificationLevel;
	}
	
	public void parseUserQuery(String userQuery) {
		userQuery = userQuery.toUpperCase();
		String[] selectStage;
		selectStage = userQuery.split("SELECT\\s|\\sFROM\\s");
		columnNames = new ArrayList<String>(Arrays.asList(selectStage[1].split(",\\s*")));
		String[] fromStage = selectStage[2].split("\\sWHERE\\s");
		fromStage[0] = fromStage[0].replaceAll(";", "");
		tableNames = new ArrayList<String>(Arrays.asList(fromStage[0].split(",\\s")));
		if (fromStage.length > 1) {
			fromStage[1] = fromStage[1].replaceAll(";", "");
			conditions = new ArrayList<String>(Arrays.asList(fromStage[1].split("\\sAND\\s")));
		}
	}
}
