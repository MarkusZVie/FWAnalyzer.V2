package at.ac.univie.FirewallLogAnayzer.GUI;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRows;
import at.ac.univie.FirewallLogAnayzer.Data.LogTypeSingelton;
import at.ac.univie.FirewallLogAnayzer.Exceptions.LogIdNotFoundException;
import at.ac.univie.FirewallLogAnayzer.Input.IInputHandler;
import at.ac.univie.FirewallLogAnayzer.Input.InputHandler;
import at.ac.univie.FirewallLogAnayzer.Output.IPreparingCompositionForGui;
import at.ac.univie.FirewallLogAnayzer.Output.PreparingCompositionForGui;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by josefweber on 22.05.17.
 */
public class AnalyzeMenuController {

	private ReportViewController rvc;
	private LogTreeViewController ltvc;
	private IPreparingCompositionForGui prepairedComposion;
	private ArrayList<LogRow> allLogRows;
	
	
	private final static int[] PADDINGS = {5,5,5,5};
	
    @FXML ListView<String> optionList;

    @FXML StackPane spCenter;

    @FXML
    VBox hb;

    @FXML
    public void initialize() {
    	rvc = new ReportViewController();
    	ltvc = new LogTreeViewController();
    	prepairedComposion = new PreparingCompositionForGui();
    	allLogRows = LogRows.getInstance().getLogRows();
    	
    	

    	
    	
    	
    	 ArrayList<Integer> numberOfThreads = new ArrayList<>();
         for(int i=0; i<13;i++){
         		//add Reports from 0 - 11
        	 if(prepairedComposion.getReport(i)!=null){
        		 numberOfThreads.add(prepairedComposion.getReport(i).getIndicater().getAllLogRows().size());
        	 }else{
        		 numberOfThreads.add(-1);
        	 }
        	 
         }
    	
        ObservableList<String> items = FXCollections.observableArrayList(
          "ACL Analysis Graphical",
          "ACL Analysis Message Per Time",
          "Log Tree Display (" + allLogRows.size() + ")",
          "Scanning & Foot-Printing (" + numberOfThreads.get(12) + ")",
          "DoS-Attacks (" + numberOfThreads.get(1) + ")",
          "IP-Spoofing (" + numberOfThreads.get(2) + ")",
          "Connection High Checking (" + numberOfThreads.get(3) + ")",
          "Routing Manipulation (" + numberOfThreads.get(4) + ")",
          "Syn-Attack (" + numberOfThreads.get(5) + ")",
          "ICMP Based Attaks (" + numberOfThreads.get(6) + ")",
          "TCP Based Attaks (" + numberOfThreads.get(7) + ")",
          "UDP Based Attaks (" + numberOfThreads.get(8) + ")",
          "Brute Force (" + numberOfThreads.get(9) + ")",
          "Weak Indicater of an Attack (" + numberOfThreads.get(10) + ")",
          "Other Attacks (" + numberOfThreads.get(11) + ")"
        );

        optionList.setItems(items);
        hb = new VBox();
        optionList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearSettings();
                changeSettings(optionList.getSelectionModel().getSelectedItem());
            }
        });
    }

    public void clearSettings(){
        spCenter.getChildren().clear();
    }

    public void changeSettings(String selectedItem){
        System.out.println("clicked: " + selectedItem);
       
        //startsWith method, because the number of Threads disurbed the comparation
        if(selectedItem.startsWith("ACL Analysis Graphical")){;
        	createACLgraphical();
        }
        if(selectedItem.startsWith("ACL Analysis Message Per Time")){
        	createACLmpt();
        }
        if(selectedItem.startsWith("Log Tree Display")){
        	createTreeView();
        }
        if(selectedItem.startsWith("DoS-Attacks")){
        	createReportView(1);
        }
        if(selectedItem.startsWith("IP-Spoofing")){
        	createReportView(2);
        }
        if(selectedItem.startsWith("Connection High Checking")){
        	createReportView(3);
        }
        if(selectedItem.startsWith("Routing Manipulation")){
        	createReportView(4);
        }
        if(selectedItem.startsWith("Syn-Attack")){
        	createReportView(5);
        }
        if(selectedItem.startsWith("ICMP Based Attaks")){
        	createReportView(6);
        }
        if(selectedItem.startsWith("TCP Based Attaks")){
        	createReportView(7);
        }
        if(selectedItem.startsWith("UDP Based Attaks")){
        	createReportView(8);
        }
        if(selectedItem.startsWith("Brute Force")){
        	createReportView(9);
        }
        if(selectedItem.startsWith("Weak Indicater of an Attack")){
        	createReportView(10);
        }
        if(selectedItem.startsWith("Other Attacks")){
        	createReportView(11);
        }
        if(selectedItem.startsWith("Scanning & Foot-Printing")){
        	createReportView(12);
        }
        
        System.out.println(selectedItem + " default Switch");
        
        /*
        switch (selectedItem) {
            case "DoS Analysis Graphical":
                createACLgraphical();
                break;
            case "DoS Analysis MPT":
                createACLmpt();
                break;
            case "Log Tree Display":
                createTreeView();
                break;
            case "Certain Attack":
                createReportView(0);
                break;
            case "DoS":
                createReportView(1);
                break;
            case "IP-Spoofing":
                createReportView(2);
                break;
            case "Connection High Checking":
                createReportView(3);
                break;
            case "Routing Manipulation":
                createReportView(4);
                break;
            case "Syn-Attack":
                createReportView(5);
                break;
            case "ICMP Based Attaks":
                createReportView(6);
                break;
            case "TCP Based Attaks":
                createReportView(7);
                break;
            case "UDP Based Attaks":
                createReportView(8);
                break;
            case "Brute Force":
                createReportView(9);
                break;
            case "Weak Indicater of an Attack":
                createReportView(10);
                break;
            case "Other Attacks":
                createReportView(11);
                break;
            default:
                System.out.println(selectedItem + " default Switch");
        }
        */

    }

    
    
    private void createReportView(int i) {
    	try {
    		
    		spCenter.getChildren().clear();
    		spCenter.getChildren().add(rvc.getReportViewNode(i, spCenter.getWidth(), spCenter.getHeight(),allLogRows,prepairedComposion.getReport(i)));
    		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}

	private void createTreeView() {
    	try {
    		
    		spCenter.getChildren().clear();
    		spCenter.getChildren().add(ltvc.getTreeViewNode(spCenter.getWidth(),allLogRows));
    		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void createACLgraphical(){
        hb = new VBox();

        HBox vbHead = new HBox(20);
        Label header = new Label("Graphical Analysis of ACL-Denies (Access Control List)");
        vbHead.setPadding(new Insets(20,20,20,20));
        vbHead.getChildren().addAll(header);




        HBox vb0 = new HBox(20);
        vb0.setPadding(new Insets(20,20,20,20));
        final String[] cbitems = {"TCP", "icmp", "udp"};
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "TCP", "icmp", "udp")
        );
        Label err = new Label("");
        //cb.getSelectionModel().selectFirst();
        final Label selectionLabel = new Label("Protocol");
        final String[] selection = new String[1];

        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //selectionLabel.setText(cbitems[newValue.intValue()]);
                selection[0] = cbitems[newValue.intValue()].toString();
            }
        });
        vb0.getChildren().addAll(selectionLabel, cb, err);

        HBox vb1 = new HBox(20);
        Button analyzeA = new Button("Graphical analysis");
        vb1.getChildren().addAll(analyzeA);

        hb.getChildren().addAll(vbHead, vb0, vb1);
        spCenter.getChildren().addAll(hb);

        analyzeA.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // Main.changeScene("/dosGraphical.fxml");

                boolean isSelected = cb.getSelectionModel().isEmpty();
                if (isSelected){
                    err.setText("Please choose a Protocol");
                } else {
                    FXMLLoader loader= new FXMLLoader(getClass().getResource("/dosGraphical.fxml"));
                    AnchorPane root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    DoSControllerGraphical ch = loader.getController();
                    ch.setProtocol(selection[0]);
                    ch.trigger();
                    //Scene sceneR = new Scene(root);
                    Main.simpleSwitchAnchor(root);
                }


            }
        });

    }

    public void createACLmpt(){
        hb = new VBox();

        HBox vbHead = new HBox(20);
        Label header = new Label("Message per Time Analysis of ACL-Denies (Access Control List)");
        vbHead.setPadding(new Insets(20,20,20,20));
        vbHead.getChildren().addAll(header);

        HBox vb0 = new HBox(20);
        vb0.setPadding(new Insets(20,20,20,20));
        final String[] cbitems = {"TCP", "icmp", "udp"};
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "TCP", "icmp", "udp")
        );
        //cb.getSelectionModel().selectFirst();
        final Label selectionLabel = new Label("Protocol");
        final String[] selection = new String[1];

        Label err = new Label("");
        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //selectionLabel.setText(cbitems[newValue.intValue()]);
                selection[0] = cbitems[newValue.intValue()].toString();
            }
        });
        vb0.getChildren().addAll(selectionLabel, cb, err);


        HBox vb1 = new HBox(20);
        final Label slotLabel = new Label("Timeslot (min)");
        // http://docs.oracle.com/javafx/2/ui_controls/slider.htm
        final Slider slotValue = new Slider(15,30,60);
        slotValue.setMin(15);
        slotValue.setMax(60);
        slotValue.setValue(30);
        slotValue.setShowTickLabels(true);
        slotValue.setShowTickMarks(true);
        slotValue.setMajorTickUnit(15);
        slotValue.setMinorTickCount(5);
        final Label opacityValue = new Label(Double.toString(slotValue.getValue()));
        vb1.setPadding(new Insets(15,15,15,15));
        vb1.getChildren().addAll(slotLabel, slotValue, opacityValue);


        HBox vb2 = new HBox(20);
        Label treshholdLabel = new Label("DoS Treshold");
        final TextField treshold = new TextField();
        vb2.setPadding(new Insets(15,15,15,15));
        vb2.getChildren().addAll(treshholdLabel, treshold);

        HBox vb3 = new HBox(20);
        Label explanation = new Label("Wie viele Logs (Treshold) dürfen in einem zeitlichen Bereich (Timeslot) maximal passieren.");
        explanation.setTextFill(Color.GRAY);
        Button analyzeB = new Button("Analyze on Messages Per Time");
        vb3.setPadding(new Insets(15,15,15,15));
        vb3.getChildren().addAll(analyzeB,explanation);

        hb.getChildren().addAll(vbHead, vb0, vb1, vb2, vb3);
        spCenter.getChildren().addAll(hb);

        slotValue.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                //cappuccino.setOpacity(new_val.doubleValue());
                opacityValue.setText(String.format("%.2f", new_val));
                slotValue.setValue(new_val.intValue());
            }
        });

        treshold.setText("10");

        analyzeB.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                double parsePrice = Double.parseDouble(treshold.getText());

                boolean isSelected = cb.getSelectionModel().isEmpty();
                if (isSelected){
                    err.setText("Please choose a Protocol");
                } else {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/dosMpt.fxml"));
                    BorderPane root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    DoSControllerMpt ch = loader.getController();
                    ch.setTreshold(parsePrice);
                    ch.setTimeslot(((int) slotValue.getValue()));
                    ch.setProtocol(selection[0]);
                    ch.trigger();
                    //Scene sceneR = new Scene(root);
                    Main.simpleSwitch(root);
                }
            }
        });
    }


}
