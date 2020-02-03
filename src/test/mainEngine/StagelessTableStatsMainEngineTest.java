package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import datamodel.MonthSchemaStats;
import datamodel.TableDetailedStatsElement;

public class StagelessTableStatsMainEngineTest {

	/**
	 * To test {@link mainEngine.TableStatsMainEngine#produceTableFiguresAndStats()}.
	 * use this one instead of the TableStatsMainEngineTest
	 * to avoid creating stages.
	 *  
	 */
	
	
	@Test
	void testProduceFiguresAndStats_T0_V1_Egee_noDates() {
		StagelessTableStatsMainEngine stagelessTablStatsMainEngine = new StagelessTableStatsMainEngine("resources/Egee", null);
		
		ArrayList<String> headerExpected = new ArrayList<String>(Arrays.asList(
				"Table", "Duration", "Birth", "Death", "LastKnownVersion", 
				"BirthDate", "LKVDate", "YearOfBirth", "YearOfLKV", "DurationDays",
				"SchemaSize@Birth", "SchemaSize@LKV", "SchemaSizeAvg", "SchemaSizeResizeRatio", 
				"SumUpd", "CountVwUpd", "ATU", "UpdRate", "AvgUpdVolume", "SurvivalClass", "ActivityClass", "LADClass")); 
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<TableDetailedStatsElement> inputTupleCollection = new ArrayList<TableDetailedStatsElement>();
		
		
		//TODO: Fix! why does this fail, if I comment out the loadData() call?
		int numRows = stagelessTablStatsMainEngine.loadData("resources/Egee/results/tables_DetailedStats.tsv", "\t", true, 22, header, inputTupleCollection);
		assertEquals(numRows,13);
		assertEquals(inputTupleCollection.size(),12);
		assertTrue(header.equals(headerExpected));
		
		stagelessTablStatsMainEngine.produceTableFiguresAndStats();
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().size(), 4);
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().get(20).size(),4);
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().get(21).size(),3);
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().get(22).size(),3);
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().get(10).size(),2);
		
		assertEquals(stagelessTablStatsMainEngine.getDurationByLADHeatmap().size(), 0);
		
		File statsFileProduced = new File("resources/test/Profiling/Egee_DurationByLADHeatmap.tsv"); 
		Long originalTimeStamp = statsFileProduced.lastModified();
		HashMap<Integer, Double[]> durationByLADHeatmap = stagelessTablStatsMainEngine.produceDurationByLADHeatmap("Egee", stagelessTablStatsMainEngine.getTuplesPerLADCollection(), "resources/test/Profiling", false);
		Long newTimeStamp = statsFileProduced.lastModified();
		assertTrue(newTimeStamp == originalTimeStamp);
	}

	
	@Test
	void testProduceFiguresAndStats_T0_V0_Atlas_HappyDay() {
		StagelessTableStatsMainEngine stagelessTablStatsMainEngine = new StagelessTableStatsMainEngine("resources/Atlas", null);
		
		ArrayList<String> headerExpected = new ArrayList<String>(Arrays.asList(
				"Table", "Duration", "Birth", "Death", "LastKnownVersion", 
				"BirthDate", "LKVDate", "YearOfBirth", "YearOfLKV", "DurationDays",
				"SchemaSize@Birth", "SchemaSize@LKV", "SchemaSizeAvg", "SchemaSizeResizeRatio", 
				"SumUpd", "CountVwUpd", "ATU", "UpdRate", "AvgUpdVolume", "SurvivalClass", "ActivityClass", "LADClass")); 
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<TableDetailedStatsElement> inputTupleCollection = new ArrayList<TableDetailedStatsElement>();
		// reference heat map for Atlas
		HashMap<Integer, Double[]> expectedHeatmap = new HashMap<Integer, Double[]>();
		Double[] lad10 = {0.045,0.0,0.0,0.0,0.0,0.034,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	    Double[] lad11 = {0.0,0.0,0.0,0.0,0.0,0.057,0.0,0.011,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	    Double[] lad12 = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.023,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	    Double[] lad20 = {0.0,0.0,0.011,0.0,0.011,0.091,0.0,0.011,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	    Double[] lad21 = {0.011,0.0,0.0,0.0,0.0,0.023,0.011,0.011,0.0,0.0,0.011,0.023,0.011,0.034,0.0,0.0,0.0,0.0,0.011,0.273};
	    Double[] lad22 = {0.0,0.0,0.0,0.0,0.0,0.011,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.011,0.011,0.0,0.0,0.011,0.239};
	    expectedHeatmap.put(10, lad10);
	    expectedHeatmap.put(11, lad11);
	    expectedHeatmap.put(12, lad12);
	    expectedHeatmap.put(20, lad20);
	    expectedHeatmap.put(21, lad21);
	    expectedHeatmap.put(22, lad22);
		
		
		//TODO: Fix! why does this fail, if I comment out the loadData() call?
		int numRows = stagelessTablStatsMainEngine.loadData("resources/Atlas/results/tables_DetailedStats.tsv", "\t", true, 22, header, inputTupleCollection);
		assertEquals(numRows,89);
		assertEquals(inputTupleCollection.size(),88);
		assertTrue(header.equals(headerExpected));
		
		stagelessTablStatsMainEngine.produceTableFiguresAndStats();
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().size(), 6);
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().get(10).size(),7);
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().get(11).size(),6);
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().get(12).size(),2);
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().get(20).size(),11);
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().get(21).size(),37);
		assertEquals(stagelessTablStatsMainEngine.getTuplesPerLADCollection().get(22).size(),25);

		int[] ladClasses = {10,11,12,20,21,22};
		assertEquals(stagelessTablStatsMainEngine.getDurationByLADHeatmap().size(), 6);
		for (Integer ladClass : ladClasses) {
			Double[] producedRow = stagelessTablStatsMainEngine.getDurationByLADHeatmap().get(ladClass);
			Double[] expectedRow = expectedHeatmap.get(ladClass);
			assertEquals(producedRow.length,expectedRow.length);
			for (int i = 0; i < producedRow.length; i++)
				assertEquals(expectedRow[i], producedRow[i], 3);
		}
		
		File statsFileProduced = new File("resources/test/Profiling/Atlas_DurationByLADHeatmap.tsv"); 
		Long originalTimeStamp = statsFileProduced.lastModified();
		HashMap<Integer, Double[]> durationByLADHeatmap = stagelessTablStatsMainEngine.produceDurationByLADHeatmap("Atlas", stagelessTablStatsMainEngine.getTuplesPerLADCollection(), "resources/test/Profiling", true);
		Long newTimeStamp = statsFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);

		File fileGamma = new File("x");
	}
}
