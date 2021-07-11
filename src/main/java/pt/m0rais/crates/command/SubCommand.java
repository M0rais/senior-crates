package pt.m0rais.crates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pt.m0rais.crates.SeniorCrates;
import pt.m0rais.crates.manager.CrateManager;
import pt.m0rais.crates.model.util.Placeholder;
import pt.m0rais.crates.util.MessageUtil;

public interface SubCommand {

    FileConfiguration config = SeniorCrates.getInstance().getConfig();

    void execute(CommandSender sender, String[] args, CrateManager crateManager);

    default boolean wrongSyntax(CommandSender sender, String[] args) {
        if (args.length < 2) {
            MessageUtil.sendMessage(sender, config.getStringList("messages.sub-wrong-syntax"),
                    new Placeholder("%sub%", args[0]));
            return true;
        }
        return false;
    }

    default boolean isNotCrate(CommandSender sender, String id, CrateManager crateManager) {
        return crateManager.isNotCrate(sender, id);
    }

    default boolean isNotPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageUtil.sendMessage(sender, config.getStringList("messages.only-players"));
            return true;
        }
        return false;
    }

}
