package chartexport.exporters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.imageio.ImageIO;

import datamodel.SchemaHeartbeatElement;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

public abstract class AbstractLineChartExporter<X> {
	
	public AbstractLineChartExporter(String pOutputPath, String pTitle, HashMap<Integer, ArrayList<SchemaHeartbeatElement>> inputTupleCollection, 
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
	}
	
	public abstract void start(Stage primaryStage) throws Exception;
	public abstract void createSeries();

	/**
	 * SUPER USEFUL, DO NOT REMOVE: This method reports all the children nodes of the linechart
	 */
	protected void reportChildrenOfChart() {
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
	 * Equivalent and precursor to saveAsPng. Attn: it saves the linechart and NOT the scene.
	 */
	protected void saveChart(){
		WritableImage image = this.lineChart.snapshot(new SnapshotParameters(), null);
		File file = new File(outputPath);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e){}
	}//end saveChart
	
	public ArrayList<Integer> getNumOfDataPerSeries() {
		ArrayList<Integer> numOfDataPerSeries = new ArrayList<Integer>();
		for(XYChart.Series<X,Number> series: allSeries)
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
	protected HashMap<Integer, ArrayList<SchemaHeartbeatElement>> inputTupleCollection;
	protected String chartTitle;
	protected String outputPath;
	protected Stage stage;
	protected ArrayList<XYChart.Series<X,Number>> allSeries;
	protected LineChart<X,Number> lineChart;

}
