package test;

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
//import org.junit.runner.RunWith;
//import org.junit.runners.Suite.SuiteClasses;

@SelectClasses( { test.mainEngine.TableStatsMainEngineTest.class, test.mainEngine.StagelessTableStatsMainEngineTest.class, 
	test.patternassessment.fisher.exact.test.FisherExactTestWrapperTest.class, test.patternassessment.GammaSimpleTest.class} )
//@SelectPackages({"test", "test.mainEngine"})

//KEEP THIS SILENT:
//@RunWith(JUnitPlatform.class)

public class HeraclitusFireTestAllTests {

}
