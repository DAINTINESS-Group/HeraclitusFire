package chartexport.exporters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
//import javafx.util.StringConverter;

import javax.imageio.ImageIO;

import chartexport.utils.DateStringConverter;
import chartexport.utils.IntegerStringConverter;
import javafx.scene.Node;
import javafx.scene.Scene;
//import javafx.application.Application;
//import javafx.stage.Stage;
//import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
//import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
//import javafx.scene.chart.XYChart.Series;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;


import datamodel.SchemaHeartbeatElement;

public class LineChartExporter {// extends Application{

	public LineChartExporter(String pOutputPath, String pTitle, HashMap<Integer, ArrayList<SchemaHeartbeatElement>> inputTupleCollection, 
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
		this.dateStringConverter = new DateStringConverter();  // to use dates for xAxis but w/ number axis
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
		// TODO: change NumberAxis to show integer values & maybe manual scaling
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();	
		xAxis.setAutoRanging(true);
		yAxis.setAutoRanging(true);

		xAxis.setLabel(xAttribute);
		String yAxisLabel = yAttributes.get(0);
		for (int i=1; i<yAttributes.size();i++) {
			yAxisLabel += " & " + yAttributes.get(i);
		}
		yAxis.setLabel(yAxisLabel);
		xAxis.setMinorTickVisible(false);
		yAxis.setMinorTickVisible(false);
		setLabelFormatter(xAxis, yAxis);

		this.lineChart = new LineChart<Number,Number>(xAxis,yAxis);
		//this.lineChart = new LineChart<String,Number>(xAxis,yAxis);
		
		for(XYChart.Series<Number,Number> nextSeries: allSeries)
			this.lineChart.getData().add(nextSeries);
		//for(XYChart.Series<String,Number> nextSeries: allSeries)
			//this.lineChart.getData().add(nextSeries);

		this.lineChart.setTitle(chartTitle);
		this.lineChart.setHorizontalGridLinesVisible(false);
		this.lineChart.setVerticalGridLinesVisible(false);
		
		xAxis.tickLabelFontProperty().set(Font.font(16));
		yAxis.tickLabelFontProperty().set(Font.font(16));
		
		//necessary, or axis tick values do not show in dump
		xAxis.setAnimated(false);
		yAxis.setAnimated(false);

		Scene scene  = new Scene(this.lineChart, 500, 500);
		stage.setScene(scene);

		//switch the flag to true if you want a report of the children of the lineChart and their styles
		Boolean reportChildrenFlag = false;
		if (reportChildrenFlag)
			reportChildrenOfChart();
		
		this.lineChart.setLegendVisible(false);
		
		File f = new File("resources/stylesheets/lineChartStyle.css");
		scene.getStylesheets().clear();
		this.lineChart.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
		
		//Export to png
		saveAsPng(scene, outputPath);
				
		//INVERT COMMENT STATUS THIS OUT ONCE DONE!
		//stage.show();
		stage.close();
		
	}//end lineCreation
	
	public ArrayList<XYChart.Series<Number,Number>> createSeries() {
		ArrayList<XYChart.Series<Number,Number>> allSeries = new ArrayList<XYChart.Series<Number,Number>>();
		
		for(int i=0; i<yAttributes.size();i++ ) {
			XYChart.Series<Number,Number> newSeries = new XYChart.Series<Number,Number>();
			newSeries.setName(yAttributes.get(i));
			boolean xAttrNotNum = inputTupleCollection.get(0).get(0).getIntValueByPosition(xAttributePos) == SchemaHeartbeatElement._ERROR_CODE
					? true : false;
			for (SchemaHeartbeatElement tuple: inputTupleCollection.get(0)) {
				if (!xAttrNotNum) {
					Integer xValue = tuple.getIntValueByPosition(xAttributePos);
					long xAxisValue = (long)tuple.getIntValueByPosition(0);
					//long xAxisValue = (long)tuple.getIntValueByPosition(0);
					Integer yValue = tuple.getIntValueByPosition(yAttributePoss.get(i));
					if((xValue != SchemaHeartbeatElement._ERROR_CODE) && (yValue != SchemaHeartbeatElement._ERROR_CODE)) {
						newSeries.getData().add(new XYChart.Data<Number,Number>(xAxisValue, yValue));
						this.dateStringConverter.addToMaps(xAxisValue, xValue.toString());
					}
					//System.out.println("x:" + xValue + "\ty:" + yValue);
				} else {
					String xValue = tuple.getStringValueByPosition(xAttributePos).substring(2, 7);
					long xAxisValue = (long)tuple.getIntValueByPosition(0);
					//long xAxisValue = Long.parseLong(tuple.getStringValueByPosition(1));
					Integer yValue = tuple.getIntValueByPosition(yAttributePoss.get(i));
					if(!(xValue.equals(SchemaHeartbeatElement._ERROR_STRING)) && (yValue != SchemaHeartbeatElement._ERROR_CODE)) {
						newSeries.getData().add(new XYChart.Data<Number,Number>(xAxisValue, yValue));
						this.dateStringConverter.addToMaps(xAxisValue, xValue);
					}
					//System.out.println("x:" + xValue + "\ty:" + yValue);
				}
			}
			allSeries.add(newSeries);
		}
		return allSeries;
	}
	
	private void setLabelFormatter(NumberAxis xAxis, NumberAxis yAxis) {
		//xAxis.setTickLabelFormatter(new IntegerStringConverter());
		xAxis.setTickLabelFormatter(this.dateStringConverter);
		yAxis.setTickLabelFormatter(new IntegerStringConverter());
	}

	/**
	 * SUPER USEFUL, DO NOT REMOVE: This method reports all the children nodes of the linechart
	 */
	private void reportChildrenOfChart() {
		System.out.println("\n\nStarting " + this.chartTitle);
		Set<Node> CHARTnodes = this.lineChart.lookupAll("*");
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
	 * Equivalent and precursor to saveAsPng. Attn: it saves the linechart and NOT the scene.
	 */
	private void saveChart(){
		WritableImage image = this.lineChart.snapshot(new SnapshotParameters(), null);
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
	private HashMap<Integer, ArrayList<SchemaHeartbeatElement>> inputTupleCollection;
	private String chartTitle;
	private String outputPath;
	private Stage stage;
	private LineChart<Number,Number> lineChart;
	private DateStringConverter dateStringConverter;


}//end class
