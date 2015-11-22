package cz.hel.aos.entity.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;

import cz.hel.aos.Constants;
import cz.hel.aos.entity.Reservation;
import cz.hel.aos.entity.Reservation.ReservationState;

@XmlRootElement
public class ReservationDTO {

	private Long id;
	private Long flightId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX", timezone = "GMT")
	private Date createdAt;
	private String password;
	private Integer numOfSeats;
	private ReservationState state;
	private String url;

	public ReservationDTO() {
	}

	public ReservationDTO(Reservation r) {
		this(r, null);
	}

	public ReservationDTO(Reservation r, String password) {
		this.id = r.getId();
		if (r.getCreatedAt() != null) {
			this.createdAt = new Date(r.getCreatedAt().getTime());
		}
		this.password = password;
		this.numOfSeats = r.getNumberOfSeats();
		this.state = r.getState();
		this.url = Constants.RESERVATION_RESOURCE_PATH + "/" + id;
		this.flightId = r.getFlight().getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFlightId() {
		return flightId;
	}

	public void setFlightId(Long flightId) {
		this.flightId = flightId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getNumOfSeats() {
		return numOfSeats;
	}

	public void setNumOfSeats(Integer numOfSeats) {
		this.numOfSeats = numOfSeats;
	}

	public ReservationState getState() {
		return state;
	}

	public void setState(ReservationState state) {
		this.state = state;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
