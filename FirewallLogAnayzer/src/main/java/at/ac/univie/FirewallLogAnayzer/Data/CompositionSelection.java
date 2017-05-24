package at.ac.univie.FirewallLogAnayzer.Data;

import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.IGroupByFactory;

public class CompositionSelection {
	private IGroupByFactory gbf;
	private String selectetKey;
	
	public CompositionSelection(IGroupByFactory gbf, String selectetKey) {
		super();
		this.gbf = gbf;
		this.selectetKey = selectetKey;
	}
	
	public IGroupByFactory getGbf() {
		return gbf;
	}
	public void setGbf(IGroupByFactory gbf) {
		this.gbf = gbf;
	}
	public String getSelectetKey() {
		return selectetKey;
	}
	public void setSelectetKey(String selectetKey) {
		this.selectetKey = selectetKey;
	}
	
	
}
