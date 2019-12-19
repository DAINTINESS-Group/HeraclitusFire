package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import datamodel.SchemaHeartbeatElement;
import datamodel.SchemaLevelInfo;


public class StagelessSchemaStatsMainEngineTest {

	/**
	 * To test {@link mainEngine.SchemaStatsMainEngine#produceSchemaFiguresAndStats()}.
	 * use this one instead of the TableStatsMainEngineTest
	 * to avoid creating stages.
	 *  
	 */
	
	
	@Test
	void testProduceFiguresAndStats_T0_V1_Egee_noDates() {
		StagelessSchemaStatsMainEngine stagelessSchemaStatsMainEngine = new StagelessSchemaStatsMainEngine("resources/Egee", null);
		
		ArrayList<String> headerExpected = new ArrayList<String>(Arrays.asList(
				"trID", "epochTime", "oldVer", "newVer", "humanTime", "distFromV0InDays", "runningYearFromV0", 
				"runningMonthFromV0", "#numOldTables", "#numNewTables", "#numOldAttrs", "#numNewAttrs", 
				"tablesIns", "tablesDel", "attrsInsWithTableIns", "attrsbDelWithTableDel", "attrsInjected", 
				"attrsEjected", "attrsWithTypeUpd", "attrsInPKUpd", "tableDelta", "attrDelta", "attrBirthsSum", 
				"attrDeathsSum", "attrUpdsSum", "Expansion", "Maintenance", "TotalAttrActivity")); 
		String schemaLevelInfoString = "Egee\t\t\t\t17\t6\t10\t34\t71\t6\t2\t31\t10\t28\t12\t18\t1\t59\t41\t100\t3.4705882352941178\t\t\t2.411764705882353\t\t\t5.882352941176471\t\t\t1.6666666666666667";
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		
		
		//TODO: Fix! why does this fail, if I comment out the loadData() call?
		int numRows = stagelessSchemaStatsMainEngine.loadData("resources/Egee/results/SchemaHeartbeat.tsv", "\t", true, 28, header, inputTupleCollection);
		assertEquals(numRows,18);
		assertEquals(inputTupleCollection.size(),17);
		assertTrue(header.equals(headerExpected));
		
		File infoFileProduced = new File("resources/test/Profiling/Egee_SchemaLevelInfo.tsv"); 
		Long originalTimeStamp = infoFileProduced.lastModified();
		
		SchemaLevelInfo schemaLevelInfo = stagelessSchemaStatsMainEngine.extractSchemaLevelInfo("Egee", inputTupleCollection, "resources/test/Profiling", false);
		assertTrue(schemaLevelInfoString.equals(schemaLevelInfo.toString()));
		Long newTimeStamp = infoFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		stagelessSchemaStatsMainEngine.produceSchemaFiguresAndStats();
		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().size(), 0);
		
		File htmlFileProduced = new File("resources/test/Profiling/Egee_Summary.html"); 
		originalTimeStamp = htmlFileProduced.lastModified();
		
		int produceSummaryReturnCode = stagelessSchemaStatsMainEngine.produceSummaryHTML("Egee", "resources/Egee/figures", "resources/test/Profiling", "resources/test/Profiling");
		assertEquals(produceSummaryReturnCode, 0);
		newTimeStamp = htmlFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
	}
	
	@Test
	void testProduceFiguresAndStats_T0_V0_Atlas_HappyDay() {
		StagelessSchemaStatsMainEngine stagelessSchemaStatsMainEngine = new StagelessSchemaStatsMainEngine("resources/Atlas", null);
		
		ArrayList<String> headerExpected = new ArrayList<String>(Arrays.asList(
				"trID", "epochTime", "oldVer", "newVer", "humanTime", "distFromV0InDays", "runningYearFromV0", 
				"runningMonthFromV0", "#numOldTables", "#numNewTables", "#numOldAttrs", "#numNewAttrs", 
				"tablesIns", "tablesDel", "attrsInsWithTableIns", "attrsbDelWithTableDel", "attrsInjected", 
				"attrsEjected", "attrsWithTypeUpd", "attrsInPKUpd", "tableDelta", "attrDelta", "attrBirthsSum", 
				"attrDeathsSum", "attrUpdsSum", "Expansion", "Maintenance", "TotalAttrActivity")); 
		String schemaLevelInfoString = "Atlas\t971\t32\t3\t85\t56\t73\t709\t858\t34\t17\t233\t122\t154\t116\t245\t1\t387\t484\t871\t4.552941176470588\t12.09375\t129.0\t5.694117647058824\t15.125\t161.33333333333334\t10.24705882352941\t27.21875\t290.3333333333333\t1.3035714285714286";
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		
		
		//TODO: Fix! why does this fail, if I comment out the loadData() call?
		int numRows = stagelessSchemaStatsMainEngine.loadData("resources/Atlas/results/SchemaHeartbeat.tsv", "\t", true, 28, header, inputTupleCollection);
		assertEquals(numRows,86);
		assertEquals(inputTupleCollection.size(),85);
		assertTrue(header.equals(headerExpected));
		
		File infoFileProduced = new File("resources/test/Profiling/Atlas_SchemaLevelInfo.tsv"); 
		Long originalTimeStamp = infoFileProduced.lastModified();
		
		SchemaLevelInfo schemaLevelInfo = stagelessSchemaStatsMainEngine.extractSchemaLevelInfo("Atlas", inputTupleCollection, "resources/test/Profiling", true);
		assertTrue(schemaLevelInfoString.equals(schemaLevelInfo.toString()));
		Long newTimeStamp = infoFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		stagelessSchemaStatsMainEngine.produceSchemaFiguresAndStats();
		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().size(), 4);
		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().get(0).size(),1);
		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().get(1).size(),36);
		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().get(2).size(),40);
		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().get(3).size(),8);
		
		File htmlFileProduced = new File("resources/test/Profiling/Atlas_Summary.html"); 
		originalTimeStamp = htmlFileProduced.lastModified();
		
		int produceSummaryReturnCode = stagelessSchemaStatsMainEngine.produceSummaryHTML("Atlas", "resources/Atlas/figures", "resources/test/Profiling", "resources/test/Profiling");
		assertEquals(produceSummaryReturnCode, 0);
		newTimeStamp = htmlFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
	}

}
