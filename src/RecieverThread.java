import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * Created by aidar on 3/27/17.
 */
public class RecieverThread implements Runnable {
    private Socket server;
    private String filename;
    private PrintStream out = null;
    private InputStream in = null;
    private OutputStream fout = null;
    private JLabel status;

    RecieverThread(Socket newServer, String newFilename, JLabel status) {
        this.filename = newFilename;
        this.server = newServer;
        this.status = status;
        try {
            out = new PrintStream(server.getOutputStream(), true);
            in = server.getInputStream();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + server.getInetAddress());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + server.getInetAddress());
            System.exit(1);
        }
    }

    @Override
    public void run() {
        out.println("DOWNLOAD: " + filename);
        out.flush();
        try {
            fout = new FileOutputStream("/home/aidar/workspace/CNProjectClient/downloading_files/" + filename);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. ");
        }
        byte[] bytes = new byte[16 * 1024];

        int count;
        try {
            System.out.println("Downloading...");
            status.setText("Downloading...");
            while ((count = in.read(bytes)) > 0) {
                fout.write(bytes, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.close();
        try {
            in.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        status.setText("Download finished "+filename);
        System.out.println("Download finished "+filename);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        status.setText("");
    }
}
