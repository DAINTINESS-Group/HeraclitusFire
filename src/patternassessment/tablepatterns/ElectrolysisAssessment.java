package patternassessment.tablepatterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;

import datamodel.TableDetailedStatsElement;
import patternassessment.fisher.exact.test.FisherExactTestWrapper;

public class ElectrolysisAssessment extends PatternAssessmentTemplateMethod {
	/**
	 * a table's duration (in days) is considered high if it is 
	 * equal or larger than _PCT_HIGH_DURATION_LIMIT * maxDuration in the data set 
	 */
	private static double _PCT_HIGH_DURATION_LIMIT = 0.8;
	
	/**
	 * a table's duration (in days) is considered medium if it is 
	 * equal or larger than _PCT_MED_DURATION_LIMIT * maxDuration and 
	 * smaller than _PCT_HIGH_DURATION_LIMIT * maxDuration in the data set 
	 * 
	 * a table's duration (in days) is considered low if it is 
	 * smaller than _PCT_MED_DURATION_LIMIT * maxDuration in the data set 
	 */
	private static double _PCT_MED_DURATION_LIMIT = 0.2;
	
	/**
	 * Low duration - dead.rigid have to be equal or more than _PCT_DEAD_RIGID_THRESHOLD * dead.rigid 
	 */
	// TODO: yet to be decided
	private static double _PCT_RIGID_DEAD_THRESHOLD = 0.8;
	
	/**
	 * High duration - survivor.active have to be equal or more than _PCT_DEAD_RIGID_THRESHOLD * survivor.active 
	 */
	private static double _PCT_ACTIVE_SURVIVOR_THRESHOLD = 0.8;
	
	/**
	 * maximum duration in the data set (in days)
	 */
	private int maxDuration;
	
	private int numTables;
	private Boolean geometricalPatternTrue;
	private Boolean pValuePatternTrue;

	public ElectrolysisAssessment(ArrayList<TableDetailedStatsElement> pInputTupleCollection, String projectName,
			String pOutputFolderWithPatterns, String globalAppendLogPath, double alpha) {
		super(pInputTupleCollection, projectName, pOutputFolderWithPatterns, globalAppendLogPath, alpha);
		this.maxDuration = 0;
		this.geometricalPatternTrue = false;
		this.pValuePatternTrue = false;
		this.numTables = this.inputTupleCollection.size();
	}

	@Override
	public String getTestName() {
		return "Electrolysis";
	}

	@Override
	public PatternAssessmentResult constructResult() {
		this.result = new PatternAssessmentResult(this.projectName + ":\t"+this.getTestName(), 3, 6);
		return this.result;
	}

	/**
	 * Computes the contingency table for the Electrolysis pattern assessment.
	 * 
	 * <p>The assessment compares the relationship between duration (days) with LifeAndDeath class.
	 * The main idea of the pattern is that dead tables are of low duration but survivor tables are of high duration 
	 * and also that the more active a table is the higher its duration will be and so the chance of survival.
	 * 
	 * <p>The contingency table is: 
	 * <pre>
	 *              R.Dead | Q.Dead | A.Dead | R.Surv | Q.Surv | A.Surv
	 *           ---------------------------------------------------------
	 * lowDur     |        |        |        |        |        |        |
	 *           ---------------------------------------------------------
	 * medDur     |        |        |        |        |        |        |
	 *           ---------------------------------------------------------
	 * highDur    |        |        |        |        |        |        |
	 *           ---------------------------------------------------------
	 * </pre>
	 *  The test for duration is that < 20% of max Duration makes you of lowDur, 
	 *  >= 20% and < 80% of max Duration makes you of medDur and >= 80% of max Duration makes you of highDur.
	 * 
	 * @param par  a PatternAssessmentResult that will be populated with the contingency table and the test results
	 * @return a int[3][6]  contingency table, populated on the basis of the pattern
	 */
	@Override
	public int[][] computeContingencyTable(PatternAssessmentResult par) {
		IntSummaryStatistics durationStats = this.inputTupleCollection.stream()
		.collect(Collectors.summarizingInt(TableDetailedStatsElement::getDurationDays));	
		this.maxDuration = durationStats.getMax();
		
		for(TableDetailedStatsElement tuple: inputTupleCollection) {
			int duration = tuple.getDurationDays();
			int ladclass = tuple.getLadclass();
			int rowPos = 0;
			int colPos = (((ladclass / 10) - 1) * 3) + (ladclass % 10);
			if (duration  >= _PCT_MED_DURATION_LIMIT * maxDuration && duration  < _PCT_HIGH_DURATION_LIMIT * maxDuration){
				rowPos = 1;
			} else if (duration  >= _PCT_HIGH_DURATION_LIMIT * maxDuration) {
				rowPos = 2;
			}
			this.result.getContingencyTable()[rowPos][colPos]++;
		}
		return this.result.getContingencyTable();
	}

