package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;

public class GroupByPriority implements IGroupByFactory{

	private IBasicFunctions basicFunctions;
	
	public GroupByPriority() {
		basicFunctions = new BasicFunctions();
	}
	
	@Override
	public String getKey(LogRow lr) {
		if (lr.getPriority()==0){
			return basicFunctions.getNullString();
		}else{
			return lr.getPriority()+"";
		}
	}

	

}
