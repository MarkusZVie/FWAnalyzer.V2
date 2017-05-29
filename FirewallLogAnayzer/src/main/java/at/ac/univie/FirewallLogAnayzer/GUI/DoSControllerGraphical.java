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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


public class DoSControllerGraphical {

    private HashMap<String, ArrayList<DoSData>> countrymap;
    private DoSDataList ddl;
    private IProcessingAnalyseGenerel da;

    private LineChart<String,Number> lineChart = null;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;

     LineChart<String, Number> singleLineChart = null;
    private CategoryAxis xAxisSingleLine;
    private NumberAxis yAxisSingleLine;
    @FXML Button prevoiusLine;
    @FXML Button nextLine;
    private int currentSlotV;
    private int loopRange;
    private int startValue;

    private BarChart<String,Number> bc = null;
    private CategoryAxis xxAxisBar;
    private NumberAxis yyAxisBar;

    @FXML Label mainLabel;
    @FXML private Button backtochartBtn;
    @FXML private Button btn2;

    @FXML private AnchorPane mainAP;
    @FXML private StackPane apPie;
    @FXML private StackPane apBar;
    @FXML private StackPane apLineSingle;

    @FXML private String protocol;

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void trigger(){
        da = new AnalyzerDos();
        ddl = da.analyseDos(protocol, 60);
        countrymap = da.messagesOfCountry(ddl);
        HashMap<String, Integer> countryCount = da.sumMessagesPerCountry(countrymap);
        HashMap<String, Integer> cc = countryCount;
        initpiechart(cc);
    }

    @FXML
    public void initialize() {
        apBar.setVisible(false);
        apLineSingle.setVisible(false);

        backtochartBtn.setVisible(false);
        btn2.setVisible(false);

        nextLine.setVisible(false);
        prevoiusLine.setVisible(false);
    }

