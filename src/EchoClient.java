/**
 * Created by aidar on 3/20/17.
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class EchoClient {
    public static void main(String[] args) throws IOException {

        String serverHostname = new String("127.0.0.1");
        if (args.length > 0)
            serverHostname = args[0];
        System.out.println("Attemping to connect to host " +
                serverHostname + " on port 10777.");

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

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
        List<String> sharedFiles = new ArrayList();

        //**********TEST, ADDING FILES*************
        System.out.println(echoSocket.getInetAddress().toString() + echoSocket.getPort());
        sharedFiles.add("<'Seka krsauchik', txt, 100, 20/03/17, " + echoSocket.getLocalAddress().toString().substring(1) + ", " + echoSocket.getLocalPort() + ">");
        sharedFiles.add("<'Seka krasauella', txt, 100, 20/03/17, " + echoSocket.getLocalAddress().toString().substring(1) + ", " + echoSocket.getLocalPort() + ">");
        System.out.println(sharedFiles);
        //*****************************************

        out.println("HELLO");
        serverResponse = in.readLine();
        System.out.println(serverResponse);
        if (serverResponse.equals("HI")) {
            System.out.printf("Yes");
            out.println(sharedFiles.toString());
//            while ((userInput = stdIn.readLine()) != null) {
//                out.println(userInput);
//                System.out.println("echo: " + in.readLine());
//                System.out.print("input: ");
//            }
        }
        else {
            System.out.println("NO");
        }
        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }
}

