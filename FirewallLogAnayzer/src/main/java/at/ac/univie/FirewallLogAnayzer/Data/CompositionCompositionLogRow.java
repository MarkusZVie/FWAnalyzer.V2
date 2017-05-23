package at.ac.univie.FirewallLogAnayzer.Data;

import java.util.HashMap;

import at.ac.univie.FirewallLogAnayzer.Processing.CompositionAnalysing;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupBySrcIP;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.IGroupByFactory;

public class CompositionCompositionLogRow {
	private HashMap<String,CompositionCompositionLogRow> ccLogRow;
	private HashMap<String,CompositionLogRow> composition;
	private int deepnessLevel;
		
	public CompositionCompositionLogRow(){
		deepnessLevel=0;
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
				ccLogRow.put(key, CompositionAnalysing.groupByLogLine(clr.getContent(), subGroups[deepnessLevel]));
				ccLogRow.get(key).setDeepnessLevel(deepnessLevel+1);
				ccLogRow.get(key).makeSubComposition(subGroups);
			}
		}
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
