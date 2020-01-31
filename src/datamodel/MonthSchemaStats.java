package datamodel;

public class MonthSchemaStats {

	public MonthSchemaStats(int mID, String humanTime, int numCommits, int numTables, int numAttrs, 
			int tablesInsertionsSum, int tablesDeletionsSum, int attrsInsWithTableInsSum, int attrsbDelWithTableDelSum, 
			int attrsInjectedSum, int attrsEjectedSum, int attrsWithTypeUpdSum, int attrsInPKUpdSum, int tableDeltaSum, 
			int attrDeltaSum, int attrBirthsSum, int attrDeathsSum, int attrUpdsSum, int totalExpansion, 
			int totalMaintenance, int totalAttrActivity) {
		super();
		this.mID = mID;
		this.humanTime = humanTime;
		this.numCommits = numCommits;
		this.numTables = numTables;
		this.numAttrs = numAttrs;
		this.tablesInsertionsSum = tablesInsertionsSum;
		this.tablesDeletionsSum = tablesDeletionsSum;
		this.attrsInsWithTableInsSum = attrsInsWithTableInsSum;
		this.attrsbDelWithTableDelSum = attrsbDelWithTableDelSum;
		this.attrsInjectedSum = attrsInjectedSum;
		this.attrsEjectedSum = attrsEjectedSum;
		this.attrsWithTypeUpdSum = attrsWithTypeUpdSum;
		this.attrsInPKUpdSum = attrsInPKUpdSum;
		this.tableDeltaSum = tableDeltaSum;
		this.attrDeltaSum = attrDeltaSum;
		this.attrBirthsSum = attrBirthsSum;
		this.attrDeathsSum = attrDeathsSum;
		this.attrUpdsSum = attrUpdsSum;
		this.totalExpansion = totalExpansion;
		this.totalMaintenance = totalMaintenance;
		this.totalAttrActivity = totalAttrActivity;
	}
	
	public int getmID() {
		return mID;
	}
	public String getHumanTime() {
		return humanTime;
	}
	public int getNumCommits() {
		return numCommits;
	}
	public int getNumTables() {
		return numTables;
	}
	public int getNumAttrs() {
		return numAttrs;
	}
	public int getTablesInsertionsSum() {
		return tablesInsertionsSum;
	}
	public int getTablesDeletionsSum() {
		return tablesDeletionsSum;
	}
	public int getAttrsInsWithTableInsSum() {
		return attrsInsWithTableInsSum;
	}
	public int getAttrsbDelWithTableDelSum() {
		return attrsbDelWithTableDelSum;
	}
	public int getAttrsInjectedSum() {
		return attrsInjectedSum;
	}
	public int getAttrsEjectedSum() {
		return attrsEjectedSum;
	}
	public int getAttrsWithTypeUpdSum() {
		return attrsWithTypeUpdSum;
	}
	public int getAttrsInPKUpdSum() {
		return attrsInPKUpdSum;
	}
	public int getTableDeltaSum() {
		return tableDeltaSum;
	}
	public int getAttrDeltaSum() {
		return attrDeltaSum;
	}
	public int getAttrBirthsSum() {
		return attrBirthsSum;
	}
	public int getAttrDeathsSum() {
		return attrDeathsSum;
	}
	public int getAttrUpdsSum() {
		return attrUpdsSum;
	}
	public int getTotalExpansion() {
		return totalExpansion;
	}
	public int getTotalMaintenance() {
		return totalMaintenance;
	}
	public int getTotalAttrActivity() {
		return totalAttrActivity;
	}

