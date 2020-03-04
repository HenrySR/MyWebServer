import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

class HTTPRequest{
    private String command;
    private String rPath;
    private String path;
    private String status;
    private Date ifModifiedSince;
    
    public HTTPRequest(String request, String rootPath){
        System.out.println(request);
        rPath = rootPath;
        String[] tokens = tokenize(request);
        command = tokens[0];
        path = tokens[1];
        String date = findModifiedDate(tokens);
        status = "200 OK";
        checkForBadRequest(date);
        makePath();
        System.out.println(command + " " + path + " " + ifModifiedSince);
    }

    public String getStatus(){
        return status;
    }

    public String getPath(){
        return path;
    }

    public String getCommand(){
        return command;
    }

    public Date getIfModifiedSince(){
        return ifModifiedSince;
    }

    private void makePath(){
        if(path.charAt(0) == 'h'){
            int slashCounter = 0;
            int startIndex = 0;
            while(slashCounter < 3){
                startIndex++;
                if(path.charAt(startIndex) == '/'){
                    slashCounter++;
                }
            }
            path = path.substring(startIndex);
        }
        path = rPath + path;
    }

    private void validateCommand(){
        if(!command.equals("GET") || !command.equals("HEAD")){
            status = "501 Not Implemented";
        }       
    }

    private void checkForBadRequest(String date){
        validateCommand();
        if(path.charAt(0) != '/'){
          status = "400 Bad Request";
          return;  
        }
        if(!date.isEmpty()){
            try{
                ifModifiedSince = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").parse(date);
            } catch (ParseException e){
                status = "400 Bad Request";
                return;
            } if (ifModifiedSince == null){
                status = "400 Bad Request";
            }
        }
    }

    private String findModifiedDate(String[] tokens){
        for(int i = 0; i < tokens.length; i++){
            if(tokens[i].equals("If-Modified-Since:")){
                // next 6 tokens will be under date header
                String date = "";
                for(int j = i + 1; j < i + 7; j++){
                    date = date + tokens[j] + " ";
                }
                return date;
            }
        }
        return "";
    }

    private String[] tokenize(String request){
        String[] toReturn = request.split(" ");
        return toReturn;
    }
}