package test.chartExport;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import chartexport.SchemaChartManager;
import datamodel.SchemaHeartbeatElement;
import test.mainEngine.StagelessSchemaStatsMainEngine;

public class StagelessSchemaChartManagerTest {
	private static StagelessSchemaStatsMainEngine stagelessSchemaStatsMainEngine;
	private static String prjName;
	private static ArrayList<SchemaHeartbeatElement> inputTupleCollection;
	private static HashMap<String, Integer> attributePositions;
	private static HashMap<Integer, ArrayList<SchemaHeartbeatElement>> tuplesPerRYFV0Collection;
	private static int numRows;
	private static SchemaChartManager stagelessSchemaChartManager;
	/**
	 * To test {@link mainEngine.SchemaChartManager#extractCharts()}.
	 * use this one instead of the TableStatsMainEngineTest
	 * to avoid creating stages.
	 *  
	 */

	@Test
	void testExtractCharts_T0_V1_Egee_noDates() {
		stagelessSchemaStatsMainEngine = new StagelessSchemaStatsMainEngine("resources/Egee", null);
		numRows = stagelessSchemaStatsMainEngine.produceSchemaFiguresAndStats();
		prjName = stagelessSchemaStatsMainEngine.getPrjName();
		inputTupleCollection = stagelessSchemaStatsMainEngine.getinputTupleCollection();
		attributePositions = stagelessSchemaStatsMainEngine.getAttributePositions();
		tuplesPerRYFV0Collection = stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection();
		stagelessSchemaChartManager = new StagelessSchemaChartManager(prjName, inputTupleCollection, attributePositions,tuplesPerRYFV0Collection, "", null, false);
		
		ArrayList<ArrayList<Integer>> numOfDataPerSeriesPerChart = stagelessSchemaChartManager.extractCharts();
		assertEquals(numOfDataPerSeriesPerChart.size(), 4);	// test number of charts
		assertEquals(numOfDataPerSeriesPerChart.get(0).get(0), 17);	// test number of data in NumTablesOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(1).get(0), 17);	// test number of data in NumAtrrsOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(2).get(0), 17);	// test number of data in ExpansionOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(2).get(1), 17);	// test number of data in MaintenanceOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(3).get(0), 17);	// test number of data in TotalActivityOverID chart
		
	}
	
	@Test
	void testExtractCharts_T0_V0_Atlas_HappyDay() {
		stagelessSchemaStatsMainEngine = new StagelessSchemaStatsMainEngine("resources/Atlas", null);
		numRows = stagelessSchemaStatsMainEngine.produceSchemaFiguresAndStats();
		prjName = stagelessSchemaStatsMainEngine.getPrjName();
		inputTupleCollection = stagelessSchemaStatsMainEngine.getinputTupleCollection();
		attributePositions = stagelessSchemaStatsMainEngine.getAttributePositions();
		tuplesPerRYFV0Collection = stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection();
		stagelessSchemaChartManager = new StagelessSchemaChartManager(prjName, inputTupleCollection, attributePositions,tuplesPerRYFV0Collection, "", null, true);
		
		ArrayList<ArrayList<Integer>> numOfDataPerSeriesPerChart = stagelessSchemaChartManager.extractCharts();
		assertEquals(numOfDataPerSeriesPerChart.size(), 7);	// test number of charts
		assertEquals(numOfDataPerSeriesPerChart.get(0).get(0), 85);	// test number of data in NumTablesOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(1).get(0), 85);	// test number of data in NumAtrrsOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(2).get(0), 85);	// test number of data in NumTablesOverHT chart
		assertEquals(numOfDataPerSeriesPerChart.get(3).get(0), 85);	// test number of data in NumAtrrsOverHT chart
		assertEquals(numOfDataPerSeriesPerChart.get(4).get(0), 85);	// test number of data in ExpansionOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(4).get(1), 85);	// test number of data in MaintenanceOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(5).get(0), 85);	// test number of data in TotalActivityOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(6).get(0), 4);	// test number of data in TablesInsPerYear chart
		assertEquals(numOfDataPerSeriesPerChart.get(6).get(1), 4);	// test number of data in TablesDelPerYear chart
		
	}

}
