/**
 * 
 */
package test.mainEngine;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import datamodel.SchemaHeartbeatElement;
import mainEngine.SchemaStatsMainEngine;



/**
 * @author alexvou
 *
 */
public class SchemaStatsMainEngineTest {

	private static SchemaStatsMainEngine schemaStatsMainEngine; 
		
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		headerExpected = new ArrayList<String>(Arrays.asList(
				"trID", "epochTime", "oldVer", "newVer", "humanTime", "distFromV0InDays", "runningYearFromV0", 
				"runningMonthFromV0", "#numOldTables", "#numNewTables", "#numOldAttrs", "#numNewAttrs", 
				"tablesIns", "tablesDel", "attrsInsWithTableIns", "attrsbDelWithTableDel", "attrsInjected", 
				"attrsEjected", "attrsWithTypeUpd", "attrsInPKUpd", "tableDelta", "attrDelta", "attrBirthsSum", 
				"attrDeathsSum", "attrUpdsSum", "Expansion", "Maintenance", "TotalAttrActivity")); 

		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/Atlas", null);
		//MockStage ms = new MockStage();
		//ms.launch(null);
		//schemaStatsMainEngine = new SchemaStatsMainEngine("resources/Atlas", ms.stage);
	}

	/**
	 * Test method for {@link mainEngine.SchemaStatsMainEngine#loadData(java.lang.String, java.lang.String, boolean, int, java.util.ArrayList, java.util.ArrayList)}.
	 */
	
	@Test
	final void testLoadData() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		
		int numRows = schemaStatsMainEngine.loadData("resources/Atlas/results/SchemaHeartbeat.tsv", "\t", true, 28, header, inputTupleCollection);
		assertEquals(numRows,86, "Atlas versions are 85 + 1 line header");
		assertEquals(inputTupleCollection.size(),85);
		assertTrue(header.equals(headerExpected));
	}

//	/**
//	 * Do NOT TEST HERE  {@link mainEngine.SchemaStatsMainEngine#processFolder()}.
//	 * Rather, use the tests of StagelessSchemaStatsMainEngineTest
//	 * subclass of SchemaStatsMainEngine, test.mainEngine.StagelessSchemaStatsMainEngine
//	 *  
//	 */
//	@Test
//	final void testProcessFolder() {
//		int numRows = schemaStatsMainEngine.processFolder();
//		assertEquals(numRows, 86);
//		CRASHES BECAUSE IT IS MISSING SOME APPLICATION WITH A STAGE
//	}
	
	private static ArrayList<String> headerExpected; 
}//end class
