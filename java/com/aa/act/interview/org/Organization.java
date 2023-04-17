package com.aa.act.interview.org;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Organization {

	private Position root;
	// to produce unique employee IDs, starting at 1 in case the data ends up in a
	// structure where is a flag or null value or similar.
	protected AtomicInteger employeeIdGenerator = new AtomicInteger(1);

	public Organization() {
		root = createOrganization();
	}

	protected abstract Position createOrganization();

	/**
	 * hire the given person as an employee in the position that has that title
	 * 
	 * This implementation will also check if matching positions are filled or not
	 * and will not overwrite existing employees.
	 * 
	 * @param person
	 * @param title
	 * @return the newly filled position or empty if no position has that title
	 */
	public Optional<Position> hire(Name person, String title) {
		List<Position> possibilities = findPosition(title);
		if (possibilities.isEmpty()) {
			return Optional.empty();
		}

		// Iterating through the results, attempting to put the candidate in the first
		// unfilled position. This can result in some imprecision. One thing that might
		// help with this was if the Position that the candidate would be a direct
		// report of were included in the arguments.
		for (Position toCheck : possibilities) {
			if (!toCheck.isFilled()) {
				toCheck.setEmployee(Optional.of(new Employee(employeeIdGenerator.getAndIncrement(), person)));
				return Optional.of(toCheck);
			}
		}
		return Optional.empty();
	}

	/**
	 * It is possible that multiple instances of a title will exist (multiple VPs
	 * might each have one "Middle Manager", or a VP Sales might have many
	 * salespeople), so this will attempt to find all of them.
	 * 
	 * @param title
	 * @return A list of all positions found that match the given title, empty list
	 *         if none are found.
	 */
	protected List<Position> findPosition(String title) {
		List<Position> result = new ArrayList<>();
		findPositionHelper(title, root, result);
		return result;
	}

	/**
	 * Helper method to check nodes recursively
	 * 
	 * @param title
	 * @param toCheck
	 */
	protected static void findPositionHelper(String title, Position toCheck, List<Position> result) {
		// shouldn't happen, preparing anyway
		if (toCheck == null || title == null) {
			return;
		}
		// really shouldn't happen, but it's defined in this class, it should be handled
		// in this class
		if (result == null) {
			throw new IllegalArgumentException("List to hold results should not be null");
		}

		if (toCheck.getTitle().equals(title)) {
			result.add(toCheck);
		}

		toCheck.getDirectReports().forEach(position -> findPositionHelper(title, position, result));
	}

	@Override
	public String toString() {
		return printOrganization(root, "");
	}

	/**
	 * Recursively prints out the given position and every position listed as a
	 * direct report or report-of-a-report, etc. Successively indents each level.
	 * 
	 * @param pos
	 *            The position to use as a root node
	 * @param prefix
	 *            The string to prepend to listed entries
	 */
	private String printOrganization(Position pos, String prefix) {
		StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
		for (Position p : pos.getDirectReports()) {
			sb.append(printOrganization(p, prefix + "\t"));
		}
		return sb.toString();
	}
}
