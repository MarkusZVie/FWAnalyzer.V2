package at.ac.univie.FirewallLogAnayzer.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
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
	private Label nullLebel1;
	private Label nullLebel2;
	private Label treeInformation;
	private VBox masterLayout;
	private ScrollPane scrollPane;
	private TextArea descriptionReport;
	

	public Node getReportViewNode(int reportID,double widthValu, double heightValue, ArrayList<LogRow> allLogRows, Report report) {
		this.reportID = reportID;
		ltvc = new LogTreeViewController();
		double customHight = heightValue-50;
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
		descriptionReport.setPrefWidth(widthValu- 15);
		descriptionReport.setPrefHeight(customHight);
		descriptionReport.setText(report.getReportText());
		
		descriptionReport.textProperty().addListener((obs,old,niu)->{
			 Text text = (Text) descriptionReport.lookup(".text");
			 double textHeigh = text.boundsInParentProperty().get().getMaxY()+10;
			 if(textHeigh>customHight){
				 descriptionReport.setPrefHeight(textHeigh);
			 }
			 
		});
		
		//addContent
		
		//set Treeview
		treeInformation = new Label(" \u2193 \u2193 \u2193 Detail Information below \u2193 \u2193 \u2193 ");
		nullLebel1 = new Label("");
		nullLebel2 = new Label("");
		
		masterLayout.getChildren().add(nullLebel1);
		masterLayout.getChildren().add(treeInformation);
		masterLayout.getChildren().add(nullLebel2);
		
		ArrayList<LogRow> caseLogRows = report.getIndicater().getAllLogRows();
		
		masterLayout.getChildren().add(ltvc.getTreeViewNode(widthValu-15,caseLogRows));
		
		
		
		
		
		
		return scrollPane;
	}

}
