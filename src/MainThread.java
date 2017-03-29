import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

/**
 * Created by aidar on 3/27/17.
 */
public class MainThread implements Runnable {
    ServerSocket server= null;
    public MainThread(ServerSocket server) {
    this.server = server;
    }

    @Override
    public void run() {

        while (!Thread.interrupted()) {
            try {
                new Thread(new SenderThread(server.accept())).start();
                System.out.println("Client connected.");
            } catch (Exception e) {

            }

        }
    }
}
