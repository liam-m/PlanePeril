package tst;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cls.Vector;

public class VectorTest2 {
	Vector testVector = new Vector(1.0, 1.1, 1.2);
	
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
		assertTrue("x = 1.0", 1.0 == testVector.x());
	}

	@Test
	public void testY() {
		assertTrue("y = 1.1", 1.1 == testVector.y());
	}

	@Test
	public void testZ() {
		assertTrue("z = 1.2", 1.2 == testVector.z());
	}

	@Test
	public void testEqualsObject() {
		Vector testVector1 = new Vector(9, 4.2, 5.1);
		Vector testVector2 = new Vector(9.0, 4.2, 5);
		assertTrue("Equals = false", !testVector1.equals(testVector2));	
	}

	@Test
	public void testMagnitude() {
		Vector testVector3 = new Vector(1.0, 2.0, 4.0);
		assertTrue("Magnitude = 3", 3.0 == testVector3.magnitude());		
	}

	@Test
	public void testMagnitudeSquared() {
		Vector testVector = new Vector(1.0, 2.0, 2.0);
		assertTrue("Magnitude = 9", 9.0 == testVector.magnitudeSquared());	
	}

	@Test
	public void testNormalise() {
		Vector testVector = new Vector(1, 2, 3);
		Vector resultVector = testVector.normalise();
		assertTrue("Normalise = 1/9, 2/9, 3/9",  (1 == (resultVector.x()*testVector.magnitude())) && (2 == (resultVector.y()*testVector.magnitude())) && (3 == (resultVector.z()*testVector.magnitude())));
	}

	@Test
	public void testScaleBy() {
		Vector testVector = new Vector(1, 2, 3);
		Vector resultVector = testVector.scaleBy(1.0);
		assertTrue("ScaledBy = (1 , 2, 3)",  (1 == resultVector.x()) && (2 == resultVector.y()) && (3 == resultVector.z()));
	}

	@Test
	public void testAdd() {
		Vector testVector2 = new Vector(1, 2, 3);
		Vector resultVector2 = new Vector(2.0, 3.1, 4.2);
		assertEquals(testVector2.add(testVector), resultVector2);
		//fail("Not yet implemented");
	}

	@Test
	public void testSub() {
		Vector testVector = new Vector(2, 4, 6);
		Vector testVector2 = new Vector(1, 2, 3);
		Vector resultVector2 = new Vector(1, 2, 3);
		assertEquals(testVector.sub(testVector2), resultVector2);
	}

	@Test
	public void testAngleBetween() {
		Vector testVector = new Vector(1, 0, 0);
		Vector testVector2 = new Vector(0, 1, 0);
		double angle = Math.PI / 2;
		assertTrue("Angle = pi/2", angle  ==  testVector.angleBetween(testVector2));	
	}

	@Test
	public void testSetZ() {
		testVector.setZ(1);
		assertEquals(testVector.z(), 1, 0);
		//fail("Not yet implemented");
	}

}
