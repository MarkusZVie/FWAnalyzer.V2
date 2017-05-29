package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;

public class GroupByDestIP implements IGroupByFactory{

	private IBasicFunctions basicFunctions;
	
	public GroupByDestIP() {
		basicFunctions = new BasicFunctions();
	}
	
	@Override
	public String getKey(LogRow lr) {
		if (lr.getDestIP()==null){
			return basicFunctions.getNullString();
		}else{
			return lr.getDestIP();
		}
	}

	@Override
	public String toString() {
		return "Destination IP";
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
		return sb.toString();
	}

}
