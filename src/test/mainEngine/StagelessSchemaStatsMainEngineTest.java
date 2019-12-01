package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import datamodel.SchemaHeartbeatElement;

public class StagelessSchemaStatsMainEngineTest {

	/**
	 * To test {@link mainEngine.SchemaStatsMainEngine#processFolder()}.
	 * use this one instead of the TableStatsMainEngineTest
	 * to avoid creating stages.
	 *  
	 */
	
	
	@Test
	void testProcessFolderStagelessly() {
		StagelessSchemaStatsMainEngine stagelessSchemaStatsMainEngine = new StagelessSchemaStatsMainEngine("resources/Egee", null);
		
		ArrayList<String> headerExpected = new ArrayList<String>(Arrays.asList(
				"trID", "epochTime", "oldVer", "newVer", "humanTime", "distFromV0InDays", "runningYearFromV0", 
				"runningMonthFromV0", "#numOldTables", "#numNewTables", "#numOldAttrs", "#numNewAttrs", 
				"tablesIns", "tablesDel", "attrsInsWithTableIns", "attrsbDelWithTableDel", "attrsInjected", 
				"attrsEjected", "attrsWithTypeUpd", "attrsInPKUpd", "tableDelta", "attrDelta", "attrBirthsSum", 
				"attrDeathsSum", "attrUpdsSum", "Expansion", "Maintenance", "TotalAttrActivity")); 
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		
		
		//TODO: Fix! why does this fail, if I comment out the loadData() call?
		int numRows = stagelessSchemaStatsMainEngine.loadData("resources/Egee/results/SchemaHeartbeat.tsv", "\t", true, 28, header, inputTupleCollection);
		assertEquals(numRows,18);
		assertEquals(inputTupleCollection.size(),17);
		assertTrue(header.equals(headerExpected));
		
		stagelessSchemaStatsMainEngine.processFolder();
		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().size(), 0);
		/*
		 * There are no dates in Egee dataset and so no grouping to be done and tested
		 * Below are the tests for Atlas dataset
		 */		
//		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().size(), 4);
//		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().get(0).size(),1);
//		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().get(1).size(),36);
//		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().get(2).size(),40);
//		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().get(3).size(),8);
	}

}
