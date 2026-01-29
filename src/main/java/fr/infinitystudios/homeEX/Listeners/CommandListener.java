package fr.infinitystudios.homeEX.Listeners;

import fr.infinitystudios.homeEX.HomeEX;
import fr.infinitystudios.homeEX.Utils.EssentialsUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandListener implements Listener {
    private HomeEX plugin = JavaPlugin.getPlugin(HomeEX.class);

    @EventHandler
    public void onHomeCommand(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        String command = e.getMessage();

        if(command.toLowerCase().startsWith("/sethome") || command.toLowerCase().startsWith("/essentials:sethome")) {
            EssentialsUtils eu = new EssentialsUtils();
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                eu.checkNewHomes(p);
            }, 2L);
        }

        if(command.toLowerCase().startsWith("/delhome") || command.toLowerCase().startsWith("/essentials:delhome")) {
            EssentialsUtils eu = new EssentialsUtils();
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                eu.checkOldHomes(p);
            }, 2L);
        }
    }
}
