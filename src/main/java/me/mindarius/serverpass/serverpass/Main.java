package me.mindarius.serverpass.serverpass;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public static List<UUID> approvedPlayers = new ArrayList<>();
    public static String pass = null;


    @Override public void onEnable() {
        File target = new File(getDataFolder().getPath()+".txt");
        if(target.exists()) {
            try {
                Scanner scan = new Scanner(target);
                pass = scan.nextLine();
                while (scan.hasNextLine()) { approvedPlayers.add(UUID.fromString(scan.nextLine())); }
                scan.close();
            } catch (FileNotFoundException e) {
                System.err.println("Unexpected error occurred. Please do not directly edit the ServerPass save file.");
                e.printStackTrace();
            }
        }


        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
    }

    @Override public void onDisable() {
        File target = new File(getDataFolder().getPath()+".txt");
        try {
            target.delete();
            if(pass==null){return;}
            target.createNewFile();
        } catch(IOException e) { System.err.println("IOException encountered while establishing file to save " + target.getPath()); }
        FileWriter writer;
        try {
            writer = new FileWriter(target);
            writer.write(pass+"\n");
            for(UUID id : approvedPlayers){ writer.write(id+"\n"); }
            writer.close();
        } catch (IOException e) {
            System.err.println("Unexpected error occurred.");
            e.printStackTrace();
        }
    }
}
