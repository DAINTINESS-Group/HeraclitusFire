package test.patternassessment;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datamodel.TableDetailedStatsElement;
import mainEngine.TableStatsMainEngine;
import patternassessment.tablepatterns.ElectrolysisAssessment;
import patternassessment.tablepatterns.PatternAssessmentResult;
import patternassessment.tablepatterns.PatternAssessmentTemplateMethod.PatternAssessmentDecision;

public class ElectrolysisSimpleTest {
	private static ElectrolysisAssessment electrolysisAssessment;
	
	private static TableStatsMainEngine tableStatsMainEngine; 
	private static ArrayList<TableDetailedStatsElement> inputTupleCollection;
	private static int numRows;
	private static double ALPHA = 0.001;
	private static PatternAssessmentResult result;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		tableStatsMainEngine = new TableStatsMainEngine("resources/Atlas", null);
		inputTupleCollection = new ArrayList<TableDetailedStatsElement>();
		ArrayList<String> header = new ArrayList<String>();
		numRows = tableStatsMainEngine.loadData("resources/Atlas/results/tables_DetailedStats.tsv", "\t", true, 22, header, inputTupleCollection);
		
		electrolysisAssessment = new ElectrolysisAssessment(inputTupleCollection, "Atlas", "resources/test/Electrolysis", "resources/test/GlobalLog.txt", ALPHA);
		result = electrolysisAssessment.constructResult();
		electrolysisAssessment.computeContingencyTable(result);
	}

	@Test
	final void testGetTestName() {
		assertEquals("Electrolysis", electrolysisAssessment.getTestName());
	}
	
	@Test
	final void testConstructResult() {
		assertEquals(3,result.getContingencyNumRows());
		assertEquals(6,result.getContingencyNumColumns());
	}
	
	@Test
	final void testComputeContingencyTable() {
		int[][] contingencyTable = result.getContingencyTable();
		assertEquals(contingencyTable[0][0], 4);
		assertEquals(contingencyTable[0][1], 0);
		assertEquals(contingencyTable[0][2], 0);
		assertEquals(contingencyTable[0][3], 1);
		assertEquals(contingencyTable[0][4], 1);
		assertEquals(contingencyTable[0][5], 0);
		
		assertEquals(contingencyTable[1][0], 3);
		assertEquals(contingencyTable[1][1], 6);
		assertEquals(contingencyTable[1][2], 2);
		assertEquals(contingencyTable[1][3], 10);
		assertEquals(contingencyTable[1][4], 11);
		assertEquals(contingencyTable[1][5], 3);
		
		assertEquals(contingencyTable[2][0], 0);
		assertEquals(contingencyTable[2][1], 0);
		assertEquals(contingencyTable[2][2], 0);
		assertEquals(contingencyTable[2][3], 0);
		assertEquals(contingencyTable[2][4], 25);
		assertEquals(contingencyTable[2][5], 22);
		
//		durRanges\LAD	10	11	12	20	21	22	sumPerRange																						
//		0-20			4	0	0	1	1	0	6																						
//		20-80			3	6	2	10	11	3	35																						
//		80-100			0	0	0	0	25	22	47																						
//		sumPerLAD		7	6	2	11	37	25	88
	}
	
	@Test
	final void testDecideIfPatternHolds() {
		assertFalse(electrolysisAssessment.decideIfPatternHolds(result) == PatternAssessmentDecision.SUCCESS);
	}
	
	@Test 
	final void testAssessPattern() { 
		File fileProduced = new File("resources/test/Electrolysis/Atlas_Electrolysis.txt"); 
		Long originalTimeStamp = fileProduced.lastModified();
		
		assertEquals(numRows,89, "Atlas tables are 88 + 1 line header");
		assertEquals(inputTupleCollection.size(),88);
		assertTrue(electrolysisAssessment.assessPatternTemplateMethod() == PatternAssessmentDecision.FAILURE); 
		Long newTimeStamp = fileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
	}

}
