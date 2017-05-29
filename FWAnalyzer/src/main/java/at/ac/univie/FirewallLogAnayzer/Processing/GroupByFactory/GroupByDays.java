package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import java.text.SimpleDateFormat;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;

public class GroupByDays implements IGroupByFactory{
	

	private IBasicFunctions basicFunctions;
	
	public GroupByDays() {
		basicFunctions = new BasicFunctions();
	}

	@Override
	public String getKey(LogRow lr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (lr.getDateTime()==null){
			return basicFunctions.getNullString();
		}else{
			return sdf.format(lr.getDateTime());
		}
	}

	@Override
	public String toString() {
		return "Time Days";
	}

	@Override
	public String getCaseDescription(LogRow lr) {
		// TODO Auto-generated method stub
		return "";
	}
	

}
