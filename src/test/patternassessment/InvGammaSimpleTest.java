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

public class InvGammaSimpleTest {
	private static  InverseGammaAssessment invGammaAssessment;
	
	private static TableStatsMainEngine tableStatsMainEngine; 
	private static ArrayList<TableDetailedStatsElement> inputTupleCollection;
	private static int numRows;
	private static double ALPHA = 0.05;
	private static PatternAssessmentResult result;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		tableStatsMainEngine = new TableStatsMainEngine("resources/Atlas", null);
		inputTupleCollection = new ArrayList<TableDetailedStatsElement>();
		ArrayList<String> header = new ArrayList<String>();
		numRows = tableStatsMainEngine.loadData("resources/Atlas/results/tables_DetailedStats.tsv", "\t", true, 22, header, inputTupleCollection);
		
		invGammaAssessment = new InverseGammaAssessment(inputTupleCollection, "Atlas", "resources/test/InvGamma", "resources/test/GlobalLog.txt", ALPHA);
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
		assertEquals(contingencyTable[0][0], 35);
		assertEquals(contingencyTable[0][1], 12);
		assertEquals(contingencyTable[1][0], 7);
		assertEquals(contingencyTable[1][1], 34);
	}

	@Test
	final void testDecideIfPatternHolds() {
		if (invGammaAssessment.decideIfPatternHolds(result) == PatternAssessmentDecision.SUCCESS)
			assertTrue(true);
		assertTrue(false);
	}

	  @Test 
	  final void testAssessPattern() { 
		  File fileProduced = new File("resources/test/InvGamma/Atlas_InverseGamma.txt"); 
		  Long originalTimeStamp = fileProduced.lastModified();
		  
		  assertEquals(numRows,89, "Atlas tables are 88 + 1 line header");
		  assertEquals(inputTupleCollection.size(),88);
		  if (invGammaAssessment.assessPatternTemplateMethod() == PatternAssessmentDecision.SUCCESS)
				assertTrue(true);
			assertTrue(false);
		  Long newTimeStamp = fileProduced.lastModified();
		  assertTrue(newTimeStamp > originalTimeStamp);
	  }

}
