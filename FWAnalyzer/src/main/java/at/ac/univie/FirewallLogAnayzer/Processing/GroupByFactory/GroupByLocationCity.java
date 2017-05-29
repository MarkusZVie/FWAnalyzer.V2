package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;

public class GroupByLocationCity implements IGroupByFactory{

	private IBasicFunctions basicFunctions;
	
	public GroupByLocationCity() {
		basicFunctions = new BasicFunctions();
	}
	
	@Override
	public String getKey(LogRow lr) {
		if (lr.getLocation()==null){
			return basicFunctions.getNullString();
		}else{
			return lr.getLocation().getCityName();
		}
	}

	@Override
	public String toString() {
		return "Location City";
	}

	@Override
	public String getCaseDescription(LogRow lr) {
		return "";
	}
}
