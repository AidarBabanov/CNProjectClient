/**
 * Created by aidar on 3/20/17.
 */

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        List<String> filesToChoose = new LinkedList<String>();
        List<String> sharedFiles = new LinkedList<String>();

        String serverHostname = new String("10.1.169.151");
        if (args.length > 0)
            serverHostname = args[0];
        System.out.println("Attemping to connect to host " +
                serverHostname + " on port 10777.");

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String toSend = "";

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
        String serverResponse;
        String userInput;

        //**********TEST, ADDING FILES*************
        sharedFiles.add("<seka, txt, 100, 20/03/17, " + echoSocket.getLocalAddress().toString().substring(1) + ", " + echoSocket.getLocalPort() + ">");
        sharedFiles.add("<ssekass, txt, 100, 20/03/17, " + echoSocket.getLocalAddress().toString().substring(1) + ", " + echoSocket.getLocalPort() + ">");
        System.out.println(sharedFiles);
        //*****************************************

        out.println("HELLO");
        serverResponse = in.readLine();
        if (serverResponse.equals("HI")) {
            out.println(sharedFiles.toString());
            System.out.print("Write your command: ");
            while ((userInput = stdIn.readLine()) != null) {
                System.out.print("Write your command: ");
                StringTokenizer st = new StringTokenizer(userInput);
                String command = st.nextToken();
                if (command.equals("s")) {
                    toSend = st.nextToken();
                    while (st.hasMoreTokens()) {
                        toSend = toSend + " " + st.nextToken();
                    }
                    out.println("SEARCH: " + toSend);
                    serverResponse = in.readLine();
                    System.out.println(serverResponse+" au");
                    //filesToChoose = serverResponse;
                } else if (command.equals("d")) {

                }

            }
        }
        System.out.println("Server don't response");
        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }
}

