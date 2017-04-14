import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by aidar on 3/25/17.
 */
public class SenderThread implements Runnable {
    private Socket client;
    private String filename;
    private OutputStream out = null;
    private BufferedReader in = null;
    private InputStream fin = null;

    SenderThread(Socket newClient) {
        this.client = newClient;
        try {
            out = client.getOutputStream();
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
        String clientResponse = null;

        try {
            clientResponse = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (clientResponse.length() >= 10 && clientResponse.substring(0, 10).equals("DOWNLOAD: ")) {
            filename = clientResponse.substring(10, clientResponse.length());
            File file = new File("/home/aidar/workspace/CNProjectClient/sharing_files/" +
                    "" + filename);
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                System.out.println("File is too large.");
            }
            byte[] bytes = new byte[16 * 1024];

            try {
                fin = new FileInputStream(file);
                int count;
                System.out.println("File sending...");
                while ((count = fin.read(bytes)) > 0) {
                    out.write(bytes, 0, count);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                out.close();
                in.close();
                fin.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Sending finished.");

    }
}
