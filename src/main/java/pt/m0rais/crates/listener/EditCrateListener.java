package pt.m0rais.crates.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import pt.m0rais.crates.SeniorCrates;
import pt.m0rais.crates.inventory.EditCrateInventory;
import pt.m0rais.crates.manager.CrateManager;
import pt.m0rais.crates.manager.InventoryManager;
import pt.m0rais.crates.model.inventory.InventoryBuilder;

public class EditCrateListener implements Listener {

    private final CrateManager crateManager;
    private final InventoryManager inventoryManager;
    private final EditCrateInventory editCrateInventory;

    public EditCrateListener(SeniorCrates plugin) {
        this.inventoryManager = plugin.getInventoryManager();
        this.editCrateInventory = plugin.getEditCrateInventory();
        this.crateManager = plugin.getCrateManager();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        crateManager.editChance(event.getPlayer(), event.getMessage(), id -> {
            event.setCancelled(true);
            editCrateInventory.initialize(event.getPlayer(), id);
        });
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == event.getView().getTopInventory()) return;
        Player player = (Player) event.getWhoClicked();
        if (!inventoryManager.getInventories().containsKey(player.getUniqueId())) return;
        InventoryBuilder inventoryBuilder = inventoryManager.getInventories().get(player.getUniqueId());
        if (inventoryBuilder.getCrate() == null) return;

        ItemStack item = event.getCurrentItem().clone();
        if (item == null || item.getType() == Material.AIR) return;

        event.setCancelled(true);

        crateManager.addPrize(inventoryBuilder.getCrate(), item);
        editCrateInventory.initialize(player, inventoryBuilder.getCrate());
    }

}
