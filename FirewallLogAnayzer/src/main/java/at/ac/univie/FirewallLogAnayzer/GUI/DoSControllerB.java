package at.ac.univie.FirewallLogAnayzer.GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

/**
 * Created by josefweber on 22.05.17.
 */
public class DoSControllerB {

    @FXML
    Button btnclick;
    @FXML
    Label fuckoff;

    public double treshold;
    public double timeslot;

    public DoSControllerB(){}
    @FXML
    public void initialize() {
        System.out.println("Init DoS-Controller B");
        System.out.println(treshold);
        fuckoff.setText(String.valueOf(treshold));

        btnclick.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                System.out.println(treshold);
                fuckoff.setText("change scene//give values: " + treshold + " /&/ " + timeslot);
            }
        });
    }

    public void setTreshold(double tr){
        treshold = tr;
    }

    public void setTimeslot(double sv){
        timeslot = sv;
    }
}
