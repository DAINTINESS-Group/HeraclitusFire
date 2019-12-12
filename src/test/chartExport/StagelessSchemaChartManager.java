package test.chartExport;

import java.util.ArrayList;
import java.util.HashMap;

import chartexport.SchemaChartManager;
import chartexport.exporters.AbstractBarChartExporter;
import chartexport.exporters.AbstractLineChartExporter;
import datamodel.SchemaHeartbeatElement;
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
	protected ArrayList<ArrayList<Integer>> launchChartExporters() {
		ArrayList<ArrayList<Integer>> numOfDataPerSeriesPerChart = new ArrayList<ArrayList<Integer>>();
		for(AbstractLineChartExporter l: this.lineExporters) {
				l.createSeries();
				ArrayList<Integer> lSeries = l.getNumOfDataPerSeries();
				numOfDataPerSeriesPerChart.add(lSeries);
		}
		for(AbstractBarChartExporter b: this.barExporters) {
				b.createSeries();
				ArrayList<Integer> bSeries = b.getNumOfDataPerSeries();
				numOfDataPerSeriesPerChart.add(bSeries);
		}
		return numOfDataPerSeriesPerChart;
	}

}
