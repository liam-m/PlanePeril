package tst;

import static org.junit.Assert.*;

import org.junit.Test;

import cls.Vector;

public class VectorTest {		
	
	@Test // Test getX function
	public void testGetX() {
		Vector testVector = new Vector(1.0, 1.1, 1.2);
		assertTrue("x = 1.0", 1.0 == testVector.x());
	}
	
	@Test // Test getY function
	public void testGetY() {
		Vector testVector = new Vector(1.0, 1.1, 1.2);
		assertTrue("y = 1.1", 1.1 == testVector.y());
		
	}
	
	@Test // Test getZ function
	public void testGetZ() {
		Vector testVector = new Vector(1.0, 1.1, 1.2);
		assertTrue("z = 1.2", 1.2 == testVector.z());		
	}
	
	@Test // Test magnitude function
	public void testMagnitude() {
		Vector testVector = new Vector(1.0, 2.0, 2.0);
		assertTrue("Magnitude = 3", 3.0 == testVector.magnitude());	
	}
	
	@Test // Test magnitudeSquared function
	public void testMagnitudeSquared() {
		Vector testVector = new Vector(1.0, 2.0, 2.0);
		assertTrue("Magnitude = 9", 9.0 == testVector.magnitudeSquared());	
	}
	
	@Test // Test equals function
	public void testEquals() {
		Vector testVector = new Vector(1.9, 2.2, 7.4);
		Vector testVector2 = new Vector(1.9, 2.2, 7.4);
		assertTrue("Equals = true", testVector.equals(testVector2));	
	}
	
	@Test // Test addition function
	public void testAddition() {
		Vector testVector = new Vector(2.0, 2.0, 4.0);
		Vector testVector2 = new Vector(1.0, 3.0, 2.0);
		Vector resultVector = testVector.add(testVector2);
		assertTrue("Result =  3.0, 4.0, 6.0", (3.0 == resultVector.x()) && (5.0 == resultVector.y()) && (6.0 == resultVector.z()));	
	}
	
	@Test // Test subtraction function
	public void testSubtraction() {
		Vector testVector = new Vector(2.0, 3.0, 4.0);
		Vector testVector2 = new Vector(1.0, 1.0, 2.0);
		Vector resultVector = testVector.sub(testVector2);
		assertTrue("Result = 1.0, 2.0, 2.0", (1.0 == resultVector.x()) && (2.0 == resultVector.y()) && (2.0 == resultVector.z()));	
	}
	
	@Test // Test normalise function
	public void testNormalise() {
		Vector testVector = new Vector(1.0, 2.0, 2.0);
		Vector resultVector = testVector.normalise();
		// This is wrong
		assertTrue("Normalise = 1/3, 2/3, 2/3",  (1/3 == resultVector.x()) && (2/3 == resultVector.y()) && (2/3 == resultVector.z()));	
	}
	
	@Test // Test angle between function
	public void testAngle() {
		Vector testVector = new Vector(2.0, 2.0, 4.0);
		Vector testVector2 = new Vector(1.0, 3.0, 2.0);
		// This is wrong
		assertTrue("Angle = 0",  0 == testVector.angleBetween(testVector2));	
	}
}
