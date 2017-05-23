package at.ac.univie.FirewallLogAnayzer.Data;

public class CompositionAnalysingSettings {
	private boolean dontCareByRecommendedActionNonRequired;
	private boolean dontCareByNoSrcIP;
	
	
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
