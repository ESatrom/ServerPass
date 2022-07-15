package me.mindarius.serverpass.serverpass;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Main extends JavaPlugin implements Listener {

    List<UUID> approvedPlayers = new ArrayList<>();
    String password = null;
    @EventHandler public void playerJoin(PlayerSpawnLocationEvent event){
        if(password==null){
            getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
                event.getPlayer().beginConversation();
            }, 1);
            return;
        }
        if(approvedPlayers.contains(event.getPlayer().getUniqueId())){return;}
        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            event.getPlayer().beginConversation();
        }, 1);
    }

    @Override public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override public void onDisable() {
        // Plugin shutdown logic
    }
}
