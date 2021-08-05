package hu.tamas.airlines.model;

import hu.tamas.airlines.model.core.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "flight")
@Getter
@Setter
public class Flight extends AbstractEntity {

    @Column(name = "airline_id")
    private Long airlineId;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "airline_id", insertable = false, updatable = false)
    private Airline airline;

    @Column(name = "from_city_id")
    private Long fromCityId;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "from_city_id", insertable = false, updatable = false)
    private City fromCity;

    @Column(name = "to_city_id")
    private Long toCityId;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "to_city_id", insertable = false, updatable = false)
    private City toCity;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "flight_time")
    private Long flightTime;

    @Override
    public String toString() {
        return "Flight: " +
                " airline = " + airline.getAirlineName() +
                ", fromCity=" + fromCity.getCityName() +
                ", toCity=" + toCity.getCityName() +
                ", distance=" + distance +
                ", flightTime=" + flightTime;
    }
}
