import java.util.Date;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

class HTTPResponse{
    private String server;
    private String command;
    private Date date;
    private String status;
    private String path;
    private String rPath;
    private File file;
    private Date ifModifiedSince;

    // Constructs HTTP response object with HTTP status, path from request, command from request,
    // date from If-Modified-Since header in request, and path from MyWebServer.main args
    // RETURN: nothing
    public HTTPResponse(String status, String path, String command, Date ifModifiedSince, String rootPath) {
        server = "Best Server Ever 1.0";
        this.status = status;
        this.path = path;
        rPath = rootPath;
        this.command = command;
        this.ifModifiedSince = ifModifiedSince;
        date = new Date();
    }

    // Public method to get HTTP response header based on command.
    // If command isn't get or head, sets status to 501 and calls GET for that file
    // RETURN: String with HTTP response
    public String getResponse(){
        
        if(command.equals("HEAD") || command.equals("GET")){
            return response();
        } else {
            status = "501 Not Implemented";
            return response();
        }
    }
    
    // Converts the file to an array of Bytes for the outputstream
    // RETURN: byte[] with all bytes of the file
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

    // gets the length of the file as a long
    // RETURN long int of the size of the file
    private long getFileSize(){
        try{
            return Files.size(Paths.get(file.getAbsolutePath()));
        } catch (IOException e){
            System.out.println(e);
            return -1;
        }
    }
    // Reads in file, calls method to check file status, sets error file, formats response header
    // RETURN: String of HTTP response header
    private String response(){         
        setFile(path);
        checkFile();
        if(ifModifiedSince != null){
            checkValidDate();
        }
        setErrorFile();
        
         Date lastModified = new Date(file.lastModified());
        //String curDate = Date.toString(date);
        String response ="HTTP/1.1 " + status + "\r\n" + "Date: " + date + "\r\n" + "Server: " + server + 
        "\r\n" + "Last-Modified: " +  lastModified + "\r\n" + "Content-Length: " + getFileSize() + "\r\n\r\n"; 
        System.out.println("In responseGET: "+response);
        return response;
    }

    // Reads in the file at the directory in path, sets status to 404 if pathname is null
    // RETURN nothing
    // THROW NullPointerException if path == null
    private void setFile(String path){
        try {
            file = new File(path);
        } catch (NullPointerException e) {
            status = "404 Not Found";
            setFile(rPath + "/404error.html");
        }
    }

    // Checks file exists and isn't just a directory, sets 404 error and changes path to 404 errorfile if so
    // RETURN nothing
    private void checkFile(){
        if(!file.exists()) {
            status = "404 Not Found";    
            setFile(rPath + "/404error.html");
        }
    }

    // checks if the date the file was last modified is before the request's specifications
    // if so, status is changed to 304
    // RETURN nothing
    private void checkValidDate(){
        Date lastModified = new Date(file.lastModified());
        if (lastModified.before(date)){
            status = "304 Not Modified";
        }
    }

    // changes path based on the HTTPResponse.status so correct file is returned to MyWebserver
    // Note: 404 errors must be checked before because reading in 
    // the error files can technically cause a 404 error.
    // RETURN nothing
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