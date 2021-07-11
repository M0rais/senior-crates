package pt.m0rais.crates.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pt.m0rais.crates.command.SubCommand;
import pt.m0rais.crates.manager.CrateManager;
import pt.m0rais.crates.util.MessageUtil;

public class CrateGiveSubCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args, CrateManager crateManager) {
        //crate give <player> <crate> <amount>
        if (args.length < 4) {
            MessageUtil.sendMessage(sender, config.getStringList("messages.give.wrong-syntax"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            MessageUtil.sendMessage(sender, config.getStringList("messages.give.player-not-found"));
            return;
        }

        String id = args[2];

        if (isNotCrate(sender, id, crateManager)) return;

        try {
            int amount = Integer.parseInt(args[3]);
            if (amount < 0 || amount > 64) {
                MessageUtil.sendMessage(sender, config.getStringList("messages.give.invalid-amount"));
                return;
            }
            crateManager.giveCrate(sender, target, id, amount);
        } catch (NumberFormatException e) {
            MessageUtil.sendMessage(sender, config.getStringList("messages.give.invalid-amount"));
        }


    }

}