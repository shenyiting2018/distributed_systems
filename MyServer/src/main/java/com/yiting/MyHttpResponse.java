package com.yiting;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class MyHttpResponse implements Serializable {
    private OutputStream output;

    public static Set<String> FORBIDDEN_FILES = new HashSet<>();
    static {
        FORBIDDEN_FILES.add("secrets.txt");
    }

    public MyHttpResponse(OutputStream output) {
        this.output = output;
    }

    public void sendErrorResponse(Exception e) throws IOException {
        if (e.getClass() == FileNotFoundException.class) {
            String dummyContent = "<html><head></head><body>Not Found!</body></html>\n";
            String responseBody = this.buildResponseBody(
                404,
                "Not Found",
                "text/html",
                    dummyContent.length(),
                dummyContent
            );
            output.write(responseBody.getBytes());
            output.close();
        } else if (e.getClass() == InvalidHttpRequestError.class) {
            String dummyContent = "<html><head></head><body>Request Invalid!</body></html>\n";
            String responseBody = this.buildResponseBody(
                    400,
                    "Invalid Request",
                    "text/html",
                    dummyContent.length(),
                    dummyContent
            );
            output.write(responseBody.getBytes());
            output.close();
        } else if (e.getClass() == ResourceForbiddenError.class) {
            String dummyContent = "<html><head></head><body>This resource needs permission to access!</body></html>\n";
            String responseBody = this.buildResponseBody(
                    403,
                    "Permission Rejected",
                    "text/html",
                    dummyContent.length(),
                    dummyContent
            );
            output.write(responseBody.getBytes());
            output.close();

        }
    }

    private String decideContentType(String fileName) {
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpet")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "text/plain";
        }
    }

    private String buildResponseBody(int statusCode, String status, String contentType, int contentLength, String content) {
        String stringTemplate = "HTTP/1.1 %s %s \r\n" +
                "Content-type: %s\r\n" +
                "Content-length: %s"+
                "\r\n\r\n" +
                "%s";
        String formattedString = String.format(stringTemplate, statusCode, status, contentType, contentLength, content);
        return formattedString;
    }

    public void sendResource(String fileName) {
        if (FORBIDDEN_FILES.contains(fileName)) {
            try {
                throw new ResourceForbiddenError();
            } catch (ResourceForbiddenError resourceForbiddenError) {
                resourceForbiddenError.printStackTrace();
                try {
                    sendErrorResponse(resourceForbiddenError);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String filePath = MyServer.serverFilesLocation + fileName;
        try {
            System.out.println(String.format("File path: %s", filePath));
            File file = new File(filePath);
            InputStream in = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            int contentLength = in.read(bytes, 0, bytes.length);
            String content = new String(bytes, 0);
            String responseBody = this.buildResponseBody(
                    200,
                    "OK",
                    this.decideContentType(fileName),
                    contentLength,
                    content
            );
            output.write(responseBody.getBytes());
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                sendErrorResponse(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
