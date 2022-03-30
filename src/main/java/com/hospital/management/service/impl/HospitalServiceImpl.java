package com.hospital.management.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.hospital.management.data.ServiceResponse;
import com.hospital.management.exception.ServiceCustomException;
import com.hospital.management.model.Patient;
import com.hospital.management.model.Staff;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.StaffRepository;
import com.hospital.management.service.HospitalService;
import com.hospital.management.utils.AppUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {
	private final PatientRepository patientRepository;
	private final StaffRepository staffRepository;

	@Override
	public ServiceResponse<Staff> createNewStaf(String name) {
		return new ServiceResponse<Staff>(HttpStatus.OK, "staff created successfully",
				staffRepository.save(Staff.builder().name(name).build()));
	}

	@Override
	public ServiceResponse<Staff> updateExistingStaff(String staffId, String name) {
		if (!validateStaff(staffId)) {
			throw new ServiceCustomException(HttpStatus.BAD_REQUEST, "invalid staff id");
		}
		Staff staff = staffRepository.findByStaffId(UUID.fromString(staffId)).get();
		staff.setName(name);
		return new ServiceResponse<Staff>(HttpStatus.OK, "staff update successfully", staffRepository.save(staff));
	}

	@Override
	public ServiceResponse<Page<Patient>> getPatientsByAgeRange(String staffId, int ageRange, int pageNumber,
			int pageSize) {
		if (!validateStaff(staffId)) {
			throw new ServiceCustomException(HttpStatus.BAD_REQUEST, "invalid staff id");
		}
		Page<Patient> response = patientRepository.findByAgeRange(ageRange, AppUtils.getPage(pageNumber, pageSize));
		return new ServiceResponse<Page<Patient>>(HttpStatus.OK, "patients retrieved successfull", response);
	}

	@Override
	public ResponseEntity<InputStream> downloadPatientDetail(String staffId, Long patientId) {
		if (!validateStaff(staffId)) {
			throw new ServiceCustomException(HttpStatus.BAD_REQUEST, "invalid staff id");
		}
		return patientRepository.findById(patientId).map(patient -> {
			String fileName = "patient-report.csv";
			byte[] data = AppUtils.generateExcelFile(patient, fileName);
			String headerValue = String.format("attachment; filename=\"%s\"", fileName);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
			headers.add("Content-Disposition", headerValue);
			headers.add("Content-Type", "application/octet-stream");
			InputStream myInputStream = new ByteArrayInputStream(data);
			return new ResponseEntity<InputStream>(myInputStream, headers, HttpStatus.OK);
		}).orElseThrow(() -> new ServiceCustomException(HttpStatus.NOT_FOUND,
				"patient with id " + patientId + " does not exist"));
	}

	@Override
	public ServiceResponse<List<Patient>> deletePatientProfileByDateRange(String staffId, LocalDate fromDate,
			LocalDate toDate) {
		if (!validateStaff(staffId)) {
			throw new ServiceCustomException(HttpStatus.BAD_REQUEST, "invalid staff id");
		}
		List<Patient> result = patientRepository.findByDateCreatedBetween(fromDate.atStartOfDay(),
				toDate.atTime(23, 59, 59));
		patientRepository.deleteAll(result);
		return new ServiceResponse<List<Patient>>(HttpStatus.OK, "patients deleted successfull", result);
	}

	public boolean validateStaff(String staffId) {
		return staffRepository.existsStaffByStaffId(UUID.fromString(staffId));
	}

}
