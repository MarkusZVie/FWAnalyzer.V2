package at.ac.univie.FirewallLogAnayzer.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import at.ac.univie.FirewallLogAnayzer.Data.Report;
import at.ac.univie.FirewallLogAnayzer.Output.IPreparingCompositionForGui;
import at.ac.univie.FirewallLogAnayzer.Output.PreparingCompositionForGui;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ReportViewController implements Initializable{
	
	private StackPane frameReference;
	private IPreparingCompositionForGui prepairedComposion;
	private int reportID;
	private Report report;
	
	private VBox masterLayout;
	private VBox gernerlTopLyout;
	private StackPane treeView;
	private ScrollPane scrollPane;
	
	private Label typeOfAttack;
	private Label description;
	private Label asaCodes;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		//Loade Reference
		frameReference = PageRefeferenceControllerSingelton.getInstance().getTreeViewPage();
		prepairedComposion = new PreparingCompositionForGui();
		reportID = PageRefeferenceControllerSingelton.getInstance().getReportID();
		report = prepairedComposion.getReport(reportID);
		
		//instance Lyouts
		scrollPane = new ScrollPane();
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		
		masterLayout = new VBox();
		frameReference.getChildren().add(scrollPane);
		scrollPane.setContent(masterLayout);
		
		gernerlTopLyout = new VBox();
		masterLayout.getChildren().add(gernerlTopLyout);
		treeView = new StackPane();
		masterLayout.getChildren().add(treeView);
		treeView.setPrefHeight(100);
		
		//instance ControllerObjects
		typeOfAttack = new Label();
		gernerlTopLyout.getChildren().add(typeOfAttack);
		description = new Label();
		gernerlTopLyout.getChildren().add(description);
		asaCodes = new Label();
		gernerlTopLyout.getChildren().add(asaCodes);
		
		
		//add Content to COntrollerObjects
		typeOfAttack.setText(report.getTypeOfAttac());
		description.setText(report.getDescription());
		String rowAsaCodes="";
		for(String s: report.getInvolvedLogLineCodes()){
			rowAsaCodes = rowAsaCodes + " " + s;
		}
		asaCodes.setText(rowAsaCodes);
		
		treeView.getChildren().add(description);
		
		/*
		try {
			treeView.getChildren().clear();
			treeView.getChildren().add(FXMLLoader.load(getClass().getResource("/LogTreeView.fxml")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

}
