package com.rg.gr.demo.controller;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rg.gr.demo.service.GRDataMigrationReportService;

@RestController
@RequestMapping("/grDataMigration")
@ControllerAdvice
public class GRDataMigrationReportController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GRDataMigrationReportController.class);

	@Autowired
	private GRDataMigrationReportService grDataMigrationReportService;

	@RequestMapping(value = "/generateReports", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String generateDataMigrationReports() throws JsonProcessingException, SQLException {
		LOGGER.info("GRDataMigrationReportController:generateDataMigrationReports- called");
		return grDataMigrationReportService.generateDataMigrationReports();
	}
}
