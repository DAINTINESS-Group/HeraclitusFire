package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.MonthSchemaStats;
import datamodel.SchemaHeartbeatElement;
import mainEngine.SchemaStatsMainEngine;

import java.util.Random;

public class Aa_alert_frbcatdbMonthlyStatsTest {
	private static SchemaStatsMainEngine schemaStatsMainEngine; 
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/AA-ALERT__frbcatdb", null);
	}
	
	@Test
	void testAa_alert_frbcatdbMonthlySchemaStats() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/AA-ALERT__frbcatdb/results/SchemaHeartbeat.tsv", "\t", true, 28, header, inputTupleCollection);
		assertEquals(numRows - 1, 16);
		
		File statsFileProduced = new File("resources/test/Profiling/AA-ALERT__frbcatdb_MonthlySchemaStats.tsv"); 
		Long originalTimeStamp = statsFileProduced.lastModified();
		
		ArrayList<MonthSchemaStats> monthlySchemaStatsCollection = schemaStatsMainEngine.extractMonthlySchemaStats("AA-ALERT__frbcatdb", inputTupleCollection, "resources/test/Profiling", true);
		assertEquals(monthlySchemaStatsCollection.size(), 12);
		
		Long newTimeStamp = statsFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		//Truth sum-values were manually calculated by SchemaHeartBeat.tsv file. 
		int truth_active = 13;
		int truth_turf = 9;
		int truth_reed = 4;
		
		int[] truth_reeds = new int[] {1,2,0,0,0,0,0,0,0,0,1,0};
		int[] truth_turfs = new int[] {0,1,0,0,0,0,0,0,0,0,0,8};
		int[] truth_actives = new int[] {1,3,0,0,0,0,0,0,0,0,1,8};
		
		int sum_active = 0;
		int sum_turf = 0;
		int sum_reed = 0;
		Random rand = new Random();
		int cnt=0;
		for(MonthSchemaStats mnt: monthlySchemaStatsCollection) {
			sum_active += mnt.getActiveCommits();
			sum_turf += mnt.getTurfs();
			sum_reed += mnt.getReeds();
			
			
			//Pick a random number 0-3
			 int upperBound = 3;
		 
		     int int_random = rand.nextInt(upperBound); 
		     if(int_random == 2)	//25% possibility to check -  test different values every time test is running
		     {
		    	 assertEquals(mnt.getReeds(), truth_reeds[cnt]);
		    	 assertEquals(mnt.getTurfs(),truth_turfs[cnt]);
		    	 assertEquals(mnt.getActiveCommits(), truth_actives[cnt]);
		     }
		     cnt++;
		}
		
		assertEquals(sum_active,truth_active);
		assertEquals(sum_turf,truth_turf);
		assertEquals(sum_reed,truth_reed);
	}

}
