package emailclient;

import java.util.Scanner;
import java.io.File;
import java.util.Vector;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Credentials {
    //this contains static methods for saving and loading
    //usage information like server names, user names and passwords
    
    //write new information to file
    public static void saveInfo(String fileName,
                                String serverName,
                                String userName,
                                String password){
        String pwd = password;
        
        try {
            String serverContent = new Scanner(new File("server" + fileName)).useDelimiter("\\Z").next();
            String userContent = new Scanner(new File("user" + fileName)).useDelimiter("\\Z").next();
            PrintWriter serverOut = new PrintWriter(new BufferedWriter(new FileWriter("server" + fileName, true)));
            PrintWriter userOut = new PrintWriter(new BufferedWriter(new FileWriter("user" + fileName, true)));
            if(!serverContent.contains(serverName))
                System.out.println(serverName);
                serverOut.println(serverName);
            if(!userContent.contains(userName)){
                System.out.println(userName);
                userOut.println(userName);
                System.out.println(pwd);
                userOut.println(pwd);
            }            
            serverOut.close();
            userOut.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
    //servers Vector
    private static Vector<String> servers = new Vector<String>();
    //users Vector
    private static Vector<String> users = new Vector<String>();
    //passwords Vector
    private static Vector<String> pwds = new Vector<String>();
    
    //load all the information for the appropriate files
    public static void load(String fileName){
        String serverContent = "";
        String userContent = "";
        try{
            serverContent = new Scanner(new File("server" + fileName)).useDelimiter("\\Z").next();
            userContent = new Scanner(new File("user" + fileName)).useDelimiter("\\Z").next();
        } catch(Exception ex){
            ex.printStackTrace();
        }
        String []serverWords = serverContent.split("\n");
        String []userWords = userContent.split("\n");
        for(int i = 0, n = serverWords.length; i < n; ++i){
            if(!servers.contains(serverWords[i]))
                servers.add(serverWords[i]);
        }
        for(int i = 0, n = userWords.length; i < n; i+=2){
            if(!users.contains(userWords[i])){
                users.add(userWords[i]);
                pwds.add(userWords[i+1]);
            }                
        }
        
    }
    
    //getter for the servers
    public static Vector<String> loadServers(){
        return servers;
    }
    
    //getter for the users
    public static Vector<String> loadUsers(){
        return users;
    }
    
    //getter for the passwords
    public static Vector<String> loadPasswords(){
        return pwds;
    }
    
}
