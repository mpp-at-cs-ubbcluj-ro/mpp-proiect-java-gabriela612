package org.example.networking;

import com.google.protobuf.MessageLite;
import org.example.domain.Angajat;
import org.example.domain.Bilet;
import org.example.domain.Meci;
import org.example.domain.MeciL;
import org.example.observer.IObserver;
import org.example.proto.ProjectProto;
import org.example.services.IServices;
import org.example.utils.ProtoUtils;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoServicesProxy implements IServices {
    private String host;
    private int port;

    private IObserver client;

    private InputStream input;
    private OutputStream output;

    private Socket connection;

    private BlockingQueue<ProjectProto.Response> qresponses;
    private volatile boolean finished;
    public ProtoServicesProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<ProjectProto.Response>();
        initializeConnection();
    }

    @Override
    public Integer login(String username, String parola, IObserver client) {
        initializeConnection();
        Angajat angajat = new Angajat(parola, username);
        ProjectProto.Request request = ProtoUtils.createLoginRequest(angajat);
        sendRequest(request);
        ProjectProto.Response response = readResponse();
        if (response.getType() == ProjectProto.Response.Type.OK){
            this.client = client;
            return response.getIdAngajat();
        }
        if (response.getType() == ProjectProto.Response.Type.ERROR){
            String err=response.getMessage().toString();
            closeConnection();
//            throw new Exception(err);
        }
        return -1;
    }

    @Override
    public void logout(Integer idAngajat) {
        ProjectProto.Request req = ProtoUtils.createLogoutRequest(idAngajat);
        sendRequest(req);
        ProjectProto.Response response = readResponse();
        closeConnection();
        if (response.getType() == ProjectProto.Response.Type.ERROR){
            String err=response.getMessage().toString();
//            throw new ChatException(err);
        }
    }

    @Override
    public int nrLocuriDisponibileMeci(Meci meci) {
        ProjectProto.Request req = ProtoUtils.createLocuriDisponibileRequest(meci);
        sendRequest(req);
        ProjectProto.Response response = readResponse();
        if (response.getType() == ProjectProto.Response.Type.ERROR){
            String err=response.getMessage().toString();
//            throw new ChatException(err);
            return 0;
        }
        Integer nrLocuri = (Integer) response.getNrLocuriDisponibile();
        return nrLocuri;
    }

    @Override
    public Iterable<MeciL> getMeciuri() {
        ProjectProto.Request req = ProtoUtils.createGetMeciuriRequest();
        sendRequest(req);
        ProjectProto.Response response = readResponse();
        if (response.getType() == ProjectProto.Response.Type.ERROR){
            String err=response.getMessage().toString();
//            throw new ChatException(err);
            return null;
        }
        Iterable<MeciL> meciuri = ProtoUtils.getMeciuri(response);

        return meciuri;
    }

    @Override
    public Iterable<MeciL> getMeciuriLibere() {
        ProjectProto.Request req = ProtoUtils.createGetMeciuriLibereRequest();
        sendRequest(req);
        ProjectProto.Response response = readResponse();
        if (response.getType() == ProjectProto.Response.Type.ERROR){
            String err=response.getMessage().toString();
//            throw new ChatException(err);
            return null;
        }
        Iterable<MeciL> meciuri = ProtoUtils.getMeciuri(response);
        return meciuri;
    }

    @Override
    public Bilet cumparaBilet(Meci meci, String numeClient, int nrLocuri) {
        Bilet bilet = new Bilet(meci, numeClient, nrLocuri);
        ProjectProto.Request req = ProtoUtils.createCumparaBiletRequest(bilet);
        sendRequest(req);
        ProjectProto.Response response = readResponse();
        if (response.getType() == ProjectProto.Response.Type.ERROR){
            String err=response.getMessage().toString();
//            throw new ChatException(err);
            return null;
        }
        Bilet bilet_salvat = ProtoUtils.getBilet(response.getBilet());
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

    private void sendRequest(ProjectProto.Request request) {
        try {
            System.out.println("Sending request ..."+request);
            //request.writeTo(output);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }

    private ProjectProto.Response readResponse() {
        ProjectProto.Response response=null;
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
            output = connection.getOutputStream();
            //output.flush();
            input = connection.getInputStream();     //new ObjectInputStream(connection.getInputStream());
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

    private void handleUpdate(ProjectProto.Response response){
        if (response.getType() == ProjectProto.Response.Type.NEW_MECIURI_LIST){
            Iterable<MeciL> meciuri = ProtoUtils.getMeciuri(response);
            System.out.println("Lista noua de meciuri "+meciuri);
            try {
                client.schimbareMeciuri(meciuri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(ProjectProto.Response response){
        if (response == null)
        {
            System.out.println("Am primit un raspuns null");
            return false;
        }
        return response.getType() == ProjectProto.Response.Type.NEW_MECIURI_LIST;
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    ProjectProto.Response response = ProjectProto.Response.parseDelimitedFrom(input);
                    System.out.println("response received "+response);

                    if (isUpdate(response)){
                        handleUpdate(response);
                    }else{
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }


    }
}
