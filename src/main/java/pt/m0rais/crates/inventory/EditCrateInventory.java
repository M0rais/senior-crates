package pt.m0rais.crates.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import pt.m0rais.crates.SeniorCrates;
import pt.m0rais.crates.manager.CrateManager;
import pt.m0rais.crates.model.crate.Crate;
import pt.m0rais.crates.model.crate.Prize;
import pt.m0rais.crates.model.inventory.InventoryBuilder;
import pt.m0rais.crates.model.inventory.InventoryItem;
import pt.m0rais.crates.model.util.ItemBuilder;

public class EditCrateInventory {

    private final CrateManager crateManager;
    private final int rows;


    public EditCrateInventory(SeniorCrates plugin) {
        this.crateManager = plugin.getCrateManager();
        rows = Math.min(6, (int) Math.ceil(plugin.getConfig().getDouble("items") / 9));
    }

    public void initialize(Player player, String id) {
        player.closeInventory();
        InventoryBuilder inventoryBuilder = new InventoryBuilder().rows(rows).crate(id).name("Crate: " + id);
        int i = 0;
        for (Prize prize : crateManager.getCrate(id).getPrizes()) {
            ItemStack prizeItem = prize.getItem().clone();
            String[] lore = {"", "§eChance: §b" + prize.getChance() + "%", "",
                    "§eRIGHT CLICK §7to §eREMOVE", "§eSHIFT CLICK §7to §eSET CHANCE"};
            inventoryBuilder.appendItem(i, InventoryItem.of(new ItemBuilder(prizeItem)
                    .name("§a" + prize.getStringID())
                    .lore(lore).getItem(), event -> {
                event.setCancelled(true);
                ClickType clickType = event.getClick();

                if (clickType.isShiftClick()) {
                    crateManager.addPrizeEdit(player, prize);
                    player.closeInventory();
                    return;
                }

                if (!clickType.isRightClick()) return;
                crateManager.removePrize(player, id, prize);
                initialize(player, id);
            }));
            i++;
        }

        inventoryBuilder.open(player);
    }

}
