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
	
	public Relation processQuery(Query query) {
		List<String> tableNames = query.getTableNames();
		Relation relation = tables.get(tableNames.get(0));
		if (tableNames.size() > 1) {
			for (int i = 1; i < tableNames.size(); i++) {
				relation = relation.cartesianProduct(tables.get(tableNames.get(i)));
			}
		}
		relation.applyConditions(query.getConditions());
		relation.filterClassified(query.getClassificationLevel());
		relation = relation.selectColumns(query.getColumnNames());
		return relation;
	}
	

}
