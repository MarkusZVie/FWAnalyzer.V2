package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;

public class GroupByProtocol implements IGroupByFactory{

	private IBasicFunctions basicFunctions;
	
	public GroupByProtocol() {
		basicFunctions = new BasicFunctions();
	}
	
	@Override
	public String getKey(LogRow lr) {
		if (lr.getProtocol()==null){
			return basicFunctions.getNullString();
		}else{
			return lr.getProtocol();
		}
	}

	@Override
	public String toString() {
		return "Protocol";
	}
	
	@Override
	public String getCaseDescription(LogRow lr) {
		// TODO Auto-generated method stub
		return "";
	}

}
