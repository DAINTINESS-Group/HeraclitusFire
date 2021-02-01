package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.SchemaHeartbeatElement;
import mainEngine.SchemaStatsMainEngine;

class Accgit__aclSchemaHeartbeatUpdatedTest {

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


		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/accgit__acl", null);
	
	}
	@Test
	void testAccgit__aclLoadHeartBeatElement() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/accgit__acl/results/accgit__acl_SchemaHeartBeat_Updated.tsv", "\t", true, 31, header, inputTupleCollection);
		assertEquals(numRows,18);
		assertEquals(inputTupleCollection.size(),17);
		assertTrue(header.equals(headerExpected));
		
		
		assertEquals(inputTupleCollection.get(7).getAttrBirthsSum(), 10);
		assertEquals(inputTupleCollection.get(13).getAttrUpdsSum(), 11);
		assertEquals(inputTupleCollection.get(7).getTotalAttrActivity(), 20);
		assertEquals(inputTupleCollection.get(16).getRunningMonthFromV0(), 15);
		assertEquals(inputTupleCollection.get(7).getAttrsInsWithTableIns(), 3);

		
		//test all distFromV0InDays
		int[] distFromV0InDaysTruthTable = new int[] {0,9,36,64,65,65,65,68,69,71,71,71,118,119,239,239,430};
		
		int[] isReedTruthTable = new int[] {1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0};
		int[] isTurfTruthTable = new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0};
		int[] isActiveTruthTable = new int[] {1,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0};
		
		int i = 0;
		for(SchemaHeartbeatElement tuple: inputTupleCollection) {
			assertEquals(tuple.getDistFromV0InDays(), distFromV0InDaysTruthTable[i]);
			
			assertEquals(tuple.getIsReed(), isReedTruthTable[i]);
			assertEquals(tuple.getIsTurf(), isTurfTruthTable[i]);
			assertEquals(tuple.getIsActive(), isActiveTruthTable[i]);
			
			i++;
		}

	}

}
