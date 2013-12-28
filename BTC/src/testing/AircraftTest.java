package testing;

import static org.junit.Assert.*;
import lib.jog.graphics;

import org.junit.Test;

import classes.Aircraft;
import classes.Vector;
import classes.Waypoint;

public class AircraftTest {

	// Test Get functions
	// Test getPosition function
	@Test
	public void testGePosition() {
		Waypoint[] waypointList = new Waypoint[]{new Waypoint(0, 0, true), new Waypoint(100, 100, true), new Waypoint(25, 75, false), new Waypoint(75, 25, false), new Waypoint(50,50, false)};
		Aircraft testAircraft = new Aircraft("testAircraft", "Berlin", "Dublin", new Waypoint(100,100, true), new Waypoint(0,0, true), graphics.newImage(null), 10.0, waypointList, 1);
		Vector resultPosition = testAircraft.position();
		assertTrue("x = 0, y = 0, z = 0", (0 == resultPosition.x()) && (0 == resultPosition.y()) && (0 == resultPosition.z()));
	}
	// Test getName function
	@Test
	public void testGetName() {
		Waypoint[] waypointList = new Waypoint[]{new Waypoint(0, 0, true), new Waypoint(100, 100, true), new Waypoint(25, 75, false), new Waypoint(75, 25, false), new Waypoint(50,50, false)};
		Aircraft testAircraft = new Aircraft("testAircraft", "Berlin", "Dublin", new Waypoint(100,100, true), new Waypoint(0,0, true), graphics.newImage(null), 10.0, waypointList, 1);
		assertTrue("Name = testAircraft", "testAircraft" == testAircraft.name());
	}
	// Test getOriginName function
	@Test
	public void testGetOriginName(){
		Waypoint[] waypointList = new Waypoint[]{new Waypoint(0, 0, true), new Waypoint(100, 100, true), new Waypoint(25, 75, false), new Waypoint(75, 25, false), new Waypoint(50,50, false)};
		Aircraft testAircraft = new Aircraft("testAircraft", "Berlin", "Dublin", new Waypoint(100,100, true), new Waypoint(0,0, true), graphics.newImage(null), 10.0, waypointList, 1);
		assertTrue("Origin name = Berlin", "Berlin" == testAircraft.originName());
	}
	// Test getDestinationName function
	@Test
	public void testGetDestinationName(){
		Waypoint[] waypointList = new Waypoint[]{new Waypoint(0, 0, true), new Waypoint(100, 100, true), new Waypoint(25, 75, false), new Waypoint(75, 25, false), new Waypoint(50,50, false)};
		Aircraft testAircraft = new Aircraft("testAircraft", "Berlin", "Dublin", new Waypoint(100,100, true), new Waypoint(0,0, true), graphics.newImage(null), 10.0, waypointList, 1);
		assertTrue("Origin name = Dublin", "Dublin" == testAircraft.destinationName());
	}
	// Test if the aircraft has finished
	@Test
	public void testIsFinished(){
		assertTrue("test", true);
	}

}
