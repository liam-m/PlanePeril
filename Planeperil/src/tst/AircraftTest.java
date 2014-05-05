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

import scn.SinglePlayer;
import cls.Aircraft;
import cls.Airport;
import cls.FlightPlan;
import cls.HoldingWaypoint;
import cls.Vector;
import cls.Waypoint;
import cls.Waypoint.WaypointType;

public class AircraftTest {

	public Airport airport;

	public ArrayList<Aircraft> aircraft_in_airspace;

	public final Waypoint[] location_waypoints = new Waypoint[] {
	/* A set of Waypoints which are origin / destination points */

			// top left
			new Waypoint(10, 10, WaypointType.ENTRY_EXIT, "Entry"),

			// bottom left
			new Waypoint(10, 1000, WaypointType.ENTRY_EXIT, "Exit"),

			// The airport
			airport = new Airport(500, 500, "Airport", true), 
	};

	public Waypoint[] airspace_waypoints = new Waypoint[] {

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
			location_waypoints[0], // 10
			location_waypoints[1], // 11
			location_waypoints[2], // 14 - Airport
	};

	// All aircraft that land must pass through this waypoint.
	private final HoldingWaypoint[] land_waypoints = {

			new HoldingWaypoint(airport.position().x() + 140, airport
					.position().y()),
			new HoldingWaypoint(airport.position().x() + 240, airport
					.position().y()),

	};

	public Aircraft test_aircraft;

	public ArrayList<HoldingWaypoint> holding_waypoints = new ArrayList<HoldingWaypoint>();

	public final Waypoint takeoff_waypoint = new Waypoint(
			airport.position().x() - 60, airport.position().y());

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		holding_waypoints.add(new HoldingWaypoint(location_waypoints[2]
				.position().x() - 100,
				location_waypoints[2].position().y() - 100));
		holding_waypoints.add(new HoldingWaypoint(location_waypoints[2]
				.position().x() + 100,
				location_waypoints[2].position().y() - 100));
		holding_waypoints.add(new HoldingWaypoint(location_waypoints[2]
				.position().x() + 100,
				location_waypoints[2].position().y() + 100));
		holding_waypoints.add(new HoldingWaypoint(location_waypoints[2]
				.position().x() - 100,
				location_waypoints[2].position().y() + 100));

		// Initialise values of setNextWaypoint.
		holding_waypoints.get(0).setNextWaypoint(holding_waypoints.get(1));
		holding_waypoints.get(1).setNextWaypoint(holding_waypoints.get(2));
		holding_waypoints.get(2).setNextWaypoint(holding_waypoints.get(3));
		holding_waypoints.get(3).setNextWaypoint(holding_waypoints.get(0));

