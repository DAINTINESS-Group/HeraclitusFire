package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.MonthSchemaStats;
import datamodel.SchemaHeartbeatElement;
import mainEngine.SchemaStatsMainEngine;

class AtlasMonthlyStatsTest {
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
	void testAtlasMonthlySchemaStats() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/Atlas/results/SchemaHeartbeat.tsv", "\t", true, 28, header, inputTupleCollection);
	
	
		File statsFileProduced = new File("resources/test/Profiling/Atlas_MonthlySchemaStats.tsv"); 
		Long originalTimeStamp = statsFileProduced.lastModified();
		ArrayList<MonthSchemaStats> monthlySchemaStatsCollection = schemaStatsMainEngine.extractMonthlySchemaStats("Atlas", inputTupleCollection, "resources/test/Profiling", true);
		assertEquals(monthlySchemaStatsCollection.size(), 33);
		Long newTimeStamp = statsFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		//Truth sum-values were manually calculated by SchemaHeartBeat.tsv file. 
		int truth_active = 70;
		int truth_turf = 51;
		int truth_reed = 19;
		
		int sum_active = 0;
		int sum_turf = 0;
		int sum_reed = 0;
		for(MonthSchemaStats mnt: monthlySchemaStatsCollection) {
			sum_active += mnt.getActiveCommits();
			sum_turf += mnt.getTurfs();
			sum_reed += mnt.getReeds();
		}
		
		assertEquals(sum_active,truth_active);
		assertEquals(sum_turf,truth_turf);
		assertEquals(sum_reed,truth_reed);
	}

}
