package chartexport.exporters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.imageio.ImageIO;

import javafx.scene.Node;
import javafx.scene.Scene;
//import javafx.application.Application;
//import javafx.stage.Stage;
//import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import datamodel.IElement;
import datamodel.SchemaHeartbeatElement;

public abstract class AbstractBarChartExporter {

	public AbstractBarChartExporter(String pOutputPath, String pTitle, HashMap<Integer, ArrayList<IElement>> inputTupleCollection, 
			String pXAttribute, ArrayList<String> pYAttributes, HashMap<String, Integer> pAttributePositions, Stage primaryStage) {
		this.outputPath = pOutputPath;
		if (!outputPath.endsWith(".png"))
			outputPath = outputPath + ".png";

		if (pTitle != null)
			this.chartTitle = pTitle;
		else
			this.chartTitle = "";

		this.inputTupleCollection = inputTupleCollection;
		this.attributePositions= pAttributePositions;
		this.xAttribute = pXAttribute;
		this.yAttribute = pYAttributes.get(0);
		this.yAttributes = pYAttributes;
		//TODO CHECKS! IF any of the above is null, setup a flag as problem

		this.xAttributePos = this.attributePositions.get(this.xAttribute);
		this.yAttributePos = this.attributePositions.get(this.yAttribute);
		this.yAttributePoss = new ArrayList<Integer>();
		for (String yAttr : this.yAttributes) {  // possibly have i to have the same position
			this.yAttributePoss.add(this.attributePositions.get(yAttr));
		}
		//TODO CHECKS! IF any of the above is null or negative, setup a flag as problem
		//or, before, can check if containsKey is true

		this.stage = primaryStage;
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
		// TODO: change NumberAxis to show integer values & maybe manual scaling
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();		
		xAxis.setAutoRanging(true);
		yAxis.setAutoRanging(true);

		xAxis.setLabel(xAttribute);
		String yAxisLabel = yAttributes.get(0);
		for (int i=1; i<yAttributes.size();i++) {
			yAxisLabel += " & " + yAttributes.get(i);
		}
		yAxis.setLabel(yAxisLabel);
		//xAxis.setMinorTickVisible(false);
		yAxis.setMinorTickVisible(false);
		setLabelFormatter(yAxis);

		this.barChart = new BarChart<String,Number>(xAxis,yAxis);
		
		for(XYChart.Series<String,Number> nextSeries: this.allSeries)
			this.barChart.getData().add(nextSeries);

		this.barChart.setTitle(chartTitle);
		this.barChart.setHorizontalGridLinesVisible(false);
		this.barChart.setVerticalGridLinesVisible(false);
		
		xAxis.tickLabelFontProperty().set(Font.font(16));
		yAxis.tickLabelFontProperty().set(Font.font(16));
		
		//necessary, or axis tick values do not show in dump
		xAxis.setAnimated(false);
		yAxis.setAnimated(false);
		
		this.barChart.setBarGap(0);

		Scene scene  = new Scene(this.barChart, 500, 500);
		stage.setScene(scene);

		//switch the flag to true if you want a report of the children of the barChart and their styles
		Boolean reportChildrenFlag = false;
		if (reportChildrenFlag)
			reportChildrenOfChart();
		
		// TODO: find how to add resources to jar and load css files from there
		//File f = new File("resources/stylesheets/barChartStyle.css");
		//scene.getStylesheets().clear();
		//this.barChart.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
		this.barChart.getStylesheets().add(getClass().getResource("stylesheets/barChartStyle.css").toExternalForm());

		//Export to png
		saveAsPng(scene, outputPath);
				
		//INVERT COMMENT STATUS THIS OUT ONCE DONE!
		//stage.show();
		stage.close();
		
	}//end chartCreation
	
	public abstract void createSeries();
	protected abstract void setLabelFormatter(NumberAxis yAxis);

	/**
	 * SUPER USEFUL, DO NOT REMOVE: This method reports all the children nodes of the barChart
	 */
	protected void reportChildrenOfChart() {
		System.out.println("\n\nStarting " + this.chartTitle);
		Set<Node> CHARTnodes = this.barChart.lookupAll("*");
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
	protected void saveAsPng(Scene scene, String path) {
		WritableImage image = scene.snapshot(null);
		File file = new File(path);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Equivalent and precursor to saveAsPng. Attn: it saves the barChart and NOT the scene.
	 */
	protected void saveChart(){
		WritableImage image = this.barChart.snapshot(new SnapshotParameters(), null);
		File file = new File(outputPath);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e){}
	}//end saveChart
	
	public ArrayList<Integer> getNumOfDataPerSeries() {
		ArrayList<Integer> numOfDataPerSeries = new ArrayList<Integer>();
		for(XYChart.Series<String,Number> series: this.allSeries)
			numOfDataPerSeries.add(series.getData().size());
		return numOfDataPerSeries;
	}


	protected String xAttribute;
	protected String yAttribute;
	protected ArrayList<String> yAttributes;
	protected Integer xAttributePos;
	protected Integer yAttributePos;
	protected ArrayList<Integer> yAttributePoss;
	protected HashMap<String, Integer> attributePositions;
	protected HashMap<Integer, ArrayList<IElement>> inputTupleCollection;
	protected String chartTitle;
	protected String outputPath;
	protected Stage stage;
	protected ArrayList<XYChart.Series<String,Number>> allSeries;
	protected BarChart<String,Number> barChart;


}//end class
