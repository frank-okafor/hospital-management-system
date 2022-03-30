package com.hospital.management.controller;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.StaffRepository;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class HospitalControllerTest {

	private @Autowired MockMvc mockMvc;
	private @Autowired StaffRepository staffRepository;
	private @Autowired PatientRepository patientRepository;

	@BeforeEach
	void setUp() throws Exception {
		staffRepository.deleteAll();
		patientRepository.deleteAll();
	}

	@Test
	void testCreateNewStaf() throws Exception {
		fail("Not yet implemented");
	}

	@Test
	void testGetPatientsByAgeRange() throws Exception {
		fail("Not yet implemented");
	}

}
