import java.io.*;
import java.net.*; 

class MyWebServer{
    public static void main(String[] args){
        int port = Integer.parseInt(args[0]);
        String fileDir = args[1];
        
        // String clientSentence;
        // String capitalizedSentence; 
        try{
            ServerSocket welcomeSocket = new ServerSocket(port);
            while(true) {
                Socket connectionSocket = welcomeSocket.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                StringBuffer input = new StringBuffer();
                String currLine = inFromClient.readLine();
                if(currLine != null){
                    while(!currLine.isEmpty()){
                        input.append(currLine);
                        input.append(" ");
                        currLine = inFromClient.readLine();
                    }
                    HTTPRequest request = new HTTPRequest(input.toString(), fileDir);
                    HTTPResponse response = new HTTPResponse(request.getStatus(), request.getPath(), request.getCommand(), request.getIfModifiedSince(), request.getRootPath());
                    outToClient.writeBytes(response.getResponse());
                    System.out.println("HEADER Bytes written: " + outToClient.size());
                    if(request.getCommand().equals("GET")){ 
                        byte[] test = response.getFile();
                        System.out.println("main length after: "+test.length);
                        outToClient.write(test);
                        System.out.println("TOTAL Bytes written: " + outToClient.size());
                    }
                }
                connectionSocket.close();
            }
        } catch (IOException e){
            System.out.println(e);
        }
    }
}