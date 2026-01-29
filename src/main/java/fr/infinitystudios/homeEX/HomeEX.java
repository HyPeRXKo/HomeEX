package fr.infinitystudios.homeEX;

import com.earth2me.essentials.Essentials;
import fr.infinitystudios.homeEX.Listeners.CommandListener;
import fr.infinitystudios.homeEX.Listeners.InterfaceHandler;
import fr.infinitystudios.homeEX.Listeners.PlayerLogin;
import fr.infinitystudios.homeEX.Utils.DatabaseManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Collections;

public final class HomeEX extends JavaPlugin {

    private Essentials essentials;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        createDataFolder();

        if(getServer().getPluginManager().getPlugin("Essentials") == null) {
           getLogger().severe("EssentialsX is not loaded, plugin disabled.");
           getServer().getPluginManager().disablePlugin(this);
           return;
        }
        this.essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        getLogger().info("EssentialsX Found, plugin activated.");

        this.databaseManager = new DatabaseManager(this);
        try {
            this.databaseManager.initDatabase();
        } catch (SQLException e) {
            getLogger().severe("Failed to init database!");
            getLogger().severe(e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayerLogin(), this);
        getServer().getPluginManager().registerEvents(new CommandListener(), this);
        getServer().getPluginManager().registerEvents(new InterfaceHandler(), this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(HomeExCommand.createCommand(), "Main Command", Collections.singleton("homeex"));
        });
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            try {
                databaseManager.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void createDataFolder(){
        if (!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
    }

    public Essentials getEssentials() {
        return essentials;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
