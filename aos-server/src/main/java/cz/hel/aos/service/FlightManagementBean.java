package cz.hel.aos.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cz.hel.aos.entity.Destination;
import cz.hel.aos.entity.Flight;
import cz.hel.aos.entity.dto.FlightDTO;
import cz.hel.aos.service.SimpleCriteria.SimpleOrderType;

@Stateless
public class FlightManagementBean implements FlightManagement {
	
	@PersistenceContext
	private EntityManager em;
	
	@EJB
	private DestinationManagement destinationManagement;

	@Override
	public List<FlightDTO> getAllFlights() {
		List<Flight> resultList = em.createNamedQuery("Flight.findAll", Flight.class).getResultList();
		
		List<FlightDTO> dtos = new ArrayList<>();
		for (Flight flight : resultList) {
			dtos.add(new FlightDTO(flight));
		}
		
		return dtos;
	}

	@Override
	public FlightDTO getFlight(Long id) {
		Flight flight = em.find(Flight.class, id);
		if (flight == null) {
			return null;
		}
		
		return new FlightDTO(flight);
	}

	@Override
	public FlightDTO createFlight(FlightDTO flight) {
		Flight f = new Flight();
		fillFlightProperties(flight, f);
		
		em.persist(f);
		
		return new FlightDTO(f);
	}

	@Override
	public FlightDTO updateFlight(FlightDTO flight) {
		Flight f = em.find(Flight.class, flight.getId());
		fillFlightProperties(flight, f);
		
		return new FlightDTO(em.merge(f));
	}
	
	@Override
	public void deleteFlight(Long id) throws ReferenceExistsException {
		Flight f = em.find(Flight.class, id);
		if (f != null) {
			try {
				em.remove(f);
			} catch (Exception e) {
				throw new ReferenceExistsException();
			}
		}
	}

	private void fillFlightProperties(FlightDTO flight, Flight f) {
		f.setDateOfDeparture(flight.getDateOfDepatrure());
		f.setDistance(flight.getDistance());
		
		Destination from = destinationManagement.getDestination(flight.getFrom());
		if (from == null) {
			throw new EJBException("Destination from '" + flight.getFrom() + "' does not exist.");
		}
		f.setFlightFrom(from);
		
		Destination to = destinationManagement.getDestination(flight.getTo());
		if (to == null) {
			throw new EJBException("Destination to '" + flight.getFrom() + "' does not exist.");
		}
		f.setFlightTo(to);
		f.setFlightName(flight.getFlightName());
		f.setPrice(flight.getPrice());
		f.setTotalSeats(flight.getSeats());
	}

	// tohle je hnuj vole, nejde to nejak lip?
	@Override
	public List<FlightDTO> getAllFlights(SimpleCriteria sc) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Flight> q = cb.createQuery(Flight.class);
		Root<Flight> root = q.from(Flight.class);

		ParameterExpression<Date> from = null;
		Predicate fp = null;
		if (sc.dateFrom != null) {
			from = cb.parameter(Date.class, "dateFrom");
			fp = cb.greaterThanOrEqualTo(root.<Date>get("dateOfDeparture"), from);
		}
		
		ParameterExpression<Date> to = null;
		Predicate tp = null;
		if (sc.dateTo != null) {
			to = cb.parameter(Date.class, "dateTo");
			tp = cb.lessThanOrEqualTo(root.<Date>get("dateOfDeparture"), to);
		}

		// timestampy
		if (fp != null && tp != null) {
			q.where(fp, tp);
		} else if (tp != null) {
			q.where(tp);
		} else if (fp != null) {
			q.where(fp);
		}
		
		// razeni
		if (sc.order != null) {
			if (sc.order.orderType.equals(SimpleOrderType.ASC)) {
				q.orderBy(cb.asc(root.get(sc.order.attrName)));
			} else {
				q.orderBy(cb.desc(root.get(sc.order.attrName)));
			}
		}
		
		// samotna query
		TypedQuery<Flight> fq = em.createQuery(q);
		if (fp != null) {
			fq.setParameter(from, sc.dateFrom, TemporalType.TIMESTAMP);
		}
		if (tp != null) {
			fq.setParameter(to, sc.dateTo, TemporalType.TIMESTAMP);
		}
		
		// strankovani
		if (sc.firstResult != null) {
			fq.setFirstResult(sc.firstResult);
			if (sc.offset != null) {
				fq.setMaxResults(sc.firstResult + sc.offset);
			}
		}
		
		List<Flight> resultList = fq.getResultList();
		List<FlightDTO> dtos = new ArrayList<>();
		for (Flight f : resultList) {
			dtos.add(new FlightDTO(f));
		}
		
		return dtos;
	}
	
}
