package at.ac.univie.FirewallLogAnayzer.Data;

public class DoSReport extends Report{
	private CompositionCompositionLogRow noIPIndicater;
	private CompositionCompositionLogRow IPIndicater;
	
	public DoSReport(CompositionCompositionLogRow indicater, String typeOfAttac, String description,
			String[] involvedLogLineCodes,CompositionCompositionLogRow noIPIndicater,CompositionCompositionLogRow IPIndicater) {
		super(indicater, typeOfAttac, description, involvedLogLineCodes);
		this.IPIndicater = IPIndicater;
		this.noIPIndicater = noIPIndicater;
	}

	public void setDoSReport(CompositionCompositionLogRow noIPIndicater, CompositionCompositionLogRow IPIndicater) {
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
