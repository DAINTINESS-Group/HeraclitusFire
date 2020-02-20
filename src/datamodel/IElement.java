package datamodel;

public interface IElement {
	
	final public static int _ERROR_CODE = -1;
	final public static String _ERROR_STRING = "";
	
	public abstract int getIntValueByPosition(int position);
	public abstract String getStringValueByPosition(int position);

}
