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
import patternassessment.tablepatterns.PatternAssessmentTemplateMethod.PatternAssessmentDecision;

class InvGammaCartalystSentryTest {

private static  InverseGammaAssessment invGammaAssessment;
	
	private static TableStatsMainEngine tableStatsMainEngine; 
	private static ArrayList<TableDetailedStatsElement> inputTupleCollection;
	private static int numRows;
	private static double ALPHA = 0.05;
	private static PatternAssessmentResult result;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		tableStatsMainEngine = new TableStatsMainEngine("resources/cartalyst__sentry", null);
		inputTupleCollection = new ArrayList<TableDetailedStatsElement>();
		ArrayList<String> header = new ArrayList<String>();
		numRows = tableStatsMainEngine.loadData("resources/cartalyst__sentry/results/tables_DetailedStats.tsv", "\t", true, 22, header, inputTupleCollection);
		
		invGammaAssessment = new InverseGammaAssessment(inputTupleCollection, "cartalyst__sentry", "resources/test/InvGamma", "resources/test/GlobalLog.txt", ALPHA);
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
		assertEquals(contingencyTable[0][0], 3);
		assertEquals(contingencyTable[0][1], 1);
		assertEquals(contingencyTable[1][0], 0);
		assertEquals(contingencyTable[1][1], 1);



	}

	@Test
	final void testDecideIfPatternHolds() {
		if (invGammaAssessment.decideIfPatternHolds(result) == PatternAssessmentDecision.SUCCESS)
			assertTrue(true);
		assertTrue(false);
	}

	  @Test 
	  final void testAssessPattern() { 
		  File fileProduced = new File("resources/test/InvGamma/cartalyst__sentry_InverseGamma.txt"); 
		  Long originalTimeStamp = fileProduced.lastModified();
		  
		  assertEquals(numRows,6, "cartalyst__sentry tables are 5 + 1 line header");
		  assertEquals(inputTupleCollection.size(),5);
		  if (invGammaAssessment.assessPatternTemplateMethod() == PatternAssessmentDecision.SUCCESS)
				assertTrue(true);
			assertTrue(false);
		  Long newTimeStamp = fileProduced.lastModified();
		  assertTrue(newTimeStamp > originalTimeStamp);
	  }

}
