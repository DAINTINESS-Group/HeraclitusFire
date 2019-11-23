/**
 * 
 */
package client;

//import java.io.File;

import mainEngine.TableStatsMainEngine;
import mainEngine.SchemaStatsMainEngine;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * A simple client to process a folder containing the results of Hecate and produce pattern figures.
 * 
 * @author pvassil, alexvou
 * @since 2019-06-10
 * @version v.0.4
 */
public class SingleFolderProcessingClient extends Application{

	/**
	 * A simple client to process a folder.
	 * Assume you have the project <code>PRJ</code> its should contain a folder <code>PRJ/results</code> with the output of Hecate.
	 * You should pass the <code>PRJ</code> as the parameter needed at args[0] (without a final "/" at the end)
	 *
	 * @param args the first parameter given should be the path of the results of Hecate. 
	 * 
	 */
	public static void main(String[] args) {
		arguments = args;
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		String folderOfProject = arguments[0];

		System.out.println("#########################################");
		System.out.println("Starting with " + folderOfProject + "\n");
		
		TableStatsMainEngine engine = new TableStatsMainEngine(folderOfProject, primaryStage);
		engine.processFolder();
		
		SchemaStatsMainEngine sengine = new SchemaStatsMainEngine(folderOfProject, primaryStage);
		sengine.processFolder();
		
		System.out.println("Done with " + folderOfProject);
		System.out.println("#########################################\n\n");
		System.exit(0);
	}

	private static String [] arguments;
}




