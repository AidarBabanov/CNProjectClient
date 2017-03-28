import java.io.IOException;
import java.net.ServerSocket;

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
        SenderThread sender = null;
        while (true) {
            try {
                sender = new SenderThread(server.accept());
                System.out.println("Client connected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(sender).start();
        }
    }
}