	/**
	 * Returns true/false on whether the Electrolysis pattern holds.
	 *
	 *  <p>The geometrical pattern holds if: 
	 *  <p>- most of dead tables are rigid (rigid.dead >= quiet.dead + active.dead)
	 *  <p>- most of survivors are of high duration [p(surv|highDur) >= p(surv|lowDur) + p(surv|medDur)]
	 *  <p>- most of rigid.dead tables are of low duration (contTable[0][0] >= _PCT_RIGID_DEAD_THRESHOLD * rigid.dead)
	 *  <p>- most of active.surv tables are of high duration (contTable[2][5] >= _PCT_ACTIVE_SURVIVOR_THRESHOLD * active.surv)
	 * 
	 * <p>We execute the Fisher's exact test on three different contingency tables, subtables of the original: 
	 * <p>The contingency table for the first test is: 
	 * <pre>
	 *              Dead | Surv 
	 *           -----------------
	 * lowDur     |      |      |
	 *           -----------------
	 * notLowDur  |      |      |
	 *           -----------------
	 * </pre>
	 * <p>The first test has: 
	 * <pre>
	 *   H0: duration and survival class are independent
	 *   Ha: dead tables die very soon
	 * </pre>
	 * <p>The contingency table for the second test is: 
	 * <pre>
	 *              Dead | Surv 
	 *           -----------------
	 * notHighDur |      |      |
	 *           -----------------
	 * highDur    |      |      |
	 *           -----------------
	 * </pre>
	 * <p>The second test has: 
	 * <pre>
	 *   H0: duration and survival class are independent
	 *   Ha: survivor tables live very long
	 * </pre>
	 * <p>The contingency table for the third test is: 
	 * <pre>
	 *              NotA.Surv |   A.Surv 
	 *           ---------------------------
	 * notHighDur |           |           |
	 *           ---------------------------
	 * highDur    |           |           |
	 *           ---------------------------
	 * </pre>
	 * <p>The third test has: 
	 * <pre>
	 *   H0: duration and activity class of survivor tables are independent
	 *   Ha: active survivor tables live very long
	 * </pre>
	 * <p>Therefore, we need the tests, and, via a small p-Values, reject the H0s.
	 * <p>See pages 127 and 133 in "Statistics in a Nutshell", 2nd Ed., O'Reilly
	 * 
 	 * @param par  a PatternAssessmentResult that will be populated with the contingency table and the test results
 	 * @return a Boolean flag that is true if the pattern holds; false otherwise
	 */
	@Override
	public Boolean decideIfPatternHolds(PatternAssessmentResult par) {
		int[][] contTable = this.result.getContingencyTable();
		int survHigh = contTable[2][3] + contTable[2][4] + contTable[2][5];
		int survMed = contTable[1][3] + contTable[1][4] + contTable[1][5];
		int survLow = contTable[0][3] + contTable[0][4] + contTable[0][5];
		
		int rigidDead = contTable[0][0] + contTable[1][0] + contTable[2][0];
		int quietDead = contTable[0][1] + contTable[1][1] + contTable[2][1];
		int activeDead = contTable[0][2] + contTable[1][2] + contTable[2][2];
		int activeSurv = contTable[0][5] + contTable[1][5] + contTable[2][5];
		
		boolean areRigidDeadMajority = rigidDead >= quietDead + activeDead;
		boolean areMostSurvHighDur = survHigh >= survLow + survMed;
		boolean areMostRigidDeadLowDur = contTable[0][0] >= _PCT_RIGID_DEAD_THRESHOLD * rigidDead;
		boolean areMostActiveSurvHighDur = contTable[2][5] >= _PCT_ACTIVE_SURVIVOR_THRESHOLD * activeSurv;
		
//		System.out.println("areRigidDeadMajority:\t\t" + areRigidDeadMajority);
//		System.out.println("areMostRigidDeadLowDur:\t\t" + areMostRigidDeadLowDur);
//		System.out.println("areMostActiveSurvHighDur:\t\t" + areMostActiveSurvHighDur);
//		System.out.println("areMostSurvHighDur:\t\t" + areMostSurvHighDur);
		
		ArrayList<int[][]> contTables2x2 = new ArrayList<int[][]>(
				Arrays.asList(new int[2][2], new int[2][2], new int[2][2]));
		ArrayList<Double> pValuesFisher = new ArrayList<Double>();
		Boolean fisherTestsPass = true;
		
		contTables2x2.get(0)[0][0] = contTable[0][0] + contTable[0][1] + contTable[0][2];
		contTables2x2.get(0)[0][1] = contTable[0][3] + contTable[0][4] + contTable[0][5];
		contTables2x2.get(0)[1][0] = contTable[1][0] + contTable[1][1] + contTable[1][2] + 
				contTable[2][0] + contTable[2][1] + contTable[2][2];
		contTables2x2.get(0)[1][1] = contTable[1][3] + contTable[1][4] + contTable[1][5] + 
				contTable[2][3] + contTable[2][4] + contTable[2][5];
		
		contTables2x2.get(1)[0][0] = contTable[0][0] + contTable[0][1] + contTable[0][2] + 
				contTable[1][0] + contTable[1][1] + contTable[1][2];
		contTables2x2.get(1)[0][1] = contTable[0][3] + contTable[0][4] + contTable[0][5] + 
				contTable[1][3] + contTable[1][4] + contTable[1][5];
		contTables2x2.get(1)[1][0] = contTable[2][0] + contTable[2][1] + contTable[2][2];
		contTables2x2.get(1)[1][1] = contTable[2][3] + contTable[2][4] + contTable[2][5];
		
		contTables2x2.get(2)[0][0] = contTable[0][3] + contTable[0][4] + contTable[1][3] + contTable[1][4];
		contTables2x2.get(2)[0][1] = contTable[0][5] + contTable[1][5];
		contTables2x2.get(2)[1][0] = contTable[2][3] + contTable[2][4];
		contTables2x2.get(2)[1][1] = contTable[2][5];

		double pValueFisher;
		par.setFisherTestExecuted(true);
		for (int[][] contTable2x2: contTables2x2) {
			pValueFisher = applyFisherTest(contTable2x2);
			pValuesFisher.add(pValueFisher);
			if (pValueFisher == 1.0)
				par.setFisherTestExecuted(false);
			par.setFisherTestPValue(pValueFisher);
			fisherTestsPass = fisherTestsPass && pValueFisher < this.alphaAcceptanceLevel;
		}
		this.pValuePatternTrue = fisherTestsPass;
		par.setFisherTestExecuted(fisherTestsPass);
		
		this.geometricalPatternTrue = areRigidDeadMajority && areMostSurvHighDur && 
				areMostRigidDeadLowDur && areMostActiveSurvHighDur; 
		
		return (geometricalPatternTrue || pValuePatternTrue);
	}
	