		test_aircraft = new Aircraft("test_aircraft", null, 10,
				SinglePlayer.DIFFICULTY_HARD,
				new FlightPlan(location_waypoints[0], location_waypoints[1],
						airspace_waypoints, holding_waypoints, takeoff_waypoint), -1);
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
		assertEquals(test_aircraft.getPosition().x(), location_waypoints[0].position().x(), 0);
		assertEquals(test_aircraft.getPosition().y(), location_waypoints[0].position().y(), 0);
	}

	@Test
	public void testName() {

		assertTrue(test_aircraft.getName().equals("test_aircraft"));
	}

	@Test
	public void testOriginName() {
		assertTrue(test_aircraft.getFlightPlan().getOriginName().equals("Entry"));
	}

	@Test
	public void testDestinationName() {
		assertTrue(test_aircraft.getFlightPlan().getDestinationName().equals("Exit"));
	}

	@Test
	public void testIsFinished() {
		assertFalse(test_aircraft.hasFinished());

		Aircraft testAircraft2 = new Aircraft("testAircraft", null, 10,
				SinglePlayer.DIFFICULTY_HARD,
				new FlightPlan(location_waypoints[0], location_waypoints[1],
						airspace_waypoints, holding_waypoints, takeoff_waypoint), -1);

		ArrayList<Aircraft> testAircrafts = new ArrayList<Aircraft>();
		testAircrafts.add(test_aircraft);
		testAircrafts.add(testAircraft2);

		testAircraft2.setAltitude((int) test_aircraft.getPosition().z());

		test_aircraft.updateCollisions(1, testAircrafts);

		assertTrue(test_aircraft.hasFinished());
	}

	@Test
	public void testAtAirport() {
		assertFalse(test_aircraft.isAtAirport());

		// need another test. put aircraft into airport, test if this returns
		// true.
	}

	@Test
	public void testIsManuallyControlled() {
		assertFalse(test_aircraft.isManuallyControlled());

		test_aircraft.setManualControl(true);

		assertTrue(test_aircraft.isManuallyControlled());
	}

	@Test
	public void testIsLanding() {
		assertFalse(test_aircraft.isLanding());

		// need another test
	}

	@Test
	public void testOutOfBounds() {
		Waypoint outOfWindow = new Waypoint(-100.0, -100.0);

		test_aircraft.alterPath(0, outOfWindow);

		for (int x = 0; x < 20; x = x + 1) {
			test_aircraft.update(0.3);
		}

		assertTrue(test_aircraft.isOutOfBounds());
	}

	@Test
	public void testSpeed() {
		assertEquals(test_aircraft.getSpeed(), 30.0, 0.1);
	}

	@Test
	public void testIsAt() {
		Vector point2 = new Vector(location_waypoints[0].position().x(),
				location_waypoints[0].position().y(), 0);

		assertTrue(test_aircraft.isAt(point2));
	}

	@Test
	public void testIsTurningLeft() {
		assertFalse(test_aircraft.isTurningLeft());
	}

	@Test
	public void testIsTurningRight() {
		assertFalse(test_aircraft.isTurningRight());
	}

	@Test
	public void testFlightPathContains() {
		assertEquals(test_aircraft.indexInFlightPath(airspace_waypoints[0]), 0,
				0);

		assertEquals(test_aircraft.indexInFlightPath(airspace_waypoints[1]), 0,
				1);

		assertEquals(test_aircraft.indexInFlightPath(location_waypoints[1]), 2,
				0);

		assertEquals(test_aircraft.indexInFlightPath(airspace_waypoints[4]), -1,
				0);
	}

	@Test
	public void testAlterPath() {

		test_aircraft.alterPath(1, airspace_waypoints[2]);

		assertEquals(test_aircraft.indexInFlightPath(airspace_waypoints[2]), 0,
				1);

	}

	@Test
	public void testUpdateCollisions() {
		Aircraft testAircraft2 = new Aircraft("testAircraft", null, 10,
				SinglePlayer.DIFFICULTY_HARD,
				new FlightPlan(location_waypoints[0], location_waypoints[1],
						airspace_waypoints, holding_waypoints, takeoff_waypoint), -1);

		ArrayList<Aircraft> testAircrafts = new ArrayList<Aircraft>();
		testAircrafts.add(test_aircraft);
		testAircrafts.add(testAircraft2);

		testAircraft2.setAltitude((int) test_aircraft.getPosition().z());

		test_aircraft.updateCollisions(1, testAircrafts);

		assertTrue(test_aircraft.hasFinished());
	}

	@Test
	public void testToggleManualControl() {
		assertFalse(test_aircraft.isManuallyControlled());

		test_aircraft.toggleManualControl();

		assertTrue(test_aircraft.isManuallyControlled());
	}

	@Test
	public void testSetManualControl() {
		assertFalse(test_aircraft.isManuallyControlled());

		test_aircraft.setManualControl(true);

		assertTrue(test_aircraft.isManuallyControlled());
	}

	@Test
	public void testToggleLand() {
		assertFalse(test_aircraft.isLanding());

		test_aircraft.toggleLand(land_waypoints[0]);

		assertTrue(test_aircraft.isLanding());
	}

	@Test
	public void testSetBearing() {
		// fail("Not yet implemented");
	}

	@Test
	public void testClimb() {
		double currentAltitude = test_aircraft.getPosition().z();

		if (currentAltitude == 15000.0) {
			test_aircraft.decreaseTargetAltitude();
			test_aircraft.update(0.6);
			currentAltitude = test_aircraft.getPosition().z();
		}

		test_aircraft.increaseTargetAltitude();
		test_aircraft.update(0.3);

		assertTrue(test_aircraft.getPosition().z() > currentAltitude);
	}

	@Test
	public void testFall() {
		double currentAltitude = test_aircraft.getPosition().z();

		if (currentAltitude == 5000.0) {
			test_aircraft.increaseTargetAltitude();
			test_aircraft.update(0.6);
			currentAltitude = test_aircraft.getPosition().z();
		}

		test_aircraft.decreaseTargetAltitude();
		test_aircraft.update(0.3);

		assertTrue(test_aircraft.getPosition().z() < currentAltitude);
	}

	@Test
	public void testDecreaseTargetAltitude() {
		double currentAltitude = test_aircraft.getPosition().z();

		if (currentAltitude == 5000.0) {
			test_aircraft.increaseTargetAltitude();
			test_aircraft.update(0.6);
			currentAltitude = test_aircraft.getPosition().z();
		}

		test_aircraft.decreaseTargetAltitude();
		test_aircraft.update(0.3);

		assertTrue(test_aircraft.getPosition().z() < currentAltitude);
	}

	@Test
	public void testIncreaseTargetAltitude() {
		double currentAltitude = test_aircraft.getPosition().z();

		if (currentAltitude == 15000.0) {
			test_aircraft.decreaseTargetAltitude();
			test_aircraft.update(0.6);
			currentAltitude = test_aircraft.getPosition().z();
		}

		test_aircraft.increaseTargetAltitude();
		test_aircraft.update(0.3);

		assertTrue(test_aircraft.getPosition().z() > currentAltitude);
	}

	@Test
	public void testSetAltitude() {
		test_aircraft.setAltitude(5000);
		assertEquals(test_aircraft.getPosition().z(), 5000, 0);

		test_aircraft.setAltitude(10000);
		assertEquals(test_aircraft.getPosition().z(), 10000, 0);

	}

	@Test
	public void testGetPoints() {
		assertEquals(test_aircraft.getNumPoints(), 20, 0);
	}

	@Test
	public void testGetDestination() {
		assertTrue(test_aircraft.getFlightPlan().getDestination().equals(location_waypoints[1]));
	}

}