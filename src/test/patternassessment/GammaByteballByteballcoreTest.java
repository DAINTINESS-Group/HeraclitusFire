package test.patternassessment;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import datamodel.TableDetailedStatsElement;
import mainEngine.TableStatsMainEngine;
import patternassessment.tablepatterns.GammaPatternLKVAssessment;
import patternassessment.tablepatterns.PatternAssessmentResult;
import patternassessment.tablepatterns.PatternAssessmentTemplateMethod.PatternAssessmentDecision;

class GammaByteballByteballcoreTest {

	private static TableStatsMainEngine tableStatsMainEngine; 
	private static ArrayList<TableDetailedStatsElement> inputTupleCollection;
	private static int numRows;
	private static GammaPatternLKVAssessment gammaAssessment;
	private static double ALPHA = 0.001;
	private static PatternAssessmentResult result;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		tableStatsMainEngine = new TableStatsMainEngine("resources/byteball__byteballcore", null);
		inputTupleCollection = new ArrayList<TableDetailedStatsElement>();
		ArrayList<String> header = new ArrayList<String>();
		numRows = tableStatsMainEngine.loadData("resources/byteball__byteballcore/results/tables_DetailedStats.tsv", "\t", true, 22, header, inputTupleCollection);
		gammaAssessment = new GammaPatternLKVAssessment(inputTupleCollection, "byteball__byteballcore", "resources/test/Gamma", "resources/test/GlobalLog.txt", ALPHA);
		result = gammaAssessment.constructResult();
		gammaAssessment.computeContingencyTable(result);
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testConstructResult() {
		assertEquals(2,result.getContingencyNumColumns());
		assertEquals(2,result.getContingencyNumRows());
	}
	
	@Test
final void testComputeContingencyTable() {
		
		int[][] contingencyTable = result.getContingencyTable();
		assertEquals(contingencyTable[0][0], 0);
		assertEquals(contingencyTable[0][1], 64);
		assertEquals(contingencyTable[1][0], 0);
		assertEquals(contingencyTable[1][1], 4);
	}

	@Test
	final void testDecideIfPatternHolds() {
		if (gammaAssessment.decideIfPatternHolds(result) == PatternAssessmentDecision.SUCCESS)
			assertTrue(true);
		assertTrue(false);
	}

	  
	  @Test 
	  final void testAssessPattern() { 
		  File fileProduced = new File("resources/test/Gamma/byteball__byteballcore.txt"); 
		  Long originalTimeStamp = fileProduced.lastModified();
		  
		  assertEquals(numRows,69, "byteball__byteballcore tables are 68 + 1 line header");
		  assertEquals(inputTupleCollection.size(),68);
		  if (gammaAssessment.assessPatternTemplateMethod() == PatternAssessmentDecision.SUCCESS)
				assertTrue(true);
			assertTrue(false);
		  Long newTimeStamp = fileProduced.lastModified();
		  assertTrue(newTimeStamp > originalTimeStamp);
	  } 
	

}
