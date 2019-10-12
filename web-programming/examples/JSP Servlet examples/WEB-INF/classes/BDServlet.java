import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class BDServlet extends HttpServlet {


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

	BD bd = new BD();
	bd.connect();
	out.println(bd.showData());

	out.println("<hr>");

        out.println("</body>");
        out.println("</html>");
    }
}


 