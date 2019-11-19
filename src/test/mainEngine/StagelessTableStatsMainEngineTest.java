package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import datamodel.TableDetailedStatsElement;

public class StagelessTableStatsMainEngineTest {

	/**
	 * To test {@link mainEngine.TableStatsMainEngine#processFolder()}.
	 * use this one instead of the TableStatsMainEngineTest
	 * to avoid creating stages.
	 *  
	 */
	
	
	@Test
	void testProcessFolderStagelessly() {
		StagelessTableStatsMainEngine stagelessTablStatsMainEngine = new StagelessTableStatsMainEngine("resources/Egee", null);
		
		ArrayList<String> headerExpected = new ArrayList<String>(Arrays.asList(
				"Table", "Duration", "Birth", "Death", "LastKnownVersion", 
				"BirthDate", "LKVDate", "YearOfBirth", "YearOfLKV", "DurationDays",
				"SchemaSize@Birth", "SchemaSize@LKV", "SchemaSizeAvg", "SchemaSizeResizeRatio", 
				"SumUpd", "CountVwUpd", "ATU", "UpdRate", "AvgUpdVolume", "SurvivalClass", "ActivityClass", "LADClass")); 
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<TableDetailedStatsElement> inputTupleCollection = new ArrayList<TableDetailedStatsElement>();
		
		
		//TODO: Fix! why does this fail, if I comment out the loadData() call?
		int numRows = stagelessTablStatsMainEngine.loadData("resources/Egee/results/tables_DetailedStats.tsv", "\t", true, 22, header, inputTupleCollection);
		assertEquals(numRows,13);
		assertEquals(inputTupleCollection.size(),12);
		assertTrue(header.equals(headerExpected));
		
		stagelessTablStatsMainEngine.processFolder();
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().size(), 4);
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().get(20).size(),4);
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().get(21).size(),3);
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().get(22).size(),3);
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().get(10).size(),2);
	}

}
