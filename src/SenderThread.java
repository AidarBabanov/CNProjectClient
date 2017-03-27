import java.io.*;
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
    private  InputStream fin = null;
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
        String clientResponse = null;
        try {
            clientResponse = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (clientResponse.length() >= 10 && clientResponse.substring(0, 10).equals("DOWNLOAD: ")) {
            filename = clientResponse.substring(10, clientResponse.length());
            File file = new File("/home/aidar/workspace/CNProjectClient/sharing_files" + filename);
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                System.out.println("File is too large.");
            }
            byte[] bytes = new byte[16 * 1024];

            try {
                fin = new FileInputStream(file);

                int count;
                while ((count = fin.read(bytes)) > 0) {
                    out.write(String.valueOf(bytes), 0, count);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            out.close();
            try {
                in.close();
                fin.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
