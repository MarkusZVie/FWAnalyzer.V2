package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;

public class GroupByLogLine implements IGroupByFactory{

	private IBasicFunctions basicFunctions;
	
	public GroupByLogLine() {
		basicFunctions = new BasicFunctions();
	}
	
	@Override
	public String getKey(LogRow lr) {
		if (lr.getLogLine()==null){
			return basicFunctions.getNullString();
		}else{
			return lr.getLogLine();
		}
	}

	@Override
	public String toString() {
		return "Log Line";
	}

	@Override
	public String getCaseDescription(LogRow lr) {
		// TODO Auto-generated method stub
		return "";
	}
	
}
