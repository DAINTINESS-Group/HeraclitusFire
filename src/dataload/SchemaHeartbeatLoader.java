/**
 * 
 */
package dataload;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import datamodel.SchemaHeartbeatElement;

/**
 * @author alexvou
 *
 */
public class SchemaHeartbeatLoader extends AbstractRecordLoader<SchemaHeartbeatElement> {


	@Override
	/**
 	 * Adds a record in the input list given the tokens of a row.
 	 * 
 	 * @param tokens: a list of fields from a row of the input file
 	 * @param objCollection: list which holds the information about the file contents
 	 * 
 	 * @return 0
 	 */
	public int constructObjectFromRow(String[] tokens, ArrayList<SchemaHeartbeatElement> objCollection) {
		int trID;
		String epochTime;
		String oldVer;  // can be empty
		String newVer;
		String humanTime;  // can be empty
		int distFromV0InDays;  // can be empty
		int runningYearFromV0;  // can be empty
		int runningMonthFromV0;  // can be empty
		
		int numOldTables;  // can be empty
		int numNewTables;
		int numOldAttrs;  // can be empty
		int numNewAttrs;
		int tablesIns;
		int tablesDel;
		int attrsInsWithTableIns;
		int attrsbDelWithTableDel;
		int attrsInjected;
		int attrsEjected;
		int attrsWithTypeUpd;
		int attrsInPKUpd;
		
		int tableDelta;  // can be empty or negative
		int attrDelta;  // can be empty or negative
		int attrBirthsSum;
		int attrDeathsSum;
		int attrUpdsSum;
		int expansion;
		int maintenance;
		int totalAttrActivity;
		
		trID = Integer.parseInt(tokens[0]);  // possibly wrap in if stat for false check
		epochTime = tokens[1];
		oldVer = tokens[2];
		newVer = tokens[3];
		humanTime = tokens[4];
		if(tokens[5].equals("")) {
			distFromV0InDays = -1;
		}
		else {
			distFromV0InDays =Integer.parseInt(tokens[5]);
		}
		if(tokens[6].equals("")) {
			runningYearFromV0 = -1;
		}
		else {
			runningYearFromV0 =Integer.parseInt(tokens[6]);
		}
		if(tokens[7].equals("")) {
			runningMonthFromV0 = -1;
		}
		else {
			runningMonthFromV0 =Integer.parseInt(tokens[7]);
		}
		
		if(tokens[8].equals("")) {
			numOldTables = 0;
		}
		else {
			numOldTables =Integer.parseInt(tokens[8]);
		}
		numNewTables =Integer.parseInt(tokens[9]);
		if(tokens[10].equals("")) {
			numOldAttrs = 0;
		}
		else {
			numOldAttrs =Integer.parseInt(tokens[10]);
		}
		numNewAttrs =Integer.parseInt(tokens[11]);
		tablesIns =Integer.parseInt(tokens[12]);
		tablesDel =Integer.parseInt(tokens[13]);
		attrsInsWithTableIns =Integer.parseInt(tokens[14]);
		attrsbDelWithTableDel =Integer.parseInt(tokens[15]);
		attrsInjected =Integer.parseInt(tokens[16]);
		attrsEjected =Integer.parseInt(tokens[17]);
		attrsWithTypeUpd =Integer.parseInt(tokens[18]);
		attrsInPKUpd =Integer.parseInt(tokens[19]);
		
		if(tokens[20].equals("")) {
			tableDelta = 0;
		}
		else {
			tableDelta =Integer.parseInt(tokens[20]);
		}
		if(tokens[21].equals("")) {
			attrDelta = 0;
		}
		else {
			attrDelta =Integer.parseInt(tokens[21]);
		}
		attrBirthsSum =Integer.parseInt(tokens[22]);
		attrDeathsSum =Integer.parseInt(tokens[23]);
		attrUpdsSum =Integer.parseInt(tokens[24]);
		expansion =Integer.parseInt(tokens[25]);
		maintenance =Integer.parseInt(tokens[26]);
		totalAttrActivity =Integer.parseInt(tokens[27]);
		
		
		SchemaHeartbeatElement element = new SchemaHeartbeatElement(trID, epochTime, oldVer, newVer, humanTime, 
				distFromV0InDays, runningYearFromV0, runningMonthFromV0, numOldTables, numNewTables, numOldAttrs, 
				numNewAttrs, tablesIns, tablesDel, attrsInsWithTableIns, attrsbDelWithTableDel, attrsInjected, 
				attrsEjected, attrsWithTypeUpd, attrsInPKUpd, tableDelta, attrDelta, attrBirthsSum, attrDeathsSum, 
				attrUpdsSum, expansion, maintenance, totalAttrActivity); 
		objCollection.add(element);
		
		return 0;
	}

}
