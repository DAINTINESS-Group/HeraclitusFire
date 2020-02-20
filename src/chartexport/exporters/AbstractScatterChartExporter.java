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

public abstract class AbstractScatterChartExporter<Y> {

	public AbstractScatterChartExporter(String pOutputPath, String pTitle, HashMap<Integer, ArrayList<IElement>> pTuplesPerLADCollection, 
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

	public abstract void start(Stage primaryStage) throws Exception;
	public abstract void createSeries();

	/**
	 * SUPER USEFUL, DO NOT REMOVE: This method reports all the children nodes of the scatterchart
	 */
	protected void reportChildrenOfChart() {
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
	 * Equivalent and precursor to saveAsPng. Attn: it saves the scatterchart and NOT the scene.
	 */
	protected void saveChart(){
		WritableImage image = this.scatterChart.snapshot(new SnapshotParameters(), null);
		File file = new File(outputPath);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e){}
	}//end saveChart
	
	public ArrayList<Integer> getNumOfDataPerSeries() {
		ArrayList<Integer> numOfDataPerSeries = new ArrayList<Integer>();
		for(XYChart.Series<Number,Y> series: allSeries)
			numOfDataPerSeries.add(series.getData().size());
		return numOfDataPerSeries;
	}


	protected String xAttribute;
	protected String yAttribute;
	protected Integer xAttributePos;
	protected Integer yAttributePos;
	protected HashMap<String, Integer> attributePositions;
	protected HashMap<Integer, ArrayList<IElement>> tuplesPerLADCollection;
	protected String chartTitle;
	protected String outputPath;
	protected Stage stage;
	protected ArrayList<XYChart.Series<Number,Y>> allSeries;
	protected ScatterChart<Number,Y> scatterChart;


}//end class
