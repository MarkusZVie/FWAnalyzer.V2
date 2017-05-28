package at.ac.univie.FirewallLogAnayzer.GUI;

import at.ac.univie.FirewallLogAnayzer.Data.DoSData;
import at.ac.univie.FirewallLogAnayzer.Data.DoSDataList;
import at.ac.univie.FirewallLogAnayzer.Data.LogTypeSingelton;
import at.ac.univie.FirewallLogAnayzer.Exceptions.LogIdNotFoundException;
import at.ac.univie.FirewallLogAnayzer.Input.IInputHandler;
import at.ac.univie.FirewallLogAnayzer.Input.InputHandler;
import at.ac.univie.FirewallLogAnayzer.Processing.AnalyzerDos;
import at.ac.univie.FirewallLogAnayzer.Processing.IProcessingAnalyseGenerel;
import com.sun.org.apache.xpath.internal.operations.Number;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

/**
 * Created by josefweber on 22.05.17.
 */
public class DoSControllerMpt {

    private DoSDataList ddl;
    private IProcessingAnalyseGenerel da;

    private BarChart<String,Number> bcSingle;
    private CategoryAxis xAxisSingle;
    private NumberAxis yAxisSingle;

    @FXML Button backToMainBar;
    @FXML Button btnclick;
    @FXML Label headerLabel;
    @FXML BorderPane mainAP;
    @FXML private StackPane spBarChart;
    @FXML private StackPane spBarSingleDetail;

    public double treshold;
    public int timeslot;
    public String protocol;


    public DoSControllerMpt(){
    }
    @FXML
    public void initialize() {
        headerLabel.setText(String.valueOf(treshold));
        backToMainBar.setVisible(false);

    }

    public void initbarchart(ArrayList<DoSData> critical){
        BarChart<String,Number> bc;
        CategoryAxis xxAxisBar = new CategoryAxis();
        NumberAxis yyAxisBar = new NumberAxis();
        bc = new BarChart(xxAxisBar, yyAxisBar);

        //yyAxisBar.setLabel("ACL Denies per Unit");
        xxAxisBar.setLabel("IP");
        yyAxisBar.setLabel("Messages");

        for (DoSData dd: critical){
            XYChart.Series series1 = new XYChart.Series();
            series1.setName(dd.getMessages().get(0).getSrcIP());
            String sTmp = dd.getMessages().get(0).getSrcIP();
            series1.getData().add(new XYChart.Data(sTmp, dd.getMessages().size()));
            bc.getData().add(series1);
        }

        bc.setCursor(Cursor.CROSSHAIR);
        bc.setLegendVisible(true);
        bc.isResizable();

        final Label caption = new Label("");
        caption.setTextFill(Color.DARKGRAY);
        caption.setStyle("-fx-font: 18 arial;");

        for (final XYChart.Series<String, Number> data:bc.getData() ){
            for (final XYChart.Data<String, Number> item: data.getData()){
                item.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        caption.setTranslateX(event.getSceneX()-500);
                        caption.setTranslateY(event.getSceneY()-300);
                        caption.setText(item.getXValue() + ": " + item.getYValue() + " messages");
                    }
                });

                item.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        //goSingleIpLineChart(item.getXValue());
                        System.out.println("clicked on bar chart .." + item.getXValue());
                        goDetailBarChart(item.getXValue());

                    }
                });
            }
        }

        spBarChart.getChildren().addAll(bc,caption);

    }

    public void goDetailBarChart(String ip){
        spBarChart.setVisible(false);
        spBarSingleDetail.setVisible(true);
        backToMainBar.setVisible(true);

        DoSData ddSingle = da.getSingleIP(ddl, ip);

        if (ddSingle.getMptList() == null){
            System.out.println("no acl per time for this ip");
        } else {
            initSingleDetailBar(ddSingle);
        }
    }

    public void initSingleDetailBar(DoSData ddd){
        if (bcSingle == null) {
            xAxisSingle = new CategoryAxis();
            yAxisSingle = new NumberAxis();
            xAxisSingle.setLabel("IP");
        } else {
            bcSingle.getData().removeAll(bcSingle.getData());
            bcSingle.getData().remove(xAxisSingle);
            bcSingle.getData().remove(yAxisSingle);
            bcSingle.getData().remove(bcSingle.getTitle());
        }
        xAxisSingle = new CategoryAxis();
        bcSingle = new BarChart(xAxisSingle, yAxisSingle);
        bcSingle.setTitle("TITLE");
        xAxisSingle.setLabel("Time Slot");
        yAxisSingle.setLabel("MPT Value");

        bcSingle.setLegendVisible(false);

        int time = timeslot;

        for (int i = 0; i < ddd.getMptList().size(); i++) {
            XYChart.Series series1 = new XYChart.Series();
            //series1.setName(ddd.getMessages().get(0).getSrcIP());
            series1.getData().add(new XYChart.Data("Slot" + i, ddd.getMptList().get(i)));
            time = time + timeslot;

            bcSingle.getData().add(series1);
        }

        final Label caption = new Label("");
        caption.setTextFill(Color.DARKGRAY);
        caption.setStyle("-fx-font: 18 arial;");

        for (final XYChart.Series<String, Number> data:bcSingle.getData() ){
            for (final XYChart.Data<String, Number> item: data.getData()){
                item.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        caption.setTranslateX(event.getSceneX()-500);
                        caption.setTranslateY(event.getSceneY()-300);
                        caption.setText("Slot: " + item.getXValue() + " Messages " + item.getYValue());
                    }
                });

                item.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        System.out.println("clicked on bar chart .." + item.getXValue());
                    }
                });
            }
        }

        spBarSingleDetail.getChildren().addAll(bcSingle, caption);
    }

    public void setTreshold(double tr){
        treshold = tr;
    }

    public void setTimeslot(int sv){
        timeslot = sv;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void trigger(){
        headerLabel.setText("Protocol " + protocol + " Timeslot: " + timeslot + " Treshold: " + treshold);
        da = new AnalyzerDos();
        ddl = da.analyseDos(protocol, timeslot);
        ArrayList<DoSData> critical = da.analyzeMpt(ddl, treshold);
        spBarSingleDetail.setVisible(false);
        initbarchart(critical);
    }

    public void btmMethod() throws IOException {
        try {
            Main.changeSceneBorderPane("/analyzeMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backToMainBar() {
        backToMainBar.setVisible(false);
        spBarSingleDetail.setVisible(false);
        spBarChart.setVisible(true);

        spBarSingleDetail.getChildren().removeAll(bcSingle);

        if (!bcSingle.getData().isEmpty()){
            System.out.println("Remove Series from Bar Chart Single Detail B");
            bcSingle.getData().remove((bcSingle.getData().size()-1));
            bcSingle.setTitle("");
            bcSingle.getData().clear();
        }
    }
}
