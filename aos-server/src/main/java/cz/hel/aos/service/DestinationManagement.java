package cz.hel.aos.service;

import java.util.List;

import javax.ejb.Local;

import cz.hel.aos.entity.Destination;

@Local
public interface DestinationManagement {

	public List<Destination> getAllDestinations();

	public Destination getDestination(String destinationId);

	public Destination createDestination(Destination destination);

	public Destination updateDestination(Destination destination);

	public void deleteDestination(String destinationId) throws ReferenceExistsException;

}
