package com.rainmc.plugins.DiscuzReader;

import com.mysql.jdbc.Connection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

/**
 * DiscuzReader
 * Created by Trexguo on 2015/11/22.
 * Updated on 2015/11/22
 */
public class DiscuzReader extends JavaPlugin{

    private final FileConfiguration config = getConfig();

    private Broadcaster mBroadcaster;
    private Gatherer mGatherer;

    private Logger logger = getLogger();

    public DiscuzReader(){
        Broadcaster mBroadcaster = new Broadcaster();
    }

    private boolean loadConfig(){

        Gatherer mGatherer = new Gatherer(config.getString("userid"), config.getString("password"), config.getString("hostname"), config.getInt("port"), config.getString("database"));

        return true;
    }

    public void saveConfig(){
        config.options().copyDefaults(true);
        saveConfig();

        mGatherer.startConnection();
    }

    @Override
    public void onEnable() {


        loadConfig();



    }

    @Override
    public void onDisable() {


        mGatherer.closeConnection();
    }


}
