package org.example.networking;

import org.example.services.IServices;

import java.net.Socket;

public class ConcurrentServer extends AbsConcurrentServer {
    private IServices chatServer;
    public ConcurrentServer(int port, IServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
         ProtoClientWorker worker=new ProtoClientWorker(chatServer, client);
//        ClientWorker worker=new ChatClientRpcReflectionWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
