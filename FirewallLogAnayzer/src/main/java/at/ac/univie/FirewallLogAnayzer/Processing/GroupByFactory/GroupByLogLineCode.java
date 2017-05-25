package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;

public class GroupByLogLineCode implements IGroupByFactory{

	private IBasicFunctions basicFunctions;
	
	public GroupByLogLineCode() {
		basicFunctions = new BasicFunctions();
	}
	
	@Override
	public String getKey(LogRow lr) {
		if (lr.getLogLineCode()==0){
			return basicFunctions.getNullString();
		}else{
			return lr.getLogLineCode()+"";
		}
	}

	@Override
	public String toString() {
		return "Log Line Code";
	}

}
