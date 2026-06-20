package pashynskyi.SERVLETS;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pashynskyi.INTERFACE.Queryable;

@WebServlet(name = "Techs", urlPatterns = {"/Techs"})
public class Techs extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("techID");
        String result = Queryable.queryTechnicians(id);
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><link rel='stylesheet' href='styles.css'></head>");
        out.println("<body><div class='container'>");
        out.println("<h2>Technician Data</h2>");
        out.println(result);
        out.println("<br><a href='Console.html' class='btn'>Return to Menu</a>");
        out.println("</div></body></html>");
    }
}