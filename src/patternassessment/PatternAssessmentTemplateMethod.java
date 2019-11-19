package patternassessment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
//import java.util.HashMap;

import org.apache.commons.math3.stat.inference.ChiSquareTest;

import datamodel.TableDetailedStatsElement;

public abstract class PatternAssessmentTemplateMethod {
	protected ArrayList<TableDetailedStatsElement> inputTupleCollection;
	//	private	HashMap<String, Integer> attributePositions;
	//	private	HashMap<Integer, ArrayList<TableDetailedStatsElement>> tuplesPerLADCollection;
	protected	String outputFolderWithPatterns;
	protected PatternAssessmentResult result;
	protected double alphaAcceptanceLevel;
	protected Boolean patternIsValid;
	protected String projectName;


	public abstract PatternAssessmentResult constructResult();
	public abstract int[][] computeContingencyTable(PatternAssessmentResult par);
	public abstract Boolean decideIfPatternHolds(PatternAssessmentResult par);

	public PatternAssessmentTemplateMethod(
			ArrayList<TableDetailedStatsElement> pInputTupleCollection,
			//HashMap<String, Integer> pAttributePositions,
			//HashMap<Integer, ArrayList<TableDetailedStatsElement>> pTuplesPerLADCollection,
			String projectName,
			String pOutputFolderWithPatterns,
			double alpha
			) {
		this.inputTupleCollection = pInputTupleCollection;
		//	attributePositions = pAttributePositions;
		//	tuplesPerLADCollection = pTuplesPerLADCollection;
		this.outputFolderWithPatterns = pOutputFolderWithPatterns;
		this.projectName = projectName;
		this.alphaAcceptanceLevel = alpha;
		this.patternIsValid = false;
	}//end constructor

	/**
	 * Returns true of the pattern holds, false otherwise
	 * 
	 * The essence of the template method pattern applied here is in this method
	 * that executes the main algo for assessing the pattern.
	 * 
	 * 1. Construct result (implemented at concrete subclasses) = new the PatternAssessmentResult
	 *    with the appropriate size for rows and columns of the contingency table
	 * 2. construct (here) the respective matrices with marginals and probabilities
	 * 3. decide if pattern holds (implemented at concrete subclasses)
	 * 4. write result at a PrintStream
	 * 5. append result to a global file, st. this can be used for mass execution over multiple projects/
	 * 
	 * @return true of the pattern holds, false otherwise
	 */
	public Boolean assessPattern() {
		//prepare matrixes -- customized for the base contingency table
		this.result = constructResult(); //child classes must have attributes for descr, #cols, #rows
		
		//Local variables are Unused, but this.result is populated
		int [][] contingencyTable = computeContingencyTable(this.result);
		
		//int rows = this.result.getContingencyNumRows();
		//int cols = this.result.getContingencyNumColumns();
		//int [][] contingencyMarginals = this.computeContingencyWithMarginals(this.result);
		//double[][] pctsOverTotal = this.computePercentagesWithMarginals(this.result);

//		//run std statistical tests -- INCLUDED IN THE decideIfPatternHolds!
//		boolean passedChiSq = this.runChiSquareTests(this.result, this.alphaAcceptanceLevel);
//		boolean passedFisher = this.runFisherExactTest(this.result, this.alphaAcceptanceLevel);

		//take a customized decision
		this.patternIsValid = this.decideIfPatternHolds(this.result);
		this.result.setPatternHolds(this.patternIsValid);

		//output the results
		this.writeToResultFile(result, System.out);
		
		//TODO: once happy, uncomment the following
		//this.writeToResultFile(result);
		//TODO: decide how to handle globalFilePath && fix the appendTo.. method()
		//this.appendToGlobalLogFile(result, "XX globalFilePath XX");
		return this.patternIsValid;
	}

	private final int[][] computeContingencyWithMarginals(PatternAssessmentResult par){
		int [][] counters= par.getContingencyTable();
		int [][] countersExtended = par.getContingencyTableWithMarginals();
		int rows = par.getContingencyNumRows();
		int cols = par.getContingencyNumColumns();

		int totalNumTuples = 0;
		for(int i = 0; i < rows; i++) {
			int rowSum = 0;
			for(int j=0; j < cols; j++) {
				countersExtended[i][j] = counters[i][j];
				rowSum += counters[i][j];
			}
			countersExtended[i][cols] = rowSum;
			totalNumTuples += rowSum;
		}
		countersExtended[rows][cols] = totalNumTuples;

		for (int j=0; j < cols; j++){
			int colSum = 0;
			for(int i = 0; i < rows; i++) {
				colSum += counters[i][j];
			}
			countersExtended[rows][j] = colSum;
		}		
		return countersExtended;
	}

