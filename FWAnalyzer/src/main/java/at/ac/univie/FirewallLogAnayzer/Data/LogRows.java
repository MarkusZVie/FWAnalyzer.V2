package at.ac.univie.FirewallLogAnayzer.Data;

import java.io.Serializable;
import java.util.ArrayList;

public class LogRows implements Serializable{
	private static final long serialVersionUID = -6035879562045698487L;
	private ArrayList<LogRow> logRows;
	private static LogRows instance =null;
	private LogRows(){
		logRows = new ArrayList<>();
	}
	public static LogRows getInstance(){
		if(instance == null){
			instance = new LogRows();
		}
		return instance;
	}
	public ArrayList<LogRow> getLogRows() {
		return logRows;
	}
	
	public void addLogRow(LogRow logRow){
		logRows.add(logRow);
	}
	
}
