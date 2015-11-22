package cz.hel.aos.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name = "destination")
@NamedQueries({
	@NamedQuery(name = "Destination.findAll", 
		query = "SELECT d FROM Destination d"),
	@NamedQuery(name = "Destination.findByName", 
		query = "SELECT d FROM Destination d where d.destinationName = :name"),
	@NamedQuery(name = "Destination.findByDestinationId", 
		query = "SELECT d FROM Destination d where d.destinationId = :id")
})
public class Destination extends AbstractEntity {

	private static final long serialVersionUID = -2419473414173142025L;

	@NotNull
	@Column(name = "dest_id", unique = true)
	private String destinationId;
	
	@NotNull
	@Column(name = "dest_name")
	private String destinationName;

	@NotNull
	@Column(name = "latitude")
	private Double latitude;

	@NotNull
	@Column(name = "longitude")
	private Double longitude;
	
	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

}
