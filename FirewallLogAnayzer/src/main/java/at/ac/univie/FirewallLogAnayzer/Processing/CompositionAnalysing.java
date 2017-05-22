package at.ac.univie.FirewallLogAnayzer.Processing;

import java.util.ArrayList;
import java.util.HashMap;
import at.ac.univie.FirewallLogAnayzer.Data.CompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRows;

public class CompositionAnalysing {	
	



	public HashMap<String, CompositionLogRow> filterLogByLineCode(ArrayList<LogRow> logRows){
		HashMap<String,CompositionLogRow> composition = new HashMap<>();
		for(LogRow lr : logRows){
			if(composition.containsKey(lr.getLogLineCode()+"")){
				//add Log Row to existing entry
				composition.get(lr.getLogLineCode()+"").addLogRow(lr);
			}else{
				//add new Row and new entry
				composition.put(lr.getLogLineCode()+"", new CompositionLogRow(lr));
			}			
		}
		return composition;
	}
	
	public HashMap<String, CompositionLogRow> filterLogBySrcIP(ArrayList<LogRow> logRows){
		HashMap<String,CompositionLogRow> composition = new HashMap<>();
		for(LogRow lr : logRows){
			if(composition.containsKey(lr.getSrcIP()+"")){
				//add Log Row to existing entry
				composition.get(lr.getSrcIP()+"").addLogRow(lr);
			}else{
				//add new Row and new entry
				composition.put(lr.getSrcIP()+"", new CompositionLogRow(lr));
			}			
		}
		return composition;
	}
	
	
	public void annalyseComposition(HashMap<String, CompositionLogRow> composition){
		for(String key :composition.keySet()){
			System.out.println("Key: " + key + " Ammount: "+ composition.get(key).getContent().size());
		}
	}
}
