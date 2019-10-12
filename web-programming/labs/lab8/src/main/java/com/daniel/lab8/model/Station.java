package com.daniel.lab8.model;

import java.util.List;

public interface Station
{
    int getId();
    String getName();
    List<Station> getNeighbouringStations();
    void setNeighbouringStations(List<Station> neighbouringCities);
}
