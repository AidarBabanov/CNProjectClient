import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by aidar on 3/25/17.
 */
public class SenderThread implements Runnable {
    private Socket client;
    private String filename;
    private PrintWriter out = null;
    private BufferedReader in = null;

    SenderThread(Socket client) {
        this.client = client;
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + client.getInetAddress());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + client.getInetAddress());
            System.exit(1);
        }
    }

    @Override
    public void run() {
        String clientResponse =null;
        try {
            clientResponse = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(clientResponse.length()>=10 && clientResponse.substring(0, 10).equals("DOWNLOAD: ")){
            filename = clientResponse.substring(10, clientResponse.length());
        }
    }
}
