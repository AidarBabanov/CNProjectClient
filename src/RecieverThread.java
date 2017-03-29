import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by aidar on 3/27/17.
 */
public class RecieverThread implements Runnable {
    private Socket server;
    private String filename;
    private PrintStream out = null;
    private InputStream in = null;
    private OutputStream fout = null;

    RecieverThread(Socket newServer, String newFilename) {
        this.filename = newFilename;
        this.server = newServer;
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
        out.println("DOWNLOAD: "+filename);
        out.flush();
        try {
            fout = new FileOutputStream("/home/aidar/workspace/CNProjectClient/downloading_files/"+filename);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. ");
        }
        byte[] bytes = new byte[16*1024];

        int count;
        try {
            System.out.println("Downloading...");
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

        System.out.println("Download finished.");
    }
}
