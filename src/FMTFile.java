/**
 * Created by aidar on 3/21/17.
 */
public class FMTFile {
    private String filename;
    private String filetype;
    private int filesize;
    private String lastModified;
    private String ip;
    private int port;

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
            filename += current;
            i++;
        }
        i += 2;
        current = file.charAt(i);
        while (current != ',') {
            current = file.charAt(i);
            filetype += current;
            i++;
        }
        i += 2;
        current = file.charAt(i);
        while (current != ',') {
            current = file.charAt(i);
            filesize = filesize * 10 + current - '0';
            i++;
        }
        i += 2;
        current = file.charAt(i);
        while (current != ',') {
            current = file.charAt(i);
            lastModified += current;
            i++;
        }
        i += 2;
        current = file.charAt(i);
        while (current != ',') {
            current = file.charAt(i);
            ip += current;
            i++;
        }
        i += 2;
        current = file.charAt(i);
        while (current != ',') {
            current = file.charAt(i);
            port = port * 10 + current - '0';
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
        else if (month == 2 && day > 28) return false;
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
            while (current != '.' || j >= ip.length()) {
                current = ip.charAt(j);
                if (current < '0' || current > '9') return false;
                num += current - '0';
                j++;
            }
            j++;
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
        i += 2;

        //checking file type
        current = file.charAt(i);
        if (current == ',') return false;
        while (current != ',') {
            current = file.charAt(i);
            i++;
        }
        i += 2;

        //checking size
        current = file.charAt(i);
        if (current == ',') return false;
        while (current != ',') {
            current = file.charAt(i);
            if (current > '9' || current < '0') return false;
            i++;
        }
        i += 2;

        //checking date
        String date = "";
        current = file.charAt(i);
        if (current == ',') return false;
        while (current != ',') {
            current = file.charAt(i);
            date = date + current;
            i++;
        }
        if (!isDate(date)) return false;
        i += 2;

        //checking ip
        String ip = "";
        current = file.charAt(i);
        if (current == ',') return false;
        while (current != ',') {
            current = file.charAt(i);
            ip = ip + current;
            i++;
        }
        if (!isIp(ip)) return false;
        i += 2;

        current = file.charAt(i);
        if (current == '>') return false;
        while (current != '>') {
            current = file.charAt(i);
            if (current > '9' || current < '0') return false;
            i++;
        }
        return true;
    }

    public String getFilename() {
        return filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public int getFilesize() {
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
     *
     * @return
     */
    public String toString(){
        return "<"+filename+", "+filetype+", "+filesize+", "+lastModified+", "+ip+", "+port+">";
    }

}
