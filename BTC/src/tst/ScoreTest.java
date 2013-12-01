package tst;

import static org.junit.Assert.*;

import org.junit.Test;

import cls.Score;

public class ScoreTest {

	
	// Tests Get Functions
	// Tests the get time played function
	@Test 
	public void testGetTime(){
		Score testScore = new Score();
		assertTrue("Time played = 0", 0 == testScore.timePlayed());
	}
	
	// Tests the get time played function
	@Test
	public void testGetFlightsSuccessful(){
		Score testScore = new Score();
		assertTrue("Total flights = 0", 0 == testScore.flightsSuccessful());
	}
	
	// Tests the get manual time played function
	@Test 
	public void testGetManualTime(){
		Score testScore = new Score();
		assertTrue("Manual time = 0", 0 == testScore.manualTime());
	}
	
	// Tests the get time violated function
	@Test 
	public void testGetTimeViolated(){
		Score testScore = new Score();
		assertTrue("Time violated = 0", 0 == testScore.timeViolated());
	}
	
	// Tests the get game overs function
	@Test 
	public void testGetGameOvers(){
		Score testScore = new Score();
		assertTrue("Game overs = 0", 0 == testScore.gameOvers());
	}
	
	// Tests adding time played
	@Test 
	public void testAddTime(){
		Score testScore = new Score();
		testScore.addTime(6);
		testScore.addTime(3);
		assertTrue("Time played = 9", 9 == testScore.timePlayed());
	}
	@Test
	public void testAddTime2(){
		Score testScore = new Score();
		testScore.addTime(1000);
		testScore.addTime(127);
		assertTrue("Time played = 1127", 1127 == testScore.timePlayed());
	}
	
	// Adding time in manual mode played
	@Test 
	public void testAddManualTime(){
		Score testScore = new Score();
		testScore.addTimeManual(8);
		testScore.addTimeManual(4);
		assertTrue("Manual time = 12", 12 == testScore.manualTime());
	}
	@Test
	public void testAddManualTime2(){
		Score testScore = new Score();
		testScore.addTimeManual(14);
		testScore.addTimeManual(88);
		assertTrue("Manual time = 102", 102 == testScore.manualTime());
	}
	
	// Adding separation violation
	@Test 
	public void testAddSepViolation(){
		Score testScore = new Score();
		testScore.addTimeViolated(10);
		testScore.addTimeViolated(16);
		assertTrue("Manual time = 26", 26 == testScore.timeViolated());
	}
	public void testAddSepViolation2(){
		Score testScore = new Score();
		testScore.addTimeViolated(1);
		assertTrue("Manual time = 1", 1 == testScore.timeViolated());
	}
	
	// Adding a successful flight test
	@Test 
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
	public void testAddGameOver2(){
		Score testScore = new Score();
		for(int i = 0; i < 4; i++)
		{
			testScore.addGameOver();
		}
		assertTrue("Game overs = 4", 4 == testScore.gameOvers());
	}
	
	// Testing the function to calculate score
	@Test 
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
	@Test 
	public void testCalculateScore2(){
		Score testScore = new Score();
		
		// Generate game overs
		testScore.addGameOver();
		
		// Generate successful flights
		for(int i = 0; i < 100; i++)
		{
			testScore.addFlight();
		}
		
		// Add test data
		testScore.addTimeViolated(0);
		testScore.addTimeManual(0);
		testScore.addTime(1000);
		
		assertTrue("Score = 10000", 10000 == testScore.calculate());
	}
}
