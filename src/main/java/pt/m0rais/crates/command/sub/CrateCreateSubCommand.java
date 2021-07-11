package pt.m0rais.crates.command.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pt.m0rais.crates.command.SubCommand;
import pt.m0rais.crates.inventory.ConfirmationInventory;
import pt.m0rais.crates.manager.CrateManager;
import pt.m0rais.crates.model.enums.ConfirmationType;
import pt.m0rais.crates.util.MessageUtil;

public class CrateCreateSubCommand implements SubCommand {

    private final ConfirmationInventory confirmationInventory;

    public CrateCreateSubCommand(ConfirmationInventory confirmationInventory) {
        this.confirmationInventory = confirmationInventory;
    }

    @Override
    public void execute(CommandSender sender, String[] args, CrateManager crateManager) {
        if (isNotPlayer(sender) || wrongSyntax(sender, args)) return;

        String id = args[1];


        if (!crateManager.isNotKey(id)) {
            MessageUtil.sendMessage(sender, config.getStringList("messages.create.already-exists"));
            return;
        }

        confirmationInventory.initialize((Player) sender, id, ConfirmationType.CREATE);
    }

}