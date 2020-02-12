package test.patternassessment.fisher.exact.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import patternassessment.fisher.exact.test.FisherExactTestWrapper;


public class FisherExactTestWrapperTest {

	@Test
	final void testGetFisherSingleTailPValue() {
		int [][] counts = {{13,2},{61,12}};
		FisherExactTestWrapper fet = new FisherExactTestWrapper(counts);
		assertEquals(0.5588, fet.getFisherSingleTailPValue(), 0.001);        
	}

	@Test
	final void testGetFisherOppositeTailPValue() {
		int [][] counts = {{13,2},{61,12}};
		FisherExactTestWrapper fet = new FisherExactTestWrapper(counts);
		assertEquals(0.4412, fet.getFisherOppositeTailPValue(), 0.001);        
	}

	@Test
	final void testGetFisherTwoTailedPValueAtlas() {
		int [][] counts = {{13,2},{61,12}};
		FisherExactTestWrapper fet = new FisherExactTestWrapper(counts);
		assertEquals(1.0, fet.getFisherTwoTailedPValue(), 0.001);        
	}
	
	@Test
	final void testGetFisherSingleTailPValueArbitrary() {
		int [][] counts = {{7,2},{5,6}};
		FisherExactTestWrapper fet = new FisherExactTestWrapper(counts);
		assertEquals(0.1569, fet.getFisherSingleTailPValue(), 0.001);
	}
	
	@Test
	final void testGetFisherOppositeTailPValueArbitrary() {
		int [][] counts = {{7,2},{5,6}};
		FisherExactTestWrapper fet = new FisherExactTestWrapper(counts);
		assertEquals(0.0399, fet.getFisherOppositeTailPValue(), 0.001);
	}

	@Test
	final void testGetFisherTwoTailedPValueArbitrary() {
		int [][] counts = {{7,2},{5,6}};
		FisherExactTestWrapper fet = new FisherExactTestWrapper(counts);
		assertEquals(0.1968, fet.getFisherTwoTailedPValue(), 0.001);
	}

}//end class
