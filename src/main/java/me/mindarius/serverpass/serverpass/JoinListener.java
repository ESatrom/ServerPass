package me.mindarius.serverpass.serverpass;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class JoinListener implements Listener {
    Plugin plugin;
    public JoinListener(Plugin plugin){ this.plugin = plugin; }

    @EventHandler public void playerJoin(PlayerSpawnLocationEvent event){ //Event called as player is joining the server, before they've loaded in.
        Player p = event.getPlayer();


        if(Main.pass==null){ //Setting the password if there is none set
            Main.approvedPlayers.add(p.getUniqueId()); //If joined before password set, assume you're supposed to be there.
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> { //This must be scheduled so the player is in the world when it triggers.
                new ConversationFactory(plugin).withFirstPrompt(new Prompts.PassSet()).buildConversation(p).begin(); //Ask for new password
            }, 1);
            return;
        }


        if(Main.approvedPlayers.contains(p.getUniqueId())){return;} //If the password has been set and the player has been approved, let them join.

        //Conversely, if it has been set, but they've not been approved, test whether they know the password
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> { //This must be scheduled so the player is in the world when it triggers.

            //The following immobilizes the player and puts them in spectator, making them harmless
            p.setGameMode(GameMode.SPECTATOR);
            p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(Main.freeze);


            new ConversationFactory(plugin).withFirstPrompt(new Prompts.TestPass()).buildConversation(p).begin(); //Ask for password
        }, 1);
    }
}
