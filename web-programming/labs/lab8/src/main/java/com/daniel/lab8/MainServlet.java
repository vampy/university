package com.daniel.lab8;

import com.daniel.lab8.model.Station;
import com.daniel.lab8.model.TransportationRoute;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


@WebServlet(name = "MainServlet", urlPatterns = {""})
public class MainServlet extends HttpServlet
{
    private final String KEY_ROUTE = "route";
    private final String ACTION_SELECT = "select";
    private final String ACTION_MARK_FINAL = "mark-final";
    private final String ACTION_BACK = "back";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);
        String action = request.getParameter("action");
        TransportationRoute route = (TransportationRoute)session.getAttribute(KEY_ROUTE);
        boolean hasFinal = route != null && route.hasFinalStation();
        System.out.println(action);

        if (action.equals(ACTION_SELECT) && !hasFinal)
        {
            int station_id = Integer.parseInt(request.getParameter("station-id"));
            Station station = Controller.getStationAndNeighbours(station_id);

            // first station
            if (route == null)
            {
                route = new TransportationRoute();
            }
            route.addStation(station);
            session.setAttribute(KEY_ROUTE, route);
        }
        else if (action.equals(ACTION_MARK_FINAL) && !hasFinal)
        {
            assert route != null;
            route.setFinalStation();
        }
        else if (action.equals(ACTION_BACK))
        {
            assert route != null;
            if (hasFinal)
            {
                route.resetFinalStation();
            }
            route.removeLastStation();
        }

//        System.out.println(station_id);
        response.sendRedirect("/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);
        TransportationRoute route = (TransportationRoute)session.getAttribute(KEY_ROUTE);
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        List<Station> stations;
//        System.out.println(route);

        // select first station
        if (route == null)
        {
            stations = Controller.getAllStations();
        }
        else
        {
            Station lastStation = route.getLastStation();
            if (lastStation == null)
            {
                stations = Controller.getAllStations();
            }
            else
            {
                stations = lastStation.getNeighbouringStations();
            }
        }
        request.setAttribute("stations", stations);
        request.setAttribute("route", route);


        rd.forward(request, response);
    }
}
