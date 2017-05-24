package at.ac.univie.FirewallLogAnayzer.Processing;

import java.util.ArrayList;
import java.util.HashMap;

import at.ac.univie.FirewallLogAnayzer.Data.CompositionAnalysingSettings;
import at.ac.univie.FirewallLogAnayzer.Data.DoSReport;
import at.ac.univie.FirewallLogAnayzer.Data.HashPairDoubleValue;

public interface IProcessingAnalyseThreats {
	public HashMap<String, Double> analyseForPortScanningOrFootPrinting();
	public void printStaticCompostionTree();
	public ArrayList<HashPairDoubleValue> genereateSortAbleFromDoubleHashMap(HashMap<String, Double> srcMap);
	public DoSReport analyseForDos();
	public void printCompostionTree(CompositionAnalysingSettings settings);
}
