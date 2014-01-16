package tst;

import static org.junit.Assert.*;

import org.junit.Test;

import cls.Waypoint;

import cls.Vector;

public class WaypointTest {

	// Test Get Functions
	// Test get position function
	@Test
	public void testGetPosition() {
		Waypoint testWaypoint = new Waypoint(10,10, false);
		Vector resultVector = testWaypoint.position();
		assertTrue("Position = (10, 10, 0)", (10 == resultVector.x()) && (10 == resultVector.y()) && (0 == resultVector.z()));
	}
	
	// Test isEntryOrExit function
	@Test
	public void testIsEntryOrExit() {
		Waypoint testWaypoint = new Waypoint(10,10, false);
		assertTrue("Entry/Exit = false", false == testWaypoint.isEntryOrExit());
	}
	@Test
	public void testIsEntryOrExit2() {
		Waypoint testWaypoint = new Waypoint(0, 0, true);
		assertTrue("Entry/Exit = true", true == testWaypoint.isEntryOrExit());
	}
	
	// Test mouseOver checking
	@Test
	public void testIsMouseOver(){
		Waypoint testWaypoint = new Waypoint(5,5, true);
		assertTrue("Mouse over = true", true == testWaypoint.isMouseOver(10,10));
	}
	@Test
	public void testIsMouseOver2(){
		Waypoint testWaypoint = new Waypoint(25,25, true);
		assertTrue("Mouse over = false", false == testWaypoint.isMouseOver(10,10));
	}
	
	// Test getCost function - incorrect
	@Test
	public void testGetCost(){
		Waypoint testWaypoint = new Waypoint(2, 4, false);
		Waypoint testWaypoint2 = new Waypoint(2, 2, true);
		double result = testWaypoint.getCost(testWaypoint2);
		assertTrue("Cost = 2", 2 == result);
	}@Test
	public void testGetCost2(){
		Waypoint testWaypoint = new Waypoint(6, 15, false);
		Waypoint testWaypoint2 = new Waypoint(15, 15, true);
		double result = testWaypoint.getCost(testWaypoint2);
		assertTrue("Cost = 9", 9 == result);
	}
	
	
}
