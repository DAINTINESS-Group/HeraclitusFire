package test.mainEngine;

import java.util.ArrayList;
import java.util.HashMap;

//import chartexport.SchemaChartManager;
import datamodel.SchemaHeartbeatElement;
import javafx.stage.Stage;
import mainEngine.SchemaStatsMainEngine;
import test.chartExport.StagelessSchemaChartManager;

public class StagelessSchemaStatsMainEngine extends SchemaStatsMainEngine{
	
	public StagelessSchemaStatsMainEngine(String anInputProjectFolder, Stage primaryStage) {
		super(anInputProjectFolder, primaryStage);
	}

	protected void createChartManager() {
		schemaChartManager = new StagelessSchemaChartManager(prjName,
				inputTupleCollection, attributePositions, 
				tuplesPerRYFV0Collection, outputFolderWithFigures, 
				null, _DATEMODE
				);
	}
	
	public String getPrjName(){
		return this.prjName;
	}
	
	public HashMap<String, Integer> getAttributePositions(){
		return this.attributePositions;
	}
	
	
	final public HashMap<Integer, ArrayList<SchemaHeartbeatElement>> getTuplesPerRYFV0Collection(){
		return this.tuplesPerRYFV0Collection;
	};
	
	final public ArrayList<SchemaHeartbeatElement> getinputTupleCollection(){ 
		return this.inputTupleCollection;
	}
}
