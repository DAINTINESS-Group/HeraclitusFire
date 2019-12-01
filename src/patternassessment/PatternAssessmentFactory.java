package patternassessment;

import java.util.ArrayList;

import datamodel.TableDetailedStatsElement;

public class PatternAssessmentFactory {

	public PatternAssessmentTemplateMethod createPatternAssessmentTester(
			String testType,
			ArrayList<TableDetailedStatsElement> pInputTupleCollection,
			String projectName,
			String pOutputFolderWithPatterns,
			String globalAppendLog,
			double alpha) {

		switch(testType) {
		case "Gamma": return new PatternGammaAssessment(pInputTupleCollection,projectName, pOutputFolderWithPatterns, globalAppendLog, alpha);

		default: return null; 
		}
		//return null;
	}
}
