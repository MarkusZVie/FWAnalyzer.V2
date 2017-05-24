package at.ac.univie.FirewallLogAnayzer.Data;

import at.ac.univie.FirewallLogAnayzer.Processing.GroupByFactory.IGroupByFactory;

public class CompositionAnalysingSettings {
	private boolean dontCareByRecommendedActionNonRequired;
	private boolean dontCareByNoSrcIP;
	private CompositionSelection[] selectOnlyGroubedByKey;

	
	public CompositionAnalysingSettings() {
		dontCareByRecommendedActionNonRequired = false;
		dontCareByNoSrcIP = false;
		selectOnlyGroubedByKey = null;
	}
	

	public CompositionSelection[] getSelectOnlyGroubedByKey() {
		return selectOnlyGroubedByKey;
	}

	public void setSelectOnlyGroubedByKey(CompositionSelection[] selectOnlyGroubedByKey) {
		this.selectOnlyGroubedByKey = selectOnlyGroubedByKey;
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
