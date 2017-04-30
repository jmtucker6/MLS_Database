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
		Relation relation = tables.get(tableNames.get(0));
		String joinCondition;
		if (tableNames.size() > 1) {
			for (int i = 1; i < tableNames.size(); i++) {
				if(query.getJoinConditions().size() >= i) {
					joinCondition = query.getJoinConditions().get(i-1);
				} else {
					joinCondition = null;
				}
				relation = relation.join(tables.get(tableNames.get(i)), joinCondition);
			}
		}
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
	

}
