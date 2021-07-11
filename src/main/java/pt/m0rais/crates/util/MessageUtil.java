package pt.m0rais.crates.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pt.m0rais.crates.model.util.Placeholder;

import java.util.List;

public class MessageUtil {

    public static void sendMessage(CommandSender sender, List<String> message) {
        for (String s : message) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }

    public static void sendMessage(CommandSender sender, List<String> message, Placeholder... placeholders) {
        for (String s : message) {
            for (Placeholder placeholder : placeholders) {
                s = placeholder.replace(s);
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }


}