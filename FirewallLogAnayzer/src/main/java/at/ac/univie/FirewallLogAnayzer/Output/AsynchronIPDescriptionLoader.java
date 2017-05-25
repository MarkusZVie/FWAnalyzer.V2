package at.ac.univie.FirewallLogAnayzer.Output;

import at.ac.univie.FirewallLogAnayzer.Input.IIPBackgroundParser;
import at.ac.univie.FirewallLogAnayzer.Input.IPBackgroundParser;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class AsynchronIPDescriptionLoader extends Thread{
	
	private String ip;
	private TreeItem<String> item;
	private TextArea description;
	private TreeView<String> treeView;
	private String existingDescription;
	private IBasicFunctions basicFunctions;
	

	public AsynchronIPDescriptionLoader(String srcIP, TreeItem<String> item, TextArea description, TreeView<String> treeView,String existingDescription) {
    	super("AsynchronIPDescriptionLoader");
    	this.ip = srcIP;
		this.item = item;
		this.description = description;
		this.treeView = treeView;
		this.existingDescription=existingDescription;
		basicFunctions= new BasicFunctions();
	}
	
	public void run() {
		if(basicFunctions.searchTheNIpInRow(ip, 1)!=null){
	        IIPBackgroundParser bparser = new IPBackgroundParser();
	        String contexInfos = bparser.getBackgroundInfos(ip);
	        if(treeView.getSelectionModel().getSelectedItem()==item){
	        	description.setText("Additional Infrmation about IP:\n"+existingDescription + contexInfos);
	        }
		}else {
			description.setText("We could NOT find more Information about the IP: " + ip +"\n"+existingDescription);
		}
    }
}
