package com.rainmc.plugins.DiscuzReader;

import com.mysql.jdbc.Connection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

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

    private boolean broadcaster_switch = true;
    private boolean gatherer_switch = true;

    protected Logger logger = getLogger();
    public ConsoleCommandSender console = getServer().getConsoleSender();

    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

    public DiscuzReader(){
        mBroadcaster = new Broadcaster();
    }

    private boolean loadConfig(){
        mGatherer = new Gatherer(config.getString("userid"), config.getString("password"), config.getString("hostname"), config.getInt("port"), config.getString("database"));

        return true;
    }

    public void saveConfig(){
        config.options().copyDefaults(true);
        saveConfig();

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player)sender;
        int length = args.length;
        if(cmd.getName().equalsIgnoreCase("discuzreader")) {
            if(length == 0) {
                player.sendMessage(ChatColor.GREEN + "HELP: ");
                return true;
            }

            if(length == 1) {
                if(args[0].equalsIgnoreCase("on")) {
                    if(player.hasPermission("discuzreader.on")) {
                        broadcaster_switch = true;
                        gatherer_switch = true;

                        scheduler.scheduleSyncRepeatingTask(this, mBroadcaster, 0L, 20L);
                        scheduler.scheduleSyncRepeatingTask(this, mGatherer, 0L, 20L);

                    } else {
                        player.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform this command.");
                    }
                    return true;
                }

                if(args[0].equalsIgnoreCase("off")) {
                    if(player.hasPermission("discuzreader.off")) {
                        broadcaster_switch = false;
                        gatherer_switch = false;

                        scheduler.cancelTasks(this);

                    } else {
                        player.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform this command.");
                    }
                    return true;
                }



                if(args[0].equalsIgnoreCase("broadcast")) {
                    if(player.hasPermission("discuzreader.broadcast")) {
                        mBroadcaster.printToChat();
                    } else {
                        player.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform this command.");
                    }
                    return true;
                }

                if(args[0].equalsIgnoreCase("fetch")) {
                    if(player.hasPermission("discuzreader.fetch")) {
                        mGatherer.fetch();
                    } else {
                        player.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform this command.");
                    }
                    return true;
                }

                if(args[0].equalsIgnoreCase("reload")) {
                    if(player.hasPermission("discuzreader.reload")) {
                        this.reloadConfig();
                        player.sendMessage(ChatColor.GREEN + "Configuration succesfully reloaded!");
                        return true;
                    }

                    player.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform this command.");
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onEnable() {
        if (loadConfig()){
            //logger.info("Loaded config successfully");
            console.sendMessage("Loaded config successfully");
        } else {
            console.sendMessage("Failed to load config");
        }

        mGatherer.startConnection();
        mGatherer.fetch();

        scheduler.scheduleSyncRepeatingTask(this, mBroadcaster, 0L, 20L);
        scheduler.scheduleSyncRepeatingTask(this, mGatherer, 0L, 20L);

    }


    @Override
    public void onDisable() {


        mGatherer.closeConnection();
    }


}
