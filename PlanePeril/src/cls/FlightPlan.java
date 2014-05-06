package cls;

import java.util.ArrayList;

import cls.Waypoint.WaypointType;

public class FlightPlan {	
	private Waypoint origin;
	private final Waypoint destination;

	private final Waypoint[] route;
	
	private Waypoint[] waypoints;
	private ArrayList<HoldingWaypoint> holding_waypoints;
	
	/**
	 * Generates a flightplan using the origin and the destination
	 * @param origin The first waypoint to start the route from
	 * @param destination The destination point
	 * @param waypoints All the waypoints in the screen
	 * @param holding_waypoints The holding waypoints used for circling
	 */
	public FlightPlan(Waypoint origin, Waypoint destination, Waypoint[] waypoints, ArrayList<HoldingWaypoint> holding_waypoints, Waypoint takeoff_waypoint) {
		this.origin = origin;
		this.destination = destination;
		this.waypoints = waypoints;
		this.holding_waypoints = holding_waypoints;
		this.route = this.generateGreedyRoute();
		
		// If origin is airport, use the takeoff waypoint as the first one
		if (this.getOrigin() instanceof Airport) {
			route[0] = takeoff_waypoint;
		}
	}
	
	public Waypoint[] getRoute() {
		return route;
	}

	/**
	 * Creates a sensible route from an origin to a destination from an array of
	 * waypoints. Waypoint costs are considered according to distance from
	 * current aircraft location Costs are further weighted by distance from
	 * waypoint to destination.
	 * @param origin the waypoint from which to begin.
	 * @param destination the waypoint at which to end.
	 * @param waypoints the waypoints to be used.
	 * @return a sensible route between the origin and the destination, using asensible amount of waypoint.
	 */
	private Waypoint[] generateGreedyRoute() {
		// to hold the route as we generate it.
		ArrayList<Waypoint> selectedWaypoints = new ArrayList<Waypoint>();


		// to track our position as we generate the route. Initialise to the start of the route
		Waypoint currentPos = origin;

		// to track the closest next waypoint
		double cost = Double.MAX_VALUE;
		Waypoint cheapest = null;

		// to track if the route is complete
		boolean atDestination = false;

		while (!atDestination) {
			for (Waypoint point : waypoints) {
				boolean skip = false;

				for (Waypoint routePoints : selectedWaypoints) {
					// check we have not already selected the waypoint if we have, skip evaluating the point
					// this protects the aircraft from getting stuck looping between points
					if (routePoints.position().equals(point.position())) {
						skip = true; // flag to skip
						break; // no need to check rest of list, already found a match.
					}
				}
				// do not consider the waypoint we are currently at or the origin
				// do not consider offscreen waypoints which are not the destination
				// also skip if flagged as a previously selected waypoint
				if (skip == true || point.position().equals(currentPos.position()) || 
						point.position().equals(origin.position()) || 
						(point.getType() == WaypointType.ENTRY_EXIT && (point.position().equals(destination.position()) == false))) {
					skip = false; // reset flag
					continue;
				} else {
					// get cost of visiting waypoint compare cost vs current; cheapest if smaller, replace
					if (point.getCost(currentPos) + 0.5 * Waypoint.getCostBetween(point, destination) < cost) {
						// cheaper route found, update
						cheapest = point;
						cost = point.getCost(currentPos) + 0.5 * Waypoint.getCostBetween(point, destination);
					}
				}

			} // end for - evaluated all waypoints

			// The cheapest waypoint must have been found
			assert cheapest != null : "The cheapest waypoint was not found";

			if (cheapest.position().equals(destination.position())) {
				// route has reached destination break out of while loop
				atDestination = true;
			}

			// update the selected route; consider further points in route from the position of the selected point
			// remove previous instances of a point
			selectedWaypoints.remove(cheapest);
			selectedWaypoints.add(cheapest);
			currentPos = cheapest;

			// resaturate cost for next loop
			cost = Double.MAX_VALUE;

		} // end while

		if (destination instanceof Airport) {
			Waypoint nearestHoldingWaypoint = holding_waypoints.get(3);
			double cheapestCost = holding_waypoints.get(0).getCost(origin);

			for (HoldingWaypoint holdingPoint : holding_waypoints) {
				if (holdingPoint.getCost(origin) < cheapestCost) {
					nearestHoldingWaypoint = holdingPoint;
					cheapestCost = holdingPoint.getCost(origin);
				}
			}

			selectedWaypoints.add(selectedWaypoints.size() - 1, nearestHoldingWaypoint);
		}
		
		return selectedWaypoints.toArray(new Waypoint[selectedWaypoints.size()]);
	}

	public Waypoint getOrigin() {
		return origin;
	}

	public Waypoint getDestination() {
		return destination;
	}

	public String getOriginName() {
		return origin.getName();
	}

	public String getDestinationName() {
		return destination.getName();
	}
}
