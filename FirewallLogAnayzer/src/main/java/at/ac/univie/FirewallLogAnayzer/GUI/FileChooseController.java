package at.ac.univie.FirewallLogAnayzer.GUI;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import at.ac.univie.FirewallLogAnayzer.Data.LogRows;
import at.ac.univie.FirewallLogAnayzer.Data.LogTypeSingelton;
import at.ac.univie.FirewallLogAnayzer.Exceptions.LogIdNotFoundException;
import at.ac.univie.FirewallLogAnayzer.Input.AsynchronFileParse;
import at.ac.univie.FirewallLogAnayzer.Input.IInputHandler;
import at.ac.univie.FirewallLogAnayzer.Input.InputHandler;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileChooseController {
	
	private Label pi;
	private ProgressBar pb;
	private Stage rootStage;
	private long numberOfRows;
	private StringProperty value = new SimpleStringProperty("");
	private BorderPane masterLayout;
	private StackPane logoContainer;
	
	public Node getFileChooseNode(double windowWidth, double windowHeight, Stage rootStage, BorderPane masterLayout){
		BorderPane rootLayout = new BorderPane();
		this.rootStage = rootStage;
		this.masterLayout = masterLayout;
		VBox centerLayout = new VBox();
		final FileChooser fileChooser = new FileChooser();
		
		rootLayout.setCenter(centerLayout);
		HBox buttonBar = new HBox();
		logoContainer = new StackPane();
		Image logo = new Image("FirewallLogAnalyzerLogo.png");
		ImageView logoView = new ImageView(logo);
		logoContainer.getChildren().add(logoView);
		logoContainer.setAlignment(Pos.CENTER);
		centerLayout.getChildren().add(logoContainer);
		centerLayout.getChildren().add(buttonBar);
		
		
		
		Button chooseLogFile = new Button();
		chooseLogFile.setText("Open Log-File");
		chooseLogFile.setPrefHeight(windowHeight-logoContainer.getHeight());
		chooseLogFile.setPrefWidth(windowWidth/2);
		chooseLogFile.setStyle("-fx-font-size:50");
		chooseLogFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	value.setValue("Parser is starting ...");
            	List<File> list = fileChooser.showOpenMultipleDialog(rootStage);
            	readeFile(list);
            }

			
        });
		
		Button chooseSavedFile = new Button();
		chooseSavedFile.setText("Open parsed File");
		chooseSavedFile.setPrefHeight(windowHeight-logoContainer.getHeight());
		chooseSavedFile.setPrefWidth(windowWidth/2);
		chooseSavedFile.setStyle("-fx-font-size:50");
		buttonBar.getChildren().add(chooseLogFile);
		buttonBar.getChildren().add(chooseSavedFile);
		
		StackPane bottemLayout = new StackPane();
		pb = new ProgressBar(0.0);
		pb.setPrefWidth(windowWidth);
		pb.setPrefHeight(60);
		pi = new Label();
		pi.setText("0.0%");
		pi.setStyle("-fx-font-size:50");
		pi.textProperty().bind(value);
		
		bottemLayout.getChildren().add(pb);
		bottemLayout.getChildren().add(pi);
		rootLayout.setBottom(bottemLayout);
		
		
		return rootLayout;
	}
	
	public synchronized void updateProgress(long numberToRead, long readedRows){
		this.numberOfRows = numberToRead;
		double percentage = ((Double.parseDouble(readedRows+"")/(Double.parseDouble(numberToRead+"")))*100);
		double percentageRound = Math.round(percentage*100);
		percentageRound = percentageRound/100;
		pb.setProgress(percentageRound/100);
		String s = readedRows + " of " + numberToRead + " " + percentageRound;
		//pi.setText(s);
		
		Platform.runLater(new Runnable() {

            @Override
            public void run() {
            	value.setValue(String.valueOf(s));
            }
        });
		
		System.out.println(percentageRound);
		
	}
	
	public StackPane getLogoContainer(){
		return logoContainer;
	}
	
	
	
	public void readeFile(List<File> fileList) {
		AsynchronFileParse afp = new AsynchronFileParse(fileList,this);
		afp.start();
	}
	
	
}
