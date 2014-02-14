package tst;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cls.Airport;

public class AirportTest {

	public AirportTest() {
		Airport testAirport = new Airport(10, 10, "INI");
		
		
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAirport() {
		//If initialisation works, this works.
		//fail("Not yet implemented");
	}

	@Test
	public void testInsertAircraft() {
		
		fail("Not yet implemented");
	}

	@Test
	public void testTakeoff() {
		fail("Not yet implemented");
	}

	@Test
	public void testDrawAirportInfo() {
		fail("Not yet implemented");
	}

}
