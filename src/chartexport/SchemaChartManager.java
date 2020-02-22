package chartexport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
//import javafx.application.Application;
import javafx.stage.Stage;
import datamodel.MonthSchemaStats;
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
			ArrayList<MonthSchemaStats> monthlySchemaStatsCollection,
			HashMap<String, Integer> monthlyAttributePositions,
			String outputFolderWithFigures, Stage primaryStage, Boolean dateMode) {
		this.prjName = prjName;
		this.inputTupleCollection = inputTupleCollection;
		this.attributePositions = attributePositions;
		this.tuplesPerRYFV0Collection = tuplesPerRYFV0Collection;
		this.monthlySchemaStatsCollection =  monthlySchemaStatsCollection;
		this.monthlyAttributePositions = monthlyAttributePositions;
		this.outputFolderWithFigures = outputFolderWithFigures;
		
		this.lineExporters = new ArrayList<AbstractLineChartExporter>();
		this.barExporters = new ArrayList<AbstractBarChartExporter>();
		this.stage = primaryStage; 
		this._DATEMODE = dateMode;
		System.out.println("************************ "+this.prjName);
	}//end constructor
	
	public ArrayList<ArrayList<Integer>> extractCharts() {
		// version id line charts
		HashMap<Integer, ArrayList<SchemaHeartbeatElement>> hashmapInputTupleCollection = new HashMap<Integer, ArrayList<SchemaHeartbeatElement>>();
		hashmapInputTupleCollection.put(0, inputTupleCollection);
		ArrayList<String> ltidYAttributes = new ArrayList<String>();  // add the attributes we want
		ltidYAttributes.add("#numNewTables");
		AbstractLineChartExporter<Number> mlTablesID = new LineChartExporter(outputFolderWithFigures+"/"+"NumTablesOverID.png", this.prjName+":\nSize(tables) over Time(versionID)", (HashMap)hashmapInputTupleCollection, 
				"trID", ltidYAttributes,	attributePositions, stage);
		this.lineExporters.add(mlTablesID);
		
		ArrayList<String> laidYAttributes = new ArrayList<String>();  // add the attributes we want
		laidYAttributes.add("#numNewAttrs");
		AbstractLineChartExporter<Number> mlAttrsID = new LineChartExporter(outputFolderWithFigures+"/"+"NumAttrsOverID.png", this.prjName+":\nSize(attributes) over Time(versionID)", (HashMap)hashmapInputTupleCollection, 
				"trID", laidYAttributes,	attributePositions, stage);
		this.lineExporters.add(mlAttrsID);
		
		// bar charts
		ArrayList<String> emYAttributes = new ArrayList<String>();  // add the attributes we want
		emYAttributes.add("Expansion");
		emYAttributes.add("Maintenance");
		AbstractBarChartExporter bExpMainID = new BarChartExporter(outputFolderWithFigures+"/"+"TotalActivityExpMntncOverID.png", this.prjName+":\nExpansion & Maintenance over Time(versionID)", (HashMap)hashmapInputTupleCollection, 
				"trID", emYAttributes,	attributePositions, stage);
		this.barExporters.add(bExpMainID);
		
		ArrayList<String> taYAttributes = new ArrayList<String>();  // add the attributes we want
		taYAttributes.add("TotalAttrActivity");
		AbstractBarChartExporter bTotalActID = new BarChartExporter(outputFolderWithFigures+"/"+"TotalActivityOverID.png", this.prjName+":\nTotal Attribute Activity over Time(versionID)", (HashMap)hashmapInputTupleCollection, 
				"trID",	taYAttributes, attributePositions, stage);
		this.barExporters.add(bTotalActID);
		// TODO: add more charts
		
		if (_DATEMODE) {
			// human time line charts
			ArrayList<String> ltdYAttributes = new ArrayList<String>();  // add the attributes we want
			ltdYAttributes.add("#numNewTables");
			AbstractLineChartExporter<LocalDateTime> mlTablesDate = new DateLineChartExporter(outputFolderWithFigures+"/"+"NumTablesOverHT.png", this.prjName+":\nSize(tables) over Time(Human Time)", (HashMap)hashmapInputTupleCollection, 
					"humanTime", ltdYAttributes,	attributePositions, stage);
			this.lineExporters.add(mlTablesDate);
			
			ArrayList<String> ladYAttributes = new ArrayList<String>();  // add the attributes we want
			ladYAttributes.add("#numNewAttrs");
			AbstractLineChartExporter<LocalDateTime> mlAttrsDate = new DateLineChartExporter(outputFolderWithFigures+"/"+"NumAttrsOverHT.png", this.prjName+":\nSize(attributes) over Time(Human Time)", (HashMap)hashmapInputTupleCollection, 
					"humanTime", ladYAttributes,	attributePositions, stage);
			this.lineExporters.add(mlAttrsDate);
			
			// grouped bar charts
			ArrayList<String> tidpyYAttributes = new ArrayList<String>();  // add the attributes we want
			tidpyYAttributes.add("tablesIns");
			tidpyYAttributes.add("tablesDel");
			//tidpyYAttributes.add("attrDelta");
			AbstractBarChartExporter gTableInsDelPerYear = new GroupedBarChartExporter(outputFolderWithFigures+"/"+"TableActivityPerYear.png", this.prjName+":\nTable Insertions & Deletions per Year", (HashMap)tuplesPerRYFV0Collection, 
					"runningYearFromV0", tidpyYAttributes,	attributePositions, stage);
			this.barExporters.add(gTableInsDelPerYear);
			
			// monthly stats bar chart
			HashMap<Integer, ArrayList<MonthSchemaStats>> hashmapMonthlySchemaStatsCollection = new HashMap<Integer, ArrayList<MonthSchemaStats>>();
			hashmapMonthlySchemaStatsCollection.put(0, monthlySchemaStatsCollection);

			//Commented out, to make room for the exp vs growth in the 4X2 html page produced.
			//WORKS FULLY
//			ArrayList<String> mtaYAttributes = new ArrayList<String>();  // add the attributes we want
//			mtaYAttributes.add("TotalAttrActivity");
//			AbstractBarChartExporter bTotalActmID = new BarChartExporter(outputFolderWithFigures+"/"+"TotalActivityPerMonth.png", this.prjName+":\nTotal Attribute Activity over Time(monthID)", (HashMap)hashmapMonthlySchemaStatsCollection, 
//					"mID",	mtaYAttributes, monthlyAttributePositions, stage);
//			this.barExporters.add(bTotalActmID);
			
			ArrayList<String> emMOnthYAttributes = new ArrayList<String>();  
			emMOnthYAttributes.add("TotalExpansion");
			emMOnthYAttributes.add("TotalMaintenance");
			AbstractBarChartExporter bMonthlyExpMainID = new BarChartExporter(outputFolderWithFigures+"/"+"TotalActivityExpMntncOverMonth.png", this.prjName+":\nExpansion & Maintenance over Month(monthID)", (HashMap)hashmapMonthlySchemaStatsCollection, 
					"mID",	emMOnthYAttributes, monthlyAttributePositions, stage);
		//	AbstractBarChartExporter bExpMainID = new BarChartExporter(outputFolderWithFigures+"/"+"TotalActivityExpMntncOverID.png", this.prjName+":\nExpansion & Maintenance over Time(versionID)", (HashMap)hashmapInputTupleCollection, 
		//	"trID", emYAttributes,	attributePositions, stage);
			this.barExporters.add( bMonthlyExpMainID);
			
			
		}
		//*/
		return launchChartExporters();
		//return 0;
	}

	// ZAS: Extracted to enable the testing process
	// ZAS: Hopefully now I can subclass and override
	protected ArrayList<ArrayList<Integer>> launchChartExporters() {
		ArrayList<ArrayList<Integer>> numOfDataPerSeriesPerChart = new ArrayList<ArrayList<Integer>>();
		for(AbstractLineChartExporter l: this.lineExporters) {
			try {
				l.start(stage);
				ArrayList<Integer> lSeries = l.getNumOfDataPerSeries();
				numOfDataPerSeriesPerChart.add(lSeries);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//s.saveChart();
		}
		for(AbstractBarChartExporter b: this.barExporters) {
			try {
				b.start(stage);
				ArrayList<Integer> bSeries = b.getNumOfDataPerSeries();
				numOfDataPerSeriesPerChart.add(bSeries);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//s.saveChart();
		}
		return numOfDataPerSeriesPerChart;
	}
	
	private ArrayList<SchemaHeartbeatElement> inputTupleCollection;
	private HashMap<String, Integer> attributePositions;
	private HashMap<Integer, ArrayList<SchemaHeartbeatElement>> tuplesPerRYFV0Collection;
	private ArrayList<MonthSchemaStats> monthlySchemaStatsCollection;
	private HashMap<String, Integer> monthlyAttributePositions;
	private String outputFolderWithFigures;
	protected ArrayList<AbstractLineChartExporter> lineExporters;
	protected ArrayList<AbstractBarChartExporter> barExporters;
	private Stage stage;
	private String prjName;
	private Boolean _DATEMODE;
}//end class
