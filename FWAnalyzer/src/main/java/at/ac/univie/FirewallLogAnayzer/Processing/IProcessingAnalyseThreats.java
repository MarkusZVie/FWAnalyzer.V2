package at.ac.univie.FirewallLogAnayzer.Processing;

import java.util.ArrayList;
import java.util.HashMap;

import at.ac.univie.FirewallLogAnayzer.Data.CompositionAnalysingSettings;
import at.ac.univie.FirewallLogAnayzer.Data.DoSReport;
import at.ac.univie.FirewallLogAnayzer.Data.HashPairDoubleValue;
import at.ac.univie.FirewallLogAnayzer.Data.Report;

public interface IProcessingAnalyseThreats {
	public void printStaticCompostionTree();
	public ArrayList<HashPairDoubleValue> genereateSortAbleFromDoubleHashMap(HashMap<String, Double> srcMap);
	public DoSReport analyseForDos();
	public void printCompostionTree(CompositionAnalysingSettings settings);
	public Report analyseForPortScanningOrFootPrinting();
	public Report analyseIPspoofedAttack(); 
	public Report analyseConnectionHighChecking();
	public Report analyseRoutingManipulation();
	public Report analyseSynAttack();
	public Report analyseWeakIndicaterOfAnAttack();
	public Report analyseICMPBasedAttaks();
	public Report analyseTCPBasedAttacks();
	public Report analyseUDPBasedAttacks();
	public Report analyseOtherAttacks();
	public Report analyseBruteForce();
}
