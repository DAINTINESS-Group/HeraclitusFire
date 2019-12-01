package test.chartExport;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import chartexport.TablesChartManager;
import chartexport.exporters.ScatterChartExporter;
import datamodel.TableDetailedStatsElement;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class StagelessTablesChartManager extends TablesChartManager {

	public StagelessTablesChartManager(String prjName,
			ArrayList<TableDetailedStatsElement> inputTupleCollection,
			HashMap<String, Integer> attributePositions,
			HashMap<Integer, ArrayList<TableDetailedStatsElement>> tuplesPerLADCollection,
			String outputFolderWithFigures, Stage primaryStage) {
		super(prjName, inputTupleCollection, attributePositions, tuplesPerLADCollection, outputFolderWithFigures, primaryStage);
	}
	
	// @Override  
	// Subclass and override to get rid of the stage dependency
	// We do not call stage - so stage can be null!!
	// We test only if the series are created right - we leave the styling untested
	protected void launchScatterChartExporters() {
		for(ScatterChartExporter s: this.scatterExporters) {
			ArrayList<XYChart.Series<Number,Number>> series = s.createSeries();
			// series is sorted by LAD value
			assertEquals(series.get(0).getData().size(), 2);
			assertEquals(series.get(1).getData().size(), 4);
			assertEquals(series.get(2).getData().size(), 3);
			assertEquals(series.get(3).getData().size(), 3);
		}
	}

}
