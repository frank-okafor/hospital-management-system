package com.hospital.management.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "patient")
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends BaseModel<Patient> implements Serializable {
	@Column(name = "name")
	private String name;
	@Column(name = "age")
	private int age;
	@Column(name = "last_visit_date", updatable = false)
	private LocalDateTime last_visit_date;

}
