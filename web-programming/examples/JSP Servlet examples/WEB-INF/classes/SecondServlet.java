import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class SecondServlet extends HttpServlet {


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");

        out.println("<title>First servlet</title>");
        out.println("</head>");
        out.println("<body>");

	String nume=request.getParameter("nume");
	String parola=request.getParameter("parola");

	out.println("It works...<hr>"+nume+"<br/>"+parola);

        out.println("</body>");
        out.println("</html>");
    }
}


 