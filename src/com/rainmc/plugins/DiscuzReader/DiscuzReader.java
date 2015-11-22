package com.rainmc.plugins.DiscuzReader;

import com.mysql.jdbc.Connection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Created by Rex on 2015/11/22.
 */
public class DiscuzReader extends JavaPlugin{

    private final FileConfiguration config = getConfig();

    private Logger logger;

    //DataBase vars.
    private String username="YOUR DB USERNAME"; //Enter in your db username
    private String password="YOUR DB PASSWORD"; //Enter your password for the db
    private String url = "jdbc:mysql://db4free.net:3306/DataBaseName"; //Enter URL w/db name

    //Connection vars
    static Connection connection; //This is the variable we will use to connect to database


    public DiscuzReader(){

    }

    private boolean loadConfig(){
        username = config.getString("username");
        password = config.getString("password");
        url = config.getString("url");

        return true;
    }

    public void saveConfig(){
        config.options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onEnable() {


        config.addDefault("youAreAwesome", true);

    }

    @Override
    public void onDisable() {

    }


}
