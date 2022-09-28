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
public class Reservation {
	
	@Id
	private String rid;
	private Integer child_num;
	private Integer adult_num;
	private Date date;
	private Integer price;
	private String rname;
	private String rtel;
	private Long cid;
	@ManyToOne(fetch=FetchType.LAZY)
	   @JsonIgnore
	   private Company company;
}
