package fr.infinitystudios.homeEX.Utils;

import fr.infinitystudios.homeEX.HomeEX;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

public class GuiUtils {

    private static final int[] fifteenSlots = {11,12,13,14,15,20,21,22,23,24,29,30,31,32,33};
    private static final int previousPageSlot = 18;
    private static final int nextPageSlot = 26;
    private static final int exitSlot = 44;
    private static final int backSlot = 35;
    private static final int iconChangerSlot = 22;

    private HomeEX plugin = JavaPlugin.getPlugin(HomeEX.class);

    private TextComponent cc(String text){return LegacyComponentSerializer.legacyAmpersand().deserialize(text);}

    public Inventory MainGUIPaged(Player p, int page){
        ItemsUtils iu = new ItemsUtils();
        DatabaseManager db = plugin.getDatabaseManager();
        ArrayList<HomeInfo> homes = new ArrayList<>();

        try {
            homes = db.getHomesInfo(p.getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        homes.sort(Comparator.comparingInt(HomeInfo::id));

        int numberofhomes = homes.size();
        int numberofpages = (int) Math.ceil((double) numberofhomes / 15);

        if(page == 0){page = 1;}

        Inventory inv = plugin.getServer().createInventory(new MainGUIPagedHolder(page), 45, cc("&bHomes"));

        for (int i = 0; i < 45; i++) {
            inv.setItem(i, iu.PanelItem());
        }

        for (int slot : fifteenSlots) {
            inv.setItem(slot, iu.EmptyHomeItem());
        }

        if(page > 1){
            inv.setItem(previousPageSlot, iu.pageitem(false));
        }
        if(page < numberofpages){
            inv.setItem(nextPageSlot, iu.pageitem(true));
        }

        inv.setItem(exitSlot, iu.exitbarrieritem());

        for(int i = 0; i < 15; i++){
            int index = (page - 1) * 15 + i;
            if(index >= numberofhomes){
                break;
            }

            HomeInfo home = homes.get(index);
            inv.setItem(fifteenSlots[i], iu.PrepareItemForIcon(home.item(), home.homeName()));
        }

        return inv;
    }


    public Inventory IconChangerGUI(Player p, int page, String homename){
        ItemsUtils iu = new ItemsUtils();
        DatabaseManager db = plugin.getDatabaseManager();
        Inventory inv = plugin.getServer().createInventory(new IconChangerHolder(page, homename), 45, cc("&bHomes > icon"));

        for (int i = 0; i < 45; i++) {
            inv.setItem(i, iu.PanelItem());
        }

        inv.setItem(exitSlot, iu.exitbarrieritem());
        inv.setItem(backSlot, iu.backitem());
        inv.setItem(iconChangerSlot, iu.iconChangerItem());


        return inv;
    }

}
