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
	private int survivorsWide;
	private int deadWide;
	private int survivorsNotWide;
	private int deadNotWide;
	
	public enum GammaInvalidityReasons{
		NO_DEAD("Not applicable, the tables are all survivors"),
		NO_SURVIVORS("Not applicable, the tables are all dead"), 
		NO_WIDE("Not applicable, the tables are all naroow"), 
		NO_NARROW("Not applicable, the tables are all wide"),
		NEITHER_DEAD_OR_WIDE("Not applicable, the tables are all survivors and narrow"),
		NEITHER_DEAD_OR_NARROW("Not applicable, the tables are all survivors and wide"),
		NEITHER_SURVIVORS_OR_WIDE("Not applicable, all tables are dead and narrow"),
		NEITHER_SURVIVORS_OR_NARROW("Not applicable, the tables are all dead and wide");
		//MUST ADD ALL THE POSSIBLE REASONS OF INVALIDITY
		
		public String gammaInvalidityDetails;
		
		GammaInvalidityReasons(String gammaInvalidityDetails) {this.gammaInvalidityDetails = gammaInvalidityDetails;}
				
		public String toString()
		{
			return this.gammaInvalidityDetails;
		}
	}
	
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
		this.survivorsWide = 0;this.survivorsNotWide = 0; this.deadWide = 0;  this.deadNotWide = 0;
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
	public PatternAssessmentDecision decideIfPatternHolds(PatternAssessmentResult par) {
		int[][] contTable = this.result.getContingencyTable();
		this.survivorsWide = contTable[1][1];
		this.deadWide = contTable[1][0];
		this.survivorsNotWide = contTable[0][1];
		this.deadNotWide = contTable[0][0];
		int total = survivorsWide + deadWide + survivorsNotWide + deadNotWide;
		double persentageTableLimit = total * 0.05;
		if(deadWide == 0 && deadNotWide == 0 && survivorsWide == 0) {
			//all survivors and narrow
			PatternAssessmentDecision.NOT_APPLICABLE.details = GammaInvalidityReasons.NEITHER_DEAD_OR_WIDE.toString();
			return PatternAssessmentDecision.NOT_APPLICABLE;
		}
		if(deadWide == 0 && deadNotWide == 0 && survivorsNotWide == 0) {
			//all survivors and wide
			PatternAssessmentDecision.NOT_APPLICABLE.details = GammaInvalidityReasons.NEITHER_DEAD_OR_NARROW.toString();
			return PatternAssessmentDecision.NOT_APPLICABLE;
		}
		if(survivorsWide == 0 && survivorsNotWide == 0 && deadWide == 0) {
			//return all dead and narrow
			PatternAssessmentDecision.NOT_APPLICABLE.details = GammaInvalidityReasons.NEITHER_SURVIVORS_OR_WIDE.toString();
			return PatternAssessmentDecision.NOT_APPLICABLE;
		}
		if(survivorsWide == 0 && survivorsNotWide == 0 && deadNotWide == 0) {
			//all dead and Wide
			PatternAssessmentDecision.NOT_APPLICABLE.details = GammaInvalidityReasons.NEITHER_SURVIVORS_OR_NARROW.toString();
			return PatternAssessmentDecision.NOT_APPLICABLE;
		}
		if(deadWide == 0 && deadNotWide == 0) {
			//all Survivors
			PatternAssessmentDecision.NOT_APPLICABLE.details = GammaInvalidityReasons.NO_DEAD.toString();
			return PatternAssessmentDecision.NOT_APPLICABLE;
		}
		if(survivorsWide == 0 && survivorsNotWide == 0) {
			//all dead
			PatternAssessmentDecision.NOT_APPLICABLE.details = GammaInvalidityReasons.NO_SURVIVORS.toString();
			return PatternAssessmentDecision.NOT_APPLICABLE;
		}
		if(deadWide == 0 && survivorsWide == 0) {
			//all narrow
			PatternAssessmentDecision.NOT_APPLICABLE.details = GammaInvalidityReasons.NO_WIDE.toString();
			return PatternAssessmentDecision.NOT_APPLICABLE;
		}
		if(deadNotWide == 0 && survivorsNotWide == 0) {
			//all wide
			PatternAssessmentDecision.NOT_APPLICABLE.details = GammaInvalidityReasons.NO_NARROW.toString();
			return PatternAssessmentDecision.NOT_APPLICABLE;
		}
		if(deadWide > 0 && deadNotWide > 0 && survivorsWide > 0 && survivorsNotWide > 0) {
			if(deadWide < persentageTableLimit || deadNotWide < persentageTableLimit || survivorsNotWide < persentageTableLimit || survivorsWide < persentageTableLimit) {
				// start pattern evaluation			
				double probSurvIfWide = ((double)survivorsWide) / (survivorsWide + deadWide);
				double probSurvIfNotWide = ((double)survivorsNotWide) / (survivorsNotWide + deadNotWide);
				double probSurv = ((double)(survivorsWide + survivorsNotWide)) / (total);
		
				double pValueFisher = 1.0;
				pValueFisher = applyFisherTest(par);
				Boolean fisherTestPass = pValueFisher < this.alphaAcceptanceLevel;
				par.setFisherTestPass(fisherTestPass);
				
				if(par.getFisherTestExecuted())
					this.pValuePatternTrue = (probSurvIfWide > probSurvIfNotWide) && fisherTestPass;
				else
					this.pValuePatternTrue = (probSurvIfWide > probSurvIfNotWide);
				this.geometricalPatternTrue = (survivorsWide > deadWide) && (deadWide <= MAX_ACCEPTABLE_NUM_WIDE_DEAD_FOR_PATTERN_TO_HOLD);
				
				if(geometricalPatternTrue || pValuePatternTrue ) {
					PatternAssessmentDecision.SUCCESS.details = "success, the Gamma pattern holds";
					return PatternAssessmentDecision.SUCCESS;
				}
			}
		}
		PatternAssessmentDecision.FAILURE.details = "failure, the Gamma pattern doesn't holds";
		return PatternAssessmentDecision.FAILURE;
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
				prjNPattern + "\t" + "p-Value? \t" + pValuePatternTrue + "\n" +
				prjNPattern + "\t" + "Surv.Wide \t" + this.survivorsWide + "\n" +
				prjNPattern + "\t" + "Surv.NotWide \t" + this.survivorsNotWide + "\n" +
				prjNPattern + "\t" + "Dead.Wide \t" + this.deadWide + "\n" +
				prjNPattern + "\t" + "Dead.NotWide \t" + this.deadNotWide + "\n" +
				prjNPattern + "\t" + "Wide \t" + (this.survivorsWide +  this.deadWide);
		//System.out.println(" p|Wide: " + probSurvIfWide + "\t p |NotWide: " + probSurvIfNotWide + "\t pSurv" + probSurv);

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
