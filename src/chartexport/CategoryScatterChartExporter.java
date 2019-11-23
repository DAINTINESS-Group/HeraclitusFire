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
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;



import datamodel.TableDetailedStatsElement;

public class CategoryScatterChartExporter{// extends Application{

	public CategoryScatterChartExporter(String pOutputPath, String pTitle, HashMap<Integer, ArrayList<TableDetailedStatsElement>> pTuplesPerLADCollection, 
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

	//@Override
	public void start(Stage primaryStage) throws Exception {


		
		HashMap<Integer, String> ladLabels = new HashMap<Integer, String>();
		ladLabels.put(10, "Rigid.Dead");
		ladLabels.put(11, "Quiet.Dead");
		ladLabels.put(12, "Activ.Dead");
		ladLabels.put(20, "Rigid.Surv");
		ladLabels.put(21, "Quiet.Surv");
		ladLabels.put(22, "Activ.Surv");
		
		double maxX = Integer.MIN_VALUE; double maxY = Integer.MIN_VALUE;
		double minX = Integer.MAX_VALUE; double minY = Integer.MAX_VALUE;

		ArrayList<Integer> LADKeySet = new ArrayList<Integer>();
		for(Integer k: tuplesPerLADCollection.keySet())
			LADKeySet.add(k);
		if(LADKeySet.size() < 1)
			return;
		Collections.sort(LADKeySet);			//very important!!! now we know that data series are sorted by their LAD asc
												//therefore 0th is LAD = 10, 1st = LAD = 11, ...
		ArrayList<XYChart.Series<Number,Number>> allSeries = new ArrayList<XYChart.Series<Number,Number>>(); 
		
		for(int i=0; i<LADKeySet.size();i++ ) {
			Integer LADvalue = LADKeySet.get(i);
			XYChart.Series<Number,Number> newSeries = new XYChart.Series<Number,Number>();
			//newSeries.setName(String.valueOf(LADvalue));
			newSeries.setName(ladLabels.get(LADvalue));
			ArrayList<TableDetailedStatsElement> tuples = tuplesPerLADCollection.get(LADvalue);
			for (TableDetailedStatsElement tuple: tuples) {
				Integer xValue = tuple.getIntValueByPosition(xAttributePos);
				Integer yValue = tuple.getIntValueByPosition(yAttributePos);
				if (xValue > maxX) maxX = xValue; if (xValue < minX) minX = xValue;
				if (yValue > maxY) maxY = yValue; if (yValue < minY) minY = yValue;
				if((xValue != TableDetailedStatsElement._ERROR_CODE) && (yValue != TableDetailedStatsElement._ERROR_CODE))
					newSeries.getData().add(new XYChart.Data<Number,Number>(xValue, yValue));
			}

			allSeries.add(newSeries);
		}//end for i = nextLAD value

		double xTickUnit = Math.round((double) (maxX - minX)/10.0);
		double yTickUnit = (double) (maxY - minY)/20.0;
		final NumberAxis xAxis = new NumberAxis(xAttribute, minX - 1, maxX + xTickUnit, xTickUnit);
		final NumberAxis yAxis = new NumberAxis(yAttribute, minY - 1, maxY + yTickUnit, yTickUnit);

		//EITHER the above, or the below
		double xRange = 0; double yRange = 0;
		//final NumberAxis xAxis = new NumberAxis();
		//final NumberAxis yAxis = new NumberAxis();	
		xRange = xAxis.getUpperBound() - xAxis.getLowerBound();yRange = yAxis.getUpperBound() - yAxis.getLowerBound();
		System.out.println("(change) xAxis range: " + xRange + "\tyAxis range: " + yRange);
		xAxis.setAutoRanging(false);
		yAxis.setAutoRanging(false);
//		xAxis.setLowerBound(xAxis.getLowerBound() - 20);
//		yAxis.setLowerBound(yAxis.getLowerBound() - 20);

		xAxis.setLabel(xAttribute);                
		yAxis.setLabel(yAttribute);
		xAxis.setMinorTickVisible(false);
		yAxis.setMinorTickVisible(false);
		xAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxis) {
	        @Override
	        public String toString(Number value) { 
	    		if(value.intValue() < 0) 
	    			return ""; 
	    		return ""+(value.intValue()); 
	    	} 
	    });
		yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
	        @Override
	        public String toString(Number value) { 
	    		if(value.intValue() < 0) 
	    			return ""; 
	    		return ""+(value.intValue()); 
	    	} 
	    });
