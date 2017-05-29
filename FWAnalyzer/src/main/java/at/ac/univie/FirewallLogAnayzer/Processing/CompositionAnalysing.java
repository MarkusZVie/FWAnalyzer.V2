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
import at.ac.univie.FirewallLogAnayzer.Data.CompositionSelection;
import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRows;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByDays;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByDescriptionLogLine;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByDestIP;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByExplanation;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByFwIP;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByHours;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLocationCity;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLocationCountry;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLogLine;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLogLineCode;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByMinutes;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByMonth;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByPriority;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByProtocol;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupBySrcIP;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupBySrcPort;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByrecommendedAction;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.IGroupByFactory;
import org.apache.commons.math3.*;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class CompositionAnalysing implements ICompositionAnalysing{	

	private IBasicFunctions basicFunctions;
		
	public CompositionAnalysing() {
		basicFunctions = new BasicFunctions();
	}

	public ArrayList<LogRow> eliminateUnnecessaryRowsBySetting(ArrayList<LogRow> baseRows,CompositionAnalysingSettings setting){
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
				if(lr.getSrcIP()==null||basicFunctions.searchTheNIpInRow(lr.getSrcIP(), 1)==null){
					willBeAdded = false;
				}
			}
			if(setting.getSelectOnlyGroubedByKey()!=null){
				boolean isPartOfSelection = false;
				for(CompositionSelection selection: setting.getSelectOnlyGroubedByKey()){
					String key = selection.getSelectetKey();
					IGroupByFactory gbf = selection.getGbf();
					if(gbf.getKey(lr).equals(key)){
						isPartOfSelection=true;
					}
				}
				if(!isPartOfSelection){
					willBeAdded = false;
				}
				
				
			}
			if(willBeAdded){
				filterdList.add(lr);
			}
		}
		
		
		return filterdList;
	}
	
	public HashMap<String, Double> getSetOfPersistencingTransferingIps(ArrayList<LogRow> logRows, Date logBegin, Date logEnd){
		HashMap<String, Double> threadList = new HashMap<>();
		CompositionCompositionLogRow cclr = groupByLogLine(logRows, new GroupBySrcIP());
		HashMap<String,CompositionLogRow> composition = cclr.getComposition();
		
		for(String key :composition.keySet()){
			if(composition.get(key).getContent().size()>1){				// only possible to get stats by size >1
				double[] stats = getStatisticsAboutTimeFriquent(composition.get(key).getContent(), logBegin, logEnd,true);
				double ammountPerHour = getAmmountPerHour(composition.get(key).getContent(), logBegin, logEnd);
				double threadScore = getThreadScore(stats, ammountPerHour);
				if(threadList.containsKey(key)){			//defensive
					if(threadList.get(key)<threadScore){
						threadList.put(key, threadScore);
					}
				}else{
					threadList.put(key, threadScore);
				}
			}
		}
		return threadList;
	}
	
	public double getThreadScore(double[] stats, double ammountPerHour){
		//middle ammountPerHour and aritmetical mean (mean is in sec)
		double threadScore = (ammountPerHour+(stats[3]/3600))/2;
		//div by standard devision (is also in sec)
		threadScore = threadScore/Math.log(((stats[0]/3600))+Math.E);
		if((threadScore+"").equals("NaN")){
			threadScore =0;
		}
		return threadScore;
	}
	
	@Override
	public double getAmmountPerHour(ArrayList<LogRow> logRows,Date logBegin, Date logEnd){
		double ammountOfLogs = logRows.size();
		double ammountOfHours = (((logEnd.getTime()-logBegin.getTime())/1000)/60)/60;
		double ammountPerHour = ammountOfLogs/ammountOfHours;
		return ammountPerHour;
	}
	
	public double[] getStatisticsAboutTimeFriquent(ArrayList<LogRow> logRows,Date logBegin, Date logEnd, boolean ignoreDayNextDayJumps){
		//double[0] = standardDeviation
		//double[1] = median
		//double[2] = trimedMean
		//double[3] = mean (aritmetical)
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
			ds.addValue(Double.parseDouble(l/1000+"")); //no need for milisecunds
		}
		double standardDeviation = ds.getStandardDeviation();
		double median = ds.getPercentile(50);
		double trimedMean = ds.getPercentile(45);
		double mean = ds.getMean();
		
		double[] persistanceStats = {standardDeviation,median,trimedMean,mean};
		
		return persistanceStats;
	}

	public CompositionCompositionLogRow groupByLogLine(ArrayList<LogRow> logRows,IGroupByFactory iGroupByFactory){
		HashMap<String,CompositionLogRow> composition = new HashMap<>();
		for(LogRow lr : logRows){
			String key = iGroupByFactory.getKey(lr);
			if(key==null){
				key = basicFunctions.getNullString();
			}
			if(composition.containsKey(key)){
				//add Log Row to existing entry
				composition.get(key).addLogRow(lr);
			}else{
				//add new Row and new entry
				
				composition.put(key, new CompositionLogRow(lr));
			}			
		}
		CompositionCompositionLogRow newcclr = new CompositionCompositionLogRow(composition);
		newcclr.setGroubedBy(iGroupByFactory);
		
		return newcclr;
	}
	
	
	
	public void printCCLogRow(CompositionCompositionLogRow ccLR){
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

	@Override
	public ArrayList<String> getAllGroupBys() {
		ArrayList<String> allGroupBys = new ArrayList<>();
		allGroupBys.add(new GroupByLogLineCode().toString());
		
		allGroupBys.add(new GroupBySrcIP().toString());
		allGroupBys.add(new GroupBySrcPort().toString());
		allGroupBys.add(new GroupByProtocol().toString());
		
		allGroupBys.add(new GroupByMonth().toString());
		allGroupBys.add(new GroupByDays().toString());
		allGroupBys.add(new GroupByHours().toString());
		allGroupBys.add(new GroupByMinutes().toString());
		
		allGroupBys.add(new GroupByLocationCountry().toString());
		allGroupBys.add(new GroupByLocationCity().toString());
		
		allGroupBys.add(new GroupByPriority().toString());
		allGroupBys.add(new GroupByDestIP().toString());
		allGroupBys.add(new GroupByFwIP().toString());
		
		allGroupBys.add(new GroupByLogLine().toString());
		allGroupBys.add(new GroupByDescriptionLogLine().toString());
		allGroupBys.add(new GroupByExplanation().toString());
		allGroupBys.add(new GroupByrecommendedAction().toString());
		
		return allGroupBys;
	}

	@Override
	public CompositionCompositionLogRow getHoleCompositionByGroubByList(ArrayList<LogRow> logRows, ArrayList<IGroupByFactory> groupByList) {
		if(groupByList!=null){
			CompositionCompositionLogRow cclr = groupByLogLine(logRows, groupByList.get(0));
			
			if(groupByList.size()>1){
				IGroupByFactory[] addtionalGroupBys = new IGroupByFactory[groupByList.size()-1];
				
				//skip the First, it is already included
				int counter =-1;
				for(IGroupByFactory gbf: groupByList){
					if(counter>=0){
						addtionalGroupBys[counter] = gbf;
					}
					counter++;
				}
							
				cclr.makeSubComposition(addtionalGroupBys);
			}
		return cclr;
		}else{
			return null;
		}
	}


}
