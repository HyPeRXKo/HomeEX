package fr.infinitystudios.homeEX.Listeners;

import fr.infinitystudios.homeEX.Utils.EssentialsUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerLogin implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent e) {
        EssentialsUtils eu = new EssentialsUtils();
        eu.checkNewHomes(e.getPlayer());
        eu.checkOldHomes(e.getPlayer());

    }



}
