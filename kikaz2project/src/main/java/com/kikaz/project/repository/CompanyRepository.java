package com.kikaz.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kikaz.project.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>{
	
	
}
