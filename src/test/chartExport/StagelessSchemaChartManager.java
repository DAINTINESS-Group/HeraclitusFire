package test.chartExport;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import chartexport.SchemaChartManager;
//import chartexport.exporters.LineChartExporter;
import datamodel.SchemaHeartbeatElement;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class StagelessSchemaChartManager extends SchemaChartManager{
	
	public StagelessSchemaChartManager(String prjName,
			ArrayList<SchemaHeartbeatElement> inputTupleCollection,
			HashMap<String, Integer> attributePositions,
			HashMap<Integer, ArrayList<SchemaHeartbeatElement>> tuplesPerRYFV0Collection,
			String outputFolderWithFigures, Stage primaryStage, Boolean dateMode) {
		super(prjName, inputTupleCollection, attributePositions, tuplesPerRYFV0Collection, outputFolderWithFigures, primaryStage, dateMode);
	}
	
	// @Override  
	// Subclass and override to get rid of the stage dependency
	// We do not call stage - so stage can be null!!
	// We test only if the series are created right - we leave the styling untested
	protected void launchChartExporters() {
		ArrayList<XYChart.Series<Number,Number>> ltSeries = (ArrayList<XYChart.Series<Number,Number>>) this.lineExporters.get(0).createSeries();
		assertEquals(ltSeries.get(0).getData().size(), 17);
		
		ArrayList<XYChart.Series<LocalDateTime,Number>> laSeries = (ArrayList<XYChart.Series<LocalDateTime,Number>>) this.lineExporters.get(1).createSeries();
		assertEquals(laSeries.get(0).getData().size(), 17);
		
		// expansion-maintenance series test
		ArrayList<XYChart.Series<String,Number>> emSeries = this.barExporters.get(0).createSeries();
		assertEquals(emSeries.get(0).getData().size(), 17);
		assertEquals(emSeries.get(1).getData().size(), 17);
		
		// total activity series test
		ArrayList<XYChart.Series<String,Number>> taSeries = this.barExporters.get(1).createSeries();
		assertEquals(taSeries.get(0).getData().size(), 17);
		
		// grouped by year series test
		/*
		 * There are no dates in Egee dataset and so no grouping to be done and tested
		 * Below are the tests for Atlas dataset
		 */	
//		ArrayList<XYChart.Series<Number,Number>> ltdSeries = (ArrayList<XYChart.Series<Number,Number>>) this.lineExporters.get(2).createSeries();
//		assertEquals(ltdSeries.get(0).getData().size(), 85);
//		ArrayList<XYChart.Series<LocalDateTime,Number>> ladSeries = (ArrayList<XYChart.Series<LocalDateTime,Number>>) this.lineExporters.get(3).createSeries();
//		assertEquals(ladSeries.get(0).getData().size(), 85);
//		ArrayList<XYChart.Series<String,Number>> gSeries = this.barExporters.get(2).createSeries();
//		assertEquals(gSeries.get(0).getData().size(), 4);
//		assertEquals(gSeries.get(0).getData().size(), 4);
	}

}
