package com.daniel.lab8.model;

import java.util.List;

public class City implements Station
{
    private int id;
    private String name;

    private List<Station> neighbouringCities;

    public City(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public City(int id, String name, List<Station> neighbouringCities)
    {
        this.id = id;
        this.name = name;
        this.neighbouringCities = neighbouringCities;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public List<Station> getNeighbouringStations()
    {
        return neighbouringCities;
    }

    @Override
    public void setNeighbouringStations(List<Station> neighbouringCities)
    {
        this.neighbouringCities = neighbouringCities;
    }

    @Override
    public String toString()
    {
        return "City{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", neighbouringCities=" + neighbouringCities +
            '}';
    }
}
