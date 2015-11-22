package cz.hel.aos.service;

import java.util.List;

import javax.ejb.Local;

import cz.hel.aos.entity.dto.FlightDTO;

@Local
public interface FlightManagement {

	public List<FlightDTO> getAllFlights();

	public FlightDTO getFlight(Long id);

	public FlightDTO createFlight(FlightDTO flight);

	public FlightDTO updateFlight(FlightDTO flight);

	public void deleteFlight(Long id) throws ReferenceExistsException;

	public List<FlightDTO> getAllFlights(SimpleCriteria parseHeadersCriteria);

}
