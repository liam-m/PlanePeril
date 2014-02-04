package tst;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cls.Aircraft;
import cls.Vector;
import cls.Waypoint;
import cls.Waypoint.WaypointType;

public class AircraftTest {

	// Create test aircraft
	private Aircraft generateTestAircraft() {
		Waypoint[] waypointList = new Waypoint[]{
				new Waypoint(0, 0, WaypointType.ENTRY_EXIT),
				new Waypoint(100, 100, WaypointType.ENTRY_EXIT),
				new Waypoint(25, 75, WaypointType.REGULAR),
				new Waypoint(75, 25, WaypointType.REGULAR),
				new Waypoint(50, 50, WaypointType.REGULAR)};
		Aircraft testAircraft = new Aircraft("testAircraft", "Berlin",
				"Dublin", new Waypoint(100, 100, WaypointType.ENTRY_EXIT),
				new Waypoint(0, 0, WaypointType.ENTRY_EXIT), null, 10.0,
				waypointList, 1);
		return testAircraft;
	}

	// Test get functions
	// Test getPosition function
	@Test
	public void testGePosition() {
		Aircraft testAircraft = generateTestAircraft();
		Vector resultPosition = testAircraft.position();
		assertTrue(
				"x >= -128 and xy <= 27, y = 0, z = 28,000 or z = 30,000",
				((0 == resultPosition.y()) && (128 >= resultPosition.x())
						&& (-128 <= resultPosition.x()) && ((28000 == resultPosition
						.z()) || (30000 == resultPosition.z()))));
	}
	// Test getName function
	@Test
	public void testGetName() {
		Aircraft testAircraft = generateTestAircraft();
		String name = testAircraft.name();
		assertTrue("Name = testAircraft", "testAircraft" == name);
	}
	// Test getOriginName function
	@Test
	public void testGetOriginName() {
		Aircraft testAircraft = generateTestAircraft();
		String name = testAircraft.originName();
		assertTrue("Origin name = Dublin", "Dublin" == name);
	}
	// Test getDestinationName function
	@Test
	public void testGetDestinationName() {
		Aircraft testAircraft = generateTestAircraft();
		String name = testAircraft.destinationName();
		assertTrue("Destination name = Berlin", "Berlin" == name);
	}
	// Test getIsFinished function
	@Test
	public void testGetIsFinishedName() {
		Aircraft testAircraft = generateTestAircraft();
		boolean status = testAircraft.isFinished();
		assertTrue("Finished = false", false == status);
	}
	// Test getIsManuallyControlled function
	@Test
	public void testIsManuallyControlled() {
		Aircraft testAircraft = generateTestAircraft();
		boolean status = testAircraft.isManuallyControlled();
		assertTrue("Manually controlled = false", false == status);
	}
	// Test getSpeed function
	@Test
	public void testGetSpeed() {
		Aircraft testAircraft = generateTestAircraft();
		double speed = (int) (testAircraft.speed() + 0.5);
		assertTrue("Speed = 20", speed == 20.0);
	}
	// Test getAltitudeState
	@Test
	public void testAltitudeState() {
		Aircraft testAircraft = generateTestAircraft();
		testAircraft.setAltitudeState(1);
		int altState = testAircraft.altitudeState();
		assertTrue("Altitude State = 1", altState == 1);
	}

	// Test outOfBounds
	@Test
	public void testOutOfBounds() {
		Waypoint[] waypointList = new Waypoint[]{
				new Waypoint(0, 0, WaypointType.ENTRY_EXIT),
				new Waypoint(100, 100, WaypointType.ENTRY_EXIT),
				new Waypoint(25, 75, WaypointType.REGULAR),
				new Waypoint(75, 25, WaypointType.REGULAR),
				new Waypoint(50, 50, WaypointType.REGULAR)};
		Aircraft testAircraft = new Aircraft("testAircraft", "Berlin",
				"Dublin", new Waypoint(100, 100, WaypointType.ENTRY_EXIT),
				new Waypoint(0, 0, WaypointType.ENTRY_EXIT), null, 10.0,
				waypointList, 1);
		boolean x = testAircraft.outOfBounds();
		assertTrue("Out of bounds = false", x == true);
	}

	// Test set methods
	// Test setAltitudeState
	@Test
	public void testSetAltitudeState() {
		Aircraft testAircraft = generateTestAircraft();
		testAircraft.setAltitudeState(1);
		int altState = testAircraft.altitudeState();
		assertTrue("Altitude State = 1", altState == 1);
	}

}
