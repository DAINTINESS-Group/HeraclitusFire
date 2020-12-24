/**
 * 
 */
package mainEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import chartexport.SchemaChartManager;
import dataload.SchemaHeartbeatLoader;
import datamodel.MonthSchemaStats;
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
public class SchemaStatsMainEngine implements IMainEngine<SchemaHeartbeatElement> {

	private SchemaHeartbeatLoader loader;
	private ArrayList<String> header;

	private Stage stage;
	private String projectFolder;
	private String inputFolderWithStats;
	private String outputFolderWithTestResults;
	private String outputFolderWithSummaries;
	private String _DELIMETER;
	private int _NUMFIELDS;
	private Boolean _DEBUGMODE = false;

	//protected, because at testing level, where we want to avoid using stages, we will subclass it with a Stageless tablesChartManager
	protected SchemaChartManager schemaChartManager;
	protected HashMap<String, Integer> attributePositions;
	protected HashMap<Integer, ArrayList<SchemaHeartbeatElement>> tuplesPerRYFV0Collection;
	protected ArrayList<SchemaHeartbeatElement> inputTupleCollection;
	protected SchemaLevelInfo schemaLevelInfo;
	protected ArrayList<MonthSchemaStats> monthlySchemaStatsCollection;
	protected HashMap<String, Integer> monthlyAttributePositions;
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
	@Override
	public int produceFiguresAndStats() {
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

		this.extractMonthlySchemaStats(this.prjName, this.inputTupleCollection, this.inputFolderWithStats, _DATEMODE);
		
		this.createChartManager();
		schemaChartManager.extractCharts();
		
		this.extractSchemaLevelInfo(this.prjName, this.inputTupleCollection, this.outputFolderWithSummaries, _DATEMODE);
		
		this.produceSummaryHTML(this.prjName , this.projectFolder + "/figures", this.outputFolderWithSummaries, this.outputFolderWithSummaries);
		
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
		File figureOutputFolder = new File(this.projectFolder + File.separator + "figures/schemaFigures");
		if (!figureOutputFolder.exists()) {
			figureOutputFolder.mkdir();
		}
		this.outputFolderWithFigures = figureOutputFolder.getAbsolutePath();
		
		File summariesOutputFolder = new File(this.projectFolder + File.separator + "summaries");
		if (!summariesOutputFolder.exists()) {
			summariesOutputFolder.mkdir();
		}
		this.outputFolderWithSummaries = summariesOutputFolder.getAbsolutePath();

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
		schemaChartManager = new SchemaChartManager(prjName, inputTupleCollection, attributePositions,tuplesPerRYFV0Collection, monthlySchemaStatsCollection, monthlyAttributePositions, outputFolderWithFigures, stage, _DATEMODE);
	}
	
