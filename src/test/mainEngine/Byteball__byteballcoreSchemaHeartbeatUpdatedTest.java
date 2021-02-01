package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.SchemaHeartbeatElement;
import mainEngine.SchemaStatsMainEngine;

class Byteball__byteballcoreSchemaHeartbeatUpdatedTest {
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


		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/byteball__byteballcore", null);
	
	}
	@Test
	void testByteball__byteballcoreLoadHeartBeatElement() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/byteball__byteballcore/results/byteball__byteballcore_SchemaHeartBeat_Updated.tsv", "\t", true, 31, header, inputTupleCollection);
		assertEquals(numRows,13);
		assertEquals(inputTupleCollection.size(),12);
		assertTrue(header.equals(headerExpected));
		
		
		assertEquals(inputTupleCollection.get(4).getNumOldAttrs(), 332);
		assertEquals(inputTupleCollection.get(3).getAttrsInsWithTableIns(), 13);
		assertEquals(inputTupleCollection.get(6).getExpansion(), 4);
		assertEquals(inputTupleCollection.get(10).getMaintenance(), 14);
		assertEquals(inputTupleCollection.get(6).getTotalAttrActivity(), 4);

		
		int[] attrBirthsSumTruthTable = new int[] {319,0,0,13,0,1,4,0,0,0,0,1};
		
		int[] isReedTruthTable = new int[] {1,0,0,0,0,0,0,0,0,0,0,0};
		int[] isTurfTruthTable = new int[] {0,1,0,1,1,1,1,0,0,0,1,1};
		int[] isActiveTruthTable = new int[] {1,1,0,1,1,1,1,0,0,0,1,1};
		
		int i = 0;
		for(SchemaHeartbeatElement tuple: inputTupleCollection) {
			
			assertEquals(tuple.getAttrBirthsSum(), attrBirthsSumTruthTable[i]);
			
			assertEquals(tuple.getIsReed(), isReedTruthTable[i]);
			assertEquals(tuple.getIsTurf(), isTurfTruthTable[i]);
			assertEquals(tuple.getIsActive(), isActiveTruthTable[i]);
			
			i++;
		}
		
	}

}
