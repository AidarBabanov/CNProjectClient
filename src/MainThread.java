import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by aidar on 3/27/17.
 */
public class MainThread implements Runnable {

    public MainThread() {
    }

    @Override
    public void run() {
        SenderThread sender = null;
        while (true) {
            try {
                sender = new SenderThread(new ServerSocket(4444).accept());
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(sender).start();
        }
    }
}
