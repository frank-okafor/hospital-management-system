package com.hospital.management.controller;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.management.data.ServiceResponse;
import com.hospital.management.model.Patient;
import com.hospital.management.model.Staff;
import com.hospital.management.service.HospitalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hospital/")
@RequiredArgsConstructor
public class HospitalController {
	private final HospitalService hospitalService;

	@PostMapping(value = "create-staff", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse<Staff>> createNewStaf(
			@RequestParam(value = "name", required = true) String name) {
		ServiceResponse<Staff> response = hospitalService.createNewStaf(name);
		return new ResponseEntity<>(response, response.getStatus());
	}

	@PutMapping(value = "update-staff", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse<Staff>> updateExistingStaff(@RequestHeader("staffId") String staffId,
			@RequestParam(value = "name", required = true) String name) {
		ServiceResponse<Staff> response = hospitalService.updateExistingStaff(staffId, name);
		return new ResponseEntity<>(response, response.getStatus());
	}

	@GetMapping(value = "getPatientsByAgeRange", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse<Page<Patient>>> getPatientsByAgeRange(
			@RequestHeader("staffId") String staffId,
			@RequestParam(value = "ageRange", required = true, defaultValue = "2") int ageRange,
			@RequestParam(value = "pageNumber", required = true, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = true, defaultValue = "10") int pageSize) {
		ServiceResponse<Page<Patient>> response = hospitalService.getPatientsByAgeRange(staffId, ageRange, pageNumber,
				pageSize);
		return new ResponseEntity<>(response, response.getStatus());
	}

	@GetMapping(value = "downloadPatientDetail", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStream> downloadPatientDetail(@RequestHeader("staffId") String staffId,
			@RequestParam(value = "patientId", required = true) Long patientId) {
		return hospitalService.downloadPatientDetail(staffId, patientId);
	}

	@DeleteMapping("delete-patients")
	public ResponseEntity<ServiceResponse<List<Patient>>> deletePatientProfileByDateRange(
			@RequestHeader("staffId") String staffId,
			@DateTimeFormat(pattern = "dd/MM/yyyy") @RequestParam(value = "fromDate", required = true) LocalDate fromDate,
			@DateTimeFormat(pattern = "dd/MM/yyyy") @RequestParam(value = "toDate", required = true) LocalDate toDate) {
		ServiceResponse<List<Patient>> response = hospitalService.deletePatientProfileByDateRange(staffId, fromDate,
				toDate);
		return new ResponseEntity<>(response, response.getStatus());
	}
}
