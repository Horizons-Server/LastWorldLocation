package org.horizons_server.lastworldlocation;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class LastWorldLocation extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTeleport(PlayerTeleportEvent event) {
        final String worldName = event.getFrom().getWorld().getName();
        if (!this.getConfig().getStringList("enabled-worlds").contains(worldName)) return;
        if (event.getFrom().getWorld().equals(event.getTo().getWorld())) return;

        this.getConfig().set("data."+ worldName + "." + event.getPlayer().getUniqueId(), event.getFrom());
        this.saveConfig();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        final String worldName = event.getPlayer().getWorld().getName();
        if (!this.getConfig().getStringList("enabled-worlds").contains(worldName)) return;

        final Location newLocation = this.getConfig().getObject("data."+ worldName + "." + event.getPlayer().getUniqueId(), Location.class);
        if (newLocation != null) {
            event.getPlayer().teleport(newLocation);
        }
    }
}
