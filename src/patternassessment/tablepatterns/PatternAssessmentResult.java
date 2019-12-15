package patternassessment.tablepatterns;

public class PatternAssessmentResult {
	private String prjNameAndPattern;
	private String description;
	private int contingencyNumRows;
	private int contingencyNumColumns;
	private int [][] contingencyTable;
	private int [][] contingencyTableWithMarginals;
	private double [][] percentageTableWithMarginals;
	private double chiSquareTestPValue;
	private double fisherTestPValue;
	private Boolean chiSquareTestPass;
	private Boolean fisherTestPass;
	private Boolean fisherTestExecuted;
	private Boolean patternHolds;
	//TODO: possibly add one or two more Boolean/double fields for "otherTestsPass"
	
	public PatternAssessmentResult(String text, int rows, int cols) {
		this.prjNameAndPattern = text;
		//TODO catch <= 1 for rows/cols
		this.contingencyNumRows = rows;
		this.contingencyNumColumns = cols;
		this.contingencyTable = new int[this.contingencyNumRows][this.contingencyNumColumns];
		this.contingencyTableWithMarginals = new int[this.contingencyNumRows+1][this.contingencyNumColumns+1];
		this.percentageTableWithMarginals = new double[this.contingencyNumRows+1][this.contingencyNumColumns+1];
		this.chiSquareTestPValue = Double.MAX_VALUE;
		this.chiSquareTestPass = null;
		this.fisherTestPValue = Double.MAX_VALUE;
		this.fisherTestExecuted = true;
		this.fisherTestPass = null;
		this.patternHolds = null;
	}//end constructor

	public String getprjNameAndPattern() {
		return this.prjNameAndPattern;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getContingencyNumRows() {
		return contingencyNumRows;
	}

	public int getContingencyNumColumns() {
		return contingencyNumColumns;
	}

	public int[][] getContingencyTable() {
		return contingencyTable;
	}

	public int[][] getContingencyTableWithMarginals() {
		return contingencyTableWithMarginals;
	}

	public double[][] getPercentageTableWithMarginals() {
		return percentageTableWithMarginals;
	}

	public double getChiSquareTestPValue() {
		return chiSquareTestPValue;
	}

	public double getFisherTestPValue() {
		return fisherTestPValue;
	}

	public Boolean getChiSquareTestPass() {
		return chiSquareTestPass;
	}

	public Boolean getFisherTestPass() {
		return fisherTestPass;
	}

	public Boolean getPatternHolds() {
		return patternHolds;
	}
	
	public Boolean getFisherTestExecuted() {
		return fisherTestExecuted;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setChiSquareTestPValue(double chiSquareTestPValue) {
		this.chiSquareTestPValue = chiSquareTestPValue;
	}

	public void setFisherTestPValue(double fisherTestPValue) {
		this.fisherTestPValue = fisherTestPValue;
	}

	public void setChiSquareTestPass(Boolean chiSquareTestPass) {
		this.chiSquareTestPass = chiSquareTestPass;
	}

	public void setFisherTestPass(Boolean fisherTestPass) {
		this.fisherTestPass = fisherTestPass;
	}

	public void setPatternHolds(Boolean patternHolds) {
		this.patternHolds = patternHolds;
	}


	public void setFisherTestExecuted(Boolean fisherTestExecuted) {
		this.fisherTestExecuted = fisherTestExecuted;
	}

	
}//end class
