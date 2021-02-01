package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.SchemaHeartbeatElement;
import mainEngine.SchemaStatsMainEngine;

class Aa_alert__frbcatdbSchemaHeartbeatTest {
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


		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/AA-ALERT__frbcatdb", null);
	
	}
	
	@Test
	void testAa_ALERT__frbcatdbLoadHeartBeatElement() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/AA-ALERT__frbcatdb/results/SchemaHeartBeat.tsv", "\t", true, 28, header, inputTupleCollection);
		assertEquals(numRows,17);
		assertEquals(inputTupleCollection.size(),16);
		assertTrue(header.equals(headerExpected));
		
		
		
		assertEquals(inputTupleCollection.get(8).getNumNewAttrs(), 119);
		assertEquals(inputTupleCollection.get(6).getAttrsInjected(), 10);
		assertEquals(inputTupleCollection.get(5).getAttrsWithTypeUpd(), 92);
		assertEquals(inputTupleCollection.get(6).getMaintenance(), 8);
		assertEquals(inputTupleCollection.get(1).getAttrUpdsSum(), 39);


		//test all totalActivities
		int[] TotalAttrActivityTruthTable = new int[] {115,42,0,2,0,94,18,0,3,4,1,3,2,1,2,2};
		
		//test isReed, isTurf, isActive
		int[] isReedTruthTable = new int[] {1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0};
		int[] isTurfTruthTable = new int[] {0,0,0,1,0,0,0,0,1,1,1,1,1,1,1,1};
		int[] isActiveTruthTable = new int[] {1,1,0,1,0,1,1,0,1,1,1,1,1,1,1,1};
		
		
		int i = 0;
		for(SchemaHeartbeatElement tuple: inputTupleCollection) {
			assertEquals(tuple.getTotalAttrActivity(), TotalAttrActivityTruthTable[i]);
			
			assertEquals(tuple.getIsReed(), isReedTruthTable[i]);
			assertEquals(tuple.getIsTurf(), isTurfTruthTable[i]);
			assertEquals(tuple.getIsActive(), isActiveTruthTable[i]);
			
			i++;
		}
	}

}
