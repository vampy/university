package com.daniel.lab8.model;

import java.util.ArrayList;
import java.util.List;

public class TransportationRoute
{
    private List<Station> stations          = new ArrayList<>();
    private int           finalStationIndex = -1;

    public TransportationRoute() {}

    public void addStation(Station station) { stations.add(station); }
    public Station getLastStation()
    {
        if (stations.size() == 0) return null;
        return stations.get(stations.size() - 1);
    }
    public void removeLastStation()
    {
        if (stations.size() > 0) stations.remove(stations.size() - 1);
    }

    public List<Station> getStations()
    {
        return stations;
    }

    public boolean hasFinalStation() { return finalStationIndex != -1; }


    public void resetFinalStation() { finalStationIndex = -1; }

    public void setFinalStation()
    {
        finalStationIndex = stations.size() - 1;
    }

    public void setFinalStation(int index)
    {
        finalStationIndex = index;
    }

    public void setFinalStation(Station station)
    {
        for (int i = 0; i < stations.size(); i++)
        {
            if (stations.get(i).getName().equals(station.getName()))
            {
                finalStationIndex = i;
                return;
            }
        }
    }

    @Override
    public String toString()
    {
        return "TransportationRoute{" +
            "stations=" + stations +
            ", finalStationIndex=" + finalStationIndex +
            '}';
    }
}
