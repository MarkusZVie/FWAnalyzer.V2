package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;

public class GroupByDescriptionLogLine implements IGroupByFactory{
	private IBasicFunctions basicFunctions;
	
	public GroupByDescriptionLogLine() {
		basicFunctions = new BasicFunctions();
	}
	
	@Override
	public String getKey(LogRow lr) {
		if (lr.getDescriptionLogLine()==null){
			return basicFunctions.getNullString();
		}else{
			return lr.getDescriptionLogLine();
		}
	}

	@Override
	public String toString() {
		return "Description";
	}
	
	
	@Override
	public String getCaseDescription(LogRow lr) {
		// TODO Auto-generated method stub
		return "";
	}

}
