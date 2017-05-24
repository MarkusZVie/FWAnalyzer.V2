package at.ac.univie.FirewallLogAnayzer.Data;

public class HashPairDoubleValue implements Comparable<HashPairDoubleValue>{
	private String key;
	private Double value;
	
	public HashPairDoubleValue() {
	}
	
	@Override
	public String toString() {
		
		return "Key: " + key + " " + value;
	}

	public HashPairDoubleValue(String key, Double value) {
		super();
		this.key = key;
		this.value = value;
	}


	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public Double getValue() {
		return value;
	}


	public void setValue(Double value) {
		this.value = value;
	}


	@Override
	public int compareTo(HashPairDoubleValue d) {
		return value.compareTo(d.getValue());
	}
	
}
