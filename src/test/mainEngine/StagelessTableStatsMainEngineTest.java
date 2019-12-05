package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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

		File fileGamma = new File("x");
	}
}
