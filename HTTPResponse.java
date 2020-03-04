import java.util.Date;
import java.io.File;

class HTTPResponse{
    private String response;
    private String server;
    private Date date;
    private String status;
    private File file;

    public HTTPResponse() {
        
    }
    
    public String response(String status, String command, String contentLength, String path){
        
        checkFile(path);
        response = status + "/r/n" + date + "/r/n" + server + "/r/n" + contentLength + "/r/n" + path + "/r/n"; 

        return response;
    }

    private void checkFile(String path){
        try {
            file = new File(path);
        } catch (NullPointerException e) {
            status = "404 Not Found";
            return; 
        }
        
        if(!file.exists() || !file.isFile()) {
            status = "404 Not Found";
        }
    }

    
}