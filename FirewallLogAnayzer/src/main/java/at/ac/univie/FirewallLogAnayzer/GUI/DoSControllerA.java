package at.ac.univie.FirewallLogAnayzer.GUI;

import at.ac.univie.FirewallLogAnayzer.Data.DoSData;
import at.ac.univie.FirewallLogAnayzer.Data.DoSDataList;
import at.ac.univie.FirewallLogAnayzer.Data.LogTypeSingelton;
import at.ac.univie.FirewallLogAnayzer.Exceptions.LogIdNotFoundException;
import at.ac.univie.FirewallLogAnayzer.Input.IInputHandler;
import at.ac.univie.FirewallLogAnayzer.Input.InputHandler;
import at.ac.univie.FirewallLogAnayzer.Processing.AnalyzerDos;
import at.ac.univie.FirewallLogAnayzer.Processing.IProcessingAnalyse;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import com.sun.org.apache.xml.internal.dtm.*;
import com.sun.org.apache.xpath.internal.operations.Number;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class DoSControllerA {

    private HashMap<String, ArrayList<DoSData>> countrymap;
    private DoSDataList ddl;
    private IProcessingAnalyse da;

    private LineChart<String,Number> lineChart = null;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;

    private LineChart<String,Number> singleLineChart = null;
    private CategoryAxis xAxisSingleLine;
    private NumberAxis yAxisSingleLine;

    private BarChart<String,Number> bc = null;
    private CategoryAxis xxAxisBar;
    private NumberAxis yyAxisBar;

    @FXML Label mainLabel;
    @FXML private AnchorPane mainAP;
    @FXML private AnchorPane apCenter;

    @FXML private Button backtochartBtn;
    @FXML private Button btn2;

    @FXML private AnchorPane apCenterZoom;
    @FXML private AnchorPane apLinechart;


    @FXML
    public void initialize() {
        System.out.println("init DoSController-A");

        tmpCallMainCode();
        // DOS
        da = new AnalyzerDos();
        ddl = da.analyseDos("icmp", 60);

        // Sort country to Controller-Objekt
        countrymap = da.messagesOfCountry(ddl);
        HashMap<String, Integer> countryCount = da.sumMessagesPerCountry(countrymap, "asc");
        HashMap<String, Integer> cc = countryCount;


        initpiechart(cc);
        apCenterZoom.setVisible(false);
        apLinechart.setVisible(false);

        backtochartBtn.setVisible(false);
        btn2.setVisible(false);
    }

    // back to pie
    public void backtochart(){
        apCenter.setVisible(true);
        apCenterZoom.setVisible(false);
        apLinechart.setVisible(false);

        backtochartBtn.setVisible(false);
        btn2.setVisible(false);

        if (!bc.getData().isEmpty()){
            System.out.println("Remove Series from Bar Chart");
            bc.getData().remove((bc.getData().size()-1));
        }
    }

    // back to bar
    public void btn2Method(){
        apCenter.setVisible(false);
        apCenterZoom.setVisible(true);
        apLinechart.setVisible(false);

        backtochartBtn.setVisible(true);
        btn2.setVisible(false);

        if (!singleLineChart.getData().isEmpty()) {
            System.out.println("Remove Series from Single Line Chart");
            singleLineChart.getData().remove((singleLineChart.getData().size()-1));

            singleLineChart.getData().clear();
            singleLineChart.layout();
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
        caption.setTextFill(Color.GRAY);
        caption.setStyle("-fx-font: 16 arial;");

        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    caption.setTranslateX(e.getSceneX());
                    caption.setTranslateY(e.getSceneY());
                    caption.setText(data.getName() + " " + data.getPieValue());
                }
            });
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    zoomCountryShowIps(data.getName());
                }
            });
        }

        apCenter.getChildren().addAll(chart,caption);

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
        caption.setTextFill(Color.BLACK);
        caption.setStyle("-fx-font: 12 arial;");

        for (final XYChart.Series<String, Number> data:bc.getData() ){
            for (final XYChart.Data<String, Number> item: data.getData()){
                item.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        caption.setTranslateX(event.getSceneX());
                        caption.setTranslateY(event.getSceneY());
                        caption.setText(item.getXValue() + ": " + item.getYValue() + " messages");
                    }
                });

                item.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        zoomSingleIp(item.getXValue());
                    }
                });
            }
        }
        apCenterZoom.getChildren().addAll(bc,caption);
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
        apCenterZoom.getChildren().add(lineChart);
        /*
        Legende ausblenden
        -> Hover an!
        1280-720 px
        Zoom funktion?
        ticks?
        zuerst IPs anzeigen und dann einzelne XY Charts machen?
         */
    }

    public void initLineChartSingle(DoSData singleData){
        if (singleLineChart == null) {
            xAxisSingleLine = new CategoryAxis();
            yAxisSingleLine = new NumberAxis();
            xAxisSingleLine.setLabel("Time");
        } else {
            singleLineChart.getData().removeAll(singleLineChart.getData());
            singleLineChart.getData().remove(xAxisSingleLine);
            singleLineChart.getData().remove(yAxisSingleLine);
        }
        singleLineChart = new LineChart(xAxisSingleLine, yAxisSingleLine);
        //singleLineChart.setTitle("Single Linechart");

        XYChart.Series series1 = new XYChart.Series();
        for (int i = 0; i < singleData.getStd().getDifferences().size(); i++) {
            //series1 = new XYChart.Series();
            series1.setName(singleData.getMessages().get(0).getDateTime().toString());
            String sTmp = Objects.toString(singleData.getMessages().get(i).getDateTime(), null);
            System.out.println(sTmp);
            series1.getData().add(new XYChart.Data(sTmp, singleData.getStd().getDifferences().get(i)));

        }
        singleLineChart.getData().add(series1);
        apLinechart.getChildren().add(singleLineChart);
    }

    public void zoomIp(String country){
        System.out.println(country + " clicked .. show Info");
        ArrayList<DoSData> countryData = countrymap.get(country);
        System.out.println("IP's: " + countryData.size());


        apCenter.setVisible(false);
        apCenterZoom.setVisible(false);
        apLinechart.setVisible(true);

        backtochartBtn.setVisible(false);
        btn2.setVisible(true);

        //ddl
        //initLineChart(countryData);

    }

    public void zoomSingleIp(String ip){
        apCenter.setVisible(false);
        apCenterZoom.setVisible(false);
        apLinechart.setVisible(true);

        backtochartBtn.setVisible(false);
        btn2.setVisible(true);

        System.out.println();
        DoSData ddSingle = da.getSingleIP(ddl, ip);
        System.out.println("AAA " + ddSingle.getMessages().size());
        initLineChartSingle(ddSingle);
    }

    public void zoomCountryShowIps(String country){
        // Zeige alle IPs als Barchart 1xIP hat <IPString,MessageCount>
        apCenter.setVisible(false);
        apCenterZoom.setVisible(true);
        backtochartBtn.setVisible(true);

        btn2.setVisible(false);
        backtochartBtn.setVisible(true);
        ArrayList<DoSData> countryData = countrymap.get(country);
        initBarChart(countryData, country);
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

}
