package at.ac.univie.FirewallLogAnayzer.GUI;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import at.ac.univie.FirewallLogAnayzer.Data.LogTypeSingelton;
import at.ac.univie.FirewallLogAnayzer.Exceptions.LogIdNotFoundException;
import at.ac.univie.FirewallLogAnayzer.Input.IInputHandler;
import at.ac.univie.FirewallLogAnayzer.Input.InputHandler;
import at.ac.univie.FirewallLogAnayzer.Output.IPreparingCompositionForGui;
import at.ac.univie.FirewallLogAnayzer.Output.PreparingCompositionForGui;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLogLineCode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class LogTreeViewController implements Initializable{
	
	private StackPane frameReference;
	private IPreparingCompositionForGui prepairedComposion;
	private HBox topLayout;
	private BorderPane localLayout;
	
	//content Elements
	@FXML TreeView<String> treeView;
	private TreeItem<String> rootTreeItem;
	private ArrayList<ComboBox<String>> choices;
	private boolean isExsistingEmptyCobobox;
	private TextArea description;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//for Testing Thing 

		System.out.println("indizilase");
		
		IInputHandler inputHandler = new InputHandler();
        // /Users/josefweber/Desktop/SyslogCatchAll-2017-03-14.txt
        // C:\Users\Lezard\Desktop\SyslogCatchAll-2017-03-14.txt
        try {
        	inputHandler.loadeFirewallLog("C:\\Users\\Lezard\\Desktop\\activeFWLogs", LogTypeSingelton.getInstance().getSupportedLogTypeList().get(0));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (LogIdNotFoundException e) {
			e.printStackTrace();
		}
		
		//End Testing Things
		
		
		//Loade Reference
		frameReference = PageRefeferenceControllerSingelton.getInstance().getTreeViewPage();
		prepairedComposion = new PreparingCompositionForGui();
		
		//inizilase Content Attributes
		choices = new ArrayList<>();
		isExsistingEmptyCobobox = true;
		
		//ChrateLayout
		localLayout = new BorderPane();
		frameReference.getChildren().add(localLayout);
		topLayout = new HBox();
		localLayout.setTop(topLayout);
		
		//create Top Contrilling Elements
		choices.add(createPossibleChoiceList());
		addComboboxesToTop();
				
		//create DetailVew
		description = new TextArea();
		description.setWrapText(true);
		localLayout.setRight(description);
	
		
		//create TreeView
        treeView = new TreeView<String> ();  
        choices.get(0).getSelectionModel().select(new GroupByLogLineCode().toString());
        
        
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(
                ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> old_val, TreeItem<String> new_val) {
            	refreshDescription(new_val);
            }
        });
        
        
        treeView.setMinWidth(350);
		localLayout.setCenter(treeView);
		System.out.println("indizilase");
        
	}
	
	private void refreshDescription(TreeItem<String> item){
		description.setText(prepairedComposion.getDiscription(item));
	}
	
	private void addComboboxesToTop(){
		topLayout.getChildren().clear();
		for(ComboBox<String> cb: choices){
			topLayout.getChildren().add(cb);
		}
	}
	
	private ComboBox<String> createPossibleChoiceList() {
		ComboBox<String> cb = new ComboBox<>();
		for(String possibleGroupBy : prepairedComposion.getCasePossibleGroupBys(generateUsedBrouBys())){
			cb.getItems().add(possibleGroupBy);
		}
		cb.getItems().add("--none--");								//For Deleting selection
		cb.valueProperty().addListener(new ChangeListener<String>() {
	       	@Override
			public void changed(ObservableValue observable, String oldValue, String newValue) {
				comboboxSelectionHasChanged(oldValue, newValue);
			}
		});
		return cb;
	}
	
	private ArrayList<String> generateUsedBrouBys() {
		ArrayList<String> usedGroupBys= new ArrayList<>();
		for(ComboBox<String> cb: choices){
			if(cb.getValue()!=null){
				usedGroupBys.add(cb.getValue());
			}
		}
		return usedGroupBys;
	}
	public void comboboxSelectionHasChanged(String oldValue, String newValue) {
		if(newValue.equals("--none--")){
			choices.remove(findComboboxByValue(newValue));
		}
		if(oldValue == null){
			choices.add(createPossibleChoiceList());
		}
		addComboboxesToTop();
		updateTree();
		treeView.getRoot().setExpanded(true);
		
	}
	
	private void updateTree() {
		TreeItem<String> newRootTreeItem = prepairedComposion.getRootTreeItem(generateUsedBrouBys());
		treeView.setRoot(newRootTreeItem);
	}
	public ComboBox<String> findComboboxByValue(String value){
		for(ComboBox<String> cb: choices){
			if(cb.getValue().equals(value)){
				return cb;
			}
		}
		return null;
	}
	
}
