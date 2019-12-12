package test.chartExport;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import chartexport.TablesChartManager;
import datamodel.TableDetailedStatsElement;
import test.mainEngine.StagelessTableStatsMainEngine;

class StagelessTablesChartManagerTest {
	private static StagelessTableStatsMainEngine stagelessTableStatsMainEngine;
	private static String prjName;
	private static ArrayList<TableDetailedStatsElement> inputTupleCollection;
	private static HashMap<String, Integer> attributePositions;
	private static HashMap<Integer, ArrayList<TableDetailedStatsElement>> tuplesPerLADCollection;
	private static int numRows;
	private static TablesChartManager stagelessTablesChartManager;
	/**
	 * To test {@link mainEngine.SchemaChartManager#extractCharts()}.
	 * use this one instead of the TableStatsMainEngineTest
	 * to avoid creating stages.
	 *  
	 */

	@Test
	void testExtractCharts_T0_V1_Egee_noDates() {
		stagelessTableStatsMainEngine = new StagelessTableStatsMainEngine("resources/Egee", null);
		numRows = stagelessTableStatsMainEngine.produceTableFiguresAndStats();
		prjName = stagelessTableStatsMainEngine.getPrjName();
		inputTupleCollection = stagelessTableStatsMainEngine.getinputTupleCollection();
		attributePositions = stagelessTableStatsMainEngine.getAttributePositions();
		tuplesPerLADCollection = stagelessTableStatsMainEngine.getTuplesPerLADCollection();
		stagelessTablesChartManager = new StagelessTablesChartManager(prjName, inputTupleCollection, attributePositions,tuplesPerLADCollection, "", null);
		
		ArrayList<ArrayList<Integer>> numOfDataPerSeriesPerChart = stagelessTablesChartManager.extractScatterCharts();
		for(ArrayList<Integer> numOfdataPerSeries: numOfDataPerSeriesPerChart) {
			assertEquals(numOfdataPerSeries.size(), 4);	// test number of series
			assertEquals(numOfdataPerSeries.get(0), 2);	// test number of data in LAD 10
			assertEquals(numOfdataPerSeries.get(1), 4);	// test number of data in LAD 20
			assertEquals(numOfdataPerSeries.get(2), 3);	// test number of data in LAD 21
			assertEquals(numOfdataPerSeries.get(3), 3);	// test number of data in LAD 22
		}
		
	}
	
	@Test
	void testExtractCharts_T0_V0_Atlas_HappyDay() {
		stagelessTableStatsMainEngine = new StagelessTableStatsMainEngine("resources/Atlas", null);
		numRows = stagelessTableStatsMainEngine.produceTableFiguresAndStats();
		prjName = stagelessTableStatsMainEngine.getPrjName();
		inputTupleCollection = stagelessTableStatsMainEngine.getinputTupleCollection();
		attributePositions = stagelessTableStatsMainEngine.getAttributePositions();
		tuplesPerLADCollection = stagelessTableStatsMainEngine.getTuplesPerLADCollection();
		stagelessTablesChartManager = new StagelessTablesChartManager(prjName, inputTupleCollection, attributePositions,tuplesPerLADCollection, "", null);
		
		ArrayList<ArrayList<Integer>> numOfDataPerSeriesPerChart = stagelessTablesChartManager.extractScatterCharts();
		for(ArrayList<Integer> numOfdataPerSeries: numOfDataPerSeriesPerChart) {
			assertEquals(numOfdataPerSeries.size(), 6);	// test number of series
			assertEquals(numOfdataPerSeries.get(0), 7);	// test number of data in LAD 10
			assertEquals(numOfdataPerSeries.get(1), 6);	// test number of data in LAD 11
			assertEquals(numOfdataPerSeries.get(2), 2);	// test number of data in LAD 12
			assertEquals(numOfdataPerSeries.get(3), 11);	// test number of data in LAD 20
			assertEquals(numOfdataPerSeries.get(4), 37);	// test number of data in LAD 21
			assertEquals(numOfdataPerSeries.get(5), 25);	// test number of data in LAD 22
		}
		
	}

}
