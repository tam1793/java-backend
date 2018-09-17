/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import app.controller.DemoApiController;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 *
 * @author tam
 */
public class DemoJavaServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //        QueuedThreadPool threadPool = new QueuedThreadPool();
//        threadPool.setMinThreads(20);
//        threadPool.setMaxThreads(500);
//        Server server = new Server(threadPool);

//        ServerConnector connector = new ServerConnector(server);
//        connector.setPort(9090);
//        server.setConnectors(new Connector[]{connector});
        Server server = new Server(9090);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");

        // Create the SessionHandler (wrapper) to handle the sessions
        HashSessionManager manager = new HashSessionManager();
        SessionHandler sessions = new SessionHandler(manager);
        context.setHandler(sessions);

//        context.addServlet(Login.class, "/login");
//        context.addServlet(Home.class, "/home");
//        context.addServlet(Signin.class, "/signin");
        context.addServlet(DemoApiController.class, "/demoService");
        server.setHandler(context);

        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            Logger.getLogger(DemoJavaServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