	private final double[][] computePercentagesWithMarginals(PatternAssessmentResult par){
		int [][] countersExtended = par.getContingencyTableWithMarginals();
		double [][] pctsOverMarginals = par.getPercentageTableWithMarginals();
		int rows = par.getContingencyNumRows() + 1;
		int cols = par.getContingencyNumColumns() + 1;
		int totalNumTuples = countersExtended[rows-1][cols-1];

		for(int i = 0; i < rows; i++) {
			for(int j=0; j < cols; j++) {
				pctsOverMarginals[i][j] = ((double) countersExtended[i][j])/totalNumTuples;
			}
		}
		return pctsOverMarginals;
	}

	/**
	 * A method to perform a Chi Square Test and output the result to a PrintStream
	 * 
	 * @param par A PatternAssessmentResult holding the contingency matrix with the counted values for the different classes
	 * @param alphaAcceptanceLevel A double with the $\alpha$ level to decide if the test is passed or not
	 * @return a Boolean value reporting whether the test is passed or not
	 * 
	 * See:
	 * https://www.itl.nist.gov/div898/handbook/eda/section3/eda35f.htm
	 * https://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/stat/inference/ChiSquareTest.html
	 * http://commons.apache.org/proper/commons-math/userguide/stat.html#a1.8_Statistical_tests
	 * https://github.com/apache/commons-math/blob/master/src/test/java/org/apache/commons/math4/stat/inference/ChiSquareTestTest.java
	 * 
	 */
	protected final Boolean runChiSquareTests(PatternAssessmentResult par, double alphaAcceptanceLevel) {
		int[][] counts = par.getContingencyTable();
		int numRows = counts.length;
		int numCols = counts[0].length;
		long[][] countsLong = new long[numRows][numCols];
		for(int i = 0 ; i < numRows; i++)
			for (int j=0; j< numCols; j++)
				countsLong[i][j] = counts[i][j];
		
		ChiSquareTest testStatistic = new ChiSquareTest();
		//Double resultRaw = testStatistic.chiSquare(counts); 
		Double pValue = testStatistic.chiSquareTest(countsLong); 
		Boolean pTest = testStatistic.chiSquareTest(countsLong, alphaAcceptanceLevel);
		par.setChiSquareTestPValue(pValue);
		par.setChiSquareTestPass(pTest);
		return pTest;
	}//end runChiSquareTests


	protected final Boolean runFisherExactTest(PatternAssessmentResult par, double alphaAcceptanceLevel) {
		double prob = computeSingleProbFisherExactTest(par);
		double pValue = 1.0;
		Boolean pTest = false;
		
		//TODO: compute more extreme Fisher tests and add prob's to get a pvalue
		
		if (pValue < alphaAcceptanceLevel)
			pTest = true;
		par.setFisherTestPass(pTest);

		return pTest;
	}
	
	/**
	 * Computes the probability for the Fisher Test for the given contingency table
	 * 
	 * @param par the PatternAssessmentResult with its cont. tables with marginals computed
	 * @return a double with the prob of the Fisher test
	 */
	protected final double computeSingleProbFisherExactTest(PatternAssessmentResult par) {
//		int rows = par.getContingencyNumRows();
//		int cols = par.getContingencyNumColumns();
//		int counts[][] = new int[rows+1][cols+1]; 
//		for (int i=0;i<rows+1;i++)
//			for (int j=0;j<cols+1;j++) {
//				counts[i][j] = par.getContingencyTableWithMarginals()[i][j];
//				System.out.println(i+"\t"+j+"\t"+counts[i][j]);
//			}
//		double prob = this.computeFisherExactTest(counts);
		double prob = this.computeFisherExactTest(par.getContingencyTableWithMarginals());
		par.setFisherTestPValue(prob);
		return prob;
	}

