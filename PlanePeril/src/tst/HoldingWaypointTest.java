package tst;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import cls.HoldingWaypoint;

public class HoldingWaypointTest {
	HoldingWaypoint test_waypoint = new HoldingWaypoint(10, 10);
	
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
		assertEquals(test_waypoint.position().x(), 10, 0);
		assertEquals(test_waypoint.position().y(), 10, 0);
		
	}

	@Test
	public void testSetNextWaypoint() {
		HoldingWaypoint testNextWaypoint = new HoldingWaypoint(20, 20);
		test_waypoint.setNextWaypoint(testNextWaypoint);
		assertEquals(test_waypoint.getNextWaypoint().position().x(), 20, 0);
		assertEquals(test_waypoint.getNextWaypoint().position().y(), 20, 0);
	
	}

	@Test
	public void testGetNextWaypoint() {
		HoldingWaypoint testNextWaypoint = new HoldingWaypoint(20, 20);
		test_waypoint.setNextWaypoint(testNextWaypoint);
		assertEquals(test_waypoint.getNextWaypoint().position().x(), 20, 0);
		assertEquals(test_waypoint.getNextWaypoint().position().y(), 20, 0);
		//fail("Not yet implemented");
	}
}