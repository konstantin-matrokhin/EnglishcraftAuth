package org.kvlt.englishcraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class EnglishcraftAuth extends JavaPlugin {

    private static final int CURRENT_VERSION = 1;
    private static final String PASSWORD_CONFIG_KEY = "password";
    private static final String ADMIN_PASSWORD_CONFIG_KEY = "admin-password";

    private static EnglishcraftAuth instance;

    private Map<String, AuthSession> sessions = new HashMap<>();
    private Set<Player> unauthedPlayers = new HashSet<>();
    private Random random = new Random();

    private String playerPassword;
    private String adminPassword;

    @Override
    public void onEnable() {
        $("Enabling EnglishcraftAuth plugin..");
        instance = this;
        int version = getConfig().getInt("version", 0);
        if (version != CURRENT_VERSION) {
            $("Config seems to be broken, resetting config file..");
            saveDefaultConfig();
            reloadConfig();
            onEnable();
            return;
        } else {
            playerPassword = getConfig().getString(PASSWORD_CONFIG_KEY);
            adminPassword = getConfig().getString(ADMIN_PASSWORD_CONFIG_KEY);

            if (playerPassword == null || playerPassword.isEmpty()) {
                $("Players password not found, generating random.");
                playerPassword = generatePassword();
                getConfig().set(PASSWORD_CONFIG_KEY, playerPassword);
            }
            if (adminPassword == null || adminPassword.isEmpty()) {
                $("Admin password not found, generating random.");
                adminPassword = generatePassword();
                getConfig().set(ADMIN_PASSWORD_CONFIG_KEY, adminPassword);
            }

            saveConfig();
            reloadConfig();
            $("Configuration loaded");
        }

        Bukkit.getPluginManager().registerEvents(new PlayerAuthEventListener(), this);
        Bukkit.getPluginCommand("setpassword").setExecutor(new PasswordCommand());

        if (Bukkit.getOnlinePlayers().size() > 0) {
            unauthedPlayers.addAll(Bukkit.getOnlinePlayers());
            Bukkit.getOnlinePlayers()
                    .parallelStream()
                    .forEach(p -> sendConfigMessage(p, "login-require"));
        }
    }

    public void $(String msg) {
        Bukkit.getLogger().info("[" + getName() + "] " + msg);
    }

    public void sendConfigMessage(Player player, String key) {
        player.sendMessage(ChatColor
                .translateAlternateColorCodes('&',
                        getConfig().getString("messages." + key)
                )
        );
    }

    public void changePlayerPassword(String newPass) {
        playerPassword = newPass;
        getConfig().set(PASSWORD_CONFIG_KEY, newPass);
        sessions.clear();
        saveConfig();
        reloadConfig();
    }

    public void changeAdminPassword(String newPass) {
        adminPassword = newPass;
        getConfig().set(ADMIN_PASSWORD_CONFIG_KEY, newPass);
        sessions.clear();
        saveConfig();
        reloadConfig();
    }

    private String generatePassword() {
        return String.valueOf(random.nextInt(99999) + 100);
    }

    public Map<String, AuthSession> getSessions() {
        return sessions;
    }

    public Set<Player> getUnauthedPlayers() {
        return unauthedPlayers;
    }

    public static EnglishcraftAuth get() {
        return instance;
    }

        protected String getPlayerPassword() {
        return playerPassword;
    }

    protected String getAdminPassword() {
        return adminPassword;
    }

}
