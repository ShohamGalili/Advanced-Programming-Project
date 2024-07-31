package test;

import server.RequestParser;
import servlets.Servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class SubServlet  implements Servlet {
    @Override
    public void handle(RequestParser.RequestInfo requestInfo, OutputStream responseStream) throws IOException {
        Map<String, String> parameters = requestInfo.getParameters();
        int a = Integer.parseInt(parameters.getOrDefault("a", "0"));
        int b = Integer.parseInt(parameters.getOrDefault("b", "0"));
        int sub = a - b;
        String response = "Result: " + sub;

        responseStream.write(("HTTP/1.1 200 OK\n").getBytes());
        responseStream.write(("Content-Length: " + response.length() + "\n").getBytes());
        responseStream.write(("\n").getBytes());
        responseStream.write(response.getBytes());
        responseStream.write(("\n").getBytes());
        responseStream.flush();
    }

    @Override
    public void close() throws IOException
    {
        //do nothing
    }
}


