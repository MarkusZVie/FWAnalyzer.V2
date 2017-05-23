package at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory;

import java.text.SimpleDateFormat;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Processing.StaticFunctions;

public class GroupByMinutes implements IGroupByFactory{

	@Override
	public String getKey(LogRow lr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (lr.getDateTime()==null){
			return StaticFunctions.getNullString();
		}else{
			return sdf.format(lr.getDateTime());
		}
	}

	

}
