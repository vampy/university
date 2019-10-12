package com.daniel.lab8;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="mytest", urlPatterns={"/test"}, initParams={ @WebInitParam(name="n1", value="v1"), @WebInitParam(name="n2", value="v2") })
public class HelloServlet extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        PrintWriter writer = response.getWriter();
        writer.print(request.getQueryString());
        writer.print("<br>1Java su sdsadsad ascks");
        writer.flush();
    }
}
