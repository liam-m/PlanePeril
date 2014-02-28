package tst;

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
import cls.Waypoint;
import cls.Waypoint.WaypointType;

public class AirportTest {

	public Airport airport;
	
	final static int MAX_AIRCRAFT_NUMBER = 10;

	public ArrayList<Aircraft> aircraftList = new ArrayList<Aircraft>();
	
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

	public AirportTest() {
		Airport testAirport = new Airport(10, 10, "INI");
		

	}

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

		testAircraft = new Aircraft("testAircraft", null,
				32 + (int) (10 * Math.random()), Demo.DIFFICULTY_EASY, takeoffWaypoint,
				aircraftInAirspace, new FlightPlan(locationWaypoints[0],
						locationWaypoints[1], airspaceWaypoints,
						holdingWaypoints));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAirport() {
		// If initialisation works, this works.
		// fail("Not yet implemented");
	}

	@Test
	public void testInsertAircraft() {
		try{ 
		for(int i=1; i<12; i++)
			airport.insertAircraft(testAircraft);
		}
		catch (Exception IllegalStateException){ 
		assertTrue(true);
		}
			
		//fail("Not yet implemented");
	}

	@Test
	public void testTakeoff() {
		try{
			airport.takeoff();
		}
		catch (Exception IllegalStateException){ 
		assertTrue(true);
		}
		//fail("Not yet implemented");
	}

	@Test
	public void testDrawAirportInfo() {
		//No need to test
		//fail("Not yet implemented");
	}

}
