package hu.tamas.airlines.model;

import hu.tamas.airlines.model.core.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "city")
@Getter
@Setter
public class City extends AbstractEntity {

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "population")
    private Long population;

    @Override
    public String toString() {
        return "City: cityName=" + cityName;
    }
}
