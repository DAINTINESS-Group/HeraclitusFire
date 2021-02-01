package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.SchemaHeartbeatElement;
import datamodel.SchemaLevelInfo;
import mainEngine.SchemaStatsMainEngine;

class Accgit__aclSchemaLevelTest {
private static SchemaStatsMainEngine schemaStatsMainEngine; 
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/accgit__acl", null);
	}
	
	@Test
	void testAccgit__aclSchemaLevelInfo() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/accgit__acl/results/SchemaHeartBeat.tsv", "\t", true, 28, header, inputTupleCollection);
		assertEquals(numRows - 1, 17); //check without header (commits)
		
		File statsFileProduced = new File("resources/test/Profiling/accgit__acl_SchemaLevelInfo.tsv"); 
		Long originalTimeStamp = statsFileProduced.lastModified();
		SchemaLevelInfo schemaLevelInfo = schemaStatsMainEngine.extractSchemaLevelInfo("accgit__acl", inputTupleCollection, "resources/test/Profiling", true);
		Long newTimeStamp = statsFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		//Truth sum-values were manually calculated 
		assertEquals(schemaLevelInfo.getReeds(), 2);
		assertEquals(schemaLevelInfo.getTurfs(), 1);
		assertEquals(schemaLevelInfo.getActiveCommits(), 3);
		
		///Truth values and Ratios manually calculated /// path to truth file: resources\test\IsValuesANDSummaries_TRUTH
		int ReedsPostV0 = 1;
		double ReedRatioAComm	= 0.67;
		double ReedRatioTComm	= 0.12;
		int ActivityDueToReeds	= 39;
		int ActivityDueToReedsPostV0	= 20;
		int TurfsPostV0	= 1;
		double TurfRatioAComm	= 0.33;
		double TurfRatioTComm	= 0.06;
		int ActivityDueToTurf	= 11;
		int ActivityDueToTurfPostV0	= 11;
		double ActiveCommitRatePerMonth = 0.2;	
		double CommitRatePerMonth	= 1.13;
		double ActiveCommitRatio = 0.18; 
		
		
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
