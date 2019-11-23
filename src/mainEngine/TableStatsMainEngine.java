/**
 * 
 */
package mainEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import chartexport.TablesChartManager;
import dataload.TableDetailedStatsLoader;
import datamodel.TableDetailedStatsElement;
import javafx.stage.Stage;

/**
 * This the main engine of the tool for working with TABLE data (i.e., not SCHEMA).
 * 
 * Its role is to process the load the table data, extract charts for TABLE patterns
 * TODO: extract patterns for tables too
 *  
 * @author	pvassil, zarras
 * @date	2019-07-16
 * @since	2019-06-15
 */
public class TableStatsMainEngine {

	public TableStatsMainEngine(String anInputProjectFolder, Stage primaryStage) {		
		File prjFolder = new File(anInputProjectFolder);
		if (prjFolder.isDirectory()) {
			this.projectFolder = anInputProjectFolder;
			this.inputFolderWithStats = anInputProjectFolder + "/" + "results";
			this.outputFolderWithFigures = this.projectFolder +  "/" + "charts";
			_DELIMETER = "\t";
			_NUMFIELDS = 17;//17 fields, one can be empty

			this.loader = new TableDetailedStatsLoader();		
			this.inputTupleCollection = new ArrayList<TableDetailedStatsElement>();
			this.header = new ArrayList<String>();
			this.attributePositions = new HashMap<String, Integer>();
			this.tuplesPerLADCollection= new HashMap<Integer, ArrayList<TableDetailedStatsElement>>();
			this.stage = primaryStage;
			this.prjName = prjFolder.getName();
		}
	}//end constructor


	/**
	 * A method that loads the data from tables_DetailedStats.tsv and produces figures for the evo patterns
	 * 
	 * To this end, the method populates the local instance variables
	 * with the structure and contents of the file, and passes them
	 * to the Chart manager for charts
	 * TODO: add the pattern manager for patterns
	 * 
	 * @return an int with the number of rows processed from tables_DetailedStats.tsv
	 */
	public int processFolder() {
		int numRows = 0;

		this.setupFolders();

		String inputFileName = inputFolderWithStats + File.separator + "tables_DetailedStats.tsv";
		numRows = this.loadData(inputFileName, _DELIMETER, true, _NUMFIELDS, this.header, this.inputTupleCollection);
		if(numRows<1) {
			System.err.println("TableStatsMainEngine.processFolder:: did not find any rows in detailed Stats");
			System.exit(-1);
		}
		if(_DEBUGMODE) System.out.println("Processed " + numRows + " rows of " + inputFileName );

		this.processHeader();

		this.organizeTuplesByLAD();

		this.createChartManager();  
		tablesChartManager.extractScatterCharts();

		//TODO ###########################################
		//TEST THAT YOU PROCESSED INPUT FILE CORRECTLY
		//RE-TEST
		//################################################

		//TODO ###########################################
		// PLAN AND EXECUTE STATISTICAL TESTS
		//################################################

		return numRows;
	}//end processFolder


	/**
	 * Given the folder that contains the hecate output files, i.e., <PRJ>.results, it initializes/creates the output folders
	 * @return the abs path of the containing folder <PRJ>
	 * 
	 * TODO: intercept what can go wrong
	 * TODO: decide the exact location of the new folders. Maybe WITHIN /results? Maybe OTHER NAME -- e.g., resultsTests resultsFigures? 
	 */
	public String setupFolders() {
		File projectFolder = new File(this.projectFolder);

		//String parent = projectFolder.getParent();
		String parentAbsolute = projectFolder.getAbsolutePath();

		File testOutputFolder = new File(this.projectFolder + File.separator + "resultsOfPatternTests");
		if (!testOutputFolder.exists()) {
			testOutputFolder.mkdir();
		}
		this.outputFolderWithTestResults = testOutputFolder.getAbsolutePath();

		File figureOutputFolder = new File(this.projectFolder + File.separator + "figures");
		if (!figureOutputFolder.exists()) {
			figureOutputFolder.mkdir();
		}
		this.outputFolderWithFigures = figureOutputFolder.getAbsolutePath();

		return parentAbsolute;
	}//end setupFolders



