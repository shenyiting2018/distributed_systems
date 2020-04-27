package com.yiting;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * An HttpRequest object to help extract information from the input stream.
 */
public class MyHttpRequest {
    public static String DEFAULT_RESOURCE = "index.html";
    private InputStream input;
    public String requestedResource;
    public String requestMethod;
    public String protocol;

    public MyHttpRequest(InputStream input) {
        this.input = input;
    }

    public void parseInputStream() throws IOException, InvalidHttpRequestError {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(this.input));
            System.out.println("Start reading request");
            StringBuffer buffer = new StringBuffer();
            String nextLine = in.readLine();
//            if (nextLine == null || nextLine.isEmpty()) {
//                throw new InvalidHttpRequestError();
//            }
            buffer.append(nextLine);
            System.out.println("Read string: " + buffer.toString());
            this.parseURL(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void parseURL(String urlString) throws InvalidHttpRequestError {
        String[] subStrings = urlString.split("\\s+");
        if (subStrings.length != 3) {
            System.out.println("Invalid! " + urlString);
            throw new InvalidHttpRequestError();
        }

        this.requestMethod = subStrings[0];
        this.protocol = subStrings[2];
        subStrings[1] = subStrings[1].trim();

        if ("/".equals(subStrings[1])) {
            this.requestedResource = DEFAULT_RESOURCE;
        } else if (subStrings[1].contains(".")) {
            this.requestedResource = subStrings[1].substring(1); // remove /
        } else {
            System.out.println("Invalid! " + urlString);
            throw new InvalidHttpRequestError();
        }
    }

}
