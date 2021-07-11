package pt.m0rais.crates.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import pt.m0rais.crates.manager.CrateManager;
import pt.m0rais.crates.model.enums.ConfirmationType;
import pt.m0rais.crates.model.inventory.InventoryBuilder;
import pt.m0rais.crates.model.inventory.InventoryItem;
import pt.m0rais.crates.model.util.ItemBuilder;

public class ConfirmationInventory {

    private final CrateManager crateManager;
    private final InventoryBuilder inventoryBuilder;

    public ConfirmationInventory(CrateManager crateManager) {
        this.crateManager = crateManager;
        this.inventoryBuilder = new InventoryBuilder()
                .rows(3)
                .name("Confirmation GUI");
    }

    public void initialize(Player player, String id, ConfirmationType confirmationType) {

        if (confirmationType != ConfirmationType.CREATE && crateManager.isNotCrate(player, id)) return;

        inventoryBuilder.appendItem(12, InventoryItem.of(new ItemBuilder(Material.WOOL, 5).name("§aConfirm"), event -> {
            event.setCancelled(true);
            crateManager.confirmation(player, id, false, confirmationType);
        }));
        inventoryBuilder.appendItem(14, InventoryItem.of(new ItemBuilder(Material.WOOL, 14).name("§cCancel"), event -> {
            event.setCancelled(true);
            crateManager.confirmation(player, id, true, confirmationType);
        }));
        inventoryBuilder.open(player);
    }


}
