import java.util.Date;
import java.io.*;

class HTTPResponse{
    private String server;
    private String command;
    private Date date;
    private String status;
    private String path;
    private File file;

    public HTTPResponse(String status, String path, String command, Date ifModifiedSince) {
        server = "Best Server Ever 1.0";
        this.status = status;
        this.path = path;
        this.command = command;
        date = ifModifiedSince;
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
    
    private String responseGET(){
        setFile(path);
        if(!status.equals("404 Not Found")){
            checkValidDate();
        }
        setErrorFile();
        String response ="HTTP/1.1 " + status + "/r/n" + "Date: " + date + "/r/n" + "Server: " + server + 
        "/r/n" + "Content-Length: " + file.getTotalSpace() + "/r/n" + fileToString(); 

        return response;
    }
    private String responseHEAD(){
        setFile(path);
        if(!status.equals("404 Not Found")){
            checkValidDate();
        }
        setErrorFile();
        String response ="HTTP/1.1 " + status + "/r/n" + "Date: " + date + "/r/n" + "Server: " + server + 
        "/r/n" + "Content-Length: " + file.getTotalSpace() + "/r/n"; 

        return response;
    }

    private void setFile(String path){
        try {
            file = new File(path);
            checkFile();
        } catch (NullPointerException e) {
            status = "404 Not Found";
            setFile(this.path + "404error.html");
        }
    }

    private void checkFile(){
        if(!file.exists() || !file.isFile()) {
            status = "404 Not Found";
            setFile(path + "404error.html");
            }
    }

    private void checkValidDate(){
        Date lastModified = new Date(file.lastModified());
        if (lastModified.before(date)){
            status = "304 Not Modified";
        }
    }

    private void setErrorFile(){
        if(status.equals("200 OK")){
            setFile(path + "index.html");
        } else if (status.equals("304 Not Modified")){
           setFile(path + "304error.html");
        } else if (status.equals("400 Bad Request")){
            setFile(path + "400error.html");
        } else {
            setFile(path + "501error.html");
        } 
    }

    private String fileToString(){
        try{
            BufferedReader buff = new BufferedReader(new FileReader(file));
            StringBuffer input = new StringBuffer();
            String currLine = buff.readLine();
            while(!currLine.isEmpty()){
                input.append(currLine);
                input.append("\n");
                currLine = buff.readLine();
            }
            buff.close();
            return input.toString();
        } catch (FileNotFoundException e){
            System.out.println(e);
            return "";
        } catch (IOException e){
            System.out.println(e);
            return "";
        }  
    }



    
}