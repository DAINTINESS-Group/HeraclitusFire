/**
 * 
 */
package mainEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import chartexport.SchemaChartManager;
import dataload.SchemaHeartbeatLoader;
import datamodel.SchemaHeartbeatElement;
import datamodel.SchemaLevelInfo;
import javafx.stage.Stage;

/**
 * This the main engine of the tool for working with SCHEMA data (i.e., not TABLE).
 * 
 * Its role is to process the load the schema data, extract charts for SCHEMA patterns
 * TODO: extract charts and patterns for schemas too
 *  
 * @author	alexvou
 */
public class SchemaStatsMainEngine {

	private SchemaHeartbeatLoader loader;
	private ArrayList<String> header;

	private Stage stage;
	private String projectFolder;
	private String inputFolderWithStats;
	private String outputFolderWithTestResults;
//	private String globalStatsOutputFolder;
	private String _DELIMETER;
	private int _NUMFIELDS;
	private Boolean _DEBUGMODE = false;

	//protected, because at testing level, where we want to avoid using stages, we will subclass it with a Stageless tablesChartManager
	protected SchemaChartManager schemaChartManager;
	protected HashMap<String, Integer> attributePositions;
	protected HashMap<Integer, ArrayList<SchemaHeartbeatElement>> tuplesPerRYFV0Collection;
	protected ArrayList<SchemaHeartbeatElement> inputTupleCollection;
	protected String outputFolderWithFigures;
	protected String prjName;
	protected Boolean _DATEMODE;  // if there are date values (running years etc.) or not, group or not, create respective charts

	public SchemaStatsMainEngine(String anInputProjectFolder, Stage primaryStage) {		
		File prjFolder = new File(anInputProjectFolder);
		if (prjFolder.isDirectory()) {
			this.projectFolder = anInputProjectFolder;
			this.inputFolderWithStats = anInputProjectFolder + "/" + "results";
//			this.outputFolderWithFigures = this.projectFolder +  "/" + "figures/schemaFigures";
			this.setupFolders();
			_DELIMETER = "\t";
			_NUMFIELDS = 28;//28 fields, one can be empty, is not practicly used
			_DATEMODE = true;

			this.loader = new SchemaHeartbeatLoader();		
			this.inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
			this.header = new ArrayList<String>();
			this.attributePositions = new HashMap<String, Integer>();
			this.tuplesPerRYFV0Collection= new HashMap<Integer, ArrayList<SchemaHeartbeatElement>>();
			this.stage = primaryStage;
			this.prjName = prjFolder.getName();
		}
	}//end constructor


