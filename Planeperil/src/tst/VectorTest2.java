package tst;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cls.Vector;

public class VectorTest2 {
	Vector test_vector = new Vector(1.0, 1.1, 1.2);
	
	public VectorTest2() {

	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testVector() {
		//fail("Not yet implemented");
	}

	@Test
	public void testX() {
		assertTrue("x = 1.0", 1.0 == test_vector.x());
	}

	@Test
	public void testY() {
		assertTrue("y = 1.1", 1.1 == test_vector.y());
	}

	@Test
	public void testZ() {
		assertTrue("z = 1.2", 1.2 == test_vector.z());
	}

	@Test
	public void testEqualsObject() {
		Vector test_vector1 = new Vector(9, 4.2, 5.1);
		Vector test_vector2 = new Vector(9.0, 4.2, 5);
		assertTrue("Equals = false", !test_vector1.equals(test_vector2));	
	}

	@Test
	public void testMagnitude() {
		Vector test_vector3 = new Vector(1.0, 2.0, 4.0);
		assertEquals("Magnitude = 3", 4.58, test_vector3.magnitude(), 0.01);
	}

	@Test
	public void testMagnitudeSquared() {
		Vector test_vector = new Vector(1.0, 2.0, 2.0);
		assertTrue("Magnitude = 9", 9.0 == test_vector.magnitudeSquared());	
	}

	@Test
	public void testNormalise() {
		Vector test_vector = new Vector(1, 2, 3);
		Vector result_vector = test_vector.normalise();
		assertTrue("Normalise = 1/9, 2/9, 3/9",  (1 == (result_vector.x()*test_vector.magnitude())) && (2 == (result_vector.y()*test_vector.magnitude())) && (3 == (result_vector.z()*test_vector.magnitude())));
	}

	@Test
	public void testScaleBy() {
		Vector test_vector = new Vector(1, 2, 3);
		Vector result_vector = test_vector.scaleBy(1.0);
		assertTrue("ScaledBy = (1 , 2, 3)",  (1 == result_vector.x()) && (2 == result_vector.y()) && (3 == result_vector.z()));
	}

	@Test
	public void testAdd() {
		Vector test_vector2 = new Vector(1, 2, 3);
		Vector result_vector2 = new Vector(2.0, 3.1, 4.2);
		assertEquals(test_vector2.add(test_vector), result_vector2);
		//fail("Not yet implemented");
	}

	@Test
	public void testSub() {
		Vector test_vector = new Vector(2, 4, 6);
		Vector test_vector2 = new Vector(1, 2, 3);
		Vector result_vector2 = new Vector(1, 2, 3);
		assertEquals(test_vector.sub(test_vector2), result_vector2);
	}

	@Test
	public void testAngleBetween() {
		Vector test_vector = new Vector(1, 0, 0);
		Vector test_vector2 = new Vector(0, 1, 0);
		double angle = Math.PI / 2;
		assertTrue("Angle = pi/2", angle  ==  test_vector.angleBetween(test_vector2));	
	}

	@Test
	public void testSetZ() {
		test_vector.setZ(1);
		assertEquals(test_vector.z(), 1, 0);
		//fail("Not yet implemented");
	}

}
