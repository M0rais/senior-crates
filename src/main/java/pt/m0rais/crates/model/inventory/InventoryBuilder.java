package pt.m0rais.crates.model.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pt.m0rais.crates.SeniorCrates;
import pt.m0rais.crates.manager.InventoryManager;

import java.util.HashMap;
import java.util.Map;

public class InventoryBuilder {

    private final HashMap<Integer, InventoryItem> items;
    private Inventory inventory;
    private String name;
    private int size;
    private String crate = null;
    private final InventoryManager manager = SeniorCrates.getInstance().getInventoryManager();


    public InventoryBuilder() {
        items = new HashMap<>();
    }

    public InventoryBuilder name(String name) {
        this.name = name;
        return this;
    }

    public InventoryBuilder crate(String id) {
        this.crate = id;
        return this;
    }

    public InventoryBuilder size(int size) {
        this.size = size;
        return this;
    }

    public InventoryBuilder rows(int rows) {
        this.size = rows * 9;
        return this;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void appendItem(int slot, InventoryItem item) {
        items.put(slot, item);
    }

    public String getCrate() {
        return crate;
    }

    public InventoryItem get(int slot) {
        return items.getOrDefault(slot, null);
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public InventoryManager getManager() {
        return manager;
    }

    public Inventory build() {
        if (inventory == null) inventory = Bukkit.createInventory(null, size, name);
        inventory.clear();
        inventory.setContents(new ItemStack[0]);
        for (Map.Entry<Integer, InventoryItem> items : items.entrySet()) {
            inventory.setItem(items.getKey(), items.getValue().getItem());
        }
        return inventory;
    }

    public void open(Player player) {
        manager.open(player, this);
    }

}