package org.example.networking;

import org.example.domain.Angajat;
import org.example.domain.Bilet;
import org.example.domain.Meci;
import org.example.domain.MeciL;
import org.example.observer.IObserver;
import org.example.services.IServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ClientWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    @Override
    public void schimbareMeciuri(Iterable<MeciL> meciuri){
        Response resp=new Response.Builder().type(ResponseType.NEW_MECIURI_LIST).data(meciuri).build();
        System.out.println("Schimbare meciuri " + meciuri);
        try {
            sendResponse(resp);
        } catch (IOException e) {
//            throw new Exception("Sending error: "+e);
        }
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request){
        Response response=null;
        if (request.type()== RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());
            Angajat angajat = (Angajat) request.data();
            try {
                Integer idAngajat = server.login(angajat.getUsername(), angajat.getParola(), this);

                return new Response.Builder().type(ResponseType.OK).data(idAngajat).build();
            } catch (Exception e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGOUT){
            System.out.println("Logout request");
            // LogoutRequest logReq=(LogoutRequest)request;
            Integer idAngajat = (Integer) request.data();
            try {
                server.logout(idAngajat);
                connected=false;
                return okResponse;

            } catch (Exception e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.GET_MECIURI){
            System.out.println("GetMeciuriRequest ...");
            try {
                return new Response.Builder().type(ResponseType.OK).data(server.getMeciuri()).build();
            } catch (Exception e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.GET_MECIURI_LIBERE){
            System.out.println("GetMeciuriLibereRequest ...");
            try {
                return new Response.Builder().type(ResponseType.OK).data(server.getMeciuriLibere()).build();
            } catch (Exception e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.CUMPARA_BILET){
            System.out.println("Vinde bilet request ..."+request.type());
            Bilet bilet = (Bilet) request.data();
            try {
                Bilet bilet_nou = server.cumparaBilet(bilet.getMeci(), bilet.getNumeClient(), bilet.getNrLocuri());
                return new Response.Builder().type(ResponseType.OK).data(bilet_nou).build();
            } catch (Exception e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.LOCURI_DISPONIBILE){
            System.out.println("Locuri disponibile ..."+request.type());
            Meci meci = (Meci) request.data();
            try {
                Integer nrLocuri = server.nrLocuriDisponibileMeci(meci);
                return new Response.Builder().type(ResponseType.LOCURI_DISPONIBILE).data(nrLocuri).build();
            } catch (Exception e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
}
