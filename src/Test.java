import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;


/**
 * Created by aidar on 3/23/17.
 */
public class Test {
    public static void main(String args[]) {
        File folder = new File("/home/aidar/workspace/CNProjectClient/src/sharing_files");
        File[] listOfFiles = folder.listFiles();


        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                FMTFile fmtfile = new FMTFile(listOfFiles[i]);
                System.out.println(fmtfile);
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }

    }
}
