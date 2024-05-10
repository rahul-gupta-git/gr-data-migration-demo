package com.rg.gr.demo.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.rg.gr.demo.model.DatabaseConfig;
import com.rg.gr.demo.model.EmployeData;

@Repository
public class DataMigrationCompratorRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataMigrationCompratorRepository.class);

	// perform database connection
	private Connection performDatabaseConnection(DatabaseConfig config) throws SQLException {
		return DriverManager.getConnection(
				"jdbc:postgresql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDbname(),
				config.getUser(), config.getPassword());
	}

	// Fetch records from a given table
	public List<EmployeData> fetchRecords(DatabaseConfig config, String tableName) throws SQLException {
		List<EmployeData> records = new ArrayList<>();
		Connection conn = performDatabaseConnection(config);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		while (rs.next()) {
			String id = rs.getString("id");
			Map<String, Object> columns = new HashMap<>();
			for (int i = 1; i <= columnCount; i++) {
				columns.put(rsmd.getColumnName(i), rs.getObject(i));
			}
			records.add(new EmployeData(id, columns));
		}
		rs.close();
		stmt.close();
		conn.close();
		return records;
	}
}
