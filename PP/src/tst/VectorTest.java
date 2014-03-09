package tst;

import static org.junit.Assert.*;

import org.junit.Test;

import cls.Vector;

public class VectorTest {		
	

	@Test 
	public void testGetX() {
		Vector test_vector = new Vector(1.0, 1.1, 1.2);
		assertTrue("x = 1.0", 1.0 == test_vector.x());
	}
	
	@Test 
	public void testGetY() {
		Vector test_vector = new Vector(1.0, 1.1, 1.2);
		assertTrue("y = 1.1", 1.1 == test_vector.y());
		
	}
	
	@Test 
	public void testGetZ() {
		Vector test_vector = new Vector(1.0, 1.1, 1.2);
		assertTrue("z = 1.2", 1.2 == test_vector.z());		
	}
	
	@Test 
	public void testMagnitude() {
		Vector test_vector = new Vector(1.0, 2.0, 2.0);
		assertTrue("Magnitude = 3", 3.0 == test_vector.magnitude());	
	}
	@Test 
	public void testMagnitude2() {
		Vector test_vector = new Vector(12, 16, 21);
		assertTrue("Magnitude = 29", 29 == test_vector.magnitude());	
	}
	
	@Test 
	public void testMagnitudeSquared() {
		Vector test_vector = new Vector(1.0, 2.0, 2.0);
		assertTrue("Magnitude = 9", 9.0 == test_vector.magnitudeSquared());	
	}
	@Test 
	public void testMagnitudeSquared2() {
		Vector test_vector = new Vector(12, 16, 21);
		assertTrue("Magnitude = 841", 841 == test_vector.magnitudeSquared());	
	}
	
	@Test 
	public void testEquals() {
		Vector test_vector = new Vector(1.9, 2.2, 7.4);
		Vector test_vector2 = new Vector(1.9, 2.2, 7.4);
		assertTrue("Equals = true", test_vector.equals(test_vector2));	
	}
	@Test 
	public void testEquals2() {
		Vector test_vector = new Vector(9, 4.2, 5.1);
		Vector test_vector2 = new Vector(9.0, 4.2, 5);
		assertTrue("Equals = false", !test_vector.equals(test_vector2));	
	}
	
	@Test 
	public void testAddition() {
		Vector test_vector = new Vector(2.0, 2.0, 4.0);
		Vector test_vector2 = new Vector(1.0, 3.0, 2.0);
		Vector result_vector = test_vector.add(test_vector2);
		assertTrue("Result =  3.0, 4.0, 6.0", (3.0 == result_vector.x()) && (5.0 == result_vector.y()) && (6.0 == result_vector.z()));	
	}
	@Test 
	public void testAddition2() {
		Vector test_vector = new Vector(6.0, 8.1, 16);
		Vector test_vector2 = new Vector(1.0, 2.0, 3.0);
		Vector result_vector = test_vector.add(test_vector2);
		assertTrue("Result =  7.0, 10.1, 19.0", (7.0 == result_vector.x()) && (10.1 == result_vector.y()) && (19.0 == result_vector.z()));	
	}
	
	@Test 
	public void testSubtraction() {
		Vector test_vector = new Vector(2.0, 3.0, 4.0);
		Vector test_vector2 = new Vector(1.0, 1.0, 2.0);
		Vector result_vector = test_vector.sub(test_vector2);
		assertTrue("Result = 1.0, 2.0, 2.0", (1.0 == result_vector.x()) && (2.0 == result_vector.y()) && (2.0 == result_vector.z()));	
	}
	@Test 
	public void testSubtraction2() {
		Vector test_vector = new Vector(14.0, 6, 100);
		Vector test_vector2 = new Vector(1.0, 6.0, 0);
		Vector result_vector = test_vector.sub(test_vector2);
		assertTrue("Result = 13.0, 0, 100.0", (13.0 == result_vector.x()) && (0 == result_vector.y()) && (100.0 == result_vector.z()));	
	}
	
	@Test
	public void testScaleBy(){
		Vector test_vector = new Vector(1, 2, 3);
		Vector result_vector = test_vector.scaleBy(1.0);
		assertTrue("ScaledBy = (1 , 2, 3)",  (1 == result_vector.x()) && (2 == result_vector.y()) && (3 == result_vector.z()));
	}
	@Test
	public void testScaleBy2(){
		Vector test_vector = new Vector(1, 2, 3);
		Vector result_vector = test_vector.scaleBy(-2.0);
		assertTrue("ScaledBy = (-2 , -4, -6)",  (-2 == result_vector.x()) && (-4 == result_vector.y()) && (-6 == result_vector.z()));
	}
	
	@Test 
	public void testNormalise() {
		Vector test_vector = new Vector(1.0, 2.0, 2.0);
		Vector result_vector = test_vector.normalise();
		assertTrue("Normalise = 1/3, 2/3, 2/3",  (1 == (result_vector.x()* 3)) && (2 == (result_vector.y()*3)) && (2 == (result_vector.z()*3)));
		
	}
	@Test 
	public void testNormalise2() {
		Vector test_vector = new Vector(1, 4, 8);
		Vector result_vector = test_vector.normalise();
		assertTrue("Normalise = 1/9, 4/9, 8/9",  (1 == (result_vector.x()*9)) && (4 == (result_vector.y()*9)) && (8 == (result_vector.z()*9)));	
	}
	
	@Test 
	public void testAngleBetween() {
		Vector test_vector = new Vector(1, 0, 0);
		Vector test_vector2 = new Vector(0, 1, 0);
		double angle = Math.PI / 2;
		assertTrue("Angle = pi/2", angle  ==  test_vector.angleBetween(test_vector2));	
	}
}
