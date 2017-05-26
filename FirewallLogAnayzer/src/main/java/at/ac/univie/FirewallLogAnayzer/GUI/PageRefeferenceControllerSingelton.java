package at.ac.univie.FirewallLogAnayzer.GUI;

import javafx.scene.layout.StackPane;

public class PageRefeferenceControllerSingelton {
	
	
	private static PageRefeferenceControllerSingelton instance =null;
	private StackPane treeViewPage;
	private int reportID;
	
	
	
	private PageRefeferenceControllerSingelton(){
	}
	public static PageRefeferenceControllerSingelton getInstance(){
		if(instance == null){
			instance = new PageRefeferenceControllerSingelton();
		}
		return instance;
	}
	public void setReference(StackPane spCenter) {
		treeViewPage = spCenter;
		
	}
	public StackPane getTreeViewPage() {
		return treeViewPage;
	}
	
	public int getReportID() {
		return reportID;
	}
	public void setReportID(int reportID) {
		this.reportID = reportID;
	}
}