	// probably not needed and to be deprecated	
	public int getIntValueByPosition(int position) {
		switch(position) {
		case 0:	 return getmID(); //break;
		//case 1:	 return getHumanTime(); //break;
		case 2:	 return getNumCommits(); //break;
		case 3:	 return getNumTables();//break;
		case 4:	 return getNumAttrs(); //break;
		case 5:	 return getTablesInsertionsSum(); //break;
		case 6:	 return getTablesDeletionsSum(); //break;
		case 7:	 return getAttrsInsWithTableInsSum(); //break;
		case 8:	 return getAttrsbDelWithTableDelSum(); //break;
		case 9:	 return getAttrsInjectedSum(); //break;
		case 10:	 return getAttrsEjectedSum(); //break;
		case 11:	 return getAttrsWithTypeUpdSum(); //break;
		case 12:	 return getAttrsInPKUpdSum();// break;
		case 13:	 return getTableDeltaSum();// break;
		case 14:	 return getAttrDeltaSum();// break;
		case 15:	 return getAttrBirthsSum();// break;
		case 16:	 return getAttrDeathsSum();// break;
		case 17:	 return getAttrUpdsSum();// break;
		case 18:	 return getTotalExpansion();// break;
		case 19:	 return getTotalMaintenance();// break;
		case 20:	 return getTotalAttrActivity();// break;
		default: return _ERROR_CODE;
		}//end switch
	}
	
	// probably not needed and to be deprecated	
	public String getStringValueByPosition(int position) {
		switch(position) {
		//case 0:	 return getmID(); //break;
		case 1:	 return getHumanTime(); //break;
		//case 2:	 return getNumCommits(); //break;
		//case 3:	 return getNumTables();//break;
		//case 4:	 return getNumAttrs(); //break;
		//case 5:	 return getTablesInsertionsSum(); //break;
		//case 6:	 return getTablesDeletionsSum(); //break;
		//case 7:	 return getAttrsInsWithTableInsSum(); //break;
		//case 8:	 return getAttrsbDelWithTableDelSum(); //break;
		//case 9:	 return getAttrsInjectedSum(); //break;
		//case 10:	 return getAttrsEjectedSum(); //break;
		//case 11:	 return getAttrsWithTypeUpdSum(); //break;
		//case 12:	 return getAttrsInPKUpdSum();// break;
		//case 13:	 return getTableDeltaSum();// break;
		//case 14:	 return getAttrDeltaSum();// break;
		//case 15:	 return getAttrBirthsSum();// break;
		//case 16:	 return getAttrDeathsSum();// break;
		//case 17:	 return getAttrUpdsSum();// break;
		//case 18:	 return getTotalExpansion();// break;
		//case 19:	 return getTotalMaintenance();// break;
		//case 20:	 return getTotalAttrActivity();// break;
		default: return _ERROR_STRING;
		}//end switch
	}

	@Override
	public String toString() {
		return mID + "\t" + humanTime + "\t" + numCommits + "\t" + numTables + "\t" + numAttrs 
				+ "\t" + tablesInsertionsSum + "\t" + tablesDeletionsSum + "\t" + attrsInsWithTableInsSum 
				+ "\t" + attrsbDelWithTableDelSum + "\t" + attrsInjectedSum + "\t" + attrsEjectedSum 
				+ "\t" + attrsWithTypeUpdSum + "\t" + attrsInPKUpdSum + "\t" + tableDeltaSum + "\t" 
				+ attrDeltaSum + "\t" + attrBirthsSum + "\t" + attrDeathsSum + "\t" + attrUpdsSum 
				+ "\t" + totalExpansion + "\t" + totalMaintenance + "\t" + totalAttrActivity;
	}
	
	private int mID;
	private String humanTime;
	private int numCommits;
	
	private int numTables;
	private int numAttrs;
	
	private int tablesInsertionsSum;
	private int tablesDeletionsSum;
	private int attrsInsWithTableInsSum;
	private int attrsbDelWithTableDelSum;
	private int attrsInjectedSum;
	private int attrsEjectedSum;
	private int attrsWithTypeUpdSum;
	private int attrsInPKUpdSum;
	
	private int tableDeltaSum;
	private int attrDeltaSum;
	private int attrBirthsSum;
	private int attrDeathsSum;
	private int attrUpdsSum;
	
	private int totalExpansion;
	private int totalMaintenance;
	private int totalAttrActivity;
	final public static int _ERROR_CODE = -1;
	final public static String _ERROR_STRING = "";

}
