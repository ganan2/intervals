package com.kforce.merging;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class MergeIntervalsTests {

	private static final Logger log = Logger.getLogger(MergeIntervals.class.getName());

	List<Interval> list = null;

	List<Interval> resultList = null;

	MergeIntervals mergeIntervals = null;

	@Before
	public void setup() {
		list = new ArrayList<>();
		mergeIntervals = new MergeIntervals();
		resultList = new ArrayList<>();
	}

	@After
	public void tearDown() {
		resultList = null;
		mergeIntervals = null;
		list = null;
	}
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	public void reset() {
		tearDown();
		setup();
	}

	/**
	 * This method is used to test the method for forward range interval list for point interval, accepts nothing and returns nothing.
	 */
	@Test
	public void getForwardRangedIntervalListTestForPointIntervalAcceptsList() {
		Interval interval = new Interval(0, 0);
		list.add(interval);

		mergeIntervals = new MergeIntervals();
		resultList = mergeIntervals.getForwardRangedIntervalList(list);

		for (Interval localInterval : resultList) {
			Assert.assertEquals(0, localInterval.getLowerBound());
			Assert.assertEquals(0, localInterval.getUpperBound());
		}

		reset();
	}

	/**
	 * This method is used to test the method for forward range interval list for single positive interval, accepts nothing and returns nothing.
	 */
	@Test
	public void getForwardRangedIntervalListTestForSinglePositiveInterval() {
		list.add(new Interval(500, 1000));

		mergeIntervals = new MergeIntervals();
		resultList = mergeIntervals.getForwardRangedIntervalList(list);

		for (Interval localInterval : resultList) {
			Assert.assertEquals(500, localInterval.getLowerBound());
			Assert.assertEquals(1000, localInterval.getUpperBound());
		}

		reset();
	}

	/**
	 * This method is used to test the method for forward range interval list for negative interval, accepts nothing, returns nothing but throws NumberFormatException.
	 */
	@Test
	public void getForwardRangedIntervalListTestForSingleNegativeIntervalThrowsNumberFormatException() {
		list.add(new Interval(-500, -1000));

		try {
			resultList = mergeIntervals.getForwardRangedIntervalList(list);
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), NumberFormatException.class);
			Assert.assertEquals("Zipcodes must be non-negative.", e.getMessage());
			log.info(e.toString());
		}

		reset();
	}

	/**
	 * This method is used to test the method for sorted intervals list for positive intervals, accepts nothing and returns nothing.
	 */
	@Test
	public void getSortedIntervalsTestForPositiveIntervals() {

		list.add(new Interval(500, 1000));
		list.add(new Interval(750, 1000));
		list.add(new Interval(250, 800));

		try {
			resultList = mergeIntervals.getSortedIntervals(list);
		} catch (Exception e) {
			log.info(e.toString());
		}
		log.info(resultList.toString());

		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				Assert.assertEquals(250, list.get(i).getLowerBound());
				Assert.assertEquals(800, list.get(i).getUpperBound());
			} else if (i == 1) {
				Assert.assertEquals(500, list.get(i).getLowerBound());
				Assert.assertEquals(1000, list.get(i).getUpperBound());
			} else if (i == 2) {
				Assert.assertEquals(750, list.get(i).getLowerBound());
				Assert.assertEquals(1000, list.get(i).getUpperBound());
			}
		}

		reset();
	}
	
	/**
	 * This method is used to test the method for sorted intervals list for negative intervals, accepts nothing, returns nothing but throws NumberFormatException.
	 */
	@Test
	public void getSortedIntervalsTestForNegativeIntervalThrowsNumberFormatException() {

		list.add(new Interval(-500, 1000));

		try {
			resultList = mergeIntervals.getSortedIntervals(list);
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), NumberFormatException.class);
			Assert.assertEquals("Zipcodes must be non-negative.", e.getMessage());
			log.info(e.toString());
		}

		reset();
	}
	
	/**
	 * This method is used to test the method for sorted intervals list for point intervals, accepts nothing and returns nothing.
	 */
	@Test
	public void getSortedIntervalsTestForPointInterval() {

		list.add(new Interval(0, 0));

		resultList = mergeIntervals.getSortedIntervals(list);
		
		Assert.assertEquals(0, resultList.get(0).getLowerBound());
		Assert.assertEquals(0, resultList.get(0).getUpperBound());

		reset();
	}

	/**
	 * This method is used to test the method for merged intervals list for positive and overlapping intervals, accepts nothing and returns nothing.
	 */
	@Test
	public void getMergedIntervalsTestForOverlappingIntervals() {
		list.add(new Interval(500, 800));
		list.add(new Interval(500, 750));
		
		resultList = mergeIntervals.getMergedIntervals(list);
		
		Assert.assertEquals(500, list.get(0).getLowerBound());
		Assert.assertEquals(800, list.get(0).getUpperBound());
		
		reset();
	}
	
	/**
	 * This method is used to test the method for merged intervals list for positive and non-overlapping intervals, accepts nothing and returns nothing.
	 */
	@Test
	public void getMergedIntervalsTestForNonOverlappingIntervals() {
		list.add(new Interval(500, 800));
		list.add(new Interval(200, 300));
		
		resultList = mergeIntervals.getMergedIntervals(list);
		
		Assert.assertEquals(500, list.get(0).getLowerBound());
		Assert.assertEquals(800, list.get(0).getUpperBound());
		
		Assert.assertEquals(200, list.get(1).getLowerBound());
		Assert.assertEquals(300, list.get(1).getUpperBound());
		
		reset();
	}
	
	/**
	 * This method is used to test the method for merged intervals list for null intervals, accepts nothing and returns nothing.
	 */
	@Test
	public void getMergedIntervalsTestForNullIntervals() {
		list.add(null);
		
		resultList = mergeIntervals.getMergedIntervals(list);
		
		Assert.assertTrue(Boolean.valueOf(resultList == null));
		
		reset();
	}
	
	/**
	 * This method is used to test the method for merged intervals list for negative intervals, accepts nothing and returns nothing but throws NumberFormatException.
	 */
	@Test
	public void getMergedIntervalsTestForNegativeIntervalsThrowsNumberFormatException() {
		list.add(new Interval(-500, 1000));
		
		try {
			resultList = mergeIntervals.getMergedIntervals(list);
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), NumberFormatException.class);
			Assert.assertEquals("Zipcodes must be non-negative.", e.getMessage());
			log.info(e.toString());
		}
		
		reset();
	}
	
	/**
	 * This method is used to test the method for merged intervals list for point intervals, accepts nothing and returns nothing.
	 */
	@Test
	public void getMergedIntervalsTestForPointInterval() {
		list.add(new Interval(0, 0));
		
		resultList = mergeIntervals.getMergedIntervals(list);
		
		Assert.assertEquals(0, resultList.get(0).getLowerBound());
		Assert.assertEquals(0, resultList.get(0).getUpperBound());
		
		reset();
	}

	/**
	 * This method is used to test the method for removing null and negative intervals from list for point intervals, accepts nothing and returns nothing.
	 */
	@Test
	public void removeNullAndNegativeIntervalsTestForPointInterval() {
		list.add(new Interval(0, 0));
		
		resultList = mergeIntervals.getMergedIntervals(list);
		
		Assert.assertEquals(0, resultList.get(0).getLowerBound());
		Assert.assertEquals(0, resultList.get(0).getUpperBound());
		
		reset();
	}
	
	/**
	 * This method is used to test the method for removing null and negative intervals from list for negative intervals, accepts nothing and returns nothing but throws NumberFormatException.
	 */
	@Test
	public void removeNullAndNegativeIntervalsTestForNegativeIntervalThrowsNumberFormatException() {
		list.add(new Interval(-1, 10));
		
		try {
			resultList = mergeIntervals.getMergedIntervals(list);
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), NumberFormatException.class);
			Assert.assertEquals("Zipcodes must be non-negative.", e.getMessage());
			log.info(e.toString());
		}
		
		reset();
	}
	
	/**
	 * This method is used to test the method for removing null and negative intervals from list for positive intervals, accepts nothing and returns nothing.
	 */
	@Test
	public void removeNullAndNegativeIntervalsTestForPositiveInterval() {
		list.add(new Interval(200, 10));
		
		resultList = mergeIntervals.getMergedIntervals(list);
		
		Assert.assertEquals(200, resultList.get(0).getLowerBound());
		Assert.assertEquals(10, resultList.get(0).getUpperBound());
		
		reset();
	}
	
	/**
	 * This method is used to test the method for removing null and negative intervals from list for null intervals, accepts nothing and returns nothing but throws NullPointerException.
	 */
	@Test
	public void removeNullAndNegativeIntervalsTestForNullIntervalThrowsNullPointerException() {
		list.add(null);
		
		resultList = mergeIntervals.getMergedIntervals(list);
		
		try {
			resultList = mergeIntervals.getMergedIntervals(list);
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), NullPointerException.class);
			log.info(e.toString());
		}
		
		reset();
	}

	/**
	 * This method is the main method for running all the test cases in this class, accepts array of String and returns nothing.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(MergeIntervals.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
	}
}
