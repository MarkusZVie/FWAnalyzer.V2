package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import java.io.IOException;

import org.apache.commons.net.whois.WhoisClient;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;

public class GroupBySrcIP implements IGroupByFactory{

	private IBasicFunctions basicFunctions;
	
	public GroupBySrcIP() {
		basicFunctions = new BasicFunctions();
	}
	
	@Override
	public String getKey(LogRow lr) {
		if (lr.getSrcIP()==null){
			return basicFunctions.getNullString();
		}else{
			return lr.getSrcIP();
		}	
	}

	@Override
	public String toString() {
		return "Source IP";
	}
	
	@Override
	public String getCaseDescription(LogRow lr) {
		StringBuilder sb = new StringBuilder();
		
		if(lr.getLocation()!=null){
			sb.append("Exakt Location of IP");
			sb.append(System.lineSeparator());
			sb.append("Country    : " + lr.getLocation().getCountryName() + " (" + lr.getLocation().getCountryIsoCode()+")");
			sb.append(System.lineSeparator());
			sb.append("City       : " + lr.getLocation().getCityName());
			sb.append(System.lineSeparator());
			sb.append("PostCode   : " + lr.getLocation().getPostCode());
			sb.append(System.lineSeparator());
			sb.append("Latitude   : " + lr.getLocation().getLatitude());
			sb.append(System.lineSeparator());
			sb.append("Longitude  : " + lr.getLocation().getLongitude());
		}else{
			sb.append("Exakt Location of IP");
			sb.append(System.lineSeparator());
			sb.append("No Location found");
		}
		
		StringBuilder whoIsResult = new StringBuilder();
		WhoisClient whois = new WhoisClient();
		boolean noError=true;
		try {
			//preString
			whoIsResult.append(System.lineSeparator());
			whoIsResult.append(System.lineSeparator());
			
			whois.connect(WhoisClient.DEFAULT_HOST);
			String whoisData1 = whois.query("=" + lr.getSrcIP());
			whoIsResult.append(whoisData1);
			whois.disconnect();
		} catch (IOException e) {
			noError=false;
		}
		if(noError){
			sb.append(whoIsResult.toString());
		}
		return sb.toString();
	}

}
