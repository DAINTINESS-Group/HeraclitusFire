package test.mainEngine;

import java.util.ArrayList;
import java.util.HashMap;

//import chartexport.TablesChartManager;
import datamodel.TableDetailedStatsElement;
import javafx.stage.Stage;
import mainEngine.TableStatsMainEngine;
import test.chartExport.StagelessChartManager;

public class StagelessTableStatsMainEngine extends TableStatsMainEngine {

	public StagelessTableStatsMainEngine(String anInputProjectFolder, Stage primaryStage) {
		super(anInputProjectFolder, primaryStage);
	}

	protected void createChartManager() {
		tablesChartManager = new StagelessChartManager(prjName,
				inputTupleCollection, attributePositions, 
				tuplesPerLADCollection, outputFolderWithFigures, 
				null
				);
	}
	
	
	final public HashMap<Integer, ArrayList<TableDetailedStatsElement>> getTuplesPerLADCollection(){
		return this.tuplesPerLADCollection;
	};
	
	final public ArrayList<TableDetailedStatsElement> getinputTupleCollection(){ 
		return this.inputTupleCollection;
	}
}
