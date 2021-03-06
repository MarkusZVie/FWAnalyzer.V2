package at.ac.univie.FirewallLogAnayzer.Data;

public class Report {
	private int id;
	private CompositionCompositionLogRow indicater;
	private String typeOfAttac;
	private String description;
	private String[] involvedLogLineCodes;
	private String explanation;
	
	
	public Report(CompositionCompositionLogRow indicater, String typeOfAttac, String description, String[] involvedLogLineCodes, int id, String explanation) {
		super();
		this.indicater = indicater;
		this.typeOfAttac = typeOfAttac;
		this.description = description;
		this.involvedLogLineCodes = involvedLogLineCodes;
		this.id=id;
		this.explanation = explanation;
	}
	
	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CompositionCompositionLogRow getIndicater() {
		return indicater;
	}
	public void setIndicater(CompositionCompositionLogRow indicater) {
		this.indicater = indicater;
	}
	public String getTypeOfAttac() {
		return typeOfAttac;
	}
	public void setTypeOfAttac(String typeOfAttac) {
		this.typeOfAttac = typeOfAttac;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getInvolvedLogLineCodes() {
		return involvedLogLineCodes;
	}
	public void setInvolvedLogLineCodes(String[] involvedLogLineCodes) {
		this.involvedLogLineCodes = involvedLogLineCodes;
	}

	public String getReportText() {
		StringBuilder sb = new StringBuilder();
		sb.append(typeOfAttac);
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(description);		
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(explanation);
		return sb.toString();
	}
	
}
