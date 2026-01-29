package fr.infinitystudios.homeEX;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.infinitystudios.homeEX.Utils.GuiUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HomeExCommand {

    private static HomeEX plugin = JavaPlugin.getPlugin(HomeEX.class);

    public static LiteralCommandNode<CommandSourceStack> createCommand(){
        return Commands.literal("homes")
                .executes((ctx) -> {
                    CommandSender sender = ctx.getSource().getSender();
                    if(!(sender instanceof Player player)){
                        plugin.getLogger().warning("You must be a player to use this command.");
                        return Command.SINGLE_SUCCESS;
                    }

                    player.openInventory(new GuiUtils().MainGUIPaged(player, 1));
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

}
