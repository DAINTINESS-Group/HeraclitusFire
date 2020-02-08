package mainEngine;

import javafx.stage.Stage;

public class MainEngineFactory {

	public IMainEngine createMainEngine(String engineType, String anInputProjectFolder, Stage primaryStage) {
		switch(engineType) {
		case "schema": return new SchemaStatsMainEngine(anInputProjectFolder, primaryStage);
		case "table": return new TableStatsMainEngine(anInputProjectFolder, primaryStage);
		default: return null; 
		}
	}

}