	/**
	 * Fisher's exact test requires a 3x3 table =  a 2x2 table with marginals
	 * 
	 * a	b	r1			//r1=(a+b)
	 * c	d	r2			//r2=(c+d)
	 * c1	c2	s			//c1=(a+c), c2=(b+d) s=a+b+c+d
	 * 
	 */
	private double computeFisherExactTest(int counts[][]) {
		//cannot work with long, have to use int[][]
		double aF = org.apache.commons.math3.util.CombinatoricsUtils.factorialDouble(counts[0][0]);
		double bF = org.apache.commons.math3.util.CombinatoricsUtils.factorialDouble(counts[0][1]);
		double cF = org.apache.commons.math3.util.CombinatoricsUtils.factorialDouble(counts[1][0]);
		double dF = org.apache.commons.math3.util.CombinatoricsUtils.factorialDouble(counts[1][1]);
		double r1F = org.apache.commons.math3.util.CombinatoricsUtils.factorialDouble(counts[0][2]);
		double r2F = org.apache.commons.math3.util.CombinatoricsUtils.factorialDouble(counts[1][2]);
		double c1F = org.apache.commons.math3.util.CombinatoricsUtils.factorialDouble(counts[2][0]);
		double c2F = org.apache.commons.math3.util.CombinatoricsUtils.factorialDouble(counts[2][1]);
		double sF = org.apache.commons.math3.util.CombinatoricsUtils.factorialDouble(counts[2][2]);

		//System.out.println(r1F * r2F * c1F * c2F);
		//System.out.println(sF * aF * bF * cF * dF);
		double pValue = (r1F * r2F * c1F * c2F) /(sF * aF * bF * cF * dF);
		return pValue;
	}

	private void writeToResultFile(PatternAssessmentResult par, PrintStream outStream) {
		outStream.println("\nNEW PRJ ----------- " + this.projectName + " -------------------------");
		outStream.println("test: " + "\t" + par.getDescription());
		outStream.println("holds? " + "\t" + this.patternIsValid);
		//outStream.println( "chi-square test statistic: " + "\t" + resultRaw );
		
		//Not useful for small numbers
		//outStream.println("chi-square test p-value: " + "\t" + par.getChiSquareTestPValue());
		//outStream.println("chi-square test accept?: " + "\t" +  par.getChiSquareTestPass());

		//Not done yet
		//outStream.println("fisher test p-value: " + "\t" + par.getFisherTestPValue());
		//outStream.println("fisher test accept?: " + "\t" +  par.getFisherTestPass());
		outStream.println("------------------------------------------------");
		return;
	}

	/**
	 * This method outputs the results to a result file for the data set.
	 * The name of the file depends on the type of the check
	 * Use PrintStream due to the need to use this.writeToResultFile
	 * which is constructed generically, s.t. we can output to sysout
	 * 
	 * @param par a PatternAssessmentResult with the computed pattern test results
	 */
	private void writeToResultFile(PatternAssessmentResult par){
		String filePath = outputFolderWithPatterns + "/" 
				+ this.result.getDescription() +
				".txt"; 

		FileOutputStream fileOutputStream=null;
		PrintStream printStream=null; 
		try{ 
			fileOutputStream = new FileOutputStream(new File(filePath)); 
			printStream=new PrintStream(fileOutputStream);

			this.writeToResultFile(result, printStream);

		} catch (Exception e) { 
			System.out.println("[PatternAssessmentTemplateMethod] There was a problem creating/writing to the file: " + filePath);
			e.printStackTrace(); 
		}
		finally { 
			try {
				if(fileOutputStream!=null){ 
					fileOutputStream.close(); 
				}
				if(printStream!=null){ 
					printStream.close(); 
				} 
			} catch (Exception e) {
				System.out.println("[PatternAssessmentTemplateMethod] There was a problem closing file: " + filePath);
				e.printStackTrace(); 
			} 
		}//finally
	}//end method

	/**
	 * This method appends to a global log file of results
	 * TODO not done yet
	 * 
	 * Use buffered output
	 * 
	 * @param par a PatternAssessmentResult with the computed pattern test results
	 * @param filePath a String with the path of the file to which we append
	 */
	private void appendToGlobalLogFile(PatternAssessmentResult par, String filePath){ File file = new File(filePath);
		FileWriter fr = null;
		BufferedWriter br = null;
		PrintWriter pr = null;
		try {
			fr = new FileWriter(file, true);
			br = new BufferedWriter(fr);
			pr = new PrintWriter(br);
			//TODO Decide global output
			pr.println("MUST DECIDE WAHT TO PUT");

		} catch (IOException e) {
			System.out.println("[PatternAssessmentTemplateMethod] There was a problem opening GLOBAL file: " + filePath);
			e.printStackTrace();
		} finally {
			try {
				if (pr !=null)
					pr.close();
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			}catch (IOException e) {
				System.out.println("[PatternAssessmentTemplateMethod] There was a problem closing GLOBAL file: " + filePath);
				e.printStackTrace();
			} 
		}
	}//end method


}//end class
