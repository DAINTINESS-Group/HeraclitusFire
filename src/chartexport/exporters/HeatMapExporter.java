package chartexport.exporters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
//import java.util.Set;

import javax.imageio.ImageIO;

//import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
//import javafx.geometry.Orientation;
//import javafx.scene.Node;
import javafx.scene.Scene;
//import javafx.scene.SnapshotParameters;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class HeatMapExporter {

    private final int HEATMAP_CELL_PIXEL_SIZE = 25;
    private final int HEATMAP_X_CELLS = 20 * HEATMAP_CELL_PIXEL_SIZE;
    private final int HEATMAP_Y_CELLS = 6 * HEATMAP_CELL_PIXEL_SIZE;
    private double MAX_RED;
    private double MAX_BLUE;
    
    protected String xAttribute;
	protected String yAttribute;
	protected Integer xAttributePos;
	protected Integer yAttributePos;
	protected HashMap<String, Integer> attributePositions;
	protected HashMap<Integer, Double[]> inputTupleCollection;
	protected String chartTitle;
	protected String outputPath;
	protected Stage stage;
	protected ArrayList<XYChart.Series<Number,String>> allSeries;
	protected Image heatMap;
    
    public HeatMapExporter(String pOutputPath, String pTitle, HashMap<Integer, Double[]> pInputHeatMapCollection, 
			String pXAttribute, String pYAttribute, HashMap<String, Integer> pAttributePositions, Stage primaryStage) {
		this.outputPath = pOutputPath;
		if (!outputPath.endsWith(".png"))
			outputPath = outputPath + ".png";

		if (pTitle != null)
			this.chartTitle = pTitle;
		else
			this.chartTitle = "";

		this.inputTupleCollection = pInputHeatMapCollection;
		this.attributePositions= pAttributePositions;
		this.xAttribute = pXAttribute;
		this.yAttribute = pYAttribute;
		//TODO CHECKS! IF any of the above is null, setup a flag as problem

		this.xAttributePos = this.attributePositions.get(this.xAttribute);
		this.yAttributePos = this.attributePositions.get(this.yAttribute);
		//TODO CHECKS! IF any of the above is null or negative, setup a flag as problem
		//or, before, can check if containsKey is true

		this.stage = primaryStage;
	}
    
    public void start(Stage primaryStage) {
    	produceHeatMap();
		if (this.allSeries.size() == 0)
			return;
    	Scene scene  = new Scene(new StackPane(new ImageView(heatMap)), 500, 500);
		stage.setScene(scene);
    	saveAsPng(scene, outputPath);
    	//saveAsPng(heatMap, outputPath);
        stage.close();
    }
    
    protected void produceHeatMap() {
		ArrayList<Integer> LADKeySet = new ArrayList<Integer>() ;
		LADKeySet.add(10);LADKeySet.add(11);LADKeySet.add(12);LADKeySet.add(20);LADKeySet.add(21);LADKeySet.add(22);
		Collections.sort(LADKeySet);
		
		calculateRedAndBlueMax(LADKeySet);
		
        WritableImage image = new WritableImage(HEATMAP_X_CELLS, HEATMAP_Y_CELLS);
        PixelWriter pixelWriter = image.getPixelWriter();
        
        for (int i=0; i<LADKeySet.size();i++ ) {
			Integer LADvalue = LADKeySet.get(i);
			Double[] tuples = this.inputTupleCollection.get(LADvalue);
        	for (int j = 0; j < tuples.length; j++) {
                Color color = getColorForValue(tuples[j], LADvalue);
            	int xStart = HEATMAP_CELL_PIXEL_SIZE * j;
            	int yStart = HEATMAP_CELL_PIXEL_SIZE * i;
            	for (int x = xStart; x < xStart + HEATMAP_CELL_PIXEL_SIZE; x++) {
                    for (int y = yStart; y < yStart + HEATMAP_CELL_PIXEL_SIZE; y++) {
                        pixelWriter.setColor(x, y, color);
                    }
                }
            }
        }
        this.heatMap = image;
        createSeries();
        //return this.allSeries;
    }

    public void createSeries() {
    	this.allSeries = new ArrayList<XYChart.Series<Number,String>>();
		
		HashMap<Integer, String> ladLabels = new HashMap<Integer, String>();
		ladLabels.put(10, "Rigid.Dead");
		ladLabels.put(11, "Quiet.Dead");
		ladLabels.put(12, "Activ.Dead");
		ladLabels.put(20, "Rigid.Surv");
		ladLabels.put(21, "Quiet.Surv");
		ladLabels.put(22, "Activ.Surv");
		
		ArrayList<Integer> LADKeySet = new ArrayList<Integer>() ;
		LADKeySet.add(10);LADKeySet.add(11);LADKeySet.add(12);LADKeySet.add(20);LADKeySet.add(21);LADKeySet.add(22);
		Collections.sort(LADKeySet);
        
        for (int i=0; i<LADKeySet.size();i++ ) {
			Integer LADvalue = LADKeySet.get(i);
			XYChart.Series<Number,String> newSeries = new XYChart.Series<Number,String>();
			newSeries.setName(ladLabels.get(LADvalue));
			Double[] tuples = this.inputTupleCollection.get(LADvalue);
        	for (int j = 0; j < tuples.length; j++) {
            	newSeries.getData().add(new XYChart.Data<Number,String>(tuples[j], LADvalue.toString()));
            }
			if (newSeries.getData().size() == 0)
				newSeries.setName("");
			this.allSeries.add(newSeries);
        }
        //return this.allSeries;
    }
    
    private void calculateRedAndBlueMax(ArrayList<Integer> LADKeySet) {
    	// find max percentage of dead table ranges
    	Double tmpMax = Collections.max(Arrays.asList(this.inputTupleCollection.get(LADKeySet.get(0))));
    	this.MAX_RED = tmpMax;
    	tmpMax = Collections.max(Arrays.asList(this.inputTupleCollection.get(LADKeySet.get(1))));
    	this.MAX_RED = tmpMax > this.MAX_RED ? tmpMax : this.MAX_RED;
    	tmpMax = Collections.max(Arrays.asList(this.inputTupleCollection.get(LADKeySet.get(2))));
    	this.MAX_RED = tmpMax > this.MAX_RED ? tmpMax : this.MAX_RED;
        
        // find max percentage of survivor table ranges
    	tmpMax = Collections.max(Arrays.asList(this.inputTupleCollection.get(LADKeySet.get(3))));
    	this.MAX_BLUE = tmpMax;
    	tmpMax = Collections.max(Arrays.asList(this.inputTupleCollection.get(LADKeySet.get(4))));
    	this.MAX_BLUE = tmpMax > this.MAX_BLUE ? tmpMax : this.MAX_BLUE;
    	tmpMax = Collections.max(Arrays.asList(this.inputTupleCollection.get(LADKeySet.get(5))));
    	this.MAX_BLUE = tmpMax > this.MAX_BLUE ? tmpMax : this.MAX_BLUE;
    }
    
    private Color getColorForValue(double value, int LADvalue) {
        if (LADvalue / 10 == 2)
        	return Color.WHITE.interpolate(Color.BLUE, value / MAX_BLUE);
        else
        	return Color.WHITE.interpolate(Color.RED, value / MAX_RED);
    }
	
    protected void saveAsPng(Image image, String path) {
		File file = new File(path);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e) {
			e.printStackTrace();
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
	 * Equivalent and precursor to saveAsPng. Attn: it saves the heat map and NOT the scene.
	 */
	protected void saveHeatMap(Image image, String path){
		File file = new File(path);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}//end saveChart
	
	public ArrayList<Integer> getNumOfDataPerSeries() {
		ArrayList<Integer> numOfDataPerSeries = new ArrayList<Integer>();
		for(XYChart.Series<Number,String> series: this.allSeries)
			numOfDataPerSeries.add(series.getData().size());
		return numOfDataPerSeries;
	}
	
}
