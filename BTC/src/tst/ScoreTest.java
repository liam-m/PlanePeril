package tst;

import static org.junit.Assert.*;

import org.junit.Test;

import cls.Score;

public class ScoreTest {

	
	
	@Test // Tests the get time played function
	public void testGetTime(){
		Score testScore = new Score();
		assertTrue("Time played = 0", 0 == testScore.timePlayed());
	}
	
	@Test // Tests the get time played function
	public void testGetFlightsSuccessful(){
		Score testScore = new Score();
		assertTrue("Total flights = 0", 0 == testScore.flightsSuccessful());
	}
	
	@Test // Tests the get manual time played function
	public void testGetManualTime(){
		Score testScore = new Score();
		assertTrue("Manual time = 0", 0 == testScore.manualTime());
	}
	
	@Test // Tests the get time violated function
	public void testGetTimeViolated(){
		Score testScore = new Score();
		assertTrue("Time violated = 0", 0 == testScore.timeViolated());
	}
	
	@Test // Tests the get game overs function
	public void testGetGameOvers(){
		Score testScore = new Score();
		assertTrue("Game overs = 0", 0 == testScore.gameOvers());
	}
	
	@Test // Tests adding time played
	public void testAddTime(){
		Score testScore = new Score();
		testScore.addTime(6);
		testScore.addTime(3);
		assertTrue("Time played = 9", 9 == testScore.timePlayed());
	}
	
	@Test // Adding time in manual mode played
	public void testAddManualTime(){
		Score testScore = new Score();
		testScore.addTimeManual(8);
		testScore.addTimeManual(4);
		assertTrue("Manual time = 12", 12 == testScore.manualTime());
	}
	
	@Test // Adding separation violation
	public void testAddSepViolation(){
		Score testScore = new Score();
		testScore.addTimeViolated(10);
		testScore.addTimeViolated(16);
		assertTrue("Manual time = 26", 26 == testScore.timeViolated());
	}
	
	@Test // Adding a successful flight test
	public void testAddSuccessfulFlight(){
		Score testScore = new Score();
		for(int i = 0; i < 10; i++)
		{
			testScore.addFlight();
		}
		assertTrue("Successful flights = 10", 10 == testScore.flightsSuccessful());
	}

	@Test // Testing adding a game over function
	public void testAddGameOver(){
		Score testScore = new Score();
		for(int i = 0; i < 13; i++)
		{
			testScore.addGameOver();
		}
		assertTrue("Game overs = 13", 13 == testScore.gameOvers());
	}
	
	@Test // Testing the function to calculate score
	public void testCalculateScore(){
		Score testScore = new Score();
		
		// Generate game overs
		for(int i = 0; i < 5; i++)
		{
			testScore.addGameOver();
		}
		
		// Generate successful flights
		for(int i = 0; i < 9; i++)
		{
			testScore.addFlight();
		}
		
		// Add test data
		testScore.addTimeViolated(9);
		testScore.addTimeManual(10);
		testScore.addTime(1000);
		
		assertTrue("Score = -3119", -3119 == testScore.calculate());
	}
}
