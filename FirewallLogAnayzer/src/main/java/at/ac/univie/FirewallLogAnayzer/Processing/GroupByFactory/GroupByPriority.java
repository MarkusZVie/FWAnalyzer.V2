package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.StaticFunctions;

public class GroupByPriority implements IGroupByFactory{

	@Override
	public String getKey(LogRow lr) {
		if (lr.getPriority()==0){
			return StaticFunctions.getNullString();
		}else{
			return lr.getPriority()+"";
		}
	}

	

}
