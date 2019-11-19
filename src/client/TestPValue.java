package client;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.apache.commons.math3.stat.inference.ChiSquareTest;

import jsc.contingencytables.ContingencyTable2x2;
import jsc.contingencytables.FishersExactTest;

public class TestPValue {

	public static void main(String[] args) {
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream("output.txt"));
		} catch (FileNotFoundException e) {
			System.err.println("outputFile not created");
		}

        long[][] counts = { {40, 0, 4}, {91, 1, 2}, {60, 2, 0}};
        Boolean result = runChiSquareTests(counts, 0.05, System.out);
        System.out.println("FINALLY: chi-square test accept?: " + "\t" + result);
        
        if (ps != null) ps.close();
        

	}

	/**
	 * A method to perform a Chi Square Test and output the result to a PrintStream
	 * 
	 * @param counts A long[][] contingency matrix with the counted values for the different classes
	 * @param alphaAcceptanceLevel A double with the $\alpha$ level to decide if the test is passed or not
	 * @param outStream a PrintStream where the details of the test will be printed
	 * @return a Boolean value reporting whether the test is passed or not
	 */
	private static Boolean runChiSquareTests(long[][] counts, double alphaAcceptanceLevel, PrintStream outStream) {
		ChiSquareTest testStatistic = new ChiSquareTest();
		Double resultRaw = testStatistic.chiSquare(counts); 
		Double pValue = testStatistic.chiSquareTest(counts); 
		Boolean pTest = testStatistic.chiSquareTest(counts, alphaAcceptanceLevel);

		//outStream.println( "chi-square test statistic: "+ resultRaw  + " || expected: " + 9.67444662263);
		outStream.println( "chi-square p-value: " + "\t" + pValue);
		//outStream.println("chi-square test reject for 2x10^-4?: " + testStatistic.chiSquareTest(counts, 0.0002));
		//outStream.println("chi-square test accept for 1x10^-4?: " + testStatistic.chiSquareTest(counts, 0.0001));
		outStream.println("chi-square test accept?: " + "\t" +  pTest);

		return pTest;
	}//end runChiSquareTests

}
