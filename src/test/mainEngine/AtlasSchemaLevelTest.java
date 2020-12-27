package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.SchemaHeartbeatElement;
import datamodel.SchemaLevelInfo;
import mainEngine.SchemaStatsMainEngine;

class AtlasSchemaLevelTest {
	private static SchemaStatsMainEngine schemaStatsMainEngine; 
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/Atlas", null);
	}
	
	@Test
	void testAtlasSchemaLevelInfo() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/Atlas/results/SchemaHeartbeat.tsv", "\t", true, 28, header, inputTupleCollection);
		assertEquals(numRows - 1, 85); //check without header (commits)
		
		File statsFileProduced = new File("resources/test/Profiling/Atlas_SchemaLevelInfo.tsv"); 
		Long originalTimeStamp = statsFileProduced.lastModified();
		SchemaLevelInfo schemaLevelInfo = schemaStatsMainEngine.extractSchemaLevelInfo("Atlas", inputTupleCollection, "resources/test/Profiling", true);
		Long newTimeStamp = statsFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		//schemaLevelInfo.get
		//Truth sum-values were manually calculated by SchemaHeartBeat.tsv file. 
		int truth_active = 70;
		int truth_turf = 51;
		int truth_reed = 19;
		
		assertEquals(schemaLevelInfo.getActiveCommits(), truth_active);
		assertEquals(schemaLevelInfo.getTurfs(), truth_turf);
		assertEquals(schemaLevelInfo.getReeds(), truth_reed);
		
		assertEquals(schemaLevelInfo.getProjectDurationInMonths(), 32);
	}

}
