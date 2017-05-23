package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.StaticFunctions;

public class GroupByFwIP implements IGroupByFactory{

	@Override
	public String getKey(LogRow lr) {
		if (lr.getFwIP()==null){
			return StaticFunctions.getNullString();
		}else{
			return lr.getFwIP();
		}		
	}

	

}
