package com.rg.gr.demo.model;

import java.util.Map;
import java.util.Objects;

public class EmployeData {

	private String id;
	private Map<String, Object> columns;

	public EmployeData(String id, Map<String, Object> columns) {
		this.id = id;
		this.columns = columns;
	}

	@Override
	public String toString() {
		return "EmployeData{id=" + id + ", columns=" + columns + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EmployeData data = (EmployeData) o;
		return columns.equals(this.columns);
	}

	@Override
	public int hashCode() {
		return Objects.hash(columns);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, Object> columns) {
		this.columns = columns;
	}
}
