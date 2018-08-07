package org.kvlt.englishcraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;

import java.util.concurrent.TimeUnit;

public class PlayerAuthEventListener implements Listener {

    private EnglishcraftAuth plugin = EnglishcraftAuth.get();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AuthSession session = plugin.getSessions().get(player.getName());

        plugin.getUnauthedPlayers().add(player);
        if (session != null) {
            plugin.$("session not null");
            String ip = player.getAddress().getHostName();
            if (ip.equals(session.getIp())) {
                plugin.$("correct ip");
                if (session.getExpirationTime() > System.currentTimeMillis()) {
                    plugin.$("session success");
                    authorize(player);
                    return;
                } else {
                    plugin.getSessions().remove(player.getName());
                }
            }
        }
        plugin.sendConfigMessage(player, "login-require");
    }

    private void authorize(Player player) {
        AuthSession session = new AuthSession(player.getAddress().getHostName(),
                System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3));
        plugin.getUnauthedPlayers().remove(player);
        plugin.getSessions().put(player.getName(), session);
        plugin.sendConfigMessage(player, "successful-login");
        plugin.$("authed!");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();

        if (plugin.getUnauthedPlayers().contains(player)) {
            if (player.hasPermission("englishcraft.admin")) {
                if (msg.equals(plugin.getAdminPassword())) {
                    authorize(player);
                } else {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.kickPlayer("Incorrect password!");
                    });
                }
            } else {
                if (msg.equals(plugin.getPlayerPassword())) {
                    authorize(player);
                } else {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.kickPlayer("Incorrect password!");
                    });
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        checkAuth(event.getPlayer(), event);
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        checkAuth(event.getPlayer(), event);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        checkAuth(event.getPlayer(), event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        checkAuth(event.getPlayer(), event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        checkAuth(event.getPlayer(), event);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        checkAuth(event.getPlayer(), event);
    }

    private void checkAuth(Player player, Event event) {
        if (plugin.getUnauthedPlayers().contains(player)) {
            if (event instanceof Cancellable) {
                ((Cancellable) event).setCancelled(true);
            }
        }
    }

}
