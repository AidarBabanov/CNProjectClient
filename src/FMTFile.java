import java.io.File;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by aidar on 3/21/17.
 */
public class FMTFile {
    private String filename;
    private String filetype;
    private long filesize;
    private String lastModified;
    private String ip;
    private int port;

    /**
     *
     * @param file
     * @param ip
     * @param port
     * @throws Exception
     */
    public FMTFile(File file, String ip, int port) throws Exception {
        String fullname = file.getName();
        int dotIndex;
        if(!isIp(ip))throw new Exception("Wrong ip!");
        dotIndex = fullname.lastIndexOf(".");
        if (dotIndex == -1) {
            filename = fullname.substring(0, fullname.length());
            filetype = "";
        } else {
            filename = fullname.substring(0, dotIndex);
            filetype = fullname.substring(dotIndex + 1);
        }
        filesize = file.length();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(file.lastModified()));
        String year = "" + cal.get(Calendar.YEAR);
        String month = "" + cal.get(Calendar.MONTH);
        String day = "" + cal.get(Calendar.DATE);
        if (day.length() == 1) day = '0' + day;
        if (month.length() == 1) month = '0' + month;
        year = year.substring(2);
        lastModified = "" + day + "/" + month + "/" + year;
        this.ip = ip;
        this.port = port;
    }

    /**
     * @param file
     * @throws Exception
     */
    public FMTFile(String file) throws Exception {
        if (!isFile(file)) throw new Exception("This is not a file!");

        filename = "";
        filetype = "";
        filesize = 0;
        lastModified = "";
        ip = "";
        port = 0;

        char current;
        int i = 1;

        current = file.charAt(i);
        while (current != ',') {
            current = file.charAt(i);
            if (current != ',') filename += current;
            i++;
        }
        i++;
        current = file.charAt(i);
        while (current != ',') {
            current = file.charAt(i);
            if (current != ',') filetype += current;
            i++;
        }
        i++;
        current = file.charAt(i);
        while (current != ',') {
            current = file.charAt(i);
            if (current != ',') filesize = filesize * 10 + current - '0';
            i++;
        }
        i++;
        current = file.charAt(i);
        while (current != ',') {
            current = file.charAt(i);
            if (current != ',') lastModified += current;
            i++;
        }
        i++;
        current = file.charAt(i);
        while (current != ',') {
            current = file.charAt(i);
            if (current != ',') ip += current;
            i++;
        }
        i++;
        current = file.charAt(i);
        while (current != '>') {
            current = file.charAt(i);
            if (current != '>') port = port * 10 + current - '0';
            i++;
        }
    }

    /**
     * @param date
     * @return
     */
    public boolean isDate(String date) {
        int day = 0, month = 0, year = 0;
        boolean isLeap = false;

        if (date.length() != 8) return false;
        if (date.charAt(2) != '/' || date.charAt(5) != '/') return false;
        if (date.charAt(0) > '9' || date.charAt(0) < '0' || date.charAt(1) < '0' || date.charAt(1) > '9') return false;
        if (date.charAt(3) > '9' || date.charAt(3) < '0' || date.charAt(4) < '0' || date.charAt(4) > '9') return false;
        if (date.charAt(6) > '9' || date.charAt(6) < '0' || date.charAt(7) < '0' || date.charAt(7) > '9') return false;

        day = (date.charAt(0) - '0') * 10 + date.charAt(1) - '0';
        month = (date.charAt(3) - '0') * 10 + date.charAt(4) - '0';
        year = (date.charAt(6) - '0') * 10 + date.charAt(7) - '0';

        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) isLeap = true;
        if (month > 12) return false;
        if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day > 31)
            return false;
        else if (month == 2 && day > 29 && isLeap) return false;
        else if (month == 2 && day > 28 && !isLeap) return false;
        else if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) return false;
        return true;
    }

    /**
     * @param ip
     * @return
     */
    public boolean isIp(String ip) {
        int num = 0;
        int j = 0;
        char current;
        for (int i = 0; i < 4; i++) {
            current = ip.charAt(j);
            if (current == '.' || j >= ip.length()) return false;
            num = 0;
            while (current != '.' && j < ip.length()) {
                current = ip.charAt(j);
                if ((current < '0' || current > '9') && current != '.' && j < ip.length()) return false;
                if (current != '.' && j < ip.length()) num = num * 10 + current - '0';
                j++;
            }
            if (num > 255) return false;
        }
        return true;
    }

    /**
     * @param file
     * @return
     */
    public boolean isFile(String file) {

        if (file.charAt(0) != '<' || file.charAt(file.length() - 1) != '>') return false;
        int i = 1;
        char current = file.charAt(i);

        //checking filename
        if (current == ',') return false;
        while (current != ',') {
            current = file.charAt(i);
            i++;
        }
        if (file.charAt(i) != ' ') return false;
        i++;

        //checking file type
        current = file.charAt(i);
        while (current != ',') {
            current = file.charAt(i);
            i++;
        }
        if (file.charAt(i) != ' ') return false;
        i++;

        //checking size
        current = file.charAt(i);
        if (current == ',') return false;
        while (current != ',') {
            current = file.charAt(i);
            if ((current > '9' || current < '0') && current != ',') return false;
            i++;
        }
        if (file.charAt(i) != ' ') return false;
        i++;

        //checking date
        String date = "";
        current = file.charAt(i);
        if (current == ',') return false;
        while (current != ',') {
            current = file.charAt(i);
            if (current != ',') date = date + current;
            i++;
        }
        if (!isDate(date)) return false;
        if (file.charAt(i) != ' ') return false;
        i++;

        //checking ip
        String ip = "";
        current = file.charAt(i);
        if (current == ',') return false;
        while (current != ',') {
            current = file.charAt(i);
            if (current != ',') ip = ip + current;
            i++;
        }
        if (!isIp(ip)) return false;
        if (file.charAt(i) != ' ') return false;
        i++;

        //checking port
        current = file.charAt(i);
        if (current == '>') return false;
        while (current != '>') {
            current = file.charAt(i);
            if ((current > '9' || current < '0') && current != '>') return false;
            i++;
        }

        if (i == file.length()) return true;
        else return false;
    }

    public String getFilename() {
        return filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public long getFilesize() {
        return filesize;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return
     */
    public String toString() {
        return "<" + filename + ", " + filetype + ", " + filesize + ", " + lastModified + ", " + ip + ", " + port + ">";
    }

}