	/**
	 * Implements the main loading functionality to populate the header and the input tuple collection
	 * 
	 * @param fileName a String with the path of the file
	 * @param delimeter a string with the attribute delimiter
	 * @param hasHeaderLine a boolean flag on whether the file has a header
	 * @param numFields  an int with the number of the fields of the file
	 * @param headerList   an ArrayList of Strings with the attribute names at the header of the file
	 * @param objCollection an ArrayList of tuples with the contents of the file
	 * @return  the number of rows that are processed
	 */
	public int loadData(String fileName, String delimeter, boolean hasHeaderLine, int numFields, ArrayList<String> headerList, ArrayList<TableDetailedStatsElement> objCollection) {
		return loader.load(fileName, delimeter, hasHeaderLine, numFields, headerList, objCollection);
	}//end loadData



	/**
	 * Populates the attributePositions hashmap
	 * s.t. for each attribute we know its position within each tuple
	 */
	private void processHeader() {	
		for (int i = 0; i< header.size(); i++) {
			String nextAttr = header.get(i);
			this.attributePositions.put(nextAttr, i);
			if(_DEBUGMODE) System.out.print(nextAttr + "\t");
		}
		if(_DEBUGMODE) System.out.println();
	}//end processHeader

	/**
	 * Populates a hashMap with keys the lad values and values
	 * their respective tuples, per lad value
	 */
	private HashMap<Integer, ArrayList<TableDetailedStatsElement>> organizeTuplesByLAD() {
		for(TableDetailedStatsElement tuple: this.inputTupleCollection) {

			Integer LADvalue = tuple.getLadclass();
			ArrayList<TableDetailedStatsElement> ladTuples = this.tuplesPerLADCollection.get(LADvalue);
			if (ladTuples == null) {
				ladTuples = new ArrayList<TableDetailedStatsElement>();
				ladTuples.add(tuple);
				this.tuplesPerLADCollection.put(LADvalue, ladTuples);
			}
			else
				if(!ladTuples.contains(tuple))
					ladTuples.add(tuple); 
		}

		if(_DEBUGMODE) {
			for(Integer LAD: this.tuplesPerLADCollection.keySet()) {
				ArrayList<TableDetailedStatsElement> ladTuples = this.tuplesPerLADCollection.get(LAD);
				System.out.println(LAD + ": " + ladTuples.size());
				for (TableDetailedStatsElement tuple:  ladTuples)
					System.out.println("\t" + tuple.getTable());
			}//for
		}//if
		return this.tuplesPerLADCollection;
	}//end organizeTuplesByLAD


	/**
	 * Initializes the Chart manager
	 * 
	 * The reason for the separate treatment is the stage needed for the new of TablesChartManager.
	 * We keep this code separately, to facilitate testing, without the need for launching stages.
	 */
	protected void createChartManager() {
		tablesChartManager = new TablesChartManager(prjName, inputTupleCollection, attributePositions, tuplesPerLADCollection, outputFolderWithFigures, stage);
	}

	private TableDetailedStatsLoader loader;
	private ArrayList<String> header;

	private Stage stage;
	private String projectFolder;
	private String inputFolderWithStats;
	private String outputFolderWithTestResults;
	private String _DELIMETER;
	private int _NUMFIELDS;
	private Boolean _DEBUGMODE = true;

	//protected, because at testing level, where we want to avoid using stages, we will subclass it with a Stageless tablesChartManager
	protected TablesChartManager tablesChartManager;
	protected  HashMap<String, Integer> attributePositions;
	protected HashMap<Integer, ArrayList<TableDetailedStatsElement>> tuplesPerLADCollection;
	protected ArrayList<TableDetailedStatsElement> inputTupleCollection;
	protected String outputFolderWithFigures;
	protected String prjName;
}//end class
