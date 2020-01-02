package chartexport;

import java.util.ArrayList;
import java.util.HashMap;
//import javafx.application.Application;
import javafx.stage.Stage;
import datamodel.TableDetailedStatsElement;
import chartexport.exporters.AbstractScatterChartExporter;
import chartexport.exporters.LADScatterChartExporter;
import chartexport.exporters.ScatterChartExporter;

public class TablesChartManager {

	
	public TablesChartManager(String prjName,
			ArrayList<TableDetailedStatsElement> inputTupleCollection,
			HashMap<String, Integer> attributePositions,
			HashMap<Integer, ArrayList<TableDetailedStatsElement>> tuplesPerLADCollection,
			String outputFolderWithFigures, Stage primaryStage) {
		this.prjName = prjName;
		this.inputTupleCollection = inputTupleCollection;
		this.attributePositions = attributePositions;
		this.tuplesPerLADCollection = tuplesPerLADCollection;
		this.outputFolderWithFigures = outputFolderWithFigures;
		
		this.scatterExporters = new ArrayList<AbstractScatterChartExporter>();
		this.stage = primaryStage; 
System.out.println("************************ "+this.prjName);
	}//end constructor
	
	public ArrayList<ArrayList<Integer>> extractScatterCharts() {
		/*
		 * (String pOutputPath, String pTitle, HashMap<Integer, ArrayList<TableDetailedStatsElement>> pTuplesPerLADCollection, 
			String pXAttribute, String pYAttribute, HashMap<String, Integer> pAttributePositions)
			
			Table	Duration	Birth	Death	LastKnownVersion	SchemaSize@Birth	SchemaSize@LKV	SchemaSizeAvg	SchemaSizeResizeRatio	
			SumUpd	CountVwUpd	ATU	UpdRate	AvgUpdVolume	SurvivalClass	ActivityClass	LADClass	

		 */
		AbstractScatterChartExporter<Number> sGammaDur = new ScatterChartExporter(outputFolderWithFigures+"/"+"GammaDur.png", this.prjName+":\nDur over Sc.Size", tuplesPerLADCollection, 
				"SchemaSize@Birth", "Duration",	attributePositions, stage);
		this.scatterExporters.add(sGammaDur);

		AbstractScatterChartExporter<Number> sGamma = new ScatterChartExporter(outputFolderWithFigures+"/"+"GammaLKV.png", this.prjName+":\nLKV over Sc.Size", tuplesPerLADCollection, 
				"SchemaSize@Birth", "LastKnownVersion",	attributePositions, stage);
		this.scatterExporters.add(sGamma);

		AbstractScatterChartExporter<Number> sComet = new ScatterChartExporter(outputFolderWithFigures+"/"+"Comet.png", this.prjName+":\nUpdates over Sc.Size", tuplesPerLADCollection, 
				"SchemaSize@Birth", "SumUpd",	attributePositions, stage);
		this.scatterExporters.add(sComet);

		AbstractScatterChartExporter<Number> sInvGamma = new ScatterChartExporter(outputFolderWithFigures+"/"+"InvGammaLKV.png", this.prjName+":\nUpdates over LKV", tuplesPerLADCollection, 
				"LastKnownVersion",	"SumUpd", attributePositions, stage);
		this.scatterExporters.add(sInvGamma);

		AbstractScatterChartExporter<Number> sTriangle = new ScatterChartExporter(outputFolderWithFigures+"/"+"EmptyTriangle.png", this.prjName+":\nDuration over Birth", tuplesPerLADCollection, 
				"Birth", "Duration",	attributePositions, stage);
		this.scatterExporters.add(sTriangle);
		
		AbstractScatterChartExporter<String> sElectrolysis = new LADScatterChartExporter(outputFolderWithFigures+"/"+"Electrolysis.png", this.prjName+":\nSpan of Duration by LADClass", tuplesPerLADCollection, 
				"Duration", "LADClass",	attributePositions, stage);
		this.scatterExporters.add(sElectrolysis);
		
		return launchScatterChartExporters();
		//return 0;
	}

	// ZAS: Extracted to enable the testing process
	// ZAS: Hopefully now I can subclass and override
	protected ArrayList<ArrayList<Integer>> launchScatterChartExporters() {
		ArrayList<ArrayList<Integer>> numOfDataPerSeriesPerChart = new ArrayList<ArrayList<Integer>>();
		for(AbstractScatterChartExporter s: this.scatterExporters) {
			try {
				s.start(stage);
				ArrayList<Integer> sSeries = s.getNumOfDataPerSeries();
				numOfDataPerSeriesPerChart.add(sSeries);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//s.saveChart();
		}
		return numOfDataPerSeriesPerChart;
	}
	
	private ArrayList<TableDetailedStatsElement> inputTupleCollection;
	private HashMap<String, Integer> attributePositions;
	private HashMap<Integer, ArrayList<TableDetailedStatsElement>> tuplesPerLADCollection;
	private String outputFolderWithFigures;
	protected ArrayList<AbstractScatterChartExporter> scatterExporters;
	private Stage stage;
	private String prjName;
}//end class
