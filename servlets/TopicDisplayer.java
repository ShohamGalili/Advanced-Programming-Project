package servlets;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import graph.Message;
import graph.TopicManagerSingleton;
import server.RequestParser;

/**
 * The TopicDisplayer class handles HTTP requests, displays topics and their messages,
 * and provides responses with improved HTML styling.
 */
public class TopicDisplayer implements Servlet {

    // Concurrent map to store topics and their latest messages
    private static final Map<String, String> topicMessageMap = new ConcurrentHashMap<>();

    // PrintWriter for sending responses
    private PrintWriter responseWriter;

    // StringBuilder for building HTML content
    StringBuilder htmlBuilder = new StringBuilder();

    @Override
    public void handle(RequestParser.RequestInfo requestInfo, OutputStream clientOutput) throws IOException {
        String httpMethod = requestInfo.getHttpCommand();
        Map<String, String> queryParams = requestInfo.getParameters();
        responseWriter = new PrintWriter(clientOutput);

        // Singleton instance to manage topics
        TopicManagerSingleton.TopicManager topicManager = TopicManagerSingleton.get();

        // Check if any topics are present in the topic manager
        if (!topicManager.getTopics().isEmpty()) {
            // Populate the map with topics and their latest messages
            for (String topic : topicManager.getTopics().keySet()) {
                if (topic.startsWith("T")) {
                    topic = topic.substring(1);
                }
                topicMessageMap.put("T" + topic, topicManager.getTopic(topic).getLastMessage().toString());
            }

            // Only handle GET requests
            if (!"GET".equalsIgnoreCase(httpMethod)) {
                responseWriter.println("HTTP/1.1 405 Method Not Allowed");
                responseWriter.println("Content-Type: text/html");
                responseWriter.println();
                responseWriter.println("<html><body><h1>405 Method Not Allowed</h1></body></html>");
                responseWriter.flush();
                return;
            }

            // Extract parameters from the request
            String requestedTopic = queryParams.get("topic");
            String messageContent = queryParams.get("message");


            // Debug prints to check parameters
            System.out.println("Received topic: " + requestedTopic);
            System.out.println("Received message: " + messageContent);

            // Validate presence of topic and message parameters
            if (requestedTopic == null || messageContent == null) {
                responseWriter.println("HTTP/1.1 400 Bad Request");
                responseWriter.println("Content-Type: text/html");
                responseWriter.println();
                responseWriter.println( "<p style='font-weight: bold; font-family: Arial, sans-serif; color: #ff0000;'>400 Bad Request: Missing topic or message parameter<br><br>Please insert a valid number and try again</p>");

                responseWriter.flush();
                return;
            }

            // Check if messageContent is a valid number
            try {
                Double.parseDouble(messageContent); // Try parsing as a double
            } catch (NumberFormatException e) {
                responseWriter.println("HTTP/1.1 400 Bad Request");
                responseWriter.println("Content-Type: text/html");
                responseWriter.println();
                responseWriter.println( "<p style='font-weight: bold; font-family: Arial, sans-serif; color: #ff0000;'>Error:<br><br>Message content is not a valid number.<br><br>Please insert a valid number and try again</p>");
                responseWriter.flush();
                return;
            }

            // Check if the topic exists in the map
            Set<String> existingTopics = topicMessageMap.keySet();
            if (!existingTopics.contains(requestedTopic)) {
                // Send error message if the topic does not exist
                sendTopicNotExistError();
            } else {
                // Publish the new message to the specified topic
                topicManager.getTopic(requestedTopic.substring(1)).publish(new Message(messageContent));

                // Update the map with the latest messages
                for (String topic : existingTopics) {
                    topicMessageMap.put(topic, topicManager.getTopic(topic.substring(1)).getLastMessage().toString());
                }

                // Build the HTML response
                htmlBuilder.append("<html>");
                htmlBuilder.append("<head>");
                htmlBuilder.append("<title>Publish Result</title>");
                htmlBuilder.append("<style>");
                htmlBuilder.append("body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 20px; }");
                htmlBuilder.append("h1 { color: #ADD8E6; }"); // Light blue color for the h1 header
                htmlBuilder.append(".container { max-width: 800px; margin: 0 auto; padding: 20px; background-color: #fff; box-shadow: 0 0 10px rgba(0,0,0,0.1); }");
                htmlBuilder.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
                htmlBuilder.append("th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }");
                htmlBuilder.append("th { background-color: #4CAF50; color: white; }");
                htmlBuilder.append("tr:nth-child(even) { background-color: #f2f2f2; }");
                htmlBuilder.append("tr:hover { background-color: #ddd; }");
                htmlBuilder.append("</style>");
                htmlBuilder.append("</head>");
                htmlBuilder.append("<body>");
                htmlBuilder.append("<div class='container'>");
                htmlBuilder.append("<h1>Publish Result</h1>");
                htmlBuilder.append("<table>");
                htmlBuilder.append("<tr><th>Topic</th><th>Last Message</th></tr>");

                // Populate the HTML table with topics and their latest messages
                for (Map.Entry<String, String> entry : topicMessageMap.entrySet()) {
                    htmlBuilder.append("<tr>");
                    htmlBuilder.append("<td>").append(entry.getKey()).append("</td>");
                    htmlBuilder.append("<td>").append(entry.getValue()).append("</td>");
                    htmlBuilder.append("</tr>");
                }

                htmlBuilder.append("</table>");
                htmlBuilder.append("</div>");
                htmlBuilder.append("</body>");
                htmlBuilder.append("</html>");

                // Send the HTML response to the client
                clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.UTF_8));
                clientOutput.write("Content-Type: text/html\r\n\r\n".getBytes(StandardCharsets.UTF_8));
                clientOutput.write(htmlBuilder.toString().getBytes(StandardCharsets.UTF_8));
                clientOutput.flush();

                // Clear the HTML builder
                htmlBuilder.setLength(0);
            }
        } else {
            // Send an error message if no topics are available
            sendConfigNotUploadedError();
        }
    }

    @Override
    public void close() throws IOException {
        if (responseWriter != null) {
            responseWriter.close();
        }
    }

    // Helper method to send an error message for nonexistent topics
    private void sendTopicNotExistError() throws IOException {
        String path = System.getProperty("user.dir") + "/html_files";
        File errorFile = new File(path + "/temp.html");

        // Clear the existing content of the file
        try (PrintWriter writer = new PrintWriter(errorFile)) {
            writer.print(""); // Write an empty string to clear the content
        }

        // Create an HTML error message
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("<p style='font-weight: bold; font-family: Arial, sans-serif; color: #ff0000;'>Error:<br><br>The topic you selected does not exist.<br><br>Please use an existing topic</p>");

        // Write the error message to the file
        try (PrintWriter writer = new PrintWriter(errorFile)) {
            writer.write(errorMessage.toString());
        }

        // Send the error message as a response
        responseWriter.println("HTTP/1.1 200 OK");
        responseWriter.println("Content-Type: text/html");
        responseWriter.println("Connection: close");
        responseWriter.println("Content-Length: " + errorMessage.length());
        responseWriter.println();
        responseWriter.println(errorMessage.toString());
        responseWriter.flush();
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
        errorMessage.append("<p style='font-weight: bold; font-family: Arial, sans-serif; color: #ff0000;'>Error:<br><br>There are no topics whose value can be updated yet.<br><br>Please upload a graph configuration and try again</p>");

        // Write the error message to the file
        try (PrintWriter writer = new PrintWriter(errorFile)) {
            writer.write(errorMessage.toString());
        }

        // Send the error message as a response
        responseWriter.println("HTTP/1.1 200 OK");
        responseWriter.println("Content-Type: text/html");
        responseWriter.println("Connection: close");
        responseWriter.println("Content-Length: " + errorMessage.length());
        responseWriter.println();
        responseWriter.println(errorMessage.toString());
        responseWriter.flush();
    }

    // Helper method to send the final HTML response
    private void sendHtmlResponse() {
        responseWriter.println("HTTP/1.1 200 OK");
        responseWriter.println("Content-Type: text/html");
        responseWriter.println("Connection: close");
        responseWriter.println("Content-Length: " + htmlBuilder.length());
        responseWriter.println();
        responseWriter.println(htmlBuilder.toString());
        responseWriter.flush();
    }
}
