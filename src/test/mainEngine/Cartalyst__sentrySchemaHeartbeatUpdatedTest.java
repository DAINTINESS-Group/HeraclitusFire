package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.SchemaHeartbeatElement;
import mainEngine.SchemaStatsMainEngine;

class Cartalyst__sentrySchemaHeartbeatUpdatedTest {
	private static SchemaStatsMainEngine schemaStatsMainEngine; 
	private static ArrayList<String> headerExpected; 
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		headerExpected = new ArrayList<String>(Arrays.asList(
				"trID", "epochTime", "oldVer", "newVer", "humanTime", "distFromV0InDays", "runningYearFromV0", 
				"runningMonthFromV0", "#numOldTables", "#numNewTables", "#numOldAttrs", "#numNewAttrs", 
				"tablesIns", "tablesDel", "attrsInsWithTableIns", "attrsbDelWithTableDel", "attrsInjected", 
				"attrsEjected", "attrsWithTypeUpd", "attrsInPKUpd", "tableDelta", "attrDelta", "attrBirthsSum", 
				"attrDeathsSum", "attrUpdsSum", "Expansion", "Maintenance", "TotalAttrActivity", "isReed", "isTurf", "isActive")); 


		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/cartalyst__sentry", null);
	
	}
	@Test
	void testCartalyst__sentryLoadHeartBeatElement() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/cartalyst__sentry/results/cartalyst__sentry_SchemaHeartBeat_Updated.tsv", "\t", true, 31, header, inputTupleCollection);
		assertEquals(numRows,14);
		assertEquals(inputTupleCollection.size(),13);
		assertTrue(header.equals(headerExpected));
	

		assertEquals(inputTupleCollection.get(5).getAttrDelta(), 2);
		assertEquals(inputTupleCollection.get(4).getExpansion(), 3);
		assertEquals(inputTupleCollection.get(5).getMaintenance(), 7);
		assertEquals(inputTupleCollection.get(5).getAttrsInsWithTableIns(), 2);
		assertEquals(inputTupleCollection.get(5).getTotalAttrActivity(), 9);
		
		int[] numNewAttrsTruthTable = new int[] {26,27,27,27,27,29,29,30,32,33,33,33,33};
		
		int[] isReedTruthTable = new int[] {1,0,0,0,0,0,0,0,0,0,0,0,0};
		int[] isTurfTruthTable = new int[] {0,1,1,0,1,1,0,1,1,1,0,0,0};
		int[] isActiveTruthTable = new int[] {1,1,1,0,1,1,0,1,1,1,0,0,0};
		
		int i = 0;
		for(SchemaHeartbeatElement tuple: inputTupleCollection) {
			
			assertEquals(tuple.getNumNewAttrs(), numNewAttrsTruthTable[i]);
			
			assertEquals(tuple.getIsReed(), isReedTruthTable[i]);
			assertEquals(tuple.getIsTurf(), isTurfTruthTable[i]);
			assertEquals(tuple.getIsActive(), isActiveTruthTable[i]);
			
			i++;
		}
	}

}
