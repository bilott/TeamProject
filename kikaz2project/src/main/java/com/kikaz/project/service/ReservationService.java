package com.kikaz.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kikaz.project.model.Reservation;
import com.kikaz.project.repository.ReservationRepository;




@Service
public class ReservationService {

	private final ReservationRepository resRepository;
	
	public ReservationService(ReservationRepository resRepository) {
		this.resRepository=resRepository;
	}
	
	public Reservation findReservationByRid(String rid) {
		return resRepository.findByrid(rid);
	}
	
	@Transactional
	public void deleteByRid(String rid) {
		resRepository.deleteById(rid);
	}
}
