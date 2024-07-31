package servlets;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import configs.GenericConfig;
import configs.Graph;
import graph.TopicManagerSingleton;
import server.RequestParser;
import views.HtmlGraphWriter;

/**
 * This class is responsible for handling the loading of configuration files,
 * creating graphs, and generating corresponding HTML files.
 */
public class ConfLoader implements Servlet {
    private PrintWriter clientOut;

    /**
     * Handles the client request and processes the configuration file.
     *
     * @param ri       The request information containing parameters and content.
     * @param toClient The output stream to send the response to the client.
     * @throws IOException If an error occurs during file handling.
     */
    @Override
    public void handle(RequestParser.RequestInfo ri, OutputStream toClient) throws IOException {
        // Initialize PrintWriter for client response
        clientOut = new PrintWriter(toClient);

        // Clear existing topics in TopicManagerSingleton
        TopicManagerSingleton.get().getTopics().clear();

        // Retrieve the filename from the request parameters
        String fileName = ri.getParameters().get("filename");

        // Remove quotes from the filename if present
        if (fileName != "") {
            fileName = fileName.replace("\"", "");
        }
        else{ //if not upload a file
            sendConfigNotUploadedError();
            return;
        }

        // Retrieve the file content from the request body
        String fileContent = new String(ri.getContent(), StandardCharsets.UTF_8);

        Path filePath = null;
        // If a filename is provided, save the file content
        if (fileName != null) {
            String currentWorkingDirectory = System.getProperty("user.dir") + "/uploads";
            Path directoryPath = Paths.get(currentWorkingDirectory);
            Files.createDirectories(directoryPath);
            // Save the content as a .conf file
            filePath = Paths.get(currentWorkingDirectory + "/" + fileName);
            Files.writeString(filePath, fileContent);
        } else {
            System.out.println("No file uploaded.");
        }

        // Create and configure the graph from the config file
        GenericConfig config = new GenericConfig();
        config.setConfFile(String.valueOf(filePath));
        try{
            config.create();
        }
        catch(Exception e){
            sendInputsError();
        }

        // Create the graph from the topics
        Graph graph = new Graph();
        graph.createFromTopics();

        // Check for cycles in the graph
        if (graph.hasCycles()) {
            // Send an HTML response indicating the graph has cycles
            sendWarningCyclesMsg();
            return;
        }

        // Generate the HTML representation of the graph
        HtmlGraphWriter.getGraphHTML(graph);

        // Send the generated graph HTML to the client
        sendHtmlResponse("graph.html");

        // Clear the content of the temporary HTML file
        clearFileContent(System.getProperty("user.dir") + "/html_files/temp.html");
    }

    /**
     * Closes the response writer.
     *
     * @throws IOException If an error occurs during closing.
     */
    @Override
    public void close() throws IOException {
        if (clientOut != null) {
            clientOut.close();
        }
    }

    /**
     * Clears the content of the specified file.
     *
     * @param filePath The path of the file to be cleared.
     * @throws IOException If an error occurs during file handling.
     */
    private void clearFileContent(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(path))) {
            writer.print(""); // Write an empty string to clear the content
        }
    }

    /**
     * Sends an HTML response to the client based on the specified HTML file.
     *
     * @param htmlFileName The name of the HTML file to be sent.
     * @throws IOException If an error occurs during file handling.
     */
    private void sendHtmlResponse(String htmlFileName) throws IOException {
        String path = System.getProperty("user.dir") + "/html_files";
        String htmlContent = HtmlLoader.readHtmlFile(new File(path + "/" + htmlFileName));
        clientOut.println("HTTP/1.1 200 OK");
        clientOut.println("Content-Type: text/html");
        clientOut.println("Connection: close");
        clientOut.println("Content-Length: " + htmlContent.length());
        clientOut.println();
        clientOut.println(htmlContent);
        clientOut.flush();
    }

    // Helper method to send an error message for configuration not uploaded
    private void sendConfigNotUploadedError() throws IOException {
        String path = System.getProperty("user.dir") + "/html_files";
        File errorFile = new File(path + "/temp.html");

        // Clear the existing content of the file
        try (PrintWriter writer = new PrintWriter(errorFile)) {
            writer.print(""); // Write an empty string to clear the content
        }

        // Create an HTML error message
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("<p style='font-weight: bold; font-family: Arial, sans-serif; color: #ff0000;'>Error:<br><br>No configuration file selected. <br><br>Please select a file and try again</p>");

        // Write the error message to the file
        try (PrintWriter writer = new PrintWriter(errorFile)) {
            writer.write(errorMessage.toString());
        }

        // Send the error message as a response
        clientOut.println("HTTP/1.1 200 OK");
        clientOut.println("Content-Type: text/html");
        clientOut.println("Connection: close");
        clientOut.println("Content-Length: " + errorMessage.length());
        clientOut.println();
        clientOut.println(errorMessage.toString());
        clientOut.flush();
    }

    // Helper method to send an error message for configuration not uploaded
    private void sendWarningCyclesMsg() throws IOException {
        String path = System.getProperty("user.dir") + "/html_files";
        File errorFile = new File(path + "/temp.html");

        // Clear the existing content of the file
        try (PrintWriter writer = new PrintWriter(errorFile)) {
            writer.print(""); // Write an empty string to clear the content
        }

        // Create an HTML error message
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("<p style='font-weight: bold; font-family: Arial, sans-serif; color: #ff0000;'>Warning!<br><br>The configuration describes a graph with a circle. <br><br>Please insert a graph without a cycle and try again.</p>");

        // Write the error message to the file
        try (PrintWriter writer = new PrintWriter(errorFile)) {
            writer.write(errorMessage.toString());
        }

        // Send the error message as a response
        clientOut.println("HTTP/1.1 200 OK");
        clientOut.println("Content-Type: text/html");
        clientOut.println("Connection: close");
        clientOut.println("Content-Length: " + errorMessage.length());
        clientOut.println();
        clientOut.println(errorMessage.toString());
        clientOut.flush();
    }

    // Helper method to send an error message for configuration not uploaded
    private void sendInputsError() throws IOException {
        String path = System.getProperty("user.dir") + "/html_files";
        File errorFile = new File(path + "/temp.html");

        // Clear the existing content of the file
        try (PrintWriter writer = new PrintWriter(errorFile)) {
            writer.print(""); // Write an empty string to clear the content
        }

        // Create an HTML error message
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("<p style='font-weight: bold; font-family: Arial, sans-serif; color: #ff0000;'>Error:<br><br>Number of subs are not valid to the operation. <br><br>Please try again</p>");

        // Write the error message to the file
        try (PrintWriter writer = new PrintWriter(errorFile)) {
            writer.write(errorMessage.toString());
        }

        // Send the error message as a response
        clientOut.println("HTTP/1.1 200 OK");
        clientOut.println("Content-Type: text/html");
        clientOut.println("Connection: close");
        clientOut.println("Content-Length: " + errorMessage.length());
        clientOut.println();
        clientOut.println(errorMessage.toString());
        clientOut.flush();
    }
}
