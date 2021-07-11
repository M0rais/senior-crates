package pt.m0rais.crates.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pt.m0rais.crates.SeniorCrates;
import pt.m0rais.crates.inventory.ConfirmationInventory;
import pt.m0rais.crates.manager.CrateManager;
import pt.m0rais.crates.model.enums.ConfirmationType;
import pt.m0rais.crates.util.NBTUtil;

public class CrateListener implements Listener {

    private final NBTUtil nbtUtil;
    private final ConfirmationInventory confirmationInventory;
    private final CrateManager crateManager;

    public CrateListener(SeniorCrates plugin) {
        this.nbtUtil = plugin.getNbtUtil();
        this.confirmationInventory = plugin.getConfirmationInventory();
        this.crateManager = plugin.getCrateManager();
    }

    @EventHandler
    public void onUseCrate(PlayerInteractEvent event) {
        switch (event.getAction()) {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                Player player = event.getPlayer();
                ItemStack item = player.getItemInHand();
                if (item == null || item.getType() == Material.AIR || !nbtUtil.hasKey(item, "crate")) return;

                String id = nbtUtil.getString(item, "crate");

                if (crateManager.isNotCrate(player, id)) return;

                confirmationInventory.initialize(player, id, ConfirmationType.OPEN);
                break;
        }
    }

}
