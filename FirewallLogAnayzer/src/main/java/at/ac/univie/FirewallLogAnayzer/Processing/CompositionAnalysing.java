package at.ac.univie.FirewallLogAnayzer.Processing;

import java.util.ArrayList;
import java.util.HashMap;

import at.ac.univie.FirewallLogAnayzer.Data.CompositionAnalysingSettings;
import at.ac.univie.FirewallLogAnayzer.Data.CompositionCompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.CompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRows;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.IGroupByFactory;

public abstract class CompositionAnalysing {	
	

	public static ArrayList<LogRow> eliminateUnnecessaryRowsBySetting(ArrayList<LogRow> baseRows,CompositionAnalysingSettings setting){
		ArrayList<LogRow> filterdList = new ArrayList<LogRow>();
		
		for(LogRow lr : baseRows){
			boolean willBeAdded = true;
			if(setting.isDontCareByRecommendedActionNonRequired()){
				if(lr.getRecommendedAction().equals("None required.")){
					willBeAdded = false;
				}
			}
			if(setting.isDontCareByNoSrcIP()){
				if(lr.getSrcIP()==null){
					willBeAdded = false;
				}
			}
			if(willBeAdded){
				filterdList.add(lr);
			}
		}
		
		
		return filterdList;
	}

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
			int ammount = composition.get(key).getContent().size();
			if(ammount>1){
				for(int i=0; i<deppnessLevel; i++){
					System.out.print("\t");
				}
				System.out.println("Key: " + key + " ("+ composition.get(key).getContent().size()+")");
			}
			if(ccLR.getCcLogRow()!=null){
				HashMap<String,CompositionCompositionLogRow> lowerccLR = ccLR.getCcLogRow();
				if(lowerccLR.containsKey(key)){
					printCCLogRow(lowerccLR.get(key));
				}
			}
		}
	}
}
