package tst;

import static org.junit.Assert.*;

import org.junit.Test;

import cls.PerformanceBar;

public class PerformanceBarTest {
	
	@Test
	
	public void testPerformanceBar() {
		// Initialization
		PerformanceBar test_performance_bar = new PerformanceBar();
		int current_value = test_performance_bar.getCurrentValue();
		int max_value = test_performance_bar.getMax();
		int min_value = 0;
		
		// Testing that changeValueBy changes value properly outside extremes
		test_performance_bar.changeValueBy(10);
		assertTrue((current_value + 10) == test_performance_bar.getCurrentValue());
		
		// Testing setValueTo works properly
		current_value = 30;
		test_performance_bar.setValueTo(current_value);
		assertTrue(current_value == test_performance_bar.getCurrentValue());
		
		// Testing setting value higher than maximum 
		
		test_performance_bar.setValueTo(max_value);
		test_performance_bar.changeValueBy(10);
		assertTrue(test_performance_bar.getCurrentValue() == max_value);
		test_performance_bar.setToMax();
		assertTrue(test_performance_bar.getCurrentValue() == max_value);
		
		// Testing setting value smaller than minimum
		test_performance_bar.setValueTo(min_value);
		assertTrue(test_performance_bar.getCurrentValue() == min_value);
		test_performance_bar.changeValueBy(-50);
		assertTrue(test_performance_bar.getCurrentValue() == min_value);
	}
}
