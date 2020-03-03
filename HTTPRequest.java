import java.util.ArrayList;

class HTTPRequest{
    private ArrayList<String> tokens;
    private String path;
    
    public HTTPRequest(String request, String rootPath){
        path = rootPath;
    }
}