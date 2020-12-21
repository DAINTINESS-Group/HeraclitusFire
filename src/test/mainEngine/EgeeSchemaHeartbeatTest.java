package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.SchemaHeartbeatElement;
import mainEngine.SchemaStatsMainEngine;

class EgeeSchemaHeartBeatTest {
	private static SchemaStatsMainEngine schemaStatsMainEngine; 
	private static ArrayList<String> headerExpected; 
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		headerExpected = new ArrayList<String>(Arrays.asList(
				"trID", "epochTime", "oldVer", "newVer", "humanTime", "distFromV0InDays", "runningYearFromV0", 
				"runningMonthFromV0", "#numOldTables", "#numNewTables", "#numOldAttrs", "#numNewAttrs", 
				"tablesIns", "tablesDel", "attrsInsWithTableIns", "attrsbDelWithTableDel", "attrsInjected", 
				"attrsEjected", "attrsWithTypeUpd", "attrsInPKUpd", "tableDelta", "attrDelta", "attrBirthsSum", 
				"attrDeathsSum", "attrUpdsSum", "Expansion", "Maintenance", "TotalAttrActivity")); 

		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/Egee", null);
	
	}
	@Test
	void testEgeeLoadHeartBeatElement() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/Egee/results/SchemaHeartbeat.tsv", "\t", true, 28, header, inputTupleCollection);
		assertEquals(numRows,18);
		assertEquals(inputTupleCollection.size(),17);
		assertTrue(header.equals(headerExpected));
		
		
		
		assertEquals(inputTupleCollection.get(7).getAttrsInsWithTableIns(), 21);
		assertEquals(inputTupleCollection.get(4).getExpansion(), 3);
		assertEquals(inputTupleCollection.get(15).getMaintenance(), 2);
		assertEquals(inputTupleCollection.get(2).getTotalAttrActivity(), 21);
		assertEquals(inputTupleCollection.get(16).getExpansion(), 8);
		
		//test all numNewTables
		int[] numNewTablesTruthTable = new int[] {6,6,4,4,4,4,4,8,8,8,8,8,8,8,9,9,10};
		int i = 0;
		for(SchemaHeartbeatElement tuple: inputTupleCollection) {
			assertEquals(tuple.getNumNewTables(), numNewTablesTruthTable[i]);
			i++;
		}
	}

}
