package com.kikaz.project.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table
public class Company {
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	private Long cid;
	private String cname;
	private String ctel;
	private String caddr;
	private String time;
}
