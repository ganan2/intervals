package com.kforce.merging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class MergeIntervals {

	private static final Logger log = Logger.getLogger(MergeIntervals.class.getName());

	/**
	 * This method removes null and negative elements from the list, uses list of Interval, returns list of Interval, throws NullPointerException and NumberFormatException.  
	 * 
	 * @param list
	 * @return
	 * @throws NullPointerException
	 * @throws NumberFormatException
	 */
	public List<Interval> removeNullAndNegativeIntervals(List<Interval> list) throws NullPointerException, NumberFormatException {
		try {
			if(list.contains(null)) {
				for(int i = 0; i < list.size(); i++) {
					if(list.get(i) == null) {
						list.remove(i);
					}
				}
			}
			
			list.forEach(e -> {
				if(e.getLowerBound() < 0 || e.getUpperBound() < 0) {
					log.info("Negative zipcode found and removed: " + e);
					list.remove(e);
					throw new NumberFormatException("Zipcodes must be non-negative.");
				}
			});
		} catch (Exception e) {
			log.info(e.toString() + " - " +  Level.WARNING);
		}
		
		return list;
	}
	
	/**
	 * This method uses list of Interval and returns a list of Interval with forward ranges.
	 * 
	 * @param list
	 * @return
	 */
	public List<Interval> getForwardRangedIntervalList(List<Interval> globalList) {
		List<Interval> list = removeNullAndNegativeIntervals(globalList);
		if (list != null && list.size() != 0) {
			
			for (Interval interval : list) {
				int temp = -1;
				if (interval.getLowerBound() > interval.getUpperBound()) {
					log.info("Before converting reverse range to forward range:" + interval);
					temp = interval.getLowerBound();
					interval.setLowerBound(interval.getUpperBound());
					interval.setUpperBound(temp);
					log.info("After converting reverse range to forward range:" + interval);
				}
			}
		}
	
		return list;
	}

	/**
	 * This method uses a list of Interval and returns sorted list of Interval.
	 * 
	 * @param list
	 * @return
	 */
	public List<Interval> getSortedIntervals(List<Interval> globalList) {
		List<Interval> list = removeNullAndNegativeIntervals(globalList);
		if (list != null && list.size() != 0) {
			List<Interval> localList = getForwardRangedIntervalList(list);
			
			Collections.sort(localList, new Comparator<Interval>() {

				public int compare(Interval i1, Interval i2) {
					return i1.getLowerBound() - i2.getLowerBound();
				}
			});

		}
			
		return list;
	}

	/**
	 * This method uses a list of Interval and returns merged intervals. 
	 * 
	 * @param list
	 * @return
	 */
	public List<Interval> getMergedIntervals(List<Interval> globalList) {
		List<Interval> localList = null;
		List<Interval> list = removeNullAndNegativeIntervals(globalList);
		if (list != null && list.size() != 0) {
			localList = new ArrayList<Interval>();
			Interval previous = list.get(0);
			for (Interval interval : list) {
				if (interval.getLowerBound() > previous.getUpperBound()) {
					localList.add(previous);
					previous = interval;
				} else {
					Interval newMerged = new Interval(previous.getLowerBound(),
							Math.max(previous.getUpperBound(), interval.getUpperBound()));
					previous = newMerged;
				}
			}
			localList.add(previous);
		}
		
		return localList;
	}

	/**
	 * This method uses a list of Interval, sorts, and returns the merged list
	 * of Interval.
	 * 
	 * @param list
	 * @return
	 */
	public List<Interval> merge(List<Interval> globalList) {
		List<Interval> list = removeNullAndNegativeIntervals(globalList);
		if (list == null || list.size() == 0) {
			return null;
		}
		
		List<Interval> sortedList = null;
		try {
			sortedList = getSortedIntervals(list);
		} catch (Exception e) {
			log.info(e.toString() + "-" + Level.SEVERE);
		}
		List<Interval> resultList = getMergedIntervals(sortedList);

		return resultList;
	}

	/**
	 * This is the main method, uses array of String, logs the initial list of Interval, and the sorted and merged list of Interval. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MergeIntervals solution = new MergeIntervals();

//		List<Interval> list = null;
		List<Interval> list = new ArrayList<>();
		list.add(new Interval(8000, 9000));
		list.add(new Interval(10000, 10000));
		list.add(new Interval(6000, 7000));
		list.add(new Interval(7200, 7800));
		list.add(new Interval(6200, 3000));
		list.add(new Interval(9000, 10000));
		list.add(new Interval(4000, 0));
		list.add(new Interval(-4000, -1));
		list.add(new Interval(-4000, -5));

		log.info("Initial list of intervals:" + list);
		@SuppressWarnings("rawtypes")
		List resultList = solution.merge(list);
		log.info("Merged list of intervals:" + resultList);
	}
}
