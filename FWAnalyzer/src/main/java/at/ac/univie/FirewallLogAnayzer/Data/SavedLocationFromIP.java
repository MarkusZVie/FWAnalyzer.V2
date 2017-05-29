package at.ac.univie.FirewallLogAnayzer.Data;

import java.util.HashMap;

import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;

public class SavedLocationFromIP {
	private HashMap<String,IpLocation> savedLocationFromIp;
	private static SavedLocationFromIP instance =null;
	private IBasicFunctions basicFunctions;
	
	
	private SavedLocationFromIP(){
		savedLocationFromIp = new HashMap<String,IpLocation>(); 		
		basicFunctions = new BasicFunctions();
	}
	public static SavedLocationFromIP getInstance(){
		if(instance == null){
			instance = new SavedLocationFromIP();
		}
		return instance;
	}
	
	public IpLocation checkIpLocation(String ip){
		if(savedLocationFromIp.containsKey(ip)){
			return savedLocationFromIp.get(ip);
		}else{
			IpLocation ipl = basicFunctions.findeLocation(ip);
			savedLocationFromIp.put(ip, ipl);
			return ipl;
		}
	}
}
