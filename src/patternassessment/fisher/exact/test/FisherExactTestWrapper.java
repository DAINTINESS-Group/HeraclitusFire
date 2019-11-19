package patternassessment.fisher.exact.test;

import jsc.contingencytables.ContingencyTable2x2;
import jsc.contingencytables.FishersExactTest;

/**
 * Wraps the execution of Fisher Exact Test
 * 
 * For the moment we use the jsc (Java Statistical Classes) that I discovered
 * after serious effort. Although people complain at the web, tht sometimes it
 * returns erroneous results, the tests that I run for known cont. tables, give 
 * identical results with R.
 * 
 * See: 
 * https://en.wikipedia.org/wiki/Fisher%27s_exact_test
 * https://stackoverflow.com/questions/6253768/efficient-fishers-exact-test-in-java
 * 
 * @author pvassil
 *
 */
public class FisherExactTestWrapper {

	/**
	 * Constructs a FisherExactTestWrapper to perform a Fisher exact test on a 2x2 table of int
	 * 
	 * @param contingencyTable a 2x2 array of int, as a contingency table
	 * @throws java.lang.IllegalArgumentException
	 */
	public FisherExactTestWrapper(int[][] contingencyTable) {
		this.contingencyTable = contingencyTable;
		if ((contingencyTable.length != 2) || (contingencyTable[0].length != 2)) 
			throw new IllegalArgumentException("We need a 2x2 contingency for this FisherTest"); 
		
        ContingencyTable2x2 counter = new ContingencyTable2x2(this.contingencyTable);
        this.fet = new FishersExactTest(counter);
	}
	
	public double getFisherSingleTailPValue() {
		return this.fet.getOneTailedSP();
	}

	public double getFisherOppositeTailPValue() {
		return this.fet.getOppositeTailProb();
	}

	public double getFisherTwoTailedPValue() {
		return this.fet.getOneTailedSP() + this.fet.getOppositeTailProb();
	}

	protected int[][] contingencyTable;
	FishersExactTest fet;
}
