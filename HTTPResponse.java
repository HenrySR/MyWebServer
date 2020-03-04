import java.util.Date;
import java.io.File;

class HTTPResponse{
    private String response;
    private String server;
    private Date date;
    private String status;
    private String command;
    private String path;
    private File file;

    public HTTPResponse(String status, String command, String path) {
        server = "";
        this.status = status;
        this.command = command;
        this.path = path;
    }
    
    public String responseGET(){
        response = status + "/r/n" + date + "/r/n" + server + "/r/n" + file.length() + "/r/n" + path + "/r/n"; 

        return response;
    }
    public String responseHEAD(){
        response = status + "/r/n" + date + "/r/n" + server + "/r/n" + file.length() + "/r/n" + path + "/r/n"; 

        return response;
    }

    private void setFile(){
        try {
            file = new File(path);
        } catch (NullPointerException e) {
            status = "404 Not Found";
            setErrorFile();
            setFile();
        }
    }

    private void checkFile(){
        if(!file.exists() || !file.isFile()) {
            status = "404 Not Found";
        }
    }

    private void checkValidDate(){

    }

    private void setErrorFile(){
        if(status.equals("200 OK")){
            path = path + "index.html";
        } else if (status.equals("304 Not Modified")){
            path = path + "304error.html";
        } else if (status.equals("400 Bad Request")){
            path = path + "400error.html";
        } else if (status.equals("404 Not Found")){
            path = path + "404error.html";
        } else {
            path = path + "501error.html";
        } 
        setFile();
    }


    
}