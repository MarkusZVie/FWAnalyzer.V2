package at.ac.univie.FirewallLogAnayzer.GUI;

import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

public class PageRefeferenceControllerSingelton {
	
	
	private static PageRefeferenceControllerSingelton instance =null;
	
	
	private StackPane referenceMasterPane;
	private StackPane reportTreeViewPane;
	private int reportID;
	
	
	
	private PageRefeferenceControllerSingelton(){
	}
	public static PageRefeferenceControllerSingelton getInstance(){
		if(instance == null){
			instance = new PageRefeferenceControllerSingelton();
		}
		return instance;
	}
	public void setReferenceMasterPane(StackPane spCenter) {
		referenceMasterPane = spCenter;
		
	}
	public StackPane getReferenceMasterPane() {
		return referenceMasterPane;
	}
	
	public int getReportID() {
		return reportID;
	}
	public void setReportID(int reportID) {
		this.reportID = reportID;
	}
	public StackPane getReportTreeView() {
		return reportTreeViewPane;
	}
	public void setReportTreeViewPane(StackPane reportTreeViewPane) {
		this.reportTreeViewPane = reportTreeViewPane;
	}
	public void test() {
		TextArea t = new TextArea();
		t.setText("asffd");
		referenceMasterPane.getChildren().add(t);
		
	}
	
}
