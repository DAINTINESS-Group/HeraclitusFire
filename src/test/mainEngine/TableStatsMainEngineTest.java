/**
 * 
 */
package test.mainEngine;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import datamodel.TableDetailedStatsElement;
import mainEngine.TableStatsMainEngine;


/**
 * @author pvassil
 *
 */
public class TableStatsMainEngineTest {

	private static TableStatsMainEngine tableStatsMainEngine; 
		
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		headerExpected = new ArrayList<String>(Arrays.asList(
				"Table", "Duration", "Birth", "Death", "LastKnownVersion",
				"BirthDate", "LKVDate", "YearOfBirth", "YearOfLKV", "DurationDays",
				"SchemaSize@Birth", "SchemaSize@LKV", "SchemaSizeAvg", "SchemaSizeResizeRatio", 
				"SumUpd", "CountVwUpd", "ATU", "UpdRate", "AvgUpdVolume", "SurvivalClass", "ActivityClass", "LADClass")); 

		tableStatsMainEngine = new TableStatsMainEngine("resources/Atlas", null);
		//MockStage ms = new MockStage();
		//ms.launch(null);
		//tableStatsMainEngine = new TableStatsMainEngine("resources/Atlas", ms.stage);
	}

	/**
	 * Test method for {@link mainEngine.TableStatsMainEngine#loadData(java.lang.String, java.lang.String, boolean, int, java.util.ArrayList, java.util.ArrayList)}.
	 */
	
	@Test
	final void testLoadData() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<TableDetailedStatsElement> inputTupleCollection = new ArrayList<TableDetailedStatsElement>();
		
		int numRows = tableStatsMainEngine.loadData("resources/Atlas/results/tables_DetailedStats.tsv", "\t", true, 22, header, inputTupleCollection);
		assertEquals(numRows,89, "Atlas tables are 88 + 1 line header");
		assertEquals(inputTupleCollection.size(),88);
		assertTrue(header.equals(headerExpected));
	}

//	/**
//	 * Do NOT TEST HERE  {@link mainEngine.TableStatsMainEngine#processFolder()}.
//	 * Rather, use the tests of StagelessTableStatsMainEngineTest
//	 * subclass of TableStatsMainEngine, test.mainEngine.StagelessTableStatsMainEngine
//	 *  
//	 */
//	@Test
//	final void testProcessFolder() {
//		int numRows = tableStatsMainEngine.processFolder();
//		assertEquals(numRows, 89);
//		CRASHES BECAUSE IT IS MISSING SOME APPLICATION WITH A STAGE
//	}
	
	private static ArrayList<String> headerExpected; 
}//end class
