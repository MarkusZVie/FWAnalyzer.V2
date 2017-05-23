package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import java.util.ArrayList;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;

public interface IGroupByFactory {
	public String getKey(LogRow lr);
}
