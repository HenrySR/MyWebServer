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
                String test = inFromClient.readLine();
                outToClient.writeBytes(test);
                connectionSocket.close();
                System.out.println("It works!");
            }
        } catch (IOException e){
            System.out.println(e);
        }
    }
}