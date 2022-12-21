package com.business.testjavaee;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//  CHIEDIAMO I COOKIE!!!!
@WebServlet(name = "CartServlet", value = "/Cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        if("0".equals(request.getParameter("action"))){ //Se premo BUY ESCO
            session.invalidate();
            response.sendRedirect("login.html");
            return;
        }
        String user = (String) session.getAttribute("Login");
        if(session.getAttribute("Login")==null){
            response.sendRedirect("login.html");
            return;
        }

        if(session.getAttribute("item")==null) {
            session.setAttribute("item", 0);
            session.setAttribute("total", 0);
        }else {
            String price =request.getParameter("price");
            if(price!=null) {
                int item = (Integer) session.getAttribute("item");
                int total = (Integer) session.getAttribute("total");
                item += 1;
                total += Integer.parseInt(price);
                session.setAttribute("item", item);
                session.setAttribute("total", total);
            }
        }
        PrintWriter out = response.getWriter();

        response.setContentType("text/html");
        Connection c = LoginServlet.getConnectToTomCat();
        ResultSet rs = null;
        out.println("<table border=3>");
        try {
            rs = c.createStatement().executeQuery("select * from products");

            while (rs.next()) {
                out.println("<tr><form><td>" + rs.getString("id") + "</td><td>" + rs.getString("name") + "</td><td>" + rs.getString("price") + "</td><td><input type='submit' value='Add'></td>" +
                        "<input type='hidden' name='price' value=" + rs.getString("price") + ">" + "<input type='hidden' name='ncart' value=" + 1 + ">" + "</form></tr>");

            }

            out.println("<tr><td>Oggetti in carrello</td><td></td><td>" +session.getAttribute("item") + "</td></tr>");
            out.println("<tr><td>TOT</td><td></td><td>" + session.getAttribute("total")  + "</td><td><a href='Cart?action=0'>BUY</a></td></tr>"); //Il tasto buy setta un valore 'action' a 0


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            out.println("</table>");
            try {
                c.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            out.close();
        }
    }

    /* Map<String,Integer[]> map = new HashMap<>();
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
            if(session.getAttribute("Login")==null){
              response.sendRedirect("login.html");
            }else {
                Integer coppia[]=new Integer[2];
                coppia[0] =0;
                coppia[1] =0;
                map.putIfAbsent((String) session.getAttribute("Login"), coppia);
                int costo =map.get((String) session.getAttribute("Login"))[0];
                int ncarrello =map.get((String) session.getAttribute("Login"))[1];
                PrintWriter out = response.getWriter();

                response.setContentType("text/html");
                Connection c = LoginServlet.getConnectToTomCat();
                ResultSet rs = null;
                out.println("<table border=1>");

                try {
                    rs = c.createStatement().executeQuery("select * from products");

                    while (rs.next()) {
                        out.println("<tr><form><td>" + rs.getString("id") + "</td><td>" + rs.getString("name") + "</td><td>" + rs.getString("price") + "</td><td><input type='submit' value='Add'></td>" +
                                "<input type='hidden' name='price' value=" + rs.getString("price") + ">" + "<input type='hidden' name='ncart' value=" + 1 + ">" + "</form></tr>");

                    }
                    if (request.getParameter("price") != null) {
                        coppia[0] = costo+Integer.parseInt(request.getParameter("price"));

                       coppia[1] = ncarrello+Integer.parseInt(request.getParameter("ncart"));
                        map.put((String) session.getAttribute("Login"),coppia);
                    }
                    out.println("<tr><td>Oggetti in carrello</td><td></td><td>" + map.get((String) session.getAttribute("Login"))[0] + "</td></tr>");
                    out.println("<tr><td>TOT</td><td></td><td>" + map.get((String) session.getAttribute("Login"))[1] + "</td></tr>");


                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                } finally {
                    out.println("</table>");
                    try {
                        c.close();
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                    }
                    out.close();
                }
            }

        }
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    public void destroy() {
        super.destroy();
        System.out.println("BYEBYE");
    }
}

