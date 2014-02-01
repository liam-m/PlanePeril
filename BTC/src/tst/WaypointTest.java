package tst;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cls.Vector;
import cls.Waypoint;
import cls.Waypoint.WaypointType;

public class WaypointTest {

	// Test Get Functions
	// Test get position function
	@Test
	public void testGetPosition() {
		Waypoint testWaypoint = new Waypoint(10, 10, WaypointType.REGULAR);
		Vector resultVector = testWaypoint.position();
		assertTrue("Position = (10, 10, 0)", (10 == resultVector.x())
				&& (10 == resultVector.y()) && (0 == resultVector.z()));
	}

	// Test isEntryOrExit function
	@Test
	public void testIsEntryOrExit() {
		Waypoint testWaypoint = new Waypoint(10, 10, WaypointType.REGULAR);
		assertTrue("Entry/Exit = false",
				WaypointType.REGULAR == testWaypoint.getType());
	}
	
	@Test
	public void testIsEntryOrExit2() {
		Waypoint testWaypoint = new Waypoint(0, 0, WaypointType.ENTRY_EXIT);
		assertTrue("Entry/Exit = true",
				WaypointType.ENTRY_EXIT == testWaypoint.getType());
	}

	// Test mouseOver checking
	@Test
	public void testIsMouseOver() {
		Waypoint testWaypoint = new Waypoint(5, 5, WaypointType.ENTRY_EXIT);
		assertTrue("Mouse over = true",
				true == testWaypoint.isMouseOver(10, 10));
	}

	@Test
	public void testIsMouseOver2() {
		Waypoint testWaypoint = new Waypoint(25, 25, WaypointType.ENTRY_EXIT);
		assertTrue("Mouse over = false",
				false == testWaypoint.isMouseOver(10, 10));
	}

	// Test getCost function
	@Test
	public void testGetCost() {
		Waypoint testWaypoint = new Waypoint(2, 4, WaypointType.REGULAR);
		Waypoint testWaypoint2 = new Waypoint(2, 2, WaypointType.ENTRY_EXIT);
		double result = testWaypoint.getCost(testWaypoint2);
		assertTrue("Cost = 2", 2 == result);
	}
	
	@Test
	public void testGetCost2() {
		Waypoint testWaypoint = new Waypoint(6, 15, WaypointType.REGULAR);
		Waypoint testWaypoint2 = new Waypoint(15, 15, WaypointType.ENTRY_EXIT);
		double result = testWaypoint.getCost(testWaypoint2);
		assertTrue("Cost = 9", 9 == result);
	}

	// Test getCostBetween function
	@Test
	public void testGetCostBetween() {
		Waypoint testWaypoint = new Waypoint(2, 4, WaypointType.REGULAR);
		Waypoint testWaypoint2 = new Waypoint(2, 2, WaypointType.ENTRY_EXIT);
		double result = Waypoint.getCostBetween(testWaypoint, testWaypoint2);
		assertTrue("Cost = 2", 2 == result);
	}
	
	@Test
	public void testGetCostBetween2() {
		Waypoint testWaypoint = new Waypoint(6, 15, WaypointType.REGULAR);
		Waypoint testWaypoint2 = new Waypoint(15, 15, WaypointType.ENTRY_EXIT);
		double result = Waypoint.getCostBetween(testWaypoint, testWaypoint2);
		assertTrue("Cost = 9", 9 == result);
	}

}
