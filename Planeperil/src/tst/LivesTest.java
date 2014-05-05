package tst;

import static org.junit.Assert.*;
import org.junit.Test;
import cls.Lives;

@SuppressWarnings("deprecation")
public class LivesTest {

	@Test
	public void testLives() {
		Lives test_lives = new Lives();
		int lives = test_lives.getLives();
		assertTrue(lives == 3);
		
		test_lives.decrement();
		lives = test_lives.getLives();
		assertTrue(lives == 2);
		
		test_lives.decrement();
		lives = test_lives.getLives();
		assertTrue(lives == 1);
		
		test_lives.decrement();
		lives = test_lives.getLives();
		assertTrue(lives == 0);
		
		test_lives.decrement();
		lives = test_lives.getLives();
		assertTrue(lives == 0);
	}
}