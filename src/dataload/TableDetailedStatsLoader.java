/**
 * 
 */
package dataload;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import datamodel.TableDetailedStatsElement;

/**
 * @author pvassil
 *
 */
public class TableDetailedStatsLoader extends AbstractRecordLoader<TableDetailedStatsElement> {


	@Override
	/**
 	 * Adds a record in the input list given the tokens of a row.
 	 * 
 	 * @param tokens: a list of fields from a row of the input file
 	 * @param objCollection: list which holds the information about the file contents
 	 * 
 	 * @return 0
 	 */
	public int constructObjectFromRow(String[] tokens, ArrayList<TableDetailedStatsElement> objCollection) {
		String table;
		int duration;
		int birth;
		String death; //is either a "-" or a number
		int lastKnownVersion;
		
		String birthDate;  //@pos5 Now, of the form:	11/02/2009 17:25
		String lkvDate;	  //of the form 07/05/2009 21:03
		int yearOfBirth;	
		int yearOfLKV;	
		int durationDays;
		
		int schemaSizeBirth;
		int schemaSizeLKV;
		double schemaSizeAvg;
		double schemaSizeResizeRatio;
		int sumupd;
		int countvwupd;
		double atu;
		double updRate;
		double avgupdvolume; //can be empty
		int survivalClass;
		int activityClass;
		int ladclass;

		table = tokens[0];
		duration = Integer.parseInt(tokens[1]);
		birth = Integer.parseInt(tokens[2]);
		death = tokens[3];				
		lastKnownVersion=Integer.parseInt(tokens[4]);

		birthDate = tokens[5];
		lkvDate = tokens[6];
		
		if(tokens[7].equals("")) {
			yearOfBirth = -1;
		}
		else {
			yearOfBirth =Integer.parseInt(tokens[7]); 
		}
			
		if(tokens[8].equals("")) {
			yearOfLKV = -1;
		}
		else {
			yearOfLKV =Integer.parseInt(tokens[8]); 
		}

		if(tokens[9].equals("")) {
			durationDays = -1;
		}
		else {
			durationDays =Integer.parseInt(tokens[9]); 
		}

		
		schemaSizeBirth=Integer.parseInt(tokens[10]);
		schemaSizeLKV=Integer.parseInt(tokens[11]);

		NumberFormat format = NumberFormat.getInstance(Locale.US);
		Number number=-Double.MIN_NORMAL;
		try {
			number = format.parse(tokens[12]);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		schemaSizeAvg = number.doubleValue();
		number=-Double.MIN_NORMAL;
		
		try {
			number = format.parse(tokens[13]);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		schemaSizeResizeRatio= number.doubleValue();
		//schemaSizeResizeRatio=Double.parseDouble(tokens[13]);
		
		sumupd=Integer.parseInt(tokens[14]);
		countvwupd=Integer.parseInt(tokens[15]);
		number=-Double.MIN_NORMAL;
		
		try {
			number = format.parse(tokens[16]);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		atu = number.doubleValue(); 
		//atu=Double.parseDouble(tokens[16]);
		number=-Double.MIN_NORMAL;
		
		try {
			number = format.parse(tokens[17]);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		updRate = number.doubleValue();
		//updRate=Double.parseDouble(tokens[17]);
		number=-Double.MIN_NORMAL;
		
		try {
			if(tokens[18].equals(""))
				avgupdvolume = 0.0;
			else
				number = format.parse(tokens[18]);
			//avgupdvolume=Double.parseDouble(tokens[18]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		avgupdvolume = number.doubleValue();
		number=-Double.MIN_NORMAL;
		
		survivalClass=Integer.parseInt(tokens[19]);
		activityClass=Integer.parseInt(tokens[20]);
		ladclass=Integer.parseInt(tokens[21]);


		TableDetailedStatsElement element = new TableDetailedStatsElement(table, duration, birth, death, lastKnownVersion,
				birthDate, lkvDate, yearOfBirth, yearOfLKV, durationDays,
				schemaSizeBirth, schemaSizeLKV, schemaSizeAvg, schemaSizeResizeRatio, sumupd,
				countvwupd, atu, updRate, avgupdvolume, survivalClass, activityClass,
				ladclass); 
		objCollection.add(element);
		
		return 0;
	}

}
