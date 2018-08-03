package org.kvlt.englishcraft;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerAuthEventListener implements Listener {

    private EnglishсraftAuth plugin;

    PlayerAuthEventListener(EnglishсraftAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AuthSession session = plugin.getSessions().get(player);

        plugin.getUnauthedPlayers().add(player);
        if (session != null) {
            String ip = player.getAddress().getHostName();
            if (ip.equals(session.getIp())) {
                if (session.getExpirationTime() > System.currentTimeMillis()) {
                    plugin.getUnauthedPlayers().remove(player);
                    player.sendMessage(
                            ChatColor.translateAlternateColorCodes('&',
                                    "TODO")
                    );
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (plugin.getUnauthedPlayers().contains(player)) {
            event.setCancelled(true);
        }
    }

}
