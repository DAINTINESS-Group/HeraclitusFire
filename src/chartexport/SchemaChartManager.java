package chartexport;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.stage.Stage;
import datamodel.SchemaHeartbeatElement;
import chartexport.exporters.AbstractBarChartExporter;
import chartexport.exporters.AbstractLineChartExporter;
import chartexport.exporters.BarChartExporter;
import chartexport.exporters.DateLineChartExporter;
import chartexport.exporters.GroupedBarChartExporter;
import chartexport.exporters.LineChartExporter;

public class SchemaChartManager {

	
	public SchemaChartManager(String prjName,
			ArrayList<SchemaHeartbeatElement> inputTupleCollection,
			HashMap<String, Integer> attributePositions,
			HashMap<Integer, ArrayList<SchemaHeartbeatElement>> tuplesPerRYFV0Collection,
			String outputFolderWithFigures, Stage primaryStage, Boolean dateMode) {
		this.prjName = prjName;
		this.inputTupleCollection = inputTupleCollection;
		this.attributePositions = attributePositions;
		this.tuplesPerRYFV0Collection = tuplesPerRYFV0Collection;
		this.outputFolderWithFigures = outputFolderWithFigures;
		
		this.lineExporters = new ArrayList<AbstractLineChartExporter>();
		this.barExporters = new ArrayList<AbstractBarChartExporter>();
		this.stage = primaryStage; 
		this._DATEMODE = dateMode;
		System.out.println("************************ "+this.prjName);
	}//end constructor
	
	public int extractCharts() {
		// version id line charts
		HashMap<Integer, ArrayList<SchemaHeartbeatElement>> hashmapInputTupleCollection = new HashMap<Integer, ArrayList<SchemaHeartbeatElement>>();
		hashmapInputTupleCollection.put(0, inputTupleCollection);
		ArrayList<String> ltidYAttributes = new ArrayList<String>();  // add the attributes we want
		ltidYAttributes.add("#numNewTables");
		AbstractLineChartExporter mlTablesID = new LineChartExporter(outputFolderWithFigures+"/"+"NumTablesOverID.png", this.prjName+":\nSize(tables) over Time(versionID)", hashmapInputTupleCollection, 
				"trID", ltidYAttributes,	attributePositions, stage);
		this.lineExporters.add(mlTablesID);
		
		ArrayList<String> laidYAttributes = new ArrayList<String>();  // add the attributes we want
		laidYAttributes.add("#numNewAttrs");
		AbstractLineChartExporter mlAtrrsID = new LineChartExporter(outputFolderWithFigures+"/"+"NumAtrrsOverID.png", this.prjName+":\nSize(attributes) over Time(versionID)", hashmapInputTupleCollection, 
				"trID", laidYAttributes,	attributePositions, stage);
		this.lineExporters.add(mlAtrrsID);
		
		// bar charts
		ArrayList<String> emYAttributes = new ArrayList<String>();  // add the attributes we want
		emYAttributes.add("Expansion");
		emYAttributes.add("Maintenance");
		AbstractBarChartExporter bExpMainID = new BarChartExporter(outputFolderWithFigures+"/"+"ExpansionMaintenanceOverID.png", this.prjName+":\nExpandion & Maintenance over Time(versionID)", hashmapInputTupleCollection, 
				"trID", emYAttributes,	attributePositions, stage);
		this.barExporters.add(bExpMainID);
		
		ArrayList<String> taYAttributes = new ArrayList<String>();  // add the attributes we want
		taYAttributes.add("TotalAttrActivity");
		AbstractBarChartExporter bTotalActID = new BarChartExporter(outputFolderWithFigures+"/"+"TotalActivityOverID.png", this.prjName+":\nTotal Attribute Activity over Time(versionID)", hashmapInputTupleCollection, 
				"trID",	taYAttributes, attributePositions, stage);
		this.barExporters.add(bTotalActID);
		// TODO: add more charts
		
		if (_DATEMODE) {
			// human time line charts
			ArrayList<String> ltdYAttributes = new ArrayList<String>();  // add the attributes we want
			ltdYAttributes.add("#numNewTables");
			AbstractLineChartExporter mlTablesDate = new DateLineChartExporter(outputFolderWithFigures+"/"+"NumTablesOverHT.png", this.prjName+":\nSize(tables) over Time(Human Time)", hashmapInputTupleCollection, 
					"humanTime", ltdYAttributes,	attributePositions, stage);
			this.lineExporters.add(mlTablesDate);
			
			ArrayList<String> ladYAttributes = new ArrayList<String>();  // add the attributes we want
			ladYAttributes.add("#numNewAttrs");
			AbstractLineChartExporter mlAtrrsDate = new DateLineChartExporter(outputFolderWithFigures+"/"+"NumAtrrsOverHT.png", this.prjName+":\nSize(attributes) over Time(Human Time)", hashmapInputTupleCollection, 
					"humanTime", ladYAttributes,	attributePositions, stage);
			this.lineExporters.add(mlAtrrsDate);
			
			// grouped bar charts
			ArrayList<String> tidpyYAttributes = new ArrayList<String>();  // add the attributes we want
			tidpyYAttributes.add("tablesIns");
			tidpyYAttributes.add("tablesDel");
			//tidpyYAttributes.add("attrDelta");
			AbstractBarChartExporter gTableInsDelPerYear = new GroupedBarChartExporter(outputFolderWithFigures+"/"+"TableActivityPerYear.png", this.prjName+":\nTable Insertions & Deletions per Year", tuplesPerRYFV0Collection, 
					"runningYearFromV0", tidpyYAttributes,	attributePositions, stage);
			this.barExporters.add(gTableInsDelPerYear);
			// TODO: add more grouped bar charts
		}
		//*/
		launchChartExporters();
		return 0;
	}

	// ZAS: Extracted to enable the testing process
	// ZAS: Hopefully now I can subclass and override
	protected void launchChartExporters() {
		for(AbstractLineChartExporter l: this.lineExporters) {
			try {
				l.start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//s.saveChart();
		}
		for(AbstractBarChartExporter b: this.barExporters) {
			try {
				b.start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//s.saveChart();
		}
	}
	
	private ArrayList<SchemaHeartbeatElement> inputTupleCollection;
	private HashMap<String, Integer> attributePositions;
	private HashMap<Integer, ArrayList<SchemaHeartbeatElement>> tuplesPerRYFV0Collection;
	private String outputFolderWithFigures;
	protected ArrayList<AbstractLineChartExporter> lineExporters;
	protected ArrayList<AbstractBarChartExporter> barExporters;
	private Stage stage;
	private String prjName;
	private Boolean _DATEMODE;
}//end class
