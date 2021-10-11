package test.patternassessment;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.TableDetailedStatsElement;
import mainEngine.TableStatsMainEngine;
import patternassessment.tablepatterns.InverseGammaAssessment;
import patternassessment.tablepatterns.PatternAssessmentResult;
import patternassessment.tablepatterns.PatternAssessmentTemplateMethod.decision;

class InvGammaByteballByteballcoreTest {
	private static  InverseGammaAssessment invGammaAssessment;
	
	private static TableStatsMainEngine tableStatsMainEngine; 
	private static ArrayList<TableDetailedStatsElement> inputTupleCollection;
	private static int numRows;
	private static double ALPHA = 0.05;
	private static PatternAssessmentResult result;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		tableStatsMainEngine = new TableStatsMainEngine("resources/byteball__byteballcore", null);
		inputTupleCollection = new ArrayList<TableDetailedStatsElement>();
		ArrayList<String> header = new ArrayList<String>();
		numRows = tableStatsMainEngine.loadData("resources/byteball__byteballcore/results/tables_DetailedStats.tsv", "\t", true, 22, header, inputTupleCollection);
		
		invGammaAssessment = new InverseGammaAssessment(inputTupleCollection, "byteball__byteballcore", "resources/test/InvGamma", "resources/test/GlobalLog.txt", ALPHA);
		result = invGammaAssessment.constructResult();
		invGammaAssessment.computeContingencyTable(result);
	}



	@Test
	final void testGetTestName() {
		assertEquals("InverseGamma", invGammaAssessment.getTestName());
	}

	@Test
	final void testConstructResult() {
		assertEquals(2,result.getContingencyNumColumns());
		assertEquals(2,result.getContingencyNumRows());
	}

	@Test
	final void testComputeContingencyTable() {
		int[][] contingencyTable = result.getContingencyTable();
		assertEquals(contingencyTable[0][0], 17);
		assertEquals(contingencyTable[0][1], 48);
		assertEquals(contingencyTable[1][0], 1);
		assertEquals(contingencyTable[1][1], 2);
	}

	@Test
	final void testDecideIfPatternHolds() {
		if (invGammaAssessment.decideIfPatternHolds(result) == decision.SUCCESS)
			assertTrue(true);
		assertTrue(false);
	}

	  @Test 
	  final void testAssessPattern() { 
		  File fileProduced = new File("resources/test/InvGamma/byteball__byteballcore_InverseGamma.txt"); 
		  Long originalTimeStamp = fileProduced.lastModified();
		  
		  assertEquals(numRows,69, "byteball__byteballcore tables are 68 + 1 line header");
		  assertEquals(inputTupleCollection.size(),68);
		  if (invGammaAssessment.assessPatternTemplateMethod() == decision.SUCCESS)
				assertTrue(true);
			assertTrue(false);
		  Long newTimeStamp = fileProduced.lastModified();
		  assertTrue(newTimeStamp > originalTimeStamp);
	  }


}
