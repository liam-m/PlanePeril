package tst;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import scn.Demo;
import cls.Aircraft;
import cls.Airport;
import cls.FlightPlan;
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
			new Waypoint(10, 10, WaypointType.ENTRY_EXIT, "Entry"),

			// bottom left
			new Waypoint(10, 1000, WaypointType.ENTRY_EXIT, "Exit"),

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
			locationWaypoints[2], // 14 - Airport
	};

	// All aircraft that land must pass through this waypoint.
	private final HoldingWaypoint[] landWaypoints = {

			new HoldingWaypoint(airport.position().x() + 140, airport
					.position().y()),
			new HoldingWaypoint(airport.position().x() + 240, airport
					.position().y()),

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
		holdingWaypoints.add(new HoldingWaypoint(locationWaypoints[2]
				.position().x() - 100,
				locationWaypoints[2].position().y() - 100));
		holdingWaypoints.add(new HoldingWaypoint(locationWaypoints[2]
				.position().x() + 100,
				locationWaypoints[2].position().y() - 100));
		holdingWaypoints.add(new HoldingWaypoint(locationWaypoints[2]
				.position().x() + 100,
				locationWaypoints[2].position().y() + 100));
		holdingWaypoints.add(new HoldingWaypoint(locationWaypoints[2]
				.position().x() - 100,
				locationWaypoints[2].position().y() + 100));

		// Initialise values of setNextWaypoint.
		holdingWaypoints.get(0).setNextWaypoint(holdingWaypoints.get(1));
		holdingWaypoints.get(1).setNextWaypoint(holdingWaypoints.get(2));
		holdingWaypoints.get(2).setNextWaypoint(holdingWaypoints.get(3));
		holdingWaypoints.get(3).setNextWaypoint(holdingWaypoints.get(0));

		aircraftInAirspace = new ArrayList<Aircraft>();

		testAircraft = new Aircraft("testAircraft", null, 10,
				Demo.DIFFICULTY_HARD, aircraftInAirspace,
				new FlightPlan(locationWaypoints[0], locationWaypoints[1],
						airspaceWaypoints, holdingWaypoints, takeoffWaypoint));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAircraft() {
		// fail("Not yet implemented");
	}

	@Test
	public void testPosition() {
		assertEquals(testAircraft.getPosition().x(), locationWaypoints[0]
				.position().x(), 0);
		assertEquals(testAircraft.getPosition().y(), locationWaypoints[0]
				.position().y(), 0);
	}

	@Test
	public void testName() {

		assertTrue(testAircraft.getName().equals("testAircraft"));
	}

	@Test
	public void testOriginName() {
		assertTrue(testAircraft.getFlightPlan().getOriginName().equals("Entry"));
	}

	@Test
	public void testDestinationName() {
		assertTrue(testAircraft.getFlightPlan().getDestinationName().equals("Exit"));
	}

	@Test
	public void testIsFinished() {
		assertFalse(testAircraft.hasFinished());

		Aircraft testAircraft2 = new Aircraft("testAircraft", null, 10,
				Demo.DIFFICULTY_HARD, aircraftInAirspace,
				new FlightPlan(locationWaypoints[0], locationWaypoints[1],
						airspaceWaypoints, holdingWaypoints, takeoffWaypoint));

		ArrayList<Aircraft> testAircrafts = new ArrayList<Aircraft>();
		testAircrafts.add(testAircraft);
		testAircrafts.add(testAircraft2);

		testAircraft2.setAltitude((int) testAircraft.getPosition().z());

		testAircraft.updateCollisions(1, testAircrafts);

		assertTrue(testAircraft.hasFinished());
	}

	@Test
	public void testAtAirport() {
		assertFalse(testAircraft.isAtAirport());

		// need another test. put aircraft into airport, test if this returns
		// true.
	}

	@Test
	public void testIsManuallyControlled() {
		assertFalse(testAircraft.isManuallyControlled());

		testAircraft.setManualControl(true);

		assertTrue(testAircraft.isManuallyControlled());
	}

	@Test
	public void testIsLanding() {
		assertFalse(testAircraft.isLanding());

		// need another test
	}

	@Test
	public void testOutOfBounds() {
		Waypoint outOfWindow = new Waypoint(-100.0, -100.0);

		testAircraft.alterPath(0, outOfWindow);

		for (int x = 0; x < 20; x = x + 1) {
			testAircraft.update(0.3);
		}

		assertTrue(testAircraft.isOutOfBounds());
	}

	@Test
	public void testSpeed() {
		assertEquals(testAircraft.getSpeed(), 30.0, 0.1);
	}

	@Test
	public void testIsAt() {
		Vector point2 = new Vector(locationWaypoints[0].position().x(),
				locationWaypoints[0].position().y(), 0);

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
		assertEquals(testAircraft.indexInFlightPath(airspaceWaypoints[0]), 0,
				0);

		assertEquals(testAircraft.indexInFlightPath(airspaceWaypoints[1]), 0,
				1);

		assertEquals(testAircraft.indexInFlightPath(locationWaypoints[1]), 2,
				0);

		assertEquals(testAircraft.indexInFlightPath(airspaceWaypoints[4]), -1,
				0);
	}

	@Test
	public void testAlterPath() {

		testAircraft.alterPath(1, airspaceWaypoints[2]);

		assertEquals(testAircraft.indexInFlightPath(airspaceWaypoints[2]), 0,
				1);

	}

	@Test
	public void testUpdateCollisions() {
		Aircraft testAircraft2 = new Aircraft("testAircraft", null, 10,
				Demo.DIFFICULTY_HARD, aircraftInAirspace,
				new FlightPlan(locationWaypoints[0], locationWaypoints[1],
						airspaceWaypoints, holdingWaypoints, takeoffWaypoint));

		ArrayList<Aircraft> testAircrafts = new ArrayList<Aircraft>();
		testAircrafts.add(testAircraft);
		testAircrafts.add(testAircraft2);

		testAircraft2.setAltitude((int) testAircraft.getPosition().z());

		testAircraft.updateCollisions(1, testAircrafts);

		assertTrue(testAircraft.hasFinished());
	}

	@Test
	public void testToggleManualControl() {
		assertFalse(testAircraft.isManuallyControlled());

		testAircraft.toggleManualControl();

		assertTrue(testAircraft.isManuallyControlled());
	}

	@Test
	public void testSetManualControl() {
		assertFalse(testAircraft.isManuallyControlled());

		testAircraft.setManualControl(true);

		assertTrue(testAircraft.isManuallyControlled());
	}

	@Test
	public void testToggleLand() {
		assertFalse(testAircraft.isLanding());

		testAircraft.toggleLand(landWaypoints[0]);

		assertTrue(testAircraft.isLanding());
	}

	@Test
	public void testSetBearing() {
		// fail("Not yet implemented");
	}

	@Test
	public void testClimb() {
		double currentAltitude = testAircraft.getPosition().z();

		if (currentAltitude == 15000.0) {
			testAircraft.decreaseTargetAltitude();
			testAircraft.update(0.6);
			currentAltitude = testAircraft.getPosition().z();
		}

		testAircraft.increaseTargetAltitude();
		testAircraft.update(0.3);

		assertTrue(testAircraft.getPosition().z() > currentAltitude);
	}

	@Test
	public void testFall() {
		double currentAltitude = testAircraft.getPosition().z();

		if (currentAltitude == 5000.0) {
			testAircraft.increaseTargetAltitude();
			testAircraft.update(0.6);
			currentAltitude = testAircraft.getPosition().z();
		}

		testAircraft.decreaseTargetAltitude();
		testAircraft.update(0.3);

		assertTrue(testAircraft.getPosition().z() < currentAltitude);
	}

	@Test
	public void testDecreaseTargetAltitude() {
		double currentAltitude = testAircraft.getPosition().z();

		if (currentAltitude == 5000.0) {
			testAircraft.increaseTargetAltitude();
			testAircraft.update(0.6);
			currentAltitude = testAircraft.getPosition().z();
		}

		testAircraft.decreaseTargetAltitude();
		testAircraft.update(0.3);

		assertTrue(testAircraft.getPosition().z() < currentAltitude);
	}

	@Test
	public void testIncreaseTargetAltitude() {
		double currentAltitude = testAircraft.getPosition().z();

		if (currentAltitude == 15000.0) {
			testAircraft.decreaseTargetAltitude();
			testAircraft.update(0.6);
			currentAltitude = testAircraft.getPosition().z();
		}

		testAircraft.increaseTargetAltitude();
		testAircraft.update(0.3);

		assertTrue(testAircraft.getPosition().z() > currentAltitude);
	}

	@Test
	public void testSetAltitude() {
		testAircraft.setAltitude(5000);
		assertEquals(testAircraft.getPosition().z(), 5000, 0);

		testAircraft.setAltitude(10000);
		assertEquals(testAircraft.getPosition().z(), 10000, 0);

	}

	@Test
	public void testGetPoints() {
		assertEquals(testAircraft.getNumPoints(), 20, 0);
	}

	@Test
	public void testGetDestination() {
		assertTrue(testAircraft.getFlightPlan().getDestination().equals(locationWaypoints[1]));
	}

}