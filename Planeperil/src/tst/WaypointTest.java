package tst;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cls.Vector;
import cls.Waypoint;
import cls.Waypoint.WaypointType;

public class WaypointTest {

	
	@Test
	public void testGetPosition() {
		Waypoint test_waypoint = new Waypoint(10, 10);
		Vector result_vector = test_waypoint.position();
		assertTrue("Position = (10, 10, 0)", (10 == result_vector.x())
				&& (10 == result_vector.y()) && (0 == result_vector.z()));
	}

	@Test
	public void testIsEntryOrExit() {
		Waypoint test_waypoint = new Waypoint(10, 10);
		assertTrue("Entry/Exit = false",
				WaypointType.REGULAR == test_waypoint.getType());
	}
	
	@Test
	public void testIsEntryOrExit2() {
		Waypoint test_waypoint = new Waypoint(0, 0, WaypointType.ENTRY_EXIT);
		assertTrue("Entry/Exit = true",
				WaypointType.ENTRY_EXIT == test_waypoint.getType());
	}

	@Test
	public void testIsMouseOver() {
		Waypoint test_waypoint = new Waypoint(5, 5);
		assertTrue("Mouse over = true",
				true == test_waypoint.isMouseOver(10, 10));
	}

	@Test
	public void testIsMouseOver2() {
		Waypoint test_waypoint = new Waypoint(40, 40);
		assertTrue("Mouse over = false",
				false == test_waypoint.isMouseOver(10, 10));
	}

	@Test
	public void testGetCost() {
		Waypoint test_waypoint = new Waypoint(2, 4);
		Waypoint test_waypoint2 = new Waypoint(2, 2);
		double result = test_waypoint.getCost(test_waypoint2);
		assertTrue("Cost = 2", 2 == result);
	}
	
	@Test
	public void testGetCost2() {
		Waypoint test_waypoint = new Waypoint(6, 15);
		Waypoint test_waypoint2 = new Waypoint(15, 15);
		double result = test_waypoint.getCost(test_waypoint2);
		assertTrue("Cost = 9", 9 == result);
	}

	@Test
	public void testGetCostBetween() {
		Waypoint test_waypoint = new Waypoint(2, 4);
		Waypoint test_waypoint2 = new Waypoint(2, 2);
		double result = Waypoint.getCostBetween(test_waypoint, test_waypoint2);
		assertTrue("Cost = 2", 2 == result);
	}
	
	@Test
	public void testGetCostBetween2() {
		Waypoint test_waypoint = new Waypoint(6, 15);
		Waypoint test_waypoint2 = new Waypoint(15, 15);
		double result = Waypoint.getCostBetween(test_waypoint, test_waypoint2);
		assertTrue("Cost = 9", 9 == result);
	}

}
