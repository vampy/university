package com.daniel.lab8;

import com.daniel.lab8.model.City;
import com.daniel.lab8.model.Station;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Controller
{
    public static List<Station> getAllStations()
    {
        Connection connection = DB.getConnection();
        List<Station> stations = new ArrayList<>();
        Statement statement = null;

        try
        {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM stations");

            while (rs.next())
            {
                stations.add(new City(rs.getInt("id"), rs.getString("name")));

//                System.out.println(stations.get(stations.size() - 1));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        finally
        {
            try
            {
                if (statement != null)
                {
                    statement.close();
                }
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e.getMessage());
            }
        }

        return stations;
    }

    public static Station getStationAndNeighbours(int id)
    {
        Connection connection = DB.getConnection();
        Station station = null;
        List<Station> neighbouringCities = new ArrayList<>();
        PreparedStatement statement = null;


        // get station
        try
        {
            statement = connection.prepareStatement("SELECT * FROM stations WHERE id = ?");
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            if (!rs.next())
                throw new RuntimeException(String.format("Station with id = %d does not exist", id));

            station = new City(id, rs.getString("name"));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        finally
        {
            try
            {
                if (statement != null)
                {
                    statement.close();
                }
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e.getMessage());
            }
        }

        // get neighbours
        String sql = "SELECT * FROM stations WHERE ID IN " +
            "(SELECT C.to as `nid` FROM `stations` S INNER JOIN connections C ON C.from = S.id WHERE id = ? " +
            "UNION " +
            "SELECT C.from as `nid` FROM `stations` S INNER JOIN connections C ON C.to = S.id WHERE id = ?)";

        try
        {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setInt(2, id);

            ResultSet rs = statement.executeQuery();

            while (rs.next())
            {
                int addId = rs.getInt("id");
                // do not add the city we are coming from in the list
                if (addId == id) continue;

                neighbouringCities.add(new City(addId, rs.getString("name")));
//                System.out.println(neighbouringCities.get(neighbouringCities.size() - 1));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        finally
        {
            try
            {
                if (statement != null)
                {
                    statement.close();
                }
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e.getMessage());
            }
        }

        station.setNeighbouringStations(neighbouringCities);
        return station;
    }
}
