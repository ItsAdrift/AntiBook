package me.itsadrift.antibook;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AntiBookCommand implements CommandExecutor {

    private AntiBook main;
    public AntiBookCommand(AntiBook main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("antibook.reload")) {
                    main.reloadData();
                    sender.sendMessage(colour(main.prefix + main.configReloaded));
                } else {
                    sender.sendMessage(colour(main.prefix + main.noPermission));
                }
            } else {
                sender.sendMessage(colour(main.prefix + main.invalidArguments));
            }
        } else {
            sender.sendMessage(colour(main.prefix + main.invalidArguments));
        }

        return false;
    }

    private String colour(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
