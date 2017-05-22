package at.ac.univie.FirewallLogAnayzer.Data;

import java.util.ArrayList;

public class CompositionLogRow {
	private ArrayList<LogRow> content;

	public CompositionLogRow() {
		content = new ArrayList<>();
	}
	
	public CompositionLogRow(LogRow lr) {
		content = new ArrayList<>();
		content.add(lr);
	}
	
	
	public void addLogRow(LogRow logRow){
		content.add(logRow);
	}
	
	public ArrayList<LogRow> getContent(){
		return content;
	}
	
	
}
