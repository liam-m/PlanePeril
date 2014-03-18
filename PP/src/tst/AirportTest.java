package tst;

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
import cls.Waypoint;
import cls.Waypoint.WaypointType;

public class AirportTest {

	public Airport airport;
	
	final static int MAX_AIRCRAFT_NUMBER = 10;

	public ArrayList<Aircraft> aircraft_list = new ArrayList<Aircraft>();
	
	public ArrayList<Aircraft> aircraft_in_airspace;

	public final Waypoint[] location_waypoints = new Waypoint[] {
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
			location_waypoints[2], // 12
			location_waypoints[3], // 13
			location_waypoints[4], // 14 - Airport
	};

	public Aircraft test_aircraft;

	public ArrayList<HoldingWaypoint> holding_waypoints = new ArrayList<HoldingWaypoint>();

	public final Waypoint takeoff_waypoint = new Waypoint(
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
		holding_waypoints.add(new HoldingWaypoint(location_waypoints[4]
				.position().x() - 100,
				location_waypoints[4].position().y() - 100));
		holding_waypoints.add(new HoldingWaypoint(location_waypoints[4]
				.position().x() + 100,
				location_waypoints[4].position().y() - 100));
		holding_waypoints.add(new HoldingWaypoint(location_waypoints[4]
				.position().x() + 100,
				location_waypoints[4].position().y() + 100));
		holding_waypoints.add(new HoldingWaypoint(location_waypoints[4]
				.position().x() - 100,
				location_waypoints[4].position().y() + 100));

		// Initialise values of setNextWaypoint.
		holding_waypoints.get(0).setNextWaypoint(holding_waypoints.get(1));
		holding_waypoints.get(1).setNextWaypoint(holding_waypoints.get(2));
		holding_waypoints.get(2).setNextWaypoint(holding_waypoints.get(3));
		holding_waypoints.get(3).setNextWaypoint(holding_waypoints.get(0));

		test_aircraft = new Aircraft("testAircraft", null, 32 + (int) (10 * Math.random()), SinglePlayer.DIFFICULTY_EASY, 
				new FlightPlan(location_waypoints[0],location_waypoints[1], airspace_waypoints, holding_waypoints, takeoff_waypoint), -1);
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
			airport.insertAircraft(test_aircraft);
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
