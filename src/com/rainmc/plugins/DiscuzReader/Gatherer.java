package com.rainmc.plugins.DiscuzReader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DiscuzReader
 * Created by Rex on 2015/11/22.
 * Updated on 2015/11/22
 */
public class Gatherer {

    //DataBase vars.
    private String USERNAME="YOUR DB USERNAME"; //Enter in your db username
    private String PASSWORD="YOUR DB PASSWORD"; //Enter your password for the db
    private String HOSTNAME="YOUR DB DB";       //Enter your password for the db
    private int PORT = 3306;                    //Enter your password for the db
    private String DATABASE="YOUR DB DB";       //Enter your password for the db

    private String URL = "jdbc:mysql://db4free.net:3306/DataBaseName"; //Enter URL w/db name

    //Connection vars
    static Connection connection; //This is the variable we will use to connect to database

    public Gatherer(String username, String password, String hostname, int port, String database){
        initializeCache();

        USERNAME = username;
        PASSWORD = password;
        HOSTNAME = hostname;
        PORT = port;
        DATABASE = database;

        URL = "jdbc:mysql://" + HOSTNAME + ":" + PORT + "/" + DATABASE;

        startConnection();


    }


    public void initializeCache(){
        try {
            File f = new File("cache.yml");
            if(f.exists() && !f.isDirectory()) {
                // do something
            } else {
                if (f.createNewFile()){
                    System.out.println("Cache File is created!");
                }else{
                    System.out.println("Cache File already exists.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startConnection(){
        try { //We use a try catch to avoid errors, hopefully we don't get any.
            Class.forName("com.mysql.jdbc.Driver"); //this accesses Driver in jdbc.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try { //Another try catch to get any SQL errors (for example connections errors)
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            //with the method getConnection() from DriverManager, we're trying to set
            //the connection's url, username, password to the variables we made earlier and
            //trying to get a connection at the same time. JDBC allows us to do this.
        } catch (SQLException e) { //catching errors)
            e.printStackTrace(); //prints out SQLException errors to the console (if any)
        }
    }


    public void closeConnection(){
        // invoke on disable.
        try { //using a try catch to catch connection errors (like wrong sql password...)
            if(connection!=null && !connection.isClosed()){ //checking if connection isn't null to
                //avoid recieving a nullpointer
                connection.close(); //closing the connection field variable.
            }
        }catch(Exception e){
            e.printStackTrace();

        }
    }
}
