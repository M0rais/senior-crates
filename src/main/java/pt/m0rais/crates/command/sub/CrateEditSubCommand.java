package pt.m0rais.crates.command.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pt.m0rais.crates.command.SubCommand;
import pt.m0rais.crates.inventory.EditCrateInventory;
import pt.m0rais.crates.manager.CrateManager;

public class CrateEditSubCommand implements SubCommand {

    private final EditCrateInventory editCrateInventory;

    public CrateEditSubCommand(EditCrateInventory editCrateInventory) {
        this.editCrateInventory = editCrateInventory;
    }

    @Override
    public void execute(CommandSender sender, String[] args, CrateManager crateManager) {
        if (isNotPlayer(sender) || wrongSyntax(sender, args)) return;

        String id = args[1];

        if (isNotCrate(sender, id, crateManager)) return;

        editCrateInventory.initialize((Player) sender, id);

    }

}