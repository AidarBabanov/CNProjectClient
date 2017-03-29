/**
 * Created by aidar on 3/20/17.
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        List<FMTFile> filesToChoose = new ArrayList<FMTFile>();
        List<FMTFile> sharedFiles = new ArrayList<FMTFile>();

        String serverHostname = new String("10.110.100.216");
        System.out.println("Attemping to connect to host " +
                serverHostname + " on port 10777.");

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String toSend;

        ServerSocket server = new ServerSocket(0);

        try {
            echoSocket = new Socket(serverHostname, 10777);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + serverHostname);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(
                new InputStreamReader(System.in));
        String serverResponse = "";
        String userInput;

        File folder = new File("/home/aidar/workspace/CNProjectClient/sharing_files");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                try {
                    sharedFiles.add(new FMTFile(listOfFiles[i], echoSocket.getLocalAddress().toString().substring(1), server.getLocalPort()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        out.println("HELLO");
        serverResponse = in.readLine();
        if (serverResponse.equals("HI")) {
            System.out.println("Connected to the server.");
            System.out.println("IP: " + echoSocket.getLocalAddress().toString().substring(1));
            System.out.println("Server port: " + server.getLocalPort());
            System.out.println("Client port: " + echoSocket.getLocalPort());
            out.println(sharedFiles.toString());
            Thread serverThread = new Thread(new MainThread(server));
            serverThread.start();
            System.out.println("Main thread running...");

            while (true) {
                System.out.print("Write your command: ");
                userInput = stdIn.readLine();
                StringTokenizer st = new StringTokenizer(userInput);
                String command = st.nextToken();

                if (command.equals("s")) {
                    toSend = st.nextToken();
                    while (st.hasMoreTokens()) {
                        toSend = toSend + " " + st.nextToken();
                    }
                    out.println("SEARCH: " + toSend);
                    serverResponse = in.readLine();
                    if (serverResponse.equals("NOT FOUND")) System.out.println(serverResponse);
                    else if (serverResponse.length() >= 7 && serverResponse.substring(0, 7).equals("FOUND: ")) {
                        Matcher m = Pattern.compile("\\<(.*?)\\>").matcher(serverResponse.substring(7, serverResponse.length()));
                        while (m.find()) {
                            try {
                                filesToChoose.add(new FMTFile("<" + m.group(1) + ">"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("FOUND: ");
                        System.out.println(filesToChoose);
                    }

                } else if (command.equals("d")) {

                    FMTFile download = filesToChoose.get(0);
                    Socket downloadSocket = new Socket(download.getIp(), download.getPort());
                    RecieverThread recieve = new RecieverThread(downloadSocket, download.getFilename() + "." + download.getFiletype());
                    new Thread(recieve).start();

                } else if (command.equals("b")) {
                    out.print("BYE");
                    System.out.println("End of program...");
                    break;
                }

            }
            server.close();
            serverThread.interrupt();
        } else System.out.println("Server doesn't respond.");

        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }
}

