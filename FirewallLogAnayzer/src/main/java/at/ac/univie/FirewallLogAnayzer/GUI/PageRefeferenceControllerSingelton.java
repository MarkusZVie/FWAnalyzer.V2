package at.ac.univie.FirewallLogAnayzer.GUI;

import javafx.scene.layout.StackPane;

public class PageRefeferenceControllerSingelton {
	
	
	private static PageRefeferenceControllerSingelton instance =null;
	private StackPane treeViewPage;
	
	
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
	
	
}
