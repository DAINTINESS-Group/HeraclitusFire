package datamodel;

public class SchemaHeartbeatElement implements IElement{
//delete this comment
	public SchemaHeartbeatElement(int trID, String epochTime, String oldVer, String newVer, String humanTime,
			int distFromV0InDays, int runningYearFromV0, int runningMonthFromV0, int numOldTables, int numNewTables,
			int numOldAttrs, int numNewAttrs, int tablesIns, int tablesDel, int attrsInsWithTableIns,
			int attrsbDelWithTableDel, int attrsInjected, int attrsEjected, int attrsWithTypeUpd, int attrsInPKUpd,
			int tableDelta, int attrDelta, int attrBirthsSum, int attrDeathsSum, int attrUpdsSum, int expansion,
			int maintenance, int totalAttrActivity) {
		super();
		this.trID = trID;
		this.epochTime = epochTime;
		this.oldVer = oldVer;
		this.newVer = newVer;
		this.humanTime = humanTime;
		this.distFromV0InDays = distFromV0InDays;
		this.runningYearFromV0 = runningYearFromV0;
		this.runningMonthFromV0 = runningMonthFromV0;
		this.numOldTables = numOldTables;
		this.numNewTables = numNewTables;
		this.numOldAttrs = numOldAttrs;
		this.numNewAttrs = numNewAttrs;
		this.tablesIns = tablesIns;
		this.tablesDel = tablesDel;
		this.attrsInsWithTableIns = attrsInsWithTableIns;
		this.attrsbDelWithTableDel = attrsbDelWithTableDel;
		this.attrsInjected = attrsInjected;
		this.attrsEjected = attrsEjected;
		this.attrsWithTypeUpd = attrsWithTypeUpd;
		this.attrsInPKUpd = attrsInPKUpd;
		this.tableDelta = tableDelta;
		this.attrDelta = attrDelta;
		this.attrBirthsSum = attrBirthsSum;
		this.attrDeathsSum = attrDeathsSum;
		this.attrUpdsSum = attrUpdsSum;
		this.expansion = expansion;
		this.maintenance = maintenance;
		this.totalAttrActivity = totalAttrActivity;
	}
	
