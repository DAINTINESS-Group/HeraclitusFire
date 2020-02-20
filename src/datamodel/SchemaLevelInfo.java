package datamodel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class SchemaLevelInfo implements IElement{

	public SchemaLevelInfo(String projectName, int projectDurationInDays, int projectDurationInMonths,
			int projectDureationInYears, int numCommits, int numTablesAtStart, int numTablesAtEnd, int numAttrsAtStart,
			int numAttrsAtEnd, int totalTableInsertions, int totalTableDeletions, int totalAttrInsWithTableIns,
			int totalAttrbDelWithTableDel, int totalAttrInjected, int totalAttrEjected, int tatalAttrWithTypeUpd,
			int totalAttrInPKUpd, int totalExpansion, int totalMaintenance, int totalTotalAttrActivity,
			double expansionRatePerCommit, double expansionRatePerMonth, double expansionRatePeryear,
			double maintenanceRatePerCommit, double maintenanceRatePerMonth, double maintenanceRatePeryear,
			double totalAttrActivityRatePerCommit, double totalAttrActivityRatePerMonth,
			double totalAttrActivityRatePeryear, double resizingratio) {
		super();
		this.projectName = projectName;
		this.projectDurationInDays = projectDurationInDays;
		this.projectDurationInMonths = projectDurationInMonths;
		this.projectDureationInYears = projectDureationInYears;
		this.numCommits = numCommits;
		this.numTablesAtStart = numTablesAtStart;
		this.numTablesAtEnd = numTablesAtEnd;
		this.numAttrsAtStart = numAttrsAtStart;
		this.numAttrsAtEnd = numAttrsAtEnd;
		this.totalTableInsertions = totalTableInsertions;
		this.totalTableDeletions = totalTableDeletions;
		this.totalAttrInsWithTableIns = totalAttrInsWithTableIns;
		this.totalAttrbDelWithTableDel = totalAttrbDelWithTableDel;
		this.totalAttrInjected = totalAttrInjected;
		this.totalAttrEjected = totalAttrEjected;
		this.tatalAttrWithTypeUpd = tatalAttrWithTypeUpd;
		this.totalAttrInPKUpd = totalAttrInPKUpd;
		this.totalExpansion = totalExpansion;
		this.totalMaintenance = totalMaintenance;
		this.totalTotalAttrActivity = totalTotalAttrActivity;
		this.expansionRatePerCommit = expansionRatePerCommit;
		this.expansionRatePerMonth = expansionRatePerMonth;
		this.expansionRatePeryear = expansionRatePeryear;
		this.maintenanceRatePerCommit = maintenanceRatePerCommit;
		this.maintenanceRatePerMonth = maintenanceRatePerMonth;
		this.maintenanceRatePeryear = maintenanceRatePeryear;
		this.totalAttrActivityRatePerCommit = totalAttrActivityRatePerCommit;
		this.totalAttrActivityRatePerMonth = totalAttrActivityRatePerMonth;
		this.totalAttrActivityRatePeryear = totalAttrActivityRatePeryear;
		this.resizingratio = resizingratio;
	}
	
	public String getProjectName() {
		return projectName;
	}
	public int getProjectDurationInDays() {
		return projectDurationInDays;
	}
	public int getProjectDurationInMonths() {
		return projectDurationInMonths;
	}
	public int getProjectDureationInYears() {
		return projectDureationInYears;
	}
	public int getNumCommits() {
		return numCommits;
	}
	public int getNumTablesAtStart() {
		return numTablesAtStart;
	}
	public int getNumTablesAtEnd() {
		return numTablesAtEnd;
	}
	public int getNumAttrsAtStart() {
		return numAttrsAtStart;
	}
	public int getNumAttrsAtEnd() {
		return numAttrsAtEnd;
	}
	public int getTotalTableInsertions() {
		return totalTableInsertions;
	}
	public int getTotalTableDeletions() {
		return totalTableDeletions;
	}
	public int getTotalAttrInsWithTableIns() {
		return totalAttrInsWithTableIns;
	}
	public int getTotalAttrbDelWithTableDel() {
		return totalAttrbDelWithTableDel;
	}
	public int getTotalAttrInjected() {
		return totalAttrInjected;
	}
	public int getTotalAttrEjected() {
		return totalAttrEjected;
	}
	public int getTatalAttrWithTypeUpd() {
		return tatalAttrWithTypeUpd;
	}
	public int getTotalAttrInPKUpd() {
		return totalAttrInPKUpd;
	}
	public int getTotalExpansion() {
		return totalExpansion;
	}
	public int getTotalMaintenance() {
		return totalMaintenance;
	}
	public int getTotalTotalAttrActivity() {
		return totalTotalAttrActivity;
	}
	public double getExpansionRatePerCommit() {
		return expansionRatePerCommit;
	}
	public double getExpansionRatePerMonth() {
		return expansionRatePerMonth;
	}
	public double getExpansionRatePeryear() {
		return expansionRatePeryear;
	}
	public double getMaintenanceRatePerCommit() {
		return maintenanceRatePerCommit;
	}
	public double getMaintenanceRatePerMonth() {
		return maintenanceRatePerMonth;
	}
	public double getMaintenanceRatePeryear() {
		return maintenanceRatePeryear;
	}
	public double getTotalAttrActivityRatePerCommit() {
		return totalAttrActivityRatePerCommit;
	}
	public double getTotalAttrActivityRatePerMonth() {
		return totalAttrActivityRatePerMonth;
	}
	public double getTotalAttrActivityRatePeryear() {
		return totalAttrActivityRatePeryear;
	}
	public double getResizingratio() {
		return resizingratio;
	}
	
	@Override
	public int getIntValueByPosition(int position) {
		switch(position) {
			//case 0:	 return getProjectName(); //break;
			case 1:	 return getProjectDurationInDays(); //break;
			case 2:	 return getProjectDurationInMonths(); //break;
			case 3:	 return getProjectDureationInYears(); //break;
			case 4:	 return getNumCommits(); //break;
			case 5:	 return getNumTablesAtStart(); //break;
			case 6:	 return getNumTablesAtEnd();//break;
			case 7:	 return getNumAttrsAtStart();//break;
			case 8:	 return getNumAttrsAtEnd();//break;
			case 9:	 return getTotalTableInsertions();//break;
			case 10:	 return getTotalTableDeletions(); //break;
			case 11:	 return getTotalAttrInsWithTableIns(); //break;
			case 12:	 return getTotalAttrbDelWithTableDel(); //break;
			case 13:	 return getTotalAttrInjected(); //break;
			case 14:	 return getTotalAttrEjected(); //break;
			case 15:	 return getTatalAttrWithTypeUpd(); //break;
			case 16:	 return getTotalAttrInPKUpd(); //break;
			case 17:	 return getTotalExpansion(); //break;
			case 18:	 return getTotalMaintenance(); //break;
			case 19:	 return getTotalTotalAttrActivity();// break;
			//case 20:	 return getExpansionRatePerCommit();// break;
			//case 21:	 return getExpansionRatePerMonth();// break;
			//case 22:	 return getExpansionRatePeryear();// break;
			//case 23:	 return getMaintenanceRatePerCommit();// break;
			//case 24:	 return getMaintenanceRatePerMonth();// break;
			//case 25:	 return getMaintenanceRatePeryear();// break;
			//case 26:	 return getTotalAttrActivityRatePerCommit();// break;
			//case 27:	 return getTotalAttrActivityRatePerMonth();// break;
			//case 28:	 return getTotalAttrActivityRatePeryear();// break;
			//case 29:	 return getResizingratio();// break;
			default: return _ERROR_CODE;
		}//end switch
	}
	
	@Override
	public String getStringValueByPosition(int position) {
		switch(position) {
		case 0:	 return getProjectName(); //break;
		//case 1:	 return getProjectDurationInDays(); //break;
		//case 2:	 return getProjectDurationInMonths(); //break;
		//case 3:	 return getProjectDureationInYears(); //break;
		//case 4:	 return getNumCommits(); //break;
		//case 5:	 return getNumTablesAtStart(); //break;
		//case 6:	 return getNumTablesAtEnd();//break;
		//case 7:	 return getNumAttrsAtStart();//break;
		//case 8:	 return getNumAttrsAtEnd();//break;
		//case 9:	 return getTotalTableInsertions();//break;
		//case 10:	 return getTotalTableDeletions(); //break;
		//case 11:	 return getTotalAttrInsWithTableIns(); //break;
		//case 12:	 return getTotalAttrbDelWithTableDel(); //break;
		//case 13:	 return getTotalAttrInjected(); //break;
		//case 14:	 return getTotalAttrEjected(); //break;
		//case 15:	 return getTatalAttrWithTypeUpd(); //break;
		//case 16:	 return getTotalAttrInPKUpd(); //break;
		//case 17:	 return getTotalExpansion(); //break;
		//case 18:	 return getTotalMaintenance(); //break;
		//case 19:	 return getTotalTotalAttrActivity();// break;
		//case 20:	 return getExpansionRatePerCommit();// break;
		//case 21:	 return getExpansionRatePerMonth();// break;
		//case 22:	 return getExpansionRatePeryear();// break;
		//case 23:	 return getMaintenanceRatePerCommit();// break;
		//case 24:	 return getMaintenanceRatePerMonth();// break;
		//case 25:	 return getMaintenanceRatePeryear();// break;
		//case 26:	 return getTotalAttrActivityRatePerCommit();// break;
		//case 27:	 return getTotalAttrActivityRatePerMonth();// break;
		//case 28:	 return getTotalAttrActivityRatePeryear();// break;
		//case 29:	 return getResizingratio();// break;
		default: return _ERROR_STRING;
		}//end switch
	}
	
	// probably not needed and to be deprecated	
	public double getDoubleValueByPosition(int position) {
		switch(position) {
		//case 0:	 return getProjectName(); //break;
		//case 1:	 return getProjectDurationInDays(); //break;
		//case 2:	 return getProjectDurationInMonths(); //break;
		//case 3:	 return getProjectDureationInYears(); //break;
		//case 4:	 return getNumCommits(); //break;
		//case 5:	 return getNumTablesAtStart(); //break;
		//case 6:	 return getNumTablesAtEnd();//break;
		//case 7:	 return getNumAttrsAtStart();//break;
		//case 8:	 return getNumAttrsAtEnd();//break;
		//case 9:	 return getTotalTableInsertions();//break;
		//case 10:	 return getTotalTableDeletions(); //break;
		//case 11:	 return getTotalAttrInsWithTableIns(); //break;
		//case 12:	 return getTotalAttrbDelWithTableDel(); //break;
		//case 13:	 return getTotalAttrInjected(); //break;
		//case 14:	 return getTotalAttrEjected(); //break;
		//case 15:	 return getTatalAttrWithTypeUpd(); //break;
		//case 16:	 return getTotalAttrInPKUpd(); //break;
		//case 17:	 return getTotalExpansion(); //break;
		//case 18:	 return getTotalMaintenance(); //break;
		//case 19:	 return getTotalTotalAttrActivity();// break;
		case 20:	 return getExpansionRatePerCommit();// break;
		case 21:	 return getExpansionRatePerMonth();// break;
		case 22:	 return getExpansionRatePeryear();// break;
		case 23:	 return getMaintenanceRatePerCommit();// break;
		case 24:	 return getMaintenanceRatePerMonth();// break;
		case 25:	 return getMaintenanceRatePeryear();// break;
		case 26:	 return getTotalAttrActivityRatePerCommit();// break;
		case 27:	 return getTotalAttrActivityRatePerMonth();// break;
		case 28:	 return getTotalAttrActivityRatePeryear();// break;
		case 29:	 return getResizingratio();// break;
		default: return _ERROR_CODE;
		}//end switch
	}

	@Override
	public String toString() {
		return projectName + "\t" + FormatString(projectDurationInDays) + "\t" + FormatString(projectDurationInMonths) + "\t"
				+ FormatString(projectDureationInYears) + "\t" + numCommits + "\t" + numTablesAtStart + "\t" + numTablesAtEnd + "\t"
				+ numAttrsAtStart + "\t" + numAttrsAtEnd + "\t" + totalTableInsertions + "\t" + totalTableDeletions
				+ "\t" + totalAttrInsWithTableIns + "\t" + totalAttrbDelWithTableDel + "\t" + totalAttrInjected + "\t"
				+ totalAttrEjected + "\t" + tatalAttrWithTypeUpd + "\t" + totalAttrInPKUpd + "\t" + totalExpansion
				+ "\t" + totalMaintenance + "\t" + totalTotalAttrActivity + "\t" + dFormatter.format(expansionRatePerCommit) + "\t"
				+ FormatString(expansionRatePerMonth) + "\t" + FormatString(expansionRatePeryear) + "\t" + dFormatter.format(maintenanceRatePerCommit) + "\t"
				+ FormatString(maintenanceRatePerMonth) + "\t" + FormatString(maintenanceRatePeryear) + "\t" + dFormatter.format(totalAttrActivityRatePerCommit) + "\t"
				+ FormatString(totalAttrActivityRatePerMonth) + "\t" + FormatString(totalAttrActivityRatePeryear) + "\t" + dFormatter.format(resizingratio);
	}
	
	private String FormatString(Number field) {
		if (field.doubleValue() < 0)
			return "";
		if (field.doubleValue() == field.intValue())
			return field.toString();
		return dFormatter.format(field).toString();
	}

	private String projectName;
	
	private int projectDurationInDays;
	private int projectDurationInMonths;
	private int projectDureationInYears;
	
	private int numCommits;
	private int numTablesAtStart;
	private int numTablesAtEnd;
	private int numAttrsAtStart;
	private int numAttrsAtEnd;
	
	private int totalTableInsertions;
	private int totalTableDeletions;
	private int totalAttrInsWithTableIns;
	private int totalAttrbDelWithTableDel;
	private int totalAttrInjected;
	private int totalAttrEjected;
	private int tatalAttrWithTypeUpd;
	private int totalAttrInPKUpd;
	
	private int totalExpansion;
	private int totalMaintenance;
	private int totalTotalAttrActivity;
	
	private double expansionRatePerCommit;
	private double expansionRatePerMonth;
	private double expansionRatePeryear;
	private double maintenanceRatePerCommit;
	private double maintenanceRatePerMonth;
	private double maintenanceRatePeryear;
	private double totalAttrActivityRatePerCommit;
	private double totalAttrActivityRatePerMonth;
	private double totalAttrActivityRatePeryear;
	private double resizingratio;
	private static NumberFormat dFormatter = new DecimalFormat("#0.000").getNumberInstance(Locale.US);
//	final public static int _ERROR_CODE = -1;
//	final public static String _ERROR_STRING = "";

}
