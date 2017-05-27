package at.ac.univie.FirewallLogAnayzer.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import at.ac.univie.FirewallLogAnayzer.Data.Report;
import at.ac.univie.FirewallLogAnayzer.Output.IPreparingCompositionForGui;
import at.ac.univie.FirewallLogAnayzer.Output.PreparingCompositionForGui;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ReportViewController{
	private LogTreeViewController ltvc;
	private IPreparingCompositionForGui prepairedComposion;
	private int reportID;
	private Report report;
	
	private VBox masterLayout;
	private StackPane treeView;
	private ScrollPane scrollPane;
	private TextArea descriptionReport;
	

	public Node getReportViewNode(int reportID) {
		this.reportID = reportID;
		ltvc = new LogTreeViewController();
		
		//Loade Reference
		
		
		prepairedComposion = new PreparingCompositionForGui();
		report = prepairedComposion.getReport(reportID);
		
		
		
		//instance Lyouts
		
		
		scrollPane = new ScrollPane();
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
				
		
		masterLayout = new VBox();
		scrollPane.setContent(masterLayout);
		
		descriptionReport = new TextArea();	
		masterLayout.getChildren().add(descriptionReport);
		
		descriptionReport.setWrapText(true);
		descriptionReport.setText(report.getReportText());
		
		descriptionReport.textProperty().addListener((obs,old,niu)->{
			 Text text = (Text) descriptionReport.lookup(".text");
			 descriptionReport.setPrefHeight(text.boundsInParentProperty().get().getMaxY()+10);
		});
		
		//addContent
		
		//set Treeview
		
		treeView = new StackPane();
		masterLayout.getChildren().add(ltvc.getTreeViewNode());
		
		
		
		
		
		
		return scrollPane;
	}

}