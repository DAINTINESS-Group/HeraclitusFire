package test.mainEngine;

import java.util.ArrayList;
import java.util.HashMap;

//import chartexport.TablesChartManager;
import datamodel.TableDetailedStatsElement;
import javafx.stage.Stage;
import mainEngine.TableStatsMainEngine;
import test.chartExport.StagelessTablesChartManager;

public class StagelessTableStatsMainEngine extends TableStatsMainEngine {

	public StagelessTableStatsMainEngine(String anInputProjectFolder, Stage primaryStage) {
		super(anInputProjectFolder, primaryStage);
	}

	protected void createChartManager() {
		tablesChartManager = new StagelessTablesChartManager(prjName,
				inputTupleCollection, attributePositions, 
				tuplesPerLADCollection, outputFolderWithFigures, 
				null, _DATEMODE
				);
	}
	
	public String getPrjName(){
		return this.prjName;
	}
	
	public HashMap<String, Integer> getAttributePositions(){
		return this.attributePositions;
	}
	
	
	final public HashMap<Integer, ArrayList<TableDetailedStatsElement>> getTuplesPerLADCollection(){
		return this.tuplesPerLADCollection;
	};
	
	final public ArrayList<TableDetailedStatsElement> getinputTupleCollection(){ 
		return this.inputTupleCollection;
	}
}