	/**
	 * A method that loads the data from SchemaHeartbeat.tsv and produces figures for the evo patterns
	 * 
	 * To this end, the method populates the local instance variables
	 * with the structure and contents of the file, 
	 * TODO: pass them to the Chart manager for charts
	 * TODO: add the pattern manager for patterns
	 * 
	 * @return an int with the number of rows processed from SchemaHeartbeat.tsv
	 */
	public int produceSchemaFiguresAndStats() {
		int numRows = 0;

		String inputFileName = inputFolderWithStats + File.separator + "SchemaHeartbeat.tsv";
		numRows = this.loadData(inputFileName, _DELIMETER, true, _NUMFIELDS, this.header, this.inputTupleCollection);
		if(numRows<1) {
			System.err.println("SchemaStatsMainEngine.processFolder:: did not find any rows in schema heartbeat");
			System.exit(-1);
		}
		if (inputTupleCollection.get(0).getRunningYearFromV0() == -1) {
			_DATEMODE = false;
		}
		if(_DEBUGMODE) System.out.println("Processed " + numRows + " rows of " + inputFileName );

		this.processHeader();

		this.organizeTuplesByRYFV0();

		this.createChartManager();
		schemaChartManager.extractCharts();
		
		this.extractSchemaLevelInfo();

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

		File figureGlobalOutputFolder = new File(this.projectFolder + File.separator + "figures");
		if (!figureGlobalOutputFolder.exists()) {
			figureGlobalOutputFolder.mkdir();
		}
		File figureOutputFolder = new File(this.projectFolder + File.separator + "figures/schemaFigures");
		if (!figureOutputFolder.exists()) {
			figureOutputFolder.mkdir();
		}
		this.outputFolderWithFigures = figureOutputFolder.getAbsolutePath();
		
//		File globalsOutputFolder = new File("resources/globalStats");
//		if (!globalsOutputFolder.exists()) {
//			globalsOutputFolder.mkdir();
//		}
//		this.globalStatsOutputFolder = globalsOutputFolder.getAbsolutePath();

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
	public int loadData(String fileName, String delimeter, boolean hasHeaderLine, int numFields, ArrayList<String> headerList, ArrayList<SchemaHeartbeatElement> objCollection) {
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
	 * Populates a hashMap with keys the ryfv0 values and values
	 * their respective tuples, per ryfv0 value
	 */
	private HashMap<Integer, ArrayList<SchemaHeartbeatElement>> organizeTuplesByRYFV0() {
		if (_DATEMODE) {
			for(SchemaHeartbeatElement tuple: this.inputTupleCollection) {
	
				Integer RYFV0value = tuple.getRunningYearFromV0();
				ArrayList<SchemaHeartbeatElement> ryfv0Tuples = this.tuplesPerRYFV0Collection.get(RYFV0value);
				if (ryfv0Tuples == null) {
					ryfv0Tuples = new ArrayList<SchemaHeartbeatElement>();
					ryfv0Tuples.add(tuple);
					this.tuplesPerRYFV0Collection.put(RYFV0value, ryfv0Tuples);
				}
				else
					if(!ryfv0Tuples.contains(tuple))
						ryfv0Tuples.add(tuple); 
			}
		}

		if(_DEBUGMODE) {
			for(Integer RYFV0: this.tuplesPerRYFV0Collection.keySet()) {
				ArrayList<SchemaHeartbeatElement> ryfv0Tuples = this.tuplesPerRYFV0Collection.get(RYFV0);
				System.out.println(RYFV0 + ": " + ryfv0Tuples.size());
				for (SchemaHeartbeatElement tuple:  ryfv0Tuples)
					System.out.println("\t" + tuple.getTrID());
			}
			if (!_DATEMODE) System.out.println("No RunningYearsFromV0 data, no grouping");
		}
		return this.tuplesPerRYFV0Collection;
	}//end organizeTuplesByLAD


	/**
	 * Initializes the Chart manager
	 * 
	 * The reason for the separate treatment is the stage needed for the new of TablesChartManager.
	 * We keep this code separately, to facilitate testing, without the need for launching stages.
	 */
	protected void createChartManager() {
		schemaChartManager = new SchemaChartManager(prjName, inputTupleCollection, attributePositions,tuplesPerRYFV0Collection, outputFolderWithFigures, stage, _DATEMODE);
	}
	
	public void extractSchemaLevelInfo() {
		String projectName = this.prjName;
		int projectDurationInDays = -1;
		int projectDurationInMonths = -1;
		int projectDureationInYears = -1;
		if (_DATEMODE) {
			projectDurationInDays = this.inputTupleCollection.get(this.inputTupleCollection.size()-1).getIntValueByPosition(5);
			projectDurationInMonths = this.inputTupleCollection.get(this.inputTupleCollection.size()-1).getIntValueByPosition(6);
			projectDureationInYears = this.inputTupleCollection.get(this.inputTupleCollection.size()-1).getIntValueByPosition(7);
		}
		
		int numCommits = this.inputTupleCollection.size();
		int numTablesAtStart = this.inputTupleCollection.get(0).getIntValueByPosition(9);
		int numTablesAtEnd = this.inputTupleCollection.get(this.inputTupleCollection.size()-1).getIntValueByPosition(9);
		int numAttrsAtStart = this.inputTupleCollection.get(0).getIntValueByPosition(11);
		int numAttrsAtEnd = this.inputTupleCollection.get(this.inputTupleCollection.size()-1).getIntValueByPosition(11);
		
		int totalTableInsertions = 0;
		//int totalTableInsertions = this.inputTupleCollection.get(0).getIntValueByPosition(12);
		int totalTableDeletions = 0;
		int totalAttrInsWithTableIns = 0;
		//int totalAttrInsWithTableIns = this.inputTupleCollection.get(0).getIntValueByPosition(14);
		int totalAttrbDelWithTableDel = 0;
		int totalAttrInjected = 0;
		int totalAttrEjected = 0;
		int tatalAttrWithTypeUpd = 0;
		int totalAttrInPKUpd = 0;
		
		int totalExpansion = this.inputTupleCollection.get(0).getIntValueByPosition(25);
		int totalMaintenance = this.inputTupleCollection.get(0).getIntValueByPosition(26);
		int totalTotalAttrActivity = this.inputTupleCollection.get(0).getIntValueByPosition(27);
		
		for (int i=1; i < this.inputTupleCollection.size(); i++) {
			totalTableInsertions += this.inputTupleCollection.get(i).getIntValueByPosition(12);
			totalTableDeletions += this.inputTupleCollection.get(i).getIntValueByPosition(13);
			totalAttrInsWithTableIns += this.inputTupleCollection.get(i).getIntValueByPosition(14);
			totalAttrbDelWithTableDel += this.inputTupleCollection.get(i).getIntValueByPosition(15);
			totalAttrInjected += this.inputTupleCollection.get(i).getIntValueByPosition(16);
			totalAttrEjected += this.inputTupleCollection.get(i).getIntValueByPosition(17);
			tatalAttrWithTypeUpd += this.inputTupleCollection.get(i).getIntValueByPosition(18);
			totalAttrInPKUpd += this.inputTupleCollection.get(i).getIntValueByPosition(19);
			
			totalExpansion += this.inputTupleCollection.get(i).getIntValueByPosition(25);
			totalMaintenance += this.inputTupleCollection.get(i).getIntValueByPosition(26);
			totalTotalAttrActivity += this.inputTupleCollection.get(i).getIntValueByPosition(27);
		}
		
		double expansionRatePerCommit = totalExpansion / (double)numCommits;
		double expansionRatePerMonth = -1;
		double expansionRatePeryear = -1;
		double maintenanceRatePerCommit = totalMaintenance / (double)numCommits;
		double maintenanceRatePerMonth = -1;
		double maintenanceRatePeryear = -1;
		double totalAttrActivityRatePerCommit = totalTotalAttrActivity / (double)numCommits;
		double totalAttrActivityRatePerMonth = -1;
		double totalAttrActivityRatePeryear = -1;
		if (_DATEMODE) {
			expansionRatePerMonth = totalExpansion / (double)projectDurationInMonths;
			expansionRatePeryear = totalExpansion / (double)projectDureationInYears;
			maintenanceRatePerMonth = totalMaintenance / (double)projectDurationInMonths;
			maintenanceRatePeryear = totalMaintenance / (double)projectDureationInYears;
			totalAttrActivityRatePerMonth = totalTotalAttrActivity / (double)projectDurationInMonths;
			totalAttrActivityRatePeryear = totalTotalAttrActivity / (double)projectDureationInYears;
		}
		double resizingratio = numTablesAtEnd / (double)numTablesAtStart;
		
		SchemaLevelInfo schemaLevelInfo = new SchemaLevelInfo(projectName, projectDurationInDays, projectDurationInMonths,
				projectDureationInYears, numCommits, numTablesAtStart, numTablesAtEnd, numAttrsAtStart,
				numAttrsAtEnd, totalTableInsertions, totalTableDeletions, totalAttrInsWithTableIns,
				totalAttrbDelWithTableDel, totalAttrInjected, totalAttrEjected, tatalAttrWithTypeUpd,
				totalAttrInPKUpd, totalExpansion, totalMaintenance, totalTotalAttrActivity,
				expansionRatePerCommit, expansionRatePerMonth, expansionRatePeryear,
				maintenanceRatePerCommit, maintenanceRatePerMonth, maintenanceRatePeryear,
				totalAttrActivityRatePerCommit, totalAttrActivityRatePerMonth,
				totalAttrActivityRatePeryear, resizingratio);
		
		File globalSchemaLevelInfoTSVFile = new File(this.outputFolderWithTestResults + File.separator + "GlobalSchemaLevelInfo.tsv");
		try {
			//PrintWriter writer = new PrintWriter(globalSchemaLevelInfoTSVFile, StandardCharsets.UTF_8);
			//boolean fileExists = globalSchemaLevelInfoTSVFile.exists() ? true : false;
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(globalSchemaLevelInfoTSVFile, false), StandardCharsets.UTF_8));
			//if (!fileExists)
				writer.println("Project\tDurationInDays\tDurationInMonths\tDurationInYears\t#Commits\t#Tables@Start\t#Tables@End" 
						+ "\t#Attrs@Start\t#Attrs@End\tTotalTableInsertions\tTotalTableDeletions\tTotalAttrInsWithTableIns\tTotalAttrbDelWithTableDel" 
						+ "\tTotalAttrInjected\tTotalAttrEjected\tTatalAttrWithTypeUpd\tTotalAttrInPKUpd\tTotalExpansion\tTotalMaintenance\tTotalTotalAttrActivity" 
						+ "\tExpansionRatePerCommit\tExpansionRatePerMonth\tExpansionRatePeryear\tMaintenanceRatePerCommit\tMaintenanceRatePerMonth\tMaintenanceRatePeryear" 
						+ "\tTotalAttrActivityRatePerCommit\tTotalAttrActivityRatePerMonth\tTotalAttrActivityRatePeryear\tResizingratio");
			writer.println(schemaLevelInfo.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	 

}//end class
