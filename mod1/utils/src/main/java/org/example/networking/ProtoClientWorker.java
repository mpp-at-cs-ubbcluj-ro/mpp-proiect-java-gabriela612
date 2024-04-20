package org.example.networking;

import org.example.domain.Angajat;
import org.example.domain.Bilet;
import org.example.domain.Meci;
import org.example.domain.MeciL;
import org.example.observer.IObserver;
import org.example.proto.ProjectProto;
import org.example.services.IServices;
import org.example.utils.ProtoUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ProtoClientWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ProtoClientWorker(IServices server, Socket connection) {
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
                ProjectProto.Response response = handleRequest((ProjectProto.Request)request);
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
        ProjectProto.Response resp = ProtoUtils.createSchimbareMeciuriResponse((ArrayList<MeciL>) meciuri);
        System.out.println("Schimbare meciuri " + meciuri);
        try {
            sendResponse(resp);
        } catch (IOException e) {
//            throw new Exception("Sending error: "+e);
        }
    }

    private static ProjectProto.Response okResponse = ProtoUtils.createOkResponse();

    private ProjectProto.Response handleRequest(ProjectProto.Request request){
        ProjectProto.Response response = null;
        if (request.getType() == ProjectProto.Request.Type.LOGIN){
            System.out.println("Login request ..."+request.getType());
            Angajat angajat = ProtoUtils.getAngajat(request.getAngajat());
            System.out.println(angajat.getUsername());
            try {
                Integer idAngajat = server.login(angajat.getUsername(), angajat.getParola(), this);

                return ProtoUtils.createLoginResponse(idAngajat);
            } catch (Exception e) {
                connected=false;
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == ProjectProto.Request.Type.LOGOUT){
            System.out.println("Logout request");
            // LogoutRequest logReq=(LogoutRequest)request;
            Integer idAngajat = (Integer) request.getIdAngajat();
            try {
                server.logout(idAngajat);
                connected=false;
                return okResponse;

            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == ProjectProto.Request.Type.GET_MECIURI){
            System.out.println("GetMeciuriRequest ...");
            try {
                return ProtoUtils.createGetMeciuriResponse((ArrayList<MeciL>) server.getMeciuri());
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == ProjectProto.Request.Type.GET_MECIURI_LIBERE){
            System.out.println("GetMeciuriLibereRequest ...");
            try {
                return ProtoUtils.createGetMeciuriResponse((ArrayList<MeciL>) server.getMeciuriLibere());
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == ProjectProto.Request.Type.CUMPARA_BILET){
            System.out.println("Vinde bilet request ..."+request.getType());
            Bilet bilet = ProtoUtils.getBilet(request.getBilet());
            try {
                Bilet bilet_nou = server.cumparaBilet(bilet.getMeci(), bilet.getNumeClient(), bilet.getNrLocuri());
                return ProtoUtils.createCumparaBiletResponse(bilet_nou);
            } catch (Exception e) {
                connected=false;
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == ProjectProto.Request.Type.LOCURI_DISPONIBILE){
            System.out.println("Locuri disponibile ..."+request.getType());
            Meci meci = ProtoUtils.getMeci(request.getMeci());
            try {
                Integer nrLocuri = server.nrLocuriDisponibileMeci(meci);
                return ProtoUtils.createNrLocuriDisponibileMeciResponse(nrLocuri);
            } catch (Exception e) {
                connected=false;
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }

        return response;
    }

    private void sendResponse(ProjectProto.Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
}
