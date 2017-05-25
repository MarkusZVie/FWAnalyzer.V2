package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;

public class GroupBySrcPort implements IGroupByFactory{

	private IBasicFunctions basicFunctions;
	
	public GroupBySrcPort() {
		basicFunctions = new BasicFunctions();
	}
	
	@Override
	public String getKey(LogRow lr) {
		if (lr.getSrcPort()==null){
			return basicFunctions.getNullString();
		}else{
			return lr.getSrcPort();
		}	
	}

	@Override
	public String toString() {
		return "Source Port";
	}

}
