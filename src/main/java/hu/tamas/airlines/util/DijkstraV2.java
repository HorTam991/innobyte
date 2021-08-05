package hu.tamas.airlines.util;

import hu.tamas.airlines.model.City;
import hu.tamas.airlines.model.Flight;
import hu.tamas.airlines.model.FlightGraph;

import java.util.*;

public class DijkstraV2 {

    private final List<Flight> edges;

    private Set<City> settledNodes;
    private Set<City> unSettledNodes;
    private Map<Flight, City> predecessors;
    private Map<City, Integer> distance;

    public DijkstraV2(FlightGraph graph) {
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
        List<Flight> adjacentNodes = getNeighbors(node);
        for (Flight target : adjacentNodes) {
            if (getShortestDistance(target.getToCity()) > getShortestDistance(node) + getDistance(node, target.getToCity())) {
                distance.put(target.getToCity(), getShortestDistance(node) + getDistance(node, target.getToCity()));
                predecessors.put(target, node);
                unSettledNodes.add(target.getToCity());
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

    private List<Flight> getNeighbors(City node) {
        List<Flight> neighbors = new ArrayList<>();
        for (Flight flight : edges) {
            if (flight.getFromCity().equals(node) && !isSettled(flight.getToCity())) {
                neighbors.add(flight);
            }
        }
        return neighbors;
    }

    private City getMinimum(Set<City> vertexes) {
        City minimum = null;
        for (City city : vertexes) {
            if (minimum == null) {
                minimum = city;
            } else {
                if (getShortestDistance(city) < getShortestDistance(minimum)) {
                    minimum = city;
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

    public LinkedList<Flight> getPath(City source, City target) {
        Set<Flight> setPath = new HashSet<>();
        boolean containsTarget = false;
        for (Flight flight : predecessors.keySet()) {
            if (flight.getToCity().getId().equals(target.getId())) {
                containsTarget = true;
                break;
            }
        }
        if (!containsTarget) {
            return new LinkedList<>();
        }
        Flight lastItem = getFlightByTargetCity(target);
        setPath.add(lastItem);
        if (lastItem.getFromCity().getId().equals(source.getId()) && lastItem.getToCity().getId().equals(target.getId())) {
            return createPathFromSet(setPath);
        }

        City step = lastItem.getFromCity();
        while (!step.getId().equals(target.getId())) {
            Flight pathItem = getFlightByTargetCity(step);
            if (pathItem != null) {
                setPath.add(pathItem);
                if (pathItem.getToCity().getId().equals(target.getId()) || pathItem.getFromCity().getId().equals(source.getId())) {
                    break;
                }

                step = pathItem.getFromCity();
            }
        }
        return createPathFromSet(setPath);
    }

    private Flight getFlightByTargetCity(City target) {
        Flight result = null;
        Flight firsFlight = null;
        for (Flight flight : predecessors.keySet()) {
            if (flight.getToCity().getId().equals(target.getId())) {
                firsFlight = flight;
                result = flight;
                break;
            }
        }
        Flight actualFlight;
        for (Flight flight : predecessors.keySet()) {
            if (flight.getToCity().getId().equals(target.getId())) {
                actualFlight = flight;
                if (actualFlight.getDistance() < firsFlight.getDistance()) {
                    result = actualFlight;
                }
            }
        }
        return result;
    }

    private LinkedList<Flight> createPathFromSet(Set<Flight> setPath) {
        LinkedList<Flight> path = new LinkedList<>(setPath);
        Collections.reverse(path);
        return path;
    }

}
