package cz.hel.aos.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "flight")
@NamedQueries({
	@NamedQuery(name = "Flight.findAll", query = "SELECT f from Flight f")
})
public class Flight extends AbstractEntity {

	private static final long serialVersionUID = 235935040450838700L;

	@NotNull
	@Column(name = "flight_name", length = 100)
	private String flightName;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_departure")
	private Date dateOfDeparture;

	@NotNull
	@Column(name = "total_seats")
	private Integer totalSeats;

	@NotNull
	@Column(name = "distance")
	private Double distance;

	@Column(name = "price")
	private BigDecimal price;

	@NotNull
	@OneToOne
	@JoinColumn(name = "dest_from")
	private Destination flightFrom;

	@NotNull
	@OneToOne
	@JoinColumn(name = "dest_to")
	private Destination flightTo;

	public String getFlightName() {
		return flightName;
	}

	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}

	public Date getDateOfDeparture() {
		return dateOfDeparture;
	}

	public void setDateOfDeparture(Date dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Destination getFlightTo() {
		return flightTo;
	}

	public void setFlightTo(Destination flightTo) {
		this.flightTo = flightTo;
	}

	public Integer getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(Integer totalSeats) {
		this.totalSeats = totalSeats;
	}

	public Destination getFlightFrom() {
		return flightFrom;
	}

	public void setFlightFrom(Destination flightFrom) {
		this.flightFrom = flightFrom;
	}

}
