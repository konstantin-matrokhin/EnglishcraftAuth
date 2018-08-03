package org.kvlt.englishcraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EnglishсraftAuth extends JavaPlugin {

    private Map<Player, AuthSession> sessions = new HashMap<>();
    private Set<Player> unauthedPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        int version = getConfig().getInt("1", 0);
        if (version == 0) {
            saveDefaultConfig();
            reloadConfig();
        }

        Bukkit.getPluginManager().registerEvents(new PlayerAuthEventListener(this), this);
    }

    @Override
    public void onDisable() {

    }

    public Map<Player, AuthSession> getSessions() {
        return sessions;
    }

    public Set<Player> getUnauthedPlayers() {
        return unauthedPlayers;
    }
}
