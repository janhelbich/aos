package cz.hel.aos.entity.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;

import cz.hel.aos.Constants;
import cz.hel.aos.entity.Flight;

@XmlRootElement
public class FlightDTO implements Serializable {

	private static final long serialVersionUID = 1816868444679666200L;

	private Long id;
	private String flightName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX", timezone="GMT")
	private Date dateOfDepatrure;
	private Double distance;
	private Integer seats;
	private BigDecimal price;
	private String from;
	private String to;
	private String url;

	public FlightDTO() {
	}

	public FlightDTO(Flight f) {
		this.id = f.getId();
		this.flightName = f.getFlightName();
		this.distance = f.getDistance();
		this.seats = f.getTotalSeats();
		this.price = f.getPrice();
		this.from = f.getFlightFrom().getDestinationId();
		this.to = f.getFlightTo().getDestinationId();
		this.url = Constants.FLIGHT_RESOURCE_PATH + "/" + id;
		if (f.getDateOfDeparture() != null) {
			this.dateOfDepatrure = new Date(f.getDateOfDeparture().getTime());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFlightName() {
		return flightName;
	}

	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}

	public Date getDateOfDepatrure() {
		return dateOfDepatrure;
	}

	public void setDateOfDepatrure(Date dateOfDepatrure) {
		this.dateOfDepatrure = dateOfDepatrure;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
