import java.io.*;
import java.net.*; 
import java.util.ArrayList;

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
                while(!currLine.isEmpty()){
                    input.append(currLine);
                    input.append(" ");
                    currLine = inFromClient.readLine();
                }
                HTTPRequest request = new HTTPRequest(input.toString(), fileDir);
                outToClient.writeBytes(request.getStatus());
                connectionSocket.close();
            }
        } catch (IOException e){
            System.out.println(e);
        }
    }
}