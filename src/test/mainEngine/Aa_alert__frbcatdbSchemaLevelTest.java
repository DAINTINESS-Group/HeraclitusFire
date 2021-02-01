package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.SchemaHeartbeatElement;
import datamodel.SchemaLevelInfo;
import mainEngine.SchemaStatsMainEngine;

class Aa_alert__frbcatdbSchemaLevelTest {
private static SchemaStatsMainEngine schemaStatsMainEngine; 
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/AA-ALERT__frbcatdb", null);
	}
	
	@Test
	void testAa_ALERT__frbcatdbSchemaLevelInfo() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/AA-ALERT__frbcatdb/results/SchemaHeartBeat.tsv", "\t", true, 28, header, inputTupleCollection);
		assertEquals(numRows - 1, 16); //check without header (commits)
		
		
		File statsFileProduced = new File("resources/test/Profiling/AA-ALERT__frbcatdb_SchemaLevelInfo.tsv"); 
		Long originalTimeStamp = statsFileProduced.lastModified();
		SchemaLevelInfo schemaLevelInfo = schemaStatsMainEngine.extractSchemaLevelInfo("AA-ALERT__frbcatdb", inputTupleCollection, "resources/test/Profiling", true);
		Long newTimeStamp = statsFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		//Truth sum-values were manually calculated 
		assertEquals(schemaLevelInfo.getReeds(), 4);
		assertEquals(schemaLevelInfo.getTurfs(), 9);
		assertEquals(schemaLevelInfo.getActiveCommits(), 13);
		
		///Truth values and Ratios manually calculated /// path to truth file: resources\test\IsValuesANDSummaries_TRUTH
		int ReedsPostV0 = 3;
		double ReedRatioAComm	= 0.31;
		double ReedRatioTComm	= 0.25;
		int ActivityDueToReeds	= 269;
		int ActivityDueToReedsPostV0	= 154;
		int TurfsPostV0	= 9;
		double TurfRatioAComm	= 0.69;
		double TurfRatioTComm	= 0.56;
		int ActivityDueToTurf	= 20;
		int ActivityDueToTurfPostV0	= 20;
		double ActiveCommitRatePerMonth = 1.18;	
		double CommitRatePerMonth	= 1.45;
		double ActiveCommitRatio = 0.81; 
		
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
