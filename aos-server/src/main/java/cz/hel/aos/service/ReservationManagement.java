package cz.hel.aos.service;

import java.util.List;

import javax.ejb.Local;
import javax.security.auth.login.CredentialException;

import cz.hel.aos.entity.dto.ReservationDTO;

@Local
public interface ReservationManagement {

	public List<ReservationDTO> getAllReservations();

	public ReservationDTO getReservation(Long id, String password) throws CredentialException;

	public ReservationDTO createReservation(ReservationDTO reservation);

	public ReservationDTO updateReservation(ReservationDTO reservation, String password) throws CredentialException;

	public void deleteReservation(Long id);

	public void pay(Long id, String payment);
	
	public void printReservation(Long id, String email);

}
