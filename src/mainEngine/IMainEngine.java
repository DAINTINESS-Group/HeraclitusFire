package mainEngine;

import java.util.ArrayList;

public interface IMainEngine<E> {
	
	public abstract int produceFiguresAndStats();
	public abstract String setupFolders();
	public abstract int loadData(String fileName, String delimeter, boolean hasHeaderLine, int numFields, ArrayList<String> headerList, ArrayList<E> objCollection);

}
