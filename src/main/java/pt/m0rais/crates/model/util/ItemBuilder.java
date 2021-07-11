package pt.m0rais.crates.model.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private final ItemStack item;

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    public ItemBuilder(Material material, int data) {
        this.item = new ItemStack(material, 1, (byte) data);
    }

    public ItemBuilder(Material material, int amount, int data) {
        this.item = new ItemStack(material, amount, (byte) data);
    }

    public ItemBuilder name(String name) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder hideFlags() {
        ItemMeta itemMeta = item.getItemMeta();
        for (ItemFlag itemFlag : itemMeta.getItemFlags()) {
            itemMeta.removeItemFlags(itemFlag);
        }
        item.setItemMeta(itemMeta);
        return this;
    }


    public ItemStack getItem() {
        return item;
    }

}