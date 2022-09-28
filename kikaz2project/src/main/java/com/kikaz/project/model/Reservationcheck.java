package com.kikaz.project.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
@Data
@Entity
@Table
public class Reservationcheck {
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	private Integer rcid;
	private Integer count;
	private Date date;
	
	@ManyToOne(fetch=FetchType.LAZY)
	   @JsonIgnore
	   private Company company;
}
