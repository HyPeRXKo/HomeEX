package fr.infinitystudios.homeEX.Utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class IconChangerHolder implements InventoryHolder {
    private String homename;
    private int page;

    public IconChangerHolder(int page,String homename){
        this.homename = homename;
        this.page = page;
    }

    public String getHomename(){
        return homename;
    }

    public int getPage(){
        return page;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
