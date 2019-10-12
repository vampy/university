package client.web;

import library.model.Book;
import library.model.BookLoan;
import library.network.rpcprotocol.LibraryServerRPCProxy;
import library.services.Constants;
import library.services.ILibraryServer;
import library.services.LibraryException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

public class LoginServlet extends HttpServlet
{
    private static final Logger logger = Logger.getLogger("client.web.LoginServlet");

    private ILibraryServer        mLibraryServer;
    private WebTerminalController mController;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        mLibraryServer = new LibraryServerRPCProxy(Constants.HOSTNAME, Constants.PORT);
        mController = new WebTerminalController(mLibraryServer);

        getServletContext().log("LoginServlet init() called");
    }

    void forwardToJSP(String to, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.getRequestDispatcher(to).forward(request, response);
    }

    void writeTest(HttpServletResponse response) throws IOException
    {
        PrintWriter out = response.getWriter();
        out.println("Hello, world!");
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        getServletContext().log("LoginServlet doGet() called");

        if (request.getQueryString() != null && request.getQueryString().contains("logout"))
        {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            request.getRequestDispatcher("/index.jsp").include(request, response);

            HttpSession session = request.getSession();
            session.invalidate();

            mController.logout();

            out.print("You are successfully logged out!");
        }
        else
        {
            forwardToJSP("/views/index.jsp", request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        getServletContext().log("LoginServlet doPost() called");

        String intputUser = request.getParameter("user");
        String intputPassword = request.getParameter("password");

        log("User=" + intputUser + "::password=" + intputPassword);

        try
        {
            Object loggedIn = request.getSession().getAttribute("logged_in");

            if ((loggedIn == null || !(Boolean) loggedIn))
            {
                if (!mController.login(intputUser, intputPassword))
                {
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
                    PrintWriter out = response.getWriter();
                    out.println("<font color=red>Either user name or password is wrong.</font>");
                    rd.include(request, response);
                }
                else
                {
                    request.getSession().setAttribute("logged_in", true);
                }
            }

            request.setAttribute("username", intputUser);
            request.setAttribute("isLibrarian", mController.isLibrarian());

            List<Book> availableBooks = null;
            if (request.getQueryString() != null)
            {
                String queryString = request.getQueryString();

                if (queryString.contains("search"))
                {
                    String title = request.getParameter("title");
                    getServletContext().log("search: " + title);
                    availableBooks = mController.searchByTitle(title);
                }

                if (queryString.contains("borrow"))
                {
                    String bookId = request.getParameter("bookId");
                    getServletContext().log("borrow: " + bookId);
                    mController.borrowBook(Integer.valueOf(bookId));
                }

                if (queryString.contains("return"))
                {
                    String bookId = request.getParameter("bookId");
                    String userId = request.getParameter("userId");
                    getServletContext().log("return: " + "bookId: " + bookId + "userId: " + userId);

                    if (userId != null) // librarian return
                    {
                        mController.returnBook(Integer.valueOf(bookId), Integer.valueOf(userId));
                    }
                    else
                    {
                        // user return
                        mController.returnBook(Integer.valueOf(bookId), mController.getUser().getId());
                    }
                }
            }

            if (mController.isLibrarian())
            {
                List<BookLoan> loanedBooks = mLibraryServer.getAllLoans();
                request.setAttribute("loanedBooks", loanedBooks);

                mController.setAvailableLoans(loanedBooks);
            }
            else
            {
                if (request.getQueryString() == null || (request.getQueryString() != null && !request.getQueryString().contains("search")))
                {
                    availableBooks = mLibraryServer.getAvailableBooks();
                }

                List<Book> borrowedBooks = mLibraryServer.getBorrowedBy(mController.getUser());

                request.setAttribute("availableBooks", availableBooks);
                request.setAttribute("borrowedBooks", borrowedBooks);

                mController.setAvailableBooks(availableBooks);
                mController.setBorrowedBooks(borrowedBooks);
            }

            forwardToJSP("/views/login_success.jsp", request, response);
        }
        catch (LibraryException e)
        {
            e.printStackTrace();

            try
            {
                if (mController.isLibrarian())
                {
                    List<BookLoan> loanedBooks = mLibraryServer.getAllLoans();
                    request.setAttribute("loanedBooks", loanedBooks);

                    mController.setAvailableLoans(loanedBooks);
                }
                else
                {
                    List<Book> availableBooks = mLibraryServer.getAvailableBooks();

                    List<Book> borrowedBooks = mLibraryServer.getBorrowedBy(mController.getUser());

                    request.setAttribute("availableBooks", availableBooks);
                    request.setAttribute("borrowedBooks", borrowedBooks);

                    mController.setAvailableBooks(availableBooks);
                    mController.setBorrowedBooks(borrowedBooks);
                }
            }
            catch (LibraryException e1)
            {
                e1.printStackTrace();
            }

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            request.getRequestDispatcher("/views/login_success.jsp").include(request, response);

            out.print("<font color=red>" + e.getMessage() + "</font>");
        }
    }
}
