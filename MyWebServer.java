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
                inFromClient.readLine();
                inFromClient.readLine();
                String test1 = inFromClient.readLine();
                outToClient.writeBytes(test1);
                connectionSocket.close();
                System.out.println(test1);
            }
        } catch (IOException e){
            System.out.println(e);
        }
    }
}