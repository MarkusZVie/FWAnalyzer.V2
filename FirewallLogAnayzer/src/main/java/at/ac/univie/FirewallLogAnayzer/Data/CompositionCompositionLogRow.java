package at.ac.univie.FirewallLogAnayzer.Data;

import java.util.ArrayList;
import java.util.HashMap;

import at.ac.univie.FirewallLogAnayzer.Processing.CompositionAnalysing;
import at.ac.univie.FirewallLogAnayzer.Processing.ICompositionAnalysing;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupBySrcIP;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.IGroupByFactory;

public class CompositionCompositionLogRow {
	private HashMap<String,CompositionCompositionLogRow> ccLogRow;
	private HashMap<String,CompositionLogRow> composition;
	private ICompositionAnalysing compositionAnalysing = new CompositionAnalysing();
	private int deepnessLevel=0;
	private IGroupByFactory groubedBy;
	
	
	public IGroupByFactory getGroubedBy() {
		return groubedBy;
	}

	public void setGroubedBy(IGroupByFactory groubedBy) {
		this.groubedBy = groubedBy;
	}

	public CompositionCompositionLogRow(HashMap<String, CompositionLogRow> composition) {
		super();
		this.composition = composition;
	}

	public void makeSubComposition(IGroupByFactory[] subGroups) {
		ccLogRow = new HashMap<String,CompositionCompositionLogRow>();
		for(String key :composition.keySet()){
			//search for each key the CompositionLogRow
			CompositionLogRow clr = composition.get(key);
			//put for every key a CompositionCompositionLogRow with the logRows from that CompositionCompositionLogRow (Content)
			if(deepnessLevel<subGroups.length){
				CompositionCompositionLogRow thisLevelcclr = compositionAnalysing.groupByLogLine(clr.getContent(), subGroups[deepnessLevel]);
				ccLogRow.put(key, thisLevelcclr);
				ccLogRow.get(key).setDeepnessLevel(deepnessLevel+1);
				ccLogRow.get(key).makeSubComposition(subGroups);
			}
		}
	}
	
	public ArrayList<LogRow> getAllLogRows(){
		ArrayList<LogRow> logRows= new ArrayList<>();
		for(String key:composition.keySet()){
			for(LogRow lr : composition.get(key).getContent()){
				logRows.add(lr);
			}
		}
		return logRows;
	}
	
	public int getDeepnessLevel() {
		return deepnessLevel;
	}

	public void setDeepnessLevel(int deepnessLevel) {
		this.deepnessLevel = deepnessLevel;
	}

	public void setCcLogRow(HashMap<String, CompositionCompositionLogRow> ccLogRow) {
		this.ccLogRow = ccLogRow;
	}



	public void setComposition(HashMap<String, CompositionLogRow> composition) {
		this.composition = composition;
	}



	public HashMap<String,CompositionCompositionLogRow> getCcLogRow() {
		return ccLogRow;
	}

	public HashMap<String, CompositionLogRow> getComposition() {
		return composition;
	}



	
	

	
	
}
