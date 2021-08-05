package hu.tamas.airlines.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FlightGraph {

    private final List<Flight> edges;

    public FlightGraph(List<Flight> edges) {
        this.edges = edges;
    }

}
