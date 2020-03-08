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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Locale;
import java.util.stream.Collectors;

import chartexport.TablesChartManager;
import dataload.TableDetailedStatsLoader;
import datamodel.TableDetailedStatsElement;
import javafx.stage.Stage;
import patternassessment.tablepatterns.PatternAssessmentManager;
import patternassessment.tablepatterns.PatternAssessmentTypesEnum;

/**
 * This the main engine of the tool for working with TABLE data (i.e., not SCHEMA).
 * 
 * Its role is to process the load the table data, extract charts for TABLE patterns
 * TODO: extract patterns for tables too
 *  
 * @author	pvassil, zarras, alexvou
 * @date	2019-12-14
 * @since	2019-06-15
 */
public class TableStatsMainEngine implements IMainEngine<TableDetailedStatsElement> {
	private TableDetailedStatsLoader loader;
	private ArrayList<String> header;

	private Stage stage;
	private String projectFolder;
	private String inputFolderWithStats;
	private String _DELIMETER;
	private int _NUMFIELDS;
	private Boolean _DEBUGMODE = false;
	protected Boolean _DATEMODE;

	//protected, because at testing level, where we want to avoid using stages, we will subclass it with a Stageless tablesChartManager
	protected PatternAssessmentManager patternAssessmentManager;
	protected TablesChartManager tablesChartManager;
	protected  HashMap<String, Integer> attributePositions;
	protected HashMap<Integer, ArrayList<TableDetailedStatsElement>> tuplesPerLADCollection;
	protected ArrayList<TableDetailedStatsElement> inputTupleCollection;
	protected HashMap<Integer, Double[]> durationByLADHeatmap;
	protected String outputFolderWithFigures;
	protected String outputFolderWithTestResults;
	protected String globalLogFilePath;
	protected double _ALPHA;
	protected String prjName;
	
