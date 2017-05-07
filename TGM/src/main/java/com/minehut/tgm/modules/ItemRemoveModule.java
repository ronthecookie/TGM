package com.minehut.tgm.modules;

import com.google.gson.JsonElement;
import com.minehut.tgm.TGM;
import com.minehut.tgm.match.Match;
import com.minehut.tgm.match.MatchModule;
import com.minehut.tgm.util.Strings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemRemoveModule extends MatchModule implements Listener {
    private final List<Material> removed = new ArrayList<>();

    @Override
    public void load(Match match) {
        if (match.getMapContainer().getMapInfo().getJsonObject().has("itemremove")) {
            for (JsonElement itemElement : match.getMapContainer().getMapInfo().getJsonObject().getAsJsonArray("itemremove")) {
                try {
                    removed.add(Material.valueOf(Strings.getTechnicalName(itemElement.getAsString())));
                } catch (Exception e) {
                    TGM.get().getPlayerManager().broadcastToAdmins(ChatColor.RED + "[JSON] Unknown material in itemremove module: \"" + itemElement.getAsString() + "\"");
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        List<ItemStack> toRemove = new ArrayList<>();
        for (ItemStack itemStack : event.getDrops()) {
            if (removed.contains(itemStack.getType())) {
                toRemove.add(itemStack);
            }
        }

        for (ItemStack itemStack : toRemove) {
            event.getDrops().remove(itemStack);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (removed.contains(event.getItemDrop().getItemStack().getType())) {
            event.getItemDrop().remove();
        }
    }
}
