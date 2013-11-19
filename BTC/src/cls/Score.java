package cls;

public class Score {
	
	private double timePlayed;
	private int flightsSuccessful;
	
	public Score(){
		this.timePlayed = 0.00;
		this.flightsSuccessful = 0;
	}

	public double getTimePlayed() {
		return timePlayed;
	}

	public int getFlightsSuccessful() {
		return flightsSuccessful;
	}

	public void setTimePlayed(double timePlayed) {
		this.timePlayed = timePlayed;
	}

	public void setFlightsSuccessful(int flightsSuccessful) {
		this.flightsSuccessful = flightsSuccessful;
	}
	
	public void incFlightsSuccessful(){
		this.flightsSuccessful ++;
	}

}
