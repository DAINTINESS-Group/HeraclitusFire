package chartexport.exporters;

//import java.io.File;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

public class GroupedBarChartExporter extends AbstractBarChartExporter{// extends Application{

	public GroupedBarChartExporter(String pOutputPath, String pTitle, HashMap<Integer, ArrayList<SchemaHeartbeatElement>> inputTupleCollection, 
			String pXAttribute, ArrayList<String> pYAttributes, HashMap<String, Integer> pAttributePositions, Stage primaryStage) {
		super(pOutputPath, pTitle, inputTupleCollection, pXAttribute, pYAttributes, pAttributePositions, primaryStage);
	}//end constructor

	@Override
	public void createSeries() {
		this.allSeries = new ArrayList<XYChart.Series<String,Number>>();
		
		ArrayList<Integer> RYFV0KeySet = new ArrayList<Integer>();
		for(Integer k: inputTupleCollection.keySet())
			RYFV0KeySet.add(k);
		if(RYFV0KeySet.size() < 1)
			return;
			//return this.allSeries;
		Collections.sort(RYFV0KeySet);
		
		for(int i=0; i<yAttributes.size();i++ ) {
			XYChart.Series<String,Number> newSeries = new XYChart.Series<String,Number>();
			newSeries.setName(yAttributes.get(i));
			for(Integer RYFV0value: RYFV0KeySet) {
				ArrayList<SchemaHeartbeatElement> tuples = inputTupleCollection.get(RYFV0value);
				Integer yValue = 0;
				for (SchemaHeartbeatElement tuple: tuples) {
					Integer yTupleValue = tuple.getIntValueByPosition(yAttributePoss.get(i));
					if(yTupleValue != SchemaHeartbeatElement._ERROR_CODE)
						yValue += yTupleValue;
				}
				//yValue /= tuples.size();  // if we want avg instead of sum
				newSeries.getData().add(new XYChart.Data<String,Number>(RYFV0value.toString(), yValue));
			}
			this.allSeries.add(newSeries);
		}
		//return this.allSeries;
	}

	@Override
	protected void setLabelFormatter(NumberAxis yAxis) {}


}//end class
