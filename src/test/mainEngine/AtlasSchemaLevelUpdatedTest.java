package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.SchemaHeartbeatElement;
import datamodel.SchemaLevelInfo;
import mainEngine.SchemaStatsMainEngine;

class AtlasSchemaLevelUpdatedTest {
	private static SchemaStatsMainEngine schemaStatsMainEngine; 
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/Atlas", null);
	}
	
	@Test
	void testAtlasSchemaLevelInfo() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/Atlas/results/Atlas_SchemaHeartBeat_Updated.tsv", "\t", true, 31, header, inputTupleCollection);
		assertEquals(numRows - 1, 85); //check without header (commits)
		
		File statsFileProduced = new File("resources/test/Profiling/Atlas_SchemaLevelInfo.tsv"); 
		Long originalTimeStamp = statsFileProduced.lastModified();
		SchemaLevelInfo schemaLevelInfo = schemaStatsMainEngine.extractSchemaLevelInfo("Atlas", inputTupleCollection, "resources/test/Profiling", true);
		Long newTimeStamp = statsFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		//schemaLevelInfo.get
		//Truth sum-values were manually calculated 
		int truth_active = 70;
		int truth_turf = 51;
		int truth_reed = 19;
		
		assertEquals(schemaLevelInfo.getActiveCommits(), truth_active);
		assertEquals(schemaLevelInfo.getTurfs(), truth_turf);
		assertEquals(schemaLevelInfo.getReeds(), truth_reed);
		
		assertEquals(schemaLevelInfo.getProjectDurationInMonths(), 32);
		
		///Truth values and Ratios manually calculated /// path to truth file: resources\test\IsValuesANDSummaries_TRUTH
		int ReedsPostV0 = 18;
		double ReedRatioAComm	= 0.27;
		double ReedRatioTComm	= 0.22;
		int ActivityDueToReeds	= 1366;
		int ActivityDueToReedsPostV0	= 657;
		int TurfsPostV0	= 51;
		double TurfRatioAComm	= 0.73;
		double TurfRatioTComm	= 0.6;
		int ActivityDueToTurf	= 214;
		int ActivityDueToTurfPostV0	= 214;
		double ActiveCommitRatePerMonth = 2.19;	
		double CommitRatePerMonth	= 2.66;
		double ActiveCommitRatio = 0.82; 
		
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
