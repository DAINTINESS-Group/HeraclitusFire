package test.chartExport;

import java.util.ArrayList;
import java.util.HashMap;

import chartexport.TablesChartManager;
import chartexport.exporters.ScatterChartExporter;
import chartexport.exporters.AbstractScatterChartExporter;
import datamodel.TableDetailedStatsElement;
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
	protected ArrayList<ArrayList<Integer>> launchScatterChartExporters() {
		ArrayList<ArrayList<Integer>> numOfDataPerSeriesPerChart = new ArrayList<ArrayList<Integer>>();
		for(AbstractScatterChartExporter s: this.scatterExporters) {
			s.createSeries();
			ArrayList<Integer> sSeries = s.getNumOfDataPerSeries();
			numOfDataPerSeriesPerChart.add(sSeries);

		}
		return numOfDataPerSeriesPerChart;
	}

}
