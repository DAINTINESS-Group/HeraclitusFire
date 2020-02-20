package chartexport.exporters;

//import java.io.File;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.Comparator;
import java.util.HashMap;
//import java.util.Set;

import datamodel.IElement;

//import javax.imageio.ImageIO;

//import chartexport.utils.IntegerStringConverter;
import datamodel.TableDetailedStatsElement;
//import javafx.embed.swing.SwingFXUtils;
//import javafx.scene.Node;
import javafx.scene.Scene;
//import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
//import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PercentageLineChartExporter extends AbstractLineChartExporter<Number> {// extends Application{

	public PercentageLineChartExporter(String pOutputPath, String pTitle, HashMap<Integer, ArrayList<IElement>> pInputTupleCollection, 
			String pXAttribute, ArrayList<String> pYAttributes, HashMap<String, Integer> pAttributePositions, Stage primaryStage) {
		super(pOutputPath, pTitle, pInputTupleCollection, pXAttribute, pYAttributes, pAttributePositions, primaryStage);
	}//end constructor

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		createSeries();
		if (this.allSeries.size() == 0)
			return;

		final NumberAxis xAxis = new NumberAxis(0.0, 1.0, 0.2);
		final NumberAxis yAxis = new NumberAxis(0.0, 1.0, 0.2);

		//EITHER the above, or the below
//		final NumberAxis xAxis = new NumberAxis();
//		final NumberAxis yAxis = new NumberAxis();
//		xAxis.setAutoRanging(true);
//		yAxis.setAutoRanging(true);

		xAxis.setLabel(xAttribute);
		String yAxisLabel = "Pct" + yAttributes.get(0);
		for (int i=1; i<yAttributes.size();i++) {
			yAxisLabel += " & Pct" + yAttributes.get(i);
		}
		yAxis.setLabel(yAxisLabel);
		xAxis.setMinorTickVisible(false);
		yAxis.setMinorTickVisible(false);
		//xAxis.setTickLabelFormatter(new IntegerStringConverter());
		//yAxis.setTickLabelFormatter(new IntegerStringConverter());

		this.lineChart = new LineChart<Number,Number>(xAxis,yAxis);
		//this.lineChart = new LineChart<String,Number>(xAxis,yAxis);
		
		for(XYChart.Series<Number,Number> nextSeries: this.allSeries)
			this.lineChart.getData().add(nextSeries);
		//for(XYChart.Series<String,Number> nextSeries: allSeries)
			//this.lineChart.getData().add(nextSeries);

		this.lineChart.setTitle(chartTitle);
		//this.lineChart.setHorizontalGridLinesVisible(false);
		//this.lineChart.setVerticalGridLinesVisible(false);
		
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
		
		// TODO: find how to add resources to jar and load css files from there
		//File f = new File("resources/stylesheets/lineChartStyle.css");
		//scene.getStylesheets().clear();
		//this.lineChart.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
		this.lineChart.getStylesheets().add(getClass().getResource("stylesheets/lineChartStyle.css").toExternalForm());
		
		//Export to png
		saveAsPng(scene, outputPath);
				
		//INVERT COMMENT STATUS THIS OUT ONCE DONE!
		//stage.show();
		stage.close();
		
	}//end lineCreation
	
	@Override
	public void createSeries() {
		this.allSeries = new ArrayList<XYChart.Series<Number,Number>>();
		
		ArrayList<Integer> tablesCollection = new ArrayList<Integer>();
		
		for(int y=0; y < yAttributes.size(); y++ ) {
			int yAttrPos = yAttributePoss.get(y);
			for (IElement t: (ArrayList<IElement>)inputTupleCollection.get(0)) {
				tablesCollection.add(t.getIntValueByPosition(yAttrPos));
			}
			Collections.sort(tablesCollection, Collections.reverseOrder());
			
			ArrayList<Double[]> updOverTables = new ArrayList<Double[]>();
			int totalCount = tablesCollection.size();
			int tableCount = 0;
			int prevSumUpd = 0;
			for (Integer i: tablesCollection) {
				tableCount ++;
				prevSumUpd = prevSumUpd + i;
				Double[] row = {(double)tableCount,(double)prevSumUpd,(double)tableCount/totalCount, 0.0};
				updOverTables.add(row);
			}
			
			double totalUpds = updOverTables.get(updOverTables.size()-1)[1];
			for(int i = 0; i < updOverTables.size(); i++) {
				updOverTables.get(i)[3] = updOverTables.get(i)[1]/totalUpds;
			}
			
			XYChart.Series<Number,Number> newSeries = new XYChart.Series<Number,Number>();
			newSeries.setName("Pct" + yAttributes.get(y));
			for (Double[] r: updOverTables) {
				newSeries.getData().add(new XYChart.Data<Number,Number>(r[2], r[3]));
			}
			this.allSeries.add(newSeries);
			tablesCollection.clear();
		}
		//return this.allSeries;
	}

}//end class
