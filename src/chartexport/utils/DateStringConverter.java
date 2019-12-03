package chartexport.utils;

import java.util.HashMap;

import javafx.util.StringConverter;

public class DateStringConverter extends StringConverter<Number>{
	
	public DateStringConverter() {
		this.labelMap = new HashMap<Number, String>();
		this.valueMap = new HashMap<String, Number>();
	}
	
	public void addToMaps(Number value, String label) {
		labelMap.put(value, label);
		valueMap.put(label, value);
	}
	
	@Override 
	public String toString(Number object) { 
		return labelMap.get(object.longValue()); 
	} 
	@Override 
	public Number fromString(String string) {
		return valueMap.get(string); 
	}
	
	private HashMap<Number, String> labelMap;
	private HashMap<String, Number> valueMap;

}
