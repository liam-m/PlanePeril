package tst;



import static org.junit.Assert.*;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cls.HoldingWaypoint;
import cls.Waypoint;
import cls.Waypoint.WaypointType;


public class HoldingWaypointTest {
	HoldingWaypoint testWaypoint = new HoldingWaypoint(10, 10);
	
	public HoldingWaypointTest() {
		
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
	public void testDrawDoubleDouble() {
		//Can't be tested
		//fail("Not yet implemented");
	}

	@Test
	public void testDraw() {
		//Can't be tested
		//fail("Not yet implemented");
	}

	@Test
	public void testHoldingWaypoint() {
		assertEquals(testWaypoint.position().x(), 10, 0);
		assertEquals(testWaypoint.position().y(), 10, 0);
		
	}

	@Test
	public void testSetNextWaypoint() {
		HoldingWaypoint testNextWaypoint = new HoldingWaypoint(20, 20);
		testWaypoint.setNextWaypoint(testNextWaypoint);
		assertEquals(testWaypoint.getNextWaypoint().position().x(), 20, 0);
		assertEquals(testWaypoint.getNextWaypoint().position().y(), 20, 0);
	
	}

	@Test
	public void testGetNextWaypoint() {
		HoldingWaypoint testNextWaypoint = new HoldingWaypoint(20, 20);
		testWaypoint.setNextWaypoint(testNextWaypoint);
		assertEquals(testWaypoint.getNextWaypoint().position().x(), 20, 0);
		assertEquals(testWaypoint.getNextWaypoint().position().y(), 20, 0);
		//fail("Not yet implemented");
	}

}
