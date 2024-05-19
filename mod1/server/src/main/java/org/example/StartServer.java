package org.example;

import org.example.domain.Angajat;
import org.example.networking.AbstractServer;
import org.example.networking.ConcurrentServer;
import org.example.repository.*;
import org.example.service.Service;
import org.example.services.IServices;
import org.example.utils.JdbcUtils;
import org.hibernate.cfg.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class StartServer {
    private static int defaultPort=55556;
    public static void main(String[] args) {

        Properties props=new Properties();
        try {
            props.load(new FileReader("../bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        Properties serverProps=new Properties();
        try {
            serverProps.load(StartServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }


        System.out.println("Starting Hibernate");
        var sessionFactory = new Configuration()
                .addAnnotatedClass(Angajat.class)
                .buildSessionFactory();


        IAngajatRepository angajatRepository = new AngajatHibernateRepository(sessionFactory);


        IMeciRepository meciRepository = new MeciDBRepository(new JdbcUtils());
//        IAngajatRepository angajatRepository = new AngajatDBRepository(props);
        IBiletRepository biletRepository = new BiletDBRepository(props);
        IServices service = new Service(angajatRepository, meciRepository, biletRepository);

        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }

        AbstractServer server = new ConcurrentServer(chatServerPort, service);
        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(Exception e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }

    }
}