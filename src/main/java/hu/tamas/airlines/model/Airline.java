package hu.tamas.airlines.model;

import hu.tamas.airlines.model.core.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "airline")
@Getter
@Setter
public class Airline extends AbstractEntity {

    @Column(name = "airline_name")
    private String airlineName;

}
