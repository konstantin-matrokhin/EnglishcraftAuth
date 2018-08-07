package org.kvlt.englishcraft;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PasswordCommand implements CommandExecutor {

    private EnglishcraftAuth plugin = EnglishcraftAuth.get();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1:
                if (args[0].equals("reload")) {
                    plugin.reloadConfig();
                    sender.sendMessage(ChatColor.GREEN + "Config was reloaded from config");
                    return  true;
                }
                break;
            case 2:
                String oldPass = args[0];
                String newPass = args[1];

                if (newPass.length() < 4 || newPass.length() > 16) {
                    sender.sendMessage(ChatColor.RED + "Password length must be 4-16 characters");
                    return true;
                }

                if (oldPass.equals(plugin.getPlayerPassword())) {
                    plugin.changePlayerPassword(newPass);
                } else if (oldPass.equals(plugin.getAdminPassword())) {
                    plugin.changeAdminPassword(newPass);
                } else {
                    plugin.$(String.format("Player %s attempted to change password (%s, %s)",
                            sender.getName(),
                            oldPass,
                            newPass));
                    sender.sendMessage(ChatColor.RED + "Incorrect old password!");
                }
                return true;
        }
        return false;
    }
}
