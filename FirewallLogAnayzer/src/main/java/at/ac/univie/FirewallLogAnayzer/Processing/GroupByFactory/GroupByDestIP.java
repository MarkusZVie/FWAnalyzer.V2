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

}
