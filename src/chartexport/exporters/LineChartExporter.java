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

public class LineChartExporter extends AbstractLineChartExporter<Number> {// extends Application{

	public LineChartExporter(String pOutputPath, String pTitle, HashMap<Integer, ArrayList<SchemaHeartbeatElement>> inputTupleCollection, 
			String pXAttribute, ArrayList<String> pYAttributes, HashMap<String, Integer> pAttributePositions, Stage primaryStage) {
		super(pOutputPath, pTitle, inputTupleCollection, pXAttribute, pYAttributes, pAttributePositions, primaryStage);
	}//end constructor

	@Override
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
		//xAxis.setTickLabelFormatter(new IntegerStringConverter());
		yAxis.setTickLabelFormatter(new IntegerStringConverter());

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
	
	@Override
	public ArrayList<XYChart.Series<Number,Number>> createSeries() {
		ArrayList<XYChart.Series<Number,Number>> allSeries = new ArrayList<XYChart.Series<Number,Number>>();
		
		for(int i=0; i<yAttributes.size();i++ ) {
			XYChart.Series<Number,Number> newSeries = new XYChart.Series<Number,Number>();
			newSeries.setName(yAttributes.get(i));
			for (SchemaHeartbeatElement tuple: inputTupleCollection.get(0)) {
					Integer xValue = tuple.getIntValueByPosition(xAttributePos);
					Integer yValue = tuple.getIntValueByPosition(yAttributePoss.get(i));
					if((xValue != SchemaHeartbeatElement._ERROR_CODE) && (yValue != SchemaHeartbeatElement._ERROR_CODE)) {
						newSeries.getData().add(new XYChart.Data<Number,Number>(xValue, yValue));
					}
					//System.out.println("x:" + xValue + "\ty:" + yValue);
			}
			allSeries.add(newSeries);
		}
		return allSeries;
	}


}//end class
