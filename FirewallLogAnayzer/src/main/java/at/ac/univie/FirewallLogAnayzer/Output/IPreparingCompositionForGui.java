package at.ac.univie.FirewallLogAnayzer.Output;

import java.util.ArrayList;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public interface IPreparingCompositionForGui {
	public ArrayList<String> getCasePossibleGroupBys(ArrayList<String> alreadyUsed);

	public TreeItem<String> getRootTreeItem(ArrayList<String> generateUsedBrouBys);
	
	public String getDiscription(TreeItem<String> item);
}
