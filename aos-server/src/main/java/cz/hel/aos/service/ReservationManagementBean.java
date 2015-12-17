package cz.hel.aos.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.security.auth.login.CredentialException;

import cz.hel.aos.entity.Flight;
import cz.hel.aos.entity.Reservation;
import cz.hel.aos.entity.Reservation.ReservationState;
import cz.hel.aos.entity.dto.ReservationDTO;
import cz.hel.aos.util.HashProvider;

@Stateless
public class ReservationManagementBean implements ReservationManagement {

	@PersistenceContext
	private EntityManager em;

	@EJB
	private FlightManagement flightManagement;

	@Override
	public List<ReservationDTO> getAllReservations() {
		TypedQuery<Reservation> q = em.createNamedQuery("Reservation.findAll",
				Reservation.class);

		List<Reservation> resultList = q.getResultList();
		List<ReservationDTO> dtos = new ArrayList<>();
		for (Reservation res : resultList) {
			dtos.add(new ReservationDTO(res));
		}
		return dtos;
	}

	@Override
	public ReservationDTO getReservation(Long id, String password)
			throws CredentialException {
		Reservation r = em.find(Reservation.class, id);

		checkPasswordsMatch(password, r);

		return new ReservationDTO(r);
	}

	@Override
	public ReservationDTO createReservation(ReservationDTO reservation) {
		Reservation r = new Reservation();
		r.setNumberOfSeats(reservation.getNumOfSeats());
		r.setState(reservation.getState());

		Flight flight = em.find(Flight.class, reservation.getFlightId());
		if (flight == null) {
			throw new EJBException("Cannot book nonexistent flight "
					+ reservation.getFlightId());
		}
		r.setFlight(flight);
		r.setState(ReservationState.NEW);
		r.setCreatedAt(new Date());

		String generated = generatePassword();
		r.setPassword(HashProvider.hash(generated));

		ReservationDTO newRes = new ReservationDTO(em.merge(r));
		newRes.setPassword(generated);

		return newRes;
	}

	private String generatePassword() {
		return UUID.randomUUID().toString().substring(0, 8);
	}

	@Override
	public ReservationDTO updateReservation(ReservationDTO reservation,
			String password) throws CredentialException {

		Reservation r = em.find(Reservation.class, reservation.getId());
		checkPasswordsMatch(password, r);
		r.setNumberOfSeats(reservation.getNumOfSeats());
		
		ReservationState updateState = reservation.getState();
		if (ReservationState.PAID.equals(updateState) 
				&& !ReservationState.PAID.equals(r.getState())) {
			throw new EJBException("Cannot update state to PAID without payment.");
		}
		r.setState(updateState);
		return new ReservationDTO(em.merge(r));
	}

	@Override
	public void deleteReservation(Long id, String password)
			throws CredentialException {
		Reservation r = em.find(Reservation.class, id);
		if (r != null) {
			if (ReservationState.PAID.equals(r.getState())) {
				throw new EJBException("Already paid reservation cannot be deleted.");
			}
			checkPasswordsMatch(password, r);
			em.remove(r);
		}
	}
	
	@Override
	public void pay(Long id, String payment) {
		Reservation reservation = em.find(Reservation.class, id);
		if (reservation == null) {
			throw new EJBException("Reservation " + id + " does not exist.");
		}
		
		reservation.setState(ReservationState.PAID);
		reservation.setPayment(payment);
		em.merge(reservation);
	}

	private void checkPasswordsMatch(String password, Reservation r)
			throws CredentialException {
		if (!HashProvider.hashesMatch(r.getPassword(), password)) {
			throw new CredentialException();
		}
	}

}
