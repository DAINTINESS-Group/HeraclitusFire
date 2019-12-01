package patternassessment;

import java.util.ArrayList;

import datamodel.TableDetailedStatsElement;
import patternassessment.fisher.exact.test.FisherExactTestWrapper;

public class PatternGammaAssessment extends PatternAssessmentTemplateMethod {
	private static int WIDE_TABLE_LIMIT = 10;
	private static int MED_TABLE_LIMIT = 5;
	private static int MAX_ACCEPTABLE_NUM_WIDE_DEAD_FOR_PATTERN_TO_HOLD = 3;

	private Boolean geometricalPatternTrue;
	private Boolean pValuePatternTrue;
	
	public PatternGammaAssessment(
			ArrayList<TableDetailedStatsElement> pInputTupleCollection,
			String projectName,
			String pOutputFolderWithPatterns,
			String globalAppendLogPath,
			double alpha
			) {
		super(pInputTupleCollection, projectName, pOutputFolderWithPatterns, globalAppendLogPath, alpha);
		this.geometricalPatternTrue = false;
		this.pValuePatternTrue = false;
	}
	
	@Override
	public PatternAssessmentResult constructResult() {
		this.result = new PatternAssessmentResult(this.projectName + ":\tGammaTest", 2, 2); 
		return this.result;
	}

	@Override
	public int[][] computeContingencyTable(PatternAssessmentResult par) {
		for(TableDetailedStatsElement tuple: inputTupleCollection) {
			int survivalClass = tuple.getSurvivalClass(); //20 for surv. 10 for dead
			int schSizeAtBirth = tuple.getSchemaSizeBirth();
			int survPos = (survivalClass / 10) - 1; 
			int sizePos = 0;
			if (schSizeAtBirth > WIDE_TABLE_LIMIT)
				sizePos = 1;
			this.result.getContingencyTable()[sizePos][survPos]++;	
		}
		//TODO: throw exception if both [0][0] and [1][0], i.e., the wide ones, are zeros
		return this.result.getContingencyTable();
	}


	/**
	 * Returns true/false on whether the Gamma pattern holds.
	 * 
	 * 1. Typically we used duration. We could approximated it with LKV.
	 * This means that you the survival/dead discrimination is close
	 * to the patterns meaning. 
	 *  ** We lose the top duration band, and we just test survival **
	 *  
	 *  2. In the contingency table, you 'd probably want one of the two scenarios:
	 *  For contingency tables with small population, you want the "geometry" to work 
	 *  - wide and surv. populated
	 *  - wide and death empty
	 *  
	 *  For densely populated (where you probably gonna have many dead tables, incl., wide ones)
	 *  - p(survive|wide) > p (survive|not wide)
	 *  - p-value of Fisher/ChiSquare test for wide and notWide to be small 
	 *  
	 *  We assign a true value to the pattern, if geometry OR stats is true.
	 *  
	 *  For the Fisher test: our contingency table is
	 *          dead | surv  
	 *          -----------
	 *  narrow |     |     |
	 *          -----------
	 *  wide   |     |     |
	 *          -----------
	 *  So, the one-tail test has
	 *    H0: prob(dead|narrow) <= prob(dead|wide)
	 *    Ha: prob(dead|narrow) > prob(dead|wide)
	 *  Therefore, we need the one-tail test, and, via a small p-Value, reject the Ho.
	 *  See page 133 in "Statistics in a Nutshell", 2nd Ed., O'Reilly
	 *  
	 * @param par a PatternAssessmentResult with the matrices and results for the assessment of the pattern
	 * @return true if the pattern holds; false otherwise

	 */
	@Override
	public Boolean decideIfPatternHolds(PatternAssessmentResult par) {
		int[][] contTable = this.result.getContingencyTable();
		int survivorsWide = contTable[1][1];
		int deadWide = contTable[1][0];
		int survivorsNotWide = contTable[0][1];
		int deadNotWide = contTable[0][0];
		int total = survivorsWide + deadWide + survivorsNotWide + deadNotWide;
		
		
		double probSurvIfWide = ((double)survivorsWide) / (survivorsWide + deadWide);
		double probSurvIfNotWide = ((double)survivorsNotWide) / (survivorsNotWide + deadNotWide);
		double probSurv = ((double)(survivorsWide + survivorsNotWide)) / (total);
//System.out.println(" p|Wide: " + probSurvIfWide + "\t p |NotWide: " + probSurvIfNotWide + "\t pSurv" + probSurv);

		FisherExactTestWrapper fet = new FisherExactTestWrapper(contTable);
		double pValueFisher = fet.getFisherSingleTailPValue();
		par.setFisherTestPValue(pValueFisher);
		Boolean fisherTestPass = pValueFisher < this.alphaAcceptanceLevel;
		par.setFisherTestPass(fisherTestPass);
		
		this.geometricalPatternTrue = (survivorsWide > deadWide) && (deadWide <= MAX_ACCEPTABLE_NUM_WIDE_DEAD_FOR_PATTERN_TO_HOLD);
		this.pValuePatternTrue = (probSurvIfWide > probSurvIfNotWide) && fisherTestPass;
		
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
	@Override public String constructResultDescription(PatternAssessmentResult par) {
		String prjNPattern = par.getprjNameAndPattern();
		String resultString =  prjNPattern + "\n"  
				+ "Geometry holds? " + geometricalPatternTrue + "\n" 
				+ "p-Value  holds? " + pValuePatternTrue;
		return resultString;
	}

	@Deprecated
	public int[][] computeContingencyTableWithThreeCols(PatternAssessmentResult par) {

		for(TableDetailedStatsElement tuple: inputTupleCollection) {
			int survivalClass = tuple.getSurvivalClass(); //20 for surv. 10 for dead
			int schSizeAtBirth = tuple.getSchemaSizeBirth();
			int survPos = (survivalClass / 10) - 1; 
			int sizePos = -1;
			if (schSizeAtBirth > WIDE_TABLE_LIMIT)
				sizePos = 2;
			else if (schSizeAtBirth > MED_TABLE_LIMIT)
				sizePos = 1;
			else sizePos = 0;
			this.result.getContingencyTable()[sizePos][survPos]++;	
		}
		return this.result.getContingencyTable();
	}


	
}//end class
