package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.SchemaHeartbeatElement;
import datamodel.SchemaLevelInfo;
import mainEngine.SchemaStatsMainEngine;

class EgeeSchemaLevelTest {
	private static SchemaStatsMainEngine schemaStatsMainEngine; 
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/Atlas", null);
	}
	@Test
	void testEgeeSchemaLevelInfo() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/Egee/results/SchemaHeartbeat.tsv", "\t", true, 28, header, inputTupleCollection);
		assertEquals(numRows - 1, 17); //check without header (commits)
		
		

		File statsFileProduced = new File("resources/test/Profiling/Egee_SchemaLevelInfo.tsv"); 
		Long originalTimeStamp = statsFileProduced.lastModified();
		SchemaLevelInfo schemaLevelInfo = schemaStatsMainEngine.extractSchemaLevelInfo("Egee", inputTupleCollection, "resources/test/Profiling", true);
		Long newTimeStamp = statsFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		assertEquals(schemaLevelInfo.getReeds(), 3);
		assertEquals(schemaLevelInfo.getTurfs(), 13);
		assertEquals(schemaLevelInfo.getActiveCommits(), 16);
	}

}