	public TableStatsMainEngine(String anInputProjectFolder, Stage primaryStage) {		
		File prjFolder = new File(anInputProjectFolder);
		if (prjFolder.isDirectory()) {
			
			this.projectFolder = anInputProjectFolder;
			this.inputFolderWithStats = anInputProjectFolder + "/" + "results";
//			this.outputFolderWithFigures = this.projectFolder +  "/" + "figures/tableFigures";
//			this.outputFolderWithTestResults = this.projectFolder +  "/" + "resultsOfPatternTests";
			this.setupFolders();
			//TODO: check if this works
			this.globalLogFilePath = prjFolder.getParent() + "resources/GlobalLog.txt";
			_DELIMETER = "\t";
			_NUMFIELDS = 17;//17 fields, one can be empty
			_ALPHA = 0.05;
			_DATEMODE = true;
			
			this.loader = new TableDetailedStatsLoader();		
			this.inputTupleCollection = new ArrayList<TableDetailedStatsElement>();
			this.header = new ArrayList<String>();
			this.attributePositions = new HashMap<String, Integer>();
			this.tuplesPerLADCollection= new HashMap<Integer, ArrayList<TableDetailedStatsElement>>();
			this.stage = primaryStage;
			this.prjName = prjFolder.getName();
			this.patternAssessmentManager = new PatternAssessmentManager();			
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
	@Override
	public int produceFiguresAndStats() {
		int numRows = 0;

		String inputFileName = inputFolderWithStats + File.separator + "tables_DetailedStats.tsv";
		numRows = this.loadData(inputFileName, _DELIMETER, true, _NUMFIELDS, this.header, this.inputTupleCollection);
		if(numRows<1) {
			System.err.println("TableStatsMainEngine.processFolder:: did not find any rows in detailed Stats");
			System.exit(-1);
		}
		if(_DEBUGMODE) System.out.println("Processed " + numRows + " rows of " + inputFileName );
		
		if (inputTupleCollection.get(0).getBirthDate().length() < 4 || inputTupleCollection.get(0).getYearOfBirth() < 0) {
			_DATEMODE = false;
		}

		this.processHeader();

		this.organizeTuplesByLAD();
		this.produceDurationByLADHeatmap(this.prjName, tuplesPerLADCollection, this.outputFolderWithTestResults, _DATEMODE);

		this.createChartManager();  
		tablesChartManager.extractScatterCharts();

		ArrayList<PatternAssessmentTypesEnum> testsToRun= new ArrayList <PatternAssessmentTypesEnum>(
				Arrays.asList(PatternAssessmentTypesEnum.GAMMA, PatternAssessmentTypesEnum.INVERSE_GAMMA));
		if (_DATEMODE) {
			testsToRun.add(PatternAssessmentTypesEnum.ELECTROLYSIS);
		}
		this.patternAssessmentManager.assessPatterns(testsToRun,
			this.inputTupleCollection, this.prjName, this.outputFolderWithTestResults, this.globalLogFilePath, _ALPHA);

		return numRows;
	}//end processFolder


	/**
	 * Given the folder that contains the hecate output files, i.e., <PRJ>.results, it initializes/creates the output folders
	 * @return the abs path of the containing folder <PRJ>
	 * 
	 * TODO: intercept what can go wrong
	 * TODO: decide the exact location of the new folders. Maybe WITHIN /results? Maybe OTHER NAME -- e.g., resultsTests resultsFigures? 
	 */
	@Override
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
		File figureOutputFolder = new File(this.projectFolder + File.separator + "figures/tableFigures");
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
	@Override
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
		tablesChartManager = new TablesChartManager(prjName, inputTupleCollection, attributePositions, tuplesPerLADCollection, durationByLADHeatmap, outputFolderWithFigures, stage, _DATEMODE);
	}
	
	public HashMap<Integer, Double[]> produceDurationByLADHeatmap(String prjName, HashMap<Integer, ArrayList<TableDetailedStatsElement>> tuplesPerLADCollection, 
			String outputFolder, boolean dateMode) {
		this.durationByLADHeatmap = new HashMap<Integer, Double[]>();
		
		if (!dateMode)
			return this.durationByLADHeatmap;
		
		double[] durationLimits = {0.05,0.10,0.15,0.20,0.25,0.30,0.35,0.40,0.45,0.50,0.55,0.60,0.65,0.70,0.75,0.80,0.85,0.90,0.95};
		ArrayList<TableDetailedStatsElement> inputTupleCollection = new ArrayList<TableDetailedStatsElement>();
		for (ArrayList<TableDetailedStatsElement> tuples : tuplesPerLADCollection.values())
			inputTupleCollection.addAll(tuples);
		int numOfTables = inputTupleCollection.size();
		IntSummaryStatistics durationStats = inputTupleCollection.stream()
				.collect(Collectors.summarizingInt(TableDetailedStatsElement::getDurationDays));	
		int maxDuration = durationStats.getMax();
		
		ArrayList<Integer> LADKeySet = new ArrayList<Integer>() ;
		LADKeySet.add(10);LADKeySet.add(11);LADKeySet.add(12);LADKeySet.add(20);LADKeySet.add(21);LADKeySet.add(22);
		Collections.sort(LADKeySet);
		
		for(int i=0; i<LADKeySet.size();i++ ) {
			Integer LADvalue = LADKeySet.get(i);
			ArrayList<TableDetailedStatsElement> tuples = tuplesPerLADCollection.get(LADvalue);
			Double[] ladHeatmap = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
			if (tuples != null) {
				for (TableDetailedStatsElement tuple: tuples) {
					double durationPct = (double)tuple.getDurationDays() / maxDuration;
					int durationRangeIndex = 19;
					for (int j = 0; j < durationLimits.length; j++) {
						if (durationPct < durationLimits[j]) {
							durationRangeIndex = j;
							break;
						}
					}
					ladHeatmap[durationRangeIndex] ++;
				}
				// convert to percentage of total tables
				for (int j = 0; j < ladHeatmap.length; j++)
					ladHeatmap[j] /= numOfTables;
			}
			this.durationByLADHeatmap.put(LADvalue, ladHeatmap);
		}
		
		// write to file
		File durationByLADHeatmapFile = new File(outputFolder + File.separator + prjName + "_DurationByLADHeatmap.tsv");
		try {
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(durationByLADHeatmapFile, false), StandardCharsets.UTF_8));
			writer.println("LADClass\t0%-5%\t5%-10%\t10%-15%\t15%-20%\t20%-25%\t25%-30%\t30%-35%\t35%-40%\t40%-45%\t45%-50%"
					+ "\t50%-55%\t55%-60%\t60%-65%\t65%-70%\t70%-75%\t75%-80%\t80%-85%\t85%-90%\t90%-95%\t95%-100%");
			NumberFormat dFormat = new DecimalFormat("#.###").getNumberInstance(Locale.US);
			for(int i=0; i<LADKeySet.size();i++ ) {
				Integer LADvalue = LADKeySet.get(i);
				Double[] tuples = this.durationByLADHeatmap.get(LADvalue);
				String line = "" + LADvalue;
				for (int j = 0; j < tuples.length; j++)
					line += "\t" + dFormat.format(tuples[j]);
				writer.println(line);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(_DEBUGMODE) {
			for(int i=0; i<LADKeySet.size();i++ ) {
				Integer LADvalue = LADKeySet.get(i);
				Double[] tuples = this.durationByLADHeatmap.get(LADvalue);
				String line = "{";
				for (int j = 0; j < tuples.length; j++)
					line += "" + tuples[j] + ", ";
				line += "}";
				System.out.println(line);
			}
		}
		
		return this.durationByLADHeatmap;
	}


}//end class
