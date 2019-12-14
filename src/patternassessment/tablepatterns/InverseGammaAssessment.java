package patternassessment.tablepatterns;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;

import datamodel.TableDetailedStatsElement;
import patternassessment.fisher.exact.test.FisherExactTestWrapper;


public class InverseGammaAssessment extends PatternAssessmentTemplateMethod {
	/**
	 * a table is considered long lived if its duration is 
	 * strictly larger than _PCT_MAX_DUR_FOR_LONGLIVED * maxDuration in the data set  
	 */
	private static double _PCT_MAX_DUR_FOR_LONGLIVED = 0.9;  

	/**
	 * a table is considered nonTriviallyActive via pct if its sumUpd is 
	 * strictly larger than _PCT_MAX_UPD_FOR_ACTIVE * maxSumUpd in the data set
	 * 
	 *   Yes, it is intentionally set low, e.g., at 15%, there are very few
	 *   tables above such limits
	 */
	private static double _PCT_MAX_UPD_FOR_ACTIVE = 0.15;
	
	/**
	 * a table is considered nonTriviallyActive via abs values if its sumUpd is 
	 * strictly larger than _ABS_UPD_FOR_ACTIVE
	 * 
	 *   Yes, it is intentionally set low, e.g., at 5 or 10 updates, there are very few
	 *   tables above such limits
	 */
	private static int _ABS_UPD_FOR_ACTIVE = 5;
	
	/**
	 * An area is considered empty if it has less or equal than _PCT_EMPTY_AREA_THRESHOLD * total number of tables 
	 */
	private static double _PCT_EMPTY_AREA_THRESHOLD = 0.15;
	
	/**
	 * maximum duration in the data set (in version id's)
	 */
	private int maxDuration;
	
	/**
	 * maximum sum of updates in the data set
	 */
	private int maxSumUpd;
	
	private int numTables;
	private Boolean geometricalPatternTrue;
	private Boolean pValuePatternTrue;
	
	public InverseGammaAssessment(ArrayList<TableDetailedStatsElement> pInputTupleCollection, String projectName,
			String pOutputFolderWithPatterns, String globalAppendLogPath, double alpha) {
		super(pInputTupleCollection, projectName, pOutputFolderWithPatterns, globalAppendLogPath, alpha);
		maxDuration = 0;
		maxSumUpd = 0;
		this.geometricalPatternTrue = false;
		this.pValuePatternTrue = false;
		this.numTables = this.inputTupleCollection.size();
	}//end constructor

	@Override	public String getTestName() {
		return "InverseGamma";
	}

	@Override public PatternAssessmentResult constructResult() {
		this.result = new PatternAssessmentResult(this.projectName + ":\t"+this.getTestName(), 2, 2); 
		return this.result;
	}

	/**
	 * Computes the contingency table for the Inverse Gamma pattern assessment
	 * 
	 * <p>The assessment compares the relationship between duration (v.id) with sum of updates (simple sum of upd occurrences).
	 * The main idea of the pattern is that there are not really many tables that are both high update and of notLong duration.
	 * 
	 * <p>The contingency table is
	 *  <pre>
	 *              highUpd | notHighUpd  
	 *            --------------------
	 *  highDur    |        |        |
	 *            --------------------
	 *  notHighDur |        |        |
	 *            --------------------	            
	 * </pre>          
	 *  The test for duration is that > 90% of max Duration makes you of highDur.
	 *  The tests for update volume to be considered high is 
	 *  (a) either strictly higher that _ABS_UPD_FOR_ACTIVE or 
	 *  (b) strictly higher than  _PCT_MAX_UPD_FOR_ACTIVE * maxSumUpd
	 *  
	 * @param par  a PatternAssessmentResult that will be populated with the contingency table and the test results
	 * @return a int[2][2]  contingency table, populated on the basis of the pattern 
	 */
	@Override	public int[][] computeContingencyTable(PatternAssessmentResult par) {
		IntSummaryStatistics durationStats = this.inputTupleCollection.stream()
		.collect(Collectors.summarizingInt(TableDetailedStatsElement::getDuration));	
		this.maxDuration = durationStats.getMax();

		IntSummaryStatistics sumUpdStats = this.inputTupleCollection.stream()
		.collect(Collectors.summarizingInt(TableDetailedStatsElement::getSumupd));	
		this.maxSumUpd = sumUpdStats.getMax();
		
		for(TableDetailedStatsElement tuple: inputTupleCollection) {
			int duration = tuple.getDuration();
			int sumUpd = tuple.getSumupd();
			int rowPos = 0; int colPos = 0;
			if (duration  <= _PCT_MAX_DUR_FOR_LONGLIVED * maxDuration){
				rowPos = 1;
			}
			if (sumUpd <= _PCT_MAX_UPD_FOR_ACTIVE * maxSumUpd) {
				colPos = 1;
			}
			this.result.getContingencyTable()[rowPos][colPos]++;
		}
		return this.result.getContingencyTable();
	}

