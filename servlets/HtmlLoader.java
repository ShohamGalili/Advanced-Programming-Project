package servlets;

import server.RequestParser;

import java.io.*;

/**
 * The HtmlLoader class handles HTTP requests and serves HTML content from the server.
 * It can serve HTML files and clear file content if needed.
 */
public class HtmlLoader implements Servlet {
    private final String basePath;
    private PrintWriter outputToClient;

    /**
     * Constructor to initialize the base path for HTML files.
     *
     * @param basePath The base path to the directory containing HTML files.
     */
    public HtmlLoader(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public void handle(RequestParser.RequestInfo requestInfo, OutputStream clientStream) throws IOException {
        outputToClient = new PrintWriter(clientStream);

        // Extract the requested URI from the request information
        String requestedUri = requestInfo.getUri();

        // Serve an HTML form if the root URI or index.html is requested
        if ("/".equals(requestedUri) || "/index.html".equals(requestedUri)) {
            String htmlForm = "<!DOCTYPE html>" +
                    "<html>" +
                    "<body>" +
                    "<form action=\"http://localhost:8080/upload\" method=\"post\" enctype=\"multipart/form-data\">" +
                    "Select file to upload:" +
                    "<input type=\"file\" name=\"fileToUpload\" id=\"fileToUpload\">" +
                    "<input type=\"submit\" value=\"Upload File\" name=\"submit\">" +
                    "</form>" +
                    "</body>" +
                    "</html>";

            outputToClient.println("HTTP/1.1 200 OK");
            outputToClient.println("Content-Type: text/html");
            outputToClient.println("Content-Length: " + htmlForm.length());
            outputToClient.println();
            outputToClient.println(htmlForm);
            outputToClient.flush();
            return;
        }

        // Handle requests for application-specific URIs
        if (requestedUri.startsWith("/app/")) {
            // Extract the file name from the URI
            String htmlFileName = requestedUri.substring("/app/".length());

            // Verify the existence of the HTML file in the specified directory
            File htmlFile = new File(basePath + "/" + htmlFileName);
            if (!htmlFile.exists()) {
                outputToClient.println("HTTP/1.1 404 Not Found");
                outputToClient.println();
                outputToClient.println("404 Not Found");
                outputToClient.flush();
                return;
            }

            // Clear the file content if the requested file is "temp.html" or "graph.html"
            if (htmlFileName.equals("temp.html") || htmlFileName.equals("graph.html")) {
                clearFileContents(htmlFile);
            }

            // Read the HTML file content into a string
            String htmlContent = readHtmlFile(htmlFile);

            // Write the HTTP response headers
            clientStream.write("HTTP/1.1 200 OK\n".getBytes());
            if (htmlFileName.endsWith(".html"))
                clientStream.write("Content-Type: text/html\n".getBytes());
            else if (htmlFileName.endsWith(".css"))
                clientStream.write("Content-Type: text/css\n".getBytes());

            clientStream.write(("Content-Length: " + htmlContent.length() + "\n\n").getBytes());

            // Write the HTML file content to the response
            clientStream.write(htmlContent.getBytes());
            clientStream.flush();
        } else {
            // Respond with a 400 Bad Request status for unknown URIs
            outputToClient.println("HTTP/1.1 400 Bad Request");
            outputToClient.println();
            outputToClient.println("400 Bad Request");
            outputToClient.flush();
        }
    }

    /**
     * Reads the content of the specified HTML file and returns it as a string.
     *
     * @param file The HTML file to be read.
     * @return The content of the file as a string.
     * @throws IOException If an I/O error occurs.
     */
    public static String readHtmlFile(File file) throws IOException {
        StringBuilder fileContentBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileContentBuilder.append(line).append("\n"); // Append a newline after each line
            }
        }
        return fileContentBuilder.toString();
    }

    /**
     * Clears the content of the specified file by writing an empty string to it.
     *
     * @param file The file to be cleared.
     * @throws IOException If an I/O error occurs.
     */
    private void clearFileContents(File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print(""); // Write an empty string to clear the file content
        }
    }

    @Override
    public void close() throws IOException {
        if (outputToClient != null) {
            outputToClient.close();
        }
    }
}
