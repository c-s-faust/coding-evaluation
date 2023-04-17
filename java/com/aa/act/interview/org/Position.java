package com.aa.act.interview.org;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Position {

	private String title;
	private Optional<Employee> employee;

	/*
	 * If I understand correctly, Sets check whether an obect is already present
	 * based on equals and hashCode. These methods are not overridden here, so Java
	 * will use Object's equals and hashCode, which are based on memory addresses.
	 * This theoretically allows someone to add many Positions with identical titles
	 * as direct reports as long as they are newly-created obects and thus have
	 * different memory addresses.
	 * 
	 * If this is not the intended behaviour, equals and hashCode should be
	 * overridden, if it is the intended behaviour, perhaps this should be a list
	 * rather than a set.
	 */
	private Set<Position> directReports;

	public Position(String title) {
		this.title = title;
		employee = Optional.empty();
		directReports = new HashSet<Position>();
	}

	public Position(String title, Employee employee) {
		this(title);
		if (employee != null)
			setEmployee(Optional.of(employee));
	}

	public String getTitle() {
		return title;
	}

	public void setEmployee(Optional<Employee> employee) {
		this.employee = employee;
	}

	public Optional<Employee> getEmployee() {
		return employee;
	}

	public boolean isFilled() {
		return employee.isPresent();
	}

	public boolean addDirectReport(Position position) {
		if (position == null)
			throw new IllegalArgumentException("position cannot be null");
		return directReports.add(position);
	}

	public boolean removePosition(Position position) {
		return directReports.remove(position);
	}

	public Collection<Position> getDirectReports() {
		return Collections.unmodifiableCollection(directReports);
	}

	@Override
	public String toString() {
		return title + employee.map(e -> ": " + e.toString()).orElse("");
	}
}
