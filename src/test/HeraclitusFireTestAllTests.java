package test;

import org.junit.platform.runner.JUnitPlatform;

/**
 * THe trick that did with JUnit 5
 * 
 * 1. The individual classes are annotated almost as before, but, 
 *    you need to import stuff from jupiter
 *  	import static org.junit.jupiter.api.Assertions.*;
 *  	import org.junit.jupiter.api.Test;
 *  	import org.junit.jupiter.api.BeforeAll;
 *  
 *  2. For the suite, here you need to work with junit.platform (not junit directly)
 *  
 *  3. [SEEMS VERY IMPORTANT] I have no idea ho suites work (they don't?) but to run a suite, 
 *  I created one, as a suite, BUT at the Run->Run Configurations-> ...the tab of the project...
 *  I had to check "Run all tests" INSTEAD of the "Run a single test" we used to have in Junit 4 for suites
 *  (as a parameter, I put the project's name)
 *  
 *  4. I had to kill some tags like @RunWith(JUnitPlatform.class) that created errors.
 */


//import org.junit.platform.runner.JUnitPlatform;
//import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;
//import org.junit.runner.RunWith;
//import org.junit.runners.Suite.SuiteClasses;

@RunWith(JUnitPlatform.class)	//added cause couldn't run it 
/*
@SelectClasses( { test.mainEngine.TableStatsMainEngineTest.class, test.mainEngine.StagelessTableStatsMainEngineTest.class, 
	test.mainEngine.SchemaStatsMainEngineTest.class, test.mainEngine.StagelessSchemaStatsMainEngineTest.class, 
	test.chartExport.StagelessTablesChartManagerTest.class, test.chartExport.StagelessSchemaChartManagerTest.class, 
	test.patternassessment.fisher.exact.test.FisherExactTestWrapperTest.class, 
	test.patternassessment.GammaSimpleTest.class, test.patternassessment.InvGammaSimpleTest.class, test.patternassessment.ElectrolysisSimpleTest.class, 
	test.mainEngine.Aa_alert_frbcatdbMonthlyStatsTest.class, test.mainEngine.Accgit__aclMonthlyStatsTest.class, test.mainEngine.AtlasMonthlyStatsTest.class} )
*/

//Select from whole packages instead of classes
//Tests pass successfully
@SelectPackages( {"test.chartExport", "test.mainEngine", "test.patternassessment", 
	"test.patternassessment.fisher.exact.test"})



public class HeraclitusFireTestAllTests {

}
