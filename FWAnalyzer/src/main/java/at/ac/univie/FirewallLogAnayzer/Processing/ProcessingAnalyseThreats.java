package at.ac.univie.FirewallLogAnayzer.Processing;

import java.io.FileNotFoundException;
import java.nio.channels.spi.AbstractSelectionKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import at.ac.univie.FirewallLogAnayzer.Data.CompositionAnalysingSettings;
import at.ac.univie.FirewallLogAnayzer.Data.CompositionCompositionLogRow;
import at.ac.univie.FirewallLogAnayzer.Data.CompositionSelection;
import at.ac.univie.FirewallLogAnayzer.Data.DoSReport;
import at.ac.univie.FirewallLogAnayzer.Data.HashPairDoubleValue;
import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRows;
import at.ac.univie.FirewallLogAnayzer.Data.Report;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByDescriptionLogLine;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByExplanation;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByHours;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLocationCity;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLocationCountry;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByLogLineCode;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByMinutes;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByProtocol;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupBySrcIP;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.GroupByrecommendedAction;
import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.IGroupByFactory;

public class ProcessingAnalyseThreats implements IProcessingAnalyseThreats{

	private ArrayList<LogRow> allLogRows;
	private ICompositionAnalysing compositionAnalysing;
	private IBasicFunctions basicFunctions;
	
	
	public ProcessingAnalyseThreats() {
		allLogRows = LogRows.getInstance().getLogRows();
		compositionAnalysing = new CompositionAnalysing();		
		basicFunctions = new BasicFunctions();
	}

