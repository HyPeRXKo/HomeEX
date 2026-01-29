package fr.infinitystudios.homeEX.Utils;

import org.bukkit.inventory.ItemStack;

public class SerialisationUtils {

    public static byte[] itemToBytes(ItemStack item){
        return item.serializeAsBytes();
    }

    public static ItemStack bytesToItem(byte[] bytes){
        return ItemStack.deserializeBytes(bytes);
    }
}
