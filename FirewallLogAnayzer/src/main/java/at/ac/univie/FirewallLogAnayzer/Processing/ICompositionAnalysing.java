package at.ac.univie.FirewallLogAnayzer.Processing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import at.ac.univie.FirewallLogAnayzer.Data.CompositionAnalysingSettings;
import at.ac.univie.FirewallLogAnayzer.Data.CompositionCompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.IGroupByFactory;

public interface ICompositionAnalysing {

	public ArrayList<LogRow> eliminateUnnecessaryRowsBySetting(ArrayList<LogRow> baseRows,CompositionAnalysingSettings setting);
	
	public HashMap<String, Double> getSetOfPersistencingTransferingIps(ArrayList<LogRow> logRows, Date logBegin, Date logEnd);
	
	public double getThreadScore(double[] stats, double ammountPerHour);
	
	public double[] getStatisticsAboutTimeFriquent(ArrayList<LogRow> logRows,Date logBegin, Date logEnd, boolean ignoreDayNextDayJumps);

	public CompositionCompositionLogRow groupByLogLine(ArrayList<LogRow> logRows,IGroupByFactory iGroupByFactory);
	
	public void printCCLogRow(CompositionCompositionLogRow ccLR);
		
}