//		xAxis.setLowerBound(xAxis.getLowerBound() - 20);
//		yAxis.setLowerBound(yAxis.getLowerBound() - 20);
		

		this.scatterChart = new ScatterChart<Number,Number>(xAxis,yAxis);

		for(XYChart.Series<Number,Number> nextSeries: allSeries)
			this.scatterChart.getData().add(nextSeries);
		xRange = xAxis.getUpperBound() - xAxis.getLowerBound();yRange = yAxis.getUpperBound() - yAxis.getLowerBound();
		System.out.println("(change) xAxis range: " + xRange + "\tyAxis range: " + yRange);

		this.scatterChart.setTitle(chartTitle);
		this.scatterChart.setHorizontalGridLinesVisible(false);
		this.scatterChart.setVerticalGridLinesVisible(false);
		xAxis.setLowerBound(xAxis.getLowerBound() - 1);
		yAxis.setLowerBound(yAxis.getLowerBound() - 1);
		 		
		
		String _FONT_SIZE = "16pt; ";	//NEVER BELOW 16pt
		String scatterStyle = this.scatterChart.getStyle();
		this.scatterChart.setStyle(scatterStyle + " -fx-font-size: " + _FONT_SIZE);
		xAxis.setStyle(" -fx-font-size: " + _FONT_SIZE);
		yAxis.setStyle(" -fx-font-size: " + _FONT_SIZE);
		xAxis.tickLabelFontProperty().set(Font.font(16));
		yAxis.tickLabelFontProperty().set(Font.font(16));
		
		//necessary, or axis tick values do not show in dump
		xAxis.setAnimated(false);
		yAxis.setAnimated(false);
//		xAxis.setLowerBound(xAxis.getLowerBound() - 20);
//		yAxis.setLowerBound(yAxis.getLowerBound() - 20);
		xRange = xAxis.getUpperBound() - xAxis.getLowerBound();yRange = yAxis.getUpperBound() - yAxis.getLowerBound();
		System.out.println("(change) xAxis range: " + xRange + "\tyAxis range: " + yRange);
		
		
//		Set<Node> axisNode = sc.lookupAll(".axis");
//      for(final Node axis : axisNode){
//          axis.setStyle("-fx-font-size: 12;");
//          //axis.setStyle("-fx-minor-tick-visible:false;");
//          //axis.setStyle("-fx-font-color: red;");
//      }
		

/*		//OTHER STYLING EXPERIMENTS		
		xAxis.tickLabelFillProperty().set(Paint.valueOf("#0000ff"));
		xAxis.setTickLabelsVisible(true);

	    xAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxis) {
	        @Override
	        public String toString(Number number) {
	            return String.format("%2.0f", number);
	        }
	    });
		xAxis.setTickLabelFill(Paint.valueOf("#0000ff"));
 */

		HashMap<Integer, String> seriesStyles = new HashMap<Integer, String>();
		seriesStyles.put(10, " -fx-shape: 'M5,0 L10,8 L0,8 Z';  -fx-background-color: red, orange; ");
		seriesStyles.put(11, " -fx-shape: 'M5,0 L10,9 L5,18 L0,9 Z'; -fx-background-color: red, orange; ");
		seriesStyles.put(12, "-fx-shape: 'M 0 0 V 40 H 40 V 0 z'; -fx-background-color: red, orange; ");
		seriesStyles.put(20, " -fx-shape: 'M5,0 L10,8 L0,8 Z';  -fx-background-color: green, lightgreen; ");
		seriesStyles.put(21, "-fx-shape: 'M5,0 L10,9 L5,18 L0,9 Z';   -fx-background-color: green, lightgreen; ");
		seriesStyles.put(22, "-fx-shape: 'M 0 0 V 40 H 40 V 0 z'; -fx-background-color: green, lightgreen; ");
		
		for(int i=0; i<LADKeySet.size();i++ ) {
			int LADValue = LADKeySet.get(i);
			String LADstyle = seriesStyles.get(LADValue);
			Set<Node> nodes = this.scatterChart.lookupAll(".series" + i);
			for (Node n : nodes) {
				n.setOpacity(.4);
				String style = n.getStyle();
				n.setStyle(style + LADstyle
//						+ " -fx-opactity: 0.3;"
			        + "    -fx-background-insets: 0, 2;\n"
			        + "    -fx-background-radius: 5px;\n"
			        + "    -fx-padding: 5px;"
					);
				n.toFront();
			}

		}
		
		//This one paints the area between the axes
		Set<Node> RegionNodes = this.scatterChart.lookupAll("Region");
		for (Node n : RegionNodes) {
			String s = n.getStyle();
			n.setStyle(s + " -fx-background-color: white;");	
			n.toBack();
		}
		String _LABEL_SIZE = "15pt; ";	//NEVER BELOW 15pt
		Set<Node> LabelNodes = this.scatterChart.lookupAll("Label");
		for (Node n : LabelNodes) {
			String s = n.getStyle();
			n.setStyle(s + " -fx-font-size: " + _LABEL_SIZE);			

		}
		Set<Node> LegendNodes = this.scatterChart.lookupAll("Legend");
		for (Node n : LegendNodes) {
			String s = n.getStyle();
			n.setStyle(s + " -fx-background-color: white;");
		}