	/**
	 * Executes the Fisher tests and catches the exception if the cont. tables cannot be new-ed well
	 * 
	 * @param contTable2x2 a 2x2 cont. table for the Fisher test
	 * @return the pValues of the Fisher test, if the test is executed, 1.0 otherwise
	 */
	private double applyFisherTest(int[][] contTable2x2) {
		double pValueFisher;
		FisherExactTestWrapper fet;
		try {
			fet = new FisherExactTestWrapper(contTable2x2);
			pValueFisher = fet.getFisherSingleTailPValue();
		} catch (IllegalArgumentException e) {
			pValueFisher = 1.0;
		}

		return pValueFisher;
	}

	/**
	 * Returns a String with the description of the pattern.
	 * 
	 * Specifically, the result holds prjName and pattern, whether the geometrical pattern holds and whether the statistical pattern holds
	 * 
	 * @param par a PatternAssessmentResult with the matrices and results for the assessment of the pattern
	 * @return a String with the description of the pattern
	 */
	@Override
	public String constructResultDescription(PatternAssessmentResult par) {
		String prjNPattern = par.getprjNameAndPattern();
		String resultString =  prjNPattern + "\t" + "Geometry? \t" + geometricalPatternTrue + "\n" + 
				prjNPattern + "\t" + "FisherExecuted? \t" + par.getFisherTestExecuted() + "\n" +
				prjNPattern + "\t" + "p-Value? \t" + pValuePatternTrue;
		return resultString;
	}

}//end class
