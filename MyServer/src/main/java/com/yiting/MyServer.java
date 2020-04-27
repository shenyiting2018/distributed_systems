package com.yiting;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;

public class MyServer {
    public static final int DEFAULT_PORT = 8080;
    private int port;
    private ServerSocket serverSocket;
    public static String serverFilesLocation = "./";

    private MyServer(int port) {
        try {
            this.port = port;
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static MyServer instance;

    public static MyServer getInstance(int port) {
        if (instance == null) {
            instance = new MyServer(port);
        }
        return instance;
    }

    public void run() {
        System.out.println(String.format("Server started at port: %d", this.port));
        while (true) {
            Socket sock = null;
            try {
                sock = this.serverSocket.accept();
                System.out.println(
                        String.format(
                                "Found socket, socket status: closed:%s, connected: %s",
                                sock.isClosed(),
                                sock.isConnected()
                        )
                );
                ServerThread newServerThread = new ServerThread(sock);
                new Thread(newServerThread).start();;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main (String args[]) {
        if (args.length < 2) {
            System.out.println("Need to provide server file location and port to run");
            return;
        }
        int port = Integer.valueOf(args[0]);
        String serverLocation = args[1];
        MyServer.serverFilesLocation = serverLocation;
        MyServer myServer = MyServer.getInstance(port);
        myServer.run();
    }
}