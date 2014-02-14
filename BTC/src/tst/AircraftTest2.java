package tst;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cls.Aircraft;
import cls.Airport;
import cls.HoldingWaypoint;
import cls.Vector;
import cls.Waypoint;
import cls.Waypoint.WaypointType;

public class AircraftTest2 {

	public Airport airport;

	public ArrayList<Aircraft> aircraftInAirspace;

	public final Waypoint[] locationWaypoints = new Waypoint[] {
	/* A set of Waypoints which are origin / destination points */

			// top left
			new Waypoint(10, 10, WaypointType.ENTRY_EXIT, "Test Entry 1"),

			// bottom left
			new Waypoint(10, 1000, WaypointType.ENTRY_EXIT, "Test Entry 2"),

			// top right
			new Waypoint(1000, 10, WaypointType.ENTRY_EXIT, "Test Entry 3"),

			// bottom right
			new Waypoint(1000, 1000, WaypointType.ENTRY_EXIT, "Test Entry 4"),

			// The airport
			airport = new Airport(500, 500, "Airport"), };

	public Waypoint[] airspaceWaypoints = new Waypoint[] {

			// airspace waypoints
			new Waypoint(160, 174), // 0
			new Waypoint(383, 481), // 1
			new Waypoint(502, 274), // 2
			new Waypoint(632, 125), // 3
			new Waypoint(949, 96), // 4
			new Waypoint(698, 430), // 4.1
			new Waypoint(909, 481), // 5

			// destination/origin waypoints - present in this list for
			// pathfinding.
			locationWaypoints[0], // 10
			locationWaypoints[1], // 11
			locationWaypoints[2], // 12
			locationWaypoints[3], // 13
			locationWaypoints[4], // 14 - Airport
	};

	public Aircraft testAircraft;

	public ArrayList<HoldingWaypoint> holdingWaypoints = new ArrayList<HoldingWaypoint>();

	public final Waypoint takeoffWaypoint = new Waypoint(
			airport.position().x() - 60, airport.position().y());

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		holdingWaypoints.add(new HoldingWaypoint(locationWaypoints[4]
				.position().x() - 100,
				locationWaypoints[4].position().y() - 100));
		holdingWaypoints.add(new HoldingWaypoint(locationWaypoints[4]
				.position().x() + 100,
				locationWaypoints[4].position().y() - 100));
		holdingWaypoints.add(new HoldingWaypoint(locationWaypoints[4]
				.position().x() + 100,
				locationWaypoints[4].position().y() + 100));
		holdingWaypoints.add(new HoldingWaypoint(locationWaypoints[4]
				.position().x() - 100,
				locationWaypoints[4].position().y() + 100));

		// Initialise values of setNextWaypoint.
		holdingWaypoints.get(0).setNextWaypoint(holdingWaypoints.get(1));
		holdingWaypoints.get(1).setNextWaypoint(holdingWaypoints.get(2));
		holdingWaypoints.get(2).setNextWaypoint(holdingWaypoints.get(3));
		holdingWaypoints.get(3).setNextWaypoint(holdingWaypoints.get(0));

		aircraftInAirspace = new ArrayList<Aircraft>();

		testAircraft = new Aircraft("testAircraft", "Exit", "Entry",
				locationWaypoints[0], locationWaypoints[1], null, 10.0,
				airspaceWaypoints, 1, holdingWaypoints, takeoffWaypoint,
				aircraftInAirspace);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAircraft() {
		fail("Not yet implemented");
	}

	@Test
	public void testPosition() {
		assertEquals(testAircraft.position().x(), 10, 0);
		assertEquals(testAircraft.position().y(), 1000, 0);
	}

	@Test
	public void testName() {
		assertTrue(testAircraft.name().equals("testAircraft"));
	}

	@Test
	public void testOriginName() {
		assertTrue(testAircraft.originName().equals("Entry"));
	}

	@Test
	public void testDestinationName() {
		assertTrue(testAircraft.destinationName().equals("Exit"));
	}

	@Test
	public void testIsFinished() {
		assertFalse(testAircraft.isFinished());

		// need another test. collide it with another aircraft, and test if this
		// returns true.
	}

	@Test
	public void testAtAirport() {
		assertFalse(testAircraft.atAirport());

		// need another test. put aircraft into airport, test if this returns
		// true.
	}

	@Test
	public void testIsManuallyControlled() {
		assertFalse(testAircraft.isManuallyControlled());

		// need another test
	}

	@Test
	public void testIsLanding() {
		assertFalse(testAircraft.isLanding());

		// need another test
	}

	@Test
	public void testOutOfBounds() {
		assertFalse(testAircraft.outOfBounds());
		// need another test
	}

	@Test
	public void testBearing() {
		// TEST NOT WORKING

		// System.out.println(testAircraft.bearing());
		//
		// Waypoint currentTarget = testAircraft.
		//
		// double x = currentTarget.position().x() - position.x();
		// double y = currentTarget.position().y() - position.y();
		//
		// velocity = new Vector(x, y, 0).normalise().scaleBy(speed);

		fail("Not yet implemented");
	}

	@Test
	public void testSpeed() {
		// TEST NOT WORKING

		// System.out.println(testAircraft.speed());
		assertEquals(testAircraft.speed(), 10.0, 0);
	}

	@Test
	public void testIsAt() {
		Vector point1 = new Vector(10.0, 10.0, 10.0);

		assertFalse(testAircraft.isAt(point1));

		Vector point2 = new Vector(10.0, 1000.0, 10.0);

		assertTrue(testAircraft.isAt(point2));
	}

	@Test
	public void testIsTurningLeft() {
		assertFalse(testAircraft.isTurningLeft());
	}

	@Test
	public void testIsTurningRight() {
		assertFalse(testAircraft.isTurningRight());
	}

	@Test
	public void testFlightPathContains() {
		fail("Not yet implemented");
	}

	@Test
	public void testAlterPath() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsMouseOverIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsMouseOver() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testTurnLeft() {
		fail("Not yet implemented");
	}

	@Test
	public void testTurnRight() {
		fail("Not yet implemented");
	}

	@Test
	public void testDraw() {
		fail("Not yet implemented");
	}

	@Test
	public void testDrawCompass() {
		fail("Not yet implemented");
	}

	@Test
	public void testDrawFlightPath() {
		fail("Not yet implemented");
	}

	@Test
	public void testDrawModifiedPath() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindGreedyRoute() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateCollisions() {
		fail("Not yet implemented");
	}

	@Test
	public void testToggleManualControl() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetManualControl() {
		fail("Not yet implemented");
	}

	@Test
	public void testToggleLand() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetBearing() {
		fail("Not yet implemented");
	}

	@Test
	public void testClimb() {
		fail("Not yet implemented");
	}

	@Test
	public void testFall() {
		fail("Not yet implemented");
	}

	@Test
	public void testDecreaseTargetAltitude() {
		fail("Not yet implemented");
	}

	@Test
	public void testIncreaseTargetAltitude() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetAltitude() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPoints() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDestination() {
		fail("Not yet implemented");
	}

}
