package tst;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cls.Vector;
import cls.Waypoint;
import cls.Waypoint.WaypointType;

public class WaypointTest2 {
	
	Waypoint test_waypoint = new Waypoint(10, 10);

	
	
	public WaypointTest2() {
		
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
	public void testWaypointDoubleDoubleWaypointTypeString() {
		Waypoint test_waypoint = new Waypoint(10, 10, WaypointType.REGULAR, "" );
		Vector result_vector = test_waypoint.position();
		assertTrue("Position = (10, 10, 0)", (10 == result_vector.x())
				&& (10 == result_vector.y()) && (0 == result_vector.z()));
		//fail("Not yet implemented");
	}

	@Test
	public void testWaypointDoubleDouble() {
		Waypoint test_waypoint = new Waypoint(10, 10);
		Vector result_vector = test_waypoint.position();
		assertTrue("Position = (10, 10, 0)", (10 == result_vector.x())
				&& (10 == result_vector.y()) && (0 == result_vector.z()));
		//fail("Not yet implemented");
	}

	@Test
	public void testPosition() {
		assertEquals(test_waypoint.position().x(), 10, 0);
		assertEquals(test_waypoint.position().x(), 10, 0);
	}

	@Test
	public void testIsMouseOver() {
		assertTrue("Mouse over = true",
				true == test_waypoint.isMouseOver(10, 10));
		//fail("Not yet implemented");
	}

	@Test															
	public void testGetType() {
		assertEquals(test_waypoint.getType(), WaypointType.REGULAR);
	}

	@Test
	public void testGetCost() {
		Waypoint test_waypoint2 = new Waypoint(10, 12);
		double result = test_waypoint.getCost(test_waypoint2);
		assertTrue("Cost = 2", 2 == result);
		
	}

	@Test
	public void testGetCostBetween() {
		Waypoint test_waypoint2 = new Waypoint(10, 12);
		double result = test_waypoint.getCost(test_waypoint2);
		assertTrue("Cost = 2", 2 == result);
		
	}

	@Test
	public void testGetName() {
		Waypoint test_waypoint = new Waypoint(10, 10, WaypointType.REGULAR, "INI" );
		assertEquals(test_waypoint.getName(), "INI");
	}

	@Test
	public void testDrawDoubleDouble() {
		// Cannot be unit tested
		//fail("Not yet implemented");
	}

	@Test
	public void testDraw() {
		// Cannot be unit tested
		//fail("Not yet implemented");
	}

}
