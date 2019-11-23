package test.chartExport;

import java.util.ArrayList;
import java.util.HashMap;

import chartexport.TablesChartManager;
import chartexport.ScatterChartExporter;
import datamodel.TableDetailedStatsElement;
import javafx.stage.Stage;

public class StagelessChartManager extends TablesChartManager {

	public StagelessChartManager(String prjName,
			ArrayList<TableDetailedStatsElement> inputTupleCollection,
			HashMap<String, Integer> attributePositions,
			HashMap<Integer, ArrayList<TableDetailedStatsElement>> tuplesPerLADCollection,
			String outputFolderWithFigures, Stage primaryStage) {
		super(prjName, inputTupleCollection, attributePositions, tuplesPerLADCollection, outputFolderWithFigures, primaryStage);
	}
	
	// @Override  
	// Subclass and override to get rid of the stage dependency
	// We do not call stage - so stage can be null!!
	protected void launchScatterChartExporters() {
		// TODO For now it does nothing but then it depends on the assertions
	}

}
