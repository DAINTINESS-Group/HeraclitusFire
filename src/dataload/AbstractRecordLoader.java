package dataload;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
//import java.util.StringTokenizer;


/**
* <h1>AbstractRecordLoader</h1>
* Class responsible for reading the data from
* an input file.
*
* @version 0.0
* @since   2017-07-22
*/
public abstract class AbstractRecordLoader <E>{

	public abstract int constructObjectFromRow(String [] tokens, ArrayList<E>objCollection);

	/**
 	 * Reads the data from the given file and stores them
 	 * in an ArrayList
 	 * 
 	 * @param fileName: name of the input file
 	 * @param delimeter: file delimeter
 	 * @param hasHeaderLine: specifies whether the file has a header
 	 * @param numFields: number of columns in the input file
 	 * @param objCollection: empty list which will be loaded with the data from the input file
 	 * 
 	 * @return the number of rows that are Processed
 	 */
	public int load(String fileName, String delimeter, boolean hasHeaderLine, int numFields, ArrayList<String> header, ArrayList<E> objCollection){
		if (numFields < 1){
			System.out.println("Wrong number of fields, less than 1!");
			System.exit(0);		
		}
		//Opening files for read and write, checking exception
		Scanner inputStream = null;
		try {
			inputStream = new Scanner(new FileInputStream(fileName));

		} catch (FileNotFoundException e) {
			System.out.println("Problem opening file: " + fileName);
			System.exit(0);
		}

		int count = 0;

		//process the title of the csv
		if(hasHeaderLine){
			String titleLine = inputStream.nextLine();
			String[] tokens = titleLine.split(delimeter);
			for(int i = 0; i< tokens.length; i++)
				header.add(tokens[i]);
			count++;
		}
		String line = "";
		//process the actual rows one by one
		while (inputStream.hasNextLine()) {
			line = inputStream.nextLine();
			count++;

			/*
			 * StringTokenizer tokenizer = new StringTokenizer(line, delimeter);
			 * if(tokenizer.countTokens() != numFields){
			 * System.out.println("Wrong Input format in file " + fileName +". I found " +
			 * tokenizer.countTokens() + " values, but I expect " + numFields +
			 * " values per row!"); // System.exit(0); }
			 * 
			 * String[] tokens = new String[numFields]; for (int i=0; i< numFields;i++){
			 * tokens[i] = tokenizer.nextToken(); }
			 */

			String[] tokens = line.split(delimeter);
			
			//ToDo: here add the method that takes the token and forms the object and puts it in the resultList			
			int objConstructionErrorCode;
			objConstructionErrorCode = constructObjectFromRow(tokens, objCollection);
			if (objConstructionErrorCode !=0){
				System.out.println("ObjParsingError. I found a problem at line " + count + " of file " + fileName);
				System.exit(0);
			}
		}		
		inputStream.close();
		System.out.println("Processed " + count + " rows and loaded " + objCollection.size() + " objects.");
		return count;
	}

}
