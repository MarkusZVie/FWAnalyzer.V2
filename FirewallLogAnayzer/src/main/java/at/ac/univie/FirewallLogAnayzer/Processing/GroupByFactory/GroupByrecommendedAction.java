package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;

public class GroupByrecommendedAction implements IGroupByFactory{

	private IBasicFunctions basicFunctions;
	
	public GroupByrecommendedAction() {
		basicFunctions = new BasicFunctions();
	}
	
	@Override
	public String getKey(LogRow lr) {
		if (lr.getRecommendedAction()==null){
			return basicFunctions.getNullString();
		}else{
			return lr.getRecommendedAction();
		}	
	}

	

}
