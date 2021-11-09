package ie.gmit.sw.ai;

public class CharacterStats {
	// member variables
	private double aggression;
	private int attackPower;
	private int stamina;
	
	// constructor
	public CharacterStats(double aggression, int attackPower, int stamina) {
		super();
		this.aggression = aggression;
		this.attackPower = attackPower;
		this.stamina = stamina;
	}

	// member methods
	public double getAggression() {
		return aggression;
	}

	public void setAggression(double aggression) {
		this.aggression = aggression;
	}

	public int getAttackPower() {
		return attackPower;
	}

	public void setAttackPower(int attackPower) {
		this.attackPower = attackPower;
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	@Override
	public String toString() {
		return "CharacterStats [aggression=" + aggression + ", attackPower=" + attackPower + ", stamina=" + stamina
				+ "]";
	}
}
