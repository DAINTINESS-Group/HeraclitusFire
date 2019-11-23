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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;



import datamodel.SchemaHeartbeatElement;

public class LineChartExporter{// extends Application{

	public LineChartExporter(String pOutputPath, String pTitle, ArrayList<SchemaHeartbeatElement> inputTupleCollection, 
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
		
		DateStringConverter dateStringConverter = new DateStringConverter();  // to use dates for xAxis but w/ number axis
		
		ArrayList<XYChart.Series<Number,Number>> allSeries = new ArrayList<XYChart.Series<Number,Number>>();
		
		for(int i=0; i<yAttributes.size();i++ ) {
			XYChart.Series<Number,Number> newSeries = new XYChart.Series<Number,Number>();
			newSeries.setName(yAttributes.get(i));
			boolean xAttrNotNum = inputTupleCollection.get(0).getIntValueByPosition(xAttributePos) == SchemaHeartbeatElement._ERROR_CODE
					? true : false;
			for (SchemaHeartbeatElement tuple: inputTupleCollection) {
				if (!xAttrNotNum) {
					Integer xValue = tuple.getIntValueByPosition(xAttributePos);
					long xAxisValue = (long)tuple.getIntValueByPosition(0);
					//long xAxisValue = (long)tuple.getIntValueByPosition(0);
					Integer yValue = tuple.getIntValueByPosition(yAttributePoss.get(i));
					if((xValue != SchemaHeartbeatElement._ERROR_CODE) && (yValue != SchemaHeartbeatElement._ERROR_CODE)) {
						newSeries.getData().add(new XYChart.Data<Number,Number>(xAxisValue, yValue));
						dateStringConverter.addToMaps(xAxisValue, xValue.toString());
					}
					//System.out.println("x:" + xValue + "\ty:" + yValue);
				} else {
					String xValue = tuple.getStringValueByPosition(xAttributePos).substring(2, 7);
					long xAxisValue = (long)tuple.getIntValueByPosition(0);
					//long xAxisValue = Long.parseLong(tuple.getStringValueByPosition(1));
					Integer yValue = tuple.getIntValueByPosition(yAttributePoss.get(i));
					if(!(xValue.equals(SchemaHeartbeatElement._ERROR_STRING)) && (yValue != SchemaHeartbeatElement._ERROR_CODE)) {
						newSeries.getData().add(new XYChart.Data<Number,Number>(xAxisValue, yValue));
						dateStringConverter.addToMaps(xAxisValue, xValue);
					}
					//System.out.println("x:" + xValue + "\ty:" + yValue);
				}
			}
			allSeries.add(newSeries);
		}

		//double xTickUnit = Math.round((double) (maxX - minX)/10.0);
		//double yTickUnit = (double) (maxY - minY)/10.0;
		//final NumberAxis xAxis = new NumberAxis(xAttribute, minX - xTickUnit, maxX + xTickUnit, xTickUnit);
		//final NumberAxis yAxis = new NumberAxis(yAttribute, minY - yTickUnit, maxY + yTickUnit, yTickUnit);

		//EITHER the above, or the below
		// TODO: change NumberAxis to show integer values & maybe manual scaling
		final NumberAxis xAxis = new NumberAxis();
		//final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();		
		xAxis.setAutoRanging(true);
		yAxis.setAutoRanging(true);

		xAxis.setLabel(xAttribute);
		//LocalDateTime dt = new LocalDateTime();
		//xAxis.setTickLabelFormatter(arg0);;;
		String yAxisLabel = yAttributes.get(0);
		for (int i=1; i<yAttributes.size();i++) {
			yAxisLabel += " & " + yAttributes.get(i);
		}
		yAxis.setLabel(yAxisLabel);
		xAxis.setMinorTickVisible(false);
		yAxis.setMinorTickVisible(false);
		//xAxis.setTickLabelFormatter(new IntegerStringConverter());
		xAxis.setTickLabelFormatter(dateStringConverter);
		yAxis.setTickLabelFormatter(new IntegerStringConverter());
		/*
		xAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxis) {
	        @Override
	        public String toString(Number value) {
	        	return String.format("%4d", value.intValue());
	        }
	    });
		yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
	        @Override
	        public String toString(Number value) {
	        	return String.format("%4d", value.intValue());
	        }
	    });
	    */

		this.lineChart = new LineChart<Number,Number>(xAxis,yAxis);
		//this.lineChart = new LineChart<String,Number>(xAxis,yAxis);
		
		for(XYChart.Series<Number,Number> nextSeries: allSeries)
			this.lineChart.getData().add(nextSeries);
		//for(XYChart.Series<String,Number> nextSeries: allSeries)
			//this.lineChart.getData().add(nextSeries);

		this.lineChart.setTitle(chartTitle);
		this.lineChart.setHorizontalGridLinesVisible(false);
		this.lineChart.setVerticalGridLinesVisible(false);
		
		String _FONT_SIZE = "16pt; ";	//NEVER BELOW 16pt
		String lineStyle = this.lineChart.getStyle();
		this.lineChart.setStyle(lineStyle + " -fx-font-size: " + _FONT_SIZE);
		xAxis.setStyle(" -fx-font-size: " + _FONT_SIZE);
		yAxis.setStyle(" -fx-font-size: " + _FONT_SIZE);
		xAxis.tickLabelFontProperty().set(Font.font(16));
		yAxis.tickLabelFontProperty().set(Font.font(16));
		
		//necessary, or axis tick values do not show in dump
		xAxis.setAnimated(false);
		yAxis.setAnimated(false);
		
		HashMap<Integer, String> seriesStyles = new HashMap<Integer, String>();
		seriesStyles.put(0, " -fx-stroke: blue; ");
		seriesStyles.put(1, " -fx-stroke: red; ");
		seriesStyles.put(2, " -fx-stroke: green; ");
		
		for(int i=0; i<yAttributes.size();i++ ) {
			int sStyle = i%seriesStyles.size();
			Set<Node> nodes = this.lineChart.lookupAll(".series" + i);
			for (Node n : nodes) {
				String style = n.getStyle();
				n.setStyle(style + seriesStyles.get(sStyle) 
						+ "-fx-stroke-width: 2px;" 
						+ "-fx-background-color: transparent, transparent;");
				n.toFront();
			}

		}
		
		//This one paints the area between the axes
		Set<Node> RegionNodes = this.lineChart.lookupAll("Region");
		for (Node n : RegionNodes) {
			String s = n.getStyle();
			n.setStyle(s + " -fx-background-color: white;");	
			n.toBack();
		}
		String _LABEL_SIZE = "15pt; ";	//NEVER BELOW 15pt
		Set<Node> LabelNodes = this.lineChart.lookupAll("Label");
		for (Node n : LabelNodes) {
			String s = n.getStyle();
			n.setStyle(s + " -fx-font-size: " + _LABEL_SIZE);
		}
		Set<Node> LegendNodes = this.lineChart.lookupAll("Legend");
		for (Node n : LegendNodes) {
			String s = n.getStyle();
			n.setStyle(s + " -fx-background-color: white;");
		}
		
		Set<Node> LineNodes = this.lineChart.lookupAll("Line");
		for (Node n : LineNodes) {
			n.setOpacity(0.2);
			n.toBack();
		}
		
		//SOMEONE PLZ. TELL ME WHY THIS IS NOT WORKING IF MOVED UPWARDS IN THE CODE
		lineStyle = this.lineChart.getStyle();
		this.lineChart.setStyle(lineStyle + " -fx-background-color: white, white;");
		//System.out.println(this.lineChart.getTypeSelector() +"\t" + this.lineChart.getStyle());


		Scene scene  = new Scene(this.lineChart, 500, 500);
		stage.setScene(scene);

		//switch the flag to true if you want a report of the children of the lineChart and their styles
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
			Set<Node> legendItems = this.lineChart.lookupAll(".chart-legend-item-symbol.series"+i);
			for (Node n : legendItems) {
				String style = n.getStyle();
				n.setStyle(style + legendStyles.get(lStyle));
				//				String whoAmI = n.toString();
				//				System.out.println("@@@@@@@@@@@@@@@@@@ " + whoAmI);
			}	
		}
		this.lineChart.setLegendVisible(false);
		
		/*
		// probably not needed since legend won't be visible
		Set<Node> legendItems = this.lineChart.lookupAll(".chart-legend-item-symbol.series0");
		for (Node n : legendItems) {
			String style = n.getStyle();
			n.setStyle(style 
					+ "    -fx-background-insets: 0, 2;\n"
					+ "    -fx-background-radius: 5px;\n"
					+ "    -fx-padding: 5px;"
					);
		}
		this.lineChart.setLegendVisible(false);
		//*/
		
		//Export to png
		saveAsPng(scene, outputPath);
				
		//INVERT COMMENT STATUS THIS OUT ONCE DONE!
		//stage.show();
		stage.close();
		
	}//end scatterCreation

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
	private ArrayList<SchemaHeartbeatElement> inputTupleCollection;
	private String chartTitle;
	private String outputPath;
	private Stage stage;
	private LineChart<Number,Number> lineChart;
	//private LineChart<String,Number> lineChart;


}//end class
