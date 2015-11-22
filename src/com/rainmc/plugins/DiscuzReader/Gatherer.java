package com.rainmc.plugins.DiscuzReader;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/**
 * DiscuzReader
 * Created by Rex on 2015/11/22.
 * Updated on 2015/11/22
 */
public class Gatherer implements Runnable{

    private File f = new File("cache.txt");

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

        USERNAME = username;
        PASSWORD = password;
        HOSTNAME = hostname;
        PORT = port;
        DATABASE = database;

        URL = "jdbc:mysql://" + HOSTNAME + ":" + PORT + "/" + DATABASE;

    }

    public void initializeCache(){
        try {
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
            System.err.println ("Cannot connect to database server");
            e.printStackTrace(); //prints out SQLException errors to the console (if any)
        } finally {
            if (connection != null)
            {
                try
                {
                    connection.close ();
                    System.out.println ("Database connection terminated");
                }
                catch (Exception e) { /* ignore close errors */ }
            }
        }
    }


    public void fetch(){
//        public void fetch(String table, String sort, String filter, String filter_value, int amount){
        int count = 0;
        Statement s = null;
        try {
            s = connection.createStatement ();
            //s.executeQuery ("SELECT `fid`, `author`, `subject`, `message` FROM `pre_forum_post` WHERE `fid` <> 59 ORDER BY `pid` DESC LIMIT 5");
            s.executeQuery ("SELECT `name`, `lastpost` FROM `pre_forum_forum` WHERE `lastpost` > '' ORDER BY `fid` DESC");

            ResultSet rs = s.getResultSet();

            while (rs.next ())
            {
                String forum_name = rs.getString ("name");
                String lastpost = rs.getString ("lastpost");

                String[] array = lastpost.split(" +");

                String pid = array[0];
                String title = array[1];
                String date = array[2];
                String author = array[3];

                BufferedWriter writer = null;

                writer = new BufferedWriter( new FileWriter("cache.txt"));

                // forum_name | title | author | links
                writer.write("[" + forum_name + "] '" + title + "' <" + author + "> -http://bbs.rainmc.com/forum.php?mod=viewthread&tid=" + pid + "\n");

                System.out.println (
                    "pid = " + pid
                            + ", title = " + title
                            + ", author = " + author);

                ++count;
            }

            rs.close ();
            s.close ();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public void run() {
        initializeCache();
        startConnection();
        fetch();
    }
}