	/**
	 * Returns true/false on whether the Inverse Gamma pattern holds.
	 *
	 * <p>The geometrical pattern holds if the cell (contTable[1][0]) of lowDuration and highUpdates is empty.
	 * Remember this holds at least 1 - _PCT_MAX_DUR_FOR_LONGLIVED of the duration range and depending on the test for updates, 
	 * either 1 - _PCT_MAX_UPD_FOR_ACTIVE of the sumUpd range or, and tables with updates less than  _ABS_UPD_FOR_ACTIVE, 
	 * i.e., a really large area of the space duration X sumUpd. We consider it empty if it has less than _PCT_EMPTY_AREA_THRESHOLD
	 * of the tables
	 * 
	 * <p>The Fisher test is simply the statistical test over the contingency table.
 	 * <p>The contingency table is
	 *  <pre>
	 *              highUpd | notHighUpd  
	 *            --------------------
	 *  highDur    |        |        |
	 *            --------------------
	 *  notHighDur |        |        |
	 *            --------------------	            
	 * </pre>    
	 *  So, the one-tail test has
	 *  <pre>
	 *    H0: prob(highUpd|highDur) <= prob(highUpd|notHighDur)
	 *    Ha: prob(highUpd|highDur) > prob(highUpd|notHighDur)
	 *  </pre>
	 *  <p>Therefore, we need the one-tail test, and, via a small p-Value, reject the Ho.
	 *  <p>See page 133 in "Statistics in a Nutshell", 2nd Ed., O'Reilly
	 *     
 	 * @param par  a PatternAssessmentResult that will be populated with the contingency table and the test results
 	 * @return a Boolean flag that is true if the pattern holds; false otherwise
	 */
	@Override public Boolean decideIfPatternHolds(PatternAssessmentResult par) {
		int[][] contTable = this.result.getContingencyTable();
		
		if (contTable[1][0] <= _PCT_EMPTY_AREA_THRESHOLD * this.numTables)
			this.geometricalPatternTrue = true;
		
		FisherExactTestWrapper fet = new FisherExactTestWrapper(contTable);
		double pValueFisher = fet.getFisherSingleTailPValue();
		par.setFisherTestPValue(pValueFisher);
		Boolean fisherTestPass = pValueFisher < this.alphaAcceptanceLevel;
		
		this.pValuePatternTrue = fisherTestPass;
		par.setFisherTestPass(fisherTestPass);

		
		return (geometricalPatternTrue || pValuePatternTrue);
	}

	/**
	 * Returns a String with the description of the pattern.
	 * 
	 *  Specifically, the result holds prjName and pattern, whether the geometrical pattern holds and whether the statistical pattern holds
	 * 
	 * @param par a PatternAssessmentResult with the matrices and results for the assessment of the pattern
	 * @return a String with the description of the pattern
	 */
	@Override	public String constructResultDescription(PatternAssessmentResult par) {
		String prjNPattern = par.getprjNameAndPattern();
		String resultString =  prjNPattern + "\t" + "Geometry? \t" + geometricalPatternTrue + "\n" + 
				prjNPattern + "\t" + "p-Value? \t" + pValuePatternTrue;
		return resultString;
	}

}//end class
