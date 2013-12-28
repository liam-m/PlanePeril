package classes;

public class Score {
	
	private int _flightsSuccessful;
	private double _timePlayed;
	private double _timeManual;
	private double _timeSeparationViolated;
	private int _gameOvers;
	
	public Score(){
		_timePlayed = 0;
		_timeManual = 0;
		_timeSeparationViolated = 0;
		_flightsSuccessful = 0;
		_gameOvers = 0;
	}

	public double timePlayed() {
		return _timePlayed;
	}

	public int flightsSuccessful() {
		return _flightsSuccessful;
	}
	
	public double manualTime()
	{
		return _timeManual;
	}
	
	public double timeViolated()
	{
		return _timeSeparationViolated;
	}
	
	public int gameOvers()
	{
		return _gameOvers;
	}

	public void addTime(double dt) {
		_timePlayed += dt;
	}
	
	public void addTimeManual(double dt) {
		_timeManual += dt;
	}
	
	public void addTimeViolated(double dt) {
		_timeSeparationViolated += dt;
	}

	public void addFlight() {
		_flightsSuccessful ++;
	}
	
	public void addGameOver() {
		_gameOvers ++;
	}
	
	public int calculate() {
		int score = 0;
		score += (int)(_timePlayed);
		score += (100 * _flightsSuccessful);
		score -= (int)(_timeSeparationViolated);
		score -= (int)(_timeManual);
		score -= (1000 * _gameOvers);
		return score;
	}

}