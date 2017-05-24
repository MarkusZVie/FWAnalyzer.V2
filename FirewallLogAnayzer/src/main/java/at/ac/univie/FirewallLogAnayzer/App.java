package at.ac.univie.FirewallLogAnayzer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.net.whois.WhoisClient;

import at.ac.univie.FirewallLogAnayzer.Data.CompositionAnalysingSettings;
import at.ac.univie.FirewallLogAnayzer.Data.CompositionCompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.DoSData;
import at.ac.univie.FirewallLogAnayzer.Data.DoSDataList;
import at.ac.univie.FirewallLogAnayzer.Data.HashPairDoubleValue;
import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRows;
import at.ac.univie.FirewallLogAnayzer.Data.LogTypeSingelton;
import at.ac.univie.FirewallLogAnayzer.Exceptions.LogIdNotFoundException;
import at.ac.univie.FirewallLogAnayzer.Input.IInputHandler;
import at.ac.univie.FirewallLogAnayzer.Input.InputHandler;
import at.ac.univie.FirewallLogAnayzer.Processing.AnalyzerDos;
import at.ac.univie.FirewallLogAnayzer.Processing.CompositionAnalysing;
import at.ac.univie.FirewallLogAnayzer.Processing.IProcessingAnalyseGenerel;
import at.ac.univie.FirewallLogAnayzer.Processing.IProcessingAnalyseThreats;
import at.ac.univie.FirewallLogAnayzer.Processing.ProcessingAnalyseThreats;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.TemporairProcessing;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByDescriptionLogLine;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByExplanation;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLocationCity;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLocationCountry;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLogLineCode;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByMinutes;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByProtocol;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupBySrcIP;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByrecommendedAction;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.IGroupByFactory;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

       IInputHandler inputHandler = new InputHandler();
        // /Users/josefweber/Desktop/SyslogCatchAll-2017-03-14.txt
        // C:\Users\Lezard\Desktop\SyslogCatchAll-2017-03-14.txt
        try {
		inputHandler.loadeFirewallLog("C:\\Users\\Lezard\\Desktop\\activeFWLogs", LogTypeSingelton.getInstance().getSupportedLogTypeList().get(0));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (LogIdNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	
	//tempWeberPrositure();
        
	tempZilaPrositure();
	

    }

	private static void tempZilaPrositure() {
		
		IProcessingAnalyseThreats threadAnalysing = new ProcessingAnalyseThreats();
		HashMap<String, Double> threadScore = threadAnalysing.analyseForPortScanningOrFootPrinting();
		ArrayList<HashPairDoubleValue> sortedThreadScore = threadAnalysing.genereateSortAbleFromDoubleHashMap(threadScore);
		for(HashPairDoubleValue hpdv : sortedThreadScore){
			System.out.println(hpdv.toString());
		}
		threadAnalysing.analyseForDos();
		
		
		
		
		/*
		StringBuilder result = new StringBuilder("");

		WhoisClient whois = new WhoisClient();
		try {
			whois.connect(WhoisClient.DEFAULT_HOST);
			String whoisData1 = whois.query("=" + "75.65.32.99");
			result.append(whoisData1);
			whois.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sss"+ result.toString());	
		*/
	}

	private static void tempWeberPrositure() {
		//test method
        //TemporairProcessing.doSomething();
        //TemporairProcessing.testPortScan();


        // DOS
        IProcessingAnalyseGenerel da = new AnalyzerDos();
        DoSDataList ddl = da.analyseDos("icmp", 60);

        // Sort mpm
        //da.sortMessagePerMinute(ddl, "asc");

        // Sort country
        //HashMap<String, ArrayList<DoSData>> countrymap = da.messagesOfCountry(ddl);
        //HashMap<String, Integer> countryCount = da.sumMessagesPerCountry(countrymap, "asc");

        // Get single DosData
        /*
        DoSData ddsingle = da.getSingleIP(ddl, "187.182.134.133");
        if (ddsingle == null){
            System.out.println("null");
        } else {
            System.out.println("Example value: " + ddsingle.getMessages().get(0).getProtocol());
        }
        */

        DoSData td1 = ddl.getDataEdited().get(5);
        System.out.println(td1.getMptList().toString() + " ip=" + td1.getMessages().get(0).getSrcIP());


        ArrayList<DoSData> crits = da.analyzeMpt(ddl,10.0);
        System.out.println(crits.size());

        // Test Message Per Time

        for (int i = 0; i < crits.size(); i++) {

            System.out.println(crits.get(i).getMessages().get(0).getSrcIP());
        }





        /*
        DoSData ddTest = ddl.getDataEdited().get(8);
        ddTest.calcMsgPerSlot(20);
        System.out.println(ddTest.getMessages().size());
        System.out.println("----");
        */

		
	}


}
