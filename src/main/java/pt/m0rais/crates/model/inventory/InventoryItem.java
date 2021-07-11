package pt.m0rais.crates.model.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import pt.m0rais.crates.model.util.ItemBuilder;

import java.util.function.Consumer;

public class InventoryItem {

    private final ItemStack item;
    private final Consumer<InventoryClickEvent> consumer;

    public InventoryItem(ItemStack item, Consumer<InventoryClickEvent> consumer) {
        this.item = item;
        this.consumer = consumer;
    }

    public InventoryItem(ItemBuilder builder, Consumer<InventoryClickEvent> consumer) {
        this.item = builder.getItem();
        this.consumer = consumer;
    }

    public static InventoryItem of(ItemBuilder builder, Consumer<InventoryClickEvent> consumer) {
        return new InventoryItem(builder, consumer);
    }

    public static InventoryItem of(ItemBuilder builder) {
        return new InventoryItem(builder, e -> {
        });
    }

    public static InventoryItem of(ItemStack item, Consumer<InventoryClickEvent> consumer) {
        return new InventoryItem(item, consumer);
    }

    public static InventoryItem of(ItemStack item) {
        return new InventoryItem(item, e -> {
        });
    }

    public void run(InventoryClickEvent e) {
        this.consumer.accept(e);
    }

    public ItemStack getItem() {
        return item;
    }

}
