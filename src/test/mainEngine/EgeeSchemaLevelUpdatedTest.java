package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.SchemaHeartbeatElement;
import datamodel.SchemaLevelInfo;
import mainEngine.SchemaStatsMainEngine;

class EgeeSchemaLevelUpdatedTest {
	private static SchemaStatsMainEngine schemaStatsMainEngine; 
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/Egee", null);
	}
	@Test
	void testEgeeSchemaLevelInfo() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/Egee/results/Egee_SchemaHeartBeat_Updated.tsv", "\t", true, 31, header, inputTupleCollection);
		assertEquals(numRows - 1, 17); //check without header (commits)
		
		

		File statsFileProduced = new File("resources/test/Profiling/Egee_SchemaLevelInfo.tsv"); 
		Long originalTimeStamp = statsFileProduced.lastModified();
		SchemaLevelInfo schemaLevelInfo = schemaStatsMainEngine.extractSchemaLevelInfo("Egee", inputTupleCollection, "resources/test/Profiling", true);
		Long newTimeStamp = statsFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		
		//Truth sum-values were manually calculated  
		assertEquals(schemaLevelInfo.getReeds(), 3);
		assertEquals(schemaLevelInfo.getTurfs(), 13);
		assertEquals(schemaLevelInfo.getActiveCommits(), 16);
		
				
		///Truth values and Ratios manually calculated /// path to truth file: resources\test\IsValuesANDSummaries_TRUTH
		int ReedsPostV0 = 2;
		double ReedRatioAComm	= 0.19;
		double ReedRatioTComm	= 0.18;
		int ActivityDueToReeds	= 95;
		int ActivityDueToReedsPostV0	= 61;
		int TurfsPostV0	= 13;
		double TurfRatioAComm	= 0.81;
		double TurfRatioTComm	= 0.76;
		int ActivityDueToTurf	= 39;
		int ActivityDueToTurfPostV0	= 39;
		double ActiveCommitRatePerMonth = 0;	
		double CommitRatePerMonth	= 0;
		double ActiveCommitRatio = 0.94; 
		
		assertEquals(schemaLevelInfo.getReedsPostV0(), ReedsPostV0);
		assertEquals(schemaLevelInfo.getReedRatioAComm(), ReedRatioAComm);
		assertEquals(schemaLevelInfo.getReedRatioTComm(), ReedRatioTComm);
		assertEquals(schemaLevelInfo.getActivityDueToReeds(), ActivityDueToReeds);
		assertEquals(schemaLevelInfo.getActivityDueToReedsPostV0(), ActivityDueToReedsPostV0);
		assertEquals(schemaLevelInfo.getTurfsPostV0(), TurfsPostV0);
		assertEquals(schemaLevelInfo.getTurfRatioAComm(), TurfRatioAComm);
		assertEquals(schemaLevelInfo.getTurfRatioTComm(), TurfRatioTComm);
		assertEquals(schemaLevelInfo.getActivityDueToTurf(), ActivityDueToTurf);
		assertEquals(schemaLevelInfo.getActivityDueToTurfPostV0(), ActivityDueToTurfPostV0);
		assertEquals(schemaLevelInfo.getActiveCommitRatePerMonth(), ActiveCommitRatePerMonth);
		assertEquals(schemaLevelInfo.getCommitRatePerMonth(), CommitRatePerMonth);
		assertEquals(schemaLevelInfo.getActiveCommitRatio(), ActiveCommitRatio);
	}

}
