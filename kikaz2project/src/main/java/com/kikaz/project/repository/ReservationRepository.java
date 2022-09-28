package com.kikaz.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kikaz.project.model.Company;
import com.kikaz.project.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String>{
	Reservation findByrid(String rid);
	
	
}
