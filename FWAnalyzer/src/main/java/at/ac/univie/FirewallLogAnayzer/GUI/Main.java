package at.ac.univie.FirewallLogAnayzer.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import at.ac.univie.FirewallLogAnayzer.Output.IPreparingCompositionForGui;
import at.ac.univie.FirewallLogAnayzer.Output.PreparingCompositionForGui;

public class Main extends Application {

    private static Stage primaryStage;
    private static IPreparingCompositionForGui pcg;
    private static BorderPane rootLayout;
    private FileChooseController fcc;
    private AnalyzeMenuController analyzerMC;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
    	analyzerMC = new AnalyzeMenuController();
    	pcg = new PreparingCompositionForGui();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Firewall Log Analyzer");
        fcc = new FileChooseController();

        // set resizeable false!
        //primaryStage.setResizable(false);

        
        setRootLayout();
        
        //initFileTab();
        //changeScene("/dosGraphical.fxml");
        //changeSceneBorderPane("/analyzeMenu.fxml");
        rootLayout.setCenter(fcc.getFileChooseNode(rootLayout.getWidth(), rootLayout.getHeight(),primaryStage,rootLayout));
        
    }
    

    private static void loadeMenue() {
    	final FileChooser fileChooser = new FileChooser();
    	MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        rootLayout.setTop(menuBar);
    	
    	Menu fileMenu = new Menu("File");
        MenuItem saveMenuItem = new MenuItem("Save loaded Logfile(s)");
        saveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	FileChooser fileChooser = new FileChooser();
            	  
                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CFWA files (*.cfwa)", "*.cfwa");
                fileChooser.getExtensionFilters().add(extFilter);
                
                //Show save file dialog
                File file = fileChooser.showSaveDialog(primaryStage);
                
                if(file != null){
                	saveLogs(file);
                }
            	
            }
        });
        
        MenuItem loadeMenueItem = new MenuItem("Loade additional Logfile(s)");
        loadeMenueItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	List<File> list = fileChooser.showOpenMultipleDialog(primaryStage);
            	pcg.readAdditionalLogFiles(list);
            	reinstallCenterLayout();
            }
        });
        MenuItem loadeparsedMenueItem = new MenuItem("Loade additional parsed Logfile(s)");
        loadeparsedMenueItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	List<File> list = fileChooser.showOpenMultipleDialog(primaryStage);
            	pcg.readParsedFiles(list);           
            	reinstallCenterLayout();
            }
        });
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(actionEvent -> Platform.exit());

        
        
        fileMenu.getItems().addAll(saveMenuItem, loadeMenueItem,loadeparsedMenueItem, new SeparatorMenuItem(), exitMenuItem);
        menuBar.getMenus().add(fileMenu);
        
        
	}
    public static void reinstallCenterLayout(){
    	try {
			changeSceneBorderPane("/analyzeMenu.fxml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void saveLogs(File f){
    	pcg.saveAllLogs(f);
    }

	public static BorderPane getRoot() {
        return rootLayout;
    }

    public static void changeScene(String scene) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(scene));
        AnchorPane ap = (AnchorPane) loader.load();

        BorderPane border = Main.getRoot();
        border.setCenter(ap);
    }

    public static void changeSceneBorderPane(String scene) throws IOException {
    	loadeMenue();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(scene));
        BorderPane ap = (BorderPane) loader.load();

        BorderPane border = Main.getRoot();
        border.setCenter(ap);
    }

    public static void simpleSwitch(BorderPane ap){
        BorderPane border = Main.getRoot();
        border.setCenter(ap);
    }

    public static void simpleSwitchAnchor(AnchorPane ap) {
        BorderPane border = Main.getRoot();
        border.setCenter(ap);
    }

    /**
     * Root Layout Set
     */
    public void setRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/root.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show/Put first Scene
     */
    public void initFileTab() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/loadScene.fxml"));
            AnchorPane ap = (AnchorPane) loader.load();

            // Set filetab View into the center of root layout.
            rootLayout.setCenter(ap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }


}
