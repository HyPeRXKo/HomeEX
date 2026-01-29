package fr.infinitystudios.homeEX.Utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MainGUIPagedHolder implements InventoryHolder {
    private int page;

    public MainGUIPagedHolder(int page){
        this.page = page;
    }

    public int getPage(){return page;}

    @Override
    public Inventory getInventory()
    {
        return null;
    }
}