	public int getTrID() {
		return trID;
	}
	public String getEpochTime() {
		return epochTime;
	}
	public String getOldVer() {
		return oldVer;
	}
	public String getNewVer() {
		return newVer;
	}
	public String getHumanTime() {
		return humanTime;
	}
	public int getDistFromV0InDays() {
		return distFromV0InDays;
	}
	public int getRunningYearFromV0() {
		return runningYearFromV0;
	}
	public int getRunningMonthFromV0() {
		return runningMonthFromV0;
	}
	public int getNumOldTables() {
		return numOldTables;
	}
	public int getNumNewTables() {
		return numNewTables;
	}
	public int getNumOldAttrs() {
		return numOldAttrs;
	}
	public int getNumNewAttrs() {
		return numNewAttrs;
	}
	public int getTablesIns() {
		return tablesIns;
	}
	public int getTablesDel() {
		return tablesDel;
	}
	public int getAttrsInsWithTableIns() {
		return attrsInsWithTableIns;
	}
	public int getAttrsbDelWithTableDel() {
		return attrsbDelWithTableDel;
	}
	public int getAttrsInjected() {
		return attrsInjected;
	}
	public int getAttrsEjected() {
		return attrsEjected;
	}
	public int getAttrsWithTypeUpd() {
		return attrsWithTypeUpd;
	}
	public int getAttrsInPKUpd() {
		return attrsInPKUpd;
	}
	public int getTableDelta() {
		return tableDelta;
	}
	public int getAttrDelta() {
		return attrDelta;
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
	public int getExpansion() {
		return expansion;
	}
	public int getMaintenance() {
		return maintenance;
	}
	public int getTotalAttrActivity() {
		return totalAttrActivity;
	}
	
	//TODO: add a test	
	@Override
	public int getIntValueByPosition(int position) {
		switch(position) {
			case 0:	 return getTrID(); //break;
			//case 1:	 return getEpochTime(); //break;
			//case 2:	 return getOldVer(); //break;
			//case 3:	 return getNewVer(); //break;
			//case 4:	 return getHumanTime(); //break;
			case 5:	 return getDistFromV0InDays(); //break;
			case 6:	 return getRunningYearFromV0();//break;
			case 7:	 return getRunningMonthFromV0();//break;
			case 8:	 return getNumOldTables();//break;
			case 9:	 return getNumNewTables();//break;
			case 10:	 return getNumOldAttrs(); //break;
			case 11:	 return getNumNewAttrs(); //break;
			case 12:	 return getTablesIns(); //break;
			case 13:	 return getTablesDel(); //break;
			case 14:	 return getAttrsInsWithTableIns(); //break;
			case 15:	 return getAttrsbDelWithTableDel(); //break;
			case 16:	 return getAttrsInjected(); //break;
			case 17:	 return getAttrsEjected(); //break;
			case 18:	 return getAttrsWithTypeUpd(); //break;
			case 19:	 return getAttrsInPKUpd();// break;
			case 20:	 return getTableDelta();// break;
			case 21:	 return getAttrDelta();// break;
			case 22:	 return getAttrBirthsSum();// break;
			case 23:	 return getAttrDeathsSum();// break;
			case 24:	 return getAttrUpdsSum();// break;
			case 25:	 return getExpansion();// break;
			case 26:	 return getMaintenance();// break;
			case 27:	 return getTotalAttrActivity();// break;
			default: return _ERROR_CODE;
		}//end switch
	}
	
	@Override
	public String getStringValueByPosition(int position) {
		switch(position) {
			//case 0:	 return getTrID(); //break;
			case 1:	 return getEpochTime(); //break;
			case 2:	 return getOldVer(); //break;
			case 3:	 return getNewVer(); //break;
			case 4:	 return getHumanTime(); //break;
			//case 5:	 return getDistFromV0InDays(); //break;
			//case 6:	 return getRunningYearFromV0();//break;
			//case 7:	 return getRunningMonthFromV0();//break;
			//case 8:	 return getNumOldTables();//break;
			//case 9:	 return getNumNewTables();//break;
			//case 10:	 return getNumOldAttrs(); //break;
			//case 11:	 return getNumNewAttrs(); //break;
			//case 12:	 return getTablesIns(); //break;
			//case 13:	 return getTablesDel(); //break;
			//case 14:	 return getAttrsInsWithTableIns(); //break;
			//case 15:	 return getAttrsbDelWithTableDel(); //break;
			//case 16:	 return getAttrsInjected(); //break;
			//case 17:	 return getAttrsEjected(); //break;
			//case 18:	 return getAttrsWithTypeUpd(); //break;
			//case 19:	 return getAttrsInPKUpd();// break;
			//case 20:	 return getTableDelta();// break;
			//case 21:	 return getAttrDelta();// break;
			//case 22:	 return getAttrBirthsSum();// break;
			//case 23:	 return getAttrDeathsSum();// break;
			//case 24:	 return getAttrUpdsSum();// break;
			//case 25:	 return getExpansion();// break;
			//case 26:	 return getMaintenance();// break;
			//case 27:	 return getTotalAttrActivity();// break;
			default: return _ERROR_STRING;
		}//end switch
	}

	private int trID;
	private String epochTime;
	private String oldVer;
	private String newVer;
	private String humanTime;
	private int distFromV0InDays;
	private int runningYearFromV0;
	private int runningMonthFromV0;
	
	private int numOldTables;
	private int numNewTables;
	private int numOldAttrs;
	private int numNewAttrs;
	private int tablesIns;
	private int tablesDel;
	private int attrsInsWithTableIns;
	private int attrsbDelWithTableDel;
	private int attrsInjected;
	private int attrsEjected;
	private int attrsWithTypeUpd;
	private int attrsInPKUpd;
	
	private int tableDelta;
	private int attrDelta;
	private int attrBirthsSum;
	private int attrDeathsSum;
	private int attrUpdsSum;
	private int expansion;
	private int maintenance;
	private int totalAttrActivity;
//	final public static int _ERROR_CODE = -1;
//	final public static String _ERROR_STRING = "";

}
