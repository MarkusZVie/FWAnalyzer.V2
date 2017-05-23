package at.ac.univie.FirewallLogAnayzer.Processing;

import java.util.ArrayList;
import java.util.HashMap;

import at.ac.univie.FirewallLogAnayzer.Data.CompositionCompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.CompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRows;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.IGroupByFactory;

public abstract class CompositionAnalysing {	
	

	

	public static CompositionCompositionLogRow groupByLogLine(ArrayList<LogRow> logRows,IGroupByFactory iGroupByFactory){
		HashMap<String,CompositionLogRow> composition = new HashMap<>();
		for(LogRow lr : logRows){
			String key = iGroupByFactory.getKey(lr);
			if(composition.containsKey(key)){
				//add Log Row to existing entry
				composition.get(key).addLogRow(lr);
			}else{
				//add new Row and new entry
				composition.put(key, new CompositionLogRow(lr));
			}			
		}
		return new CompositionCompositionLogRow(composition);
	}
	
	
	
	public static void printCCLogRow(CompositionCompositionLogRow ccLR){
		HashMap<String,CompositionLogRow> composition = ccLR.getComposition();
		int deppnessLevel = ccLR.getDeepnessLevel();
		for(String key :composition.keySet()){
			
			for(int i=0; i<deppnessLevel; i++){
				System.out.print("\t");
			}
			
			System.out.println("Key: " + key + " Ammount: "+ composition.get(key).getContent().size());
			if(ccLR.getCcLogRow()!=null){
				HashMap<String,CompositionCompositionLogRow> lowerccLR = ccLR.getCcLogRow();
				if(lowerccLR.containsKey(key)){
					printCCLogRow(lowerccLR.get(key));
				}
			}
		}
	}
}
