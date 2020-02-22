package test.chartExport;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import chartexport.SchemaChartManager;
import datamodel.MonthSchemaStats;
import datamodel.SchemaHeartbeatElement;
import test.mainEngine.StagelessSchemaStatsMainEngine;

public class StagelessSchemaChartManagerTest {
	private static StagelessSchemaStatsMainEngine stagelessSchemaStatsMainEngine;
	private static String prjName;
	private static ArrayList<SchemaHeartbeatElement> inputTupleCollection;
	private static HashMap<String, Integer> attributePositions;
	private static HashMap<Integer, ArrayList<SchemaHeartbeatElement>> tuplesPerRYFV0Collection;
	private static ArrayList<MonthSchemaStats> monthlySchemaStatsCollection;
	private static HashMap<String, Integer> monthlyAttributePositions;
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
		numRows = stagelessSchemaStatsMainEngine.produceFiguresAndStats();
		prjName = stagelessSchemaStatsMainEngine.getPrjName();
		inputTupleCollection = stagelessSchemaStatsMainEngine.getinputTupleCollection();
		attributePositions = stagelessSchemaStatsMainEngine.getAttributePositions();
		tuplesPerRYFV0Collection = stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection();
		monthlySchemaStatsCollection = stagelessSchemaStatsMainEngine.getMonthlySchemaStatsCollection();
		monthlyAttributePositions = stagelessSchemaStatsMainEngine.getMonthlyAttributePositions();
		stagelessSchemaChartManager = new StagelessSchemaChartManager(prjName, inputTupleCollection, attributePositions, 
				tuplesPerRYFV0Collection, monthlySchemaStatsCollection, monthlyAttributePositions, "", null, false);
		
		ArrayList<ArrayList<Integer>> numOfDataPerSeriesPerChart = stagelessSchemaChartManager.extractCharts();
		assertEquals(numOfDataPerSeriesPerChart.size(), 4);	// test number of charts
		
		assertEquals(numOfDataPerSeriesPerChart.get(0).size(), 1);	// test number of series in NumTablesOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(0).get(0), 17);	// test number of data in NumTablesOverID chart
		
		assertEquals(numOfDataPerSeriesPerChart.get(1).size(), 1);	// test number of series in NumAtrrsOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(1).get(0), 17);	// test number of data in NumAtrrsOverID chart
		
		assertEquals(numOfDataPerSeriesPerChart.get(2).size(), 2);	// test number of series in ExpansionMaintenanceOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(2).get(0), 17);	// test number of data in ExpansionOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(2).get(1), 17);	// test number of data in MaintenanceOverID chart
		
		assertEquals(numOfDataPerSeriesPerChart.get(3).size(), 1);	// test number of series in TotalActivityOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(3).get(0), 17);	// test number of data in TotalActivityOverID chart
		
	}
	
	@Test
	void testExtractCharts_T0_V0_Atlas_HappyDay() {
		stagelessSchemaStatsMainEngine = new StagelessSchemaStatsMainEngine("resources/Atlas", null);
		numRows = stagelessSchemaStatsMainEngine.produceFiguresAndStats();
		prjName = stagelessSchemaStatsMainEngine.getPrjName();
		inputTupleCollection = stagelessSchemaStatsMainEngine.getinputTupleCollection();
		attributePositions = stagelessSchemaStatsMainEngine.getAttributePositions();
		tuplesPerRYFV0Collection = stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection();
		monthlySchemaStatsCollection = stagelessSchemaStatsMainEngine.getMonthlySchemaStatsCollection();
		monthlyAttributePositions = stagelessSchemaStatsMainEngine.getMonthlyAttributePositions();
		stagelessSchemaChartManager = new StagelessSchemaChartManager(prjName, inputTupleCollection, attributePositions, 
				tuplesPerRYFV0Collection, monthlySchemaStatsCollection, monthlyAttributePositions, "", null, true);
		
		ArrayList<ArrayList<Integer>> numOfDataPerSeriesPerChart = stagelessSchemaChartManager.extractCharts();
		assertEquals(numOfDataPerSeriesPerChart.size(), 8);	// test number of charts
		
		assertEquals(numOfDataPerSeriesPerChart.get(0).size(), 1);	// test number of series in NumTablesOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(0).get(0), 85);	// test number of data in NumTablesOverID chart
		
		assertEquals(numOfDataPerSeriesPerChart.get(1).size(), 1);	// test number of series in NumAtrrsOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(1).get(0), 85);	// test number of data in NumAtrrsOverID chart
		
		assertEquals(numOfDataPerSeriesPerChart.get(2).size(), 1);	// test number of series in NumTablesOverHT chart
		assertEquals(numOfDataPerSeriesPerChart.get(2).get(0), 85);	// test number of data in NumTablesOverHT chart
		
		assertEquals(numOfDataPerSeriesPerChart.get(3).size(), 1);	// test number of series in NumAtrrsOverHT chart
		assertEquals(numOfDataPerSeriesPerChart.get(3).get(0), 85);	// test number of data in NumAtrrsOverHT chart
		
		assertEquals(numOfDataPerSeriesPerChart.get(4).size(), 2);	// test number of series in ExpansionMaintenanceOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(4).get(0), 85);	// test number of data in ExpansionOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(4).get(1), 85);	// test number of data in MaintenanceOverID chart
		
		assertEquals(numOfDataPerSeriesPerChart.get(5).size(), 1);	// test number of series in TotalActivityOverID chart
		assertEquals(numOfDataPerSeriesPerChart.get(5).get(0), 85);	// test number of data in TotalActivityOverID chart
		
		assertEquals(numOfDataPerSeriesPerChart.get(6).size(), 2);	// test number of series in TableActivityPerYear chart
		assertEquals(numOfDataPerSeriesPerChart.get(6).get(0), 4);	// test number of data in TablesInsPerYear chart
		assertEquals(numOfDataPerSeriesPerChart.get(6).get(1), 4);	// test number of data in TablesDelPerYear chart
		
		//assertEquals(numOfDataPerSeriesPerChart.get(7).size(), 1);	// test number of series in TotalActivityOvermID chart
		//assertEquals(numOfDataPerSeriesPerChart.get(7).get(0), 33);	// test number of data in TotalActivityOvermID chart

		assertEquals(numOfDataPerSeriesPerChart.get(7).size(), 2);	// test number of series in ExpansionMaintenanceOverMonthID chart
		assertEquals(numOfDataPerSeriesPerChart.get(7).get(0), 33);	// test number of data in ExpansionOverMonthID chart
		assertEquals(numOfDataPerSeriesPerChart.get(7).get(1), 33);	// test number of data in MaintenanceOverMonthID chart
		
	}

}
