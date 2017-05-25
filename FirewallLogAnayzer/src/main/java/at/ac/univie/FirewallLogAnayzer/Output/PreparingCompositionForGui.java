package at.ac.univie.FirewallLogAnayzer.Output;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import at.ac.univie.FirewallLogAnayzer.Data.CompositionCompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.CompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRows;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.CompositionAnalysing;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.ICompositionAnalysing;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class PreparingCompositionForGui implements IPreparingCompositionForGui{

	private ICompositionAnalysing compositionAnalysing;
	private ArrayList<LogRow> allLogRows;
	
	
	
	public PreparingCompositionForGui() {
		compositionAnalysing = new CompositionAnalysing();
		allLogRows = LogRows.getInstance().getLogRows();
	}



	@Override
	public ArrayList<String> getCasePossibleGroupBys(ArrayList<String> alreadyUsed) {
		ArrayList<String> casePossibleGroupBys = new ArrayList<>();
		for(String groupBy: compositionAnalysing.getAllGroupBys()){
			boolean isUsed = false;
			for(String usedGroupBy: alreadyUsed){
				if(groupBy.equals(usedGroupBy)){
					isUsed=true;
				}
			}
			if(!isUsed){
				casePossibleGroupBys.add(groupBy);
			}
		}
		return casePossibleGroupBys;
	}



	@Override
	public TreeItem<String> getRootTreeItem(ArrayList<String> groupByList) {
		TreeItem<String> tree = new TreeItem<String>();
		
		ArrayList<IGroupByFactory> groupByFactoryList = getGroupByClassList(groupByList);
		CompositionCompositionLogRow cclr = compositionAnalysing.getHoleCompositionByGroubByList(allLogRows, groupByFactoryList);
		
		tree=convertCCLRInTree(new TreeItem<String>("Root"),cclr);
		
		return tree;
	}
	
	private TreeItem<String> convertCCLRInTree(TreeItem<String> rootTreeItem,CompositionCompositionLogRow cclr) {
		
		//Make List of Objects in this note
		HashMap<String,CompositionLogRow> composition = cclr.getComposition();
		Map<String, CompositionLogRow> treeMap = new TreeMap<String, CompositionLogRow>(composition);
		
		//add This list do RootNode
		for(String key :treeMap.keySet()){
				TreeItem<String> subitem = new TreeItem<String>(key);
				if(cclr.getCcLogRow()!=null){
					if(cclr.getCcLogRow().get(key)!=null){
						if(cclr.getCcLogRow().get(key).getComposition()!=null){
							subitem=convertCCLRInTree(subitem, cclr.getCcLogRow().get(key));
						}
					}
				}
				rootTreeItem.getChildren().add(subitem);
				
		}
		
		return rootTreeItem;
	}



	private ArrayList<IGroupByFactory> getGroupByClassList(ArrayList<String> groupByList) {
		ArrayList<IGroupByFactory> gbf = new ArrayList<>();
		for(String gbName : groupByList){
			gbf.add(getGrouByClassByName(gbName));
		}
		return gbf;
	}



	public IGroupByFactory getGrouByClassByName (String name){
		if(name.equals(new GroupByDays().toString())){
			return new GroupByDays();
		}
		if(name.equals(new GroupByDays().toString())){
			return new GroupByDays();
		}
		if(name.equals(new GroupByDescriptionLogLine().toString())){
			return new GroupByDescriptionLogLine();
		}
		if(name.equals(new GroupByDestIP().toString())){
			return new GroupByDestIP();
		}
		if(name.equals(new GroupByExplanation().toString())){
			return new GroupByExplanation();
		}
		if(name.equals(new GroupByFwIP().toString())){
			return new GroupByFwIP();
		}
		if(name.equals(new GroupByHours().toString())){
			return new GroupByHours();
		}
		if(name.equals(new GroupByLocationCity().toString())){
			return new GroupByLocationCity();
		}
		if(name.equals(new GroupByLocationCountry().toString())){
			return new GroupByLocationCountry();
		}
		if(name.equals(new GroupByLogLine().toString())){
			return new GroupByLogLine();
		}
		if(name.equals(new GroupByLogLineCode().toString())){
			return new GroupByLogLineCode();
		}
		if(name.equals(new GroupByMinutes().toString())){
			return new GroupByMinutes();
		}
		if(name.equals(new GroupByPriority().toString())){
			return new GroupByPriority();
		}
		if(name.equals(new GroupByMonth().toString())){
			return new GroupByMonth();
		}
		if(name.equals(new GroupByProtocol().toString())){
			return new GroupByProtocol();
		}
		if(name.equals(new GroupByrecommendedAction().toString())){
			return new GroupByrecommendedAction();
		}
		if(name.equals(new GroupBySrcIP().toString())){
			return new GroupBySrcIP();
		}
		if(name.equals(new GroupBySrcPort().toString())){
			return new GroupBySrcPort();
		}
		
		
		return null;
	}

}
