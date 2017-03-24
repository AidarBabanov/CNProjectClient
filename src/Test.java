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
        FMTFile file = null;
        try {
            file = new FMTFile("<asd, a, 1a00, 22/12/17, 127.0.0.1, 123>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(file);
//        File folder = new File("/home/aidar/workspace/CNProjectClient/src/sharing_files");
//        File[] listOfFiles = folder.listFiles();
//
//
//        for (int i = 0; i < listOfFiles.length; i++) {
//            if (listOfFiles[i].isFile()) {
//                FMTFile fmtfile = null;
//                try {
//                    fmtfile = new FMTFile(listOfFiles[i], "127.0.0.1", 10777);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                System.out.println(fmtfile);
//            } else if (listOfFiles[i].isDirectory()) {
//                System.out.println("Directory " + listOfFiles[i].getName());
//            }
//        }

    }
}
