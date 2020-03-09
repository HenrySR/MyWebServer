import java.util.Date;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

class HTTPResponse{
    private String server;
    private String command;
    private Date date;
    private String status;
    private String path;
    private String rPath;
    private File file;
    private Date ifModifiedSince;

    public HTTPResponse(String status, String path, String command, Date ifModifiedSince, String rootPath) {
        server = "Best Server Ever 1.0";
        this.status = status;
        this.path = path;
        rPath = rootPath;
        this.command = command;
        this.ifModifiedSince = ifModifiedSince;
        date = new Date();
    }

    public String getResponse(){
        
        if(command.equals("HEAD")){
            return responseHEAD();
        } else if (command.equals("GET")){
            return responseGET();
        } else {
            status = "501 Not Implemented";
            return responseGET();
        }
    }
    
    public byte[] getFile(){
        try {
            System.out.println(file.getAbsolutePath());
            byte[] fileData = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            System.out.println("file length: " + fileData.length);
            return fileData;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
        
    }

    private long getFileSize(){
        try{
            return Files.size(Paths.get(file.getAbsolutePath()));
        } catch (IOException e){
            System.out.println(e);
            return -1;
        }
    }

    private String responseGET(){         
        setFile(path);
        checkFile();
        if(ifModifiedSince != null){
            checkValidDate();
        }
        setErrorFile();
        
         Date lastModified = new Date(file.lastModified());
        //String curDate = Date.toString(date);
        String response ="HTTP/1.1 " + status + "/r/n" + "Date: " + date + "/r/n" + "Server: " + server + 
        "/r/n" + "Last-Modified: " +  lastModified + "/r/n" + "Content-Length: " + getFileSize() + "/r/n"; 
        System.out.println("In responseGET: "+response);
        return response;
    }
    private String responseHEAD(){
        setFile(path);
        checkFile();
        if(ifModifiedSince != null){
            checkValidDate();
        }
        setErrorFile();
        String response ="HTTP/1.1 " + status + "/r/n" + "Date: " + date + "/r/n" + "Server: " + server + 
        "/r/n" + "Content-Length: " + getFileSize() + "/r/n"; 

        return response;
    }

    private void setFile(String path){
        try {
            file = new File(path);
        } catch (NullPointerException e) {
            status = "404 Not Found";
            setFile(rPath + "/404error.html");
        }
    }

    private void checkFile(){
        if(!file.exists()) {
            status = "404 Not Found";    
            setFile(rPath + "/404error.html");
        }
    }

    private void checkValidDate(){
        Date lastModified = new Date(file.lastModified());
        if (lastModified.before(date)){
            status = "304 Not Modified";
        }
    }

    private void setErrorFile(){
        checkFile(); 
        if(status.equals("200 OK")){
            if(path.endsWith("/")){
                setFile(path + "index.html");
            }
        } else if (status.equals("304 Not Modified")){
           setFile(rPath + "/304error.html");
        } else if (status.equals("400 Bad Request")){
            setFile(rPath + "/400error.html");
        }
        else if (status.equals("404 Not Found")){
            return;
        }
        else {
            setFile(rPath + "/501error.html");
        }   
    }

}