	@Override
	public Report analyseForPortScanningOrFootPrinting() {
		//define Grouping desiction
		IGroupByFactory[] subGroups = {	new GroupByExplanation(), 
										new GroupByrecommendedAction(), 
										new GroupByProtocol() ,
										new GroupByLocationCountry(), 
										new GroupByLocationCity(), 
										new GroupBySrcIP(), 
										new GroupByHours()};
		
		//generate completeComposition
		CompositionAnalysingSettings generellSettings = new CompositionAnalysingSettings();
		CompositionSelection[] generellCompostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "106023"),
				new CompositionSelection(new GroupByLogLineCode(), "733101")
		};
		generellSettings.setSelectOnlyGroubedByKey(generellCompostionSelection);
		ArrayList<LogRow> generellFilterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, generellSettings);
		CompositionCompositionLogRow generellcclr = compositionAnalysing.groupByLogLine(generellFilterdLogRowsBySetting, new GroupByDescriptionLogLine());
		generellcclr.makeSubComposition(subGroups);
		
		String[] involvedLogLineCodes = {"106021","733101"};
		String analyseName="Scanning and Foot-Printing";
		
		String explanation="";
		try {
			explanation = basicFunctions.readeFile("GUITextFiles\\PortScanningOrFootPrinting.Explanation.txt")[0];
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String description="Searching for open Ports and other Weaknesses";
		Report report = new Report(generellcclr, analyseName, description, involvedLogLineCodes,12,explanation);
		return report;		
	
		
		
		
		
		
	}
	
	@Override
	public ArrayList<HashPairDoubleValue> genereateSortAbleFromDoubleHashMap(HashMap<String, Double> srcMap) {
		ArrayList<HashPairDoubleValue> sortedSrcMap = new ArrayList<>();
		for(String key :srcMap.keySet()){
			sortedSrcMap.add(new HashPairDoubleValue(key, srcMap.get(key)));
		}
		Collections.sort(sortedSrcMap);
		return sortedSrcMap;
	}
	

	@Override
	public void printStaticCompostionTree() {
		
		//create Settings
		CompositionAnalysingSettings settings = new CompositionAnalysingSettings();
		settings.setDontCareByRecommendedActionNonRequired(true);
		settings.setDontCareByNoSrcIP(true);
		
		//filter Logs by Settings
		ArrayList<LogRow> filterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, settings);
				
		//create Basic Compostion by one GroupBy
		CompositionCompositionLogRow cclr = compositionAnalysing.groupByLogLine(filterdLogRowsBySetting, new GroupByDescriptionLogLine());
		
		//Build Tree with custom GroupBys
		IGroupByFactory[] subGroups = {new GroupByExplanation(), new GroupByrecommendedAction(), new GroupByProtocol() ,new GroupByLocationCountry(), new GroupByLocationCity(), new GroupBySrcIP(), new GroupByMinutes()};
		cclr.makeSubComposition(subGroups);
		
		//print
		compositionAnalysing.printCCLogRow(cclr);
		
	}

	@Override
	public DoSReport analyseForDos() {
		//define Grouping desiction
		IGroupByFactory[] subGroups = {	new GroupByExplanation(), 
										new GroupByrecommendedAction(), 
										new GroupByProtocol() ,
										new GroupByLocationCountry(), 
										new GroupByLocationCity(), 
										new GroupBySrcIP(), 
										new GroupByHours()};
		
		//generate completeComposition
		CompositionAnalysingSettings generellSettings = new CompositionAnalysingSettings();
		CompositionSelection[] generellCompostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "106101"), //noIP, ACL LogdenyFlows has reach the limit
				new CompositionSelection(new GroupByLogLineCode(), "210011"), //noIP, Connection limit exceeded
				new CompositionSelection(new GroupByLogLineCode(), "402128"), //noIP, persists -> Dos
				new CompositionSelection(new GroupByLogLineCode(), "404102"), //noIP, Exceeded embryonic limit
				new CompositionSelection(new GroupByLogLineCode(), "407002"), //noIP, Embryonic limit
				new CompositionSelection(new GroupByLogLineCode(), "733100"), //noIP, Object drop rate rate_ID exceeded
				new CompositionSelection(new GroupByLogLineCode(), "109017"), //if persists from IP DoS Possible (proxy-Limit)
				new CompositionSelection(new GroupByLogLineCode(), "209003"), //if persists from IP DoS Possible (Fragment Database Limit)
				new CompositionSelection(new GroupByLogLineCode(), "400033"), //UDP Chargen DoS attack
				new CompositionSelection(new GroupByLogLineCode(), "210011") 
		};
		generellSettings.setSelectOnlyGroubedByKey(generellCompostionSelection);
		ArrayList<LogRow> generellFilterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, generellSettings);
		CompositionCompositionLogRow generellcclr = compositionAnalysing.groupByLogLine(generellFilterdLogRowsBySetting, new GroupByDescriptionLogLine());
		generellcclr.makeSubComposition(subGroups);
		
		
		//Generate Composition by DosIndicater with no IP
		CompositionAnalysingSettings doSNoIPSettings = new CompositionAnalysingSettings();
		CompositionSelection[] doSNoIPCompostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "106101"), //noIP, ACL LogdenyFlows has reach the limit
				new CompositionSelection(new GroupByLogLineCode(), "210011"), //noIP, Connection limit exceeded
				new CompositionSelection(new GroupByLogLineCode(), "402128"), //noIP, persists -> Dos
				new CompositionSelection(new GroupByLogLineCode(), "404102"), //noIP, Exceeded embryonic limit
				new CompositionSelection(new GroupByLogLineCode(), "407002"), //noIP, Embryonic limit
				new CompositionSelection(new GroupByLogLineCode(), "733100"), //noIP, Object drop rate rate_ID exceeded
				new CompositionSelection(new GroupByLogLineCode(), "210011") 
		};
		doSNoIPSettings.setSelectOnlyGroubedByKey(doSNoIPCompostionSelection);
		ArrayList<LogRow> doSNoIPFilterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, doSNoIPSettings);
		CompositionCompositionLogRow doSNoIPcclr = compositionAnalysing.groupByLogLine(doSNoIPFilterdLogRowsBySetting, new GroupByDescriptionLogLine());
		doSNoIPcclr.makeSubComposition(subGroups);
		
		//Generate Composition by DosIndicater with IP
		CompositionAnalysingSettings doSIPSettings = new CompositionAnalysingSettings();
		CompositionSelection[] doSIPCompostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "109017"), //if persists from IP DoS Possible (proxy-Limit)
				new CompositionSelection(new GroupByLogLineCode(), "209003"), //if persists from IP DoS Possible (Fragment Database Limit)
				new CompositionSelection(new GroupByLogLineCode(), "400033") //UDP Chargen DoS attack
		}; 
		doSIPSettings.setSelectOnlyGroubedByKey(doSIPCompostionSelection);
		
		ArrayList<LogRow> doSIPFilterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, doSIPSettings);
		CompositionCompositionLogRow doSIPcclr = compositionAnalysing.groupByLogLine(doSIPFilterdLogRowsBySetting, new GroupByDescriptionLogLine());
		doSIPcclr.makeSubComposition(subGroups);
		
		String[] involvedLogLineCodes = {"106101","210011","402128","404102","407002","733100","109017","209003","400033","210011"};
		
		String analyseName="(Distributed) Denial of Service (DoS) attack";
		String description="The denial-of-service attack (DoS attack) is a cyber-attack where the perpetrator seeks to make a machine "
				+ "or network resource unavailable to its intended users by temporarily or indefinitely disrupting services of "
				+ "a host connected to the Internet";
		String explanation="";
		try {
			explanation = basicFunctions.readeFile("GUITextFiles\\DoS.Explanation.txt")[0];
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DoSReport report = new DoSReport(generellcclr, analyseName, description, involvedLogLineCodes,1, doSNoIPcclr, doSIPcclr,explanation);
		return report;
		
	}


	@Override
	public void printCompostionTree(CompositionAnalysingSettings settings) {
		//filter Logs by Settings
		ArrayList<LogRow> filterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, settings);
				
		//create Basic Compostion by one GroupBy
		CompositionCompositionLogRow cclr = compositionAnalysing.groupByLogLine(filterdLogRowsBySetting, new GroupByDescriptionLogLine());
		
		//Build Tree with custom GroupBys
		IGroupByFactory[] subGroups = {new GroupByExplanation(), new GroupByrecommendedAction(), new GroupByProtocol() ,new GroupByLocationCountry(), new GroupByLocationCity(), new GroupBySrcIP(), new GroupByMinutes()};
		cclr.makeSubComposition(subGroups);
		
		//print
		compositionAnalysing.printCCLogRow(cclr);
		
	}

	@Override
	public Report analyseIPspoofedAttack() {
		//define Grouping desiction
		IGroupByFactory[] subGroups = {	new GroupByExplanation(), 
										new GroupByrecommendedAction(), 
										new GroupByProtocol() ,
										new GroupByLocationCountry(), 
										new GroupByLocationCity(), 
										new GroupBySrcIP(), 
										new GroupByHours()};
		
		//generate completeComposition
		CompositionAnalysingSettings generellSettings = new CompositionAnalysingSettings();
		CompositionSelection[] generellCompostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "106017"), //IP source address equal to the IP destination (indicates)
				new CompositionSelection(new GroupByLogLineCode(), "210011"), //possible DOS attack, in which case the source of the traffic could likely be a spoofed IP address
				new CompositionSelection(new GroupByLogLineCode(), "403109"), //The module received a spoofed PPTP packet
				new CompositionSelection(new GroupByLogLineCode(), "106021"), //An attack is in progress. Someone is attempting to spoof an IP address
				new CompositionSelection(new GroupByLogLineCode(), "713256"), //Sending spoofed ISAKMP Aggressive Mode message 2 due to receipt of unknown tunnel group
				new CompositionSelection(new GroupByLogLineCode(), "322001"), //MAC-spoofing attack or a misconfiguration
				new CompositionSelection(new GroupByLogLineCode(), "322002"), //ARP spoofing attacks in the network or an invalid configuration (IP-MAC binding). 
				new CompositionSelection(new GroupByLogLineCode(), "322003"), //This situation may be caused by either ARP spoofing attacks in the network or an invalid configuration (IP-MAC binding). 
				new CompositionSelection(new GroupByLogLineCode(), "400007"), //IP Fragment Attack 
				new CompositionSelection(new GroupByLogLineCode(), "400008"), //IP Impossible Packet 
				new CompositionSelection(new GroupByLogLineCode(), "400009"), //IP Fragments Overlap 
				new CompositionSelection(new GroupByLogLineCode(), "405002") //This traffic might be legitimate, or it might indicate that a spoofing attack is in progress. 
				
		};
		generellSettings.setSelectOnlyGroubedByKey(generellCompostionSelection);
		ArrayList<LogRow> generellFilterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, generellSettings);
		CompositionCompositionLogRow generellcclr = compositionAnalysing.groupByLogLine(generellFilterdLogRowsBySetting, new GroupByDescriptionLogLine());
		generellcclr.makeSubComposition(subGroups);
		
		String[] involvedLogLineCodes = {"106017","210011","403109","713256","106021","322001","322002","322003","400007","400008","400009","405002"};
		String explanation="";
		try {
			explanation = basicFunctions.readeFile("GUITextFiles\\IPspoofedAttack.Explanation.txt")[0];
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Report report = new Report(generellcclr, "IP-Spoofing", "Somebody tries to give himself up as someone else", involvedLogLineCodes,2,explanation);
		return report;
	}

	
	@Override
	public Report analyseConnectionHighChecking() {
		//define Grouping desiction
		IGroupByFactory[] subGroups = {	new GroupByExplanation(), 
										new GroupByrecommendedAction(), 
										new GroupByProtocol() ,
										new GroupByLocationCountry(), 
										new GroupByLocationCity(), 
										new GroupBySrcIP(), 
										new GroupByHours()};
		
		//generate completeComposition
		CompositionAnalysingSettings generellSettings = new CompositionAnalysingSettings();
		CompositionSelection[] generellCompostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "106022"), //An attacker also might be attempting to append packets from one connection to another as a way to break into the security appliance. 
				new CompositionSelection(new GroupByLogLineCode(), "313005"), //  ICMP error messages are not related to any session already established
				new CompositionSelection(new GroupByLogLineCode(), "337004"), //This message occurs when the computed authentication tag is not the same as the authentication tag in the packet. 
				new CompositionSelection(new GroupByLogLineCode(), "402114"), // It may also indicate incorrect packets sent by the IPSec peer,
				new CompositionSelection(new GroupByLogLineCode(), "402115"), //The peer is sending packets that do not match the negotiated security policy
				new CompositionSelection(new GroupByLogLineCode(), "402116"), //This message is displayed when a decapsulated IPSec packet does not match the negotiated identity.
				new CompositionSelection(new GroupByLogLineCode(), "402117"), //This message is displayed when the received packet matched the crypto map ACL, but it is not IPSec-encapsulated
				new CompositionSelection(new GroupByLogLineCode(), "402118"), //This message is displayed when a decapsulatd IPSec packet contains an IP fragment with an offset less than or equal to 128 bytes
				new CompositionSelection(new GroupByLogLineCode(), "402119"), //his message is displayed when an IPSec packet is received with an invalid sequence number
				new CompositionSelection(new GroupByLogLineCode(), "402120"), //This message is displayed when an IPSec packet is received and fails authentication. 
				new CompositionSelection(new GroupByLogLineCode(), "406002"), //A client issued an FTP port command and supplied an address other than the address used in the connection.
				new CompositionSelection(new GroupByLogLineCode(), "431001"), //Examine the dropped RTP packets to determine which field the RTP source is setting incorrectly.
				new CompositionSelection(new GroupByLogLineCode(), "431002"), //Also examine the source to verify that it is legitimate and not an attacker trying to misuse an opening in the security appliance. 
				new CompositionSelection(new GroupByLogLineCode(), "722001") // The request from the SSL VPN client (SVC) was invalid. 
				
		};
		generellSettings.setSelectOnlyGroubedByKey(generellCompostionSelection);
		ArrayList<LogRow> generellFilterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, generellSettings);
		CompositionCompositionLogRow generellcclr = compositionAnalysing.groupByLogLine(generellFilterdLogRowsBySetting, new GroupByDescriptionLogLine());
		generellcclr.makeSubComposition(subGroups);
		
		String[] involvedLogLineCodes = {"106022","313005","337004","402114","402115","402116","402117","402118","402119","402120","406002","431001","431002","722001"};
		String explanation="";
		try {
			explanation = basicFunctions.readeFile("GUITextFiles\\ConnectionHighChecking.Explanation.txt")[0];
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Report report = new Report(generellcclr, "Connection High Checking", "An attacker also might be attempting to append packets from one connection to another as a way to break into the security appliance", involvedLogLineCodes,3,explanation);
		return report;
	}

	@Override
	public Report analyseRoutingManipulation() {
		//define Grouping desiction
		IGroupByFactory[] subGroups = {	new GroupByExplanation(), 
										new GroupByrecommendedAction(), 
										new GroupByProtocol() ,
										new GroupByLocationCountry(), 
										new GroupByLocationCity(), 
										new GroupBySrcIP(), 
										new GroupByHours()};
		
		//generate completeComposition
		CompositionAnalysingSettings generellSettings = new CompositionAnalysingSettings();
		CompositionSelection[] generellCompostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "107001"), //received a RIP reply message with bad authentication
				new CompositionSelection(new GroupByLogLineCode(), "107002"), //The packet has passed authentication, if enabled, and bad data is in the packet.
				new CompositionSelection(new GroupByLogLineCode(), "320001"), //a device spoofs the peer IP address and attempts to intercept a VPN connection from the security appliance. 
				new CompositionSelection(new GroupByLogLineCode(), "402114"), // It may also indicate incorrect packets sent by the IPSec peer,
				new CompositionSelection(new GroupByLogLineCode(), "402115"), //The peer is sending packets that do not match the negotiated security policy
				new CompositionSelection(new GroupByLogLineCode(), "402116"), //This message is displayed when a decapsulated IPSec packet does not match the negotiated identity.
				new CompositionSelection(new GroupByLogLineCode(), "402117"), //This message is displayed when the received packet matched the crypto map ACL, but it is not IPSec-encapsulated
				new CompositionSelection(new GroupByLogLineCode(), "402118"), //This message is displayed when a decapsulatd IPSec packet contains an IP fragment with an offset less than or equal to 128 bytes
				new CompositionSelection(new GroupByLogLineCode(), "402119"), //his message is displayed when an IPSec packet is received with an invalid sequence number
				new CompositionSelection(new GroupByLogLineCode(), "402120"), //This message is displayed when an IPSec packet is received and fails authentication. 
				new CompositionSelection(new GroupByLogLineCode(), "405001"), //  This traffic might be legitimate, or it might indicate that an ARP poisoning attack is in progress.
				new CompositionSelection(new GroupByLogLineCode(), "410002") //A high rate of mismatched DNS identifiers might indicate an attack on the cache.
				
		};
		generellSettings.setSelectOnlyGroubedByKey(generellCompostionSelection);
		ArrayList<LogRow> generellFilterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, generellSettings);
		CompositionCompositionLogRow generellcclr = compositionAnalysing.groupByLogLine(generellFilterdLogRowsBySetting, new GroupByDescriptionLogLine());
		generellcclr.makeSubComposition(subGroups);
		
		String[] involvedLogLineCodes = {"107001","107002","320001","402114","402115","402116","402117","402118","402119","402120","405001","410002"};
		String explanation="";
		try {
			explanation = basicFunctions.readeFile("GUITextFiles\\RoutingManipulation.Explanation.txt")[0];
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Report report = new Report(generellcclr, "Routing Manipulation", "It can be producsed by an not well configruated Router, or indicater of an 'Man in the middel attack'", involvedLogLineCodes,4,explanation);
		return report;
	}

	@Override
	public Report analyseSynAttack() {
		//define Grouping desiction
		IGroupByFactory[] subGroups = {	new GroupByExplanation(), 
										new GroupByrecommendedAction(), 
										new GroupByProtocol() ,
										new GroupByLocationCountry(), 
										new GroupByLocationCity(), 
										new GroupBySrcIP(), 
										new GroupByHours()};
		
		//generate completeComposition
		CompositionAnalysingSettings generellSettings = new CompositionAnalysingSettings();
		CompositionSelection[] generellCompostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "201003"), //SYN attack, or by a very heavy load of legitimate traffic
				new CompositionSelection(new GroupByLogLineCode(), "201012"), // the limit is reached, any new connection request will be proxied by the security appliance to prevent a SYN flood attack
				new CompositionSelection(new GroupByLogLineCode(), "733100"), //The specified object in the syslog message has exceeded the specified burst threshold rate or average threshold rate.
				new CompositionSelection(new GroupByLogLineCode(), "733104"), //This message is generated when the system is under Syn flood attacks, if the average rate exceeds the configured threshold. 
				new CompositionSelection(new GroupByLogLineCode(), "733105") // This message is generated when the system is under Syn flood attacks, if the burst rate exceeds the configured threshold. 
				
		};
		generellSettings.setSelectOnlyGroubedByKey(generellCompostionSelection);
		ArrayList<LogRow> generellFilterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, generellSettings);
		CompositionCompositionLogRow generellcclr = compositionAnalysing.groupByLogLine(generellFilterdLogRowsBySetting, new GroupByDescriptionLogLine());
		generellcclr.makeSubComposition(subGroups);
		
		String[] involvedLogLineCodes = {"201003","201012","733100","733104","733105"};
		String explanation="";
		try {
			explanation = basicFunctions.readeFile("GUITextFiles\\SynAttack.Explanation.txt")[0];
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Report report = new Report(generellcclr, "Syn-Attack", "Subtype of DoS", involvedLogLineCodes,5,explanation);
		return report;
	}

	@Override
	public Report analyseWeakIndicaterOfAnAttack() {
		//define Grouping desiction
		IGroupByFactory[] subGroups = {	new GroupByExplanation(), 
										new GroupByrecommendedAction(), 
										new GroupByProtocol() ,
										new GroupByLocationCountry(), 
										new GroupByLocationCity(), 
										new GroupBySrcIP(), 
										new GroupByHours()};
		
		//generate completeComposition
		CompositionAnalysingSettings generellSettings = new CompositionAnalysingSettings();
		CompositionSelection[] generellCompostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "324001"), //There was an error processing the packet
				new CompositionSelection(new GroupByLogLineCode(), "324003"), // The response received does not have a matching request in the request queue and should not be processed further. 
				new CompositionSelection(new GroupByLogLineCode(), "324004"), //The packet being processed has a version other than the currently supported version, which is 0 or 1
				new CompositionSelection(new GroupByLogLineCode(), "324006") // The GSN sending the request has exceeded the maximum allowed tunnels created, so no tunnel will be created.					
		};
		generellSettings.setSelectOnlyGroubedByKey(generellCompostionSelection);
		ArrayList<LogRow> generellFilterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, generellSettings);
		CompositionCompositionLogRow generellcclr = compositionAnalysing.groupByLogLine(generellFilterdLogRowsBySetting, new GroupByDescriptionLogLine());
		generellcclr.makeSubComposition(subGroups);
		
		String[] involvedLogLineCodes = {"324001","324003","324004", "324006"};
		String explanation = "";
		try {
			explanation = basicFunctions.readeFile("GUITextFiles\\WeakIndicaterOfAnAttack.Explanation.txt")[0];
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Report report = new Report(generellcclr, "Weak Indicater of an Attack", "If those Indicater appiers frequently, it can be the side effect of an attack", involvedLogLineCodes,10,explanation);
		return report;
	}

	@Override
	public Report analyseICMPBasedAttaks() {
		//define Grouping desiction
		IGroupByFactory[] subGroups = {	new GroupByExplanation(), 
										new GroupByrecommendedAction(), 
										new GroupByProtocol() ,
										new GroupByLocationCountry(), 
										new GroupByLocationCity(), 
										new GroupBySrcIP(), 
										new GroupByHours()};
			
		//generate completeComposition
		CompositionAnalysingSettings generellSettings = new CompositionAnalysingSettings();
		CompositionSelection[] generellCompostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "400023"), //Fragmented ICMP Traffic 
				new CompositionSelection(new GroupByLogLineCode(), "400024"), //Large ICMP Traffic 
				new CompositionSelection(new GroupByLogLineCode(), "400025") //Ping of Death Attack 
					
		};
		generellSettings.setSelectOnlyGroubedByKey(generellCompostionSelection);
		ArrayList<LogRow> generellFilterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, generellSettings);
		CompositionCompositionLogRow generellcclr = compositionAnalysing.groupByLogLine(generellFilterdLogRowsBySetting, new GroupByDescriptionLogLine());
		generellcclr.makeSubComposition(subGroups);
		
		String[] involvedLogLineCodes = {"400023","400024","400025"};
		String explanation = "";
		try {
			explanation = basicFunctions.readeFile("GUITextFiles\\ICMPBasedAttaks.Explanation.txt")[0];
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Report report = new Report(generellcclr, "ICMP Based Attaks", "Container of Diffrent ICMP Based Attaks", involvedLogLineCodes,6,explanation);
		return report;
	}

	@Override
	public Report analyseTCPBasedAttacks() {
		//define Grouping desiction
		IGroupByFactory[] subGroups = {	new GroupByExplanation(), 
										new GroupByrecommendedAction(), 
										new GroupByProtocol() ,
										new GroupByLocationCountry(), 
										new GroupByLocationCity(), 
										new GroupBySrcIP(), 
										new GroupByHours()};
			
		//generate completeComposition
		CompositionAnalysingSettings generellSettings = new CompositionAnalysingSettings();
		CompositionSelection[] generellCompostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "400026"), //TCP NULL flags 
				new CompositionSelection(new GroupByLogLineCode(), "400027"), //TCP SYN+FIN flags 
				new CompositionSelection(new GroupByLogLineCode(), "400028"), //TCP FIN only flags 
				new CompositionSelection(new GroupByLogLineCode(), "710005"), // This message appears when the security appliance does not have a UDP server that services the UDP request. The message can also indicate a TCP packet that does not belong to any session on the security appliance.
				new CompositionSelection(new GroupByLogLineCode(), "710006") //This message appears when the security appliance does not have an IP server that services the IP protocol request; for example, the security appliance receives IP packets that are not TCP or UDP
					
		};
		generellSettings.setSelectOnlyGroubedByKey(generellCompostionSelection);
		ArrayList<LogRow> generellFilterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, generellSettings);
		CompositionCompositionLogRow generellcclr = compositionAnalysing.groupByLogLine(generellFilterdLogRowsBySetting, new GroupByDescriptionLogLine());
		generellcclr.makeSubComposition(subGroups);
		
		String[] involvedLogLineCodes = {"400026","400027","400028","710005","710006"};
		String explanation = "";
		try {
			explanation = basicFunctions.readeFile("GUITextFiles\\TCPBasedAttacks.Explanation.txt")[0];
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Report report = new Report(generellcclr, "TCP Based Attaks", "Container of Diffrent TCP Based Attaks", involvedLogLineCodes,7,explanation);
		return report;	}

	@Override
	public Report analyseUDPBasedAttacks() {
		//define Grouping desiction
		IGroupByFactory[] subGroups = {	new GroupByExplanation(), 
										new GroupByrecommendedAction(), 
										new GroupByProtocol() ,
										new GroupByLocationCountry(), 
										new GroupByLocationCity(), 
										new GroupBySrcIP(), 
										new GroupByHours()};
			
		//generate completeComposition
		CompositionAnalysingSettings generellSettings = new CompositionAnalysingSettings();
		CompositionSelection[] generellCompostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "400031"), //UDP Bomb attack 
				new CompositionSelection(new GroupByLogLineCode(), "400032"), //UDP Snork attack 
				new CompositionSelection(new GroupByLogLineCode(), "400033"), //UDP Chargen DoS attack 
					
		};
		generellSettings.setSelectOnlyGroubedByKey(generellCompostionSelection);
		ArrayList<LogRow> generellFilterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, generellSettings);
		CompositionCompositionLogRow generellcclr = compositionAnalysing.groupByLogLine(generellFilterdLogRowsBySetting, new GroupByDescriptionLogLine());
		generellcclr.makeSubComposition(subGroups);
		
		String[] involvedLogLineCodes = {"400031","400032","400033"};
		String explanation = "";
		try {
			explanation = basicFunctions.readeFile("GUITextFiles\\UDPBasedAttacks.Explanation.txt")[0];
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Report report = new Report(generellcclr, "UDP Based Attaks", "Container of Diffrent UDP Based Attaks", involvedLogLineCodes,8,explanation);
		return report;	}

	@Override
	public Report analyseOtherAttacks() {
		//define Grouping desiction
		IGroupByFactory[] subGroups = {	new GroupByExplanation(), 
										new GroupByrecommendedAction(), 
										new GroupByProtocol() ,
										new GroupByLocationCountry(), 
										new GroupByLocationCity(), 
										new GroupBySrcIP(), 
										new GroupByHours()};
			
		//generate completeComposition
		CompositionAnalysingSettings generellSettings = new CompositionAnalysingSettings();
		CompositionSelection[] generellCompostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "400041"), // Proxied RPC Request 
				new CompositionSelection(new GroupByLogLineCode(), "400050"), // statd Buffer Overflow 
				new CompositionSelection(new GroupByLogLineCode(), "412002"), //This message is generated when the bridge table is full and an attempt is made to add one more entry
				new CompositionSelection(new GroupByLogLineCode(), "605004"), // This message appears after an incorrect login attempt or a failed login to the security appliance.
				new CompositionSelection(new GroupByLogLineCode(), "733102")  //To investigate whether the shunned host is an actual attacker, 	
					
		};
		generellSettings.setSelectOnlyGroubedByKey(generellCompostionSelection);
		ArrayList<LogRow> generellFilterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, generellSettings);
		CompositionCompositionLogRow generellcclr = compositionAnalysing.groupByLogLine(generellFilterdLogRowsBySetting, new GroupByDescriptionLogLine());
		generellcclr.makeSubComposition(subGroups);
		
		String[] involvedLogLineCodes = {"400041","400050","412002","605004,733102"};
		String explanation = "";
		try {
			explanation = basicFunctions.readeFile("GUITextFiles\\OtherAttacks.Explanation.txt")[0];
		} catch (FileNotFoundException e) {
			try {
				explanation = basicFunctions.readeFile("/GUITextFiles/OtherAttacks.Explanation.txt")[0];
			} catch (FileNotFoundException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			e.printStackTrace();
		}
		Report report = new Report(generellcclr, "Other Attacks", "Container of Diffrent Attaks", involvedLogLineCodes,11,explanation);
		return report;	
		
	}

	@Override
	public Report analyseBruteForce() {
		//define Grouping desiction
		IGroupByFactory[] subGroups = {	new GroupByExplanation(), 
										new GroupByrecommendedAction(), 
										new GroupByProtocol() ,
										new GroupByLocationCountry(), 
										new GroupByLocationCity(), 
										new GroupBySrcIP(), 
										new GroupByHours()};
			
		//generate completeComposition
		CompositionAnalysingSettings generellSettings = new CompositionAnalysingSettings();
		CompositionSelection[] generellCompostionSelection = {
				new CompositionSelection(new GroupByLogLineCode(), "605004"), // This message appears after an incorrect login attempt or a failed login to the security appliance.
				new CompositionSelection(new GroupByLogLineCode(), "710003"), // This message is displayed when the security appliance denies an attempt to connect to the interface service. 
					
		};
		generellSettings.setSelectOnlyGroubedByKey(generellCompostionSelection);
		ArrayList<LogRow> generellFilterdLogRowsBySetting = compositionAnalysing.eliminateUnnecessaryRowsBySetting(allLogRows, generellSettings);
		CompositionCompositionLogRow generellcclr = compositionAnalysing.groupByLogLine(generellFilterdLogRowsBySetting, new GroupByDescriptionLogLine());
		generellcclr.makeSubComposition(subGroups);
		
		String[] involvedLogLineCodes = {"605004","710003"};
		String explanation = "";
		try {
			explanation = basicFunctions.readeFile("GUITextFiles\\BruteForce.Explanation.txt")[0];
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Report report = new Report(generellcclr, "Brute Force", "No specific Hacke, more Try and Error", involvedLogLineCodes,9,explanation);
		return report;	
	}





	

}
