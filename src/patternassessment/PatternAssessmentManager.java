package patternassessment;

import java.util.List;
import java.util.ArrayList;
import datamodel.TableDetailedStatsElement;

public class PatternAssessmentManager {
	private PatternAssessmentFactory patternAssessmentFactory;
	private ArrayList<PatternAssessmentTemplateMethod> assessmentsToRun; 
	
	public PatternAssessmentManager() {
		this.patternAssessmentFactory = new PatternAssessmentFactory();
		this.assessmentsToRun = new ArrayList<PatternAssessmentTemplateMethod>();
	}
	
	public int assessPatterns(List<PatternAssessmentTypesEnum> testsToRun,
			ArrayList<TableDetailedStatsElement> pInputTupleCollection,
			String projectName,
			String pOutputFolderWithPatterns,
			String globalAppendLog,
			double alpha
			) {
		for(PatternAssessmentTypesEnum a: testsToRun) {
			PatternAssessmentTemplateMethod pa = this.patternAssessmentFactory.createPatternAssessmentTester(a, pInputTupleCollection, 
					projectName, pOutputFolderWithPatterns, globalAppendLog, alpha);	
			if(pa != null)
				this.assessmentsToRun.add(pa);
		}
		int patternsToAssess = this.assessmentsToRun.size();
		
		//TODO: Don;t like it. Maybe it's better to update the PAResult
		//and return the result?
		for(PatternAssessmentTemplateMethod pa: assessmentsToRun) {
			Boolean resultFlag = pa.assessPatternTemplateMethod();
		}
		
		return patternsToAssess;
	}
	
}//end class
