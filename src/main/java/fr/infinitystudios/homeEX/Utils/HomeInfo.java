package fr.infinitystudios.homeEX.Utils;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public record HomeInfo(int id, UUID uuid, String homeName, ItemStack item) {
}
