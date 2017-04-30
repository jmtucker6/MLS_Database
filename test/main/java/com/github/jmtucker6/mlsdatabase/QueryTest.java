package main.java.com.github.jmtucker6.mlsdatabase;
import java.util.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class QueryTest {

	@Test
	public void testParseUserQuery() {
		Query query = new Query();
		query.parseUserQuery("SELECT C2 FROM T3 WHERE C3=50 AND C1=2;"); 
		assertEquals(query.getColumnNames().get(0), "C2");
	}

}
