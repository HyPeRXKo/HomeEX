package fr.infinitystudios.homeEX.Utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Random;

public class ItemsUtils {

    private TextComponent cc(String text){return LegacyComponentSerializer.legacyAmpersand().deserialize(text);}

    public ItemStack randomBed(){
        ItemStack item;
        int random = new Random().nextInt(1,8);

        item = switch (random) {
            case 1 -> new ItemStack(Material.RED_BED);
            case 2 -> new ItemStack(Material.BLUE_BED);
            case 3 -> new ItemStack(Material.GREEN_BED);
            case 4 -> new ItemStack(Material.YELLOW_BED);
            case 5 -> new ItemStack(Material.PURPLE_BED);
            case 6 -> new ItemStack(Material.ORANGE_BED);
            case 7 -> new ItemStack(Material.PINK_BED);
            default -> new ItemStack(Material.RED_BED);
        };

        return item;
    }

    public ItemStack StripItemForIcon(ItemStack item){
        ItemStack newItem = item.clone();
        ItemMeta meta = newItem.getItemMeta();

        if(!meta.getEnchants().isEmpty()){
            newItem.removeEnchantments();
            meta.setEnchantmentGlintOverride(true);
        }

        if(meta.hasLore() && !meta.lore().isEmpty()){
            meta.lore(new ArrayList<>());
        }

        if(meta.hasCustomName()){
            meta.customName(null);
        }

        newItem.setItemMeta(meta);
        return newItem;
    }
    public ItemStack PrepareItemForIcon(ItemStack item, String homename){
        ItemStack newItem = item.clone();
        newItem = StripItemForIcon(newItem);
        newItem.setAmount(1);
        ItemMeta meta = newItem.getItemMeta();
        meta.customName(cc("&bHome&7: &f" + capitalizeFirst(homename)));
        meta.lore(iconLore());

        meta.getPersistentDataContainer().set(new NamespacedKey("homesex", "homename"), PersistentDataType.STRING, homename);

        newItem.setItemMeta(meta);
        return newItem;
    }

    public ItemStack iconChangerItem(){
        ItemStack item = new  ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        ArrayList<Component> lore = new ArrayList<>();
        meta.customName(cc("&bChange Icon"));

        lore.add(Component.space());
        lore.add(cc("&7To change your &dhome &7icon,"));
        lore.add(cc("&7click on an item in &cyour inventory&7."));
        lore.add(cc("&fIt will automatically apply it."));

        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack exitbarrieritem(){
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        ArrayList<Component> lore = new ArrayList<>();
        meta.customName(cc("&4Exit"));
        lore.add(Component.space());
        lore.add(cc("&cClick to exit the menu"));


        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack pageitem(boolean mode){
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        ArrayList<Component> lore = new ArrayList<>();
        if(!mode){
            meta.customName(cc("&cPrevious Page"));
            lore.add(Component.space());
            lore.add(cc("&cClick to return to the previous page"));

            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey("homesex", "mode"), PersistentDataType.BOOLEAN, false);
        }
        else{
            meta.customName(cc("&cNext Page"));
            lore.add(Component.space());
            lore.add(cc("&cClick to return to the next page"));

            meta.getPersistentDataContainer().set(new NamespacedKey("homesex", "mode"), PersistentDataType.BOOLEAN, true);
        }

        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack backitem(){
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        ArrayList<Component> lore = new ArrayList<>();

        meta.customName(cc("&cBack"));
        lore.add(Component.space());
        lore.add(cc("&bClick to return to the main menu."));

        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack PanelItem(){
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.customName(cc("&7&l"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack EmptyHomeItem(){
        ItemStack item = new ItemStack(Material.STRUCTURE_VOID);
        ItemMeta meta = item.getItemMeta();
        meta.customName(cc("&cEmpty Home"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return item;
    }


    private ArrayList<Component> iconLore(){
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.space());
        lore.add(cc("&bLeft Click &fto teleport to this home"));
        lore.add(cc("&bRight Click &fto change the icon"));
        return lore;
    }


    private String capitalizeFirst(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
