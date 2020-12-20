package datamodel;

public class TableDetailedStatsElement implements IElement{
	
	public TableDetailedStatsElement(String table, int duration, int birth, String death, int lastKnownVersion,
			String birthDate, String lkvDate, int yearOfBirth, int yearOfLKV, int durationDays,
			int schemaSizeBirth, int schemaSizeLKV, double schemaSizeAvg, double schemaSizeResizeRatio, int sumupd,
			int countvwupd, double atu, double updRate, double avgupdvolume, int survivalClass, int activityClass,
			int ladclass) {
		super();
		this.table = table;
		this.duration = duration;
		this.birth = birth;
		this.death = death;
		this.lastKnownVersion = lastKnownVersion;
		this.birthDate = birthDate;
		this.lkvDate = lkvDate;	 
		this.yearOfBirth = yearOfBirth;	
		this.yearOfLKV = yearOfLKV;	
		this.durationDays = durationDays;
		this.schemaSizeBirth = schemaSizeBirth;
		this.schemaSizeLKV = schemaSizeLKV;
		this.schemaSizeAvg = schemaSizeAvg;
		this.schemaSizeResizeRatio = schemaSizeResizeRatio;
		this.sumupd = sumupd;
		this.countvwupd = countvwupd;
		this.atu = atu;
		this.updRate = updRate;
		this.avgupdvolume = avgupdvolume;
		this.survivalClass = survivalClass;
		this.activityClass = activityClass;
		this.ladclass = ladclass;
	}
	
