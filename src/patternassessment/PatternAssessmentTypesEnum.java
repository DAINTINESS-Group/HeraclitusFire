/**
 * 
 */
package patternassessment;

/**
 *  Types of patterns that can be assessed
 *  
 *  Patterns that can be assessed
 *  <li>{@link #GAMMA}: duration/survival as a function of schema size at birth</li>
 *  <li>{@link #INVERSE_GAMMA}: sumOfUpdates as a function of duration</li>
 *  <li>{@link #ELECTROLYSIS}: correlation of LAD and duration</li>
 *  
 *  @author pvassil
 */
public enum PatternAssessmentTypesEnum {
	GAMMA,
	INVERSE_GAMMA,
	ELECTROLYSIS
}
