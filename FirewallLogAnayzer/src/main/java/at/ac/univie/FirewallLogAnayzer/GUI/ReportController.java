package at.ac.univie.FirewallLogAnayzer.GUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class ReportController implements Initializable{
	@FXML private TextField name;
	
	@FXML Button button;
	
	@FXML TreeView<String> treeView;
	
	private TreeItem<String> rootTreeItem;
	
	@FXML public void prositure(){
		name.setText("trlololol");
		System.out.println("ssss");
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		TreeItem<String> rootItem = new TreeItem<String> ("Inbox");
        rootItem.setExpanded(true);
        for (int i = 1; i < 6; i++) {
            TreeItem<String> item = new TreeItem<String> ("Message" + i);            
            rootItem.getChildren().add(item);
        }        
        TreeView<String> treeView = new TreeView<String> (rootItem);  
		
		System.out.println("indizilase");
		
		
	}
}
