package chartexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import javafx.util.StringConverter;

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



import datamodel.SchemaHeartbeatElement;

public class BarChartExporter{// extends Application{

	public BarChartExporter(String pOutputPath, String pTitle, ArrayList<SchemaHeartbeatElement> inputTupleCollection, 
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

	//@Override
	public void start(Stage primaryStage) throws Exception {
		
		ArrayList<XYChart.Series<String,Number>> allSeries = new ArrayList<XYChart.Series<String,Number>>();
		
		for(int i=0; i<yAttributes.size();i++ ) {
			XYChart.Series<String,Number> newSeries = new XYChart.Series<String,Number>();
			newSeries.setName(yAttributes.get(i));
			for (SchemaHeartbeatElement tuple: inputTupleCollection) {
				Integer xValue = tuple.getIntValueByPosition(xAttributePos);
				Integer yValue = tuple.getIntValueByPosition(yAttributePoss.get(i));
				if (i > 0) yValue = -yValue;
				if((xValue != SchemaHeartbeatElement._ERROR_CODE) && (yValue != SchemaHeartbeatElement._ERROR_CODE))
					newSeries.getData().add(new XYChart.Data<String,Number>(xValue.toString(), yValue));
			}
			allSeries.add(newSeries);
		}

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
		yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
	        @Override
	        public String toString(Number value) {
	        	if (value.intValue() < 0)
	        		return String.format("%4d", -value.intValue());
	        	else
	        		return String.format("%4d", value.intValue());
	        }
	    });

		this.barChart = new BarChart<String,Number>(xAxis,yAxis);
//		String chartStyle = this.barChart.getStyle();
//		this.barChart.setStyle(chartStyle 
//				+ ".default-color0.chart-bar {-fx-bar-fill: blue;}" 
//				+ ".default-color1.chart-bar {-fx-bar-fill: red;}" 
//				+ ".default-color2.chart-bar {-fx-bar-fill: green;}");
		
		//this.barChart.getData().add(series);
		
		for(XYChart.Series<String,Number> nextSeries: allSeries)
			this.barChart.getData().add(nextSeries);

		this.barChart.setTitle(chartTitle);
		this.barChart.setHorizontalGridLinesVisible(false);
		this.barChart.setVerticalGridLinesVisible(false);
		
		
		String _FONT_SIZE = "16pt; ";	//NEVER BELOW 16pt
		String barStyle = this.barChart.getStyle();
		this.barChart.setStyle(barStyle + " -fx-font-size: " + _FONT_SIZE);
		xAxis.setStyle(" -fx-font-size: " + _FONT_SIZE);
		yAxis.setStyle(" -fx-font-size: " + _FONT_SIZE);
		xAxis.tickLabelFontProperty().set(Font.font(16));
		yAxis.tickLabelFontProperty().set(Font.font(16));
		
		//necessary, or axis tick values do not show in dump
		xAxis.setAnimated(false);
		yAxis.setAnimated(false);
		
		HashMap<Integer, String> seriesStyles = new HashMap<Integer, String>();
		seriesStyles.put(0, " -fx-bar-fill: blue; ");
		seriesStyles.put(1, " -fx-bar-fill: red; ");
		seriesStyles.put(2, " -fx-bar-fill: green; ");
		
		for(int i=0; i<yAttributes.size();i++ ) {
			int sStyle = i%seriesStyles.size();
			Set<Node> nodes = this.barChart.lookupAll(".series" + i);
			for (Node n : nodes) {
				String style = n.getStyle();
				n.setStyle(style + seriesStyles.get(sStyle));
				n.toFront();
			}

		}
		
		//This one paints the area between the axes
		Set<Node> RegionNodes = this.barChart.lookupAll("Region");
		for (Node n : RegionNodes) {
			String s = n.getStyle();
			n.setStyle(s + " -fx-background-color: white;");	
			n.toBack();
		}
		String _LABEL_SIZE = "15pt; ";	//NEVER BELOW 15pt
		Set<Node> LabelNodes = this.barChart.lookupAll("Label");
		for (Node n : LabelNodes) {
			String s = n.getStyle();
			n.setStyle(s + " -fx-font-size: " + _LABEL_SIZE);
		}
		Set<Node> LegendNodes = this.barChart.lookupAll("Legend");
		for (Node n : LegendNodes) {
			String s = n.getStyle();
			n.setStyle(s + " -fx-background-color: white;");
		}
		
		Set<Node> LineNodes = this.barChart.lookupAll("Line");
		for (Node n : LineNodes) {
			n.setOpacity(0.2);
			n.toBack();
		}
		
		//SOMEONE PLZ. TELL ME WHY THIS IS NOT WORKING IF MOVED UPWARDS IN THE CODE
		barStyle = this.barChart.getStyle();
		this.barChart.setStyle(barStyle + " -fx-background-color: white, white;");
		//System.out.println(this.barChart.getTypeSelector() +"\t" + this.barChart.getStyle());


		Scene scene  = new Scene(this.barChart, 1500, 500);
		stage.setScene(scene);

		//switch the flag to true if you want a report of the children of the barChart and their styles
		Boolean reportChildrenFlag = false;
		if (reportChildrenFlag)
			reportChildrenOfChart();
		
		//ATTN: does not work if you put somewhere else
		// TODO: fix legend colors
		HashMap<Integer, String> legendStyles = new HashMap<Integer, String>();
		legendStyles.put(0, " -fx-background-color: blue; ");
		legendStyles.put(1, " -fx-background-color: red; ");
		legendStyles.put(2, " -fx-background-color: green; ");
		
		for(int i=0; i<yAttributes.size();i++ ) {
			int lStyle = i%legendStyles.size();
			Set<Node> legendItems = this.barChart.lookupAll(".chart-legend-item.series"+i);
			for (Node n : legendItems) {
				String style = n.getStyle();
				n.setStyle(style + legendStyles.get(lStyle));
				//				String whoAmI = n.toString();
				//				System.out.println("@@@@@@@@@@@@@@@@@@ " + whoAmI);
			}	
		}
		
		//this.barChart.getStylesheets().addAll(this.getClass().getResource("resources/chart.css").toExternalForm());
		
		//Export to png
		saveAsPng(scene, outputPath);
				
		//INVERT COMMENT STATUS THIS OUT ONCE DONE!
		//stage.show();
		stage.close();
		
	}//end scatterCreation

	/**
	 * SUPER USEFUL, DO NOT REMOVE: This method reports all the children nodes of the barChart
	 */
	private void reportChildrenOfChart() {
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
	 * Equivalent and precursor to saveAsPng. Attn: it saves the barChart and NOT the scene.
	 */
	private void saveChart(){
		WritableImage image = this.barChart.snapshot(new SnapshotParameters(), null);
		File file = new File(outputPath);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e){}
	}//end saveChart


	private String xAttribute;
	private String yAttribute;
	private ArrayList<String> yAttributes;
	private Integer xAttributePos;
	private Integer yAttributePos;
	private ArrayList<Integer> yAttributePoss;
	private HashMap<String, Integer> attributePositions;
	private ArrayList<SchemaHeartbeatElement> inputTupleCollection;
	private String chartTitle;
	private String outputPath;
	private Stage stage;
	private BarChart<String,Number> barChart;


}//end class
