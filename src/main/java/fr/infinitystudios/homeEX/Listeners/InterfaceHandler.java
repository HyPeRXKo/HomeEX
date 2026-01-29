package fr.infinitystudios.homeEX.Listeners;

import fr.infinitystudios.homeEX.HomeEX;
import fr.infinitystudios.homeEX.Utils.DatabaseManager;
import fr.infinitystudios.homeEX.Utils.GuiUtils;
import fr.infinitystudios.homeEX.Utils.IconChangerHolder;
import fr.infinitystudios.homeEX.Utils.MainGUIPagedHolder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class InterfaceHandler implements Listener {

    private HomeEX plugin = JavaPlugin.getPlugin(HomeEX.class);

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getInventory().getHolder(false) instanceof MainGUIPagedHolder guiPagedHolder){
            if(e.getClickedInventory() != e.getView().getTopInventory()){
                return;
            }
            e.setCancelled(true);
            int page = guiPagedHolder.getPage();
            Player p = (Player) e.getWhoClicked();
            ItemStack item = e.getCurrentItem();
            if(item == null) {
                return;}
            if(item.getType() == Material.BARRIER){
                p.closeInventory();
            }
            if(item.getType() == Material.PAPER){
                if(item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey("homesex", "mode"), PersistentDataType.BOOLEAN)){
                    boolean mode = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey("homesex", "mode"), PersistentDataType.BOOLEAN);
                    if(!mode){ //previous
                        page = page - 1;
                        p.openInventory(new GuiUtils().MainGUIPaged((Player) e.getWhoClicked(), page));
                        return;
                    }
                    else{ //next
                        page = page + 1;
                        p.openInventory(new GuiUtils().MainGUIPaged((Player) e.getWhoClicked(), page));
                        return;
                    }

                }
            }
            if(item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey("homesex", "homename"), PersistentDataType.STRING)){
                ClickType clickType = e.getClick();
                String homename = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey("homesex", "homename"), PersistentDataType.STRING);
                if(clickType.isLeftClick()){
                    p.performCommand("home " + homename);
                    p.closeInventory();
                    return;
                }
                if(clickType.isRightClick()){
                    p.openInventory(new GuiUtils().IconChangerGUI(p, page, homename));
                    return;
                }
            }
        }
        if(e.getInventory().getHolder(false) instanceof IconChangerHolder iconChangerHolder){
            e.setCancelled(true);
            int page = iconChangerHolder.getPage();
            String homename = iconChangerHolder.getHomename();
            Player p = (Player) e.getWhoClicked();
            ItemStack item = e.getCurrentItem();
            if(item == null) {
                return;}
            if(e.getClickedInventory() == e.getView().getTopInventory()){
                if(item.getType() == Material.BARRIER){
                    p.closeInventory();
                    return;
                }
                if(item.getType() == Material.PAPER){
                    p.openInventory(new GuiUtils().MainGUIPaged(p, page));
                    return;
                }
            }
            if(e.getClickedInventory() == e.getView().getBottomInventory()){
                DatabaseManager db = plugin.getDatabaseManager();
                try {
                    int id = db.getHomeId(p.getUniqueId(), homename);
                    db.editHomeItem(id, item);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                p.openInventory(new GuiUtils().MainGUIPaged(p, page));
            }
        }

    }
}
