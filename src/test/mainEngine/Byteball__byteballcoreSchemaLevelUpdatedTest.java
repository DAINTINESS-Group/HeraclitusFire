package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.SchemaHeartbeatElement;
import datamodel.SchemaLevelInfo;
import mainEngine.SchemaStatsMainEngine;

class Byteball__byteballcoreSchemaLevelUpdatedTest {
private static SchemaStatsMainEngine schemaStatsMainEngine; 
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/byteball__byteballcore", null);
	}
	@Test
	void testByteball__byteballcoreSchemaLevelInfo() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/byteball__byteballcore/results/byteball__byteballcore_SchemaHeartBeat_Updated.tsv", "\t", true, 31, header, inputTupleCollection);
		assertEquals(numRows - 1, 12); //check without header (commits)
		
		File statsFileProduced = new File("resources/test/Profiling/byteball__byteballcore_SchemaLevelInfo.tsv"); 
		Long originalTimeStamp = statsFileProduced.lastModified();
		SchemaLevelInfo schemaLevelInfo = schemaStatsMainEngine.extractSchemaLevelInfo("byteball__byteballcore", inputTupleCollection, "resources/test/Profiling", true);
		Long newTimeStamp = statsFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		//Truth sum-values were manually calculated 
		assertEquals(schemaLevelInfo.getReeds(), 1);
		assertEquals(schemaLevelInfo.getTurfs(), 7);
		assertEquals(schemaLevelInfo.getActiveCommits(), 8);
		
		///Truth values and Ratios manually calculated /// path to truth file: resources\test\IsValuesANDSummaries_TRUTH
		int ReedsPostV0 = 0;
		double ReedRatioAComm	= 0.13;
		double ReedRatioTComm	= 0.08;
		int ActivityDueToReeds	= 319;
		int ActivityDueToReedsPostV0	= 0;
		int TurfsPostV0	= 7;
		double TurfRatioAComm	= 0.88;
		double TurfRatioTComm	= 0.58;
		int ActivityDueToTurf	= 36;
		int ActivityDueToTurfPostV0	= 36;
		double ActiveCommitRatePerMonth = 2;	
		double CommitRatePerMonth	= 3;
		double ActiveCommitRatio = 0.67; 
		
		
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
