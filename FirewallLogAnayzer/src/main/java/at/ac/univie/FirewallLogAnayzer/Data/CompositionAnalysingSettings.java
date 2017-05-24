package at.ac.univie.FirewallLogAnayzer.Data;

import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.IGroupByFactory;

public class CompositionAnalysingSettings {
	private boolean dontCareByRecommendedActionNonRequired;
	private boolean dontCareByNoSrcIP;
	private IGroupByFactory selectOnlyGroubedByKey;
	private String key;
	
	public void setGroupByedOnly (IGroupByFactory gb, String key){
		selectOnlyGroubedByKey = gb;
		this.key = key;
	}
	
	public IGroupByFactory getSelectOnlyGroubedByKey() {
		return selectOnlyGroubedByKey;
	}

	public String getKey() {
		return key;
	}

	public CompositionAnalysingSettings() {
		dontCareByRecommendedActionNonRequired = false;
		dontCareByNoSrcIP = false;
	}

	public boolean isDontCareByNoSrcIP() {
		return dontCareByNoSrcIP;
	}

	public void setDontCareByNoSrcIP(boolean dontCareByNoSrcIP) {
		this.dontCareByNoSrcIP = dontCareByNoSrcIP;
	}

	public boolean isDontCareByRecommendedActionNonRequired() {
		return dontCareByRecommendedActionNonRequired;
	}

	public void setDontCareByRecommendedActionNonRequired(boolean dontCareByRecommendedActionNonRequired) {
		this.dontCareByRecommendedActionNonRequired = dontCareByRecommendedActionNonRequired;
	}
	
	
}
