package pt.m0rais.crates.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import pt.m0rais.crates.manager.InventoryManager;
import pt.m0rais.crates.model.inventory.AnimationInventory;
import pt.m0rais.crates.model.inventory.InventoryBuilder;
import pt.m0rais.crates.model.inventory.InventoryItem;

public class InventoryListener implements Listener {

    private final InventoryManager inventoryManager;

    public InventoryListener(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();

        Inventory inventory = event.getClickedInventory();

        if (!inventoryManager.getInventories().containsKey(p.getUniqueId())) {
            if (inventory instanceof AnimationInventory) {
                ((AnimationInventory) inventory.getHolder()).getConsumer().accept(event);
            }
            return;
        }

        if (inventory == p.getOpenInventory().getTopInventory()) {
            event.setCancelled(true);
            InventoryBuilder builder = inventoryManager.getInventories().get(p.getUniqueId());
            if (event.getRawSlot() > builder.getSize())
                return;
            InventoryItem item = builder.get(event.getRawSlot());
            if (item != null)
                item.run(event);
        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        inventoryManager.close((Player) event.getPlayer());
    }

}