//		//FAILED TO SOLVE HOW POINTS GO _UNDER_ THE LINES OF THE AXES
		//CLEARLY IT IS STH ELSE THAT IS VISUALIZED THERE, ALTHOUGH OPACITY WORKS
		//ALSO, MAYBE IT IS AN ISSUE OF HALF-THE-POINT BEING OUTSIDE THE PLOTTING AREA
		Set<Node> LineNodes = this.scatterChart.lookupAll("Line");
		for (Node n : LineNodes) {
			n.setOpacity(0.2);
			n.toBack();
		}	
//		Set<Node> symNodes = this.scatterChart.lookupAll("StackPane.chart-symbol");
//		for (Node n : symNodes) {
//			n.toFront();
//		}	
			
		//SOMEONE PLZ. TELL ME WHY THIS IS NOT WORKING IF MOVED UPWARDS IN THE CODE
		scatterStyle = this.scatterChart.getStyle();
		this.scatterChart.setStyle(scatterStyle + " -fx-background-color: white, white;");
		//System.out.println(this.scatterChart.getTypeSelector() +"\t" + this.scatterChart.getStyle());


		Scene scene  = new Scene(this.scatterChart, 500, 500);
		stage.setScene(scene);

		//switch the flag to true if you want a report of the children of the scatterChartand their styles
		Boolean reportChildrenFlag = false;
		if (reportChildrenFlag)
			reportChildrenOfChart();

		
		//ATTN: does not work if you put somewhere else
		for(int i=0; i<LADKeySet.size();i++ ) {
			int LADValue = LADKeySet.get(i);
			String LADstyle = seriesStyles.get(LADValue);
			Set<Node> legendItems = this.scatterChart.lookupAll(".chart-legend-item-symbol.series"+i);
			for (Node n : legendItems) {
				String style = n.getStyle();
				n.setStyle(style + LADstyle
						+ "    -fx-background-insets: 0, 2;\n"
						+ "    -fx-background-radius: 5px;\n"
						+ "    -fx-padding: 5px;"
						);
				//				String whoAmI = n.toString();
				//				System.out.println("@@@@@@@@@@@@@@@@@@ " + whoAmI);
			}	
		}
		
		//Export to png
		saveAsPng(scene, outputPath);
				
		//INVERT COMMENT STATUS THIS OUT ONCE DONE!
		//stage.show();
		stage.close();
		
	}//end scatterCreation

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



/* https://stackoverflow.com/questions/20983131/remove-javafx-2-linechart-legend-items
private static Node findNode(final Parent aParent, final String aClassname, final String aStyleName) {

if (null != aParent) {
    final ObservableList<Node> children = aParent.getChildrenUnmodifiable();
    if (null != children) {
        for (final Node child : children) {

            String className = child.getClass().getName();

            if (className.contains("$")) {
                className = className.substring(0, className.indexOf("$"));
            }

            if (0 == aClassname.compareToIgnoreCase(className)) {
                if ((null == aStyleName) || (0 == aStyleName.length())) {
                    // No aStyleName, just return this:
                    return child;
                }
                else {
                    final String styleName = child.getStyleClass().toString();
                    if (0 == aStyleName.compareToIgnoreCase(styleName)) {
                        return child;
                    }
                }
            }

            if (child instanceof Parent) {
                final Node node = findNode((Parent) child, aClassname, aStyleName);

                if (null != node) {
                    return node;
                }
            }
        }
    }
}

return null;
}

//Calling it with the chart in question to retrieve the Legend node:

Legend legend = (Legend) findNode(chart, Legend.class.getName(), "chart-legend");
//Which you can then iterate through the children and remove the ones you don't want to be displayed:

for (final Node legendItem : legend.getChildren()) {

    final Label legendLabel = (Label) legendItem;

    if (0 == legendLabel.getText().compareToIgnoreCase("the name of the legend I want hidden (or replaced with some other test)")) {
        legend.getChildren().remove(legendItem);
        break;
    }
}
*/