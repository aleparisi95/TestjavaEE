package com.business.testjavaee;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


//SERVE DARE LA MAPPATURA!!! CON  @WEBSERVLET
@WebServlet(name = "TestServerLifecycle!",urlPatterns = "/login")
public class LoginServlet extends HttpServlet { //SERVLET PER CREARE DELLE SERVLET
    //START DAL CICLO DI VITA --->NON POSSO PASSARE PARAMETRI
    public LoginServlet(){
        System.out.println("In TestServerLifecycle()");
    }
    //INIT SERVER A CONFIGURARE LA SERVLET E QUINDI POSSO PASSARE I PARAMETRI
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println("In TestServerLifecycle() - init");
    }


    //ATTRAVERSO LA HttpServletRequest prende la richiesta e HttpServletResponse invece dà la RISPOSTA che magari voglio far vedere a video
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // super.doGet(req, resp); //MANDA AUTOMATICAMENTE UN MESSAGGIO PER DIRE CHE NON HO ACNORA MANDATO IN DIETRO IL CODICE----> DA RIMUOVERE SEMPRE
        System.out.println("In TestServerLifecycle() - doGet");

    /*
        String user = req.getParameter("username");
        String pass = req.getParameter("password");

        */


      //  PrintWriter out = resp.getWriter();
//
      //  resp.setContentType("text/html");
       // out.println("<h1>BAD CREDENTIAL!</h1>");
      //  out.close();
       // doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // super.doPost(req, resp);


        System.out.println("In TestServerLifecycle() - doPost");

        String user = req.getParameter("username");
        String pass = req.getParameter("password");

        PrintWriter out = resp.getWriter();
        Connection c = getConnectToTomCat();

       if(loginFunc(c,user,pass)){

            resp.setContentType("text/html");
            //out.println("<h1>Hello from "+user+"</h1>");
           HttpSession session = req.getSession(true);
           if(!session.isNew()){
               session.invalidate();
               session = req.getSession(true);

           }
           //session.setAttribute("Login",(String)user);
           session.setAttribute("Login",user);
           resp.sendRedirect("Cart");
        }else {

            resp.setContentType("text/html");
            out.println("<h1>BAD CREDENTIAL!</h1>"); //Altrimenti BAD CREDENTIAL
             resp.sendRedirect("login.html");
        }

        out.close();
    }

    //Quindi dovremmo rilasciare la connesione al database
    @Override
    public void destroy() {
        super.destroy();
        System.out.println("In TestServerLifecycle() - destroy");
    }



   /* private boolean getConnectionFromTomcat(String user,String pas) {
        //FUNZIONA PER OTTENERE LA CONNESIONE CON IL SERVER GIA' APERTO CON TOMCAT
        //USIAMO----> JNDI : Java naming directory Interface
        Context ctx = null; //Inizializzo il SISTEMA DI RICHIESTA DI RISORSE ESTERNE
        Connection c = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/testfromjava"); //CERCA IL NOME DELLA CONNESSIONE che ho scritto in context.xml MA deve partire da una cartella nel mio caso java:comp/env/
            c = ds.getConnection();
            //Ora possiamo pure preparare i Statement
            st = c.prepareStatement("SELECT * FROM USERS WHERE username = ? AND password = ?");
            st.setString(1,user);
            st.setString(2,pas);
            rs = st.executeQuery();
            while (rs.next()){
                return true;
            }

            System.out.println(c);
            return false;
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }finally {
            try {
                c.close();
                st.close();
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }



    }
*/
    public static Connection getConnectToTomCat(){
        Context ctx = null;
        Connection c = null;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/testfromjava"); //CERCA IL NOME DELLA CONNESSIONE che ho scritto in context.xml MA deve partire da una cartella nel mio caso java:comp/env/
            c = ds.getConnection();
            System.out.println(c);
            return c;
        }catch (NamingException | SQLException e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    private boolean loginFunc(Connection c,String us,String ps){
        if(c!=null){
            PreparedStatement st = null;
            ResultSet rs = null;
            try {
                st = c.prepareStatement("SELECT * FROM USERS WHERE username = ? AND password = ?");
                st.setString(1,us);
                st.setString(2,ps);
                rs = st.executeQuery();
                while (rs.next()){
                    return true;
                }
                return false;
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }finally {
                try {
                    c.close();
                    st.close();
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return false;
    }
}


