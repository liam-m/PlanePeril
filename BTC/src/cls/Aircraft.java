package cls;

import lib.jog.graphics;
import lib.jog.input;

import scn.Demo;

public class Aircraft {

	public final static int SEPARATION_RULE = 32;
	public final static int RADIUS = 16;
	public final static int MOUSE_LENIANCY = 16;
	
	private double _speed;
	private double _turnSpeed = Math.PI / 2;
	
	private Vector _position;
	private Vector _velocity;
	private boolean _manualControl;
	private String _flightName;
	private Vector _target;
	private String _originName, _destinationName;
	private Waypoint[] _route;
	private int _routeStage;
	private Vector _destination;
	private graphics.Image _image;
	private boolean _finished;

	//Constructor
	public Aircraft(String flightName, String originName, String destinationName, Vector origin, Vector destination, graphics.Image image, double speed) {
		_flightName = flightName;
		_destinationName = destinationName;
		_originName = originName;
		_route = findRoute(origin, destination);
		_target = _route[0].position();
		_image = image;
		_speed = speed;
		_position = origin;
		_destination = destination;
		_manualControl = false;
		double x = _target.x() - _position.x();
		double y = _target.y() - _position.y();
		_velocity = new Vector(x, y, 0).normalise().mul(speed);
		_finished = false;
		_routeStage = 0;
	}
	
	@Override
	public String toString() {
		String repr = "";
		repr += "<Aircraft> (" + _flightName + ")\n";
		repr += "------------------\n";
		repr += "Origin:\t\t\t" + _originName + " (" + _position.x() + ", " + _position.y() + ")\n";
		repr += "Destination:\t\t" + _destinationName + " (" + _destination.x() + ", " + _destination.y() + ")\n";
		repr += "Flight Path:\t\t(" + _target.x() + ", " + _target.y() + "),\n";
		for (int i = 1; i < _route.length; i ++) {
			repr += "\t\t\t(" + _route[i].position().x() + ", " + _route[i].position().y() + "),\n";
		}
		return repr;
	}
	
	public Vector position() {
		return _position;
	}
	
	public String name () {
		return _flightName;
	}
	
	public boolean isFinished() {
		return _finished;
	}
	
	public boolean isManuallyControlled() {
		return _manualControl;
	}
	
	public int flightPathContains(Waypoint waypoint) {
		int index = -1;
		for (int i = 0; i < _route.length; i ++) {
			if (_route[i] == waypoint) index = i;
		}
		return index;
	}
	
	public void alterPath(int routeStage, Waypoint newWaypoint) {
		_route[routeStage] = newWaypoint;
		if (!_manualControl) resetBearing();
	}
	
	public boolean isMouseOver(int mx, int my) {
		double dx = _position.x() - mx;
		double dy = _position.y() - my;
		return dx*dx + dy*dy < MOUSE_LENIANCY*MOUSE_LENIANCY;
	}
	public boolean isMouseOver() { return isMouseOver(input.mouseX(), input.mouseY()); }
	
	public void update(double dt) {
		if (_finished) return;

		Vector dv = _velocity.mul(dt);
		_position = _position.add(dv);
		
		if (_manualControl) {
			double angle = 0;
			if (input.isKeyDown(input.KEY_RIGHT)) {
				angle += dt;
			}
			if (input.isKeyDown(input.KEY_LEFT)) {
				angle -= dt;
			}
			if (angle != 0) turnBy(angle * _turnSpeed);
			return;
		}
		
		Vector oldTarget = _target;
		if (isAt(_target) && _target.equals(_destination)) {
			_finished = true;
		} else if (isAt(_target) && (_routeStage == _route.length-1)) {
			_routeStage ++;
			_target = _destination;
		} else if (isAt(_target)) {
			_routeStage ++;
			_target = _route[_routeStage].position();
		}
		if (oldTarget != _target) {
			turnTowards(_target.x(), _target.y());
		}
	}
	
	public void turnBy(double angle) {
		double cosA = Math.cos(angle);
		double sinA = Math.sin(angle);
		double x = _velocity.x();
		double y = _velocity.y();
		_velocity = new Vector(x*cosA - y*sinA, y*cosA + x*sinA, _velocity.z());
	}

	private void turnTowards(double tx, double ty) {
		double x = tx - _position.x();
		double y = ty - _position.y();
		_velocity = new Vector(x, y, 0).normalise().mul(_speed);
	}
	
	public void draw() {
		graphics.setColour(128, 128, 128);
		graphics.draw(_image, _position.x(), _position.y(), bearing(), 8, 8);
		graphics.setColour(128, 128, 128, 96);
		graphics.print(String.valueOf(altitude()), _position.x()+8, _position.y()-8);
		graphics.setColour(0, 128, 128);
		graphics.circle(false, _position.x(), _position.y(), SEPARATION_RULE);
		graphics.setColour(128, 0, 0);
		graphics.circle(false, _position.x(), _position.y(), RADIUS);
	}
	
	private double altitude() {
		return _position.z();
	}

	public void drawFlightPath() {
		graphics.setColour(0, 128, 128);
		if (_target != _destination) {
			graphics.line(_position.x(), _position.y(), _route[_routeStage].position().x(), _route[_routeStage].position().y());
		}
		for (int i = _routeStage; i < _route.length-1; i++) {
			graphics.line(_route[i].position().x(), _route[i].position().y(), _route[i+1].position().x(), _route[i+1].position().y());	
		}
		if (_target == _destination) {
			graphics.line(_position.x(), _position.y(), _destination.x(), _destination.y());
		} else {
			graphics.line(_route[_route.length-1].position().x(), _route[_route.length-1].position().y(), _destination.x(), _destination.y());
		}
	}
	
	public double bearing() {
		double a = Math.acos( _velocity.x() / Math.sqrt(Math.pow(_velocity.x(), 2) + Math.pow(_velocity.y(), 2)) );
		if (_velocity.y() < 0) a *= -1;
		return a;
	}
	
	public boolean isAt(Vector point) {
		double dy = point.y() - _position.y();
		double dx = point.x() - _position.x();
		return dy*dy + dx*dx < 16;
	}
	
	private Waypoint[] findRoute(Vector origin, Vector destination) {
		// Placeholder over-simplified version
		int n = 4;
		Waypoint[] route = new Waypoint[n];
		for (int i = 0; i < n; i ++ ) {
			route[i] = Demo._waypoints[(int)( Math.random() * Demo._waypoints.length )];
		}
		return route;
	}

	public void updateCollisions(double dt, scn.Demo scene) {
		for (Aircraft plane : scene.aircraftList()) {
			if (plane != this && isWithin(plane, RADIUS)) {
				scene.gameOver(this, plane);
				_finished = true;
			} else if (plane != this && isWithin(plane, SEPARATION_RULE)) {
				scene.main().score().addTimeViolated(dt);
			}
		}
	}
	
	private boolean isWithin(Aircraft aircraft, int distance) {
		double dx = aircraft.position().x() - _position.x();
		double dy = aircraft.position().y() - _position.y();
		return dx*dx + dy*dy < distance*distance;
	}

	public void toggleManualControl() {
		_manualControl = !_manualControl;
		if (!_manualControl) resetBearing();
	}
	
	private void resetBearing() {
		if (_routeStage < _route.length) {
			_target = _route[_routeStage].position();
		}
		turnTowards(_target.x(), _target.y());
	}
	
}
