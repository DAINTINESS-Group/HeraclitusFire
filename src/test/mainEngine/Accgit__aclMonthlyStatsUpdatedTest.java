package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.MonthSchemaStats;
import datamodel.SchemaHeartbeatElement;
import mainEngine.SchemaStatsMainEngine;

public class Accgit__aclMonthlyStatsUpdatedTest {
private static SchemaStatsMainEngine schemaStatsMainEngine; 
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		schemaStatsMainEngine = new SchemaStatsMainEngine("resources/accgit__acl", null);
	}
	@Test
	void testAccgit__aclMonthlySchemaStats() {
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		int numRows = schemaStatsMainEngine.loadData("resources/accgit__acl/results/accgit__acl_SchemaHeartbeat_Updated.tsv", "\t", true, 31, header, inputTupleCollection);
		assertEquals(numRows - 1, 17);
		
		File statsFileProduced = new File("resources/test/Profiling/accgit__acl_MonthlySchemaStats.tsv"); 
		Long originalTimeStamp = statsFileProduced.lastModified();
		
		ArrayList<MonthSchemaStats> monthlySchemaStatsCollection = schemaStatsMainEngine.extractMonthlySchemaStats("accgit__acl", inputTupleCollection, "resources/test/Profiling", true);
		assertEquals(monthlySchemaStatsCollection.size(), 15);
		
		Long newTimeStamp = statsFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		//Truth sum-values were manually calculated 
		int truth_active = 3;
		int truth_turf = 1;
		int truth_reed = 2;
		
		int[] truth_reeds = new int[] {1,0,1,0,0,0,0,0,0,0,0,0,0,0,0};
		int[] truth_turfs = new int[] {0,0,0,0,1,0,0,0,0,0,0,0,0,0,0};
		int[] truth_actives = new int[] {1,0,1,0,1,0,0,0,0,0,0,0,0,0,0};
		
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
