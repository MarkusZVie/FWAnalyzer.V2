package at.ac.univie.FirewallLogAnayzer.Processing;

import java.nio.channels.spi.AbstractSelectionKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import at.ac.univie.FirewallLogAnayzer.Data.CompositionAnalysingSettings;
import at.ac.univie.FirewallLogAnayzer.Data.CompositionCompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.CompositionSelection;
import at.ac.univie.FirewallLogAnayzer.Data.HashPairDoubleValue;
import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRows;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByDescriptionLogLine;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByExplanation;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLocationCity;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLocationCountry;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLogLineCode;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByMinutes;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByProtocol;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupBySrcIP;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByrecommendedAction;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.IGroupByFactory;

public class ProcessingAnalyseThreats implements IProcessingAnalyseThreats{

	private ArrayList<LogRow> allLogRows;
	private ICompositionAnalysing compositionAnalysing;
	private IBasicFunctions basicFunctions;
	
	
	public ProcessingAnalyseThreats() {
		allLogRows = LogRows.getInstance().getLogRows();
		compositionAnalysing = new CompositionAnalysing();		
		basicFunctions = new BasicFunctions();
	}

	@Override
	public HashMap<String, Double> analyseForPortScanningOrFootPrinting() {
		
		//create Settings
		CompositionAnalysingSettings settings = new CompositionAnalysingSettings();
		settings.setDontCareByRecommendedActionNonRequired(true);
		settings.setDontCareByNoSrcIP(true);
		CompositionSelection[] compostionSelection = {new CompositionSelection(new GroupByLogLineCode(), "106023")}; //define which Lines will appier
		settings.setSelectOnlyGroubedByKey(compostionSelection);
		
		//filter Logs by Settings
		ArrayList<LogRow> filterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, settings);
		
		//create ThreadScore of Time Persistence by IP
		HashMap<String, Double> thredScore = compositionAnalysing.getSetOfPersistencingTransferingIps(filterdLogRowsBySetting, basicFunctions.getLogBeginDate(filterdLogRowsBySetting), basicFunctions.getLogEndDate(filterdLogRowsBySetting));

		return thredScore;
		
		
		
	}
	
	@Override
	public ArrayList<HashPairDoubleValue> genereateSortAbleFromDoubleHashMap(HashMap<String, Double> srcMap) {
		ArrayList<HashPairDoubleValue> sortedSrcMap = new ArrayList<>();
		for(String key :srcMap.keySet()){
			sortedSrcMap.add(new HashPairDoubleValue(key, srcMap.get(key)));
		}
		Collections.sort(sortedSrcMap);
		return sortedSrcMap;
	}
	

	@Override
	public void printStaticCompostionTree() {
		
		//create Settings
		CompositionAnalysingSettings settings = new CompositionAnalysingSettings();
		settings.setDontCareByRecommendedActionNonRequired(true);
		settings.setDontCareByNoSrcIP(true);
		
		//filter Logs by Settings
		ArrayList<LogRow> filterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, settings);
				
		//create Basic Compostion by one GroupBy
		CompositionCompositionLogRow cclr = compositionAnalysing.groupByLogLine(filterdLogRowsBySetting, new GroupByDescriptionLogLine());
		
		//Build Tree with custom GroupBys
		IGroupByFactory[] subGroups = {new GroupByExplanation(), new GroupByrecommendedAction(), new GroupByProtocol() ,new GroupByLocationCountry(), new GroupByLocationCity(), new GroupBySrcIP(), new GroupByMinutes()};
		cclr.makeSubComposition(subGroups);
		
		//print
		compositionAnalysing.printCCLogRow(cclr);
		
	}

	@Override
	public void analyseForDos() {
		//create Settings
		CompositionAnalysingSettings settings = new CompositionAnalysingSettings();
		
		CompositionSelection[] compostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "106101"), //noIP, ACL LogdenyFlows has reach the limit
				new CompositionSelection(new GroupByLogLineCode(), "109017"), //if persists from IP DoS Possible (proxy-Limit)
				new CompositionSelection(new GroupByLogLineCode(), "209003"), //if persists from IP DoS Possible (Fragment Database Limit)
				new CompositionSelection(new GroupByLogLineCode(), "210011"), //noIP, Connection limit exceeded
				new CompositionSelection(new GroupByLogLineCode(), "400033"), //UDP Chargen DoS attack
				new CompositionSelection(new GroupByLogLineCode(), "402128"), //noIP, persists -> Dos
				new CompositionSelection(new GroupByLogLineCode(), "404102"), //noIP, Exceeded embryonic limit
				new CompositionSelection(new GroupByLogLineCode(), "407002"), //noIP, Embryonic limit
				new CompositionSelection(new GroupByLogLineCode(), "733100") //noIP, Object drop rate rate_ID exceeded
		}; //define which Lines will appier
		
		settings.setSelectOnlyGroubedByKey(compostionSelection);
		
		System.out.println("..........");
		printCompostionTree(settings);
		
	}


	@Override
	public void printCompostionTree(CompositionAnalysingSettings settings) {
		//filter Logs by Settings
		ArrayList<LogRow> filterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, settings);
				
		//create Basic Compostion by one GroupBy
		CompositionCompositionLogRow cclr = compositionAnalysing.groupByLogLine(filterdLogRowsBySetting, new GroupByDescriptionLogLine());
		
		//Build Tree with custom GroupBys
		IGroupByFactory[] subGroups = {new GroupByExplanation(), new GroupByrecommendedAction(), new GroupByProtocol() ,new GroupByLocationCountry(), new GroupByLocationCity(), new GroupBySrcIP(), new GroupByMinutes()};
		cclr.makeSubComposition(subGroups);
		
		//print
		compositionAnalysing.printCCLogRow(cclr);
		
	}





	

}
