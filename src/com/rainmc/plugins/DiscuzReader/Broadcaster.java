package com.rainmc.plugins.DiscuzReader;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;

/**
 * DiscuzReader
 * Created by Rex on 2015/11/22.
 * Updated on 2015/11/22
 */



// http://bbs.rainmc.com/forum.php?mod=viewthread&tid=359



public class Broadcaster implements Runnable {
    private int broadcast_internal = 5;
    private int broadcast_items = 5;
    private File cacheFile = new File("cache.txt");

    public Broadcaster(){

    }

    public void printToChat(){
        // Construct BufferedReader from FileReader
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(cacheFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = null;
        try {
            // forum_name | title | author | links

            while ((line = br.readLine()) != null) {
                String[] array = line.split(" +");
                String forum_name = array[0];
                String title = array[1];
                String author = array[2];
                String links = array[3];

                for (Player p : Bukkit.getServer().getOnlinePlayers()){
                    p.sendMessage(forum_name+title+author+links);
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        printToChat();

    }

}
