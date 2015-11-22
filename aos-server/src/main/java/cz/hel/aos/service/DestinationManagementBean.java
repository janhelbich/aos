package cz.hel.aos.service;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cz.hel.aos.entity.Destination;
import cz.hel.aos.util.StringUtils;

@Stateless
public class DestinationManagementBean implements DestinationManagement {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Destination> getAllDestinations() {
		return em.createNamedQuery("Destination.findAll", 
				Destination.class).getResultList();
	}

	@Override
	public Destination getDestination(String id) {
		return internalGetDestination(id);
	}

	@Override
	public Destination createDestination(Destination destination) {
		if (internalGetDestination(destination.getDestinationId()) != null) {
			throw new EJBException("Destination with ID "
					+ destination.getDestinationId() + " already exists.");
		}
		if (internalGetDestinationByName(destination.getDestinationName()) != null) {
			throw new EJBException("Destination with name "
					+ destination.getDestinationName() + " already exists.");
		}
		
		if (destination.getId() != null) {
			destination.setId(null);
		}
		
		em.persist(destination);
		
		return destination;
	}

	@Override
	public Destination updateDestination(Destination destination) {
		Destination d = em.find(Destination.class, destination.getId());
		if (d == null) {
			throw new EJBException("Destination with ID "
					+ destination.getId() + " does not exists - nothing to update.");
		}
		
		if (!StringUtils.compareStrings(d.getDestinationId(), destination.getDestinationId())) {
			if (internalGetDestination(destination.getDestinationId()) != null) {
				throw new EJBException("Destination with ID "
						+ destination.getDestinationId() + " already exists.");
			}
			d.setDestinationId(destination.getDestinationId());
		}
		if (!StringUtils.compareStrings(d.getDestinationName(), destination.getDestinationName())) {
			if (internalGetDestinationByName(destination.getDestinationName()) != null) {
				throw new EJBException("Destination with name "
						+ destination.getDestinationId() + " already exists.");
			}
			d.setDestinationName(destination.getDestinationName());
		}
		d.setLatitude(destination.getLatitude());
		d.setLongitude(destination.getLongitude());
		return em.merge(d);
	}

	@Override
	public void deleteDestination(String destId) throws ReferenceExistsException {
		Destination d = internalGetDestination(destId);
		if (d != null) {
			try {
				em.remove(d);
			} catch (Exception e) {
				throw new ReferenceExistsException(); // TODO
			}
		}
	}
	
	private Destination internalGetDestination(String id) {
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("Destination ID cannot be empty.");
		}
		
		TypedQuery<Destination> query = em.createNamedQuery(
				"Destination.findByDestinationId", Destination.class);
		query.setParameter("id", id);
		
		List<Destination> results = query.getResultList();
		if (results.isEmpty()) {
			return null;
		}
		
		return results.get(0);
	}
	
	private Destination internalGetDestinationByName(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Destination ID cannot be empty.");
		}
		
		TypedQuery<Destination> query = em.createNamedQuery(
				"Destination.findByName", Destination.class);
		query.setParameter("name", name);
		
		List<Destination> results = query.getResultList();
		if (results.isEmpty()) {
			return null;
		}
		
		return results.get(0);
	}

	
}
