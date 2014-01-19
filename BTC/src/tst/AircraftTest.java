package tst;

import static org.junit.Assert.*;

import org.junit.Test;

import cls.Aircraft;
import cls.Waypoint;
import cls.Vector;

public class AircraftTest {

	// Test Get functions
	// Test getPosition function
	@Test
	public void testGePosition() {
		Waypoint[] waypointList = new Waypoint[]{new Waypoint(0, 0, true), new Waypoint(100, 100, true), new Waypoint(25, 75, false), new Waypoint(75, 25, false), new Waypoint(50,50, false)};
		Aircraft testAircraft = new Aircraft("testAircraft", "Berlin", "Dublin", new Waypoint(100,100, true), new Waypoint(0,0, true), null, 10.0, waypointList, 1);
		Vector resultPosition = testAircraft.position();
		assertTrue("x >= -64 and xy <= 64, y = 0, z = 28,000 or z = 30,000", ((0 == resultPosition.y()) && (64 >= resultPosition.x()) && (-64 <= resultPosition.x()) && ((28000 == resultPosition.z()) || (30000 == resultPosition.z()))));
	}
	// Test getName function
	@Test
	public void testGetName() {
		Waypoint[] waypointList = new Waypoint[]{new Waypoint(0, 0, true), new Waypoint(100, 100, true), new Waypoint(25, 75, false), new Waypoint(75, 25, false), new Waypoint(50,50, false)};
		Aircraft testAircraft = new Aircraft("testAircraft", "Berlin", "Dublin", new Waypoint(100,100, true), new Waypoint(0,0, true), null, 10.0, waypointList, 1);
		String name = testAircraft.name();
		assertTrue("Name = testAircraft", "testAircraft" == name);
	}
	// Test getOriginName function
	@Test
	public void testGetOriginName(){
		Waypoint[] waypointList = new Waypoint[]{new Waypoint(0, 0, true), new Waypoint(100, 100, true), new Waypoint(25, 75, false), new Waypoint(75, 25, false), new Waypoint(50,50, false)};
		Aircraft testAircraft = new Aircraft("testAircraft", "Berlin", "Dublin", new Waypoint(100,100, true), new Waypoint(0,0, true), null, 10.0, waypointList, 1);
		String name = testAircraft.originName();
		assertTrue("Origin name = Dublin", "Dublin" == name);
	}
	// Test getDestinationName function
	@Test
	public void testGetDestinationName(){
		Waypoint[] waypointList = new Waypoint[]{new Waypoint(0, 0, true), new Waypoint(100, 100, true), new Waypoint(25, 75, false), new Waypoint(75, 25, false), new Waypoint(50,50, false)};
		Aircraft testAircraft = new Aircraft("testAircraft", "Berlin", "Dublin", new Waypoint(100,100, true), new Waypoint(0,0, true), null, 10.0, waypointList, 1);
		String name = testAircraft.destinationName();
		assertTrue("Destination name = Berlin", "Berlin" == name);
	}
	// Test getIsFinished function
	@Test
	public void testGetIsFinishedName(){
		Waypoint[] waypointList = new Waypoint[]{new Waypoint(0, 0, true), new Waypoint(100, 100, true), new Waypoint(25, 75, false), new Waypoint(75, 25, false), new Waypoint(50,50, false)};
		Aircraft testAircraft = new Aircraft("testAircraft", "Berlin", "Dublin", new Waypoint(100,100, true), new Waypoint(0,0, true), null, 10.0, waypointList, 1);
		boolean status = testAircraft.isFinished();
		assertTrue("Finished = false", false == status);
	}
	// Test getIsManuallyControlled function
	@Test
	public void testIsManuallyControlled(){
		Waypoint[] waypointList = new Waypoint[]{new Waypoint(0, 0, true), new Waypoint(100, 100, true), new Waypoint(25, 75, false), new Waypoint(75, 25, false), new Waypoint(50,50, false)};
		Aircraft testAircraft = new Aircraft("testAircraft", "Berlin", "Dublin", new Waypoint(100,100, true), new Waypoint(0,0, true), null, 10.0, waypointList, 1);
		boolean status = testAircraft.isManuallyControlled();
		assertTrue("Manually controlled = false", false == status);
	}
	// Test getSpeed function
	@Test
	public void testGetSpeed(){
		Waypoint[] waypointList = new Waypoint[]{new Waypoint(0, 0, true), new Waypoint(100, 100, true), new Waypoint(25, 75, false), new Waypoint(75, 25, false), new Waypoint(50,50, false)};
		Aircraft testAircraft = new Aircraft("testAircraft", "Berlin", "Dublin", new Waypoint(100,100, true), new Waypoint(0,0, true), null, 10.0, waypointList, 1);
		double speed = testAircraft.speed();
		assertTrue("Speed = 20", speed == 20.0);
	}
}
