package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.SchemaHeartbeatElement;
import mainEngine.SchemaStatsMainEngine;

class AtlasSchemaHeartbeatTest {
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

		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/Atlas", null);
	
	}
	
	@Test
	void testAtlasLoadHeartBeatElement() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/Atlas/results/SchemaHeartbeat.tsv", "\t", true, 28, header, inputTupleCollection);
		assertEquals(numRows,86);
		assertEquals(inputTupleCollection.size(),85);
		assertTrue(header.equals(headerExpected));
		
		
		assertEquals(inputTupleCollection.get(3).getAttrsInsWithTableIns(), 12);
		assertEquals(inputTupleCollection.get(28).getTotalAttrActivity(), 8);
		assertEquals(inputTupleCollection.get(78).getAttrUpdsSum(), 15);
		assertEquals(inputTupleCollection.get(12).getAttrsInsWithTableIns(), 14);
		assertEquals(inputTupleCollection.get(22).getMaintenance(), 43);
		
		//test all numNewTables
		int[] numNewTablesTruthTable = new int[]{56,56,55,57,57,57,57,57,57,57,57,57,58,58,58,58,58,59,59,59,59,59,51,51,51,51,51,51,52,52,53,53,53,55,55,56,56,56,55,55,57,57,58,58,58,58,58,58,58,58,58,59,59,61,59,59,59,59,59,59,59,59,59,59,59,59,60,62,62,62,69,69,69,70,69,70,70,71,71,71,72,72,72,73,73};
		int i = 0;
		for(SchemaHeartbeatElement tuple: inputTupleCollection) {
			assertEquals(tuple.getNumNewTables(), numNewTablesTruthTable[i]);
			i++;
		}
 	}

}
