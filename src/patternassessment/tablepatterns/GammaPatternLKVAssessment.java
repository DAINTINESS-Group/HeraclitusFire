package patternassessment.tablepatterns;

import java.util.ArrayList;

import datamodel.TableDetailedStatsElement;
import patternassessment.fisher.exact.test.FisherExactTestWrapper;

public class GammaPatternLKVAssessment extends PatternAssessmentTemplateMethod {
	private static int WIDE_TABLE_LIMIT = 10;
	private static int MED_TABLE_LIMIT = 5;
	private static int MAX_ACCEPTABLE_NUM_WIDE_DEAD_FOR_PATTERN_TO_HOLD = 3;

	private Boolean geometricalPatternTrue;
	private Boolean pValuePatternTrue;
	
	public GammaPatternLKVAssessment(
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
	public String getTestName() {
		return "Gamma";
	}


	@Override
	public PatternAssessmentResult constructResult() {
		this.result = new PatternAssessmentResult(this.projectName + ":\tGamma", 2, 2); 
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
		//TODO: throw exception if both [1][0] and [1][1], i.e., the wide ones, are zeros
		return this.result.getContingencyTable();
	}


	/**
	 * Returns true/false on whether the Gamma pattern holds.
	 * 
	 * <p>1. Typically we used duration. We could approximated it with LKV.
	 * This means that you the survival/dead discrimination is close
	 * to the patterns meaning. 
	 *  <p>** We lose the top duration band, and we just test survival **
	 *  <p>
	 *  <p>2. In the contingency table, you 'd probably want one of the two scenarios:
	 *  <p>For contingency tables with small population, you want the "geometry" to work 
	 *  <p>- wide and surv. populated
	 *  <p>- wide and death empty
	 *  <p>
	 *  <p>For densely populated (where you probably gonna have many dead tables, incl., wide ones)
	 *  <p>- p(survive|wide) > p (survive|not wide)
	 *  <p>- p-value of Fisher/ChiSquare test for wide and notWide to be small 
	 *  
	 *  <p>We assign a true value to the pattern, if geometry OR stats is true.
	 *  
	 *  <p>For the Fisher test: our contingency table is
	 *  <pre>
	 *          dead | surv  
	 *          -----------
	 *  narrow |     |     |
	 *          -----------
	 *  wide   |     |     |
	 *          -----------
	 * </pre>          
	 *  So, the one-tail test has
	 *  <pre>
	 *    H0: prob(dead|narrow) <= prob(dead|wide)
	 *    Ha: prob(dead|narrow) > prob(dead|wide)
	 *  </pre>
	 *  <p>Therefore, we need the one-tail test, and, via a small p-Value, reject the Ho.
	 *  <p>See page 133 in "Statistics in a Nutshell", 2nd Ed., O'Reilly
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

		double pValueFisher = 1.0;
		pValueFisher = applyFisherTest(par);
		Boolean fisherTestPass = pValueFisher < this.alphaAcceptanceLevel;
		par.setFisherTestPass(fisherTestPass);
		
		if(par.getFisherTestExecuted())
			this.pValuePatternTrue = (probSurvIfWide > probSurvIfNotWide) && fisherTestPass;
		else
			this.pValuePatternTrue = (probSurvIfWide > probSurvIfNotWide);
		this.geometricalPatternTrue = (survivorsWide > deadWide) && (deadWide <= MAX_ACCEPTABLE_NUM_WIDE_DEAD_FOR_PATTERN_TO_HOLD);
				
		return (geometricalPatternTrue || pValuePatternTrue);
	}

	/**
	 * Executes the Fisher test and catches the exception if the cont. table cannot be new-ed well
	 * 
	 * @param par a PatternAssessmentResult with the matrices and results for the assessment of the pattern
	 * @return the pValue of the Fisher test, if the test is executed, 1.0 otherwise
	 */
	private double applyFisherTest(PatternAssessmentResult par) {
		int[][] contTable = par.getContingencyTable();
		
		double pValueFisher = 1.0;
		FisherExactTestWrapper fet;
		par.setFisherTestExecuted(true);
		try {
			fet = new FisherExactTestWrapper(contTable);
			pValueFisher = fet.getFisherSingleTailPValue();
			
		} catch (IllegalArgumentException e) {
			par.setFisherTestExecuted(false);
			pValueFisher = 1.0;
			par.setFisherTestPValue(pValueFisher);
		}
		par.setFisherTestPValue(pValueFisher);

		return pValueFisher;
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
		String resultString =  prjNPattern + "\t" + "Geometry? \t" + geometricalPatternTrue + "\n" + 
				prjNPattern + "\t" + "FisherExecuted? \t" + par.getFisherTestExecuted() + "\n" +
				prjNPattern + "\t" + "p-Value? \t" + pValuePatternTrue;
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
