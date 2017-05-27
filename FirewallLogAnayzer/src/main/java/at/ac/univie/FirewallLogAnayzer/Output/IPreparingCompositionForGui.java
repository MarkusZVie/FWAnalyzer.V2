package at.ac.univie.FirewallLogAnayzer.Output;

import java.util.ArrayList;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.Report;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public interface IPreparingCompositionForGui {
	public ArrayList<String> getCasePossibleGroupBys(ArrayList<String> alreadyUsed, ArrayList<LogRow> caseLogRows);

	public TreeItem<String> getRootTreeItem(ArrayList<String> generateUsedBrouBys, ArrayList<LogRow> caseLogRows);
	

	public Object[] getDiscription(TreeItem<String> item, TextArea description, TreeView<String> treeView, ArrayList<LogRow> caseLogRows);

	public Report getReport(int reportID);
	
	
}
