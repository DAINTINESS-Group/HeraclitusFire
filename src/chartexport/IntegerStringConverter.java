package chartexport;

import javafx.util.StringConverter;

public class IntegerStringConverter extends StringConverter<Number>{
	@Override 
	public String toString(Number object) { 
		if(object.intValue()!=object.doubleValue()) 
			return ""; 
		return ""+(object.intValue()); 
	} 
	@Override 
	public Number fromString(String string) { 
		Number val = Double.parseDouble(string); 
		return val.intValue(); 
	} 
}
