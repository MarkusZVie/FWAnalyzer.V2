package at.ac.univie.FirewallLogAnayzer.Output;

import java.util.ArrayList;

import at.ac.univie.FirewallLogAnayzer.Data.Report;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public interface IPreparingCompositionForGui {
	public ArrayList<String> getCasePossibleGroupBys(ArrayList<String> alreadyUsed);

	public TreeItem<String> getRootTreeItem(ArrayList<String> generateUsedBrouBys);
	

	public Object[] getDiscription(TreeItem<String> item, TextArea description, TreeView<String> treeView);

	public Report getReport(int reportID);
	
	
}
