package test.chartExport;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import chartexport.TablesChartManager;
import datamodel.TableDetailedStatsElement;
import test.mainEngine.StagelessTableStatsMainEngine;

public class StagelessTablesChartManagerTest {
	private static StagelessTableStatsMainEngine stagelessTableStatsMainEngine;
	private static String prjName;
	private static ArrayList<TableDetailedStatsElement> inputTupleCollection;
	private static HashMap<String, Integer> attributePositions;
	private static HashMap<Integer, ArrayList<TableDetailedStatsElement>> tuplesPerLADCollection;
	private static HashMap<Integer, Double[]> durationByLADHeatmap;
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
		numRows = stagelessTableStatsMainEngine.produceFiguresAndStats();
		prjName = stagelessTableStatsMainEngine.getPrjName();
		inputTupleCollection = stagelessTableStatsMainEngine.getinputTupleCollection();
		attributePositions = stagelessTableStatsMainEngine.getAttributePositions();
		tuplesPerLADCollection = stagelessTableStatsMainEngine.getTuplesPerLADCollection();
		durationByLADHeatmap = stagelessTableStatsMainEngine.getDurationByLADHeatmap();
		stagelessTablesChartManager = new StagelessTablesChartManager(prjName, inputTupleCollection, attributePositions,tuplesPerLADCollection, durationByLADHeatmap, "", null, false);
		
		ArrayList<ArrayList<Integer>> numOfDataPerSeriesPerChart = stagelessTablesChartManager.extractScatterCharts();
		assertEquals(numOfDataPerSeriesPerChart.size(), 6);	// test number of charts
		int lastScatter = numOfDataPerSeriesPerChart.size() - 1;
		for(int i = 0; i < lastScatter; i++) {
			assertEquals(numOfDataPerSeriesPerChart.get(i).size(), 6);	// test number of series
			assertEquals(numOfDataPerSeriesPerChart.get(i).get(0), 2);	// test number of data in LAD 10
			assertEquals(numOfDataPerSeriesPerChart.get(i).get(3), 4);	// test number of data in LAD 20
			assertEquals(numOfDataPerSeriesPerChart.get(i).get(4), 3);	// test number of data in LAD 21
			assertEquals(numOfDataPerSeriesPerChart.get(i).get(5), 3);	// test number of data in LAD 22
		}
		// test for line chart
		assertEquals(numOfDataPerSeriesPerChart.get(lastScatter).size(), 1);	// test number of series
		assertEquals(numOfDataPerSeriesPerChart.get(lastScatter).get(0), 12);	// test number of data
		
	}
	
	@Test
	void testExtractCharts_T0_V0_Atlas_HappyDay() {
		stagelessTableStatsMainEngine = new StagelessTableStatsMainEngine("resources/Atlas", null);
		numRows = stagelessTableStatsMainEngine.produceFiguresAndStats();
		prjName = stagelessTableStatsMainEngine.getPrjName();
		inputTupleCollection = stagelessTableStatsMainEngine.getinputTupleCollection();
		attributePositions = stagelessTableStatsMainEngine.getAttributePositions();
		tuplesPerLADCollection = stagelessTableStatsMainEngine.getTuplesPerLADCollection();
		durationByLADHeatmap = stagelessTableStatsMainEngine.getDurationByLADHeatmap();
		stagelessTablesChartManager = new StagelessTablesChartManager(prjName, inputTupleCollection, attributePositions,tuplesPerLADCollection, durationByLADHeatmap, "", null, true);
		
		ArrayList<ArrayList<Integer>> numOfDataPerSeriesPerChart = stagelessTablesChartManager.extractScatterCharts();
		assertEquals(numOfDataPerSeriesPerChart.size(), 8);	// test number of charts
		int lastScatter = numOfDataPerSeriesPerChart.size() - 2;
		for(int i = 0; i < lastScatter; i++) {
			assertEquals(numOfDataPerSeriesPerChart.get(i).size(), 6);	// test number of series
			assertEquals(numOfDataPerSeriesPerChart.get(i).get(0), 7);	// test number of data in LAD 10
			assertEquals(numOfDataPerSeriesPerChart.get(i).get(1), 6);	// test number of data in LAD 11
			assertEquals(numOfDataPerSeriesPerChart.get(i).get(2), 2);	// test number of data in LAD 12
			assertEquals(numOfDataPerSeriesPerChart.get(i).get(3), 11);	// test number of data in LAD 20
			assertEquals(numOfDataPerSeriesPerChart.get(i).get(4), 37);	// test number of data in LAD 21
			assertEquals(numOfDataPerSeriesPerChart.get(i).get(5), 25);	// test number of data in LAD 22
		}
		// test for heatmap
		assertEquals(numOfDataPerSeriesPerChart.get(lastScatter).size(), 6);	// test number of series
		assertEquals(numOfDataPerSeriesPerChart.get(lastScatter).get(0), 20);	// test number of data in LAD 10
		assertEquals(numOfDataPerSeriesPerChart.get(lastScatter).get(1), 20);	// test number of data in LAD 11
		assertEquals(numOfDataPerSeriesPerChart.get(lastScatter).get(2), 20);	// test number of data in LAD 12
		assertEquals(numOfDataPerSeriesPerChart.get(lastScatter).get(3), 20);	// test number of data in LAD 20
		assertEquals(numOfDataPerSeriesPerChart.get(lastScatter).get(4), 20);	// test number of data in LAD 21
		assertEquals(numOfDataPerSeriesPerChart.get(lastScatter).get(5), 20);	// test number of data in LAD 22
		// test for line chart
		assertEquals(numOfDataPerSeriesPerChart.get(lastScatter+1).size(), 1);	// test number of series
		assertEquals(numOfDataPerSeriesPerChart.get(lastScatter+1).get(0), 88);	// test number of data
		
	}

}
