package org.example.networking;

import org.example.domain.Angajat;
import org.example.domain.Bilet;
import org.example.domain.Meci;
import org.example.observer.IObserver;
import org.example.services.IServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesProxy implements IServices {
    private String host;
    private int port;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public ServicesProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
        initializeConnection();
    }

    @Override
    public Integer login(String username, String parola, IObserver client) {
        initializeConnection();
        Angajat angajat = new Angajat(parola, username);
        Request req=new Request.Builder().type(RequestType.LOGIN).data(angajat).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type()== ResponseType.OK){
            this.client = client;
            return (Integer) response.data();
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
//            throw new Exception(err);
        }
        return -1;
    }

    @Override
    public void logout(Integer idAngajat) {
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(idAngajat).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
//            throw new ChatException(err);
        }
    }

    @Override
    public int nrLocuriDisponibileMeci(Meci meci) {
        Request req=new Request.Builder().type(RequestType.LOCURI_DISPONIBILE).data(meci).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
//            throw new ChatException(err);
            return 0;
        }
        Integer nrLocuri = (Integer) response.data();
        return nrLocuri;
    }

    @Override
    public Iterable<Meci> getMeciuri() {
        Request req=new Request.Builder().type(RequestType.GET_MECIURI).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
//            throw new ChatException(err);
            return null;
        }
        Iterable<Meci> meciuri = (Iterable<Meci>) response.data();
        return meciuri;
    }

    @Override
    public Iterable<Meci> getMeciuriLibere() {
        Request req=new Request.Builder().type(RequestType.GET_MECIURI_LIBERE).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
//            throw new ChatException(err);
            return null;
        }
        Iterable<Meci> meciuri = (Iterable<Meci>) response.data();
        return meciuri;
    }

    @Override
    public Bilet cumparaBilet(Meci meci, String numeClient, int nrLocuri) {
        Bilet bilet = new Bilet(meci, numeClient, nrLocuri);
        Request req=new Request.Builder().type(RequestType.CUMPARA_BILET).data(bilet).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
//            throw new ChatException(err);
            return null;
        }
        Bilet bilet_salvat = (Bilet) response.data();
        return bilet_salvat;
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
//            throw new ChatException("Error sending object "+e);
        }

    }

    private Response readResponse() {
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response){
        if (response.type()== ResponseType.NEW_MECIURI_LIST){
            Iterable<Meci> meciuri = (Iterable<Meci>) response.data();
            System.out.println("Lista noua de meciuri "+meciuri);
            try {
                client.schimbareMeciuri(meciuri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response){
        return response.type()== ResponseType.NEW_MECIURI_LIST;
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
