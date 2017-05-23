package at.ac.univie.FirewallLogAnayzer.GUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;

/**
 * Created by josefweber on 22.05.17.
 */
public class analyzeMenuController {


    @FXML ListView<String> optionList;

    @FXML StackPane spCenter;

    @FXML
    VBox hb;

    @FXML
    public void initialize() {
        System.out.println("Init Analyze Menu");
        ObservableList<String> items = FXCollections.observableArrayList(
          "DoS","DDoS",".."
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
            case "DoS":
                hb = new VBox();

                HBox vb1 = new HBox(20);
                Label slotLabel = new Label("Timeslot (min)");
                // http://docs.oracle.com/javafx/2/ui_controls/slider.htm
                Slider slotValue = new Slider(15,30,45);
                slotValue.setMin(15);
                slotValue.setMax(45);
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
                TextField treshold = new TextField();
                vb2.setPadding(new Insets(15,15,15,15));
                vb2.getChildren().addAll(treshholdLabel, treshold);

                HBox vb3 = new HBox(20);
                Button analyzeA = new Button("Graphical analysis");
                Button analyzeB = new Button("Analyze on attacks");
                vb3.setPadding(new Insets(15,15,15,15));
                vb3.getChildren().addAll(analyzeA,analyzeB);

                hb.getChildren().addAll(vb1, vb2, vb3);
                spCenter.getChildren().addAll(hb);

                slotValue.valueProperty().addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number> ov,
                                        Number old_val, Number new_val) {
                        //cappuccino.setOpacity(new_val.doubleValue());
                        opacityValue.setText(String.format("%.2f", new_val));
                    }
                });

                analyzeA.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        try {
                            Main.changeScene("/dosGraphicsA.fxml");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

                analyzeB.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        Main.changeSceneDoSB("/dosGraphicsB.fxml", 12345.0, 99.9);
                    }
                });
                break;
            case "DDoS":
                System.out.println("DDoS B-)");
                break;
            default:
                System.out.println(selectedItem + " default Switch");
        }

    }
    public void abchange() throws IOException {
       /* FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/analyzeDoSAttack.fxml"));
        Parent root = fxmlLoader.load();

        DoSControllerB ch = fxmlLoader.<DoSControllerB>getController();

        ch.setTreshold(12345.534);

        Scene scene = new Scene(root);
        Stage homeStage = (Stage) spCenter.getScene().getWindow();

        homeStage.setScene(scene);
        homeStage.show();  */



    }



}
