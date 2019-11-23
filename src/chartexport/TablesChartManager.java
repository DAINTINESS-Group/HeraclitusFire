package chartexport;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.stage.Stage;
import datamodel.TableDetailedStatsElement;

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
		
		this.scatterExporters = new ArrayList<ScatterChartExporter>();
		this.categoryScatterExporters = new ArrayList<CategoryScatterChartExporter>();
		this.stage = primaryStage; 
System.out.println("************************ "+this.prjName);
	}//end constructor
	
	public int extractScatterCharts() {
		/*
		 * (String pOutputPath, String pTitle, HashMap<Integer, ArrayList<TableDetailedStatsElement>> pTuplesPerLADCollection, 
			String pXAttribute, String pYAttribute, HashMap<String, Integer> pAttributePositions)
			
			Table	Duration	Birth	Death	LastKnownVersion	SchemaSize@Birth	SchemaSize@LKV	SchemaSizeAvg	SchemaSizeResizeRatio	
			SumUpd	CountVwUpd	ATU	UpdRate	AvgUpdVolume	SurvivalClass	ActivityClass	LADClass	

		 */
		ScatterChartExporter sGammaDur = new ScatterChartExporter(outputFolderWithFigures+"/"+"GammaDur.png", this.prjName+":\nDur over Sc.Size", tuplesPerLADCollection, 
				"SchemaSize@Birth", "Duration",	attributePositions, stage);
		this.scatterExporters.add(sGammaDur);

		ScatterChartExporter sGamma = new ScatterChartExporter(outputFolderWithFigures+"/"+"GammaLKV.png", this.prjName+":\nLKV over Sc.Size", tuplesPerLADCollection, 
				"SchemaSize@Birth", "LastKnownVersion",	attributePositions, stage);
		this.scatterExporters.add(sGamma);

		ScatterChartExporter sComet = new ScatterChartExporter(outputFolderWithFigures+"/"+"Comet.png", this.prjName+":\nUpdates over Sc.Size", tuplesPerLADCollection, 
				"SchemaSize@Birth", "SumUpd",	attributePositions, stage);
		this.scatterExporters.add(sComet);

		ScatterChartExporter sInvGamma = new ScatterChartExporter(outputFolderWithFigures+"/"+"InvGammaLKV.png", this.prjName+":\nUpdates over LKV", tuplesPerLADCollection, 
				"LastKnownVersion",	"SumUpd", attributePositions, stage);
		this.scatterExporters.add(sInvGamma);

		ScatterChartExporter sTriangle = new ScatterChartExporter(outputFolderWithFigures+"/"+"EmptyTriangle.png", this.prjName+":\nDuration over Birth", tuplesPerLADCollection, 
				"Birth", "Duration",	attributePositions, stage);
		this.scatterExporters.add(sTriangle);
		
		///*
		CategoryScatterChartExporter csGammaDur = new CategoryScatterChartExporter(outputFolderWithFigures+"/"+"GammaDurcategory.png", this.prjName+":\nDur over Sc.Size", tuplesPerLADCollection, 
				"SchemaSize@Birth", "Duration",	attributePositions, stage);
		this.categoryScatterExporters.add(csGammaDur);

		CategoryScatterChartExporter csGamma = new CategoryScatterChartExporter(outputFolderWithFigures+"/"+"GammaLKVcategory.png", this.prjName+":\nLKV over Sc.Size", tuplesPerLADCollection, 
				"SchemaSize@Birth", "LastKnownVersion",	attributePositions, stage);
		this.categoryScatterExporters.add(csGamma);

		CategoryScatterChartExporter csComet = new CategoryScatterChartExporter(outputFolderWithFigures+"/"+"Cometcategory.png", this.prjName+":\nUpdates over Sc.Size", tuplesPerLADCollection, 
				"SchemaSize@Birth", "SumUpd",	attributePositions, stage);
		this.categoryScatterExporters.add(csComet);

		CategoryScatterChartExporter csInvGamma = new CategoryScatterChartExporter(outputFolderWithFigures+"/"+"InvGammaLKVcategory.png", this.prjName+":\nUpdates over LKV", tuplesPerLADCollection, 
				"LastKnownVersion",	"SumUpd", attributePositions, stage);
		this.categoryScatterExporters.add(csInvGamma);

		CategoryScatterChartExporter csTriangle = new CategoryScatterChartExporter(outputFolderWithFigures+"/"+"EmptyTrianglecategory.png", this.prjName+":\nDuration over Birth", tuplesPerLADCollection, 
				"Birth", "Duration",	attributePositions, stage);
		this.categoryScatterExporters.add(csTriangle);
		//*/
		
		launchScatterChartExporters();
		return 0;
	}

	// ZAS: Extracted to enable the testing process
	// ZAS: Hopefully now I can subclass and override
	protected void launchScatterChartExporters() {
		for(ScatterChartExporter s: this.scatterExporters) {
			try {
				s.start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//s.saveChart();
		}
		for(CategoryScatterChartExporter s: this.categoryScatterExporters) {
			try {
				s.start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//s.saveChart();
		}
	}
	
	private ArrayList<TableDetailedStatsElement> inputTupleCollection;
	private HashMap<String, Integer> attributePositions;
	private HashMap<Integer, ArrayList<TableDetailedStatsElement>> tuplesPerLADCollection;
	private String outputFolderWithFigures;
	private ArrayList<ScatterChartExporter> scatterExporters;
	private ArrayList<CategoryScatterChartExporter> categoryScatterExporters;
	private Stage stage;
	private String prjName;
}//end class
