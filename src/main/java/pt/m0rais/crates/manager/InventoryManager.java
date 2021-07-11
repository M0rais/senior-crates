package pt.m0rais.crates.manager;

import org.bukkit.entity.Player;
import pt.m0rais.crates.SeniorCrates;
import pt.m0rais.crates.listener.InventoryListener;
import pt.m0rais.crates.model.inventory.InventoryBuilder;

import java.util.UUID;
import java.util.WeakHashMap;

public class InventoryManager {

    private final WeakHashMap<UUID, InventoryBuilder> inventories;

    public InventoryManager(SeniorCrates plugin) {
        this.inventories = new WeakHashMap<>();
        plugin.getServer().getPluginManager().registerEvents(new InventoryListener(this), plugin);
    }


    public void open(Player player, InventoryBuilder builder) {
        inventories.remove(player.getUniqueId());
        inventories.put(player.getUniqueId(), builder);
        player.openInventory(builder.build());
    }

    public void close(Player p) {
        if (!inventories.containsKey(p.getUniqueId()))
            return;
        inventories.remove(p.getUniqueId());
    }

    public boolean hasInventoryBuilder(Player player, InventoryBuilder builder) {
        UUID uuid = player.getUniqueId();
        if (!inventories.containsKey(uuid)) return false;
        return inventories.get(uuid) == builder;
    }

    public WeakHashMap<UUID, InventoryBuilder> getInventories() {
        return inventories;
    }

}
