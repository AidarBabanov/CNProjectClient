import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aidar on 4/12/17.
 */
public class MainFrame {
    private JPanel mainPanel;
    private JButton searchButton;
    private JButton downloadButton;
    private JTextField searchTextField;
    private JPanel header;
    private JLabel appNameLabel;
    private JTable filesList;
    private JScrollPane scrollPane;
    private JLabel statusLabel;

    private static Socket echoSocket = null;
    private static PrintWriter out = null;
    private static BufferedReader in = null;
    private static String toSend;

    public MainFrame() {

        String[] columnNames = {"Filename", "Filetype", "Filesize", "Last Modified", "IP", "Port"};
        DefaultTableModel model;
        model = new DefaultTableModel(0, 6);
        model.setColumnIdentifiers(columnNames);
        filesList.setModel(model);
        JFrame frame = new JFrame("FailMail");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<FMTFile>filesToChoose = search(searchTextField.getText());
                if (model.getRowCount() > 0) {
                    for (int i = model.getRowCount() - 1; i > -1; i--) {
                        model.removeRow(i);
                    }
                }
                for (FMTFile i:
                        filesToChoose) {
                    model.addRow((Vector) i.toList());
                }
            }
        });
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FMTFile download = null;
                try {
                    download = new FMTFile((Vector) model.getDataVector().elementAt(filesList.getSelectedRow()));
                    System.out.println(download);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Socket downloadSocket = null;
                try {
                    downloadSocket = new Socket(download.getIp(), download.getPort());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                RecieverThread recieve = new RecieverThread(downloadSocket, download.getFilename() + "." + download.getFiletype(), statusLabel);
                new Thread(recieve).start();
            }
        });
    }

    public static List<FMTFile> stringToFMTFile(String str) throws Exception {
        List<FMTFile> result = new LinkedList<FMTFile>();
        if (str.equals("NOT FOUND")) throw new Exception("NOT FOUND");
        else if (str.length() >= 7 && str.substring(0, 7).equals("FOUND: ")) {
            Matcher m = Pattern.compile("\\<(.*?)\\>").matcher(str.substring(7, str.length()));
            while (m.find()) {
                try {
                    result.add(new FMTFile("<" + m.group(1) + ">"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static List<FMTFile> search(String filename) {
        System.out.println(filename);
        List<FMTFile> filesToChoose = new LinkedList<>();
        out.println("SEARCH: " + filename);
        try {
            filesToChoose = stringToFMTFile(in.readLine());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return filesToChoose;
    }

    public static void main(String[] args) throws IOException {
        List<FMTFile> sharedFiles = new LinkedList<>();

        String serverHostname = new String("127.0.0.1");
        System.out.println("Attemping to connect to host " +
                serverHostname + " on port 10777.");


        ServerSocket server = new ServerSocket(0);

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
        String serverResponse = "";
        String userInput;

        File folder = new File("/home/aidar/workspace/CNProjectClient/sharing_files");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                try {
                    sharedFiles.add(new FMTFile(listOfFiles[i], echoSocket.getLocalAddress().toString().substring(1), server.getLocalPort()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        out.println("HELLO");
        serverResponse = in.readLine();
        if (serverResponse.equals("HI")) {
            out.println(sharedFiles.toString());
            Thread serverThread = new Thread(new MainThread(server));
            serverThread.start();
            System.out.println("Main thread running...");
            MainFrame frame = new MainFrame();
            while(true);
        } else System.out.println("Server doesn't respond.");

        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }
}
