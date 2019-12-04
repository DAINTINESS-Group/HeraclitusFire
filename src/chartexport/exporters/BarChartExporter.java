package chartexport.exporters;

//import java.io.File;
//import java.io.IOException;
import java.util.ArrayList;
//import java.util.Collections;
import java.util.HashMap;
//import java.util.Set;
//import javafx.util.StringConverter;
//import javax.imageio.ImageIO;

//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.application.Application;
//import javafx.stage.Stage;
//import javafx.scene.Scene;
//import javafx.scene.SnapshotParameters;
//import javafx.scene.chart.BarChart;
//import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
//import javafx.scene.image.WritableImage;
//import javafx.scene.text.Font;
import javafx.stage.Stage;
//import javafx.embed.swing.SwingFXUtils;



import datamodel.SchemaHeartbeatElement;

public class BarChartExporter extends AbstractBarChartExporter{// extends Application{

	public BarChartExporter(String pOutputPath, String pTitle, HashMap<Integer, ArrayList<SchemaHeartbeatElement>> inputTupleCollection, 
			String pXAttribute, ArrayList<String> pYAttributes, HashMap<String, Integer> pAttributePositions, Stage primaryStage) {
		super(pOutputPath, pTitle, inputTupleCollection, pXAttribute, pYAttributes, pAttributePositions, primaryStage);
	}//end constructor

	@Override
	public ArrayList<XYChart.Series<String,Number>> createSeries() {
		ArrayList<XYChart.Series<String,Number>> allSeries = new ArrayList<XYChart.Series<String,Number>>();
		
		for(int i=0; i<yAttributes.size();i++ ) {
			XYChart.Series<String,Number> newSeries = new XYChart.Series<String,Number>();
			newSeries.setName(yAttributes.get(i));
			for (SchemaHeartbeatElement tuple: inputTupleCollection.get(0)) {
				Integer xValue = tuple.getIntValueByPosition(xAttributePos);
				Integer yValue = tuple.getIntValueByPosition(yAttributePoss.get(i));
				if((xValue != SchemaHeartbeatElement._ERROR_CODE) && (yValue != SchemaHeartbeatElement._ERROR_CODE)) {
					if (i > 0) yValue = -yValue;
					newSeries.getData().add(new XYChart.Data<String,Number>(xValue.toString(), yValue));
				}
			}
			allSeries.add(newSeries);
		}
		return allSeries;
	}

	@Override
	protected void setLabelFormatter(NumberAxis yAxis) {
		yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
	        @Override
	        public String toString(Number value) {
	        	if (value.intValue() < 0)
	        		return String.format("%4d", -value.intValue());
	        	else
	        		return String.format("%4d", value.intValue());
	        }
	    });
	}


}//end class
