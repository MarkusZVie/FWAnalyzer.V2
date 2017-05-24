package at.ac.univie.FirewallLogAnayzer.GUI;

import at.ac.univie.FirewallLogAnayzer.Data.LogTypeSingelton;
import at.ac.univie.FirewallLogAnayzer.Exceptions.LogIdNotFoundException;
import at.ac.univie.FirewallLogAnayzer.Input.IInputHandler;
import at.ac.univie.FirewallLogAnayzer.Input.InputHandler;
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

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by josefweber on 22.05.17.
 */
public class AnalyzeMenuController {


    @FXML ListView<String> optionList;

    @FXML StackPane spCenter;

    @FXML
    VBox hb;

    @FXML
    public void initialize() {
        System.out.println("Init Analyze Menu");
        ObservableList<String> items = FXCollections.observableArrayList(
          "DoS Analysis Graphical","DoS Analysis MPT","NewWindowTest",".."
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
        switch (selectedItem) {
            case "DoS Analysis Graphical":
                createACLgraphical();
                break;
            case "DoS Analysis MPT":
                createACLmpt();
                break;
            case "NewWindowTest":
                createReportTab();
                break;
            default:
                System.out.println(selectedItem + " default Switch");
        }

    }

    private void createReportTab() {
    	try {
    		spCenter.getChildren().clear();
    		spCenter.getChildren().add(FXMLLoader.load(getClass().getResource("/ReportDoS.fxml")));
    		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void createACLgraphical(){
        hb = new VBox();

        HBox vb0 = new HBox(20);
        vb0.setPadding(new Insets(20,20,20,20));
        final String[] cbitems = {"TCP", "icmp"};
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "TCP", "icmp")
        );
        //cb.getSelectionModel().selectFirst();
        final Label selectionLabel = new Label("Protocol");
        final String[] selection = new String[1];

        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                selectionLabel.setText(cbitems[newValue.intValue()]);
                selection[0] = cbitems[newValue.intValue()].toString();
            }
        });
        vb0.getChildren().addAll(selectionLabel, cb);

        HBox vb1 = new HBox(20);
        Button analyzeA = new Button("Graphical analysis");
        vb1.getChildren().addAll(analyzeA);

        hb.getChildren().addAll(vb0, vb1);
        spCenter.getChildren().addAll(hb);

        analyzeA.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    Main.changeScene("/dosGraphical.fxml");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    public void createACLmpt(){
        hb = new VBox();

        HBox vb0 = new HBox(20);
        vb0.setPadding(new Insets(20,20,20,20));
        final String[] cbitems = {"TCP", "icmp"};
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "TCP", "icmp")
        );
        //cb.getSelectionModel().selectFirst();
        final Label selectionLabel = new Label("Protocol");
        final String[] selection = new String[1];

        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                selectionLabel.setText(cbitems[newValue.intValue()]);
                selection[0] = cbitems[newValue.intValue()].toString();
            }
        });
        vb0.getChildren().addAll(selectionLabel, cb);


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
        Button analyzeB = new Button("Analyze on attacks");
        vb3.setPadding(new Insets(15,15,15,15));
        vb3.getChildren().addAll(analyzeB);

        hb.getChildren().addAll(vb0, vb1, vb2, vb3);
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

                FXMLLoader loader= new FXMLLoader(getClass().getResource("/dosMpt.fxml"));
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
        });
    }

    public void tmpCallMainCode(){
        IInputHandler inputHandler = new InputHandler();
        // /Users/josefweber/Desktop/SyslogCatchAll-2017-03-14.txt
        // C:\Users\Lezard\Desktop\SyslogCatchAll-2017-03-14.txt
        try {
            inputHandler.loadeFirewallLog("/Users/josefweber/Desktop/SyslogCatchAll-2017-03-14.txt", LogTypeSingelton.getInstance().getSupportedLogTypeList().get(0));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (LogIdNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadOnce(){
        tmpCallMainCode();
    }

}