    public void backtoMenu() throws IOException {
        try {
            Main.changeSceneBorderPane("/analyzeMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // back to pie
    public void backtoPieChart(){
        apPie.setVisible(true);
        apBar.setVisible(false);
        apLineSingle.setVisible(false);

        backtochartBtn.setVisible(false);
        btn2.setVisible(false);

        if (!bc.getData().isEmpty()){
            System.out.println("Remove Series from Bar Chart");
            bc.getData().remove((bc.getData().size()-1));
            bc.setTitle("");
        }
    }

    // back to bar
    public void backtoBarChart(){
        apPie.setVisible(false);
        apBar.setVisible(true);
        apLineSingle.setVisible(false);

        backtochartBtn.setVisible(true);
        btn2.setVisible(false);

        nextLine.setVisible(false);
        prevoiusLine.setVisible(false);

        if (!singleLineChart.getData().isEmpty()) {
            System.out.println("Remove Series from Single Line Chart");
            singleLineChart.getData().remove((singleLineChart.getData().size()-1));

            singleLineChart.getData().clear();
            singleLineChart.layout();
            singleLineChart.setTitle("");
        }
        // remove Line Chart Data single/multiple
    }

    public void initpiechart(HashMap<String, Integer> cc){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : cc.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(),entry.getValue()));
        }

        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("country pie chart of messages");
        chart.setLabelLineLength(10);
        chart.setLegendSide(Side.RIGHT);
        chart.setCursor(Cursor.CROSSHAIR);
        chart.setLabelsVisible(true);

        final Label caption = new Label();
        caption.setTextFill(Color.DARKGRAY);
        caption.setStyle("-fx-font: 18 arial;");

        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    caption.setTranslateX(e.getSceneX()-500);
                    caption.setTranslateY(e.getSceneY()-300);
                    caption.setText(data.getName() + " " + data.getPieValue());
                }
            });
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    goMultipleIpLineChart(data.getName());
                }
            });
        }

        apPie.getChildren().addAll(chart,caption);

    }

    public void initBarChart(ArrayList<DoSData> countrydata, String country){
        if (bc == null) {
            xxAxisBar = new CategoryAxis();
            yyAxisBar = new NumberAxis();
            xxAxisBar.setLabel("IP");
        } else {
            bc.getData().removeAll(bc.getData());
            bc.getData().remove(xxAxisBar);
            bc.getData().remove(yyAxisBar);
            bc.getData().remove(bc.getTitle());
            xxAxisBar = new CategoryAxis();
            //yyAxisBar = new NumberAxis();
        }

        bc = new BarChart(xxAxisBar, yyAxisBar);
        bc.setTitle("IPs of " + country);

        xxAxisBar.setLabel("IP");
        yyAxisBar.setLabel("Messages");

        for (DoSData dd: countrydata){
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
                        goSingleIpLineChart(item.getXValue());
                    }
                });
            }
        }
        apBar.getChildren().addAll(bc,caption);
    }

    public void initLineChart(ArrayList<DoSData> countrydata){
        if (lineChart == null) {
            xAxis = new CategoryAxis();
            yAxis = new NumberAxis();
            xAxis.setLabel("Time");
        } else {
            lineChart.getData().removeAll(lineChart.getData());
            lineChart.getData().remove(xAxis);
            lineChart.getData().remove(yAxis);
        }
        lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Stock Monitoring");

        for (DoSData dd: countrydata){
            XYChart.Series series1 = new XYChart.Series();
            series1.setName(dd.getMessages().get(0).getSrcIP());

            for (int i = 0; i < dd.getStd().getDifferences().size(); i++) {
                String sTmp = Objects.toString(dd.getMessages().get(i).getDateTime(), null);
                series1.getData().add(new XYChart.Data(sTmp, dd.getStd().getDifferences().get(i)));
            }
            lineChart.getData().add(series1);
        }
        apBar.getChildren().add(lineChart);
    }

    public void initLineChartSingle(DoSData singleData){
        if (singleLineChart == null){
            xAxisSingleLine = new CategoryAxis();
            yAxisSingleLine = new NumberAxis();
            xAxisSingleLine.setLabel("Log");
            yAxisSingleLine.setLabel("Differences in s");
        } else {
            singleLineChart.getData().removeAll(singleLineChart.getData());
            singleLineChart.getData().remove(xAxisSingleLine);
            singleLineChart.getData().remove(yAxisSingleLine);
        }
        singleLineChart = new LineChart(xAxisSingleLine, yAxisSingleLine);
        singleLineChart.setTitle("differences bewteen message occurance from IP: " + singleData.getMessages().get(0).getSrcIP());
        singleLineChart.setLegendVisible(false);


        startValue = 0;
        int sizeAll = singleData.getStd().getDifferences().size();
        loopRange = 100;
        currentSlotV = 0;
        if (sizeAll > loopRange) {
            currentSlotV = 0;
            nextLine.setVisible(true);
            prevoiusLine.setVisible(true);
        } else {
            loopRange = sizeAll;
            nextLine.setVisible(false);
            prevoiusLine.setVisible(false);
        }

        drawNextLines(singleData, startValue, loopRange);

        nextLine.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (nextStartValue(sizeAll)){
                    apLineSingle.getChildren().remove(singleLineChart);
                    System.out.println("INcrement" + currentSlotV);
                    drawNextLines(singleData, startValue, loopRange);
                }

            }

        });

        prevoiusLine.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (prevStartValue(sizeAll)){
                    apLineSingle.getChildren().remove(singleLineChart);
                    System.out.println("DEcrement" + currentSlotV);
                    drawNextLines(singleData, startValue, loopRange);
                }

            }

        });

    }



    public void drawNextLines(DoSData singleData, int startValue, int loopRange){
        singleLineChart.getData().clear();
        System.out.println("Draw next lines Startvalue = " + startValue);
        System.out.println("Draw next lines loopRange = " + loopRange);
        int toValue = startValue + loopRange;
        System.out.println("Draw next lines toValue = " + toValue);
        int tmpA;
        int tmpB;
        XYChart.Series<String, Number> series1 = new XYChart.Series();
        for (int i = startValue; i < toValue; i++) {
            //series1 = new XYChart.Series();
            series1.setName(singleData.getMessages().get(0).getDateTime().toString());
            String sTmp = Objects.toString(singleData.getMessages().get(i).getDateTime(), null);
            //System.out.println(sTmp);
            tmpB = i + 2;tmpA = i + 1;
            String mtemp = " "+tmpA + "-" + tmpB;
            series1.getData().add(new XYChart.Data(mtemp, singleData.getStd().getDifferences().get(i)));
        }


        singleLineChart.getData().add(series1);
        apLineSingle.getChildren().add(singleLineChart);

        final Label caption = new Label("");
        caption.setTextFill(Color.DARKGRAY);
        caption.setStyle("-fx-font: 18 arial;");

        for (final XYChart.Data<String, Number> data : series1.getData()){
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    caption.setTranslateX(event.getSceneX()-500);
                    caption.setTranslateY(event.getSceneY()-300);
                    caption.setText("Diff. between msg: " + data.getXValue() + " = " + data.getYValue() + "sec");
                }

            });
        }

        apLineSingle.getChildren().add(caption);


    }

    public boolean nextStartValue(int all){
        if (incrementSlot(all)){
            int next = currentSlotV * loopRange;
            if (next > all){
                return false;
            } else {
                startValue = next;
                return true;
            }
        }
        return false;
    }
    public boolean incrementSlot(int all){
        int tmpCurr = currentSlotV+1;
        if ((tmpCurr * loopRange) + loopRange > all) {
            System.out.println((tmpCurr * loopRange) + loopRange + " !! W");
            // set value to all!!
            // damit die restlichen erquetschten angezeigt werden
            return false;
        }
        currentSlotV++;
        return true;
    }




    public boolean prevStartValue(int all){
        if (decrementSlot()){
            int next = currentSlotV * loopRange;
            if (next < all){
                startValue = next;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    public boolean decrementSlot(){
        if (currentSlotV == 0){
            return false;
        } else {
            currentSlotV--;
            return true;
        }
    }

    public void zoomIp(String country){
        System.out.println(country + " clicked .. show Info");
        ArrayList<DoSData> countryData = countrymap.get(country);
        System.out.println("IP's: " + countryData.size());


        apPie.setVisible(false);
        apBar.setVisible(false);
        apLineSingle.setVisible(true);

        backtochartBtn.setVisible(false);
        btn2.setVisible(true);

        //ddl
        //initLineChart(countryData);
    }

    public void goSingleIpLineChart(String ip){
        apPie.setVisible(false);
        apBar.setVisible(false);
        apLineSingle.setVisible(true);

        backtochartBtn.setVisible(false);
        btn2.setVisible(true);

        System.out.println();
        DoSData ddSingle = da.getSingleIP(ddl, ip);
        System.out.println("AAA " + ddSingle.getMessages().size());

        if (ddSingle.getStd().getDifferences() == null){
                System.out.println("no differences avaliable for this ip");
        } else {
            initLineChartSingle(ddSingle);
        }
    }

    public void goMultipleIpLineChart(String country){
        // Zeige alle IPs als Barchart 1xIP hat <IPString,MessageCount>
        apPie.setVisible(false);
        apBar.setVisible(true);
        backtochartBtn.setVisible(true);

        btn2.setVisible(false);
        backtochartBtn.setVisible(true);
        ArrayList<DoSData> countryData = countrymap.get(country);
        initBarChart(countryData, country);
    }

}
