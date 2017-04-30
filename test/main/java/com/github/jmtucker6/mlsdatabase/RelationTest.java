package main.java.com.github.jmtucker6.mlsdatabase;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class RelationTest {

	@Test
	public void testCartesianProduct() {
		List<String> leftColumns = new ArrayList<String>();
		List<String> rightColumns = new ArrayList<String>();
		leftColumns.add("a");
		leftColumns.add("b");
		rightColumns.add("c");
		rightColumns.add("d");
		Map<String, Integer> tuple1 = new LinkedHashMap<String, Integer>();
		Map<String, Integer> tuple2 = new LinkedHashMap<String, Integer>();
		Map<String, Integer> tuple3 = new LinkedHashMap<String, Integer>();
		Map<String, Integer> tuple4 = new LinkedHashMap<String, Integer>();
		
		tuple1.put("a", 1);
		tuple1.put("b", 2);
		tuple2.put("a", 2);
		tuple2.put("b", 3);
		tuple3.put("c", 1);
		tuple3.put("d", 2);
		tuple4.put("c", 3);
		tuple4.put("d", 4);
		
		Set<Map<String, Integer>> leftTuples = new LinkedHashSet<Map<String, Integer>>();
		Set<Map<String, Integer>> rightTuples = new LinkedHashSet<Map<String, Integer>>();
		
		leftTuples.add(tuple1);
		leftTuples.add(tuple2);
		rightTuples.add(tuple3);
		rightTuples.add(tuple4);
		
		Relation leftRelation = new Relation("left", leftColumns, leftTuples);
		Relation rightRelation = new Relation("right", rightColumns, rightTuples);
		
		Relation result = leftRelation.join(rightRelation, null);
		System.out.println(result.getTuples().toString());
		assertEquals(result.getTuples().toString(), "[{a=1, b=2, c=1, d=2}, {a=1, b=2, c=3, d=4}, {a=2, b=3, c=1, d=2}, {a=2, b=3, c=3, d=4}]");
	}

}
