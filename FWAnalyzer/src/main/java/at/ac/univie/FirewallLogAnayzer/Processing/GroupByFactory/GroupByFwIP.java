package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;

public class GroupByFwIP implements IGroupByFactory{

	private IBasicFunctions basicFunctions;
	
	public GroupByFwIP() {
		basicFunctions = new BasicFunctions();
	}
	
	@Override
	public String getKey(LogRow lr) {
		if (lr.getFwIP()==null){
			return basicFunctions.getNullString();
		}else{
			return lr.getFwIP();
		}		
	}

	@Override
	public String toString() {
		return "Firewall IP";
	}
	
	@Override
	public String getCaseDescription(LogRow lr) {
		// TODO Auto-generated method stub
		return "";
	}

}
