package chartexport;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.stage.Stage;
import datamodel.SchemaHeartbeatElement;

public class SchemaChartManager {

	
	public SchemaChartManager(String prjName,
			ArrayList<SchemaHeartbeatElement> inputTupleCollection,
			HashMap<String, Integer> attributePositions,
			HashMap<Integer, ArrayList<SchemaHeartbeatElement>> tuplesPerRYFV0Collection,
			String outputFolderWithFigures, Stage primaryStage, Boolean _DATEMODE) {
		this.prjName = prjName;
		this.inputTupleCollection = inputTupleCollection;
		this.attributePositions = attributePositions;
		this.tuplesPerRYFV0Collection = tuplesPerRYFV0Collection;
		this.outputFolderWithFigures = outputFolderWithFigures;
		
		this.lineExporters = new ArrayList<LineChartExporter>();
		this.barExporters = new ArrayList<BarChartExporter>();
		this.groupedBarExporters = new ArrayList<GroupedBarChartExporter>();
		this.stage = primaryStage; 
		this._DATEMODE = _DATEMODE;
		System.out.println("************************ "+this.prjName);
	}//end constructor
	
	public int extractCharts() {
		// line charts
		ArrayList<String> ltidYAttributes = new ArrayList<String>();  // add the attributes we want
		ltidYAttributes.add("#numNewTables");
		LineChartExporter mlTablesID = new LineChartExporter(outputFolderWithFigures+"/"+"NumTablesOverID_multi.png", this.prjName+":\nSize(tables) over Time(versionID)", inputTupleCollection, 
				"trID", ltidYAttributes,	attributePositions, stage);
		this.lineExporters.add(mlTablesID);
		
		ArrayList<String> laidYAttributes = new ArrayList<String>();  // add the attributes we want
		laidYAttributes.add("#numNewAttrs");
		LineChartExporter mlAtrrsID = new LineChartExporter(outputFolderWithFigures+"/"+"NumAtrrsOverID_multi.png", this.prjName+":\nSize(attributes) over Time(versionID)", inputTupleCollection, 
				"trID", laidYAttributes,	attributePositions, stage);
		this.lineExporters.add(mlAtrrsID);
		// TODO: add charts over humanTime --> moved to _DATEMODE
		
		// bar charts
		ArrayList<String> emYAttributes = new ArrayList<String>();  // add the attributes we want
		emYAttributes.add("Expansion");
		emYAttributes.add("Maintenance");
		BarChartExporter bExpMainID = new BarChartExporter(outputFolderWithFigures+"/"+"ExpansionMaintenanceOverID.png", this.prjName+":\nExpandion & Maintenance over Time(versionID)", inputTupleCollection, 
				"trID", emYAttributes,	attributePositions, stage);
		this.barExporters.add(bExpMainID);
		
		ArrayList<String> taYAttributes = new ArrayList<String>();  // add the attributes we want
		taYAttributes.add("TotalAttrActivity");
		BarChartExporter bTotalActID = new BarChartExporter(outputFolderWithFigures+"/"+"TotalActivityOverID.png", this.prjName+":\nTotal Attribute Activity over Time(versionID)", inputTupleCollection, 
				"trID",	taYAttributes, attributePositions, stage);
		this.barExporters.add(bTotalActID);
		// TODO: add more charts
		
		// grouped bar charts
		if (_DATEMODE) {
			ArrayList<String> ltdYAttributes = new ArrayList<String>();  // add the attributes we want
			ltdYAttributes.add("#numNewTables");
			LineChartExporter mlTablesDate = new LineChartExporter(outputFolderWithFigures+"/"+"NumTablesOverHT_multi.png", this.prjName+":\nSize(tables) over Time(Human Time)", inputTupleCollection, 
					"humanTime", ltdYAttributes,	attributePositions, stage);
			this.lineExporters.add(mlTablesDate);
			
			ArrayList<String> ladYAttributes = new ArrayList<String>();  // add the attributes we want
			ladYAttributes.add("#numNewAttrs");
			LineChartExporter mlAtrrsDate = new LineChartExporter(outputFolderWithFigures+"/"+"NumAtrrsOverHT_multi.png", this.prjName+":\nSize(attributes) over Time(Human Time)", inputTupleCollection, 
					"humanTime", ladYAttributes,	attributePositions, stage);
			this.lineExporters.add(mlAtrrsDate);
			
			// grouped line charts example if needed
			ArrayList<String> labdusYAttributes = new ArrayList<String>();  // add the attributes we want
			labdusYAttributes.add("attrBirthsSum");
			labdusYAttributes.add("attrDeathsSum");
			labdusYAttributes.add("attrUpdsSum");
			LineChartExporter mlAtrrBirthsDeathsUpdsSums = new LineChartExporter(outputFolderWithFigures+"/"+"AtrrBirthsDeathsUpdatesSums_multi.png", this.prjName+":\nAttributes Births, Deaths & Updates Sum", inputTupleCollection, 
					"trID", labdusYAttributes,	attributePositions, stage);
			this.lineExporters.add(mlAtrrBirthsDeathsUpdsSums);
			
			// grouped bar charts
			ArrayList<String> tidpyYAttributes = new ArrayList<String>();  // add the attributes we want
			tidpyYAttributes.add("tablesIns");
			tidpyYAttributes.add("tablesDel");
			//tidpyYAttributes.add("attrDelta");
			GroupedBarChartExporter gTableInsDelPerYear = new GroupedBarChartExporter(outputFolderWithFigures+"/"+"TableActivityPerYear.png", this.prjName+":\nTable Insertions & Deletions per Year", tuplesPerRYFV0Collection, 
					"runningYearFromV0", tidpyYAttributes,	attributePositions, stage);
			this.groupedBarExporters.add(gTableInsDelPerYear);
			// TODO: add more grouped bar charts
		}
		//*/
		launchChartExporters();
		return 0;
	}

	// ZAS: Extracted to enable the testing process
	// ZAS: Hopefully now I can subclass and override
	protected void launchChartExporters() {
		for(LineChartExporter l: this.lineExporters) {
			try {
				l.start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//s.saveChart();
		}
		for(BarChartExporter b: this.barExporters) {
			try {
				b.start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//s.saveChart();
		}
		for(GroupedBarChartExporter b: this.groupedBarExporters) {
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
	private ArrayList<LineChartExporter> lineExporters;
	private ArrayList<BarChartExporter> barExporters;
	private ArrayList<GroupedBarChartExporter> groupedBarExporters;
	private Stage stage;
	private String prjName;
	private Boolean _DATEMODE;
}//end class
