package pashynskyi.SERVLETS;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pashynskyi.INTERFACE.Queryable;

@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String u = request.getParameter("uid");
        String p = request.getParameter("pw");
        ResultSet rs = Queryable.conductLogin(u, p);
        
        try {
            if (rs != null && rs.next()) {
                response.sendRedirect("Console.html");
            } else {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><head><link rel='stylesheet' href='styles.css'></head>");
                out.println("<body><div class='container'>");
                out.println("<h2 style='color:red;'>Login Failed</h2>");
                out.println("<a href='index.html' class='btn'>Try Again</a>");
                out.println("</div></body></html>");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}