package hu.tamas.airlines.util;

import hu.tamas.airlines.model.City;
import hu.tamas.airlines.model.Flight;
import hu.tamas.airlines.model.FlightGraph;

import java.util.*;

@Deprecated
public class Dijkstra {

    private final List<Flight> edges;

    private Set<City> settledNodes;
    private Set<City> unSettledNodes;
    private Map<City, City> predecessors;
    private Map<City, Integer> distance;

    public Dijkstra(FlightGraph graph) {
        this.edges = new ArrayList<>(graph.getEdges());
    }

    public void execute(City source) {
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distance = new HashMap<>();
        predecessors = new HashMap<>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            City node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(City node) {
        List<City> adjacentNodes = getNeighbors(node);
        for (City target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node) + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }
    }

    private int getDistance(City node, City target) {
        for (Flight flight : edges) {
            if (flight.getFromCity().equals(node) && flight.getToCity().equals(target)) {
                return flight.getDistance();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<City> getNeighbors(City node) {
        List<City> neighbors = new ArrayList<>();
        for (Flight Flight : edges) {
            if (Flight.getFromCity().equals(node) && !isSettled(Flight.getToCity())) {
                neighbors.add(Flight.getToCity());
            }
        }
        return neighbors;
    }

    private City getMinimum(Set<City> vertexes) {
        City minimum = null;
        for (City City : vertexes) {
            if (minimum == null) {
                minimum = City;
            } else {
                if (getShortestDistance(City) < getShortestDistance(minimum)) {
                    minimum = City;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(City City) {
        return settledNodes.contains(City);
    }

    private int getShortestDistance(City destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    public LinkedList<City> getPath(City target) {
        LinkedList<City> path = new LinkedList<>();
        City step = target;
        if (predecessors.get(step) == null) {
            return path;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        Collections.reverse(path);
        return path;
    }

}
