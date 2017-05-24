package at.ac.univie.FirewallLogAnayzer.Data;

public class DoSReport {
	private CompositionCompositionLogRow noIPIndicater;
	private CompositionCompositionLogRow IPIndicater;
	
	
	public DoSReport(CompositionCompositionLogRow noIPIndicater, CompositionCompositionLogRow IPIndicater) {
		this.noIPIndicater = noIPIndicater;
		this.IPIndicater = IPIndicater;
	}


	public CompositionCompositionLogRow getNoIPIndicater() {
		return noIPIndicater;
	}


	public void setNoIPIndicater(CompositionCompositionLogRow noIPIndicater) {
		this.noIPIndicater = noIPIndicater;
	}


	public CompositionCompositionLogRow getIPIndicater() {
		return IPIndicater;
	}


	public void setIPIndicater(CompositionCompositionLogRow iPIndicater) {
		IPIndicater = iPIndicater;
	}
	
	
	
	
}
