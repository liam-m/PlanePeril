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
	
	Waypoint testWaypoint = new Waypoint(10, 10);

	
	
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
		Waypoint testWaypoint = new Waypoint(10, 10, WaypointType.REGULAR, "" );
		Vector resultVector = testWaypoint.position();
		assertTrue("Position = (10, 10, 0)", (10 == resultVector.x())
				&& (10 == resultVector.y()) && (0 == resultVector.z()));
		//fail("Not yet implemented");
	}

	@Test
	public void testWaypointDoubleDouble() {
		Waypoint testWaypoint = new Waypoint(10, 10);
		Vector resultVector = testWaypoint.position();
		assertTrue("Position = (10, 10, 0)", (10 == resultVector.x())
				&& (10 == resultVector.y()) && (0 == resultVector.z()));
		//fail("Not yet implemented");
	}

	@Test
	public void testPosition() {
		assertEquals(testWaypoint.position().x(), 10, 0);
		assertEquals(testWaypoint.position().x(), 10, 0);
	}

	@Test
	public void testIsMouseOver() {
		assertTrue("Mouse over = true",
				true == testWaypoint.isMouseOver(10, 10));
		//fail("Not yet implemented");
	}

	@Test															
	public void testGetType() {
		assertEquals(testWaypoint.getType(), WaypointType.REGULAR);
	}

	@Test
	public void testGetCost() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCostBetween() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetName() {
		Waypoint testWaypoint = new Waypoint(10, 10, WaypointType.REGULAR, "INI" );
		assertEquals(testWaypoint.getName(), "INI");
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