	public String getTable() {
		return table;
	}
	public int getDuration() {
		return duration;
	}
	public int getBirth() {
		return birth;
	}
	public String getDeath() {
		return death;
	}
	public int getLastKnownVersion() {
		return lastKnownVersion;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public String getLkvDate() {
		return lkvDate;
	}
	public int getYearOfBirth() {
		return yearOfBirth;
	}
	public int getYearOfLKV() {
		return yearOfLKV;
	}
	public int getDurationDays() {
		return durationDays;
	}
	public int getSchemaSizeBirth() {
		return schemaSizeBirth;
	}
	public int getSchemaSizeLKV() {
		return schemaSizeLKV;
	}
	public double getSchemaSizeAvg() {
		return schemaSizeAvg;
	}
	public double getSchemaSizeResizeRatio() {
		return schemaSizeResizeRatio;
	}
	public int getSumupd() {
		return sumupd;
	}
	public int getCountvwupd() {
		return countvwupd;
	}
	public double getAtu() {
		return atu;
	}
	public double getUpdRate() {
		return updRate;
	}
	public double getAvgupdvolume() {
		return avgupdvolume;
	}
	public int getSurvivalClass() {
		return survivalClass;
	}
	public int getActivityClass() {
		return activityClass;
	}
	public int getLadclass() {
		return ladclass;
	}
	
	//TODO: add a test	
	@Override
	public int getIntValueByPosition(int position) {
		switch(position) {
			//case 0:	 return getTable(); //break;
			case 1:	 return getDuration(); //break;
			case 2:	 return getBirth(); //break;
			//case 3:	 return getDeath(); //break;
			case 4:	 return getLastKnownVersion(); //break;
			//case 5:	 return getBirthDate(); //break;
			//case 6:	 return getLkvDate();//break;
			case 7:	 return getYearOfBirth();//break;
			case 8:	 return getYearOfLKV();//break;
			case 9:	 return getDurationDays();//break;
			case 10:	 return getSchemaSizeBirth(); //break;
			case 11:	 return getSchemaSizeLKV(); //break;
			//case 12:	 return getSchemaSizeAvg(); //break;
			//case 13:	 return getSchemaSizeResizeRatio(); //break;
			case 14:	 return getSumupd(); //break;
			case 15:	 return getCountvwupd(); //break;
			//case 16:	 return getATU(); break;
			//case 17:	 return getUpdRate(); break;
			//case 18:	 return getAvgUpdVolume(); break;
			case 19:	 return getSurvivalClass();// break;
			case 20:	 return getActivityClass();// break;
			case 21:	 return getLadclass();// break;
			default: return _ERROR_CODE;
		}//end switch
	}
	
	@Override
	public double getDoubleValueByPosition(int position) {
		switch(position) {
		//case 0:	 return getTable(); //break;
		//case 1:	 return getDuration(); //break;
		//case 2:	 return getBirth(); //break;
		//case 3:	 return getDeath(); //break;
		//case 4:	 return getLastKnownVersion(); //break;
		//case 5:	 return getBirthDate(); //break;
		//case 6:	 return getLkvDate();//break;
		//case 7:	 return getYearOfBirth();//break;
		//case 8:	 return getYearOfLKV();//break;
		//case 9:	 return getDurationDays();//break;
		//case 10:	 return getSchemaSizeBirth(); //break;
		//case 11:	 return getSchemaSizeLKV(); //break;
		//case 12:	 return getSchemaSizeAvg(); //break;
		//case 13:	 return getSchemaSizeResizeRatio(); //break;
		//case 14:	 return getSumupd(); //break;
		//case 15:	 return getCountvwupd(); //break;
		//case 16:	 return getATU(); break;
		//case 17:	 return getUpdRate(); break;
		//case 18:	 return getAvgUpdVolume(); break;
		//case 19:	 return getSurvivalClass();// break;
		//case 20:	 return getActivityClass();// break;
		//case 21:	 return getLadclass();// break;
		default: return _ERROR_CODE;
		}
	}
	
	@Override
	public String getStringValueByPosition(int position) {
		switch(position) {
			case 0:	 return getTable(); //break;
			//case 1:	 return getDuration(); //break;
			//case 2:	 return getBirth(); //break;
			case 3:	 return getDeath(); //break;
			//case 4:	 return getLastKnownVersion(); //break;
			case 5:	 return getBirthDate(); //break;
			case 6:	 return getLkvDate();//break;
			//case 7:	 return getYearOfBirth();//break;
			//case 8:	 return getYearOfLKV();//break;
			//case 9:	 return getDurationDays();//break;
			//case 10:	 return getSchemaSizeBirth(); //break;
			//case 11:	 return getSchemaSizeLKV(); //break;
			//case 12:	 return getSchemaSizeAvg(); //break;
			//case 13:	 return getSchemaSizeResizeRatio(); //break;
			//case 14:	 return getSumupd(); //break;
			//case 15:	 return getCountvwupd(); //break;
			//case 16:	 return getATU(); break;
			//case 17:	 return getUpdRate(); break;
			//case 18:	 return getAvgUpdVolume(); break;
			//case 19:	 return getSurvivalClass();// break;
			//case 20:	 return getActivityClass();// break;
			//case 21:	 return getLadclass();// break;
			default: return _ERROR_STRING;
		}//end switch
	}
	
	private String table;
	private int duration;
	private int birth;
	private String death;
	private int lastKnownVersion;
	
	private String birthDate;	//String or LocalDateTime @ pos 5 starting from 0, right after LKV
	private String lkvDate;	//String or LocalDateTime
	private int yearOfBirth;	
	private int yearOfLKV;	
	private int durationDays;
	
	private int schemaSizeBirth;
	private int schemaSizeLKV;
	private double schemaSizeAvg;
	private double schemaSizeResizeRatio;
	private int sumupd;
	private int countvwupd;
	private double atu;
	private double updRate;
	private double avgupdvolume;
	private int survivalClass;
	private int activityClass;
	private int ladclass;
//	final public static int _ERROR_CODE = -1;
//	final public static String _ERROR_STRING = "";

}

/*
 private String birthDate;	//String or LocalDateTime @ pos 5 starting from 0, right after LKV
 private String lkvDate;	//String or LocalDateTime
 private int yearOfBirth;	
 private int yearOfLKV;	
 private int durationDays;

 */
