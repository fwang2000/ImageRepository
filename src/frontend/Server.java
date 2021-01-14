package frontend;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class Server implements Runnable {

    static final File WEB_ROOT = new File(".");
    static final String DEFAULT_FILE = "index.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final String METHOD_NOT_SUPPORTED = "500.html";

    // port for connection
    static final int PORT = 2000;

    // verbose mode
    static final boolean verbose = true;

    // Client Connection
    private Socket connection;

    public Server(Socket connection) {

        this.connection = connection;
    }

    public static void main(String[] args) {

        try {

            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("frontend.Server started.\nListening for connections on port: " + PORT + "...\n");

            // run until user halts the server
            while (true) {

                Server myServer = new Server(serverSocket.accept());

                if (verbose) {

                    System.out.println("Connection Opened. (" + new Date() + ")");
                }

                Thread thread = new Thread(myServer);
                thread.start();
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new PrintWriter(connection.getOutputStream());

            dataOut = new BufferedOutputStream(connection.getOutputStream());

            String input = in.readLine();

            StringTokenizer parser = new StringTokenizer(input);
            String method = parser.nextToken().toUpperCase();

            fileRequested = parser.nextToken().toLowerCase();

            if (!method.equals("GET") && !method.equals("HEAD")) {

                if (verbose) {
                    System.out.println("501 Not Implemented: " + method + " method.");
                }

                File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
                int fileLength = (int) file.length();

                String contentMimeType = "text/html";

                byte[] fileData = readFileData(file, fileLength);

                out.println("HTTP/1.1 501 Not Implemented");
                out.println("frontend.Server: Java HTTP frontend.Server: 1.0");
                out.println("Date: " + new Date());
                out.println("Content-type: " + contentMimeType);
                out.println("Content-length: " + fileLength);
                out.println();
                out.flush();

                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();

            } else {

                if (fileRequested.endsWith("/")) {

                    fileRequested += DEFAULT_FILE;
                }

                File file = new File(WEB_ROOT, fileRequested);
                int fileLength = (int) file.length();
                String content = getContentType(fileRequested);

                if (method.equals("GET")) {
                    byte[] fileData = readFileData(file, fileLength);

                    out.println("HTTP/1.1 501 Not Implemented");
                    out.println("frontend.Server: Java HTTP frontend.Server: 1.0");
                    out.println("Date: " + new Date());
                    out.println("Content-type: " + content);
                    out.println("Content-length: " + fileLength);
                    out.println();
                    out.flush();

                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();
                }

                if (verbose) {
                    System.out.println("File " + fileRequested + " of type " + content + " returned.");
                }
            }

        } catch (FileNotFoundException fileNotFoundException) {

            try {

                fileNotFound(out, dataOut, fileRequested);

            } catch (IOException e) {

                System.err.println("File Not Found Error: " + e.getMessage());
            }

        } catch (IOException e) {

            System.err.println("frontend.Server Error: " + e);

        } finally {

            try {
                in.close();
                out.close();
                dataOut.close();
                connection.close();

            } catch (Exception e) {
                System.err.println("Error closing stream: " + e.getMessage());
            }

            if (verbose) {

                System.out.println("Connection Closed");
            }
        }
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {

        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try {

            fileIn = new FileInputStream(file);
            fileIn.read(fileData);

        } finally {

            if (fileIn != null) {

                fileIn.close();
            }
        }

        return fileData;
    }

    private String getContentType(String fileRequested) {

        if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html")) {

            return "text/html";

        } else {

            return "text/plain";
        }
    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {

        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

        out.println("HTTP/1.1 501 Not Implemented");
        out.println("frontend.Server: Java HTTP frontend.Server: 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println();
        out.flush();

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (verbose) {

            System.out.println("File " + fileRequested + " not found.");
        }
    }
}
