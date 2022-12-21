package com.business.testjavaee;

//import java.util.logging.Filter; NON QUESTO!!!! deve essere javax.servlet.Filter

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//Implemneta l'intrefaccia FILTER
//@WebFilter(urlPatterns = "/*",initParams = {@WebInitParam(name ="p1",value = "p1val")}) //Se metto '/*' VIENE SEMPRE MAPPATO ED ESEGUITO QUESTO ... qui CONDIZIONE DI QUANDO IL FILTRO VIENE MESSO IN ESECUZIONE ... nota che i parametri possono essere inizializzati anche da web.xml
public class Filter1 implements Filter {
    @Override //COME PASSARE DEI PARAMETRI? DPO AVER IMPOSTATO URLPATTERN
    public void init(FilterConfig filterConfig) throws ServletException {
       //TOMCAT CHIAMA ISTANZE DI FILTRI esempio: parametro è magari una condizione
        String value1 = filterConfig.getInitParameter("p1");
    }

    @Override   //CUORE DEL FILTRO
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException { //Se non usiamo il filterchain nel doFilter la palla di fermerà a qui
        long start = System.currentTimeMillis();
         System.out.println("in doFilter - TOWARD ENDPOINT");
         //Si blocca la richiesta SE non uso il chain
        //Se non metto il filtrochain otterei una pagina biuanca perchè non continua
        filterChain.doFilter(servletRequest,servletResponse);
        //servletResponse.getWriter().println("NON SEI AUTORIZZATO");
        System.out.println("in doFilter - TOWARD CLIENT");
        long end = System.currentTimeMillis();
        System.out.println("Elapsed: "+(end-start)/1000f +"s URL:"+((HttpServletRequest)servletRequest).getRequestURL());
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
