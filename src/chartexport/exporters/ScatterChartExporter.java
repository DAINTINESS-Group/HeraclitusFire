package chartexport.exporters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
//import javafx.util.StringConverter;

import javax.imageio.ImageIO;

import javafx.scene.Node;
import javafx.scene.Scene;
//import javafx.application.Application;
//import javafx.stage.Stage;
//import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
//import javafx.scene.chart.XYChart.Series;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import datamodel.IElement;
//import datamodel.SchemaHeartbeatElement;
import datamodel.TableDetailedStatsElement;

public class ScatterChartExporter extends AbstractScatterChartExporter<Number>{// extends Application{

	public ScatterChartExporter(String pOutputPath, String pTitle, HashMap<Integer, ArrayList<IElement>> pTuplesPerLADCollection, 
			String pXAttribute, String pYAttribute, HashMap<String, Integer> pAttributePositions, Stage primaryStage) {
		super(pOutputPath, pTitle, pTuplesPerLADCollection, pXAttribute, pYAttribute, pAttributePositions, primaryStage);
	}//end constructor

	public void start(Stage primaryStage) throws Exception {

		createSeries();
		if (this.allSeries.size() == 0)
			return;

		//double xTickUnit = Math.round((double) (maxX - minX)/10.0);
		//double yTickUnit = (double) (maxY - minY)/10.0;
		//final NumberAxis xAxis = new NumberAxis(xAttribute, minX - xTickUnit, maxX + xTickUnit, xTickUnit);
		//final NumberAxis yAxis = new NumberAxis(yAttribute, minY - yTickUnit, maxY + yTickUnit, yTickUnit);

		//EITHER the above, or the below
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();		
		xAxis.setAutoRanging(true);
		yAxis.setAutoRanging(true);

		xAxis.setLabel(xAttribute);                
		yAxis.setLabel(yAttribute);
		xAxis.setMinorTickVisible(false);
		yAxis.setMinorTickVisible(false);

		this.scatterChart = new ScatterChart<Number,Number>(xAxis,yAxis);

		for(XYChart.Series<Number,Number> nextSeries: this.allSeries)
			this.scatterChart.getData().add(nextSeries);

		this.scatterChart.setTitle(chartTitle);
		this.scatterChart.setHorizontalGridLinesVisible(false);
		this.scatterChart.setVerticalGridLinesVisible(false);
		
		xAxis.tickLabelFontProperty().set(Font.font(16));
		yAxis.tickLabelFontProperty().set(Font.font(16));
		
		//necessary, or axis tick values do not show in dump
		xAxis.setAnimated(false);
		yAxis.setAnimated(false);


		Scene scene  = new Scene(this.scatterChart, 500, 500);
		stage.setScene(scene);

		//switch the flag to true if you want a report of the children of the scatterChartand their styles
		Boolean reportChildrenFlag = false;
		if (reportChildrenFlag)
			reportChildrenOfChart();
		
		// TODO: find how to add resources to jar and load css files from there
		//File f = new File("resources/stylesheets/scatterChartStyle.css");
		//scene.getStylesheets().clear();
		//this.scatterChart.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
		this.scatterChart.getStylesheets().add(getClass().getResource("stylesheets/scatterChartStyle.css").toExternalForm());
		
		//Export to png
		saveAsPng(scene, outputPath);
				
		//INVERT COMMENT STATUS THIS OUT ONCE DONE!
		//stage.show();
		stage.close();
		
	}//end scatterCreation
	
	public void createSeries() {
		this.allSeries = new ArrayList<XYChart.Series<Number,Number>>();
		
		HashMap<Integer, String> ladLabels = new HashMap<Integer, String>();
		ladLabels.put(10, "Rigid.Dead");
		ladLabels.put(11, "Quiet.Dead");
		ladLabels.put(12, "Activ.Dead");
		ladLabels.put(20, "Rigid.Surv");
		ladLabels.put(21, "Quiet.Surv");
		ladLabels.put(22, "Activ.Surv");
		
		//double maxX = Integer.MIN_VALUE; double maxY = Integer.MIN_VALUE;
		//double minX = Integer.MAX_VALUE; double minY = Integer.MAX_VALUE;

		ArrayList<Integer> LADKeySet = new ArrayList<Integer>() ;
		//ATTN: added __all__ the values. Otherwise, if one class is missing, the style loader puts the next
		//style available and the colors are messed up...
		LADKeySet.add(10);LADKeySet.add(11);LADKeySet.add(12);LADKeySet.add(20);LADKeySet.add(21);LADKeySet.add(22);
		

		
//		for(Integer k: tuplesPerLADCollection.keySet()) {
//			LADKeySet.add(k);
//		}
////		if(LADKeySet.size() < 1)
////			return allSeries;
		Collections.sort(LADKeySet);			//very important!!! now we know that data series are sorted by their LAD asc
												//therefore 0th is LAD = 10, 1st = LAD = 11, ...
		
		for(int i=0; i<LADKeySet.size();i++ ) {
			Integer LADvalue = LADKeySet.get(i);
			XYChart.Series<Number,Number> newSeries = new XYChart.Series<Number,Number>();
			//newSeries.setName(String.valueOf(LADvalue));
			newSeries.setName(ladLabels.get(LADvalue));
			ArrayList<IElement> tuples = tuplesPerLADCollection.get(LADvalue);
			if (tuples != null) {
				for (IElement tuple: tuples) {
					Integer xValue = tuple.getIntValueByPosition(xAttributePos);
					Integer yValue = tuple.getIntValueByPosition(yAttributePos);
					//if (xValue > maxX) maxX = xValue; if (xValue < minX) minX = xValue;
					//if (yValue > maxY) maxY = yValue; if (yValue < minY) minY = yValue;
					if((xValue != IElement._ERROR_CODE) && (yValue != IElement._ERROR_CODE))
						newSeries.getData().add(new XYChart.Data<Number,Number>(xValue, yValue));
				}
				}
			if (newSeries.getData().size() == 0)
				newSeries.setName("");
			this.allSeries.add(newSeries);
		}//end for i = nextLAD value
		//return this.allSeries;
	}


}//end class
