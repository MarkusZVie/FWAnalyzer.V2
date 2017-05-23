package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;

public class GroupBySrcIP implements IGroupByFactory{

	@Override
	public String getKey(LogRow lr) {
		return lr.getSrcIP();
	}

	

}
