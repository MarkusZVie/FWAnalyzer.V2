package at.ac.univie.FirewallLogAnayzer.Processing;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import at.ac.univie.FirewallLogAnayzer.Data.CompositionAnalysingSettings;
import at.ac.univie.FirewallLogAnayzer.Data.CompositionCompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.CompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRows;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByDescriptionLogLine;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByExplanation;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLocationCity;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLocationCountry;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByMinutes;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByProtocol;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupBySrcIP;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByrecommendedAction;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.IGroupByFactory;
import org.apache.commons.math3.*;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public abstract class CompositionAnalysing {	
	
	private static final double STANDARD_DEVIATION_PERSISTENCE_THRESHOLD = 300.0;
	private static final double MEDIAN_PERSISTENCE_THRESHOLD = 0.0;
	private static final double TRIMEDMEAN_PERSISTENCE_THRESHOLD = 0.0;
	private static final double MEAN_PERSISTENCE_THRESHOLD = 0.0;

	
	public static ArrayList<LogRow> eliminateUnnecessaryRowsBySetting(ArrayList<LogRow> baseRows,CompositionAnalysingSettings setting){
		//Filtering by Setting obj.
		ArrayList<LogRow> filterdList = new ArrayList<LogRow>();
		if(setting == null){
			return baseRows;
		}
		for(LogRow lr : baseRows){
			boolean willBeAdded = true;
			if(setting.isDontCareByRecommendedActionNonRequired()){
				if(lr.getRecommendedAction().equals("None required.")){
					willBeAdded = false;
				}
			}
			if(setting.isDontCareByNoSrcIP()){
				//no IP or no Interface, because Interfaces are always on the Firewall side.
				if(lr.getSrcIP()==null||StaticFunctions.searchTheNIpInRow(lr.getSrcIP(), 1)==null){
					willBeAdded = false;
				}
			}
			if(willBeAdded){
				filterdList.add(lr);
			}
		}
		
		
		return filterdList;
	}
	
	public static void getSetOfPersistencingTransferingIps(ArrayList<LogRow> logRows, Date logBegin, Date logEnd){
		
		
		CompositionCompositionLogRow cclr = CompositionAnalysing.groupByLogLine(logRows, new GroupBySrcIP());
		HashMap<String,CompositionLogRow> composition = cclr.getComposition();
		for(String key :composition.keySet()){
			if(composition.get(key).getContent().size()>1){
				for(LogRow lr : composition.get(key).getContent()){
					System.out.println(lr.toString());
				}
				System.out.println("------------");
				for(double d: getStatisticsAboutTimeFriquent(composition.get(key).getContent(), logBegin, logEnd,true)){
					System.out.println(d);
				}
				System.out.println("---------------------------------");
				
			}
		}
	}
	public static boolean makeDecisionOfSignificantLogPersistance(double[] stats){
		
		
		return false;
	}
	
	public static double[] getStatisticsAboutTimeFriquent(ArrayList<LogRow> logRows,Date logBegin, Date logEnd, boolean ignoreDayNextDayJumps){
		//make dateList
		ArrayList<Date> datlist = new ArrayList<>();
		for(LogRow lr : logRows){
			datlist.add(lr.getDateTime());
		}
		Collections.sort(datlist);
		
		ArrayList<Long> timeSpacesBetweenLogs = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date savedDateForDiffrece = logBegin;
		for(Date d: datlist){
			if(ignoreDayNextDayJumps){
				if(sdf.format(d).equals(sdf.format(savedDateForDiffrece))){
					timeSpacesBetweenLogs.add(d.getTime()-savedDateForDiffrece.getTime());
				}
				savedDateForDiffrece = d;
			}else{
				timeSpacesBetweenLogs.add(d.getTime()-savedDateForDiffrece.getTime());
				savedDateForDiffrece = d;
			}
			
		}
		if(ignoreDayNextDayJumps){
			if(sdf.format(logEnd).equals(sdf.format(savedDateForDiffrece))){
				timeSpacesBetweenLogs.add(logEnd.getTime()-savedDateForDiffrece.getTime());
			}
		}else{
			timeSpacesBetweenLogs.add(logEnd.getTime()-savedDateForDiffrece.getTime());
		}
		DescriptiveStatistics ds = new DescriptiveStatistics();
		for(Long l:timeSpacesBetweenLogs){
			System.out.println("l    " + l);
			ds.addValue(Double.parseDouble(l/1000+""));
		}
		double standardDeviation = ds.getStandardDeviation();
		double median = ds.getPercentile(50);
		double trimedMean = ds.getPercentile(45);
		double mean = ds.getMean();
		
		double[] persistanceStats = {standardDeviation,median,trimedMean,mean};
		
		return persistanceStats;
	}

	public static CompositionCompositionLogRow groupByLogLine(ArrayList<LogRow> logRows,IGroupByFactory iGroupByFactory){
		HashMap<String,CompositionLogRow> composition = new HashMap<>();
		for(LogRow lr : logRows){
			String key = iGroupByFactory.getKey(lr);
			if(key==null){
				key = StaticFunctions.getNullString();
			}
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
		
		Map<String, CompositionLogRow> treeMap = new TreeMap<String, CompositionLogRow>(composition);
				
		for(String key :treeMap.keySet()){
				for(int i=0; i<deppnessLevel; i++){
					System.out.print("\t");
				}
				System.out.println("Key: " + key + " ("+ composition.get(key).getContent().size()+")");
			if(ccLR.getCcLogRow()!=null){
				HashMap<String,CompositionCompositionLogRow> lowerccLR = ccLR.getCcLogRow();
				if(lowerccLR.containsKey(key)){
					printCCLogRow(lowerccLR.get(key));
				}
			}
		}
	}
}
