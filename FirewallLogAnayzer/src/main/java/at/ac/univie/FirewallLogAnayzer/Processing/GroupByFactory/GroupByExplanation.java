package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;

public class GroupByExplanation implements IGroupByFactory{

	private IBasicFunctions basicFunctions;
	
	public GroupByExplanation() {
		basicFunctions = new BasicFunctions();
	}
	
	@Override
	public String getKey(LogRow lr) {
		if (lr.getExplanation()==null){
			return basicFunctions.getNullString();
		}else{
			return lr.getExplanation();
		}
	}

	@Override
	public String toString() {
		return "Explanation";
	}
	

}