	public SchemaLevelInfo extractSchemaLevelInfo(String prjName, ArrayList<SchemaHeartbeatElement> inputTupleCollection, String outputFolderWithSummaries, boolean dateMode) {
		String projectName = prjName;
		int projectDurationInDays = -1;
		int projectDurationInMonths = -1;
		int projectDureationInYears = -1;
		if (dateMode) {
			projectDurationInDays = inputTupleCollection.get(inputTupleCollection.size()-1).getDistFromV0InDays();
			projectDurationInMonths = inputTupleCollection.get(inputTupleCollection.size()-1).getRunningMonthFromV0();
			projectDureationInYears = inputTupleCollection.get(inputTupleCollection.size()-1).getRunningYearFromV0();
		}
		
		int numCommits = inputTupleCollection.size();
		int numTablesAtStart = inputTupleCollection.get(0).getNumNewTables();
		int numTablesAtEnd = inputTupleCollection.get(inputTupleCollection.size()-1).getNumNewTables();
		int numAttrsAtStart = inputTupleCollection.get(0).getNumNewAttrs();
		int numAttrsAtEnd = inputTupleCollection.get(inputTupleCollection.size()-1).getNumNewAttrs();
		
		int totalTableInsertions = 0;
		//int totalTableInsertions = inputTupleCollection.get(0).getIntValueByPosition(12);
		int totalTableDeletions = 0;
		int totalAttrInsWithTableIns = 0;
		//int totalAttrInsWithTableIns = inputTupleCollection.get(0).getIntValueByPosition(14);
		int totalAttrbDelWithTableDel = 0;
		int totalAttrInjected = 0;
		int totalAttrEjected = 0;
		int totalAttrWithTypeUpd = 0;
		int totalAttrInPKUpd = 0;
		
		int totalExpansion = 0;
		//int totalExpansion = inputTupleCollection.get(0).getIntValueByPosition(25);
		int totalMaintenance = 0;
		//int totalMaintenance = inputTupleCollection.get(0).getIntValueByPosition(26);
		int totalTotalAttrActivity = 0;
		//int totalTotalAttrActivity = inputTupleCollection.get(0).getIntValueByPosition(27);
		
		///
		int reeds = 0;
		int reedsPostV0 = 0;
		double reedRatioAComm = 0.0;
		double reedRatioTComm = 0.0;
		int activityDueToReeds = 0;
		int activityDueToReedsPostV0 = 0;
		int turfs = 0;
		int turfsPostV0 = 0;
		double turfRatioAComm = 0.0;
		double turfRatioTComm = 0.0;
		int activityDueToTurf = 0;
		int activityDueToTurfPostV0 = 0;
		int activeCommits = 0;
		double activeCommitRatePerMonth = 0.0;
		double commitRatePerMonth = 0.0;
		double activeCommitRatio = 0.0;
		
		if(inputTupleCollection.get(0).getIsReed() == 0) //if first commit is reed
		{
			reeds = 1;
			activityDueToReeds += inputTupleCollection.get(0).getTotalAttrActivity(); 
		}
		if(inputTupleCollection.get(0).getIsTurf() == 0) //if first commit is turf
		{
			turfs = 1;
			activityDueToTurf += inputTupleCollection.get(0).getTotalAttrActivity();
		}
		if(inputTupleCollection.get(0).getIsActive() == 0) //if first commit is active
		{
			activeCommits = 1;
		}
		///
		
		for (int i=1; i < inputTupleCollection.size(); i++) {
			totalTableInsertions += inputTupleCollection.get(i).getTablesIns();
			totalTableDeletions += inputTupleCollection.get(i).getTablesDel();
			totalAttrInsWithTableIns += inputTupleCollection.get(i).getAttrsInsWithTableIns();
			totalAttrbDelWithTableDel += inputTupleCollection.get(i).getAttrsbDelWithTableDel();
			totalAttrInjected += inputTupleCollection.get(i).getAttrsInjected();
			totalAttrEjected += inputTupleCollection.get(i).getAttrsEjected();
			totalAttrWithTypeUpd += inputTupleCollection.get(i).getAttrsWithTypeUpd();
			totalAttrInPKUpd += inputTupleCollection.get(i).getAttrsInPKUpd();
			
			totalExpansion += inputTupleCollection.get(i).getExpansion();
			totalMaintenance += inputTupleCollection.get(i).getMaintenance();
			totalTotalAttrActivity += inputTupleCollection.get(i).getTotalAttrActivity();
			
			///
			if(inputTupleCollection.get(i).getIsReed() == 0)
			{
				reeds++;
				reedsPostV0++;
				activityDueToReeds += inputTupleCollection.get(i).getTotalAttrActivity();
				activityDueToReedsPostV0 += inputTupleCollection.get(i).getTotalAttrActivity();
			}
			if(inputTupleCollection.get(i).getIsTurf() == 0)
			{
				turfs++;
				turfsPostV0++;
				activityDueToTurf += inputTupleCollection.get(i).getTotalAttrActivity();
				activityDueToTurfPostV0 += inputTupleCollection.get(i).getTotalAttrActivity();
			}
			if(inputTupleCollection.get(i).isActive() == 0)
			{
				activeCommits++;
			}
			///
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
		if (dateMode) {
			expansionRatePerMonth = totalExpansion / (double)projectDurationInMonths;
			expansionRatePeryear = totalExpansion / (double)projectDureationInYears;
			maintenanceRatePerMonth = totalMaintenance / (double)projectDurationInMonths;
			maintenanceRatePeryear = totalMaintenance / (double)projectDureationInYears;
			totalAttrActivityRatePerMonth = totalTotalAttrActivity / (double)projectDurationInMonths;
			totalAttrActivityRatePeryear = totalTotalAttrActivity / (double)projectDureationInYears;
		}
		double resizingratio = numTablesAtEnd / (double)numTablesAtStart;
		
		///
		if(activeCommits != 0)
		{
			reedRatioAComm = (double)reeds / activeCommits;
			reedRatioAComm = Math.round(reedRatioAComm * 100.0) / 100.0;
			
			turfRatioAComm = (double)turfs / activeCommits;
			turfRatioAComm = Math.round(turfRatioAComm * 100.0) / 100.0;
			
		}
		if(numCommits != 0)
		{
			reedRatioTComm = (double)reeds / numCommits;
			reedRatioTComm = Math.round(reedRatioTComm * 100.0) / 100.0;
			
			turfRatioTComm = (double)turfs / numCommits;
			turfRatioTComm = Math.round(turfRatioTComm * 100.0) / 100.0;
			
			activeCommitRatio = (double)activeCommits / numCommits;
			activeCommitRatio = Math.round(activeCommitRatio * 100.0) / 100.0;
		}
		
		if((projectDurationInMonths != -1) && (projectDurationInMonths != 0))	//if dateMode in current project exists	and - is not zero - (maybe zero check not needed)
		{
			activeCommitRatePerMonth = (double)activeCommits / projectDurationInMonths;
			activeCommitRatePerMonth = Math.round(activeCommitRatePerMonth * 100.0) / 100.0;
			
			commitRatePerMonth = (double)numCommits / projectDurationInMonths;
			commitRatePerMonth = Math.round(commitRatePerMonth * 100.0) / 100.0;
		} 
		
		///
		
		this.schemaLevelInfo = new SchemaLevelInfo(projectName, projectDurationInDays, projectDurationInMonths,
				projectDureationInYears, numCommits, numTablesAtStart, numTablesAtEnd, numAttrsAtStart,
				numAttrsAtEnd, totalTableInsertions, totalTableDeletions, totalAttrInsWithTableIns,
				totalAttrbDelWithTableDel, totalAttrInjected, totalAttrEjected, totalAttrWithTypeUpd,
				totalAttrInPKUpd, totalExpansion, totalMaintenance, totalTotalAttrActivity,
				expansionRatePerCommit, expansionRatePerMonth, expansionRatePeryear,
				maintenanceRatePerCommit, maintenanceRatePerMonth, maintenanceRatePeryear,
				totalAttrActivityRatePerCommit, totalAttrActivityRatePerMonth,
				totalAttrActivityRatePeryear, resizingratio, reeds, reedsPostV0, reedRatioAComm, reedRatioTComm,
				activityDueToReeds, activityDueToReedsPostV0, turfs, turfsPostV0, turfRatioAComm, turfRatioTComm, 
				activityDueToTurf, activityDueToTurfPostV0, activeCommits, activeCommitRatePerMonth, commitRatePerMonth, activeCommitRatio);
		
		File globalSchemaLevelInfoTSVFile = new File(outputFolderWithSummaries + File.separator + prjName + "_SchemaLevelInfo.tsv");
		try {
			//PrintWriter writer = new PrintWriter(globalSchemaLevelInfoTSVFile, StandardCharsets.UTF_8);
			//boolean fileExists = globalSchemaLevelInfoTSVFile.exists() ? true : false;
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(globalSchemaLevelInfoTSVFile, false), StandardCharsets.UTF_8));
			//if (!fileExists)
				writer.println("Project\tDurationInDays\tDurationInMonths\tDurationInYears\t#Commits\t#Tables@Start\t#Tables@End" 
						+ "\t#Attrs@Start\t#Attrs@End\tTotalTableInsertions\tTotalTableDeletions\tTotalAttrInsWithTableIns\tTotalAttrbDelWithTableDel" 
						+ "\tTotalAttrInjected\tTotalAttrEjected\tTotalAttrWithTypeUpd\tTotalAttrInPKUpd\tTotalExpansion\tTotalMaintenance\tTotalTotalAttrActivity" 
						+ "\tExpansionRatePerCommit\tExpansionRatePerMonth\tExpansionRatePeryear\tMaintenanceRatePerCommit\tMaintenanceRatePerMonth\tMaintenanceRatePeryear" 
						+ "\tTotalAttrActivityRatePerCommit\tTotalAttrActivityRatePerMonth\tTotalAttrActivityRatePeryear\tResizingratio\tReeds\tReedsPostV0"
						+ "\tReedRatioAComm\tReedRatioTComm\tActivityDueToReeds\tActivityDueToReedsPostV0\tTurfs\tTurfsPostV0"
						+ "\tTurfRatioAComm\tTurfRatioTComm\tActivityDueToTurf\tActivityDueToTurfPostV0\tActiveCommits\tActiveCommitRatePerMonth"
						+ "\tCommitRatePerMonth\tActiveCommitRatio");
			writer.println(this.schemaLevelInfo.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.schemaLevelInfo;
	}
	
	public int produceSummaryHTML(String prjName, String figuresFolder, String folderWithSummaries, String outputFolder) {
		// load schema profile data
		//File schemaLevelInfoFile = new File(outputFolderWithPatterns + File.separator + prjName + "_SchemaLevelInfo.tsv");
		Scanner inputStream = null;
		try {
			//inputStream = new Scanner(new FileInputStream(outputFolderWithPatterns + File.separator + prjName + "_SchemaLevelInfo.tsv"));
			inputStream = new Scanner(new FileInputStream(folderWithSummaries + File.separator + prjName + "_SchemaLevelInfo.tsv"));

		} catch (FileNotFoundException e) {
			System.out.println("Problem opening file: " + folderWithSummaries + File.separator + prjName + "_SchemaLevelInfo.tsv");
			return -1;
		}
		ArrayList<String[]> schemaLevelInfoList = new ArrayList<String[]>();
		while (inputStream.hasNextLine()) 
			schemaLevelInfoList.add(inputStream.nextLine().split("\t"));
		inputStream.close();
		
		// load figures
		ArrayList<ArrayList<File>> figsPerFolder = new ArrayList<ArrayList<File>>();
		File directory = new File(figuresFolder + File.separator + "schemaFigures");
	    if (!directory.exists())
	    	return -1;
	    File[] fList = directory.listFiles();
	    if(fList != null) {
	    	ArrayList<File> files = new ArrayList<File>();
	        for (File file : fList) 
	        	if (file.isFile() && file.getName().endsWith(".png")) 
	            	files.add(file);
	        figsPerFolder.add(files);
	    } else {
	    	return -1;
	    }
	    directory = new File(figuresFolder + File.separator + "tableFigures");
	    if (!directory.exists())
	    	return -1;
	    fList = directory.listFiles();
	    if(fList != null) {
	    	ArrayList<File> files = new ArrayList<File>();
	        for (File file : fList) 
	        	if (file.isFile() && file.getName().endsWith(".png")) 
	            	files.add(file);
	        figsPerFolder.add(files);
	    } else {
	    	return -1;
	    }

	    //TODO: split the table in chunks, so that it fits in a reasonable width & move at the end
	    //TODO: re-arrange the position of the schema figures, they are placed in alphab. order, but, the e&m h/b should go _after_ the 4 schema sizes
	    //(maybe rename the produced image, s.t., it is after 'Num' and before 'Total')
	    // create html
		File summaryHTMLFile = new File(outputFolder + File.separator + prjName + "_Summary.html");
		try {
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(summaryHTMLFile, false), StandardCharsets.UTF_8));
			writer.println("<html>");
			writer.println("<head>\n<title>" + prjName + "</title>\n</head>");
			writer.println("<body>");
			writer.println("<h1>" + prjName + "</h1>");
			writer.println("<table style=\"width:100%\">");
			int colsPerRow = 12;
			int totalColumns = schemaLevelInfoList.get(0).length;
			int numOfRows = (int)Math.ceil((double)totalColumns/colsPerRow);
			int jStart, jEnd = 0;
			for(int i = 0; i < numOfRows; i++) {
				jStart = i * colsPerRow;
				jEnd = Math.min((i+1)*colsPerRow, totalColumns);
				writer.println("<tr>");
				for (int j = jStart; j < jEnd; j++) {
					writer.println("<td><b>" + schemaLevelInfoList.get(0)[j] + "</b></td>");
				}
				writer.println("</tr>\n<tr>");
				for (int j = jStart; j < jEnd; j++) {
					writer.println("<td>" + schemaLevelInfoList.get(1)[j] + "</td>");
				}
				writer.println("</tr>");
			}
			writer.println("</table>");
			writer.println("<h3>Schema Figures</h3>");
			writer.println("<table style=\"width:100%\">");
			writer.println("<tr>");
			for (File f: figsPerFolder.get(0)) {
				writer.println("<img src=\"../figures/schemaFigures/" + f.getName() + "\" alt=\"" + f.getName() + "\" width=\"500\" height=\"500\">");
			}
			writer.println("</tr>");
			writer.println("</table>");
			writer.println("<h3>Table Figures</h3>");
			writer.println("<table style=\"width:100%\">");
			writer.println("<tr>");
			for (File f: figsPerFolder.get(1)) {
				writer.println("<img src=\"../figures/tableFigures/" + f.getName() + "\" alt=\"" + f.getName() + "\" width=\"500\" height=\"500\">");
			}
			writer.println("</tr>");
			writer.println("</table>\n</body>\n</html>");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public ArrayList<MonthSchemaStats> extractMonthlySchemaStats(String prjName, ArrayList<SchemaHeartbeatElement> inputTupleCollection, String outputFolderWithStats, boolean dateMode) {
		this.monthlySchemaStatsCollection = new ArrayList<MonthSchemaStats>();
		String mssHeader = "mID\thumanTime\t#numCommits\t#numTables\t#numAttrs\ttablesInssSum\ttablesDelSum\tattrsInsWithTableInsSum"
				+ "\tattrsbDelWithTableDelSum\tattrsInjectedSum\tattrsEjectedSum\tattrsWithTypeUpdSum\tattrsInPKUpdSum\ttableDeltaSum"
				+ "\tattrDeltaSum\tattrBirthsSum\tattrDeathsSum\tattrUpdsSum\tTotalExpansion\tTotalMaintenance\tTotalAttrActivity\tReeds"
				+ "\tReedRatioAComm\tReedRatioTComm\tActivityDueToReeds\tTurfs\tTurfRatioAComm\tTurfRatioTComm\tActivityDueToTurfs\tActiveCommits\tActiveCommitRatio";
		createMonthlyAttributePositions(mssHeader);
		if (!dateMode)
			return this.monthlySchemaStatsCollection;
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		
		int mID = 0;
		YearMonth humanTime = YearMonth.parse(inputTupleCollection.get(0).getHumanTime(), dateFormatter);
		int numCommits = 1;
		
		int numTables = inputTupleCollection.get(0).getNumNewTables();
		int numAttrs = inputTupleCollection.get(0).getNumNewAttrs();
		
		int tablesInsertionsSum = inputTupleCollection.get(0).getTablesIns();
		int tablesDeletionsSum = inputTupleCollection.get(0).getTablesDel();
		int attrsInsWithTableInsSum = inputTupleCollection.get(0).getAttrsInsWithTableIns();
		int attrsbDelWithTableDelSum = inputTupleCollection.get(0).getAttrsbDelWithTableDel();
		int attrsInjectedSum = inputTupleCollection.get(0).getAttrsInjected();
		int attrsEjectedSum = inputTupleCollection.get(0).getAttrsEjected();
		int attrsWithTypeUpdSum = inputTupleCollection.get(0).getAttrsWithTypeUpd();
		int attrsInPKUpdSum = inputTupleCollection.get(0).getAttrsInPKUpd();
		
		int tableDeltaSum = inputTupleCollection.get(0).getTableDelta();
		int attrDeltaSum = inputTupleCollection.get(0).getAttrDelta();
		int attrBirthsSum = inputTupleCollection.get(0).getAttrBirthsSum();
		int attrDeathsSum = inputTupleCollection.get(0).getAttrDeathsSum();
		int attrUpdsSum = inputTupleCollection.get(0).getAttrUpdsSum();
		
		int totalExpansion = inputTupleCollection.get(0).getExpansion();
		int totalMaintenance = inputTupleCollection.get(0).getMaintenance();
		int totalAttrActivity = inputTupleCollection.get(0).getTotalAttrActivity();
		
		///
		int reeds = 0;
		int turfs = 0;
		int activeCommits = 0;
		double reedRatioAComm = 0.0;
		double reedRatioTComm = 0.0;
		double turfRatioAComm = 0.0;
		double turfRatioTComm = 0.0;
		double activeCommitRatio = 0.0;
		int activityDueToReeds = 0;
		int activityDueToTurfs = 0;
		
		if(inputTupleCollection.get(0).getIsReed() == 0) //if first commit is reed
		{
			reeds = 1;
			activityDueToReeds += inputTupleCollection.get(0).getTotalAttrActivity(); //we may not want to add the first total activity
		}
		if(inputTupleCollection.get(0).getIsTurf() == 0) //if first commit is turf
		{
			turfs = 1;
			activityDueToTurfs += inputTupleCollection.get(0).getTotalAttrActivity();
		}
		if(inputTupleCollection.get(0).getIsActive() == 0) //if first commit is active
		{
			activeCommits = 1;
		}
		///
		
		for(SchemaHeartbeatElement element: inputTupleCollection.subList(1, inputTupleCollection.size())) {
			YearMonth currentRunningMonth = YearMonth.parse(element.getHumanTime(), dateFormatter);
			
			if (humanTime.compareTo(currentRunningMonth) == 0) {	// update current fields
				numCommits ++;
				
				numTables = element.getNumNewTables();
				numAttrs = element.getNumNewAttrs();
				
				tablesInsertionsSum += element.getTablesIns();
				tablesDeletionsSum += element.getTablesDel();
				attrsInsWithTableInsSum += element.getAttrsInsWithTableIns();
				attrsbDelWithTableDelSum += element.getAttrsbDelWithTableDel();
				attrsInjectedSum += element.getAttrsInjected();
				attrsEjectedSum += element.getAttrsEjected();
				attrsWithTypeUpdSum += element.getAttrsWithTypeUpd();
				attrsInPKUpdSum += element.getAttrsInPKUpd();
				
				tableDeltaSum += element.getTableDelta();
				attrDeltaSum += element.getAttrDelta();
				attrBirthsSum += element.getAttrBirthsSum();
				attrDeathsSum += element.getAttrDeathsSum();
				attrUpdsSum += element.getAttrUpdsSum();
				
				totalExpansion += element.getExpansion();
				totalMaintenance += element.getMaintenance();
				totalAttrActivity += element.getTotalAttrActivity();
				
				///
				if(element.getIsReed() == 0)
				{
					reeds++;
					activityDueToReeds += element.getTotalAttrActivity();
				}
				if(element.getIsTurf() == 0)
				{
					turfs++;
					activityDueToTurfs += element.getTotalAttrActivity();
				}
				if(element.getIsActive() == 0)
				{
					activeCommits++;
				}
				///
				
			} else if (humanTime.compareTo(currentRunningMonth) < 0) {
				
				///
				if(activeCommits != 0)
				{
					turfRatioAComm = (double)turfs / activeCommits;
					turfRatioAComm =  Math.round(turfRatioAComm * 100.0) / 100.0;
					
					reedRatioAComm = 1 - turfRatioAComm;
				}
				
				if(numCommits != 0)
				{
					
					turfRatioTComm = (double)turfs / numCommits;
					turfRatioTComm = Math.round(turfRatioTComm * 100.0) / 100.0;
					
					reedRatioTComm = (double)reeds / numCommits;
					reedRatioTComm = Math.round(reedRatioTComm * 100.0) / 100.0;
					
					activeCommitRatio = (double)activeCommits / numCommits;
					activeCommitRatio =  Math.round(activeCommitRatio * 100.0) / 100.0;
				}
				///
				//System.out.println(turfs + " : " + numCommits);
				//System.out.println(turfRatio);
				this.monthlySchemaStatsCollection.add(new MonthSchemaStats(mID,humanTime.toString(),numCommits,numTables,numAttrs,
						tablesInsertionsSum,tablesDeletionsSum,attrsInsWithTableInsSum,attrsbDelWithTableDelSum,
						attrsInjectedSum,attrsEjectedSum,attrsWithTypeUpdSum,attrsInPKUpdSum,tableDeltaSum,attrDeltaSum,
						attrBirthsSum,attrDeathsSum,attrUpdsSum,totalExpansion,totalMaintenance,totalAttrActivity,reeds,reedRatioAComm,reedRatioTComm,activityDueToReeds,turfs,turfRatioAComm,turfRatioTComm,activityDueToTurfs,activeCommits,activeCommitRatio));
				
				// reset month stats
				mID ++;
				humanTime = humanTime.plusMonths(1);
				numCommits = 0;
				
				tablesInsertionsSum = 0;
				tablesDeletionsSum = 0;
				attrsInsWithTableInsSum = 0;
				attrsbDelWithTableDelSum = 0;
				attrsInjectedSum = 0;
				attrsEjectedSum = 0;
				attrsWithTypeUpdSum = 0;
				attrsInPKUpdSum = 0;
				
				tableDeltaSum = 0;
				attrDeltaSum = 0;
				attrBirthsSum = 0;
				attrDeathsSum = 0;
				attrUpdsSum = 0;
				
				totalExpansion = 0;
				totalMaintenance = 0;
				totalAttrActivity = 0;
				
				///
				reeds = 0;
				turfs = 0;
				turfRatioAComm = 0.0;
				turfRatioTComm = 0.0;
				reedRatioAComm = 0.0;
				reedRatioTComm = 0.0;
				activeCommits = 0;
				activeCommitRatio = 0.0;
				
				activityDueToReeds = 0;
				activityDueToTurfs = 0;
				///
				
				// add padding months
				while (humanTime.compareTo(currentRunningMonth) < 0) {
					this.monthlySchemaStatsCollection.add(new MonthSchemaStats(mID,humanTime.toString(),numCommits,numTables,numAttrs,
							tablesInsertionsSum,tablesDeletionsSum,attrsInsWithTableInsSum,attrsbDelWithTableDelSum,
							attrsInjectedSum,attrsEjectedSum,attrsWithTypeUpdSum,attrsInPKUpdSum,tableDeltaSum,attrDeltaSum,
							attrBirthsSum,attrDeathsSum,attrUpdsSum,totalExpansion,totalMaintenance,totalAttrActivity,reeds,reedRatioAComm,reedRatioTComm,activityDueToReeds,
							turfs,turfRatioAComm,turfRatioTComm,activityDueToTurfs,activeCommits,activeCommitRatio));
					mID ++;
					humanTime = humanTime.plusMonths(1);
				}
				
				// after done padding update current fields
				if (!humanTime.equals(YearMonth.parse(element.getHumanTime(), dateFormatter))) {
					System.out.println("Error calculating padding months!");
					this.monthlySchemaStatsCollection.clear();
					return this.monthlySchemaStatsCollection;
				}
				numCommits ++;
				
				numTables = element.getNumNewTables();
				numAttrs = element.getNumNewAttrs();
				
				tablesInsertionsSum += element.getTablesIns();
				tablesDeletionsSum += element.getTablesDel();
				attrsInsWithTableInsSum += element.getAttrsInsWithTableIns();
				attrsbDelWithTableDelSum += element.getAttrsbDelWithTableDel();
				attrsInjectedSum += element.getAttrsInjected();
				attrsEjectedSum += element.getAttrsEjected();
				attrsWithTypeUpdSum += element.getAttrsWithTypeUpd();
				attrsInPKUpdSum += element.getAttrsInPKUpd();
				
				tableDeltaSum += element.getTableDelta();
				attrDeltaSum += element.getAttrDelta();
				attrBirthsSum += element.getAttrBirthsSum();
				attrDeathsSum += element.getAttrDeathsSum();
				attrUpdsSum += element.getAttrUpdsSum();
				
				totalExpansion += element.getExpansion();
				totalMaintenance += element.getMaintenance();
				totalAttrActivity += element.getTotalAttrActivity();
				
				///
				if(element.getIsReed() == 0)
				{
					reeds++;
					activityDueToReeds += element.getTotalAttrActivity();
				}
				if(element.getIsTurf() == 0)
				{
					turfs++;
					activityDueToTurfs += element.getTotalAttrActivity();
				}
				if(element.getIsActive() == 0)
				{
					activeCommits++;
				}
				///
			}
		}
		
		///
		if(activeCommits != 0)
		{
			turfRatioAComm = (double)turfs / activeCommits;
			turfRatioAComm = Math.round(turfRatioAComm * 100.0) / 100.0;
			
			reedRatioAComm = 1 - turfRatioAComm;
		}
		
		if(numCommits != 0)
		{
			turfRatioTComm = (double)turfs / numCommits;
			turfRatioTComm = Math.round(turfRatioTComm * 100.0) / 100.0;
			
			reedRatioTComm = (double)reeds / numCommits;
			reedRatioTComm = Math.round(reedRatioTComm * 100.0) / 100.0;
			
			activeCommitRatio = (double)activeCommits / numCommits;
			activeCommitRatio =  Math.round(activeCommitRatio * 100.0) / 100.0;
		}
		///
		
		this.monthlySchemaStatsCollection.add(new MonthSchemaStats(mID,humanTime.toString(),numCommits,numTables,numAttrs,
				tablesInsertionsSum,tablesDeletionsSum,attrsInsWithTableInsSum,attrsbDelWithTableDelSum,
				attrsInjectedSum,attrsEjectedSum,attrsWithTypeUpdSum,attrsInPKUpdSum,tableDeltaSum,attrDeltaSum,
				attrBirthsSum,attrDeathsSum,attrUpdsSum,totalExpansion,totalMaintenance,totalAttrActivity,reeds,reedRatioAComm,reedRatioTComm,activityDueToReeds,turfs,turfRatioAComm,turfRatioTComm,activityDueToTurfs,activeCommits,activeCommitRatio));
		
		// save to tsv
		File monthlySchemaStatsTSVFile = new File(outputFolderWithStats + File.separator + prjName + "_MonthlySchemaStats.tsv");
		try {
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(monthlySchemaStatsTSVFile, false), StandardCharsets.UTF_8));
			writer.println(mssHeader);
			for (MonthSchemaStats mStats: this.monthlySchemaStatsCollection)
				writer.println(mStats.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.monthlySchemaStatsCollection;
	}
	
	private void createMonthlyAttributePositions(String header) {
		this.monthlyAttributePositions = new HashMap<String, Integer>();
		String[] attrNames = header.split("\t");
		for (int i = 0; i< attrNames.length; i++) {
			String nextAttr = attrNames[i];
			this.monthlyAttributePositions.put(nextAttr, i);
			if(_DEBUGMODE) System.out.print(nextAttr + "\t");
		}
		if(_DEBUGMODE) System.out.println();
	}

}//end class
