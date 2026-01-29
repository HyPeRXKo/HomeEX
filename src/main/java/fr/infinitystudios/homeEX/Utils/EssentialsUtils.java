package fr.infinitystudios.homeEX.Utils;

import com.earth2me.essentials.Essentials;
import fr.infinitystudios.homeEX.HomeEX;
import net.ess3.api.IUser;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EssentialsUtils {

    private HomeEX plugin = JavaPlugin.getPlugin(HomeEX.class);

    public void checkNewHomes(Player p){
        Essentials ess = plugin.getEssentials();
        DatabaseManager db = plugin.getDatabaseManager();
        ItemsUtils iu = new ItemsUtils();
        IUser user = ess.getUser(p);
        List<String> homes = user.getHomes();
        List<String> homesDB = new ArrayList<>();

        try {
            homesDB = db.getHomes(p.getUniqueId());
        } catch (SQLException ex) {
            plugin.getLogger().severe("Failed to get homes from database!");
            plugin.getLogger().severe(ex.getMessage());
            return;
        }

        for(String home : homes) {
            if(!homesDB.contains(home)) {
                try {
                    db.addHome(p.getUniqueId(), home, iu.randomBed());
                } catch (SQLException ignored) {}
            }
        }

    }

    public void checkOldHomes(Player p){
        Essentials ess = plugin.getEssentials();
        DatabaseManager db = plugin.getDatabaseManager();
        IUser user = ess.getUser(p);
        List<String> homes = user.getHomes();
        List<String> homesDB = new ArrayList<>();

        try {
            homesDB = db.getHomes(p.getUniqueId());
        } catch (SQLException ex) {
            plugin.getLogger().severe("Failed to get homes from database!");
            plugin.getLogger().severe(ex.getMessage());
            return;
        }

        for(String home: homesDB){
            if(!homes.contains(home)){
                try {
                    db.removeHome(p.getUniqueId(), home);
                } catch (SQLException ignored) {}
            }
        }
    }
}
