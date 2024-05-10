package com.rg.gr.demo.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rg.gr.demo.model.DatabaseConfig;
import com.rg.gr.demo.model.EmployeData;
import com.rg.gr.demo.repository.DataMigrationCompratorRepository;

@Service
public class GRDataMigrationReportService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GRDataMigrationReportService.class);

	private List<EmployeData> oldRecordData;
	private List<EmployeData> newRecordData;
	private List<EmployeData> missingRecords;
	private List<EmployeData> corruptedRecords;
	private List<EmployeData> newRecords;

	private static DatabaseConfig oldDatabaseConfig;
	private static DatabaseConfig newDatabaseConfig;
	private static String referenceTableName;

	@Autowired
	private DataMigrationCompratorRepository dataMigrationCompratorRepository;

	// Load the database configuration
	public GRDataMigrationReportService() {
		oldDatabaseConfig = new DatabaseConfig("localhost", 5432, "old", "old", "hehehe");
		newDatabaseConfig = new DatabaseConfig("localhost", 5433, "new", "new", "hahaha");
		referenceTableName = "accounts";
	}

	// Generate the reports after gathering all the data.
	public String generateDataMigrationReports() throws JsonProcessingException, SQLException {

		oldRecordData = dataMigrationCompratorRepository.fetchRecords(oldDatabaseConfig, referenceTableName);
		LOGGER.info("Total oldRecordData record count:" + oldRecordData.size());

		newRecordData = dataMigrationCompratorRepository.fetchRecords(newDatabaseConfig, referenceTableName);
		LOGGER.info("Total newRecordData record count:" + newRecordData.size());

		missingRecords = captureMissingRecords(oldRecordData, newRecordData);
		corruptedRecords = captureCorruptedRecords(oldRecordData, newRecordData);
		newRecords = captureNewRecords(oldRecordData, newRecordData);

		return generateJsonReport(missingRecords, corruptedRecords, newRecords);
	}

	// Identify missing records
	public List<EmployeData> captureMissingRecords(List<EmployeData> oldRecordData, List<EmployeData> newRecordData) {
		Set<String> oldIds = new HashSet<>();
		for (EmployeData r : oldRecordData)
			oldIds.add(r.getId());

		List<EmployeData> missing = new ArrayList<>();
		for (EmployeData r : oldRecordData) {
			if (!oldIds.contains(r.getId())) {
				missing.add(r);
			}
		}
		return missing;
	}

	// Identify corrupted records
	public List captureCorruptedRecords(List<EmployeData> oldRecordData, List<EmployeData> newRecordData) {
		Map<String, EmployeData> oldMap = new HashMap<>();
		for (EmployeData r : oldRecordData)
			oldMap.put(r.getId(), r);

		List corrupted = new ArrayList<>();
		for (EmployeData r : newRecordData) {
			if (oldMap.containsKey(r.getId()) && !oldMap.get(r.getId()).equals(r)) {
				Map details = new HashMap<>();
				details.put("id", r.getId());
				details.put("old", oldMap.get(r.getId()));
				details.put("new", r.getColumns());
				corrupted.add(details);
			}
		}
		return corrupted;
	}

	// Identify new records
	static List<EmployeData> captureNewRecords(List<EmployeData> oldRecordData, List<EmployeData> newRecordData) {
		Set<String> oldIds = new HashSet<>();
		for (EmployeData r : oldRecordData)
			oldIds.add(r.getId());

		List<EmployeData> newRecords = new ArrayList<>();
		for (EmployeData r : newRecordData) {
			if (!oldIds.contains(r.getId())) {
				newRecords.add(r);
			}
		}

		return newRecords;
	}

	// Generate a report
	private String generateJsonReport(List<EmployeData> missing, List<EmployeData> corrupted,
			List<EmployeData> newRecords) throws JsonProcessingException {
		Map<String, List<EmployeData>> report = new HashMap<>();
		report.put("missing_records", missing);
		report.put("corrupted_records", corrupted);
		report.put("new_records", newRecords);
		LOGGER.info("Total missing records-" + missing.size());
		LOGGER.info("Total corrupted records-" + corrupted.size());
		LOGGER.info("Total new records-" + newRecords.size());
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(report);
	}

}
