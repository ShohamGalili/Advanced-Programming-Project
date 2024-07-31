package test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import servlets.Servlet;
import server.RequestParser.RequestInfo;

public class SimpleServlet implements Servlet {

    @Override
	public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
        // Set content type
        PrintWriter out = new PrintWriter(toClient, true);

        // Get parameters from request
        String param1 = ri.getParameters().get("param1");
        String param2 = ri.getParameters().get("param2");

        // Validate parameters
        if (param1 == null || param2 == null) {
            out.println("Error: Missing parameters 'param1' or 'param2'");
            return;
        }

        // Perform calculation
        try {
            int num1 = Integer.parseInt(param1);
            int num2 = Integer.parseInt(param2);
            // Print parameters for debugging
            System.out.println("Received parameters: param1=" + param1 + ", param2=" + param2);

            // Perform calculation
            int result = num1 + num2;
            System.out.println("Calculation result: " + num1 + " + " + num2 + " = " + result);

            // Print result to client
            out.println("Result of " + num1 + " + " + num2 + " = " + result);
            
            // Print result to client
            out.println("Result of " + num1 + " + " + num2 + " = " + result);
        } catch (NumberFormatException e) {
            out.println("Error: Invalid parameters 'param1' or 'param2'. Please provide integers.");
        } finally {
            out.close();
        }
    }

    @Override
    public void close() throws IOException {
        // Cleanup resources if needed
    }

	
}

