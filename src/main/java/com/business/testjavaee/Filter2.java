package com.business.testjavaee;

import javax.servlet.*;
import java.io.IOException;
import java.util.Calendar;

//LE ANNOTAZIONI NON PERMETTONO DI CONTROLLARE LA SEQUENZA DEI FILTRI
//@WebFilter(filterName = "Filter2",urlPatterns = "/*")
public class Filter2 implements Filter {
    Integer startLavoroh;
    Integer endLavoroh;
    public void init(FilterConfig config) throws ServletException {
        startLavoroh = Integer.parseInt(config.getInitParameter("startWork"));
        endLavoroh = Integer.parseInt(config.getInitParameter("endWork"));
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        Calendar a = Calendar.getInstance();


        System.out.println("Ora Inizio: "+ startLavoroh +" Fine: "+ endLavoroh +" Ora di oggi: "+a.get(Calendar.HOUR_OF_DAY));
        if(a.get(Calendar.HOUR_OF_DAY)>= startLavoroh && a.get(Calendar.HOUR_OF_DAY)< endLavoroh){
            System.out.println("in doFilter2 - TOWARD ENDPOINT");
            chain.doFilter(request, response);
            System.out.println("in doFilter2 - TOWARD CLIENT");
        }
        else {
            response.setContentType("text/html");
            response.getWriter().println("<h1>Endpoint not aviable at moment</h1>");
        }
    }

}
