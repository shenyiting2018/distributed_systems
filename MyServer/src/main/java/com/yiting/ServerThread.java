package com.yiting;

import java.io.*;
import java.net.Socket;

/**
 * A dispatch thread to handle each connection
 */
public class ServerThread implements Runnable {
    private Socket clientSocket;
    private MyHttpRequest request;
    private MyHttpResponse response;

    public ServerThread(Socket socket) {
        this.clientSocket = socket;
        try {
            this.request = new MyHttpRequest(clientSocket.getInputStream());
            this.response = new MyHttpResponse(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Server thread dispatched at " + Thread.currentThread().getId());
        try {
            this.request.parseInputStream();
            System.out.println(String.format(
                    "Request method: %s, resource: %s, protocol",
                    this.request.requestMethod,
                    this.request.requestedResource,
                    this.request.protocol
                )
            );
            this.response.sendResource(this.request.requestedResource);
            this.clientSocket.close();
            System.out.println("Socket closed at " + Thread.currentThread().getId());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidHttpRequestError invalidHttpRequestError) {
            try {
                this.response.sendErrorResponse(invalidHttpRequestError);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
