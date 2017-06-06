package at.ac.univie.FirewallLogAnayzer.Output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import at.ac.univie.FirewallLogAnayzer.Data.CompositionCompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.CompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRows;
import at.ac.univie.FirewallLogAnayzer.Data.LogType;
import at.ac.univie.FirewallLogAnayzer.Data.LogTypeSingelton;
import at.ac.univie.FirewallLogAnayzer.Data.Report;
import at.ac.univie.FirewallLogAnayzer.Exceptions.LogIdNotFoundException;
import at.ac.univie.FirewallLogAnayzer.Input.IInputHandler;
import at.ac.univie.FirewallLogAnayzer.Input.InputHandler;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.CompositionAnalysing;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.ICompositionAnalysing;
import at.ac.univie.FirewallLogAnayzer.Processing.IProcessingAnalyseThreats;
import at.ac.univie.FirewallLogAnayzer.Processing.ProcessingAnalyseThreats;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class PreparingCompositionForGui implements IPreparingCompositionForGui{

	private ICompositionAnalysing compositionAnalysing;
	private ArrayList<LogRow> allLogRows5;
	private HashMap<TreeItem<String>, Object[]> contextInformation;
	private IProcessingAnalyseThreats threatAnalyse;
	private IBasicFunctions basicFunctions;
	private IInputHandler inputHandler;
	
	
	public PreparingCompositionForGui() {
		compositionAnalysing = new CompositionAnalysing();
		contextInformation = new HashMap<>();
		threatAnalyse = new ProcessingAnalyseThreats();
		basicFunctions = new BasicFunctions();
		inputHandler = new InputHandler();
	}



	@Override
	public ArrayList<String> getCasePossibleGroupBys(ArrayList<String> alreadyUsed, ArrayList<LogRow> logRows) {
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
	public TreeItem<String> getRootTreeItem(ArrayList<String> groupByList, ArrayList<LogRow> logRows) {
		TreeItem<String> tree = new TreeItem<String>();
		
		ArrayList<IGroupByFactory> groupByFactoryList = getGroupByClassList(groupByList);
		CompositionCompositionLogRow cclr = compositionAnalysing.getHoleCompositionByGroubByList(logRows, groupByFactoryList);
		
		tree=convertCCLRInTree(new TreeItem<String>("Root"),cclr);
		
		return tree;
	}
	
	private TreeItem<String> convertCCLRInTree(TreeItem<String> rootTreeItem,CompositionCompositionLogRow cclr) {
		
		//Make List of Objects in this note
		HashMap<String,CompositionLogRow> composition = cclr.getComposition();
		Map<String, CompositionLogRow> treeMap = new TreeMap<String, CompositionLogRow>(composition);
		
		//add This list do RootNode
		for(String key :treeMap.keySet()){
				TreeItem<String> subitem = new TreeItem<String>(key + " (" + composition.get(key).getContent().size() + ")");
				if(cclr.getCcLogRow()!=null){
					if(cclr.getCcLogRow().get(key)!=null){
						if(cclr.getCcLogRow().get(key).getComposition()!=null){
							subitem=convertCCLRInTree(subitem, cclr.getCcLogRow().get(key));
						}
					}
				}
				rootTreeItem.getChildren().add(subitem);
				Object[] contextIfos = {composition.get(key).getContent().get(0),cclr.getGroubedBy()};
				contextInformation.put(subitem, contextIfos);
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
	
	@SuppressWarnings("restriction")
	private PieChart getPieChart(IGroupByFactory gbf, ArrayList<LogRow> logRows){
		//http://docs.oracle.com/javafx/2/charts/pie-chart.htm
		double minPieSize = 0.005;
		HashMap<String, Integer> hashData = new HashMap<>();
		for(LogRow lr : logRows){
			String key = gbf.getKey(lr);
			if(hashData.containsKey(key)){
				hashData.put(key, hashData.get(key)+1);
			}else{
				hashData.put(key, 1);
			}
		}
		PieChart chart = new PieChart();
		boolean tooLittlePieces = false;
		for(String key:hashData.keySet()){
			double value = hashData.get(key);
			if(value/logRows.size()>minPieSize){
				PieChart.Data slice = new PieChart.Data(key + " (" + ((double)((int)(value/logRows.size()*1000)))/10 +  "%)", 
																										hashData.get(key));
				chart.getData().add(slice);
			}else{
				tooLittlePieces = true;
			}
		}
		if(tooLittlePieces){
			chart.setTitle("Distribution of Events \n (Pieces with less than " + minPieSize*100 + "% are not included)");
		}else{
			chart.setTitle("Distribution of Events");
		}
		chart.setLegendSide(Side.BOTTOM);	
		return chart;
	}
	
	@SuppressWarnings("restriction")
	private LineChart<Number, Number> getLineChatOfActivity(Date begin, Date end,String key,ArrayList<LogRow> logRows){
		//http://www.java2s.com/Tutorials/Java/JavaFX/0820__JavaFX_LineChart.htm
		final NumberAxis xAxis = new NumberAxis();
	    final NumberAxis yAxis = new NumberAxis();
	    xAxis.setLabel("from " + basicFunctions.getSimpleDateFormat().format(begin) + " to " + 
	    				basicFunctions.getSimpleDateFormat().format(end) + "(hour Segments)");
	    yAxis.setLabel("Ammout of LogEvents");
	    final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
	    lineChart.setTitle("Activity Chart");
	    XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
	    series.setName(key + " requests");
	    
	    int numberOfOneHourBlocks = ((int) ((end.getTime()-begin.getTime())/1000)/3600)+1;
	    int ammoutOfActionInThisBlock =0;
	    Date compareAbleDate1 = begin;
	    Date compareAbleDate2 = new Date();
	    compareAbleDate2.setTime(begin.getTime()+(3600000));
	    
	    for(int i=0; i<numberOfOneHourBlocks; i++){
	    	for(LogRow lr : logRows){
	    		if( lr.getDateTime().getTime()>=compareAbleDate1.getTime()&& //it must be bigger than beginBlock
	    			lr.getDateTime().getTime()<compareAbleDate2.getTime()){ //it must be smaller than endBlock
	    			ammoutOfActionInThisBlock++;
	    		}
	    	}
	    	series.getData().add(new XYChart.Data<Number, Number>(i, ammoutOfActionInThisBlock));
	    	//restore vars
	    	ammoutOfActionInThisBlock=0;
	    	compareAbleDate1.setTime(compareAbleDate2.getTime());
	    	compareAbleDate2.setTime(compareAbleDate2.getTime()+3600000);
	    }
	    lineChart.getData().add(series);
	    return lineChart;
	}
	
	@SuppressWarnings("restriction")
	@Override
	public Object[] getDiscription(TreeItem<String> item, TextArea description, TreeView<String> treeView, ArrayList<LogRow> logRows) {
		ArrayList<TreeItem<String>> itemTree = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		LineChart<Number, Number> lineChart = null;
		PieChart pieChart = null;
		//Create Basic Discription
		if(contextInformation.containsKey(item)){
			LogRow representiveLogRow = (LogRow) contextInformation.get(item)[0];
			sb.append("Description:");
			sb.append(System.lineSeparator());
			sb.append(representiveLogRow.getDescriptionLogLine());
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
			sb.append("Represental Log Line:");
			sb.append(System.lineSeparator());
			sb.append(representiveLogRow.getLogLine());
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
			sb.append("Explanation:");
			sb.append(System.lineSeparator());
			sb.append(representiveLogRow.getExplanation());
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
			sb.append("RecommendedAction:");
			sb.append(System.lineSeparator());
			sb.append(representiveLogRow.getRecommendedAction());
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
		}
		//add items until Root
		TreeItem<String> activeItem = item;
		if(activeItem!=null){
			itemTree.add(activeItem);
			while(activeItem.getParent()!=null){
				activeItem= activeItem.getParent();
				itemTree.add(activeItem);
			}
		}
		for(int i =(itemTree.size()-2); i>=0;i--){//  (-2)  dont care the root root node
			if(contextInformation.containsKey(itemTree.get(i))){
				Object[] caseContextInformation = contextInformation.get(itemTree.get(i));
				LogRow lr = (LogRow) caseContextInformation[0];
				IGroupByFactory gbf = (IGroupByFactory) caseContextInformation[1];
				if(!gbf.getCaseDescription(lr).equals("")){
					sb.append(gbf.toString() + " Details:");
					sb.append(gbf.getCaseDescription(lr));
					sb.append(System.lineSeparator());
					sb.append(System.lineSeparator());		
					if( gbf.toString().equals(new GroupBySrcIP().toString())||
						gbf.toString().equals(new GroupByDestIP().toString())){
						sb.append(getFrequentAnalyse(item,logRows));
						AsynchronIPDescriptionLoader aIpLoader = new AsynchronIPDescriptionLoader(lr.getSrcIP(), item, description, 
																										treeView, sb.toString());
						aIpLoader.start();
					}
				}
			}
		}	
		sb.append("Additional Information will be available in view seconds");
		if(item!=null){
			if(item.getParent()!=null){
				IGroupByFactory gbf = getGroubBy(item);
				ArrayList<LogRow> logRowsPart = getLogRows(item,logRows);
				lineChart = getLineChatOfActivity(basicFunctions.getLogBeginDate(logRows), basicFunctions.getLogEndDate(logRows), 
																							gbf.getKey(getLogRow(item)), logRowsPart);
				if(item.getChildren()!=null){
					if(item.getChildren().size()>=1){
						if(item.getChildren().get(0)!=null){
							pieChart = getPieChart(getGroubBy(item.getChildren().get(0)), logRowsPart); 
						}
					}
				}				
			}
		}
		Object[] returnContent = {sb.toString(),lineChart, pieChart};
		return returnContent;
	}

	@SuppressWarnings("restriction")
	private ArrayList<LogRow> getLogRows(TreeItem<String> item, ArrayList<LogRow> logRows) {
		CompositionCompositionLogRow cclr = compositionAnalysing.getHoleCompositionByGroubByList(logRows, getGroupByList(item));
		//IGroupByFactory gbf = getGroubBy(item);
		ArrayList<IGroupByFactory> groupByList = getGroupByList(item);
		LogRow reprensentiveLogRow = getLogRow(item);
		String finallyKey = groupByList.get(groupByList.size()-1).getKey(reprensentiveLogRow);
		
		CompositionCompositionLogRow redusedCclr = cclr;
		for(IGroupByFactory gbf : groupByList){

			String key = gbf.getKey(reprensentiveLogRow);
			HashMap<String,CompositionCompositionLogRow> cclrList = redusedCclr.getCcLogRow();
			if(cclrList!=null){
				CompositionCompositionLogRow tempcclr = cclrList.get(key);
				if(tempcclr!=null){
					redusedCclr = cclrList.get(gbf.getKey(reprensentiveLogRow));
					
				}
			}else{
				return cclr.getComposition().get(key).getContent();
			}
		}
		return redusedCclr.getComposition().get(finallyKey).getContent();
	}


	@SuppressWarnings("restriction")
	private String getFrequentAnalyse(TreeItem<String> item, ArrayList<LogRow> logRows) {
		ArrayList<LogRow> logRowsPart = getLogRows(item,logRows); 
		double[] stats = compositionAnalysing.getStatisticsAboutTimeFriquent(logRowsPart, 
						 basicFunctions.getLogBeginDate(logRows), basicFunctions.getLogEndDate(logRows), true);
		double ammountPerHour = compositionAnalysing.getAmmountPerHour(logRowsPart, 
						 basicFunctions.getLogBeginDate(logRows), basicFunctions.getLogEndDate(logRows));
		double threadScore = compositionAnalysing.getThreatScore(stats, ammountPerHour);
	
		StringBuilder sb = new StringBuilder();
		sb.append("Persistent Time Analyse");
		sb.append(System.lineSeparator());
		sb.append("Standard Deviation:  \t" + stats[0]);
		sb.append(System.lineSeparator());
		sb.append("Median:              \t\t" + stats[1]);
		sb.append(System.lineSeparator());
		sb.append("Trimed Mean (45%) :  \t" + stats[2]);
		sb.append(System.lineSeparator());
		sb.append("Arithmetic Mean :    \t" + stats[3]);
		sb.append(System.lineSeparator());
		sb.append("Logs per Hour :      \t\t" + ammountPerHour);
		sb.append(System.lineSeparator());
		sb.append("Thread Score :       \t\t" + threadScore);
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		return sb.toString();
	}



	


	private ArrayList<LogRow> getCompositionLogRow(CompositionCompositionLogRow cclr, ArrayList<IGroupByFactory> groupByList, LogRow reprensentiveLogRow) {
		CompositionCompositionLogRow redusedCclr = cclr;
		for(IGroupByFactory gbf : groupByList){
			HashMap<String,CompositionCompositionLogRow> cclrList = redusedCclr.getCcLogRow();
			String key = gbf.getKey(reprensentiveLogRow);
			CompositionCompositionLogRow tempcclr = cclrList.get(key);
			if(tempcclr!=null){
				redusedCclr = cclrList.get(gbf.getKey(reprensentiveLogRow));
				
			}
		}
		IGroupByFactory lastGroupBy = groupByList.get(groupByList.size()-1);
		ArrayList<LogRow> allList = redusedCclr.getAllLogRows();
		ArrayList<LogRow> selectedList = new ArrayList<>();
		for(LogRow lr: allList){
			if(lastGroupBy.getKey(lr).equals(lastGroupBy.getKey(reprensentiveLogRow))){
				selectedList.add(lr);
			}
		}
		return selectedList;
	}

	@SuppressWarnings("restriction")
	private LogRow getLogRow(TreeItem<String> item) {
		if(contextInformation.containsKey(item)){
			Object[] caseContextInformation = contextInformation.get(item);
			LogRow lr = (LogRow) caseContextInformation[0];
			return lr;
		}
		else{
			return null;
		}
	}

	@SuppressWarnings("restriction")
	private ArrayList<IGroupByFactory> getGroupByList(TreeItem<String> item) {
		ArrayList<IGroupByFactory> groubByList = new ArrayList<>();
		ArrayList<TreeItem<String>> itemTree = new ArrayList<>();
		
		TreeItem<String> activeItem = item;
		if(activeItem!=null){
			itemTree.add(activeItem);
			while(activeItem.getParent()!=null){
				activeItem= activeItem.getParent();
				itemTree.add(activeItem);
			}
		}
		for(int i =(itemTree.size()-2); i>=0;i--){//  (-2)  dont care the root root node
			IGroupByFactory igb = getGroubBy(itemTree.get(i));
			if(igb!=null){
				groubByList.add(igb);
			}
		}
		
		return groubByList;
	}

	@SuppressWarnings("restriction")
	public IGroupByFactory getGroubBy(TreeItem<String> item){
		if(contextInformation.containsKey(item)){
			Object[] caseContextInformation = contextInformation.get(item);
			IGroupByFactory gbf = (IGroupByFactory) caseContextInformation[1];
			return gbf;
		}
		else{
			return null;
		}
		
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



	@Override
	public Report getReport(int reportID) {
		switch (reportID) {
		case 1:
			return threatAnalyse.analyseForDos();
		case 2:
			return threatAnalyse.analyseIPspoofedAttack();
		case 3:
			return threatAnalyse.analyseConnectionHighChecking();
		case 4:
			return threatAnalyse.analyseRoutingManipulation();
		case 5:
			return threatAnalyse.analyseSynAttack();
		case 6:
			return threatAnalyse.analyseICMPBasedAttaks();
		case 7:
			return threatAnalyse.analyseTCPBasedAttacks();
		case 8:
			return threatAnalyse.analyseUDPBasedAttacks();
		case 9:
			return threatAnalyse.analyseBruteForce();
		case 10:
			return threatAnalyse.analyseWeakIndicaterOfAnAttack();
		case 11:
			return threatAnalyse.analyseOtherAttacks();
		case 12:
			return threatAnalyse.analyseForPortScanningOrFootPrinting();
		default:
			System.out.println("There is an Uniplementet Report (unknown ReportID)");
			break;
		}
		return null;
	}



	@Override
	public void saveAllLogs(File file) {
		//https://www.mkyong.com/java/how-to-write-an-object-to-file-in-java/
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			fout = new FileOutputStream(file.getAbsolutePath());
			oos = new ObjectOutputStream(fout);
			oos.writeObject(LogRows.getInstance().getLogRows());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}



	@Override
	public void readParsedFiles(List<File> list) {
		inputHandler.readParsedFiles(list);
		
	}



	@Override
	public void readAdditionalLogFiles(List<File> fileList) {
		try {
			inputHandler.loadeFirewallLog(fileList, LogTypeSingelton.getInstance().getSupportedLogTypeList().get(0), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LogIdNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}






	

}
