package main.java.com.github.jmtucker6.mlsdatabase;

import java.util.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MainTest {
	Database database;

	@Before
	public void setUp() throws Exception {
		Map<String, Relation> tables = new LinkedHashMap<String, Relation>();
		tables.put("T1", Main.readTableFromFile("T1.txt", "T1"));
		tables.put("T2", Main.readTableFromFile("T2.txt", "T2"));
		tables.put("T3", Main.readTableFromFile("T3.txt", "T3"));
		database = new Database(tables);
	}

	@Test
	public void test() {
		Query query = new Query(4);
		query.parseUserQuery("SELECT C2 FROM T3 WHERE C3=50;");
		assertEquals(database.processQuery(query).getTuples().size(), 4);

		query = new Query(3);
		query.parseUserQuery("SELECT C2 FROM T3 WHERE C3=50;");
		assertEquals(database.processQuery(query).getTuples().size(), 3);

		query = new Query(3);
		query.parseUserQuery("SELECT C2 FROM T3 WHERE C3=50 AND TC=2;");
		assertEquals(database.processQuery(query).getTuples().size(), 2);
		
		query = new Query(3);
		query.parseUserQuery("SELECT * FROM T2 WHERE B2=54;");
		assertEquals(database.processQuery(query).getTuples().size(), 3);
	}

}
