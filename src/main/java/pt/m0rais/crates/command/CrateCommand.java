package pt.m0rais.crates.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import pt.m0rais.crates.SeniorCrates;
import pt.m0rais.crates.command.sub.CrateCreateSubCommand;
import pt.m0rais.crates.command.sub.CrateDeleteSubCommand;
import pt.m0rais.crates.command.sub.CrateEditSubCommand;
import pt.m0rais.crates.command.sub.CrateGiveSubCommand;
import pt.m0rais.crates.inventory.ConfirmationInventory;
import pt.m0rais.crates.manager.CrateManager;
import pt.m0rais.crates.model.util.Placeholder;
import pt.m0rais.crates.util.MessageUtil;

import java.util.Map;
import java.util.TreeMap;

public class CrateCommand implements CommandExecutor {

    private final FileConfiguration config;
    private final CrateManager crateManager;
    private final Map<String, SubCommand> subCommands = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public CrateCommand(SeniorCrates plugin) {
        this.crateManager = plugin.getCrateManager();
        this.config = plugin.getConfig();

        final ConfirmationInventory confirmationInventory = plugin.getConfirmationInventory();

        this.subCommands.put("create", new CrateCreateSubCommand(confirmationInventory));
        this.subCommands.put("delete", new CrateDeleteSubCommand(confirmationInventory));
        this.subCommands.put("edit", new CrateEditSubCommand(plugin.getEditCrateInventory()));
        this.subCommands.put("give", new CrateGiveSubCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 0) {
            MessageUtil.sendMessage(sender, config.getStringList("messages.crate.args0"));
            return false;
        }

        String command = args[0];
        if (!subCommands.containsKey(command)) {
            MessageUtil.sendMessage(sender, config.getStringList("messages.crate.sub-command-not-found"));
            return false;
        }

        if (!sender.hasPermission("crates.admin." + command)) {
            MessageUtil.sendMessage(sender, config.getStringList("messages.crate.need-perm"),
                    new Placeholder("%sub%", command));
            return false;
        }

        subCommands.get(command).execute(sender, args, crateManager);
        return false;
    }

}
