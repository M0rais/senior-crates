package pt.m0rais.crates.command.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pt.m0rais.crates.command.SubCommand;
import pt.m0rais.crates.inventory.ConfirmationInventory;
import pt.m0rais.crates.manager.CrateManager;
import pt.m0rais.crates.model.enums.ConfirmationType;

public class CrateDeleteSubCommand implements SubCommand {

    private final ConfirmationInventory confirmationInventory;

    public CrateDeleteSubCommand(ConfirmationInventory confirmationInventory) {
        this.confirmationInventory = confirmationInventory;
    }

    @Override
    public void execute(CommandSender sender, String[] args, CrateManager crateManager) {
        if (wrongSyntax(sender, args)) return;

        String id = args[1];

        if (isNotCrate(sender, id, crateManager)) return;

        confirmationInventory.initialize((Player) sender, id, ConfirmationType.DELETE);

    }

}