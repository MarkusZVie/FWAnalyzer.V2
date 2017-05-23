package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;

public class GroupByLocationCity implements IGroupByFactory{

	@Override
	public String getKey(LogRow lr) {
		return lr.getLocation().getCityName();
	}

	

}
