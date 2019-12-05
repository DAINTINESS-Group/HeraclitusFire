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
//import datamodel.SchemaHeartbeatElement;
import datamodel.TableDetailedStatsElement;

public class ScatterChartExporter{// extends Application{

	public ScatterChartExporter(String pOutputPath, String pTitle, HashMap<Integer, ArrayList<TableDetailedStatsElement>> pTuplesPerLADCollection, 
			String pXAttribute, String pYAttribute, HashMap<String, Integer> pAttributePositions, Stage primaryStage) {
		this.outputPath = pOutputPath;
		if (!outputPath.endsWith(".png"))
			outputPath = outputPath + ".png";

		if (pTitle != null)
			this.chartTitle = pTitle;
		else
			this.chartTitle = "";

		this.tuplesPerLADCollection = pTuplesPerLADCollection;
		this.attributePositions= pAttributePositions;
		this.xAttribute = pXAttribute;
		this.yAttribute = pYAttribute;
		//TODO CHECKS! IF any of the above is null, setup a flag as problem

		this.xAttributePos = this.attributePositions.get(this.xAttribute);
		this.yAttributePos = this.attributePositions.get(this.yAttribute);
		//TODO CHECKS! IF any of the above is null or negative, setup a flag as problem
		//or, before, can check if containsKey is true

		this.stage = primaryStage;
	}//end constructor

	public void start(Stage primaryStage) throws Exception {

		ArrayList<XYChart.Series<Number,Number>> allSeries = createSeries();
		if (allSeries.size() == 0)
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

		for(XYChart.Series<Number,Number> nextSeries: allSeries)
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
		
		File f = new File("resources/stylesheets/scatterChartStyle.css");
		scene.getStylesheets().clear();
		this.scatterChart.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
		
		//Export to png
		saveAsPng(scene, outputPath);
				
		//INVERT COMMENT STATUS THIS OUT ONCE DONE!
		//stage.show();
		stage.close();
		
	}//end scatterCreation
	
	public ArrayList<XYChart.Series<Number,Number>> createSeries() {
		ArrayList<XYChart.Series<Number,Number>> allSeries = new ArrayList<XYChart.Series<Number,Number>>();
		
		HashMap<Integer, String> ladLabels = new HashMap<Integer, String>();
		ladLabels.put(10, "Rigid.Dead");
		ladLabels.put(11, "Quiet.Dead");
		ladLabels.put(12, "Activ.Dead");
		ladLabels.put(20, "Rigid.Surv");
		ladLabels.put(21, "Quiet.Surv");
		ladLabels.put(22, "Activ.Surv");
		
		//double maxX = Integer.MIN_VALUE; double maxY = Integer.MIN_VALUE;
		//double minX = Integer.MAX_VALUE; double minY = Integer.MAX_VALUE;

		ArrayList<Integer> LADKeySet = new ArrayList<Integer>();
		for(Integer k: tuplesPerLADCollection.keySet())
			LADKeySet.add(k);
//		if(LADKeySet.size() < 1)
//			return allSeries;
		Collections.sort(LADKeySet);			//very important!!! now we know that data series are sorted by their LAD asc
												//therefore 0th is LAD = 10, 1st = LAD = 11, ...
		
		for(int i=0; i<LADKeySet.size();i++ ) {
			Integer LADvalue = LADKeySet.get(i);
			XYChart.Series<Number,Number> newSeries = new XYChart.Series<Number,Number>();
			//newSeries.setName(String.valueOf(LADvalue));
			newSeries.setName(ladLabels.get(LADvalue));
			ArrayList<TableDetailedStatsElement> tuples = tuplesPerLADCollection.get(LADvalue);
			for (TableDetailedStatsElement tuple: tuples) {
				Integer xValue = tuple.getIntValueByPosition(xAttributePos);
				Integer yValue = tuple.getIntValueByPosition(yAttributePos);
				//if (xValue > maxX) maxX = xValue; if (xValue < minX) minX = xValue;
				//if (yValue > maxY) maxY = yValue; if (yValue < minY) minY = yValue;
				if((xValue != TableDetailedStatsElement._ERROR_CODE) && (yValue != TableDetailedStatsElement._ERROR_CODE))
					newSeries.getData().add(new XYChart.Data<Number,Number>(xValue, yValue));
			}
			allSeries.add(newSeries);
		}//end for i = nextLAD value
		return allSeries;
	}

	/**
	 * SUPER USEFUL, DO NOT REMOVE: This method reports all the children nodes of the scatterchart
	 */
	private void reportChildrenOfChart() {
		System.out.println("\n\nStarting " + this.chartTitle);
		Set<Node> CHARTnodes = this.scatterChart.lookupAll("*");
		for (Node n : CHARTnodes) {
			System.out.println(n.getTypeSelector() +"\t" + n.toString() + "\t\t" + n.getStyle());
		}
	}

	/**
	 * Saves the scene to an Image of type png.
	 * 
	 * @param scene  a Scene to be exported
	 * @param path  a String with the path of the file to be exported
	 */
	private void saveAsPng(Scene scene, String path) {
		WritableImage image = scene.snapshot(null);
		File file = new File(path);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Equivalent and precursor to saveAsPng. Attn: it saves the scatterchart and NOT the scene.
	 */
	private void saveChart(){
		WritableImage image = this.scatterChart.snapshot(new SnapshotParameters(), null);
		File file = new File(outputPath);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e){}
	}//end saveChart


	private String xAttribute;
	private String yAttribute;
	private Integer xAttributePos;
	private Integer yAttributePos;
	private HashMap<String, Integer> attributePositions;
	private HashMap<Integer, ArrayList<TableDetailedStatsElement>> tuplesPerLADCollection;
	private String chartTitle;
	private String outputPath;
	private Stage stage;
	private ScatterChart<Number,Number> scatterChart;


}//end class
